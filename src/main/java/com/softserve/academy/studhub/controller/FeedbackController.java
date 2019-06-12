package com.softserve.academy.studhub.controller;

import com.softserve.academy.studhub.dto.FeedbackDTO;
import com.softserve.academy.studhub.entity.Feedback;
import com.softserve.academy.studhub.service.FeedbackService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    private final ModelMapper modelMapper;

    public FeedbackController(FeedbackService feedbackService, ModelMapper modelMapper) {
        this.feedbackService = feedbackService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(path = "/teacher/{teacherId}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackByTeacher(@PathVariable Integer teacherId) {
        List<Feedback> feedbacks = feedbackService.findByTeacherId(teacherId);
        List<FeedbackDTO> feedbackDTOS = feedbacks.stream()
            .map(feedback -> modelMapper.map(feedback, FeedbackDTO.class))
            .collect(Collectors.toList());

//        List<FeedbackDTO> feedbackDTOS = new ArrayList<>();
//        for (Feedback feedback:feedbacks) {
//            feedbackDTOS.add(modelMapper.map(feedback, FeedbackDTO.class));
//        }

        return ResponseEntity.ok(feedbackDTOS);
    }

    @PostMapping(path = "/feedback")
    public ResponseEntity<FeedbackDTO> addNewFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        Feedback result = feedbackService.save(modelMapper.map(feedbackDTO, Feedback.class));
        return ResponseEntity.ok(modelMapper.map(result, FeedbackDTO.class));
    }

    @GetMapping(path = "/university/{universityId}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackByUniversuty(@PathVariable Integer universityId) {
        List<Feedback> feedbacks = feedbackService.findByUniversityId(universityId);
        List<FeedbackDTO> feedbackDTOS = feedbacks.stream()
            .map(feedback -> modelMapper.map(feedback, FeedbackDTO.class))
            .collect(Collectors.toList());

        return ResponseEntity.ok(feedbackDTOS);
    }

    }
