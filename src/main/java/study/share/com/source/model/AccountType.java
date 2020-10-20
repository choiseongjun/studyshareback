package study.share.com.source.model;


import org.apache.commons.lang3.StringUtils;

public enum  AccountType {
    email, google, naver, kakao;

    public static AccountType ofAccountType(String name) {
        for(AccountType type : values()){
            if(StringUtils.equals(type.name(), name)) {
                return type;
            }
        }
        throw new RuntimeException();
    }


}
