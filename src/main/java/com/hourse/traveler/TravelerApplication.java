package com.hourse.traveler;

import javax.servlet.http.HttpServlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hourse.traveler.controller.HourseTravelerServlet;
import com.hourse.traveler.model.Point;
import com.hourse.traveler.service.HourseTraveler;
import com.hourse.traveler.service.PointConverter;
import com.hourse.traveler.service.Traveler;

@EnableWebMvc
@SpringBootApplication
@EnableAspectJAutoProxy
public class TravelerApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(TravelerApplication.class, args);
	}
	
	@Bean
	public Converter<String, Point> excelPointConvertor() {
		return PointConverter.EXCEL_FORMAT;
	}
	
	@Bean
	public Traveler horseTravaler() {
		return new HourseTraveler();
	}
	
	@Bean
	public ServletRegistrationBean<HttpServlet> hourseTravelerServlet() {
		var servletRegBean = new ServletRegistrationBean<HttpServlet>();
		servletRegBean.setServlet(new HourseTravelerServlet(horseTravaler()));
		servletRegBean.addUrlMappings("/hourse/servlet/count");
		servletRegBean.setLoadOnStartup(1);
		return servletRegBean;
	}
	
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(this.excelPointConvertor());
	}
}
