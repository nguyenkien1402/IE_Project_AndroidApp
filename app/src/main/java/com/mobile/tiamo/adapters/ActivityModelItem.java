package com.mobile.tiamo.adapters;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;

import java.io.Serializable;

public class ActivityModelItem implements Parcelable {
    private long uid;
    private String title;
    private int hours;
    private int minutes;
    private int dayPerWeek;
    private int isHighPriority;
    private int hourPractice;
    private int minutePractice;

    public ActivityModelItem() {
    }

    public ActivityModelItem(long uid, String title, int hours, int minutes, int dayPerWeek, int isHighPriority, int hourPractice, int minutePractice) {
        this.uid = uid;
        this.title = title;
        this.hours = hours;
        this.minutes = minutes;
        this.dayPerWeek = dayPerWeek;
        this.isHighPriority = isHighPriority;
        this.hourPractice = hourPractice;
        this.minutePractice = minutePractice;
    }

    protected ActivityModelItem(Parcel in) {
        uid = in.readLong();
        title = in.readString();
        hours = in.readInt();
        minutes = in.readInt();
        dayPerWeek = in.readInt();
        isHighPriority = in.readInt();
        hourPractice = in.readInt();
        minutePractice = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(uid);
        dest.writeString(title);
        dest.writeInt(hours);
        dest.writeInt(minutes);
        dest.writeInt(dayPerWeek);
        dest.writeInt(isHighPriority);
        dest.writeInt(hourPractice);
        dest.writeInt(minutePractice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ActivityModelItem> CREATOR = new Creator<ActivityModelItem>() {
        @Override
        public ActivityModelItem createFromParcel(Parcel in) {
            return new ActivityModelItem(in);
        }

        @Override
        public ActivityModelItem[] newArray(int size) {
            return new ActivityModelItem[size];
        }
    };

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getDayPerWeek() {
        return dayPerWeek;
    }

    public void setDayPerWeek(int dayPerWeek) {
        this.dayPerWeek = dayPerWeek;
    }

    public int getIsHighPriority() {
        return isHighPriority;
    }

    public void setIsHighPriority(int isHighPriority) {
        this.isHighPriority = isHighPriority;
    }

    public int getHourPractice() {
        return hourPractice;
    }

    public void setHourPractice(int hourPractice) {
        this.hourPractice = hourPractice;
    }

    public int getMinutePractice() {
        return minutePractice;
    }

    public void setMinutePractice(int minutePractice) {
        this.minutePractice = minutePractice;
    }
}
