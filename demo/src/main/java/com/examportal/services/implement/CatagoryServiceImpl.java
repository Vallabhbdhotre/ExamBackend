package com.examportal.services.implement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.examportal.exception.DataValidationException;
import com.examportal.exception.GenericResponse;
import com.examportal.model.exam.Category;
import com.examportal.model.repo.CategoryRepository;
import com.examportal.services.CategoryService;

@Service
public class CatagoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public GenericResponse addCategory(Category category) throws Exception {
		if (category.getTitle() == null || category.getTitle().isBlank()) {
			throw new DataValidationException("please fill title");
		} else {
			category.setTitle(category.getTitle().trim());
		}

		if (categoryRepository.findByTitle(category.getTitle()).isPresent()) {
			throw new DataValidationException("Category with the same title already exists");
		}
		categoryRepository.save(category);

		return new GenericResponse(201, "Created Succesfully");

	}

	@Override
	public GenericResponse updateCategory(Category category, Long cid) {
		Optional<Category> optionalCategory = categoryRepository.findById(cid);
		if (optionalCategory.isPresent()) {
			Category presentCategory = optionalCategory.get();
			if (category.getTitle() != null && !category.getTitle().isBlank()) {
				presentCategory.setTitle(category.getTitle().trim());
			} else {
				throw new DataValidationException("Please fill a valid title ");
			}
			if (category.getDescription() != null) {
				presentCategory.setDescription(category.getDescription());
			}
			categoryRepository.save(presentCategory);
			return new GenericResponse(202, "Updated Successfully");
		} else {
			throw new DataValidationException("Category with ID " + cid + " not found");
		}
	}

	@Override
	public Page<Category> getCategories(int page, int size) {

		Pageable pageable1 = PageRequest.of(page, size, Sort.by("id").descending());

		Page<Category> category;

		category = categoryRepository.findAll(pageable1);
		return category;
	}

	@Override
	public Category getCategory(Long categoryId) {
		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
		if (optionalCategory.isEmpty()) {
			throw new DataValidationException("ID NOT FOUND");

		}

		return categoryRepository.findById(categoryId).get();
	}

	@Override
	public GenericResponse changeCategoryStatus(Long categoryId, Boolean newStatus) {
		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

		if (optionalCategory.isPresent()) {
			Category category = optionalCategory.get();
			category.setIsActive(newStatus);
			categoryRepository.save(category);

			return new GenericResponse(200, "Category status updated successfully.");
		}
    	throw new DataValidationException("Id Not Found");

	}

	@Override
	public List<Category> getActiveCategories() {
		return categoryRepository.findByIsActiveTrue();
	}

}
