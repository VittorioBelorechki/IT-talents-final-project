package com.vtube.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfiguration {
	//To convert from dto to model and vice versa
	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
}
