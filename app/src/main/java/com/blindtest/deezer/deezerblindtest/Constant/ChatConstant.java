package com.blindtest.deezer.deezerblindtest.Constant;

/**
 * Created by pierre-louisbertheau on 30/01/2017.
 */

public class ChatConstant {

    public static Long USER_ID;

    public static String USERNAME;

    public static String AVATAR_URL;

    public static final String TAG_NAME = "name";

    public static final String TAG_MESSAGE = "message";

//    public static final String URL = "http://172.16.3.165:3001";
    public static final String URL = "http://172.16.6.133:3000";
//    public static final String URL = "http://172.16.5.47:3000";

    public static final String APPLICATION_ID = "130151";

    public static final String SEND_MESSAGE = "ClientGuessMessage";

    public static final String RECEIVE_START_TRACK = "StartTrackMessage";

    public static final String RECEIVE_END_TRACK = "EndOfTrackMessage";

    public static final String RECEIVE_BAD_ANSWER = "ServerBadAnswerMessage";

    public static final String RECEIVE_BAD_ANSWER_BROADCAST = "ServerBadAnswerBroadcast";

    public static final String RECEIVE_GOOD_ANSWER = "ServerGoodAnswerMessage";

    public static final String RECEIVE_GOOD_ANSWER_BROADCAST = "ServerGoodAnswerBroadcast";

    public static final String RECEIVE_BROADCAST_NEWPLAYER = "NewPlayerBroadcast";

    public static final String RECEIVE_NEWPLAYER = "NewPlayerMessage";

    class State {
        public static final int ONGOING = 1;
        public static final int TRACK_START = 2;
        public static final int TRACK_END = 3;
        public static final int OTHER = 0;
    }

    class Type {
        public static final int FLOW = 1;
        public static final int CURATED = 2;
        public static final int OTHER = 0;
    }

    class Mode {

        public static final int NORMAL = 1;//(le jeu continue si qqn trouve)
        public static final int CONTRE_LA_MONTRE = 2;
    }


}
