package br.com.issler.azura_api.services;

import br.com.issler.azura_api.database.models.CategoryEntity;
import br.com.issler.azura_api.database.repositories.ICategoryRepository;
import br.com.issler.azura_api.database.repositories.ICourseRepository;
import br.com.issler.azura_api.dtos.CreateCategoryDTO;
import br.com.issler.azura_api.dtos.UpdateCategoryDTO;
import br.com.issler.azura_api.exceptions.BadRequestException;
import br.com.issler.azura_api.exceptions.CategoryInUseException;
import br.com.issler.azura_api.exceptions.NotFoundException;
import br.com.issler.azura_api.mappers.ICategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ICategoryRepository categoryRepository;
    private final ICourseRepository courseRepository;
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

    public List<CategoryEntity> getAll() {
        return categoryRepository.findAllByDeletedAtIsNull();
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

    @Transactional(rollbackFor = Exception.class)
    public void delete(long categoryId) throws Exception {
        CategoryEntity category = categoryRepository.findById(categoryId).
                orElseThrow(() -> new NotFoundException("Category not found"));

        if (courseRepository.existsByCategoryIdAndDeletedAtIsNull(categoryId)) throw new CategoryInUseException("Some course have this category, so it can't be deleted");

        try {
            category.setDeletedAt(LocalDateTime.now());
        } catch (Exception e) {
            throw new Exception("Error occurred while deleting category on database");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void restore(long categoryId) throws Exception {
        CategoryEntity category = categoryRepository.findById(categoryId).
                orElseThrow(() -> new NotFoundException("Category not found"));

        try {
            category.setDeletedAt(null);
        } catch (Exception e) {
            throw new Exception("Error occurred while deleting category on database");
        }
    }
}
