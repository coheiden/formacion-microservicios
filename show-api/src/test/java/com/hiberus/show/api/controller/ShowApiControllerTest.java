package com.hiberus.show.api.controller;

import com.hiberus.show.api.service.ShowApiService;
import com.hiberus.show.library.dto.ReviewDto;
import com.hiberus.show.library.dto.ShowDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShowApiControllerTest {

    @Mock
    private ShowApiService showApiService;

    @InjectMocks
    private ShowApiControllerImpl showApiController;

    @Test
    public void testRetrieveByIdNotFound() {
        when(showApiService.retrieveShowByIdentifier("1")).thenReturn(Optional.empty());
        assertThat(showApiController.retrieveShowById("1").getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);

        when(showApiService.retrieveShowByIdentifier("1")).thenReturn(Optional.of(ShowDto.builder()
                .identifier("1")
                .title("Tenet")
                .build()));
        assertThat(showApiController.retrieveShowById("1")).satisfies(r -> {
            assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(r.getBody()).isEqualTo(ShowDto.builder()
                    .identifier("1")
                    .title("Tenet")
                    .build());
        });
    }

    @Test
    public void testRetrieveAllShows() {
        when(showApiService.retrieveAllShows()).thenReturn(new ShowDto[0]);
        assertThat(showApiController.retrieveAllShows()).satisfies(r -> {
            assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(r.getBody()).isEmpty();
        });

        when(showApiService.retrieveAllShows()).thenReturn(Collections.singleton(ShowDto.builder()
                .identifier("1")
                .title("Tenet")
                .reviews(Collections.singletonList(ReviewDto.builder()
                        .comment("Good")
                        .rating(8)
                        .build()).toArray(new ReviewDto[0]))
                .build()).toArray(new ShowDto[0]));
        assertThat(showApiController.retrieveAllShows()).satisfies(r -> {
            assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(r.getBody()).hasSize(1);
        });
    }
}
