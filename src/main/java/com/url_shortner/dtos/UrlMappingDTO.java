package com.url_shortner.dtos;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UrlMappingDTO {

    private long id;
    private String originalUrl;
    private String shortUrl;
    private int clickCount;
    private LocalDateTime createdDate;
    private String username;

}
