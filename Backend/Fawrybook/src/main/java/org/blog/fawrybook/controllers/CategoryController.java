package org.blog.fawrybook.controllers;

import lombok.RequiredArgsConstructor;
import org.blog.fawrybook.domain.dtos.CategoryDto;
import org.blog.fawrybook.domain.dtos.CreateCategoryRequestDto;
import org.blog.fawrybook.domain.entities.Category;
import org.blog.fawrybook.mappers.CategoryMapper;
import org.blog.fawrybook.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> listCategories(){
        List<CategoryDto> categories = categoryService.listCategories()
                .stream().map(categoryMapper::toDto).toList();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory
            (@RequestBody CreateCategoryRequestDto createCategoryRequestDto){
        Category categoryToCreate = categoryMapper.toEntity(createCategoryRequestDto);
        Category savedCategory= categoryService.createCategory(categoryToCreate);

        return new ResponseEntity<>(
                categoryMapper.toDto(savedCategory),
                HttpStatus.CREATED
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable UUID id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
