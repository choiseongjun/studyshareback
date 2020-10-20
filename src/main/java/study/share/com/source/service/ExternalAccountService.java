package study.share.com.source.service;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import study.share.com.source.model.AccountType;
import study.share.com.source.model.ExternalAccount;
import study.share.com.source.model.ExternalAccountPK;
import study.share.com.source.model.User;
import study.share.com.source.model.exception.GeneralErrorException;
import study.share.com.source.repository.ExternalAccountRepository;
import study.share.com.source.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Service
@Transactional
public class ExternalAccountService {


    private static final String GET_GOOGLE_PROFILE_URL = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=%s";
    RestTemplate restTemplate;

    @PostConstruct
    public void init() {

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(); factory.setReadTimeout(5000); // 읽기시간초과, ms
        factory.setConnectTimeout(3000); // 연결시간초과, ms
        HttpClient httpClient = HttpClientBuilder.create() .setMaxConnTotal(100) // connection pool 적용
        .setMaxConnPerRoute(5) // connection pool 적용
        .build();
        factory.setHttpClient(httpClient); // 동기실행에 사용될 HttpClient 세팅

        restTemplate = new RestTemplate(factory);
    }

    @Autowired
    UserRepository userRepository;

    @Autowired
    ExternalAccountRepository externalAccountRepository;

    public User authentcateUser(AccountType accountType, String accessToken) {
        
        String accountId = getId(accountType, accessToken);
        String userId = externalAccountRepository.findUserIdByAccountTypeAndAccountId(accountType.name(), accountId);
        if (StringUtils.isEmpty(userId)) {
            throw new GeneralErrorException();
        }
        User user = userRepository.findByUserid(userId).orElseThrow(() -> new GeneralErrorException());

        return user;
    }





    private String getId(AccountType accountType, String accessToken) {

        String id = null;
        switch (accountType) {
            case google:
                String url = String.format(GET_GOOGLE_PROFILE_URL, accessToken);
                HashMap<String, Object> result = restTemplate.getForObject(url, HashMap.class);

                if(result == null || StringUtils.isEmpty(result.get("sub"))) {
                    throw new GeneralErrorException();
                }
                id = (String) result.get("sub");
            case email:
            case kakao:
            case naver:
        }

        return id;
    }

    public void connect(User user, AccountType accountType, String accessToken) {

        String accountId = getId(accountType, accessToken);
        String userId = externalAccountRepository.findUserIdByAccountTypeAndAccountId(accountType.name(), accountId);
        if (!StringUtils.isEmpty(userId)) {
            throw new GeneralErrorException();
        }

        ExternalAccount externalAccount = new ExternalAccount();
        externalAccount.setUserId(user.getUserid());
        externalAccount.setAccountType(accountType);
        externalAccount.setAccountId(accountId);

        externalAccountRepository.save(externalAccount);
    }


    public void disconnect(User user, AccountType accountType) {
        ExternalAccountPK externalAccountPK = new ExternalAccountPK();
        externalAccountPK.setUserId(user.getUserid());
        externalAccountPK.setAccountType(accountType);
        externalAccountRepository.deleteById(externalAccountPK);
    }
}
