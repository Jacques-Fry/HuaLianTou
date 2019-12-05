package com.transo.hualiantou;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;


@MapperScan("com.transo.hualiantou.mapper")
@SpringBootApplication
public class HualiantouPojoApplication /*extends SpringBootServletInitializer */{

	public static void main(String[] args) {
		SpringApplication.run(HualiantouPojoApplication.class, args);
	}


	/*@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// 注意这里要指向原先用main方法执行的Application启动类
		return builder.sources(HualiantouPojoApplication.class);
	}*/

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return (container -> {
			ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/index.html");
			ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/index.html");
			ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/index.html");
			container.addErrorPages(error401Page, error404Page, error500Page);
		});
	}


}
