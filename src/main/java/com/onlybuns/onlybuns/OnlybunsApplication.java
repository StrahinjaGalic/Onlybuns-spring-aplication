package com.onlybuns.onlybuns;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import com.onlybuns.onlybuns.Service.ImageService;
import com.onlybuns.onlybuns.Service.PostService;

@SpringBootApplication
@EnableCaching
public class OnlybunsApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(OnlybunsApplication.class, args);

	}


}
