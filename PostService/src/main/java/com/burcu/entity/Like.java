package com.burcu.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Like extends BaseEntity {

    @Id
    private String id;
    private String userId;
    private String postId;
    private String commentId;
    private String username;
    private String userAvatar;
}
