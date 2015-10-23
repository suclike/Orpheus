/*
 * Copyright (c) 2015 OpenSilk Productions LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.opensilk.music.library.mediastore.provider;

import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;

import org.apache.commons.lang3.StringUtils;
import org.opensilk.common.core.dagger2.AppContextComponent;
import org.opensilk.common.core.mortar.DaggerService;
import org.opensilk.music.library.LibraryConfig;
import org.opensilk.music.library.internal.LibraryException;
import org.opensilk.music.library.mediastore.R;
import org.opensilk.music.library.mediastore.util.CursorHelpers;
import org.opensilk.music.library.mediastore.util.FilesHelper;
import org.opensilk.music.library.mediastore.util.StorageLookup;
import org.opensilk.music.library.provider.LibraryExtras;
import org.opensilk.music.library.provider.LibraryProvider;
import org.opensilk.music.model.Container;
import org.opensilk.music.model.Folder;
import org.opensilk.music.model.Model;
import org.opensilk.music.model.Track;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by drew on 5/17/15.
 */
public class FoldersLibraryProvider extends LibraryProvider {

    @Inject @Named("foldersLibraryAuthority") String mAuthority;
    @Inject StorageLookup mStorageLookup;

    UriMatcher mUriMatcher;

    @Override
    public boolean onCreate() {
        final AppContextComponent acc = DaggerService.getDaggerComponent(getContext());
        FoldersLibraryComponent.FACTORY.call(acc).inject(this);
        super.onCreate();
        mUriMatcher = FoldersUris.makeMatcher(mAuthority);
        return true;
    }

    @Override
    protected LibraryConfig getLibraryConfig() {
        return LibraryConfig.builder()
                .setAuthority(mAuthority)
                .setLabel(getContext().getString(R.string.folders_library_label))
                .build();
    }

    @Override
    protected String getAuthority() {
        return mAuthority;
    }

    @Override
    protected void listObjs(Uri uri, Subscriber<? super Model> subscriber, Bundle args) {
        switch (mUriMatcher.match(uri)) {
            case FoldersUris.M_FOLDERS: {
                browseFolders(uri.getPathSegments().get(0), null, subscriber, args);
                break;
            }
            case FoldersUris.M_FOLDER: {
                browseFolders(uri.getPathSegments().get(0), uri.getLastPathSegment(), subscriber, args);
                break;
            }
            default:
                Timber.w("Unmatched uri %s", uri);
                subscriber.onError(new LibraryException(LibraryException.Kind.ILLEGAL_URI,
                        new IllegalArgumentException("Invalid uri " + uri)));
        }
    }

    @Override
    protected void getObj(Uri uri, Subscriber<? super Model> subscriber, Bundle args) {
        switch (mUriMatcher.match(uri)) {
            case FoldersUris.M_FOLDERS: {
                getRoot(uri.getPathSegments().get(0), subscriber, args);
                break;
            }
            case FoldersUris.M_FOLDER: {
                getFolder(uri.getPathSegments().get(0), uri.getLastPathSegment(), subscriber, args);
                break;
            }
            case FoldersUris.M_TRACK_MS:
            case FoldersUris.M_TRACK_PTH: {
                getTrack(uri.getPathSegments().get(0), uri.getLastPathSegment(), subscriber, args);
                break;
            }
            default: {
                Timber.w("Unmatched uri %s", uri);
                subscriber.onError(new LibraryException(LibraryException.Kind.ILLEGAL_URI,
                        new IllegalArgumentException("Invalid uri " + uri)));
            }
        }
    }

