package br.com.issler.azura_api.controllers;

import br.com.issler.azura_api.database.models.CategoryEntity;
import br.com.issler.azura_api.dtos.CategoryResponse;
import br.com.issler.azura_api.dtos.CreateCategoryDTO;
import br.com.issler.azura_api.dtos.UpdateCategoryDTO;
import br.com.issler.azura_api.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/category")
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@Valid @RequestBody CreateCategoryDTO categoryDTO) throws Exception {
        categoryService.save(categoryDTO);
    }

    @PatchMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse update(@PathVariable Long categoryId, @Valid @RequestBody UpdateCategoryDTO updateCategoryDTO) throws Exception {
        CategoryEntity category = categoryService.update(categoryId, updateCategoryDTO);

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long categoryId) throws Exception {
        categoryService.delete(categoryId);
    }

    @PatchMapping("/{categoryId}/restore")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void restore(@PathVariable Long categoryId) throws Exception {
        categoryService.restore(categoryId);
    }
}
