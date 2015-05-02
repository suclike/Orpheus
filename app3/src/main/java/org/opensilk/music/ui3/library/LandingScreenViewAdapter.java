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

package org.opensilk.music.ui3.library;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.opensilk.common.ui.recycler.RecyclerListAdapter;

import javax.inject.Inject;

/**
 * Created by drew on 5/1/15.
 */
public class LandingScreenViewAdapter extends
        RecyclerListAdapter<LandingScreenViewAdapter.ViewItem, LandingScreenViewAdapter.ViewHolder> {

    final LandingScreenPresenter presenter;

    @Inject
    public LandingScreenViewAdapter(LandingScreenPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflate(parent, android.R.layout.simple_list_item_1));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ((TextView) holder.itemView).setText(getItem(position).text);
    }

    public static class ViewItem {
        public static final ViewItem ALBUMS = new ViewItem("Albums");
        public static final ViewItem ARTISTS = new ViewItem("Artists");
        public static final ViewItem FOLDERS = new ViewItem("Folders");
        public static final ViewItem TRACKS = new ViewItem("Tracks");
        final String text;

        public ViewItem(String text) {
            this.text = text;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
