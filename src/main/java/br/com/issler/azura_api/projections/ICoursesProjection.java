package br.com.issler.azura_api.projections;

public interface ICoursesProjection {
    Long getCourseId();
    String getCourseTitle();
    Long getCategoryId();
    String getCategory();
}
