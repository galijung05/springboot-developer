package me.blogSpringBoot.springbootdeveloper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RandomMessageDTO {
    private String uuid;
    private int randomNumber;
    private String randomName;
    private String randomUrl;
    private LocalDateTime currentTime;
    private String randomContent;
}