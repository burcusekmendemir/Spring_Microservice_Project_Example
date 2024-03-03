package com.burcu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor

public class CommentLike extends BaseEntity {

    @Id
    String id;
    String userProfileId;
    String commentId;

}
