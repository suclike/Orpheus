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

package org.opensilk.cast;

import org.opensilk.cast.CastRouteListener;
import org.opensilk.cast.CastManagerCallback;

/**
 * Created by drew on 2/19/14.
 */
interface ICastManager {
    void changeVolume(double increment);
    int getReconnectionStatus();
    void setReconnectionStatus(int status);
    CastRouteListener getRouteListener();
    void registerListener(CastManagerCallback cb);
    void unregisterListener(CastManagerCallback cb);
}