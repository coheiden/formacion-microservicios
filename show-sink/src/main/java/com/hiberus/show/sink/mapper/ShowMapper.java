package com.hiberus.show.sink.mapper;

import com.hiberus.show.library.repository.Show;
import com.hiberus.show.library.topology.OutputShowListEvent;
import com.hiberus.show.library.topology.OutputShowListKey;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ShowMapper {

    @Mapping(target = "availablePlatforms", source = "value.platforms")
    Show map(final OutputShowListKey key, final OutputShowListEvent value);

    @Mapping(target = "identifier", source = "previous.identifier")
    @Mapping(target = "isan", source = "previous.isan")
    @Mapping(target = "name", source = "current.name")
    @Mapping(target = "availablePlatforms", source = "current.availablePlatforms")
    @Mapping(target = "reviews", source = "current.reviews")
    Show merge(final Show previous, final Show current);
}
