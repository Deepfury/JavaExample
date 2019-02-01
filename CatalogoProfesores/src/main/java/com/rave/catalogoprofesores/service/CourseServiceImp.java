package com.rave.catalogoprofesores.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rave.catalogoprofesores.dao.CourseDao;
import com.rave.catalogoprofesores.model.Course;

@Service("courseService")
@Transactional
public class CourseServiceImp implements CourseService{

	//se trae la clase implementada del dao, esta es una inyeccion DI
	@Autowired
	private CourseDao _courseDao;
	
	@Override
	public void saveCourse(Course course) {
		_courseDao.saveCourse(course);
	}

	@Override
	public void deleteCourse(Long idCourse) {
		_courseDao.deleteCourse(idCourse);
	}

	@Override
	public void updateCourse(Course course) {
		_courseDao.updateCourse(course);
	}

	@Override
	public List<Course> findAllCourses() {
		return _courseDao.findAllCourses();
	}

	@Override
	public Course findById(Long idCourse) {
		return _courseDao.findById(idCourse);
	}

	@Override
	public Course finByName(String name) {
		return _courseDao.finByName(name);
	}

	@Override
	public List<Course> finByIdTeacher(Long idTeacher) {
		return _courseDao.finByIdTeacher(idTeacher);
	}

}
