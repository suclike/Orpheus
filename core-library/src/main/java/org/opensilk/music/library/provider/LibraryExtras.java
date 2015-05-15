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

package org.opensilk.music.library.provider;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;

import org.opensilk.music.library.internal.IBundleableObserver;
import org.opensilk.music.library.internal.LibraryException;
import org.opensilk.music.library.internal.ResultReceiver;
import org.opensilk.music.library.sort.BundleableSortOrder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by drew on 5/14/15.
 */
public class LibraryExtras {

    /**
     * Request uri: always built with {@link LibraryUris}, never null
     */
    public static final String URI = "uri";
    /**
     * Sortorder: one of the strings in the sort package. never null for {@link LibraryMethods#QUERY}
     */
    public static final String SORTORDER = "sortorder";
    /**
     *
     */
    public static final String URI_LIST = "uri_list";
    /**
     * Internal use: {@link org.opensilk.music.library.internal.IBundleableObserver}, never null
     */
    public static final String BUNDLE_SUBSCRIBER_CALLBACK = "bundle_sub_cb";
    /**
     * Internal use: argument in returned bundle if not true. {@link #CAUSE} must be set
     */
    public static final String OK = "ok";
    /**
     * Internal use: argument in returned bundle containing {@link LibraryException}
     * when {@link #OK} is false.
     */
    public static final String CAUSE = "cause";
    private static final String WRAPPEDCAUSE = "wrappedcause";
    /**
     * Internel use:
     */
    public static final String RESULT_RECEIVER_CALLBACK = "result_receiver_cb";

    public static Uri getUri(Bundle extras) {
        return extras.getParcelable(URI);
    }

    public static String getSortOrder(Bundle extras) {
        return extras.getString(SORTORDER, BundleableSortOrder.A_Z);
    }

    public static List<Uri> getUriList(Bundle extras) {
        return extras.<Uri>getParcelableArrayList(URI_LIST);
    }

    public static boolean getOk(Bundle extras) {
        return extras.getBoolean(OK);
    }

    public static LibraryException getCause(Bundle extras) {
        Bundle b = extras.getBundle(WRAPPEDCAUSE);
        b.setClassLoader(LibraryException.class.getClassLoader());
        return b.getParcelable(CAUSE);
    }

    private static Method _getIBinder = null;
    public static IBinder getBundleableObserverBinder(Bundle extras) {
        if (Build.VERSION.SDK_INT >= 18) {
            return extras.getBinder(BUNDLE_SUBSCRIBER_CALLBACK);
        } else {
            try {
                if (_getIBinder == null) {
                    synchronized (LibraryExtras.class) {
                        if (_getIBinder == null) {
                            _getIBinder = Bundle.class.getDeclaredMethod("getIBinder", String.class);
                        }
                    }
                }
                return (IBinder) _getIBinder.invoke(extras, BUNDLE_SUBSCRIBER_CALLBACK);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static Method _putIBinder = null;
    private static void putBundleableObserverBinder(Bundle extras, IBinder binder) {
        if (Build.VERSION.SDK_INT >= 18) {
            extras.putBinder(BUNDLE_SUBSCRIBER_CALLBACK, binder);
        } else {
            try {
                if (_putIBinder == null) {
                    synchronized (LibraryExtras.class) {
                        if (_putIBinder == null) {
                            _putIBinder = Bundle.class.getDeclaredMethod("putIBinder", String.class, IBinder.class);
                        }
                    }
                }
                _putIBinder.invoke(extras, BUNDLE_SUBSCRIBER_CALLBACK, binder);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static ResultReceiver getResultReciever(Bundle extras) {
        extras.setClassLoader(ResultReceiver.class.getClassLoader());
        return extras.<ResultReceiver>getParcelable(RESULT_RECEIVER_CALLBACK);
    }

    public static Builder b() {
        return new Builder();
    }

    public static class Builder {
        final Bundle b = new Bundle();

        private Builder() {
        }

        public Builder putUri(Uri uri) {
            b.putParcelable(URI, uri);
            return this;
        }

        public Builder putSortOrder(String sortorder) {
            b.putString(SORTORDER, sortorder);
            return this;
        }

        public Builder putUriList(List<Uri> uris) {
            b.putParcelableArrayList(URI_LIST, new ArrayList<Parcelable>(uris));
            return this;
        }

        public Builder putOk(boolean ok) {
            b.putBoolean(OK, ok);
            return this;
        }

        public Builder putCause(LibraryException e) {
            //HAX since the bundle is returned (i guess)
            //the system classloader remarshals the bundle before we
            //can set our classloader...causing ClassNotFoundException.
            //To remedy nest the cause in another bundle.
            Bundle b2 = new Bundle();
            b2.putParcelable(CAUSE, e);
            b.putBundle(WRAPPEDCAUSE, b2);
            return this;
        }

        public Builder putBundleableObserverCallback(IBundleableObserver o) {
            putBundleableObserverBinder(b, o.asBinder());
            return this;
        }

        public Builder putResultReceiver(ResultReceiver r) {
            b.putParcelable(RESULT_RECEIVER_CALLBACK, r);
            return this;
        }

        public Bundle get() {
            return b;
        }
    }
}