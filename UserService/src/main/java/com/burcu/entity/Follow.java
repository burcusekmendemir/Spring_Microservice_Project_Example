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
public class Follow extends BaseEntity {

    @Id
    private String id;

    private String followingUsersId;

    private String followedUsersId;


}
