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

package org.opensilk.music.ui.cards;

import android.content.Context;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.andrew.apollo.R;
import com.andrew.apollo.model.LocalArtist;
import com.andrew.apollo.utils.MusicUtils;
import com.squareup.otto.Bus;

import org.opensilk.music.api.meta.ArtInfo;
import org.opensilk.music.api.model.Artist;
import org.opensilk.music.artwork.ArtworkImageView;
import org.opensilk.music.artwork.ArtworkManager;
import org.opensilk.music.ui.cards.event.ArtistCardClick;
import org.opensilk.music.ui.cards.event.CardEvent;
import org.opensilk.silkdagger.qualifier.ForFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by drew on 6/19/14.
 */
public class ArtistCard extends AbsBundleableCard<Artist> {

    @Inject @ForFragment
    Bus mBus; //Injected by adapter

    @InjectView(R.id.artwork_thumb)
    protected ArtworkImageView mArtwork;
    // no inject
    protected View mDescOverlay;

    public ArtistCard(Context context, Artist data) {
        super(context, data, R.layout.listcard_artwork_inner);
    }

    @Override
    protected void init() {
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                mBus.post(new ArtistCardClick(CardEvent.OPEN, mData));
            }
        });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        if (isGridStyle()) {
            mDescOverlay = ButterKnife.findById(view, R.id.griditem_desc_overlay);
        }
        super.setupInnerViewElements(parent, view);
    }

    @Override
    protected void onInnerViewSetup() {
        mCardTitle.setText(mData.name);
        String subTitle = "";
        if (mData.albumCount > 0) {
            subTitle = MusicUtils.makeLabel(getContext(), R.plurals.Nalbums, mData.albumCount);
        }
        if (mData.songCount > 0) {
            if (!TextUtils.isEmpty(subTitle)) subTitle += ", ";
            subTitle += MusicUtils.makeLabel(getContext(), R.plurals.Nsongs, mData.songCount);
        }
        if (!TextUtils.isEmpty(subTitle)) {
            mCardSubTitle.setText(subTitle);
            mCardSubTitle.setVisibility(View.VISIBLE);
        } else {
            mCardSubTitle.setVisibility(View.GONE);
        }
        if (isGridStyle()) {
            mArtwork.setPaletteListener(GridOverlayHelper.create(getContext(), mDescOverlay));
        }
        ArtworkManager.loadImage(new ArtInfo(mData.name, null, null), mArtwork);
    }

    @Override
    protected void cleanupViews() {
        mArtwork.setPaletteListener(null);
        super.cleanupViews();
    }

    @Override
    protected void onCreatePopupMenu(PopupMenu m) {
        m.inflate(R.menu.popup_play_all);
        m.inflate(R.menu.popup_shuffle_all);
        m.inflate(R.menu.popup_add_to_queue);
        if (mData instanceof LocalArtist) {
            m.inflate(R.menu.popup_add_to_playlist);
            m.inflate(R.menu.popup_delete);
        }
        m.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                try {
                    CardEvent event = CardEvent.valueOf(item.getItemId());
                    mBus.post(new ArtistCardClick(event, mData));
                    return true;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    @Override
    protected int getListLayout() {
        return R.layout.listcard_artwork_inner;
    }

    @Override
    protected int getGridLayout() {
        return R.layout.gridcard_artwork_inner;
    }
}
