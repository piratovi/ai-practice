package com.kolosov.aipractice.openmeteo;

import com.kolosov.openmeteosdk.Location;
import com.kolosov.openmeteosdk.OpenMeteoService;
import com.kolosov.openmeteosdk.api.WeatherDayData;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;

import java.util.SortedSet;

@Command
@RequiredArgsConstructor
public class OpenMeteoCommand {

    private final OpenMeteoService openMeteoService;

    @Command(command = "forecast")
    public String forecast() {
        SortedSet<WeatherDayData> weekForecast = openMeteoService.getWeekForecast(Location.pickleball());
        return formatForecast(weekForecast);
    }

    private String formatForecast(SortedSet<WeatherDayData> forecast) {
        StringBuilder sb = new StringBuilder();
        for (WeatherDayData dayData : forecast) {
            sb.append("Date: ").append(dayData.day()).append("\n");
            for (WeatherDayData.WeatherHourData hourData : dayData.weatherHourData()) {
                sb.append(String.format("%-8s", hourData.time())).append("  ")
                        .append(String.format("%-15s", "Temperature: " + hourData.temperature() + "°C")).append("  ")
                        .append(String.format("%-20s", "Feels like: " + hourData.apparentTemperature() + "°C")).append("  ")
                        .append(String.format("%-15s", "Precipitation: " + hourData.precipitation() + "mm")).append("  ")
                        .append(String.format("%-15s", "Wind: " + hourData.windSpeed() + "m/s")).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
