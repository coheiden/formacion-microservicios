package com.hiberus.show.mixer.topology;

import com.hiberus.show.library.topology.EventType;
import com.hiberus.show.library.topology.InputReviewEvent;
import com.hiberus.show.library.topology.OutputShowListKey;
import com.hiberus.show.library.topology.ReviewListEvent;
import com.hiberus.show.mixer.service.ShowMixerServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.Predicate;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewListFilter implements Predicate<OutputShowListKey, InputReviewEvent> {

    private final InteractiveQueryService queryService;

    private ReadOnlyKeyValueStore<OutputShowListKey, ReviewListEvent> reviewListStore;

    @Override
    public boolean test(final OutputShowListKey key, final InputReviewEvent current) {
        final boolean passes;
        final ReviewListEvent previous = reviewListStore().get(key);

        if (EventType.DELETE.equals(current.getEventType())) {
            passes = true;
        } else {
            current.setEventType(EventType.CREATE);
            passes = previous == null || !previous.getReviews().stream().anyMatch(a -> a.getUid().equals(current.getUid()));
        }

        log.info("Previous Already entered: {}", previous);
        log.info("New to add: {}", current);
        log.info("Review is repeated: {}", !passes);

        return passes;
    }

    private ReadOnlyKeyValueStore<OutputShowListKey, ReviewListEvent> reviewListStore() {
        if (reviewListStore == null) {
            reviewListStore = queryService.getQueryableStore(ShowMixerServiceImpl.REVIEW_TABLE, QueryableStoreTypes.keyValueStore());
        }

        return reviewListStore;
    }
}
