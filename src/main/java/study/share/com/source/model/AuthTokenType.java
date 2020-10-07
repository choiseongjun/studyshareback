package study.share.com.source.model;

import org.springframework.util.StringUtils;

public enum AuthTokenType {
    accessToken(1, 86400),
    refreshToken(2, 2592000);

    private int id;
    private int expiresIn;

    public int getId() {
        return id;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    AuthTokenType(int id, int expiresIn) {
        this.id = id;
        this.expiresIn = expiresIn;
    }

    public static AuthTokenType convert(String tokenType) {
        for (AuthTokenType type : values()) {
            if (StringUtils.endsWithIgnoreCase(tokenType, type.name())) {
                return type;
            }
        }
        throw new RuntimeException();
    }

}
