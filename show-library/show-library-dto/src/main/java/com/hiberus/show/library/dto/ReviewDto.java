package com.hiberus.show.library.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
@Builder
@JsonPropertyOrder(alphabetic = true)
public class ReviewDto {
    String identifier;
    String uid;
    String isan;
    int rating;
    String comment;
}
