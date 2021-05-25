package study.share.com.source.config.firebase.push.service;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// firebase_server_key = firebase project > cloud messaging > server key

@Service
public class AndroidPushNotificationsService {
    private static final String firebase_server_key="AAAAyyfHZX4:APA91bHwOptNuaVzlQxyDJ6IzBu0OXIoPIhKY0Ylo5xET8S0JfPZtUfbgM0ysFu7fRfR9dhErjwU3HwvXmpSRiAzeJD0VQtkWhp_sDUIZe2MfhjP2kDA8-g5urDFwYKs0t23rtotCP8w";
    private static final String firebase_api_url="https://fcm.googleapis.com/fcm/send";

    @Async 
    public CompletableFuture<String> send(HttpEntity<String> entity) {

        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

        interceptors.add(new HeaderRequestInterceptor("Authorization",  "key=" + firebase_server_key));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json; UTF-8 "));
        restTemplate.setInterceptors(interceptors);
       
        String firebaseResponse = restTemplate.postForObject(firebase_api_url, entity, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }
}