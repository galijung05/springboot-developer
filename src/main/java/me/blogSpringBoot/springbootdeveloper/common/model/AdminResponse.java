package me.blogSpringBoot.springbootdeveloper.common.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminResponse<T> {

    @Builder.Default
    private int status = 200;
    private T payload;
}