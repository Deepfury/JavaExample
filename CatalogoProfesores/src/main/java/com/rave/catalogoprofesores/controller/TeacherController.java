package com.rave.catalogoprofesores.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.rave.catalogoprofesores.model.Course;
import com.rave.catalogoprofesores.model.SocialMedia;
import com.rave.catalogoprofesores.model.Teacher;
import com.rave.catalogoprofesores.model.TeacherSocialMedia;
import com.rave.catalogoprofesores.service.SocialMediaService;
import com.rave.catalogoprofesores.service.TeacherService;
import com.rave.catalogoprofesores.util.CustomErrorType;

@Controller
@RequestMapping("/v1")
public class TeacherController {

	@Autowired
	private TeacherService _teacherService;
	@Autowired
	private SocialMediaService _socialMediaService;

	// getTeachers
	// GET
	@RequestMapping(value = "/teachers", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Teacher>> getTeachers(@RequestParam(value = "name", required = false) String name) {
		List<Teacher> teachers = new ArrayList<>();
		if (name == null) {
			teachers = _teacherService.findAllTeachers();
			if (teachers.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Teacher>>(teachers, HttpStatus.OK);
		} else {
			Teacher teacher = _teacherService.findByName(name);
			if (teacher == null) {
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			teachers.add(teacher);
			return new ResponseEntity<List<Teacher>>(teachers, HttpStatus.OK);
		}
	}

	// getById
	// GET
	@RequestMapping(value = "/teachers/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Teacher> getTeacherById(@PathVariable("id") Long idTeacher) {
		if (idTeacher == null || idTeacher <= 0) {
			return new ResponseEntity(new CustomErrorType("IdSocialMedia is required"), HttpStatus.CONFLICT);
		}
		Teacher teacher = _teacherService.findById(idTeacher);
		if (teacher == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Teacher>(teacher, HttpStatus.OK);

	}

	// uriComponentBuilder->Ayuda a construir la url /v1/courses/{id}
	// POST
	@RequestMapping(value = "/teachers", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> createCourse(@RequestBody Teacher teacher, UriComponentsBuilder uriComponentsBuilder) {
		if (teacher.getName().equals(null) || teacher.getName().isEmpty()) {
			return new ResponseEntity(new CustomErrorType("Teacher name is required"), HttpStatus.CONFLICT);
		}

		if (_teacherService.findByName(teacher.getName()) != null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		_teacherService.saveTeacher(teacher);
		Teacher teacher2 = _teacherService.findByName(teacher.getName());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				uriComponentsBuilder.path("/v1/teachers/{id}").buildAndExpand(teacher2.getIdTeacher()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// deleteTeacher
	// DELETE
	@RequestMapping(value = "/teachers/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<?> deleteTeacher(@PathVariable("id") Long idTeacher) {
		if (idTeacher == null || idTeacher <= 0) {
			return new ResponseEntity(new CustomErrorType("IdTeacher is required"), HttpStatus.CONFLICT);
		}

		Teacher teacher = _teacherService.findById(idTeacher);
		if (teacher == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		_teacherService.deleteTeacher(idTeacher);
		return new ResponseEntity<Course>(HttpStatus.OK);
	}
	
	
	//CREATE TEACHER IMAGE
	//ruta para definir dónde se guardan
	static final String TEACHER_UPLOADED_FOLDER = "images/teachers/";
	
	@RequestMapping(value = "/teachers/images", method = RequestMethod.POST, headers=("content-type=multipart/form-data"))
	public ResponseEntity<byte[]> uploadTeacherImage(@RequestParam("id_teacher") Long idTeacher, 
			@RequestParam("file") MultipartFile multipartFile, 
			UriComponentsBuilder uriComponentsBuilder){
		if(idTeacher == null){
			return new ResponseEntity(new CustomErrorType("Please set id_reacher"), HttpStatus.NO_CONTENT);
		}
		if(multipartFile.isEmpty()){
			return new ResponseEntity(new CustomErrorType("Please select a file to upload"), HttpStatus.NO_CONTENT);
		}
		
		Teacher teacher = _teacherService.findById(idTeacher);
		if(teacher == null){
			return new ResponseEntity(new CustomErrorType("Teacher with id_teacher: "+idTeacher+" was Not found"), 
					HttpStatus.NOT_FOUND);
		}
		if(teacher.getAvatar().isEmpty() || teacher.getAvatar() != null){
			String fileName = teacher.getAvatar();
			Path path = Paths.get(fileName);
			File file = path.toFile();
			if(file.exists()){
				file.delete();
			}
		}
		try{
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String dateName =  dateFormat.format(date);
			
			String fileName = String.valueOf(idTeacher) + "-pictureTeacher-" + dateName + "." + 
					multipartFile.getContentType().split("/")[1];
			
			teacher.setAvatar(TEACHER_UPLOADED_FOLDER + fileName);//aquí es donde guardo la ruta del avatar!
			
			byte[] bytes = multipartFile.getBytes();
			Path path = Paths.get(TEACHER_UPLOADED_FOLDER + fileName);
			Files.write(path, bytes);
			
			_teacherService.updateTeacher(teacher);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
			
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error during upload "+ multipartFile.getOriginalFilename()), HttpStatus.CONFLICT);

		}
	}//endUploadImage
	
	//GET IMAGE
	@RequestMapping(value = "/teachers/{id_teacher}/images/", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getTeacherImage(@PathVariable("id_teacher") Long idTeacher){
		if(idTeacher == null){
			return new ResponseEntity(new CustomErrorType("Please set id_reacher"), HttpStatus.NO_CONTENT);
		}
		
		Teacher teacher = _teacherService.findById(idTeacher);
		if(teacher == null){
			return new ResponseEntity(new CustomErrorType("Teacher with id_teacher: "+idTeacher+" was Not found"), 
					HttpStatus.NOT_FOUND);
		}
		
		try {
			
			String fileName = teacher.getAvatar();
			Path path = Paths.get(fileName);
			File file = path.toFile();
			if(!file.exists()){
				return new ResponseEntity(new CustomErrorType("image not found"), HttpStatus.NOT_FOUND);
			}
			
			byte[] image = Files.readAllBytes(path);
			
			//este es el que muestra la imagen
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error showing image"),HttpStatus.CONFLICT);
		}
		
	}
	//finGetTEacher IMG
	
	//DELETE TEACHER IMAGE
	@RequestMapping(value = "/teachers/{id_teacher}/images/", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<?> deleteTeacherImage(@PathVariable("id_teacher") Long idTeacher){
		if(idTeacher == null){
			return new ResponseEntity(new CustomErrorType("Please set id_reacher"), HttpStatus.NO_CONTENT);
		}
		
		Teacher teacher = _teacherService.findById(idTeacher);
		if(teacher == null){
			return new ResponseEntity(new CustomErrorType("Teacher with id_teacher: "+idTeacher+" was Not found"), 
					HttpStatus.NOT_FOUND);
		}
		
		if(teacher.getAvatar().isEmpty() || teacher.getAvatar() == null){
			return new ResponseEntity(new CustomErrorType("This teacher doesn't have a image"), HttpStatus.NOT_FOUND);
		}
		
		String fileName = teacher.getAvatar();
		Path path = Paths.get(fileName);
		File file = path.toFile();
		if(file.exists()){
			file.delete();
		}
		
		//pongo el avatar en vacío y hago el update con el service
		teacher.setAvatar("");
		_teacherService.updateTeacher(teacher);
		
		return new ResponseEntity<Teacher>(HttpStatus.NO_CONTENT);
	}

//	@RequestMapping(value = "teachers/socialMedias", method = RequestMethod.PATCH, headers = "Accept=application/json")
//	public ResponseEntity<?> assignTeacherSocialMedia(@RequestBody Teacher teacher, UriComponentsBuilder uriComponentsBuilder){
//		if(teacher.getIdTeacher() == null || teacher.getTeacherSocialMedias().size()<=0){
//			return new ResponseEntity(new CustomErrorType("We need at least id_teacher, id_social_media and nickname"), HttpStatus.NO_CONTENT);
//		}else{
//			Iterator<TeacherSocialMedia> i = teacher.getTeacherSocialMedias().iterator();
//			while(i.hasNext()){
//				TeacherSocialMedia teacherSocialMedia = i.next();
//				if(teacherSocialMedia.getSocialMedia().getIdSocialMedia() == null || teacherSocialMedia.getNickname() == null){
//					return new ResponseEntity(new CustomErrorType("We need at least id_teacher, id_social_media and nickname"), HttpStatus.NO_CONTENT);
//				}else{
//					TeacherSocialMedia tsmAux = _socialMediaService.finSocialMediaByIdAndName(
//							teacherSocialMedia.getSocialMedia().getIdSocialMedia(), 
//							teacherSocialMedia.getNickname());
//					if(tsmAux == null){
//						return new ResponseEntity(new CustomErrorType("We need at least id_teacher, id_social_media and nickname"), HttpStatus.NO_CONTENT);
//					}
//					
//					SocialMedia socialMedia = _socialMediaService.findById(teacherSocialMedia.getSocialMedia().getIdSocialMedia());
//					
//				}
//			}
//		}
//		
//		Teacher teacherSaved = _teacherService.findById(teacher.getIdTeacher());
//		if(teacher == null){
//			return new ResponseEntity(new CustomErrorType("Teacher with id_teacher: "+teacher.getIdTeacher()+" was Not found"), 
//					HttpStatus.NOT_FOUND);
//		}
//		
//		
//	}
}
