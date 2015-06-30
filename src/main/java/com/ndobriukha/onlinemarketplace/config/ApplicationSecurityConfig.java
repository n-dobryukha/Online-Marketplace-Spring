package com.ndobriukha.onlinemarketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ndobriukha.onlinemarketplace.security.AuthFailureHandler;
import com.ndobriukha.onlinemarketplace.security.AuthSuccessHandler;
import com.ndobriukha.onlinemarketplace.security.CustomLogoutHandler;
import com.ndobriukha.onlinemarketplace.security.OracleAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/js/**").permitAll()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/logout").permitAll()
				.antMatchers(HttpMethod.POST, "/rest/user/").anonymous()
				.antMatchers(HttpMethod.GET, "/items/show/all", "/items/search", "/rest/item/", "/rest/item/**/bid/").hasAnyRole("USER", "ANONYMOUS")				
				.antMatchers("/items/**", "/rest/**").hasRole("USER").and()
			.formLogin()
				.loginPage("/login").usernameParameter("login").permitAll()
				.failureHandler(new AuthFailureHandler())
				.successHandler(new AuthSuccessHandler()).and()
			.logout()
				.addLogoutHandler(new CustomLogoutHandler())
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login")
				.invalidateHttpSession(true);
	}

	// установка провайдера авторизации (может быть примитивная - InMemory, или
	// на основе токенов, связанная с БД и т.д.
	// в данном случае это установка кастомного провайдера
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	// требование конфигуратора, без определения менеджера вылетает исключение;
	// базовое поведение
	@Bean
	@Override
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	// бин кастомного провайдера
	@Bean(name = "authenticationProvider")
	public AuthenticationProvider authenticationProvider() {
		return new OracleAuthenticationProvider();
	}
	
}
