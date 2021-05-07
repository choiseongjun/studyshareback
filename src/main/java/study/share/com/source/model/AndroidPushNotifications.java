package study.share.com.source.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AndroidPushNotifications {
    public static String PeriodicNotificationJson() throws JSONException {
        LocalDate localDate = LocalDate.now();

        String sampleData[] = {"device token"
        };

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
        notification.put("title","알림 확인!");
        notification.put("body","Today is Good "+localDate.getDayOfWeek().name()+"!");

        body.put("notification", notification);

        System.out.println(body.toString());

        return body.toString();
    }
}
