package study.share.com.source.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import study.share.com.source.model.AccountType;
import study.share.com.source.model.User;
import study.share.com.source.model.VerificationToken;
import study.share.com.source.model.exception.GeneralErrorException;
import study.share.com.source.repository.VerificationTokenRepository;
import study.share.com.source.utils.MillsConstant;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

@Service
public class VerificationTokenService {

    private static long EXPIRES_MILLS = MillsConstant.ONE_DAY;
    private static long EXPIRED_MILLS = MillsConstant.ONE_HOUR;
    private static String EMAIL_VERIFY_API = "http://3.35.255.192:9090/email/verify?token=";

    @Autowired
    MailService mailService;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserService userService;

    @Autowired
    VerificationTokenService verificationTokenService;

    public void sendAuthEmail(Long userNo, String email) throws AddressException, MessagingException {

        String token = createVerificationToken(userNo, AccountType.email, email);

        //String title = messageSource.getMessage("email.auth.title", null, LocaleContextHolder.getLocale());
        String title ="이메일 주소 인증";
       // String content = messageSource.getMessage("email.auth.content", new Object[]{String.format(EMAIL_VERIFY_API, token)}, LocaleContextHolder.getLocale());
        String content = "<h2>안녕하세요</h2><p>가입해 주셔서 감사합니다! 시작하기 전에, 본인 확인을 해야 해요. 아래를 클릭해서 이메일 주소를 인증하세요:</p>";
        content+="<br /><br /><br />";
       //content+="<a href=\"{0}\" target=\"_blank\" rel=\"noreferrer noopener\">이메일 인증</a>".format("%s", EMAIL_VERIFY_API+token);
        content+="<a href=\"{0}\" target=\"_blank\" rel=\"noreferrer noopener\">이메일 인증</a>".replace("{0}", EMAIL_VERIFY_API+token);
        // TODO: 2020/12/05 Async 처리 필요( 너무 느림 .. )
        mailService.sendMail(email, title, content);
    }

    public void verify(AccountType accountType, String token) {

        // 토큰을 통해서 사용자 및 이메일 정보 가져오기
        VerificationToken verificationToken = verificationTokenRepository.findByAccountTypeAndToken(accountType, token).orElseThrow(() -> new RuntimeException("Fail! -> Cause: Invalid EmailVerificationToken."));

        if (verificationToken.isExpired()) {
            new RuntimeException("Fail! -> Cause: Invalid EmailVerificationToken.");
        }

        verificationToken.setExpriesAt(LocalDateTime.now().minusHours(1));
        // 토큰 만료
        verificationTokenRepository.save(verificationToken);


        // 사용자 가져오기
        User user = userService.findUserId(verificationToken.getUserNo()).orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));

        // 토큰 이메일과 사용자 이메일이 다른 경우
        if (!StringUtils.equals(user.getEmail(), verificationToken.getAccountId())) {
            throw new GeneralErrorException();
        }

        // TODO: 2020/12/05 사용자 테이블에 이메일 인증 컬럼 및 이메일 인증 마킹
        user.setVerified(true);
        userService.updateUser(user);

        verificationToken.setUsedYn(true);
        verificationTokenService.updateVerification(verificationToken);

    }

    private String createVerificationToken(Long userNo, AccountType accountType, String accountId) {

        LocalDateTime expiresAt = LocalDateTime.now().plusDays(1);
        VerificationToken verificationToken = verificationTokenRepository.findByUserNoAndAccountTypeAndAccountId(userNo, accountType, accountId);

        // 기존 토큰이 유효하면 재사용
        if (verificationToken != null && !verificationToken.isExpired()) {
            verificationToken.setExpriesAt(expiresAt);
            verificationTokenRepository.save(verificationToken);
            return verificationToken.getToken();
        }

        // 신규 토큰 생성
        String token = makeToken(accountType, accountId, expiresAt);
        verificationToken = makeVerificationToken(userNo, accountType, accountId, token, expiresAt);
        verificationTokenRepository.save(verificationToken);
        return token;

    }

    private VerificationToken makeVerificationToken(Long userNo, AccountType accountType, String email, String token, LocalDateTime expiresAt) {
        Date now = new Date();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setAccountId(email);
        verificationToken.setToken(token);
        verificationToken.setUserNo(userNo);
        verificationToken.setAccountType(accountType);
        verificationToken.setExpriesAt(expiresAt);
        return verificationToken;
    }

    private String makeToken(AccountType accountType, String email, LocalDateTime expiresAt) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(email).append(expiresAt).append(ThreadLocalRandom.current().nextInt()).append(accountType);
        return DigestUtils.md5Hex(stringBuilder.toString()).toUpperCase();
    }

    public void updateVerification(VerificationToken verificationToken) {
        verificationTokenRepository.save(verificationToken);
    }

    public void userIdEmailsend(String email,User user) throws AddressException, MessagingException {

        String title ="아이디 조회";
        String content = "<h2>안녕하세요 StudyShare 입니다!</h2><p>고객님의 아이디는 다음과 같습니다</p><hr>";
        content+=user.getUserid();

        mailService.sendMail(email, title, content);
    }

    public void userPwEmailsend(String email,User user) throws AddressException, MessagingException {

        String title ="비밀번호 변경";
        String content = "<h2>안녕하세요 StudyShare 입니다!</h2><p>비밀번호 변경을 위한 링크를 클릭해 주세요</p><hr>";
        content+="<a href=\"http://localhost:8080/PwFind/{0}\"" .replace("{0}",email);
        content+=">비밀번호 변경을 위한 링크</a>";
        content+="<br /><br /><br />";

        mailService.sendMail(email, title, content);
    }
}
