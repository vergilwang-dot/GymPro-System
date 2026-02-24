package com.gym.model;
import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class Course {
    private Integer id;
    private String courseName;
    private String teacher;
    private Integer price;
    private Integer maxCapacity;
    private Integer currentCapacity;
    private String courseDay;
    private String courseTime;
    private String imgPath;
}
