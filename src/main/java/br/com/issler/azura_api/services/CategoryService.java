package br.com.issler.azura_api.services;

import br.com.issler.azura_api.database.models.CategoryEntity;
import br.com.issler.azura_api.database.repositories.ICategoryRepository;
import br.com.issler.azura_api.dtos.CreateCategoryDTO;
import br.com.issler.azura_api.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ICategoryRepository categoryRepository;

    public void save(CreateCategoryDTO createCategoryDTO) throws Exception {
        CategoryEntity category = categoryRepository.findByName(createCategoryDTO.getName())
                .orElse(null);

        if (category != null) {
            throw new BadRequestException("Category already exists");
        }


        try {
            categoryRepository.save(CategoryEntity.builder()
                    .name(createCategoryDTO.getName())
                    .build());
        } catch (Exception e) {
            throw new Exception("Error occurred while saving category on database");
        }
    }
}
