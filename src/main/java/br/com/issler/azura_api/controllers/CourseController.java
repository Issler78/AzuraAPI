package br.com.issler.azura_api.controllers;

import br.com.issler.azura_api.dtos.CreateCourseDTO;
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
    public Page<CoursesProjection> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "") String search
    ) {
        return courseService.getAll(page, size, search);
    }

    @DeleteMapping("/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long courseId) throws Exception {
        courseService.delete(courseId);
    }

}
