package com.blindtest.deezer.deezerblindtest.Activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blindtest.deezer.deezerblindtest.Adapters.HAdapter;
import com.blindtest.deezer.deezerblindtest.Adapters.MessagesAdapter;
import com.blindtest.deezer.deezerblindtest.Constant.ChatConstant;
import com.blindtest.deezer.deezerblindtest.Model.Message;
import com.blindtest.deezer.deezerblindtest.Model.Players;
import com.blindtest.deezer.deezerblindtest.R;
import com.deezer.sdk.model.Track;
import com.deezer.sdk.model.User;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.deezer.sdk.network.request.event.RequestListener;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.blindtest.deezer.deezerblindtest.Activity.MainActivity.deezerConnect;

/**
 * Created by pierre-louisbertheau on 30/01/2017.
 */

public class BlindTestMain extends AppCompatActivity {
    private static String TAG = BlindTestMain.class.getName();


    private CountDownTimer countDownTimer;
    private static int COUNTDOWN_TIMER = 30 * 1000;//30s
    private Long playlist_id;
    private int number_of_track = 0;
    private MediaPlayer mediaPlayer;

    private MessagesAdapter messagesAdapter;
    private HAdapter playerAdapter;

    private ArrayList<Message> mMessages = new ArrayList<Message>();
    private Socket mSocket;
    private String room = "";
    private List<Players> mPlayersList = new ArrayList<>();

    private ProgressBar pbCountDown;
    private TextView mTextField;

    @Bind(R.id.editMessage)
    EditText editMessage;

    @Bind(R.id.btnSendMessage)
    Button btnSendMessage;

    @Bind(R.id.recyclerView_messages)
    RecyclerView recyclerView;

    @Bind(R.id.recyclerview_players)
    RecyclerView recyclerView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        setContentView(R.layout.activity_main);
//        Intent intent = getIntent();
//        playlist_id = intent.getLongExtra("PLAYLIST_ID", 0);
        mediaPlayer = new MediaPlayer();
        mSocket = MainActivity.getSocket();
        room = "playlist/1570259211";
        mSocket.on("join", onConnect);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", ChatConstant.USER_ID);
            jsonObject.put("name", ChatConstant.USERNAME);
            jsonObject.put("avatarUrl", ChatConstant.AVATAR_URL);
            mSocket.emit("join", room, jsonObject);
        }catch (Exception e) {
            Log.e(TAG, "Exception ", e);
        }


        mSocket.on(ChatConstant.RECEIVE_START_TRACK, onStartTrack);
        mSocket.on(ChatConstant.RECEIVE_END_TRACK, onStopTrack);
        mSocket.on(ChatConstant.RECEIVE_BAD_ANSWER, onBadAnswer);
        mSocket.on(ChatConstant.RECEIVE_BAD_ANSWER_BROADCAST, onBadAnswerBroadcast);
        mSocket.on(ChatConstant.RECEIVE_GOOD_ANSWER, onGoodAnswer);
        mSocket.on(ChatConstant.RECEIVE_GOOD_ANSWER_BROADCAST, onGoodAnswerBroadcast);
        mSocket.on(ChatConstant.RECEIVE_NEWPLAYER, newPlayerMessage);
        mSocket.on(ChatConstant.RECEIVE_BROADCAST_NEWPLAYER, newPlayerMessageBroadcast);

//        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
//        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//        mSocket.on("new message", onNewMessage);
//        mSocket.on("user joined", onUserJoined);
//        mSocket.on("user left", onUserLeft);
//        mSocket.on("typing", onTyping);
//        mSocket.on("stop typing", onStopTyping);
        mSocket.connect();
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(layoutManager1);


        playerAdapter = new HAdapter(this, mPlayersList);
        recyclerView1.setAdapter(playerAdapter);





        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        messagesAdapter = new MessagesAdapter(mMessages, this);

        recyclerView.setAdapter(messagesAdapter);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                 * On vérifie que notre edittext n'est pas vide
                 */
                if (!TextUtils.isEmpty(getMessage())) {

                    // On met "true" car c'est notre message

                    Message message = new Message(getMessage(), true);

//                    String json = ChatUtils.messageToJson(message);
//                    “room”: “playlist/1245”
//                    "guess": "Jul",
//                            "trackTime": 23
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("room", room);
                        jsonObject.put("guess", getMessage());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            jsonObject.put("trackTime", mediaPlayer.getTimestamp());
                        }
                        mSocket.emit(ChatConstant.SEND_MESSAGE, jsonObject);
                    }catch (Exception e) {
                        Log.e(TAG, "Exception ", e);
                    }
                    // On envoie notre message


                    // On ajoute notre message à notre list
                    mMessages.add(message);

                    // On notifie notre adapter
                    messagesAdapter.notifyDataSetChanged();

                    scrollToBottom();

                    // On efface !
                    editMessage.setText("");
                }

            }
        });

        pbCountDown = (ProgressBar) findViewById(R.id.pb_countdown);
