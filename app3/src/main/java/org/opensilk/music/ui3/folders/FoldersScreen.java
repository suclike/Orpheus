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

package org.opensilk.music.ui3.folders;

import android.content.res.Resources;

import org.opensilk.common.core.mortar.DaggerService;
import org.opensilk.common.ui.mortar.ComponentFactory;
import org.opensilk.common.ui.mortar.WithComponentFactory;
import org.opensilk.common.ui.mortarfragment.Layout;
import org.opensilk.music.R;
import org.opensilk.music.library.LibraryConfig;
import org.opensilk.music.library.LibraryInfo;
import org.opensilk.music.ui3.MusicActivityComponent;

import mortar.MortarScope;

/**
 * Created by drew on 5/2/15.
 */
@Layout(R.layout.bundleable_recycler)
@WithComponentFactory(FoldersScreen.Factory.class)
public class FoldersScreen {

    final LibraryConfig libraryConfig;
    final LibraryInfo libraryInfo;

    public FoldersScreen(LibraryConfig libraryConfig, LibraryInfo libraryInfo) {
        this.libraryConfig = libraryConfig;
        this.libraryInfo = libraryInfo;
    }

    @Override
    public String toString() {
        return libraryInfo.folderId;
    }

    public static class Factory extends ComponentFactory<FoldersScreen> {
        @Override
        protected Object createDaggerComponent(Resources resources, MortarScope parentScope, FoldersScreen screen) {
            return FoldersScreenComponent.FACTORY.call(DaggerService.<MusicActivityComponent>getDaggerComponent(parentScope), screen);
        }
    }
}