package com.kolosov.aipractice.openmeteo;

import com.kolosov.openmeteosdk.Location;
import com.kolosov.openmeteosdk.OpenMeteoService;
import com.kolosov.openmeteosdk.api.WeatherDayData;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;

import java.math.BigDecimal;
import java.util.SortedSet;

@Command
@RequiredArgsConstructor
public class OpenMeteoCommand {

    private final OpenMeteoService openMeteoService;

    @Command(command = "openMeteo")
    public void openMeteo() {
        SortedSet<WeatherDayData> weekForecast = openMeteoService.getWeekForecast(Location.pickleball());
        System.out.println(weekForecast);
    }
}
