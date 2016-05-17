package com.skycopyhot.aweather.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yongqiang
 * 17/5/16
 */
public class Weather implements Parcelable {

    private String city;
    private String description;
    private String icon;
    private String date;
    private String temp_ave;
    private String temp_min;
    private String temp_max;

    public Weather(String city, String description, String icon, String date, String temp_ave, String temp_min, String temp_max) {
        this.city = city;
        this.description = description;
        this.icon = icon;
        this.date = date;
        this.temp_ave = temp_ave;
        this.temp_max = temp_max;
        this.temp_min = temp_min;
    }

    public Weather(Parcel source) {
        this.city = source.readString();
        this.description = source.readString();
        this.icon = source.readString();
        this.date = source.readString();
        this.temp_ave = source.readString();
        this.temp_max = source.readString();
        this.temp_min = source.readString();
    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel source) {
            return new Weather(source);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };


    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.city);
        dest.writeString(this.description);
        dest.writeString(this.icon);
        dest.writeString(this.date);
        dest.writeString(this.temp_ave);
        dest.writeString(this.temp_max);
        dest.writeString(this.temp_min);
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public String getDate() {
        return date;
    }

    public String getTemp_ave() {
        return temp_ave;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public String getTemp_max() {
        return temp_max;
    }
}
