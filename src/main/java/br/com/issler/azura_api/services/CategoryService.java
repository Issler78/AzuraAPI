package br.com.issler.azura_api.services;

import br.com.issler.azura_api.database.models.CategoryEntity;
import br.com.issler.azura_api.database.repositories.ICategoryRepository;
import br.com.issler.azura_api.dtos.CreateCategoryDTO;
import br.com.issler.azura_api.dtos.UpdateCategoryDTO;
import br.com.issler.azura_api.exceptions.BadRequestException;
import br.com.issler.azura_api.exceptions.NotFoundException;
import br.com.issler.azura_api.mappers.ICategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ICategoryRepository categoryRepository;
    private final ICategoryMapper categoryMapper;

    public void save(CreateCategoryDTO createCategoryDTO) throws Exception {
        CategoryEntity category = categoryRepository.findByName(createCategoryDTO.name())
                .orElse(null);

        if (category != null) {
            throw new BadRequestException("Category already exists");
        }


        try {
            categoryRepository.save(CategoryEntity.builder()
                    .name(createCategoryDTO.name())
                    .build());
        } catch (Exception e) {
            throw new Exception("Error occurred while saving category on database");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public CategoryEntity update(long categoryId, UpdateCategoryDTO updateCategoryDTO) throws Exception {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found"));


        String newName = updateCategoryDTO.name();
        if (newName != null && categoryRepository.existsByNameAndIdNot(newName, categoryId)) {
            throw new BadRequestException("Category already exists");
        }

        try {
            categoryMapper.updateCategory(updateCategoryDTO, category);
        } catch (Exception e) {
            throw new Exception("Error occurred while updating category on database");
        }

        return category;
    }
}
