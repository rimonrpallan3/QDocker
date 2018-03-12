package com.voyager.qdocker;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 *
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private FirebaseAuth.AuthStateListener mAuthListener;

    public MyFirebaseInstanceIDService() {
    }
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
        //registerToken(refreshedToken);
        //sendTockenToServer(refreshedToken);
    }
    // [END refresh_token]

  /*  private void registerToken(String token) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token",token)
                .build();

        Request request = new Request.Builder()
                .url("http://10.1.1.18/sayara/user/getDrivers/")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void sendTockenToServer(String token) {
        UserDetails userDetails = new UserDetails();
        SharedPreferences sharedPreferences = getSharedPreferences(Helper.UserDetails, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Helper.FCM_ID, token);
        editor.apply();
        userDetails.setFcm(token);

        Retrofit retrofit = new ApiClient().getRetrofitClient();
        WebServices webServices = retrofit.create(WebServices.class);

        Call<UserDetails> call = webServices.updateFCMId(89,token);

        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                Log.e("FCM Update Response: ", response.body().error_msg);
            }
            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

*/
    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        // TODO: Implement this method to send token to your app server.
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    String uid = user.getUid();
                    Map<String, Object> fcm_reg_token = new HashMap<>();
                    fcm_reg_token.put("fcm_reg_token", token);
                    FirebaseDatabase.getInstance().getReference("users").child(uid).updateChildren(fcm_reg_token);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("developer");

                } else {

                }

            }
        };
    }
}