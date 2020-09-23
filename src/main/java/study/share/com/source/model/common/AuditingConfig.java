package study.share.com.source.model.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
/**
* @param 
* @return
* @throws 
* @date 공통 데이트 설정  2020-09-23
* @author choiseongjun
*/
@Configuration
@EnableJpaAuditing
public class AuditingConfig {
    // That's all here for now. We'll add more auditing configurations later.
}