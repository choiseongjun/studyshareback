package study.share.com.source.config.firebase.push.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AndroidPushPeriodicNotifications {
    public static String PeriodicNotificationJson() throws JSONException {
        LocalDate localDate = LocalDate.now();

        String sampleData[] = {"d12ltJnLQ8qmlwCXaq56hQ:APA91bE8GUQGaGO55VLtsSsJSp1pqf-5FJRHkJY2-RduBuulEMQWgulGYg9JCd0l-FGMgxo2xkaD_5W3p0T_sZFGEl2GXJO9Bq2dhNAUFSlYwgHC4ZH8dThYLr1BLl-XPeuJNVhU9K2m"
        };//디바이스키값 넣기..


        JSONObject body = new JSONObject();

        List<String> tokenlist = new ArrayList<String>();

        for(int i=0; i<sampleData.length; i++){
            tokenlist.add(sampleData[i]);
        }

        JSONArray array = new JSONArray();

        for(int i=0; i<tokenlist.size(); i++) {
            array.put(tokenlist.get(i));
        }

        body.put("registration_ids", array);

        JSONObject notification = new JSONObject();
        notification.put("title","서버에서 보내는 제목!");
        notification.put("body","서버에서 보내는 내용! "+localDate.getDayOfWeek().name()+"!");

        body.put("notification", notification);

        System.out.println(body.toString());

        return body.toString();
    }
}
