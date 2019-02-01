package com.rave.catalogoprofesores.dao;

import java.util.List;
import java.util.Observable;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.rave.catalogoprofesores.model.Course;
import com.rave.catalogoprofesores.model.SocialMedia;
import com.rave.catalogoprofesores.model.TeacherSocialMedia;


@Repository
@Transactional //hibernate para transacciones
public class SocialMediaDaoImp extends AbstractSession implements SocialMediaDao {

	@Override
	public void saveSocialMedia(SocialMedia socialMedia) {
		getSession().persist(socialMedia);
	}

	@Override
	public void deleteSocialMedia(Long idSocialMedia) {
		SocialMedia socialMedia = findById(idSocialMedia);
		if(socialMedia != null){
			getSession().delete(socialMedia);
		}
	}

	@Override
	public void updateSocialMedia(SocialMedia socialMedia) {
		getSession().update(socialMedia);
	}

	@Override
	public List<SocialMedia> findAllCourses() {
		return getSession().createQuery("from SocialMedia").list();
	}

	@Override
	public SocialMedia findById(Long idSocialMedia) {
		return (SocialMedia) getSession().get(SocialMedia.class, idSocialMedia);
	}

	@Override
	public SocialMedia finByName(String name) {
		return (SocialMedia)getSession().createQuery(
				"from SocialMedia where name = :name")
				.setParameter("name", name).uniqueResult();
	}

	@Override
	public TeacherSocialMedia finSocialMediaByIdAndName(Long idSocialMedia, String nickname) {
		List<Object[]> objects = getSession().createQuery(
				"from TeacherSocialMedia tsm join tsm.socialMedia sm "
				+ "where sm.idSocialMedia = :idSocialMedia and tsm.nickname = :nickname")
				.setParameter("idSocialMedia", idSocialMedia)
				.setParameter("nickname", nickname).list();
		
		//recorrer la lista de arreglos buscando los objetos de tipo TeacherSocialMedia y devovlerlos
		if(objects.size() > 0){
			for(Object[] objects2: objects){
				for(Object object: objects2){
					if (object instanceof TeacherSocialMedia ){
						return (TeacherSocialMedia) object;
					}
				}
			}
		}
		return null;
	}
	
}
