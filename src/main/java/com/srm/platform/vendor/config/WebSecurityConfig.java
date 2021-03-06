package com.srm.platform.vendor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import com.srm.platform.vendor.service.AccountService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AccountService accountService;

	@Bean
	public TokenBasedRememberMeServices rememberMeServices() {
		return new TokenBasedRememberMeServices("remember-me-key", accountService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().ignoringAntMatchers("/login").and().authorizeRequests().antMatchers("/assets/**", "/**").permitAll()
				.anyRequest().authenticated().and().formLogin().loginPage("/login").failureUrl("/login?error=1")
				.permitAll().defaultSuccessUrl("/").and().logout().permitAll().logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout").deleteCookies("remember-me").deleteCookies("JSESSIONID")
				.invalidateHttpSession(true).and().rememberMe().rememberMeServices(rememberMeServices())
				.key("remember-me-key").tokenValiditySeconds(86400).and().exceptionHandling()
				.accessDeniedPage("/forbidden");

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		http.sessionManagement().sessionFixation().migrateSession();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.eraseCredentials(true).userDetailsService(accountService).passwordEncoder(passwordEncoder());
	}

}