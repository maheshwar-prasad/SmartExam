package com.smartexam.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchRequest {

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    @Builder.Default
    private String sortBy = "firstName";

    @Builder.Default
    private String direction = "ASC";

    private String keyword;

    private Long organizationId;

    private Long roleId;

    private Boolean active;

}