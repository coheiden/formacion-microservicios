package com.hiberus.show.mixer.topology;

import com.hiberus.show.library.topology.EventType;
import com.hiberus.show.library.topology.InputPlatformEvent;
import com.hiberus.show.library.topology.OutputShowListKey;
import com.hiberus.show.library.topology.PlatformListEvent;
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
public class PlatformListFilter implements Predicate<OutputShowListKey, InputPlatformEvent> {

    private final InteractiveQueryService queryService;

    private ReadOnlyKeyValueStore<OutputShowListKey, PlatformListEvent> platformListStore;

    @Override
    public boolean test(final OutputShowListKey key, final InputPlatformEvent current) {
        final boolean passes;
        final PlatformListEvent previous = platformListStore().get(key);

        if (EventType.DELETE.equals(current.getEventType())) {
            passes = true;
        } else {
            current.setEventType(EventType.CREATE);
            passes = previous == null || !previous.getPlatforms().contains(current.getPlatform());
        }

        log.info("Previous Already entered: {}", previous);
        log.info("New to add: {}", current);
        log.info("Platform is repeated: {}", !passes);

        return passes;
    }

    private ReadOnlyKeyValueStore<OutputShowListKey, PlatformListEvent> platformListStore() {
        if (platformListStore == null) {
            platformListStore = queryService.getQueryableStore(ShowMixerServiceImpl.PLATFORM_TABLE, QueryableStoreTypes.keyValueStore());
        }

        return platformListStore;
    }
}
