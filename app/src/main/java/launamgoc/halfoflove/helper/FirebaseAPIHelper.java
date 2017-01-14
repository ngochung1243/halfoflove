package launamgoc.halfoflove.helper;

import org.json.JSONObject;

import java.util.Map;

import launamgoc.halfoflove.model.Message;
import launamgoc.halfoflove.model.MessageResponse;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

import static android.R.attr.id;

/**
 * Created by Admin on 1/9/2017.
 */

public interface FirebaseAPIHelper {
    @Headers({"Content-Type: application/json", "Authorization: key=AIzaSyBrK-HpEhAP8fjqReLyNSllNthg9Ckdk68"})
    @POST("/fcm/send")
    Call <MessageResponse> sendMessage(@Body Message data);
}
