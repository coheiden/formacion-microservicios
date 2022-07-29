package com.hiberus.show.mixer.topology;

import com.hiberus.show.library.topology.InputReviewEvent;
import com.hiberus.show.library.topology.InputReviewKey;
import com.hiberus.show.library.topology.OutputShowListKey;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.springframework.stereotype.Component;

@Component
public class InputReviewKeyMapper implements KeyValueMapper<InputReviewKey, InputReviewEvent, OutputShowListKey> {

    @Override
    public OutputShowListKey apply(final InputReviewKey key, final InputReviewEvent value) {
        return OutputShowListKey.newBuilder().setIsan(value.getIsan()).build();
    }
}