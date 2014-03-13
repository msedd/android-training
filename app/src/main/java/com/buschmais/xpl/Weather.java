package com.buschmais.xpl;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Weather class represents the retrieved weather information
 *
 * Created by kathrin.heindl
 */
public class Weather implements Serializable {

    private String city;
    private String cityCode;
    private String temperature;
    private String date;
    private String condition;
    private String conditionCode;
    private ArrayList<Weather> forecast = new ArrayList<Weather>();

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        return  city + ": " + temperature + "°C ("+condition+")";
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getConditionCode() {
        return conditionCode;
    }

    public void setConditionCode(String conditionCode) {
        this.conditionCode = conditionCode;
    }

    public void addForecast(Weather forecast){
        this.forecast.add(forecast);
    }

    public ArrayList<Weather> getForecast(){
        return forecast;
    }
}
