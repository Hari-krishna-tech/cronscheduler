package com.ms.cronscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class CronschedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CronschedulerApplication.class, args);
	}

}
