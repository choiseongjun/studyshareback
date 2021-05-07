package study.share.com.source.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import study.share.com.source.model.AndroidPushNotifications;
import study.share.com.source.service.AndroidPushNotificationsService;

@RestController
public class AndroidPushController {
    private static final Logger logger = LoggerFactory.getLogger(AndroidPushController.class);

    @Autowired
    AndroidPushNotificationsService androidPushNotificationsService;

    @GetMapping(value = "/send")
    public ResponseEntity<String> send() throws JSONException {

        String notifications = AndroidPushNotifications.PeriodicNotificationJson();

        HttpEntity<String> request = new HttpEntity<>(notifications);

        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try{
            String firebaseResponse = pushNotification.get();
            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        }
        catch (InterruptedException e){
            logger.debug("got interrupted!");
        }
        catch (ExecutionException e){
            logger.debug("execution error!");
        }


        return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
    }
}
