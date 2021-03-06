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

package org.opensilk.music.playback.service;

import android.os.PowerManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by drew on 5/6/15.
 */
@Module
public class PlaybackServiceModule {
    final PlaybackServiceProxy mService;

    private PlaybackServiceModule(PlaybackServiceProxy mService) {
        this.mService = mService;
    }

    public static PlaybackServiceModule create(PlaybackServiceProxy service) {
        return new PlaybackServiceModule(service);
    }

    @Provides @PlaybackServiceScope
    public PlaybackServiceProxy providePlaybackService() {
        return mService;
    }

    @Provides @PlaybackServiceScope
    public PowerManager.WakeLock provideWakeLock(PowerManager powerManager) {
        PowerManager.WakeLock w = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, PlaybackService.NAME);
        w.setReferenceCounted(false);
        return w;
    }

}
