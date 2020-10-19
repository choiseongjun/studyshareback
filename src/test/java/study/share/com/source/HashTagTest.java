package study.share.com.source;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/*해시태그 테스트..*/
public class HashTagTest {
	

	@Test
	public void extractHashTagTest() {
	    String test ="나는 어딘가에서 #테스트 포를 #가#나다#라라라$ #배$#%@ #443##fefef";
	    String test1 ="#아무개가 세미나에 참여했다.";
	    String test2 ="#아무개? 이 캐릭터는 누구냐?";
	    String test3 ="#작두#망토 어때요?";
	    String test4= "말도안돼#니가$정말#그 사람이었다니 말야##이상하군!! 정말";
	    
	 
	    Pattern p = Pattern.compile("\\#([0-9a-zA-Z가-힣]*)");
	    Matcher m = p.matcher(test4);
	    String extractHashTag = null;
	 
	    while(m.find()) {
		    extractHashTag = sepcialCharacter_replace(m.group());
		 
		    if(extractHashTag != null) {
		    	 System.out.println("최종 추출 해시태그 ::"+extractHashTag);
		    }
	    }
	}
	 
	public String sepcialCharacter_replace(String str) {
	    str = StringUtils.replaceChars(str, "-_+=!@#$%^&*()[]{}|\\;:'\"<>,.?/~`） ","");
	 
	    if(str.length() < 1) {
	    return null;
	    }
	 
	    return str;
	}
}
