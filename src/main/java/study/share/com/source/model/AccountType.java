package study.share.com.source.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

public enum  AccountType {
    email, google, naver, kakao, none, phone_number;

    public static AccountType ofAccountType(String name) {
        for(AccountType type : values()){
            if(StringUtils.equals(type.name(), name)) {
                return type;
            }
        }
        throw new RuntimeException();
    }


    @JsonCreator
    public static AccountType fromString(String value) {
        for (AccountType type : AccountType.values()) {
            if (StringUtils.equalsAnyIgnoreCase(type.name(), value)) {
                return type;
            }
        }

        return AccountType.none;
    }


}
