package com.mobile.tiamo.adapters;

import android.os.Parcel;
import android.os.Parcelable;

public class DailyRoutineItem implements Parcelable{
    private String title;
    private String hours;
    private String days;
    private int isDone;
    private long uid;

    public DailyRoutineItem() {
    }

    protected DailyRoutineItem(Parcel in) {
        title = in.readString();
        hours = in.readString();
        days = in.readString();
        isDone = in.readInt();
        uid = in.readLong();
    }

    public static final Creator<DailyRoutineItem> CREATOR = new Creator<DailyRoutineItem>() {
        @Override
        public DailyRoutineItem createFromParcel(Parcel in) {
            return new DailyRoutineItem(in);
        }

        @Override
        public DailyRoutineItem[] newArray(int size) {
            return new DailyRoutineItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(hours);
        dest.writeString(days);
        dest.writeInt(isDone);
        dest.writeLong(uid);
    }

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

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
