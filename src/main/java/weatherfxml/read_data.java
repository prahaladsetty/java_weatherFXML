/*
    file to parse .JSON file to get latitude + longitude
    uses latitude + longitude to get weather data
    displays weather data

    functions:

        main()

        url_get()
        parse_json()
        get_location()
        display_data()

 */

package weatherfxml;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

// each function uses a try and catch method to make sure program doesn't stop when an error occurs.

public class read_data {
    public static void main(String[] args) // calls functions
    {
        try {
            Scanner scanner = new Scanner(System.in);
            String city;

            System.out.println("enter city: ");
            city = scanner.nextLine();

            JSONObject cityLocationData = (JSONObject) getLocationData(city);
            double latitude = (double) cityLocationData.get("latitude"); // calls function(s)
            double longitude = (double) cityLocationData.get("longitude"); // calls function(s)

            System.out.println("lat: " + latitude);
            System.out.println("long: " + longitude);

            displayWeatherData(latitude, longitude); //parameters needed to access weather data

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void displayWeatherData(double latitude, double longitude) { // prints data to user, variables are useful
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

            double temp = (double) currentWeatherJSON.get("temperature_2m"); //temp
            System.out.println("Current temp: " + temp + "C");

            long relHumidity = (long) currentWeatherJSON.get("relative_humidity_2m"); //humidity
            System.out.println("Current rel_humidity: " + relHumidity + "%");

            double wind_speed = (double) currentWeatherJSON.get("wind_speed_10m"); //wind speed
            System.out.println("Current wind_speed: " + wind_speed + "m/s");

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Object getLocationData(String city) throws IOException { // gets long + lat

        city = city.replaceAll(" ", "+");

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name="+city+"&count=10&language=en&format=json";
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

            String[] strings = new String[5]; // initialize array

            for (int i = 0; i < strings.length; i++ ) { // assign state name to each element in array
                JSONObject locationID = (JSONObject) locationData.get(i);
                locationID.get("admin1");
                strings[i] = locationID.get("admin1").toString();
            }

            System.out.println(Arrays.toString(strings));

            Scanner scanner = new Scanner(System.in);
            System.out.println("which specific " + city + " do you want the weather data for?"); // ask user which ID they want

            String ID = scanner.nextLine();
            int usableID = 0;
            for (int i = 0; i < strings.length; i++) { // find which ID user wants
                if (Objects.equals(strings[i], ID)) {
                    usableID = i;
                    break;
                }
            }

            return (JSONObject) locationData.get(usableID); // gets the weather data for the ID user picks.
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            
        return null;    


    }
}

    private static String readApiResponse(HttpURLConnection apiConnection) { //parses .JSON file

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

    private static HttpURLConnection fetchApiResponse(String urlString) { // connects to the API .JSON file

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
