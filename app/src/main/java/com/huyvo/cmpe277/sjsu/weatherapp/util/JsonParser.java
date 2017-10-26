package com.huyvo.cmpe277.sjsu.weatherapp.util;

import android.util.Log;

import com.huyvo.cmpe277.sjsu.weatherapp.model.WeatherModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.huyvo.cmpe277.sjsu.weatherapp.util.JsonHelper.createJSONObject;

/**
 * Created by Huy Vo on 10/5/17.
 */

public class JsonParser {
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
        if (weatherModel.icon != null) {
            weatherModel.icon = weatherModel.icon.substring(0, weatherModel.icon.length() - 1);
        }
        JSONObject mainObject = JsonHelper.getJSONObject(jsonObject, "main");
        weatherModel.maxTemperature = (int) Math.round(JsonHelper.getDouble(mainObject, "temp_max"));
        weatherModel.minTemperature = (int) Math.round(JsonHelper.getDouble(mainObject, "temp_min"));
        weatherModel.pressure = (int) Math.round(JsonHelper.getDouble(mainObject, "pressure"));
        weatherModel.humidity = (int) Math.round(JsonHelper.getDouble(mainObject, "humidity"));
        weatherModel.temp = (float) JsonHelper.getDouble(mainObject, "temp");
        JSONObject windObject = JsonHelper.getJSONObject(jsonObject, "wind");
        weatherModel.windSpeed = (float) JsonHelper.getDouble(windObject, "speed");


        JSONObject sysObject = JsonHelper.getJSONObject(jsonObject, "sys");
        weatherModel.country = JsonHelper.getString(sysObject, "country");
        weatherModel.city = JsonHelper.getString(jsonObject, "name");
        JSONObject coorObjec = JsonHelper.getJSONObject(jsonObject, "coord");
        weatherModel.lon = JsonHelper.getString(coorObjec, "lon");
        weatherModel.lat = JsonHelper.getString(coorObjec, "lat");

        return weatherModel;
    }

    public static ArrayList<WeatherModel> newParseForecast(String jsonObjectString) {
        ArrayList<WeatherModel> weatherModels = new ArrayList<>();
        JSONArray weatherJsonArray = JsonHelper.getJSONArray(createJSONObject(jsonObjectString), "list");
        int length = weatherJsonArray == null ? 0 : weatherJsonArray.length();

        for (int i = 0; i < length; i++) {
            Log.d("newParseForecast", i+"");
            WeatherModel weatherModel = new WeatherModel();
            JSONObject jsonObject = weatherJsonArray.optJSONObject(i);
            weatherModel.dt = JsonHelper.getLong(jsonObject, "dt");
            weatherModel.humidity = (int) Math.round(JsonHelper.getDouble(jsonObject, "humidity"));
            weatherModel.windSpeed = (float) JsonHelper.getDouble(jsonObject, "speed");
            weatherModel.degree = (float) JsonHelper.getDouble(jsonObject, "deg");
            JSONObject tempObject = JsonHelper.getJSONObject(jsonObject, "temp");
            weatherModel.maxTemperature = (int) Math.round(JsonHelper.getDouble(tempObject, "max"));
            weatherModel.minTemperature = (int) Math.round(JsonHelper.getDouble(tempObject, "min"));
            JSONArray weatherArray = JsonHelper.getJSONArray(jsonObject, "weather");
            weatherModel.main = JsonHelper.getString(weatherArray, 0, "main");
            weatherModel.icon = JsonHelper.getString(weatherArray, 0, "icon");
            if (weatherModel.icon != null) {
                weatherModel.icon = weatherModel.icon.substring(0, weatherModel.icon.length() - 1);
            }
            weatherModels.add(weatherModel);
        }
        return weatherModels;
    }

    public static ArrayList<WeatherModel> parseForecast(String jsonObjectString) {
        JSONObject cityArray =  JsonHelper.createJSONObject(jsonObjectString);

        JSONObject cityObject = JsonHelper.getJSONObject(cityArray, "city");

        String city = JsonHelper.getString(cityObject, "name");

        ArrayList<WeatherModel> weatherModels = new ArrayList<>();
        JSONArray weatherJsonArray = JsonHelper.getJSONArray(createJSONObject(jsonObjectString), "list");
        int length = weatherJsonArray == null ? 0 : weatherJsonArray.length();

        Logger.e("JsonParser", JsonHelper.toString(weatherJsonArray, true));
        for (int i = 0; i < length; i++) {
            WeatherModel weatherModel = new WeatherModel();
            JSONObject jsonObject = weatherJsonArray.optJSONObject(i);
            weatherModel.dt = JsonHelper.getLong(jsonObject, "dt");
            if (DateHelper.numberOfDayFromToday(weatherModel.dt, "GMT-4") < 0) {
                continue;
            }
            weatherModel.city = city;
            weatherModel.pressure = (int) Math.round(JsonHelper.getDouble(jsonObject, "pressure"));
            weatherModel.humidity = (int) Math.round(JsonHelper.getDouble(jsonObject, "humidity"));
            weatherModel.windSpeed = (float) JsonHelper.getDouble(jsonObject, "speed");
            weatherModel.degree = (float) JsonHelper.getDouble(jsonObject, "deg");
            JSONObject tempObject = JsonHelper.getJSONObject(jsonObject, "temp");
            weatherModel.maxTemperature = (int) Math.round(JsonHelper.getDouble(tempObject, "max"));
            weatherModel.minTemperature = (int) Math.round(JsonHelper.getDouble(tempObject, "min"));
            weatherModel.day = (float) JsonHelper.getDouble(tempObject, "day");
            JSONArray weatherArray = JsonHelper.getJSONArray(jsonObject, "weather");
            weatherModel.main = JsonHelper.getString(weatherArray, 0, "main");
            weatherModel.icon = JsonHelper.getString(weatherArray, 0, "icon");

            weatherModels.add(weatherModel);
        }
        return weatherModels;
    }
}
