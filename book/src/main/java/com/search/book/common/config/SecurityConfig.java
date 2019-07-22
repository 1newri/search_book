package com.search.book.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.search.book.common.auth.AuthProvider;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	AuthProvider authProvider;
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		// Resource 파일 Javascript 파일 경로 무시
		web.ignoring().antMatchers("/static/css/**, /static/js/**, *.ico");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		// 개발시에만
//		http.csrf().disable();
		
		/*
		 * http.authorizeRequests() .antMatchers("/login").permitAll()
		 * .antMatchers("/**").authenticated();
		 */
		
		http.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/home")
			.usernameParameter("id")
			.passwordParameter("password");
		
		http.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true);
		
		http.authenticationProvider(authProvider);
	}
}
