/*
 * Copyright (c) 2014 OpenSilk Productions LLC
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

package org.opensilk.music.ui2.gallery;

import android.os.Parcel;

import org.opensilk.common.flow.Screen;
import org.opensilk.common.mortar.WithModule;
import org.opensilk.music.R;

import flow.Layout;

/**
 * Created by drew on 10/3/14.
 */
@Layout(R.layout.gallery_page)
@WithModule(AlbumsScreenModule.class)
@GalleryPageTitle(R.string.page_albums)
public class AlbumsScreen extends Screen {

    public static final Creator<AlbumsScreen> CREATOR = new Creator<AlbumsScreen>() {
        @Override
        public AlbumsScreen createFromParcel(Parcel source) {
            AlbumsScreen s = new AlbumsScreen();
            s.restoreFromParcel(source);
            return s;
        }

        @Override
        public AlbumsScreen[] newArray(int size) {
            return new AlbumsScreen[size];
        }
    };

}
