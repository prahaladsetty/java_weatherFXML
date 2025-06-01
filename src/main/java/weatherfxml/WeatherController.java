package weatherfxml;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WeatherController {
    @FXML private Label city1;
    @FXML private Label temp1;
    @FXML private Label humidity1;
    @FXML private Label wind_speed1;

    public void setWeatherData(String city, double temp, long humidity, double windSpeed) {
        city1.setText(city);
        temp1.setText(temp + " °C");
        humidity1.setText(humidity + " %");
        wind_speed1.setText(windSpeed + " m/s");
    }
}
