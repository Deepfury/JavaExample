package com.rave.catalogoprofesores.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rave.catalogoprofesores.dao.SocialMediaDao;
import com.rave.catalogoprofesores.model.SocialMedia;
import com.rave.catalogoprofesores.model.TeacherSocialMedia;


@Service("socialMediaService")
@Transactional
public class SocialMediaServiceImp implements SocialMediaService{

	@Autowired
	private SocialMediaDao _socialMediaDao;
	
	@Override
	public void saveSocialMedia(SocialMedia socialMedia) {
		_socialMediaDao.saveSocialMedia(socialMedia);
	}

	@Override
	public void deleteSocialMedia(Long idSocialMedia) {
		_socialMediaDao.deleteSocialMedia(idSocialMedia);
	}

	@Override
	public void updateSocialMedia(SocialMedia socialMedia) {
		_socialMediaDao.updateSocialMedia(socialMedia);
	}

	@Override
	public List<SocialMedia> findAllCourses() {
		return _socialMediaDao.findAllCourses();
	}

	@Override
	public SocialMedia findById(Long idSocialMedia) {
		return _socialMediaDao.findById(idSocialMedia);
	}

	@Override
	public SocialMedia finByName(String name) {
		return _socialMediaDao.finByName(name);
	}

	@Override
	public TeacherSocialMedia finSocialMediaByIdAndName(Long idSocialMedia, String nickname) {
		return _socialMediaDao.finSocialMediaByIdAndName(idSocialMedia, nickname);
	}

}
