package br.com.issler.azura_api.controllers;

import br.com.issler.azura_api.dtos.CreateCategoryDTO;
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
}
