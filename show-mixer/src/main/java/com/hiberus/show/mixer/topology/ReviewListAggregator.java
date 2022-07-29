package com.hiberus.show.mixer.topology;

import com.hiberus.show.library.topology.EventType;
import com.hiberus.show.library.topology.InputReviewEvent;
import com.hiberus.show.library.topology.OutputShowListKey;
import com.hiberus.show.library.topology.ReviewListEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Initializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ReviewListAggregator implements Aggregator<OutputShowListKey, InputReviewEvent, ReviewListEvent>, Initializer<ReviewListEvent> {

    @Override
    public ReviewListEvent apply(final OutputShowListKey key, final InputReviewEvent value, ReviewListEvent aggregate) {
        if (EventType.DELETE.equals(value.getEventType())) {
            aggregate.setReviews(aggregate.getReviews().stream().filter(p -> !p.getUid().equals(value.getUid())).collect(Collectors.toList()));
        } else {
            aggregate.getReviews().add(value);
        }

        log.info("Aggregate: {}", aggregate);

        if (aggregate.getReviews().isEmpty()) {
            log.info("List of reviews for isan {} is empty; sending tombstone...", value.getIsan());
            aggregate = null;
        }

        return aggregate;
    }

    @Override
    public ReviewListEvent apply() {
        return ReviewListEvent.newBuilder().setReviews(new ArrayList<>()).setEventType(EventType.CREATE).build();
    }
}
