package com.kolosov.aipractice.function;

import com.kolosov.openmeteosdk.Location;
import com.kolosov.openmeteosdk.OpenMeteoService;
import com.kolosov.openmeteosdk.api.WeatherDayData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.SortedSet;
import java.util.function.Function;

@Configuration
public class FunctionConfig {

    @Bean
    @Description("Gives a weather forecast one week ahead")
    public Function<Location, SortedSet<WeatherDayData>> weekForecast(OpenMeteoService openMeteoService) {
        return openMeteoService::getWeekForecast;
    }
}
