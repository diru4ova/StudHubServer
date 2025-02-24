package com.softserve.academy.studhub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TeacherPaginatedDTO {

    private List<TeacherForListDTO> teachers;

    private Long teachersTotalCount;
}
