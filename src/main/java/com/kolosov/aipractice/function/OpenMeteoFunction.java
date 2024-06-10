package com.kolosov.aipractice.function;

import com.kolosov.openmeteosdk.Location;
import com.kolosov.openmeteosdk.OpenMeteoService;
import com.kolosov.openmeteosdk.api.WeatherDayData;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.util.SortedSet;
import java.util.function.Function;

@RequiredArgsConstructor
@Service("OpenMeteoFunction")
@Description("Gives weather forecast week ahead")
public class OpenMeteoFunction implements Function<Object, SortedSet<WeatherDayData>> {

    private final OpenMeteoService openMeteoService;

    @Override
    public SortedSet<WeatherDayData> apply(Object dummy) {
        return openMeteoService.getWeekForecast(Location.pickleball());
    }
}
