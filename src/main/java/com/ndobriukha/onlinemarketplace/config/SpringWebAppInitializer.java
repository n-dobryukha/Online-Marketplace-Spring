package com.ndobriukha.onlinemarketplace.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class SpringWebAppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
		appContext.register(ApplicationContextConfig.class);
		servletContext.addListener(new ContextLoaderListener(appContext));
		// appContext.register(ApplicationSecurityConfig.class);

		ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
				"SpringDispatcher", new DispatcherServlet(appContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
		
		registerCharachterEncodingFilter(servletContext);
        registerHiddenFieldFilter(servletContext);
	}
	
	private void registerCharachterEncodingFilter(ServletContext aContext) {
        CharacterEncodingFilter cef = new CharacterEncodingFilter();
        cef.setForceEncoding(true);
        cef.setEncoding("UTF-8");
        aContext.addFilter("charachterEncodingFilter", cef).addMappingForUrlPatterns(null ,true, "/*");
    }
    private void registerHiddenFieldFilter(ServletContext aContext) {
        aContext.addFilter("hiddenHttpMethodFilter", new HiddenHttpMethodFilter()).addMappingForUrlPatterns(null ,true, "/*"); 
    }

}