//        startMusic();
        mTextField = (TextView) findViewById(R.id.tv_countdown);
        countDownTimer = new CountDownTimer(COUNTDOWN_TIMER, 1000) {
            public void onTick(long millisUntilFinished) {
//                Log.d(TAG, "onTick");
                pbCountDown.setVisibility(View.VISIBLE);
                mTextField.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                pbCountDown.setVisibility(View.GONE);
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mTextField.setText("Done !");
            }
        };

    }


    private void startMusicProcess() {
        mTextField.setText("30");
        COUNTDOWN_TIMER = 30000;
        countDownTimer.start();
    }


    private Emitter.Listener onStartTrack = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {

            Log.d(TAG,"##############CALLL############# onStartTrack"+args[0]);
            BlindTestMain.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];

                    String md5 = "";
                    Long track = 0L;
                    try {
                        md5 = data.getString("md5");
                        track = data.getLong("id");

                    } catch (JSONException e) {
                        return;
                    }
                    String s = md5.substring(0);
                    Log.d(TAG , "CALLLL ############### : "+ s);
                    String finalMd5 = "http://cdn-preview-"+s+".deezer.com/stream/"+md5+"-0.mp3";



                    // add the message to view
                    addMessage("Prochaine chanson");
                    startMusicProcess();
                    startMusic(track);
                }
            });
        }


    };

    private Emitter.Listener onStopTrack = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {

            Log.d(TAG,"##############CALLL############# onStopTrack "+args[0]);
            BlindTestMain.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];
                    JSONObject answer;
                    String artist = "";
//                    String album = "";
//                    String track = "";
//                    String cover = "";

                    Long id = 0L;
                    String name = "";
                    String avatarUrl = "";
                    int score = 0;

                    List<Players> listPlayer = new ArrayList<Players>();

                    try {
                        JSONArray playersJson = data.getJSONArray("scores");
                        answer = data.getJSONObject("answer");
                        artist = answer.getString("artist");
//                        album = answer.getString("album");
//                        track = answer.getString("track");
//                        cover = answer.getString("cover");
//                        playersJson = data.getJSONArray("scores");

                        for(int i = 0 ; i < playersJson.length() ; i++) {
                            JSONObject playerObject = playersJson.getJSONObject(i);


                            id = playerObject.getLong("id");
                            name = playerObject.getString("name");
                            avatarUrl = playerObject.getString("avatarUrl");
                            score = playerObject.getInt("score");
                            Players player = new Players();
                            player.setId(id);
                            player.setName(name);
                            player.setAvatarUrl(avatarUrl);
                            player.setScore(score);
                            listPlayer.add(player);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "EXCEPTION ################# : "+e);
                    }

                    String s = "la réponse était : "+ artist.toUpperCase(Locale.FRANCE);
                    addMessage(s);

                    mPlayersList.clear();
                    mPlayersList.addAll(listPlayer);
                    playerAdapter.notifyDataSetChanged();

                    if(mediaPlayer.isPlaying()) {
                        countDownTimer.cancel();
                        mediaPlayer.pause();
                    }
                }
            });
        }


    };


    private Emitter.Listener onBadAnswer = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {

            Log.d(TAG,"##############CALLL############# onBadAnswer "+args[0]);
            BlindTestMain.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];

                    String guess = "";
                    String message;
                    try {
                        guess = data.getString("guess");
                        message = data.getString("message");

                    } catch (JSONException e) {
                        return;
                    }

