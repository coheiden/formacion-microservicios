package com.hiberus.show.mixer;

import com.hiberus.kafka.manager.KafkaManager;
import com.hiberus.show.library.topology.*;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.Date;

public class InsertDataTest {

    private static final Show SHOW = Show.FIREFLY;
    //private static final Show SHOW = Show.TENET;

    private static final Platform PLATFORM = Platform.FILMIN;
    //private static final Platform PLATFORM = Platform.HBO;

    private static final Review REVIEW= Review.GOOD;
    //private static final Review REVIEW= Review.BAD;



    private static final String SHOWS_TOPIC = "show-mixer-input-shows";
    private static final String PLATFORMS_TOPIC = "show-mixer-input-platforms";
    private static final String REVIEWS_TOPIC = "show-mixer-input-reviews";
    private static final String OUTPUT_TOPIC = "show-mixer-output";

    @Before
    public void init() {
        KafkaManager.initConsumers(OUTPUT_TOPIC);
    }

    @Test
    public void testHappyPath() {
        final Show show = Show.BRAVE;
        final Platform platform = Platform.HBO;

        addShow(show, EventType.CREATE);
        addPlatform(show, platform, EventType.CREATE);

        final ConsumerRecords<OutputShowListKey, OutputShowListEvent> records =
                KafkaManager.receiveRecords(OUTPUT_TOPIC, Duration.ofSeconds(2));

        Assert.assertEquals(1, records.count());
    }

    @Test
    public void testUpdate() {
        final Show show = Show.BRAVE;
        final Platform platform = Platform.HBO;

        addShow(show, EventType.CREATE);
        addPlatform(show, platform, EventType.CREATE);

        ConsumerRecords<OutputShowListKey, OutputShowListEvent> records =
                KafkaManager.receiveRecords(OUTPUT_TOPIC, Duration.ofSeconds(2));

        Assert.assertEquals(1, records.count());

        OutputShowListEvent event = records.iterator().next().value();

        Assert.assertEquals(show.name, event.getName().toString());

        show.name = "Brave (Updated)";

        addShow(show, EventType.UPDATE);

        records = KafkaManager.receiveRecords(OUTPUT_TOPIC, Duration.ofSeconds(2));

        Assert.assertEquals(1, records.count());

        event = records.iterator().next().value();

        Assert.assertEquals(show.name, event.getName().toString());
    }

    @Test
    public void addShow() {
        addShow(SHOW, EventType.CREATE);
    }

    @Test
    public void updateShow() {
        final Show show = SHOW;

        show.name = show.name + " (Updated)";

        addShow(SHOW, EventType.UPDATE);
    }

    @Test
    public void deleteShow() {
        addShow(SHOW, EventType.DELETE);
    }

    @Test
    public void deleteReview() {
        addReview(SHOW, REVIEW, EventType.DELETE);
    }


    @Test
    public void deletePlatform() {
        addPlatform(SHOW, PLATFORM, EventType.DELETE);
    }

    @Test
    public void addPlatform() {
        addPlatform(SHOW, PLATFORM, EventType.CREATE);
    }
    @Test
    public void addReview() {
        addReview(SHOW, REVIEW, EventType.CREATE);
    }
    private void addShow(final Show show, final EventType eventType) {
        final InputShowKey inputShowKey = InputShowKey.newBuilder()
                .setId(generateRandomId())
                .build();
        final InputShowEvent inputShowEvent = InputShowEvent.newBuilder()
                .setName(show.name)
                .setIsan(show.isan)
                .setEventType(eventType)
                .build();

        KafkaManager.sendRecord(SHOWS_TOPIC, inputShowKey, inputShowEvent);
    }

    private void addPlatform(final Show show, final Platform platform, final EventType eventType) {
        final InputPlatformKey inputPlatformKey = InputPlatformKey.newBuilder()
                .setId(generateRandomId())
                .build();
        final InputPlatformEvent inputPlatformEvent = InputPlatformEvent.newBuilder()
                .setPlatform(platform.name)
                .setIsan(show.isan)
                .setEventType(eventType)
                .build();

        KafkaManager.sendRecord(PLATFORMS_TOPIC, inputPlatformKey, inputPlatformEvent);
    }


    private void addReview(final Show show, final Review review, final EventType eventType) {
        final InputReviewKey inputReviewKey = InputReviewKey.newBuilder()
                .setId(generateRandomId())
                .build();
        final InputReviewEvent inputReviewEvent = InputReviewEvent.newBuilder()
                .setComment(review.comment)
                .setUid(review.uid)
                .setRating(review.rating)
                .setIsan(show.isan)
                .setEventType(eventType)
                .build();

        KafkaManager.sendRecord(REVIEWS_TOPIC, inputReviewKey, inputReviewEvent);
    }

    private String generateRandomId() {
        return Long.toString(new Date().getTime());
    }

    private enum Show {
        FIREFLY("Firefly", "815200861"),
        TENET("Tenet", "536464742"),
        BRAVE("Brave", "64445536");

        String name;
        final String isan;

        Show(final String name, final String isan) {
            this.name = name;
            this.isan = isan;
        }
    }

    private enum Platform {
        HBO("HBO"),
        DISNEY("Disney+"),
        FILMIN("Filmin");

        final String name;

        Platform(final String name) {
            this.name = name;
        }
    }
    private enum Review {
        GOOD("1", 10, "Impresionante"),
        BAD("2", 1, "Muy aburrida");

        final String uid;
        int rating;
        String comment;

        Review(final String uid, int rating, String comment) {
            this.uid = uid;
            this.rating = rating;
            this.comment = comment;
        }
    }
}