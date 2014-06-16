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

package com.andrew.apollo;

import android.content.Context;

import org.opensilk.silkdagger.qualifier.ForApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by drew on 6/16/14.
 */
@Module (
        library = true
)
public class ApolloModule {

    private final ApolloApplication app;

    public ApolloModule(ApolloApplication app) {
        this.app = app;
    }

    @Provides @Singleton @ForApplication
    public Context provideAppContext() {
        return app.getApplicationContext();
    }

}