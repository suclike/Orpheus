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

package org.opensilk.music.ui2.theme;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import org.opensilk.music.R;

import org.opensilk.music.ui2.theme.widget.CompatSeekBar;

/**
 * Created by drew on 10/12/14.
 */
public class Themer {

    private static final TypedValue sTypedValue = new TypedValue();

    public static TypedValue resolveAttr(Context context, int attr) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, outValue, true);
        return outValue;
    }

    public static int getPrimaryColor(Context context) {
        return getThemeAttrColor(context, R.attr.colorPrimary);
    }

    public static int getAccentColor(Context context) {
        return getThemeAttrColor(context, R.attr.colorAccent);
    }

    public static int setColorAlpha(int color, int alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    public static int[] getSecondaryFabColors(Context context) {
        int colors[] = new int[2];
        boolean islight = isLightTheme(context);
        colors[0] = islight ? context.getResources().getColor(android.R.color.white)
                : context.getResources().getColor(android.R.color.black);
        colors[1] = colors[0]; //TODO
        return colors;
    }

    public static int getPlayIcon(Context context, boolean forcewhite) {
        return forcewhite ? R.drawable.ic_action_playback_play_white :
                (isLightTheme(context) ? R.drawable.ic_action_playback_play_black
                        : R.drawable.ic_action_playback_play_white);
    }

    public static int getPauseIcon(Context context, boolean forcewhite) {
        return forcewhite ? R.drawable.ic_action_playback_pause_white :
                (isLightTheme(context) ? R.drawable.ic_action_playback_pause_black
                        : R.drawable.ic_action_playback_pause_white);
    }

    public static int getNextIcon(Context context) {
        return isLightTheme(context) ? R.drawable.ic_action_playback_next_black
                : R.drawable.ic_action_playback_next_white;
    }

    public static int getPrevIcon(Context context) {
        return isLightTheme(context) ? R.drawable.ic_action_playback_prev_black
                : R.drawable.ic_action_playback_prev_white;
    }

    public static void themeToolbar(Toolbar toolbar) {
        toolbar.setBackgroundColor(getPrimaryColor(toolbar.getContext()));
    }

    public static void themeSeekBar(SeekBar seekBar) {
        int color = getAccentColor(seekBar.getContext());
        seekBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            seekBar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        } else if (seekBar instanceof CompatSeekBar) {
            Drawable thumb = ((CompatSeekBar) seekBar).getThumb();
            if (thumb != null) thumb.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    public static void themeProgressBar(ProgressBar progressBar) {
        int color = getAccentColor(progressBar.getContext());
        progressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    /**
     * TintManager (support-v7:21)
     */
    public static int getThemeAttrColor(Context context, int attr) {
        synchronized (sTypedValue) {
            if (context.getTheme().resolveAttribute(attr, sTypedValue, true)) {
                if (sTypedValue.type >= TypedValue.TYPE_FIRST_INT
                        && sTypedValue.type <= TypedValue.TYPE_LAST_INT) {
                    return sTypedValue.data;
                } else if (sTypedValue.type == TypedValue.TYPE_STRING) {
                    return context.getResources().getColor(sTypedValue.resourceId);
                }
            }
        }
        return 0;
    }

    /**
     * MediaRouterThemeHelper (support-v7)
     */
    public static boolean isLightTheme(Context context) {
        synchronized (sTypedValue) {
            return context.getTheme().resolveAttribute(R.attr.isLightTheme, sTypedValue, true) && sTypedValue.data != 0;
        }
    }

}