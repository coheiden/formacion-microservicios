package com.hiberus.show.library.repository;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Value
@Builder
@Document(collection = "reviews")
public class Review {
    @Id
    String identifier;
    String uid;
    String isan;
    int rating;
    String comment;
}
