package com.blindtest.deezer.deezerblindtest.Model;

/**
 * Created by pierre-louisbertheau on 30/01/2017.
 */

public class Message {

    private String name;
    private String message;

    /**
     * Pour savoir si c'est notre message ou non
     */
    private boolean me;

    public Message( String message, boolean me) {

        this.message = message;
        this.me = me;
    }

    public Message(String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public boolean isMe() {
        return me;
    }

    public void setMe(boolean me) {
        this.me = me;
    }
}
