package com.mobile.tiamo.adapters;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
    A schedule item for the list view
 **/
public class ScheduleItem implements Parcelable {

    private String title;
    private String hours;
    private String days;

    public ScheduleItem() {
    }

    public ScheduleItem(String title, String hours, String days) {
        this.title = title;
        this.hours = hours;
        this.days = days;
    }

    protected ScheduleItem(Parcel in) {
        title = in.readString();
        hours = in.readString();
        days = in.readString();
    }

    public static final Creator<ScheduleItem> CREATOR = new Creator<ScheduleItem>() {
        @Override
        public ScheduleItem createFromParcel(Parcel in) {
            return new ScheduleItem(in);
        }

        @Override
        public ScheduleItem[] newArray(int size) {
            return new ScheduleItem[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(hours);
        dest.writeString(days);
    }
}
