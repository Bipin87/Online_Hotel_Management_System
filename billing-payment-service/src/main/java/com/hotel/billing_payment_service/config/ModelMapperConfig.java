package com.hotel.billing_payment_service.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    // Creates and registers a ModelMapper bean for object mapping
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
