package com.hiberus.show.mixer.topology;

import com.hiberus.show.library.topology.InputShowEvent;
import com.hiberus.show.library.topology.OutputShowListEvent;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class InputShowEventValueMapper implements ValueMapper<InputShowEvent, OutputShowListEvent> {

    @Override
    public OutputShowListEvent apply(InputShowEvent value) {
        return OutputShowListEvent.newBuilder()
                .setEventType(value.getEventType())
                .setName(value.getName())
                .setPlatforms(new ArrayList<>())
                .setReviews(new ArrayList<>())
                .build();
    }
}
