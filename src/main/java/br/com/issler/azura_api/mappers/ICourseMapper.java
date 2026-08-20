package br.com.issler.azura_api.mappers;

import br.com.issler.azura_api.database.models.CourseEntity;
import br.com.issler.azura_api.dtos.course.requests.UpdateCourseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ICourseMapper {
    void updateCourse(UpdateCourseDTO updateCourseDTO, @MappingTarget CourseEntity courseEntity);
}
