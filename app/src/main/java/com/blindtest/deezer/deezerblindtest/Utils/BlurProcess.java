package com.blindtest.deezer.deezerblindtest.Utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

/**
 * Created by pierre-louisbertheau on 31/01/2017.
 */

interface BlurProcess {
    /**
     * Process the given image, blurring by the supplied radius.
     * If radius is 0, this will return original
     * @param inputBitmap the bitmap to blur
     * @param outputBitmap the bitmap to return
     * @param radius the radius in pixels to blur the image
     * @param multiplyFactor factor used to multiply (ie darken) the image. Use 1f if you don't want to darken the image
     *                       must be 0< factor <= 1f
     * @return the blurred version of the image.
     */
    @WorkerThread
    @NonNull
    Bitmap blur(@NonNull Bitmap inputBitmap, @Nullable Bitmap outputBitmap, float radius, float multiplyFactor) throws BlurException;
}
