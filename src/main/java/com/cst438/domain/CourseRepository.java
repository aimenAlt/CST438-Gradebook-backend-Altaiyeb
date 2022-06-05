package com.cst438.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface CourseRepository extends CrudRepository <Course, Integer> {

//    Course findByCourse_id(int course_id);
}
