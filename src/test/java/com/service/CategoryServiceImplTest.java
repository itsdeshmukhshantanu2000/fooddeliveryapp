package com.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dto.CategoryDTO;
import com.entity.Category;
import com.repository.CategoryRepository;
import com.serviceimpl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    public void testAddCategory() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(1);
        categoryDTO.setCategoryName("Test Category");

        when(categoryRepository.save(any(Category.class))).thenReturn(new Category());

        CategoryDTO result = categoryService.addCategory(categoryDTO);

        assertNotNull(result);
        assertEquals(1, result.getCategoryId());
        assertEquals("Test Category", result.getCategoryName());
    }

    @Test
    public void testUpdateCategory() {
        Category category = new Category();
        category.setCategoryId(1);
        category.setCategoryName("Test Category");

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("Updated Category");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        String result = categoryService.updateCategory(1, categoryDTO);

        assertEquals("Updated Successfully", result);
        assertEquals("Updated Category", category.getCategoryName());
    }

    @Test
    public void testRemoveCategory() {
        doNothing().when(categoryRepository).deleteById(1);

        String result = categoryService.removeCategory(1);

        assertEquals("Deleted Successfully", result);
    }

    @Test
    public void testSearchCategoryByName() {
        Category category = new Category();
        category.setCategoryId(1);
        category.setCategoryName("Test Category");

        when(categoryRepository.findByCategoryName("Test Category")).thenReturn(category);

        CategoryDTO result = categoryService.searchCategoryByName("Test Category");

        assertNotNull(result);
        assertEquals(1, result.getCategoryId());
        assertEquals("Test Category", result.getCategoryName());
    }

    @Test
    public void testSearchCategoryById() {
        Category category = new Category();
        category.setCategoryId(1);
        category.setCategoryName("Test Category");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        CategoryDTO result = categoryService.searchCategoryById(1);

        assertNotNull(result);
        assertEquals(1, result.getCategoryId());
        assertEquals("Test Category", result.getCategoryName());
    }

    @Test
    public void testFindAllCategories() {
        List<Category> categories = new ArrayList<>();
        Category category1 = new Category();
        category1.setCategoryId(1);
        category1.setCategoryName("Category 1");
        categories.add(category1);

        Category category2 = new Category();
        category2.setCategoryId(2);
        category2.setCategoryName("Category 2");
        categories.add(category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryDTO> result = categoryService.findAllCategories();

        assertNotNull(result);
        assertEquals(2, result.size());

        CategoryDTO categoryDTO1 = result.get(0);
        assertEquals(1, categoryDTO1.getCategoryId());
        assertEquals("Category 1", categoryDTO1.getCategoryName());

        CategoryDTO categoryDTO2 = result.get(1);
        assertEquals(2, categoryDTO2.getCategoryId());
        assertEquals("Category 2", categoryDTO2.getCategoryName());
    }
}

