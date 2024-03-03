package com.burcu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CreatePostRequestDto {

    private String token;
    private String title;
    private String content;
    private List<String> mediaUrls;
}
