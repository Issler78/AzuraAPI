package br.com.issler.azura_api.projections;

public interface CoursesProjection {
    Long getCourseId();
    String getCourseTitle();
    Long getCategoryId();
    String getCategory();
}
