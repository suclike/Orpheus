/*
 * Copyright (C) 2014 OpenSilk Productions LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opensilk.music.ui.cards.old;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.andrew.apollo.R;
import com.andrew.apollo.menu.DeleteDialog;
import com.andrew.apollo.model.Song;
import com.andrew.apollo.utils.MusicUtils;
import com.andrew.apollo.utils.NavUtils;

import org.opensilk.music.artwork.ArtworkImageView;
import org.opensilk.music.artwork.ArtworkManager;
import org.opensilk.music.dialogs.AddToPlaylistDialog;
import org.opensilk.music.ui.cards.old.CardBaseListNoHeader;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by drew on 2/11/14.
 */
public class CardSongList extends CardBaseListNoHeader<Song> {

    private boolean mAllowDelete = true;

    public CardSongList(Context context, Song data) {
        super(context, data);
    }

    public CardSongList(Context context, Song data, boolean allowDelete) {
        super(context, data);
        mAllowDelete = allowDelete;
    }

    public CardSongList(Context context, Song data, int innerLayout) {
        super(context, data, innerLayout);
    }

    @Override
    protected void initContent() {
        mTitle = mData.mSongName;
        mSubTitle = mData.mArtistName;
        mExtraText = MusicUtils.makeTimeString(getContext(),mData.mDuration);
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                MusicUtils.playAll(getContext(), new long[]{
                        mData.mSongId
                }, 0, false);
            }
        });
    }

    @Override
    protected void initThumbnail() {
        if (mData.mAlbumId > 0) {
            super.initThumbnail();
        }
    }

    @Override
    protected void loadThumbnail(ArtworkImageView view) {
        ArtworkManager.loadAlbumImage(mData.mArtistName, mData.mAlbumName, null, view);
    }

    @Override
    public int getOverflowMenuId() {
        int menuRes = mAllowDelete ? R.menu.card_song : R.menu.card_song_no_delete;
        return menuRes;
    }

    @Override
    public PopupMenu.OnMenuItemClickListener getOverflowPopupMenuListener() {
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.card_menu_play_next:
                        MusicUtils.playNext(new long[] {
                                mData.mSongId
                        });
                        break;
                    case R.id.card_menu_add_queue:
                        MusicUtils.addToQueue(getContext(), new long[] {
                                mData.mSongId
                        });
                        break;
                    case R.id.card_menu_add_playlist:
                        AddToPlaylistDialog.newInstance(new long[]{
                                mData.mSongId
                        }).show(((FragmentActivity) getContext()).getSupportFragmentManager(), "AddToPlaylistDialog");
                        break;
                    case R.id.card_menu_more_by:
                        NavUtils.openArtistProfile(getContext(), MusicUtils.makeArtist(getContext(), mData.mArtistName));
                        break;
                    case R.id.card_menu_set_ringtone:
                        MusicUtils.setRingtone(getContext(), mData.mSongId);
                        break;
                    case R.id.card_menu_delete:
                        final String song = mData.mSongName;
                        DeleteDialog.newInstance(song, new long[]{
                                mData.mSongId
                        }, null).show(((FragmentActivity) getContext()).getSupportFragmentManager(), "DeleteDialog");
                        break;
                }
                return false;
            }
        };
    }
}