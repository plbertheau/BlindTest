package com.blindtest.deezer.deezerblindtest.Utils;

/**
 * Created by pierre-louisbertheau on 31/01/2017.
 */

public class BlurException extends Exception {

    public BlurException(String detailMessage) {
        super(detailMessage);
    }

    public BlurException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}

