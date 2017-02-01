package com.blindtest.deezer.deezerblindtest.Utils;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;

/**
 * Created by pierre-louisbertheau on 31/01/2017.
 */

public class BlurManager {

    public final static float DEFAULT_DARKEN_FACTOR = 0.45f;
    public final static int DEFAULT_RADIUS = 15;

    public static BlurManager getInstance() {
        if (mInstance == null) {
            mInstance = new BlurManager();
        }
        return mInstance;
    }
    private static BlurManager mInstance;

    /**
     * Used to know if the RenderScript is available
     */
    private static boolean sBlurAvailability = Build.VERSION.SDK_INT >= ICE_CREAM_SANDWICH_MR1;

    private StackBlurManager mStackBlurManager = new StackBlurManager();

    /**
     * @return true if the RenderScript blur is available on the current device
     */
    public static boolean isBlurAvailable() {
        return sBlurAvailability;
    }

    /**
     * Perform a blur on the bitmap with the default parameters.
     * Remember to check {@link BlurManager#isBlurAvailable()}.
     * If applicable, consider using {@link com.deezer.imagelibrary.transformation.BlurTransformation} instead.
     * @see BlurManager#blur(Bitmap, Bitmap, int, float)
     */
    @NonNull
    public Bitmap blur(@NonNull Bitmap inputBitmap) throws BlurException {
        return blur(inputBitmap, null, DEFAULT_RADIUS, DEFAULT_DARKEN_FACTOR);
    }

    /**
     * blurs a bitmap using StackBlur, remember to check {@link BlurManager#isBlurAvailable()}.
     * @param inputBitmap the bitmap to blur
     * @param outputBitmap the bitmap to return. This bitmap must be mutable. An exception will be thrown otherwise.
     * @param radius the blur radius in pixels
     * @param darkenFactor the factor used to multiply each pixel of the blurred image
     * @return the transformed bitmap or {@code null}.
     */
    @NonNull
    public Bitmap blur(@NonNull Bitmap inputBitmap, @Nullable Bitmap outputBitmap, int radius, float darkenFactor) throws BlurException {
        if (!sBlurAvailability) throw new BlurException("Blur not available");
        return mStackBlurManager.process(inputBitmap, outputBitmap, radius, darkenFactor);
    }

}
