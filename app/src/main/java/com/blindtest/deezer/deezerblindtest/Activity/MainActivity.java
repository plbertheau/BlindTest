package com.blindtest.deezer.deezerblindtest.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.blindtest.deezer.deezerblindtest.Constant.ChatConstant;
import com.blindtest.deezer.deezerblindtest.R;
import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.model.User;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.event.DialogListener;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.deezer.sdk.network.request.event.RequestListener;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

import static com.blindtest.deezer.deezerblindtest.Constant.ChatConstant.APPLICATION_ID;
import static com.blindtest.deezer.deezerblindtest.Constant.ChatConstant.AVATAR_URL;
import static com.blindtest.deezer.deezerblindtest.Constant.ChatConstant.USERNAME;
import static com.blindtest.deezer.deezerblindtest.Constant.ChatConstant.USER_ID;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getName();

    public static DeezerConnect deezerConnect;

    private static Socket mSocket;

    public static Socket getSocket() {
        return mSocket;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mSocket = IO.socket(ChatConstant.URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        setContentView(R.layout.connect_activity);
        // replace with your own Application ID
        deezerConnect = new DeezerConnect(this, APPLICATION_ID);

        // The set of Deezer Permissions needed by the app
        String[] permissions = new String[] {
                Permissions.BASIC_ACCESS,
                Permissions.MANAGE_LIBRARY,
                Permissions.LISTENING_HISTORY };

        // The listener for authentication events
        DialogListener listener = new DialogListener() {

            public void onComplete(Bundle values) {
                Log.d(TAG,"onComplete : "+ values);

                // the request listener
                RequestListener listener = new JsonRequestListener() {

                    public void onResult(Object result, Object requestId) {
                        User user = (User) result;
                        USER_ID = user.getId();
                        USERNAME = user.getName();
                        AVATAR_URL = user.getMediumImageUrl();

                        Intent intent = new Intent(MainActivity.this, ChooseRoomConnected.class);
                        startActivity(intent);

                        // do something with the albums
                    }

                    public void onUnparsedResult(String requestResponse, Object requestId) {}

                    public void onException(Exception e, Object requestId) {}
                };

                // create the request

                DeezerRequest requestUser = DeezerRequestFactory.requestCurrentUser();

                // set a requestId, that will be passed on the listener's callback methods
                requestUser.setId("requestUserId");

                // launch the request asynchronously
                deezerConnect.requestAsync(requestUser, listener);
            }

            public void onCancel() {
                Log.d(TAG,"onCancel");
                Intent intent = new Intent(MainActivity.this, ChooseRoomNotConnected.class);
                startActivity(intent);
            }

            public void onException(Exception e) {
                Log.d(TAG,"onExeception : "+e.getMessage());
            }
        };

        // Launches the authentication process
        deezerConnect.authorize(this, permissions, listener);

    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onStart() {
        super.onStart();
    }

}
