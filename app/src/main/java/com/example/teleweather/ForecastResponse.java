package com.example.teleweather;

import java.util.List;

/*
*  Promt: Clase para manejar la respuesta de la API de pronóstico del clima
*/
public class ForecastResponse {
    private Location location;
    private Current current;
    private Forecast forecast;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    // Clase para la información de ubicación
    public static class Location {
        private String name;
        private String region;
        private String country;
        private String localtime;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getLocaltime() {
            return localtime;
        }

        public void setLocaltime(String localtime) {
            this.localtime = localtime;
        }
    }

    // Clase para la información actual del clima
    public static class Current {
        private double temp_c;
        private Condition condition;

        public double getTemp_c() {
            return temp_c;
        }

        public void setTemp_c(double temp_c) {
            this.temp_c = temp_c;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }
    }

    // Clase para el pronóstico
    public static class Forecast {
        private List<ForecastDay> forecastday;

        public List<ForecastDay> getForecastday() {
            return forecastday;
        }

        public void setForecastday(List<ForecastDay> forecastday) {
            this.forecastday = forecastday;
        }
    }

    // Clase para el pronóstico diario
    public static class ForecastDay {
        private String date;
        private Day day;
        private Astro astro;
        private List<Hour> hour;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Day getDay() {
            return day;
        }

        public void setDay(Day day) {
            this.day = day;
        }

        public Astro getAstro() {
            return astro;
        }

        public void setAstro(Astro astro) {
            this.astro = astro;
        }

        public List<Hour> getHour() {
            return hour;
        }

        public void setHour(List<Hour> hour) {
            this.hour = hour;
        }
    }

    // Clase para la información diaria del clima
    public static class Day {
        private double maxtemp_c;
        private double mintemp_c;
        private double avgtemp_c;
        private double maxwind_kph;
        private double totalprecip_mm;
        private double avgvis_km;
        private double avghumidity;
        private Condition condition;

        public double getMaxtemp_c() {
            return maxtemp_c;
        }

        public void setMaxtemp_c(double maxtemp_c) {
            this.maxtemp_c = maxtemp_c;
        }

        public double getMintemp_c() {
            return mintemp_c;
        }

        public void setMintemp_c(double mintemp_c) {
            this.mintemp_c = mintemp_c;
        }

        public double getAvgtemp_c() {
            return avgtemp_c;
        }

        public void setAvgtemp_c(double avgtemp_c) {
            this.avgtemp_c = avgtemp_c;
        }

        public double getMaxwind_kph() {
            return maxwind_kph;
        }

        public void setMaxwind_kph(double maxwind_kph) {
            this.maxwind_kph = maxwind_kph;
        }

        public double getTotalprecip_mm() {
            return totalprecip_mm;
        }

        public void setTotalprecip_mm(double totalprecip_mm) {
            this.totalprecip_mm = totalprecip_mm;
        }

        public double getAvgvis_km() {
            return avgvis_km;
        }

        public void setAvgvis_km(double avgvis_km) {
            this.avgvis_km = avgvis_km;
        }

        public double getAvghumidity() {
            return avghumidity;
        }

        public void setAvghumidity(double avghumidity) {
            this.avghumidity = avghumidity;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }
    }

    // Clase para la información astronómica
    public static class Astro {
        private String sunrise;
        private String sunset;
        private String moonrise;
        private String moonset;
        private String moon_phase;

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public String getMoonrise() {
            return moonrise;
        }

        public void setMoonrise(String moonrise) {
            this.moonrise = moonrise;
        }

        public String getMoonset() {
            return moonset;
        }

        public void setMoonset(String moonset) {
            this.moonset = moonset;
        }

        public String getMoon_phase() {
            return moon_phase;
        }

        public void setMoon_phase(String moon_phase) {
            this.moon_phase = moon_phase;
        }
    }

    // Clase para la información por hora
    public static class Hour {
        private String time;
        private double temp_c;
        private Condition condition;
        private double wind_kph;
        private double precip_mm;
        private int humidity;
        private double feelslike_c;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public double getTemp_c() {
            return temp_c;
        }

        public void setTemp_c(double temp_c) {
            this.temp_c = temp_c;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }

        public double getWind_kph() {
            return wind_kph;
        }

        public void setWind_kph(double wind_kph) {
            this.wind_kph = wind_kph;
        }

        public double getPrecip_mm() {
            return precip_mm;
        }

        public void setPrecip_mm(double precip_mm) {
            this.precip_mm = precip_mm;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public double getFeelslike_c() {
            return feelslike_c;
        }

        public void setFeelslike_c(double feelslike_c) {
            this.feelslike_c = feelslike_c;
        }
    }

    // Clase para la condición del clima
    public static class Condition {
        private String text;
        private String icon;
        private int code;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
