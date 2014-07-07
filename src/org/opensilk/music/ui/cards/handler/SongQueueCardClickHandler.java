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

package org.opensilk.music.ui.cards.handler;

import android.support.v4.app.FragmentActivity;

import org.opensilk.silkdagger.qualifier.ForActivity;

import javax.inject.Inject;

/**
 * Created by drew on 7/1/14.
 */
public class SongQueueCardClickHandler {
    private final FragmentActivity activity;

    @Inject
    public SongQueueCardClickHandler(@ForActivity FragmentActivity activity) {
        this.activity = activity;
    }

    public FragmentActivity getActivity() {
        return activity;
    }
}