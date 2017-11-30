package com.cy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SpringBootApplication
@MapperScan("com.cy.dao")
public class SpringbootMybatisThymeleafApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootMybatisThymeleafApplication.class, args);
	}

	@GetMapping(value ="/")
		public String index(){
		return "redirect:/list";
			}

}
