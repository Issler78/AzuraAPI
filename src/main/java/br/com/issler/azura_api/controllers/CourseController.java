package br.com.issler.azura_api.controllers;

import br.com.issler.azura_api.database.models.CourseEntity;
import br.com.issler.azura_api.dtos.*;
import br.com.issler.azura_api.projections.CoursesProjection;
import br.com.issler.azura_api.services.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/courses")
@RequiredArgsConstructor
@Validated
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@Valid @RequestBody CreateCourseDTO createCourseDTO) throws Exception {
        courseService.save(createCourseDTO);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<CoursesProjection> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "") String search
    ) {
        Page<CoursesProjection> coursesPage = courseService.getAll(page, size, search);

        return PageResponse.<CoursesProjection>builder()
                .content(coursesPage.getContent())
                .page(coursesPage.getNumber())
                .size(coursesPage.getSize())
                .elementsInPage(coursesPage.getNumberOfElements())
                .totalPages(coursesPage.getTotalPages())
                .totalElements(coursesPage.getTotalElements())
                .firstPage(coursesPage.isFirst())
                .lastPage(coursesPage.isLast())
                .hasPreviousPage(coursesPage.hasPrevious())
                .hasNextPage(coursesPage.hasNext())
                .build();
    }

    @DeleteMapping("/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long courseId) throws Exception {
        courseService.delete(courseId);
    }

    @PatchMapping("/{courseId}/restore")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void restore(@PathVariable Long courseId) throws Exception {
        courseService.restore(courseId);
    }

    @PatchMapping("/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public CourseResponse update(@PathVariable Long courseId, @Valid @RequestBody CourseUpdateDTO courseUpdateDTO) throws Exception {
        CourseEntity course = courseService.update(courseId, courseUpdateDTO);

        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .category(CategoryResponse.builder()
                        .id(course.getCategory().getId())
                        .name(course.getCategory().getName())
                        .build()
                )
                .build();
    }
}
