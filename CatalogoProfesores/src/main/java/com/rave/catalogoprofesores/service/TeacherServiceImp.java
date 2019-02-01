package com.rave.catalogoprofesores.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rave.catalogoprofesores.dao.TeacherDao;
import com.rave.catalogoprofesores.model.Teacher;

@Service("teacherService")
@Transactional
public class TeacherServiceImp implements TeacherService {

	@Autowired
	private TeacherDao _teacherDao;
	
	@Override
	public void saveTeacher(Teacher teacher) {
		_teacherDao.saveTeacher(teacher);
	}

	@Override
	public void deleteTeacher(Long idTeacher) {
		_teacherDao.deleteTeacher(idTeacher);
	}

	@Override
	public void updateTeacher(Teacher teacher) {
		_teacherDao.updateTeacher(teacher);
	}

	@Override
	public List<Teacher> findAllTeachers() {
		return _teacherDao.findAllTeachers();
	}

	@Override
	public Teacher findById(Long idTeacher) {
		return _teacherDao.findById(idTeacher);
	}

	@Override
	public Teacher findByName(String name) {
		return _teacherDao.findByName(name);
	}

}
