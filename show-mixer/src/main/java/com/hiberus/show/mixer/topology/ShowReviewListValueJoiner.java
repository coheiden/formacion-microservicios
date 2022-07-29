package com.hiberus.show.mixer.topology;

import com.hiberus.show.library.topology.OutputShowListEvent;
import com.hiberus.show.library.topology.ReviewListEvent;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ShowReviewListValueJoiner implements ValueJoiner<OutputShowListEvent, ReviewListEvent, OutputShowListEvent> {

    @Override
    public OutputShowListEvent apply(OutputShowListEvent inputShowEvent, ReviewListEvent reviewListEvent) {
        return OutputShowListEvent.newBuilder()
                .setName(inputShowEvent.getName())
                .setEventType(inputShowEvent.getEventType())
                .setReviews(reviewListEvent != null ? reviewListEvent.getReviews() : new ArrayList<>())
                .setPlatforms(inputShowEvent.getPlatforms())
                .build();
    }
}
