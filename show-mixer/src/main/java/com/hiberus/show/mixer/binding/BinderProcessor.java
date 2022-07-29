package com.hiberus.show.mixer.binding;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface BinderProcessor {

    String OUTPUT = "output";
    String SHOW = "shows";
    String PLATFORM = "platforms";
    String REVIEW = "reviews";

    @Input(SHOW)
    KStream<?, ?> show();

    @Input(PLATFORM)
    KStream<?, ?> platform();
    @Input(REVIEW)
    KStream<?, ?> review();

    @Output(OUTPUT)
    KStream<?, ?> output();
}
