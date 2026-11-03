package com.smartexam.staff.directory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffPageResponse {

    private List<StaffResponse> content;

    private int number;

    private int size;

    private int totalPages;

    private long totalElements;

    private int numberOfElements;

    private boolean first;

    private boolean last;

    private boolean empty;
}