package study.share.com.source.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserGrade {

    NEWBIE(1,"뉴비"),
    SHORTPENCIL(2,"몽당연필"),
    LONGPENCIL(3,"연필"),
    FOUNTAINPEN(4,"만년필");

    private long id;
    private String content;

}
