package com.smartexam.academic.dto;

//import lombok.*;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class AcademicSessionResponse {
//
//    private Long id;
//
//    private String name;
//
//    private String code;
//
//    private LocalDate startDate;
//
//    private LocalDate endDate;
//
//    private Boolean currentSession;
//
//    private String description;
//
//    private String organizationName;
//
//    private Boolean active;
//
//    private LocalDateTime createdAt;
//
//    private LocalDateTime updatedAt;
//
//}
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AcademicSessionResponse(

        Long id,

        String name,

        String code,

        LocalDate startDate,

        LocalDate endDate,

        Boolean currentSession,

        String description,

        Long organizationId,

        String organizationName,

        Boolean active,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {
}
