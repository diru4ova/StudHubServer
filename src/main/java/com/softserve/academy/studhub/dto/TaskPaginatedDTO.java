package com.softserve.academy.studhub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TaskPaginatedDTO {

    private List<TaskDTO> tasks;

    private Long tasksTotalCount;

}
