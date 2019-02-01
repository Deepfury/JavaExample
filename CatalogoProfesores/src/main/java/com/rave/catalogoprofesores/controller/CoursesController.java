package com.rave.catalogoprofesores.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.rave.catalogoprofesores.model.Course;
import com.rave.catalogoprofesores.model.SocialMedia;
import com.rave.catalogoprofesores.service.CourseService;
import com.rave.catalogoprofesores.util.CustomErrorType;

@Controller
@RequestMapping("/v1")
public class CoursesController {

	@Autowired
	CourseService _courseService;

	// getCourses
	// GET
	@RequestMapping(value = "/courses", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Course>> getCourses(@RequestParam(value = "name", required = false) String name) {
		List<Course> courses = new ArrayList<>();
		if (name == null) {
			courses = _courseService.findAllCourses();
			if (courses.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Course>>(courses, HttpStatus.OK);
		} else {
			Course course = _courseService.finByName(name);
			if (course == null) {
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			courses.add(course);
			return new ResponseEntity<List<Course>>(courses, HttpStatus.OK);
		}
	}

	// getById
	// GET
	@RequestMapping(value = "/courses/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Course> getCourseById(@PathVariable("id") Long idCourse) {
		if (idCourse == null || idCourse <= 0) {
			return new ResponseEntity(new CustomErrorType("IdSocialMedia is required"), HttpStatus.CONFLICT);
		}
		Course course = _courseService.findById(idCourse);
		if (course == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Course>(course, HttpStatus.OK);

	}

	// createCourses
	//uriComponentBuilder->Ayuda a construir la url /v1/courses/{id}
	// POST
	@RequestMapping(value = "/courses", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> createCourse(@RequestBody Course course, UriComponentsBuilder uriComponentsBuilder) {
		if (course.getName().equals(null) || course.getName().isEmpty()) {
			return new ResponseEntity(new CustomErrorType("Course name is required"), HttpStatus.CONFLICT);
		}

		if (_courseService.finByName(course.getName()) != null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		_courseService.saveCourse(course);
		Course course2 = _courseService.finByName(course.getName());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				uriComponentsBuilder.path("/v1/courses/{id}").buildAndExpand(course2.getIdCourse()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// UpdateCourses
	// UPDATE
	@RequestMapping(value = "/courses/{id}", method = RequestMethod.PATCH, headers = "Accept=application/json")
	public ResponseEntity<?> updateCourse(@PathVariable("id") Long idCourse, @RequestBody Course course) {
		Course currentCourse = _courseService.findById(idCourse);
		if (currentCourse == null) {
			return new ResponseEntity(new CustomErrorType("IdCourse is required"), HttpStatus.CONFLICT);
		}
		currentCourse.setName(course.getName());
		currentCourse.setProject(course.getProject());
		currentCourse.setTeacher(course.getTeacher());
		currentCourse.setThemes(course.getThemes());
		_courseService.updateCourse(currentCourse);
		return new ResponseEntity<Course>(currentCourse, HttpStatus.CREATED);
	}

	// deleteCourses
	// DELETE
	@RequestMapping(value = "/courses/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<?> deleteSocialMedia(@PathVariable("id") Long idCourse) {
		if (idCourse == null || idCourse <= 0) {
			return new ResponseEntity(new CustomErrorType("IdCourse is required"), HttpStatus.CONFLICT);
		}

		Course course = _courseService.findById(idCourse);
		if (course == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		_courseService.deleteCourse(idCourse);
		return new ResponseEntity<Course>(HttpStatus.OK);
	}

}
