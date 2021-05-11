package study.share.com.source;

import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableConfigurationProperties
public class StudyshareBackApplication extends SpringBootServletInitializer{
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(StudyshareBackApplication.class);
	}
	
	@PostConstruct
	void init() {
		
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
	}

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:aws.yml,"
            + "classpath:application-email.yml";
	
	public static void main(String[] args) {
		SpringApplication.run(StudyshareBackApplication.class, args);
	}

}
