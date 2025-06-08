package com.hotel.billing_payment_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
@Slf4j
@SpringBootApplication
@EnableFeignClients(basePackages = "com.hotel.billing_payment_service.service")
@EnableDiscoveryClient
public class BillingPaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingPaymentServiceApplication.class, args);
	}

}
