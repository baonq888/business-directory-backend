package com.where.business.service.category;

import com.where.business.dto.request.CategoryRequest;
import com.where.business.entity.Category;

public interface CategoryService {
    Category createCategory(CategoryRequest categoryRequest);
    Category updateCategory(Long categoryId, CategoryRequest categoryRequest);
}
