package com.where.business.service.category;

import com.where.business.dto.request.CategoryRequest;
import com.where.business.entity.Category;
import com.where.business.exception.CategoryException;
import com.where.business.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryRequest categoryRequest) {
        Category category = new Category(categoryRequest.getName());
        log.info(category.getName());
        return categoryRepository.save(category);
//        try {
//            Category category = new Category(categoryRequest.getName());
//            return categoryRepository.save(category);
//        } catch (IllegalArgumentException e) {
//            log.error("Validation error: {}", e.getMessage(), e);
//            throw new CategoryException(e.getMessage(), e);
//
//        } catch (Exception e) {
//            log.error("Error occurred while creating category: {}", e.getMessage(), e);
//            throw new CategoryException("Failed to create category", e);
//        }
    }

    @Override
    public Category updateCategory(Long categoryId, CategoryRequest categoryRequest) {
        try {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
            category.setName(categoryRequest.getName());
            return categoryRepository.save(category);
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage(), e);
            throw new CategoryException(e.getMessage(), e);

        } catch (Exception e) {
            log.error("Error occurred while updating category: {}", e.getMessage(), e);
            throw new CategoryException("Failed to update category", e);
        }
    }
}
