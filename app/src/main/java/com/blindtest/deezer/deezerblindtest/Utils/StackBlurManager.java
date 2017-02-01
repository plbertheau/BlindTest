package com.blindtest.deezer.deezerblindtest.Utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pierre-louisbertheau on 31/01/2017.
 */

public class StackBlurManager {

    static final int EXECUTOR_THREADS = Runtime.getRuntime().availableProcessors();
    static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(EXECUTOR_THREADS);
    private static final String TAG = StackBlurManager.class.getSimpleName();

    /**
     * Method of blurring
     */
    private final BlurProcess _javaBlurProcess;

    /**
     * Constructor method (basic initialization and construction of the pixel array)
     */
    public StackBlurManager() {
        _javaBlurProcess = new JavaBlurProcess();
    }

    @NonNull
    public Bitmap process (@NonNull Bitmap inputBitmap, @Nullable Bitmap outputBitmap, int radius, float darkenFactor) throws BlurException {
        return _javaBlurProcess.blur(inputBitmap, outputBitmap, radius, darkenFactor);
    }

}
