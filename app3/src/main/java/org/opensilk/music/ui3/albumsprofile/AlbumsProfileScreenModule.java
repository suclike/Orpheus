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

package org.opensilk.music.ui3.albumsprofile;

import android.content.Context;
import android.net.Uri;

import org.opensilk.common.core.dagger2.ScreenScope;
import org.opensilk.music.AppPreferences;
import org.opensilk.music.library.provider.LibraryUris;
import org.opensilk.music.library.sort.TrackSortOrder;
import org.opensilk.music.model.spi.Bundleable;
import org.opensilk.music.ui3.common.BundleablePresenter;
import org.opensilk.music.ui3.common.ItemClickListener;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by drew on 5/5/15.
 */
@Module
public class AlbumsProfileScreenModule {
    final AlbumsProfileScreen screen;

    public AlbumsProfileScreenModule(AlbumsProfileScreen screen) {
        this.screen = screen;
    }

    @Provides @Named("loader_uri")
    public Uri provideLoaderUri() {
        return LibraryUris.albumTracks(screen.libraryConfig.authority, screen.libraryInfo.libraryId, screen.libraryInfo.folderId);
    }

    @Provides @Named("loader_sortorder")
    public String provideLoaderSortOrder(AppPreferences preferences) {//TODO proper sort orders
        return preferences.getString(preferences.makePluginPrefKey(screen.libraryConfig, AppPreferences.ALBUM_TRACK_SORT_ORDER), TrackSortOrder.A_Z);
    }

    @Provides @Named("presenter_wantGrid")
    public Boolean provideWantGrid() {
        return false;
    }

    @Provides @ScreenScope
    public ItemClickListener provideItemClickListener() {
        return new ItemClickListener() {
            @Override
            public void onItemClicked(BundleablePresenter presenter, Context context, Bundleable item) {
                //TODO
            }
        };
    }
}