//                    // add the message to view
//                    addMessage(username, message);


                    badAnswerMessage(guess+ "!!!!  "+message);
                }
            });
        }


    };

    private Emitter.Listener onBadAnswerBroadcast = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {

            Log.d(TAG,"##############CALLL############# onBadAnswerBroadcast "+args[0]);
            BlindTestMain.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];

                    Long id ;
                    String message;
                    try {
                        id = data.getLong("id");
                        message = data.getString("message");

                    } catch (JSONException e) {
                        return;
                    }

//                    // add the message to view
//                    addMessage(username, message);


                    getUser(id, false, message);
                }
            });
        }


    };

    private void badAnswerMessage(String messages) {
        Message message = new Message(
                messages,
                // On met "false" car ce n'est pas notre message
                false);
        mMessages.add(message);
        messagesAdapter.notifyDataSetChanged();
        scrollToBottom();
    }

    private void addMessage(String messages) {
        Message message = new Message(
                messages,
                // On met "false" car ce n'est pas notre message
                false);
        mMessages.add(message);
        messagesAdapter.notifyDataSetChanged();

        scrollToBottom();

    }

    @Override
    public void onStart() {
        super.onStart();
        mPlayersList.clear();
        Players me = new Players();
        me.setId(ChatConstant.USER_ID);
        me.setName(ChatConstant.USERNAME);
        me.setAvatarUrl(ChatConstant.AVATAR_URL);
        me.setScore(0);
        mPlayersList.add(me);
        playerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("join", onConnect);
//        mSocket.off(room, onUserJoined);
        mSocket.off(ChatConstant.RECEIVE_START_TRACK, onStartTrack);
        mSocket.off(ChatConstant.RECEIVE_END_TRACK, onStopTrack);
        mSocket.off(ChatConstant.RECEIVE_BAD_ANSWER, onBadAnswer);
        mSocket.off(ChatConstant.RECEIVE_BAD_ANSWER_BROADCAST, onBadAnswerBroadcast);
        mSocket.off(ChatConstant.RECEIVE_GOOD_ANSWER, onGoodAnswer);
        mSocket.off(ChatConstant.RECEIVE_GOOD_ANSWER_BROADCAST,onGoodAnswerBroadcast);
        mediaPlayer.stop();
    }

    private void startMusic(Long trackId) {
        // the request listener
        RequestListener listener = new JsonRequestListener() {

            public void onResult(Object result, Object requestId) {

                try {
                    playFromUrl(((Track)result));
                } catch (IOException e) {
                    stopPlay();
                }

            }

            public void onUnparsedResult(String requestResponse, Object requestId) {}

            public void onException(Exception e, Object requestId) {}
        };


        DeezerRequest request = DeezerRequestFactory.requestTrack(trackId);

        // set a requestId, that will be passed on the listener's callback methods
        request.setId("number_of_the_track_"+number_of_track);

        // launch the request asynchronously
        deezerConnect.requestAsync(request, listener);

    }

    private void stopPlay() {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            countDownTimer.cancel();
        }
    }


    /**
     * Scroller notre recyclerView en bas
     */
    private void scrollToBottom() {
        recyclerView.scrollToPosition(mMessages.size() - 1);
    }

    private String getMessage() {
        return editMessage.getText().toString().trim();
    }


    private void playFromUrl(Track track) throws IOException {
        String url = track.getPreviewUrl(); // your URL here
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare(); // might take long! (for buffering, etc)
        mediaPlayer.start();

    }

    private void playFromUrl(String md5) throws IOException {

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(md5);
//        mediaPlayer.prepare(); // might take long! (for buffering, etc)
        mediaPlayer.start();

    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(TAG,"##############CALLL############# onConnect "+args[0]);
            BlindTestMain.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

//                    JSONArray player = new JSONArray();
//                    int track = 0;
//                    int countdown = 0;
//                    try {
//                        player = data.getJSONArray("players");
//                        for ()
//                        track = data.getInt("track");
//                        countdown = data.getInt("countDown");
//                    } catch (JSONException e) {
//                        return;
//                    }
                    addMessage("le joueur " + "?" +" a rejoint la salle");
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG,"##############CALLL############# onDisconnect "+args[0]);
            BlindTestMain.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    isConnected = false;
//                    Toast.makeText(getActivity().getApplicationContext(),
//                            R.string.disconnect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG,"##############CALLL############# onConnectError "+args[0]);
            BlindTestMain.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(getActivity().getApplicationContext(),
//                            R.string.error_connect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(TAG,"##############CALLL############# onUserJoined "+args[0]);
            BlindTestMain.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        return;
                    }

