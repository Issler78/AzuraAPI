package br.com.issler.azura_api.services;

import br.com.issler.azura_api.database.models.CategoryEntity;
import br.com.issler.azura_api.database.models.CourseEntity;
import br.com.issler.azura_api.database.repositories.ICategoryRepository;
import br.com.issler.azura_api.database.repositories.ICourseRepository;
import br.com.issler.azura_api.dtos.CreateCourseDTO;
import br.com.issler.azura_api.exceptions.BadRequestException;
import br.com.issler.azura_api.exceptions.NotFoundException;
import br.com.issler.azura_api.projections.CoursesProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final ICourseRepository courseRepository;
    private final ICategoryRepository categoryRepository;

    public void save(CreateCourseDTO createCourseDTO) throws Exception {
        CategoryEntity category = categoryRepository.findById(createCourseDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        CourseEntity course = courseRepository.findByTitle(createCourseDTO.getTitle()).orElse(null);
        if (course != null) {
            throw new BadRequestException("Course already exists");
        }


        try {
            courseRepository.save(CourseEntity.builder()
                    .title(createCourseDTO.getTitle())
                    .description(createCourseDTO.getDescription())
                    .category(category)
                    .build());
        } catch (Exception e) {
            throw new Exception("Error occurred while saving course on database");
        }
    }

    public Page<CoursesProjection> getAll(Integer page, Integer size, String search) {
        return courseRepository.getAllPaginate(PageRequest.of(page, size), search);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long courseId) throws Exception {
        CourseEntity course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));

        course.setDeletedAt(LocalDateTime.now());
    }
}
