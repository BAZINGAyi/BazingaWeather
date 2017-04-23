package com.bazinga.bazingaweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bazinga on 2017/4/1.
 */

public class Suggestion {

    public Comfort getComfort() {
        return comfort;
    }

    public void setComfort(Comfort comfort) {
        this.comfort = comfort;
    }

    public CarWash getCarWash() {
        return carWash;
    }

    public void setCarWash(CarWash carWash) {
        this.carWash = carWash;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public static class Comfot{
        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        @SerializedName("txt")
        public String info;

    }

    public static class CarWash{
        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        @SerializedName("txt")
        public String info;
    }

    public static class Sport{
        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        @SerializedName("txt")
        public String info;
    }

    public static class Comfort{
        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        @SerializedName("txt")
        public String info;
    }
}
