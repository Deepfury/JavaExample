package com.rave.catalogoprofesores.dao;

import java.util.List;

import com.rave.catalogoprofesores.model.Teacher;



public interface TeacherDao {
	
	void saveTeacher(Teacher teacher);
	void deleteTeacher(Long idTeacher);
	void updateTeacher(Teacher teacher);
	
	List<Teacher> findAllTeachers();
	Teacher findById(Long idTeacher);
	Teacher findByName(String name);

}

