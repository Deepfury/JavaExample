package com.rave.catalogoprofesores.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.rave.catalogoprofesores.model.Course;


@Repository
@Transactional //hibernate para transacciones
public class CourseDaoImp extends AbstractSession implements CourseDao {

	@Override
	public void saveCourse(Course course) {
		getSession().persist(course);
	}

	@Override
	public void deleteCourse(Long idCourse) {
		Course course = findById(idCourse);
		if(course != null){
			getSession().delete(course);
		}
	}

	@Override
	public void updateCourse(Course course) {
		getSession().update(course);
	}

	@Override
	public List<Course> findAllCourses() {
		return getSession().createQuery("from Course").list();
	}

	@Override
	public Course findById(Long idCourse) {
		return (Course) getSession().get(Course.class, idCourse);
	}

	@Override
	public Course finByName(String name) {
		return (Course)getSession().createQuery(
				"from Course where name = :name")
				.setParameter("name", name).uniqueResult();
	}

	@Override
	public List<Course> finByIdTeacher(Long idTeacher) {
		return (List<Course>)getSession().createQuery(
				"from Course c join c.teacher t where t.idTeacher = :idTeacher")
				.setParameter("idTeacher", idTeacher).list();
	}

}
