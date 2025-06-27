package com.gotrid.trid.api.review.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Short rating;
    private String comment;
}