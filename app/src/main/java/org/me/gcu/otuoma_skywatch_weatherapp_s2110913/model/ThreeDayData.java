package org.me.gcu.otuoma_skywatch_weatherapp_s2110913.model;

public class ThreeDayData {
    private String todayLabel;
    private String todayWeather;
    private String temperatureMax;
    private String temperatureMin;
    private String tomorrowLabel;
    private String tomorrowWeather;
    private String tomorrowTemperatureMax;
    private String tomorrowTemperatureMin;


    private String laterLabel;
    private String laterWeather;
    private String laterTemperatureMax;
    private String laterTemperatureMin;


    // Added fields for latitude and longitude
    private String latitude;
    private String longitude;

    private String locationName;

    public String getTodayLabel() {
        return todayLabel;
    }
    public void setTodayLabel(String todayLabel) {
        this.todayLabel = todayLabel;
    }
    public String getTodayWeather() {
        return todayWeather;
    }
    public void setTodayWeather(String todayWeather) {
        this.todayWeather = todayWeather;
    }
    public String getTemperatureMax() {
        return temperatureMax;
    }
    public void setTemperatureMax(String temperatureMax) {
        this.temperatureMax = temperatureMax;
    }
    public String getTemperatureMin() {
        return temperatureMin;
    }
    public void setTemperatureMin(String temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public String getTomorrowLabel() {
        return tomorrowLabel;
    }
    public void setTomorrowLabel(String tomorrowLabel) {
        this.tomorrowLabel = tomorrowLabel;
    }
    public String getTomorrowWeather() {
        return tomorrowWeather;
    }
    public void setTomorrowWeather(String tomorrowWeather) {
        this.tomorrowWeather = tomorrowWeather;
    }
    public String getTomorrowTemperatureMax() {
        return tomorrowTemperatureMax;
    }
    public void setTomorrowTemperatureMax(String tomorrowTemperatureMax) {
        this.tomorrowTemperatureMax = tomorrowTemperatureMax;
    }
    public String getTomorrowTemperatureMin() {
        return tomorrowTemperatureMin;
    }
    public void setTomorrowTemperatureMin(String tomorrowTemperatureMin) {
        this.tomorrowTemperatureMin = tomorrowTemperatureMin;
    }

    public String getLaterLabel() {
        return laterLabel;
    }
    public void setLaterLabel(String laterLabel) {
        this.laterLabel = laterLabel;
    }
    public String getLaterWeather() {
        return laterWeather;
    }
    public void setLaterWeather(String laterWeather) {
        this.laterWeather = laterWeather;
    }
    public String getLaterTemperatureMax() {
        return laterTemperatureMax;
    }
    public void setLaterTemperatureMax(String laterTemperatureMax) {
        this.laterTemperatureMax = laterTemperatureMax;
    }
    public String getLaterTemperatureMin() {
        return laterTemperatureMin;
    }
    public void setLaterTemperatureMin(String laterTemperatureMin) {
        this.laterTemperatureMin = laterTemperatureMin;
    }

    // Getters and setters for latitude and longitude
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocationName(){return locationName;}

    public void setLocationName(String locationName){this.locationName = locationName;}

    @Override
    public String toString() {
        return "ThreeDayData{" +
                "todayLabel='" + todayLabel + '\'' +
                ", todayWeather='" + todayWeather + '\'' +
                ", temperatureMax='" + temperatureMax + '\'' +
                ", temperatureMin='" + temperatureMin + '\'' +
                ", tomorrowLabel='" + tomorrowLabel + '\'' +
                ", tomorrowWeather='" + tomorrowWeather + '\'' +
                ", tomorrowTemperatureMax='" + tomorrowTemperatureMax + '\'' +
                ", tomorrowTemperatureMin='" + tomorrowTemperatureMin + '\'' +
                ", laterLabel='" + laterLabel + '\'' +
                ", laterWeather='" + laterWeather + '\'' +
                ", laterTemperatureMax='" + laterTemperatureMax + '\'' +
                ", laterTemperatureMin='" + laterTemperatureMin + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", locationName='" + locationName + '\'' +
                '}';
    }
}
