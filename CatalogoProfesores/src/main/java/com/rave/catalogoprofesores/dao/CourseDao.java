package com.rave.catalogoprofesores.dao;

import java.util.List;

import com.rave.catalogoprofesores.model.Course;

public interface CourseDao {
	
	void saveCourse(Course course);
	void deleteCourse(Long idCourse);
	void updateCourse(Course course);
	
	List<Course> findAllCourses();
	
	Course findById(Long idCourse);
	Course finByName(String name);
	List<Course> finByIdTeacher(Long idTeacher);
	
}