    @Override
    protected void listRoots(Uri uri, Subscriber<? super Container> subscriber, Bundle args) {
        List<StorageLookup.StorageVolume> volumes = mStorageLookup.getStorageVolumes();
        if (volumes == null || volumes.size() == 0) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(new Exception("No storage volumes found"));
            }
        } else {
            for (StorageLookup.StorageVolume v : volumes) {
                Folder f = FilesHelper.makeRoot(mAuthority, v);
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                subscriber.onNext(f);
            }
            subscriber.onCompleted();
        }
    }

    void browseFolders(String library, String identity, Subscriber<? super Model> subscriber, Bundle args) {
        final StorageLookup.StorageVolume volume;
        final File rootDir;
        try {
            volume = mStorageLookup.getStorageVolume(library);
            final File base = new File(volume.path);
            rootDir = StringUtils.isEmpty(identity) ? base : new File(base, identity);
            if (!rootDir.exists() || !rootDir.isDirectory() || !rootDir.canRead()) {
                Timber.e("Can't access path %s", rootDir.getPath());
                throw new IllegalArgumentException("Can't access path " + rootDir.getPath());
            }
        } catch (IllegalArgumentException e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
            }
            return;
        }

        final boolean isScan = args.getBoolean("fldr.isscan", false);

        File[] dirList = rootDir.listFiles();
        List<File> files = new ArrayList<>(dirList.length);
        for (File f : dirList) {
            if (!f.canRead()) {
                continue;
            }
            if (f.getName().startsWith(".")) {
                continue;
            }
            if (f.isDirectory()) {
                subscriber.onNext(FilesHelper.makeFolder(mAuthority, volume, f));
            } else if (f.isFile()) {
                files.add(f);
            }
        }
        //Save ourselves the trouble
        if (subscriber.isUnsubscribed()) {
            return;
        }
        // convert raw file list into something useful
        List<File> audioFiles = FilesHelper.filterAudioFiles(getContext(), files);
        List<Track> tracks;
        tracks = FilesHelper.convertAudioFilesToTracks(getContext(),
                    mAuthority, volume, audioFiles);
        if (subscriber.isUnsubscribed()) {
            return;
        }
        for (Track track : tracks) {
            subscriber.onNext(track);
        }
        subscriber.onCompleted();
    }

    protected void getRoot(String library, Subscriber<? super Model> subscriber, Bundle args) {
        final StorageLookup.StorageVolume volume;
        try {
            volume = mStorageLookup.getStorageVolume(library);
            final File rootDir = new File(volume.path);
            if (!rootDir.exists() || !rootDir.isDirectory() || !rootDir.canRead()) {
                Timber.e("Can't access path %s", rootDir.getPath());
                throw new IllegalArgumentException("Can't access path " + rootDir.getPath());
            }
        } catch (IllegalArgumentException e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
            }
            return;
        }
        if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(FilesHelper.makeRoot(mAuthority, volume));
            subscriber.onCompleted();
        }
    }

    protected void getFolder(String library, String identity, Subscriber<? super Model> subscriber, Bundle args) {
        final StorageLookup.StorageVolume volume;
        final File rootDir;
        try {
            volume = mStorageLookup.getStorageVolume(library);
            final File base = new File(volume.path);
            rootDir = new File(base, identity);
            if (!rootDir.exists() || !rootDir.isDirectory() || !rootDir.canRead()) {
                Timber.e("Can't access path %s", rootDir.getPath());
                throw new IllegalArgumentException("Can't access path " + rootDir.getPath());
            }
        } catch (IllegalArgumentException e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
            }
            return;
        }
        if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(FilesHelper.makeFolder(mAuthority, volume, rootDir));
            subscriber.onCompleted();
        }
    }

    protected void getTrack(String library, String identity, Subscriber<? super Track> subscriber, Bundle args) {
        final StorageLookup.StorageVolume volume;
        try {
            volume = mStorageLookup.getStorageVolume(library);
            final File rootDir = new File(volume.path);
            if (!rootDir.exists() || !rootDir.isDirectory() || !rootDir.canRead()) {
                Timber.e("Can't access path %s", rootDir.getPath());
                throw new IllegalArgumentException("Can't access path " + rootDir.getPath());
            }
        } catch (IllegalArgumentException e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
            }
            return;
        }
        if (StringUtils.isNumeric(identity)) {
            Track track = FilesHelper.findTrack(getContext(), mAuthority, volume, identity);
            if (!subscriber.isUnsubscribed()) {
                if (track == null) {
                    subscriber.onError(new IllegalArgumentException("Unable to find track id=" + identity));
                } else {
                    subscriber.onNext(track);
                    subscriber.onCompleted();
                }
            }
        } else {
            final File f = new File(volume.path, identity);
            if(!subscriber.isUnsubscribed()) {
                if (!f.exists() || !f.isFile() || !f.canRead()) {
                    subscriber.onError(new IllegalArgumentException("Can't access file " + f.getPath()));
                } else {
                    subscriber.onNext(FilesHelper.makeTrackFromFile(mAuthority, volume, f));
                    subscriber.onCompleted();
                }
            }
        }
    }

    @Override
    protected void deleteObj(Uri uri, Subscriber<? super Uri> subscriber, Bundle args) {
        final File base;
        try {
            base = mStorageLookup.getStorageFile(uri.getPathSegments().get(0));
            if (!base.exists() || !base.isDirectory() || !base.canRead()) {
                Timber.e("Can't access path %s", base.getPath());
                throw new IllegalArgumentException("Can't access path " + base.getPath());
            }
        } catch (IllegalArgumentException e) {
            subscriber.onError(e);
            return;
        }
//        boolean success = FilesHelper.deleteDirectory(getContext(), new File(base, identity));
//        if (!subscriber.isUnsubscribed()) {
//            subscriber.onNext(success);
//            subscriber.onCompleted();
//        }
        subscriber.onError(new UnsupportedOperationException());
    }

    protected void deleteTracks(String library, Subscriber<? super Boolean> subscriber, Bundle args) {
        List<Uri> uris = LibraryExtras.getUriList(args);
        List<Long> ids = new ArrayList<>(uris.size());
        List<String> names = new ArrayList<>(uris.size());
        for (Uri uri : uris) {
            String id = uri.getLastPathSegment();
            if (StringUtils.isNumeric(id)) {
                ids.add(Long.parseLong(id));
            } else {
                names.add(id);
            }
        }
        int numremoved = 0;
        if (!ids.isEmpty()) {
            numremoved += CursorHelpers.deleteTracks(getContext(), ids);
        }
        if (!names.isEmpty()) {
            final File base;
            try {
                base = mStorageLookup.getStorageFile(library);
                if (!base.exists() || !base.isDirectory() || !base.canRead()) {
                    Timber.e("Can't access path %s", base.getPath());
                    throw new IllegalArgumentException("Can't access path " + base.getPath());
                }
            } catch (IllegalArgumentException e) {
                subscriber.onError(e);
                return;
            }
            numremoved += FilesHelper.deleteFiles(base, names);
        }
        if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(numremoved == uris.size());
            subscriber.onCompleted();
        }
    }
}
