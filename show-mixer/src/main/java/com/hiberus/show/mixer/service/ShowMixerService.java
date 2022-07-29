package com.hiberus.show.mixer.service;


import com.hiberus.show.library.topology.*;
import org.apache.kafka.streams.kstream.KStream;

public interface ShowMixerService {

    KStream<OutputShowListKey, OutputShowListEvent> process(
            final KStream<InputShowKey, InputShowEvent> shows,
            final KStream<InputPlatformKey, InputPlatformEvent> platforms,
            final KStream<InputReviewKey, InputReviewEvent> reviews);
}