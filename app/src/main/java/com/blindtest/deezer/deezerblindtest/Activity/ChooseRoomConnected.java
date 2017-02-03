package com.blindtest.deezer.deezerblindtest.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.blindtest.deezer.deezerblindtest.Adapters.ImageAdapter;
import com.blindtest.deezer.deezerblindtest.Constant.ChatConstant;
import com.blindtest.deezer.deezerblindtest.R;
import com.blindtest.deezer.deezerblindtest.Transformation.RoundBlurTransformation;
import com.blindtest.deezer.deezerblindtest.Transformation.RoundFilledBackgroundTransformation;
import com.blindtest.deezer.deezerblindtest.Utils.BlurManager;
import com.bumptech.glide.Glide;
import com.deezer.sdk.model.Album;
import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.deezer.sdk.network.request.event.RequestListener;

import java.util.List;

import static com.blindtest.deezer.deezerblindtest.Activity.MainActivity.deezerConnect;

/**
 * Created by pierre-louisbertheau on 30/01/2017.
 */

public class ChooseRoomConnected extends AppCompatActivity {

    private ImageAdapter adapter;
    private GridView publicRoom;
    private ImageView ivAvatar;

    private final static float ROUND_BLUR_MULTIPLY_FACTOR = 0.9f;
    private final static float ROUND_BLUR_FALLBACK_MULTIPLY_FACTOR = 0.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_room_connected);

        Button createRoom = (Button) findViewById(R.id.button_create_room);
        Button joinRoom = (Button) findViewById(R.id.button_join_room);
//        publicRoom = (GridView) findViewById(R.id.gv_public_room);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);

        Glide.with(this).load(ChatConstant.AVATAR_URL)
                .transform(new RoundFilledBackgroundTransformation(this, 4, this.getResources().getColor(R.color.bg_transparency)))
                .placeholder(R.drawable.cover_icon_profile)
                .into(ivAvatar);

        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });



        joinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseRoomConnected.this, BlindTestMain.class);
                startActivity(intent);
            }
        });

//        publicRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //TODO
//                Long playlist_id = ((Playlist)adapterView.getItemAtPosition(i)).getId();
//                Intent intent = new Intent(ChooseRoomConnected.this, BlindTestMain.class);
//                intent.putExtra("PLAYLIST_ID", playlist_id);
//                startActivity(intent);
//            }
//        });



    }

    public void onResume() {
        super.onResume();

//        // the request listener
//        RequestListener listener = new JsonRequestListener() {
//
//            public void onResult(Object result, Object requestId) {
////                List<Album> albums = (List<Album>) result;
//                List<Playlist> playlists = (List<Playlist>) result;
//                adapter = new ImageAdapter(ChooseRoomConnected.this, playlists);
//                publicRoom.setAdapter(adapter);
//
//                // do something with the albums
//            }
//
//            public void onUnparsedResult(String requestResponse, Object requestId) {}
//
//            public void onException(Exception e, Object requestId) {}
//        };
//
//
//        DeezerRequest request = DeezerRequestFactory.requestUserPlaylists(ChatConstant.USER_ID);
//
//        // set a requestId, that will be passed on the listener's callback methods
//        request.setId("myRequest");
//
//        // launch the request asynchronously
//        deezerConnect.requestAsync(request, listener);
    }
}
