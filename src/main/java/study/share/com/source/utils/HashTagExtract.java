package study.share.com.source.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class HashTagExtract {
	
	public List<String> extractHashTagTest(String testname) {
	 
	    Pattern p = Pattern.compile("\\#([0-9a-zA-Z가-힣]*)");
	    Matcher m = p.matcher(testname);
	    String extractHashTag = null;
	    List<String> lists = new ArrayList<String>();
	    while(m.find()) {
		    extractHashTag = sepcialCharacter_replace(m.group());
		 
		    if(extractHashTag != null) {
		    	 lists.add(extractHashTag);
		    	 
		    }
	    }
		return lists;
	}
	 
	public String sepcialCharacter_replace(String str) {
		str = StringUtils.replaceChars(str, "-_+=!@#$%^&*()[]{}|\\;:'\"<>,.?/~`） ","");
	 
	    if(str.length() < 1) {
	    	return null;
	    }
	 
	    return str;
	}
}
