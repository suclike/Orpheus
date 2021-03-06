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

package org.opensilk.music.index.provider;

/**
 * Created by drew on 9/19/15.
 */
public interface Methods {
    String IS_INDEXED = "idx.is_indexed";
    String ADD = "idx.add";
    String REMOVE = "idx.remove";
    String LAST_QUEUE_LIST = "idx.lastqueue.list";
    String LAST_QUEUE_POSITION = "idx.lastqueue.position";
    String LAST_QUEUE_REPEAT = "idx.lastqueue.repeat";
    String LAST_QUEUE_SHUFFLE = "idx.lastqueu.shuffle";
    String SAVE_QUEUE_LIST = "idx.savequeue.list";
    String SAVE_QUEUE_POSITION = "idx.savequeue.position";
    String SAVE_QUEUE_REPEAT = "idx.savequeue.repeat";
    String SAVE_QUEUE_SHUFFLE = "idx.savequeue.shuffle";
    String LAST_SEEK_POSITION = "idx.lastseekposition";
    String SAVE_SEEK_POSITION = "idx.saveseekposition";
    String GET_BROADCAST_META = "idx.get.broadcastmeta";
    String SAVE_BROADCAST_META = "idx.save.broadcastmeta";
    String GET_USE_MEDIASTYLE_NOTIF = "idx.get.usemediastylenotif";
    String SAVE_USE_MEDIASTYLE_NOTIF = "idx.save.usemediastylenotif";
    String GET_TRACK = "idx.get.track";
    String GET_TRACK_LIST = "idx.get.track.list";
}
