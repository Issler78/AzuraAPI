package br.com.issler.azura_api.mappers;

import br.com.issler.azura_api.database.models.CategoryEntity;
import br.com.issler.azura_api.dtos.UpdateCategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ICategoryMapper {
    void updateCategory(UpdateCategoryDTO updateCategoryDTO, @MappingTarget CategoryEntity category);
}
