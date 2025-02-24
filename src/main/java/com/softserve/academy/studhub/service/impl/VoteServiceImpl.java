package com.softserve.academy.studhub.service.impl;

import com.softserve.academy.studhub.constants.ErrorMessage;
import com.softserve.academy.studhub.dto.VotePostDTO;
import com.softserve.academy.studhub.entity.*;
import com.softserve.academy.studhub.repository.*;
import com.softserve.academy.studhub.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@AllArgsConstructor
@Service
public class VoteServiceImpl implements VoteService {

    private VoteRepository voteRepository;

    private UserRepository userRepository;

    private AnswerRepository answerRepository;

    private FeedbackRepository feedbackRepository;

    private QuestionRepository questionRepository;



    @Override
    public List<Vote> findByUsernameAndQuestionId(String username, Integer questionId) {
        Optional<User> userResponse = userRepository.findByUsername(username);
        Optional<Question> questionResponse = questionRepository.findById(questionId);
        if (userResponse.isPresent() && questionResponse.isPresent()) {
            return voteRepository.findByUserAndAnswer_Question(userResponse.get(), questionResponse.get());
        } else {
            return new ArrayList<>();
        }
    }


    @Transactional
    @Override
    public Vote update(VotePostDTO voteDTO, String username) {

        Optional<Vote> optionalVote = findVoteByVoteDTO(voteDTO, username);

        if (optionalVote.isPresent()) {
            Vote vote = optionalVote.get();
            if (vote.getAnswer() != null) {
                Answer answer = vote.getAnswer();
                answer.setRate(answer.getRate() - vote.getValue() + voteDTO.getValue());
                answerRepository.saveAndFlush(answer);
                vote.setAnswer(answer);
            } else if (vote.getFeedback() != null) {
                Feedback feedback = vote.getFeedback();
                feedback.setRate(feedback.getRate() - vote.getValue() + voteDTO.getValue());
                feedbackRepository.saveAndFlush(feedback);
                vote.setFeedback(feedback);
            }
            vote.setValue(voteDTO.getValue());
            return voteRepository.saveAndFlush(vote);
        } else {
            Vote vote = new Vote();
            vote.setValue(voteDTO.getValue());
            if (voteDTO.getAnswerId() != null) {
                Answer answer = answerRepository.findById(voteDTO.getAnswerId()).orElseThrow(() ->
                        new IllegalArgumentException(ErrorMessage.ANSWER_NOTFOUND)
                );
                answer.setRate(answer.getRate() + voteDTO.getValue());
                answerRepository.saveAndFlush(answer);
                vote.setAnswer(answer);
            } else if (voteDTO.getFeedbackId() != null) {
                Feedback feedback = feedbackRepository.findById(voteDTO.getFeedbackId()).orElseThrow(() ->
                        new IllegalArgumentException(ErrorMessage.FEEDBACK_NOTFOUND)
                );
                feedback.setRate(feedback.getRate() + voteDTO.getValue());
                feedbackRepository.saveAndFlush(feedback);
                vote.setFeedback(feedback);
            }
            vote.setUser(userRepository.findByUsername(username).orElseThrow(() ->
                    new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND_BY_USERNAME)
            ));
            return voteRepository.saveAndFlush(vote);
        }
    }

    private Optional<Vote> findVoteByVoteDTO(VotePostDTO voteDTO, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND_BY_USERNAME)
        );
        if (voteDTO.getAnswerId() != null) {
            Answer answer = answerRepository.findById(voteDTO.getAnswerId()).orElseThrow(() ->
                    new IllegalArgumentException(ErrorMessage.ANSWER_NOTFOUND)
            );
            return voteRepository.findByUserAndAnswer(user, answer);
        } else if (voteDTO.getFeedbackId() != null) {
            Feedback feedback = feedbackRepository.findById(voteDTO.getFeedbackId()).orElseThrow(() ->
                    new IllegalArgumentException(ErrorMessage.FEEDBACK_NOTFOUND)
            );
            return voteRepository.findByUserAndFeedback(user, feedback);
        } else {
            throw new NullPointerException(ErrorMessage.VOTE_POST_DTO_NULL_FIELDS);
        }
    }

    @Override
    public Integer getVoteSumByUsername(String username) {

        return voteRepository.getVoteSumByUsername(username);
    }
}
