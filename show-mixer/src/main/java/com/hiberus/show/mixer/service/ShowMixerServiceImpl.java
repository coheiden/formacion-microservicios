package com.hiberus.show.mixer.service;

import com.hiberus.show.library.topology.*;
import com.hiberus.show.mixer.topology.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Named;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Slf4j
@Service
@RequiredArgsConstructor
public class ShowMixerServiceImpl implements ShowMixerService {

    public static final String SHOW_TABLE = "ktable-shows-reduce";
    public static final String PLATFORM_TABLE = "ktable-platforms-agg";
    public static final String REVIEW_TABLE = "ktable-reviews-agg";

    private final ShowReducer showReducer;
    private final InputShowKeyMapper inputShowKeyMapper;
    private final InputPlatformKeyMapper inputPlatformKeyMapper;
    private final InputReviewKeyMapper inputReviewKeyMapper;
    private final PlatformListAggregator platformListAggregator;
    private final ReviewListAggregator reviewListAggregator;
    private final ShowPlatformListValueJoiner showPlatformListValueJoiner;
    private final ShowReviewListValueJoiner showReviewListValueJoiner;
    private final ShowFilter showFilter;
    private final PlatformListFilter platformListFilter;
    private final ReviewListFilter reviewListFilter;
    private final InputShowEventValueMapper inputShowEventValueMapper;

    @Override
    public KStream<OutputShowListKey, OutputShowListEvent> process(
            final KStream<InputShowKey, InputShowEvent> shows,
            final KStream<InputPlatformKey, InputPlatformEvent> platforms,
            final KStream<InputReviewKey, InputReviewEvent> reviews) {

        final KStream<OutputShowListKey, InputShowEvent> keyMappedShowStream = shows
                .peek((k, show) -> log.info("Show received: {}", show))
                .selectKey(inputShowKeyMapper);

        final KStream<OutputShowListKey, InputShowEvent> keyMappedShowStreamToDelete = keyMappedShowStream
                .branch((k, v) -> EventType.DELETE.equals(v.getEventType()))[0];

        final KTable<OutputShowListKey, InputShowEvent> showsTable = keyMappedShowStream
                .filter(showFilter)
                .groupByKey()
                .reduce(showReducer, Named.as(SHOW_TABLE), Materialized.as(SHOW_TABLE));

        final KTable<OutputShowListKey, PlatformListEvent> platformsTable = platforms
                .peek((k, platform) -> log.info("Platform association received: {}", platform))
                .selectKey(inputPlatformKeyMapper)
                .filter(platformListFilter)
                .groupByKey()
                .aggregate(platformListAggregator, platformListAggregator, Named.as(PLATFORM_TABLE), Materialized.as(PLATFORM_TABLE));

        final KTable<OutputShowListKey, ReviewListEvent> reviewsTable = reviews
                .peek((k, review) -> log.info("Review association received: {}", review))
                .selectKey(inputReviewKeyMapper)
                .filter(reviewListFilter)
                .groupByKey()
                .aggregate(reviewListAggregator, reviewListAggregator, Named.as(REVIEW_TABLE), Materialized.as(REVIEW_TABLE));

        //KTable<OutputShowListKey, OutputShowListEvent> x = showsTable.leftJoin(reviewsTable, showReviewListValueJoiner);

        return showsTable.leftJoin(platformsTable, showPlatformListValueJoiner)
                .leftJoin(reviewsTable, showReviewListValueJoiner)
                .toStream()
                .merge(keyMappedShowStreamToDelete.mapValues(inputShowEventValueMapper))
                .filter((k, v) -> v != null)
                .peek((k, showReviewListEvent) -> log.info("Sent message {} to output channel", showReviewListEvent));

    }


}
