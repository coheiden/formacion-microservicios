package com.hiberus.show.mixer.topology;

import com.hiberus.show.library.topology.InputShowEvent;
import com.hiberus.show.library.topology.OutputShowListEvent;
import com.hiberus.show.library.topology.PlatformListEvent;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ShowPlatformListValueJoiner implements ValueJoiner<InputShowEvent, PlatformListEvent, OutputShowListEvent> {

    @Override
    public OutputShowListEvent apply(InputShowEvent inputShowEvent, PlatformListEvent platformListEvent) {
        return OutputShowListEvent.newBuilder()
                .setName(inputShowEvent.getName())
                .setEventType(inputShowEvent.getEventType())
                .setPlatforms(platformListEvent != null ? platformListEvent.getPlatforms() : new ArrayList<>())
                .setReviews(new ArrayList<>())
                .build();
    }
}
