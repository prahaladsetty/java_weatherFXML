package weatherfxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.IOException;

public class mainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_page.fxml"));
        Parent root = loader.load();

        WeatherController controller = loader.getController();


        JSONObject cityData = (JSONObject) read_data.getLocationData("Chicago");
        double lat = (double) cityData.get("latitude");
        double lon = (double) cityData.get("longitude");


        controller.setWeatherData("Chicago", 25.3, 76, 3.4);

        Scene scene = new Scene(root, 1280, 720);

        stage.setScene(scene);
        stage.setTitle("WeatherFXML");
        stage.show();


    }

    public static void main(String[] args) {

        launch();
    }
}