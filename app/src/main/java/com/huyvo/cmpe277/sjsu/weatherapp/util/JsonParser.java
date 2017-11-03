package com.huyvo.cmpe277.sjsu.weatherapp.util;

import com.huyvo.cmpe277.sjsu.weatherapp.model.LocalTimeModel;
import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.huyvo.cmpe277.sjsu.weatherapp.util.JsonHelper.createJSONObject;

/**
 * Created by Huy Vo on 10/5/17.
 */

public class JsonParser {

    public static ArrayList<WeatherModel> parseThreeHourWeather(String jsonObjectString){

        ArrayList<WeatherModel> weatherModels = new ArrayList<>();
        JSONArray weatherJsonArray = JsonHelper.getJSONArray(createJSONObject(jsonObjectString), "list");
        int length = weatherJsonArray == null ? 0 : weatherJsonArray.length();

        for(int i = 0; i < length; i++){
            WeatherModel weatherModel = new WeatherModel();
            JSONObject jsonObject = weatherJsonArray.optJSONObject(i);
            weatherModel.dt = JsonHelper.getLong(jsonObject, "dt");

            JSONObject mainObject = JsonHelper.getJSONObject(jsonObject, "main");
            weatherModel.temp_max = (float) JsonHelper.getDouble(mainObject, "temp_max");
            weatherModel.temp_min = (float) JsonHelper.getDouble(mainObject, "temp_min");
            weatherModel.temp = (float) JsonHelper.getDouble(mainObject, "temp");

            JSONArray weatherArray = JsonHelper.getJSONArray(jsonObject, "weather");
            weatherModel.main = JsonHelper.getString(weatherArray, 0, "main");
            weatherModel.description = JsonHelper.getString(weatherArray, 0, "description");
            weatherModel.icon = JsonHelper.getString(weatherArray, 0, "icon");

            weatherModels.add(weatherModel);
        }

        return weatherModels;
    }


    public static LocalTimeModel parse(JSONObject jsonObject){
        LocalTimeModel timeModel = new LocalTimeModel();
        timeModel.timeZoneId = JsonHelper.getString(jsonObject, "timeZoneId");
        return timeModel;
    }
    public static LocalTimeModel parse(String jsonObjectString){
        JSONObject jsonObject = createJSONObject(jsonObjectString);
        return parse(jsonObject);
    }
    public static WeatherModel parseWeather(String jsonObjectString) {
        JSONObject jsonObject = createJSONObject(jsonObjectString);
        return jsonObject == null ? null : parseWeather(jsonObject);
    }

    public static WeatherModel parseWeather(JSONObject jsonObject){
        WeatherModel weatherModel = new WeatherModel();
        weatherModel.dt = JsonHelper.getLong(jsonObject, "dt");
        JSONArray weatherArray = JsonHelper.getJSONArray(jsonObject, "weather");
        weatherModel.main = JsonHelper.getString(weatherArray, 0, "main");
        weatherModel.icon = JsonHelper.getString(weatherArray, 0, "icon");

        weatherModel.description = JsonHelper.getString(weatherArray, 0, "description");
        if (weatherModel.icon != null) {
            weatherModel.icon = weatherModel.icon.substring(0, weatherModel.icon.length() - 1);
        }
        JSONObject mainObject = JsonHelper.getJSONObject(jsonObject, "main");
        weatherModel.temp_max = (float) JsonHelper.getDouble(mainObject, "temp_max");
        weatherModel.temp_min = (float) JsonHelper.getDouble(mainObject, "temp_min");

        weatherModel.pressure = (int) Math.round(JsonHelper.getDouble(mainObject, "pressure"));
        weatherModel.humidity = (int) Math.round(JsonHelper.getDouble(mainObject, "humidity"));
        weatherModel.temp = (float) JsonHelper.getDouble(mainObject, "temp");
        JSONObject windObject = JsonHelper.getJSONObject(jsonObject, "wind");
        weatherModel.windSpeed = (float) JsonHelper.getDouble(windObject, "speed");


        JSONObject sysObject = JsonHelper.getJSONObject(jsonObject, "sys");
        weatherModel.country = JsonHelper.getString(sysObject, "country");
        weatherModel.city = JsonHelper.getString(jsonObject, "name");

        JSONObject coorObject = JsonHelper.getJSONObject(jsonObject, "coord");
        weatherModel.lon = JsonHelper.getDouble(coorObject, "lon");
        weatherModel.lat = JsonHelper.getDouble(coorObject, "lat");

        return weatherModel;
    }


    public static ArrayList<WeatherModel> parseForecast(String jsonObjectString) {

        JSONObject cityArray =  JsonHelper.createJSONObject(jsonObjectString);
        JSONObject cityObject = JsonHelper.getJSONObject(cityArray, "city");

        String city = JsonHelper.getString(cityObject, "name");
        String country = JsonHelper.getString(cityObject, "country");

        JSONObject coorObjec = JsonHelper.getJSONObject(cityObject, "coord");
        double lon = JsonHelper.getDouble(coorObjec, "lon");
        double lat = JsonHelper.getDouble(coorObjec, "lat");
        ArrayList<WeatherModel> weatherModels = new ArrayList<>();
        JSONArray weatherJsonArray = JsonHelper.getJSONArray(createJSONObject(jsonObjectString), "list");
        int length = weatherJsonArray == null ? 0 : weatherJsonArray.length();

        for (int i = 1; i < length; i++) {
            WeatherModel weatherModel = new WeatherModel();
            JSONObject jsonObject = weatherJsonArray.optJSONObject(i);
            weatherModel.dt = JsonHelper.getLong(jsonObject, "dt");
            weatherModel.lat = lat;
            weatherModel.lon = lon;


            weatherModel.country = country;
            weatherModel.city = city;

            weatherModel.pressure = (int) Math.round(JsonHelper.getDouble(jsonObject, "pressure"));
            weatherModel.humidity = (int) Math.round(JsonHelper.getDouble(jsonObject, "humidity"));
            weatherModel.windSpeed = (float) JsonHelper.getDouble(jsonObject, "speed");
            weatherModel.degree = (float) JsonHelper.getDouble(jsonObject, "deg");

            JSONObject tempObject = JsonHelper.getJSONObject(jsonObject, "temp");
            weatherModel.temp_max = (float) JsonHelper.getDouble(tempObject, "max");
            weatherModel.temp_min = (float) JsonHelper.getDouble(tempObject, "min");
            weatherModel.temp_day = (float) JsonHelper.getDouble(tempObject, "day");
            weatherModel.temp_night = (float) JsonHelper.getDouble(tempObject, "night");
            weatherModel.temp_eve = (float) JsonHelper.getDouble(tempObject, "eve");
            weatherModel.temp_morn = (float) JsonHelper.getDouble(tempObject, "morn");

            JSONArray weatherArray = JsonHelper.getJSONArray(jsonObject, "weather");
            weatherModel.main = JsonHelper.getString(weatherArray, 0, "main");
            weatherModel.description = JsonHelper.getString(weatherArray, 0, "description");
            weatherModel.icon = JsonHelper.getString(weatherArray, 0, "icon");

            weatherModels.add(weatherModel);
        }
        return weatherModels;
    }
}
