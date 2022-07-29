package com.hiberus.show.mixer.topology;

import com.hiberus.show.library.topology.InputPlatformEvent;
import com.hiberus.show.library.topology.InputPlatformKey;
import com.hiberus.show.library.topology.OutputShowListKey;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.springframework.stereotype.Component;

@Component
public class InputPlatformKeyMapper implements KeyValueMapper<InputPlatformKey, InputPlatformEvent, OutputShowListKey> {

    @Override
    public OutputShowListKey apply(final InputPlatformKey key, final InputPlatformEvent value) {
        return OutputShowListKey.newBuilder().setIsan(value.getIsan()).build();
    }
}
