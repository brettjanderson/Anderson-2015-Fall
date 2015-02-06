package com.brettjamesanderson.designpatterns;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Brettness on 2/3/15.
 */
public class DesignPattern implements Parcelable {

    private String designPatternName;
    private String designPatternDescription;
    private boolean favorite;

    public DesignPattern(String name, String description){
        this.designPatternDescription = description;
        this.designPatternName = name;
        this.favorite = false;
    }

    public String getDesignPatternDescription() {
        return designPatternDescription;
    }

    public String getDesignPatternName(){
        return designPatternName;
    }

    public void setFavorite(boolean fav){
        favorite = fav;
    }

    public boolean getFavorite(){
        return favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected DesignPattern(Parcel in) {
        designPatternName = in.readString();
        designPatternDescription = in.readString();
        favorite = in.readByte() != 0x00;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(designPatternName);
        dest.writeString(designPatternDescription);
        dest.writeByte((byte) (favorite ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DesignPattern> CREATOR = new Parcelable.Creator<DesignPattern>() {
        @Override
        public DesignPattern createFromParcel(Parcel in) {
            return new DesignPattern(in);
        }

        @Override
        public DesignPattern[] newArray(int size) {
            return new DesignPattern[size];
        }
    };
}
