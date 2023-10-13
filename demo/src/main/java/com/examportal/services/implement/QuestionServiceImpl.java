package com.examportal.services.implement;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.examportal.exception.DataValidationException;
import com.examportal.exception.GenericResponse;
import com.examportal.model.exam.CorrectOption;
import com.examportal.model.exam.Question;
import com.examportal.model.repo.QuestionRepository;
import com.examportal.services.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private QuestionRepository questionRepository;

	@Override
	public GenericResponse addQuestion(Question question) throws Exception {

		if (question.getContent().isBlank() || question.getContent() == null) {
			throw new DataValidationException("Enter a question.");
		} else {
			question.setContent(question.getContent().trim());

		}

		if (question.getOption1().isBlank() || question.getOption1() == null || question.getOption2().isBlank()
				|| question.getOption2() == null || question.getOption3().isBlank() || question.getOption3() == null
				|| question.getOption4().isBlank() || question.getOption4() == null) {
			throw new DataValidationException("Enter options for the question.");
		} else {
			question.setOption1(question.getOption1().trim());
			question.setOption2(question.getOption2().trim());
			question.setOption3(question.getOption3().trim());
			question.setOption4(question.getOption4().trim());
		}

		if (question.getCategory() == null || question.getCategory().getId() == null) {
			throw new DataValidationException("Select a category for the question.");
		}

		if (!isValidCorrectOption(question.getCorrectOption())) {
			throw new DataValidationException(
					"Invalid CorrectOption. Choose from: OPTION1, OPTION2, OPTION3, OPTION4.");
		}

		if (!questionUnique(question)) {
			throw new DataValidationException("A question with the same content already exists.");
		}

		if (!optionUnique(question)) {
			throw new DataValidationException("Options for the question should be unique.");
		}

		questionRepository.save(question);

		return new GenericResponse(201, "Created Successfully");
	}

	private Boolean questionUnique(Question question) {
		Optional<Question> questionOptional = questionRepository.findByContent(question.getContent());
		return !questionOptional.isPresent();
	}

	private Boolean optionUnique(Question question) {
		return !(question.getOption1().equals(question.getOption2())
				|| question.getOption1().equals(question.getOption3())
				|| question.getOption1().equals(question.getOption4())
				|| question.getOption2().equals(question.getOption3())
				|| question.getOption2().equals(question.getOption4())
				|| question.getOption3().equals(question.getOption4()));
	}

	private Boolean isValidCorrectOption(CorrectOption correctOption) {
		for (CorrectOption option : CorrectOption.values()) {
			if (option == correctOption) {
				return true;
			}
		}
		return false;
	}

	@Override
	public GenericResponse updateQuestion(Question question, Long questionId) {
		Optional<Question> optionalQuestion = questionRepository.findById(questionId);

		if (optionalQuestion.isEmpty()) {
			throw new DataValidationException("Question with ID " + questionId + " not found.");
		}

		Question existingQuestion = optionalQuestion.get();

		if (question.getContent().isBlank() || question.getContent() == null) {
			throw new DataValidationException("Enter a question.");
		} else {
			question.setContent(question.getContent().trim());

		}
		if (question.getOption1().isBlank() || question.getOption1() == null || question.getOption2().isBlank()
				|| question.getOption2() == null || question.getOption3().isBlank() || question.getOption3() == null
				|| question.getOption4().isBlank() || question.getOption4() == null) {
			throw new DataValidationException("Enter options for the question.");
		} else {
			question.setOption1(question.getOption1().trim());
			question.setOption2(question.getOption2().trim());
			question.setOption3(question.getOption3().trim());
			question.setOption4(question.getOption4().trim());
		}

		if (question.getCategory() == null || question.getCategory().getId() == null) {
			throw new DataValidationException("Select a category for the question.");
		}
		if (!isValidCorrectOption(question.getCorrectOption())) {
			throw new DataValidationException(
					"Invalid CorrectOption. Choose from: OPTION1, OPTION2, OPTION3, OPTION4.");
		}

		if (!questionUnique(question)) {
			throw new DataValidationException("A question with the same content already exists.");
		}

		if (!optionUnique(question)) {
			throw new DataValidationException("Options for the question should be unique.");
		}

		existingQuestion.setContent(question.getContent());
		existingQuestion.setOption1(question.getOption1());
		existingQuestion.setOption2(question.getOption2());
		existingQuestion.setOption3(question.getOption3());
		existingQuestion.setOption4(question.getOption4());
		existingQuestion.setCorrectOption(question.getCorrectOption());
		existingQuestion.setCategory(question.getCategory());

		questionRepository.save(existingQuestion);

		return new GenericResponse(202, "Updated Successfully");
	}

	@Override
	public Page<Question> getQuestions(int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		Page<Question> question;

		question = questionRepository.findAll(pageable);
		return question;
	}

	@Override
	public Question getQuestion(Long questionId) {
		Optional<Question> optionalQuestion = questionRepository.findById(questionId);

		if (optionalQuestion.isEmpty()) {
			throw new DataValidationException("Question with ID " + questionId + " not found.");
		}
		return questionRepository.findById(questionId).get();
	}

}
