package study.share.com.source.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReportConstant {

    INAPPROPRIATE(0,"게시판 성격에 부적절함"),
    ABOMINATION(1,"낚시/혐오/도배"),
    ADVERTISEMENT(2,"상업적 광고 및 판매"),
    BAD_LANGUAGE(3,"욕설/비하"),
    ETC(4,"기타 사유");

    private long id;
    private String content;
}
