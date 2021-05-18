package study.share.com.source;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableConfigurationProperties
@EnableMongoRepositories(basePackages = "study.share.com.source.mongo")
@EnableJpaRepositories(basePackages = "study.share.com.source.repository")
public class StudyshareBackApplication extends SpringBootServletInitializer{
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(StudyshareBackApplication.class);
	}
	
	@PostConstruct
	void init() {
		
		//TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:aws.yml,"
            + "classpath:application-email.yml";
	
	public static void main(String[] args) {
		SpringApplication.run(StudyshareBackApplication.class, args);
	}

}
