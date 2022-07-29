package com.hiberus.show.api.mapper;

import com.hiberus.show.library.dto.ShowDto;
import com.hiberus.show.library.repository.Show;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShowMapper {

    @Mapping(target = "title", expression = "java(mapTitle(show))")
    ShowDto mapShow(final Show show);

    default String mapTitle(final Show show) {
        return show.getName();

    }
}
