package com.ndobriukha.onlinemarketplace.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.ndobriukha.onlinemarketplace.dao.BidDao;
import com.ndobriukha.onlinemarketplace.dao.GenericDao;
import com.ndobriukha.onlinemarketplace.dao.ItemDao;
import com.ndobriukha.onlinemarketplace.dao.ItemDaoImpl;
import com.ndobriukha.onlinemarketplace.dao.UserDao;
import com.ndobriukha.onlinemarketplace.dao.UserDaoImpl;
import com.ndobriukha.onlinemarketplace.domain.Bid;
import com.ndobriukha.onlinemarketplace.domain.Item;
import com.ndobriukha.onlinemarketplace.domain.User;

@Configuration
@EnableWebMvc
@ComponentScan("com.ndobriukha.onlinemarketplace")
@EnableTransactionManagement
public class ApplicationContextConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**").addResourceLocations("/css/");
		registry.addResourceHandler("/js/**").addResourceLocations("/js/");
	}

	@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	@Bean(name = "viewResolver")
	public InternalResourceViewResolver getViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
		dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
		dataSource.setUsername("MARKETPLACE");
		dataSource.setPassword("marketplace");
		return dataSource;
	}

	private Properties getHibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.show_sql", "true");
		properties.put("hibernate.dialect",
				"org.hibernate.dialect.Oracle10gDialect");
		return properties;
	}

	@Autowired
	@Bean(name = "sessionFactory")
	public SessionFactory getSessionFactory(DataSource dataSource) {
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(
				dataSource);
		sessionBuilder.addProperties(getHibernateProperties());
		sessionBuilder.addAnnotatedClasses(User.class);
		sessionBuilder.addAnnotatedClasses(Item.class);
		sessionBuilder.addAnnotatedClasses(Bid.class);
		return sessionBuilder.buildSessionFactory();
	}

	@Autowired
	@Bean(name = "transactionManager")
	public HibernateTransactionManager getTransactionManager(
			SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(
				sessionFactory);
		return transactionManager;
	}

	@Autowired
	@Bean(name = "userDao")
	public UserDao<User, Long> getUserDao(SessionFactory sessionFactory) {
		return new UserDaoImpl(sessionFactory);
	}

	@Autowired
	@Bean(name = "itemDao")
	public ItemDao<Item, Long> getItemDao(SessionFactory sessionFactory) {
		return new ItemDaoImpl(sessionFactory);
	}

	@Autowired
	@Bean(name = "bidDao")
	public GenericDao<Bid, Long> getBidDao(SessionFactory sessionFactory) {
		return new BidDao(sessionFactory);
	}
}
