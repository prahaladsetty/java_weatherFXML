package weatherfxml;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class read_data {
    public static void main(String[] args)
    {
        try {
            Scanner scanner = new Scanner(System.in);
            String city;

            System.out.println("enter city: ");
            city = scanner.nextLine();

            JSONObject cityLocationData = (JSONObject) getLocationData(city);
            double latitude = (double) cityLocationData.get("latitude");
            double longitude = (double) cityLocationData.get("longitude");

            System.out.println("lat: " + latitude);
            System.out.println("long: " + longitude);

            displayWeatherData(latitude, longitude);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void displayWeatherData(double latitude, double longitude) {
        try {
            String url = "https://api.open-meteo.com/v1/forecast?latitude="
                    +latitude+"&longitude="+longitude+"&current=temperature_2m,relative_humidity_2m,wind_speed_10m";

            HttpURLConnection apiConnection = fetchApiResponse(url);

            if (apiConnection.getResponseCode() != 200) {
                System.out.println("error: could not connect to API loser");
                return;
            }

            String jsonResponse = readApiResponse(apiConnection);

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
            JSONObject currentWeatherJSON = (JSONObject) jsonObject.get("current");

            double temp = (double) currentWeatherJSON.get("temperature_2m");
            System.out.println("Current temp: " + temp);

            long relHumidity = (long) currentWeatherJSON.get("relative_humidity_2m");
            System.out.println("Current rel_humidity: " + relHumidity);

            double wind_speed = (double) currentWeatherJSON.get("wind_speed_10m");
            System.out.println("Current wind_speed: " + wind_speed);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Object getLocationData(String city) throws IOException {

        city = city.replaceAll(" ", "+");

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name="+city+"&count=1&language=en&format=json";
        try {
            HttpURLConnection apidonnection = fetchApiResponse(urlString);

            if (apidonnection.getResponseCode() != 200) {
                System.out.println("error: can't connect to API loser");
                return null;
            }

            String jsonResponse = readApiResponse(apidonnection);

            JSONParser parser = new JSONParser();
            JSONObject resultsJsonObj = (JSONObject) parser.parse(jsonResponse);

            JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
            return (JSONObject) locationData.get(0);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            
        return null;    


    }
}

    private static String readApiResponse(HttpURLConnection apiConnection) {

        try {
            StringBuilder resultJson = new StringBuilder();

            Scanner scanner = new Scanner(apiConnection.getInputStream());

            while (scanner.hasNext()) {
                resultJson.append(scanner.nextLine());
            }

            scanner.close();

            return resultJson.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; //means an error occurred
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {

        try{
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            conn.connect();

            return conn;
        }
        catch (IOException e) {
            System.out.println("e");
            e.printStackTrace();
        }

        return null;
    }
    }
