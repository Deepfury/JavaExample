package com.rave.catalogoprofesores.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;


//Clase para la conexion con la BD

@Configuration 
@EnableTransactionManagement //Como es conexion a BD, aydua con las transacciones
public class DataBaseConfiguration {
	
	@Bean
	public LocalSessionFactoryBean sessionFactory(){
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource());
		//dodne se hace el mapeo de las clases
		sessionFactoryBean.setPackagesToScan("com.rave.catalogoprofesores.model");
		sessionFactoryBean.setHibernateProperties(hibernateProperties());
		
		return sessionFactoryBean;
	}
	
	//configura los datos de la conexion a la BD 
	@Bean
	public DataSource dataSource(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/platziprofesores");
		dataSource.setUsername("platziprofesores");
		dataSource.setPassword("platziprofesores");
		
		return dataSource();
	}
	
	//propiedades del hibernate, no hace falta que sea un bean
	public Properties hibernateProperties(){
		Properties properties = new Properties();
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		properties.put("show_sql", "true");
		
		return properties;
	}
	
	//Transacciones con la BD con hibernate
	@Bean
	@Autowired //porq lo estoy usando con un objeto que ya esta persistido "bean"
	public HibernateTransactionManager transactionManager(){
		HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
		hibernateTransactionManager.setSessionFactory(sessionFactory().getObject());
		
		return hibernateTransactionManager;
	}
}
