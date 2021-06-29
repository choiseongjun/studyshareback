package study.share.com.source.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserPoint {

    NEWBIE(10,"뉴비"),
    SHORTPENCIL(50,"몽당연필"),
    LONGPENCIL(150,"연필"),
    FOUNTAINPEN(500,"만년필");

    private long id;
    private String content;

}
