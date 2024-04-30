package org.me.gcu.otuoma_skywatch_weatherapp_s2110913.model;

public class WeatherData {
    private String weatherType;
    private String date;
    private String time;
    private String temperature;
    private String windSpeed;
    private String humidity;
    private String pressure;
    private String visibility;


    public String getWeatherType() {
        return weatherType;
    }
    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

}

