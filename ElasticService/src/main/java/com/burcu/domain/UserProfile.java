package com.burcu.domain;

import com.burcu.utility.enums.EStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


import java.io.Serializable;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Document(indexName = "user-profile-index")
public class UserProfile extends BaseEntity implements Serializable {


    @Id
    private String id;
    private Long authId;
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private String address;
    private String about;


    @Builder.Default
    private EStatus status=EStatus.PENDING;
}
