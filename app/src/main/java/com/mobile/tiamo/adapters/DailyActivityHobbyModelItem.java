package com.mobile.tiamo.adapters;

import android.os.Parcel;
import android.os.Parcelable;

public class DailyActivityHobbyModelItem implements Parcelable {

    private long uid;
    private String title;
    private int hours;
    private int minutes;
    private int dayPerWeek;
    private int isHighPriority;
    public static final Creator<DailyActivityHobbyModelItem> CREATOR = new Creator<DailyActivityHobbyModelItem>() {
        @Override
        public DailyActivityHobbyModelItem createFromParcel(Parcel in) {
            return new DailyActivityHobbyModelItem(in);
        }

        @Override
        public DailyActivityHobbyModelItem[] newArray(int size) {
            return new DailyActivityHobbyModelItem[size];
        }
    };
    private String dateCreated;

    public DailyActivityHobbyModelItem() {
    }

    public DailyActivityHobbyModelItem(long uid, String title, int hours, int dayPerWeek, int isHighPriority, int minutes, String dateCreated) {
        this.uid = uid;
        this.title = title;
        this.hours = hours;
        this.dayPerWeek = dayPerWeek;
        this.isHighPriority = isHighPriority;
        this.minutes = minutes;
        this.dateCreated = dateCreated;
    }

    protected DailyActivityHobbyModelItem(Parcel in) {
        uid = in.readLong();
        title = in.readString();
        hours = in.readInt();
        minutes = in.readInt();
        dayPerWeek = in.readInt();
        isHighPriority = in.readInt();
        dateCreated = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(uid);
        dest.writeString(title);
        dest.writeInt(hours);
        dest.writeInt(minutes);
        dest.writeInt(dayPerWeek);
        dest.writeInt(isHighPriority);
        dest.writeString(dateCreated);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
