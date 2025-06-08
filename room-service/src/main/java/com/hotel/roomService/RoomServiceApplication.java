package com.hotel.roomService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class RoomServiceApplication {

	public static void main(String[] args) {
		log.info("Starting Room Service Application...");
		SpringApplication.run(RoomServiceApplication.class, args);
		log.info("Room Service Application started successfully.");
	}
}
