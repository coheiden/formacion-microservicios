package com.hiberus.show.mixer.topology;

import com.hiberus.show.library.topology.InputShowEvent;
import com.hiberus.show.library.topology.InputShowKey;
import com.hiberus.show.library.topology.OutputShowListKey;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.springframework.stereotype.Component;

@Component
public class InputShowKeyMapper implements KeyValueMapper<InputShowKey, InputShowEvent, OutputShowListKey> {

    @Override
    public OutputShowListKey apply(final InputShowKey key, final InputShowEvent value) {
        return OutputShowListKey.newBuilder().setIsan(value.getIsan()).build();
    }
}