//                    addLog(getResources().getString(R.string.message_user_joined, username));
//                    addParticipantsLog(numUsers);
                }
            });
        }
    };

    private Emitter.Listener onGoodAnswer = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(TAG,"##############CALLL############# onGoodAnswer "+args[0]);
            BlindTestMain.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    int newScore;
                    try {
                        newScore = data.getInt("newScore");
                    } catch (JSONException e) {
                        return;
                    }
                    addMessage("BONNE REPONSE, nouveau scrore : " + newScore);
                }
            });
        }
    };

    private Emitter.Listener newPlayerMessageBroadcast = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(TAG,"##############CALLL############# newPlayerMessageBroadcast "+args[0]);
            BlindTestMain.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = (JSONArray) args[0];
                    Long id = 0L;
                    String name = "";
                    String avatarUrl = "";
                    Players player = new Players();

                    try {
                        for(int i = 0; i <data.length(); i++) {

                            JSONObject playerJson = data.getJSONObject(i);

                            id = playerJson.getLong("id");
                            name = playerJson.getString("name");
                            avatarUrl = playerJson.getString("avatarUrl");

                            player.setId(id);
                            player.setName(name);
                            player.setAvatarUrl(avatarUrl);
                            player.setScore(0);
                        }
                    } catch (JSONException e) {
                        return;
                    }
                    mPlayersList.add(player);
                    playerAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    private Emitter.Listener newPlayerMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(TAG,"##############CALLL############# newPlayerMessage "+args[0]);
            BlindTestMain.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    int timeRemaining = 0;
                    List<Players> playersList = new ArrayList<Players>();
                    JSONArray playerListJson = new JSONArray();
                    Long id = 0L;
                    String name = "";
                    String avatarUrl = "";
                    int score = 0;
                    try {
                        timeRemaining = data.getInt("timeRemaining");
                        playerListJson = data.getJSONArray("players");
                        for(int i = 0 ; i < playerListJson.length() ; i++) {
                            JSONObject playerObject = playerListJson.getJSONObject(i);
                            id = playerObject.getLong("id");
                            name = playerObject.getString("name");
                            avatarUrl = playerObject.getString("avatarUrl");
                            score = playerObject.getInt("score");
                            Players player = new Players();
                            player.setId(id);
                            player.setName(name);
                            player.setAvatarUrl(avatarUrl);
                            player.setScore(score);
                            playersList.add(player);

                        }
                    } catch (JSONException e) {
                        return;
                    }
//                    mTextField.setText(timeRemaining/1000);
//                    COUNTDOWN_TIMER = timeRemaining/1000;
//                    countDownTimer.start();
                    mPlayersList.clear();
                    mPlayersList.addAll(playersList);
                    playerAdapter.notifyDataSetChanged();
                }
            });
        }

    };

    private Emitter.Listener onGoodAnswerBroadcast = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d(TAG,"##############CALLL############# onGoodAnswerBroadcast "+args[0]);
            BlindTestMain.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Long id;
                    try {
                        id = data.getLong("id");
                    } catch (JSONException e) {
                        return;
                    }
                    getUser(id, true,"");
                }
            });
        }
    };

    private void getUser(Long id, final boolean good,final String message) {
        // the request listener
        RequestListener listener = new JsonRequestListener() {

            public void onResult(Object result, Object requestId) {
                User winner = (User) result;
                if(good) {
                    theWinnerIsMessage(winner.getName() + " a bien répondu!!");
                }else {
                    badAnswerMessage(message.replace("%s",winner.getName()));
                }

            }

            public void onUnparsedResult(String requestResponse, Object requestId) {}

            public void onException(Exception e, Object requestId) {}
        };


        DeezerRequest request = DeezerRequestFactory.requestUser(id);

        // set a requestId, that will be passed on the listener's callback methods
        request.setId("number_of_the_track_"+number_of_track);

        // launch the request asynchronously
        deezerConnect.requestAsync(request, listener);
    }

    private void theWinnerIsMessage(String messages) {
        Message message = new Message(
                messages,
                // On met "false" car ce n'est pas notre message
                false);
        mMessages.add(message);
        messagesAdapter.notifyDataSetChanged();

        scrollToBottom();
    }

}