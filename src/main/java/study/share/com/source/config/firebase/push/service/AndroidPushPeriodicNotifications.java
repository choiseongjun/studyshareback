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

        String sampleData[] = {"dU8K6ycgSoahyqcTuU9ONw:APA91bEp_jTDn5QXJZCwTr48-aPoJotLyl8HwDS9Dc9uMafWLEyefFs09KSpXxVYhdCwnpCppS-DmI78HhcWEvHdGCiEYQ34236b1LDx4LTU7fUHqeWjMhuncxLhwUzqf_3rv-9-MOhC"
        		,"dpo8bvUhS9GoTpJA9X70Jm:APA91bGq89fGW2yimE_RTTGpBR0L4hOupwKTKZ5eVD2tEcXR0t9FfigYAHlHU0PIuLlEq1393VPVKlI-4EPIWQWjAqE-vzetfcb0kz0UvH9iUYar7Dd3JC57KgNxAhCMQVu9fmfzLft1"
                ,"dD9Qn8jzRjeJ9C1PzmQfW1:APA91bFQgRyo3a_otkIpACMc1T1I9hfdq3pAM7V8-gEKxE3FQZP9Rnxo05pit5twVVF3pHu2ht4W-p3A1oHqH7w5BF9x43HpKxrlzpcV5xYTfuea6NyfcLRzyl5fHPlAkQMIhayohT6H"
                ,"c8eeMlnGQKmIFM06Nv_8Tq:APA91bFPRRZN4VVtieuObfj32wm4sfbC_gaeiqcyLnuQjzUroBVk8Dc3mTigS2-RghBBwxyC0XABpc9oBUydREomxeAZ6VwKOqOUbhAcAsCwjz0ep5x03mtdgYZL8G85MqyBrtuNb03Q"
                ,"fE2iCbr5QQKzFpZP_BPYRL:APA91bFuTwp8OnGmJPOfkgmkvafCTSRrWDUxSazdwEzku3gR6E7s_TlQ2BXbs7Edt0Z9zexmuuMK7Pa-HerXXj6mxISh4CVrGlDcHkW5so_dp0G16oNtlBbf1RFrxTkaN3xEuIHM4gJp"
                ,"cW-Et4VVSxmnhw6K-FaZRQ:APA91bGWE0e3c2bpFhVQzJ8wuMo7t5U8EOMpOnIBn7unCTob8UCTUOk5Lfvyay8HLmZv1lxmW-zVWdNwQXy6Xm5Zxj-17mLs3lHaWJerRfVUUg-Oou_eWfsF9f5oF294KiHdOaqHstWX"
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
        notification.put("title","안녕하세요!");
        notification.put("body","스터디인증하러 오세요~~ "+localDate.getDayOfWeek().name()+"!");

        body.put("notification", notification);

        return body.toString();
    }
}
