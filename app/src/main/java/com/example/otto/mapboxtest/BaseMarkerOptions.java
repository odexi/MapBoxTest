package com.example.otto.mapboxtest;

import android.os.Parcel;
import android.os.Parcelable;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * Created by Otto on 27.10.2017.
 */

public abstract class BaseMarkerOptions<U extends Marker, T extends BaseMarkerOptions<U, T>> implements Parcelable {

    protected LatLng position;
    protected String snippet;
    protected String title;
    protected Icon icon;

    /**
     * Set the geographical location of the Marker.
     *
     * @param position the location to position the {@link Marker}.
     * @return the object for which the method was called.
     */
    public T position(LatLng position) {
        this.position = position;
        return getThis();
    }

    /**
     * Set the snippet of the Marker.
     *
     * @param snippet the snippet of the {@link Marker}.
     * @return the object for which the method was called.
     */
    public T snippet(String snippet) {
        this.snippet = snippet;
        return getThis();
    }

    /**
     * Set the title of the Marker.
     *
     * @param title the title of the {@link Marker}.
     * @return the object for which the method was called.
     */
    public T title(String title) {
        this.title = title;
        return getThis();
    }

    /**
     * Set the icon of the Marker.
     *
     * @param icon the icon of the {@link Marker}.
     * @return the object for which the method was called.
     */
    public T icon(Icon icon) {
        this.icon = icon;
        return getThis();
    }

    /**
     * Set the icon of the Marker.
     *
     * @param icon the icon of the {@link Marker}.
     * @return the object for which the method was called.
     */
    public T setIcon(Icon icon) {
        return icon(icon);
    }

    /**
     * Set the geographical location of the Marker.
     *
     * @param position the location to position the {@link Marker}.
     * @return the object for which the method was called.
     */
    public T setPosition(LatLng position) {
        return position(position);
    }

    /**
     * Set the snippet of the Marker.
     *
     * @param snippet the snippet of the {@link Marker}.
     * @return the object for which the method was called.
     */
    public T setSnippet(String snippet) {
        return snippet(snippet);
    }

    /**
     * Set the title of the Marker.
     *
     * @param title the title of the {@link Marker}.
     * @return the object for which the method was called.
     */
    public T setTitle(String title) {
        return title(title);
    }

    /**
     * Get the instance of the object for which this method was called.
     *
     * @return the object for which the this method was called.
     */
    public abstract T getThis();

    /**
     * Get the Marker.
     *
     * @return the Marker created from this builder.
     */
    public abstract U getMarker();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
