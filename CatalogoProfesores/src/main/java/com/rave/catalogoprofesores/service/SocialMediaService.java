package com.rave.catalogoprofesores.service;

import java.util.List;

import com.rave.catalogoprofesores.model.SocialMedia;
import com.rave.catalogoprofesores.model.TeacherSocialMedia;

public interface SocialMediaService {
	
	void saveSocialMedia(SocialMedia socialMedia);
	void deleteSocialMedia(Long idSocialMedia);
	void updateSocialMedia(SocialMedia socialMedia);
	
	List<SocialMedia> findAllCourses();
	
	SocialMedia findById(Long idSocialMedia);
	SocialMedia finByName(String name);
	TeacherSocialMedia finSocialMediaByIdAndName(Long idSocialMedia, String nickname);
	
}
