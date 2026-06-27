package br.com.issler.azura_api.projections;

public interface ICoursesProjection {
    Long getCourseId();
    String getCourseTitle();
    Double getCoursePrice();
    Long getCategoryId();
    String getCategory();
}
