package com.blindtest.deezer.deezerblindtest.Transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Debug;
import android.support.annotation.ColorInt;

import com.blindtest.deezer.deezerblindtest.Utils.BlurException;
import com.blindtest.deezer.deezerblindtest.Utils.BlurManager;
import com.blindtest.deezer.deezerblindtest.Utils.ChatUtils;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by pierre-louisbertheau on 31/01/2017.
 */

public class RoundBlurTransformation extends BitmapTransformation {

    /**
     * The scale we apply to the original bitmap to create the blurred one
     */
    private static final float BLUR_SCALE = 0.25f;

    /**
     * The scale of the circle
     */
    private static final float ORIGINAL_IMAGE_SCALE = 0.70f;

    private final int mCornerRadius;
    private final int mBorderWidth;
    private final float mDarkenFactor;
    private final int mBorderColor;
    private final float mFallbackDarken;
    private final int mBlurRadius;

    private final Paint mBorderPaint;

    // avoid concurrency problems on the paint as the lib makes no guaranty on the number of Transformation threads
    private final ThreadLocal<Paint> mPaintThreadLocal = new ThreadLocal<Paint>() {

        @Override
        protected Paint initialValue() {
            return new Paint(Paint.ANTI_ALIAS_FLAG);
        }
    };

    public RoundBlurTransformation(Context context,
                                   int blurRadius,
                                   float darkenFactor,
                                   int cornerRadius,
                                   int borderWidth,
                                   @ColorInt int borderColor,
                                   float fallbackDarken) {
        super(context);
        mCornerRadius = cornerRadius;
        mBorderWidth = borderWidth;
        mBlurRadius = blurRadius;
        mDarkenFactor = darkenFactor;
        mBorderColor = borderColor;
        mFallbackDarken = fallbackDarken;

        // border paint
        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setAntiAlias(true);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform,
                               int outWidth, int outHeight) {

        final Paint imagePaint = mPaintThreadLocal.get();

        // create the bitmap to return
        Bitmap outputBitmap = ChatUtils.getBitmapFromPoolOrCreate(pool, outWidth, outHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputBitmap);

        // the size of the downscaled bitmap to blur
        final int blurBitmapWidth = (int) (outWidth * BLUR_SCALE);
        final int blurBitmapHeight = (int) (outHeight * BLUR_SCALE);

        // create a downscaled bitmap to be blurred
        Bitmap blurredBitmap = ChatUtils.getScaledBitmapFromPoolOrCreate(pool, toTransform, blurBitmapWidth, blurBitmapHeight);

        // blur the bitmap
        boolean blurSuccess = true;
        try {
            BlurManager.getInstance().blur(blurredBitmap, blurredBitmap, mBlurRadius, mDarkenFactor);
        } catch (BlurException e) {
//            Debug.log_e(DebugConfig.UI, "RoundBlurTransformation", "Couldn't create the blur background");
            blurSuccess = false;
        }
        if (blurSuccess) {
            // draw the blurred background
            BitmapShader shaderBlur = new BitmapShader(blurredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            imagePaint.setShader(shaderBlur);
            canvas.save();
            // scale to the inverse so the blurred bitmap take the size of the output bitmap
            canvas.scale(1 / BLUR_SCALE, 1 / BLUR_SCALE);
            canvas.drawRoundRect(
                    new RectF(0,
                            0,
                            outWidth * BLUR_SCALE,
                            outHeight * BLUR_SCALE),
                    mCornerRadius * BLUR_SCALE,
                    mCornerRadius * BLUR_SCALE,
                    imagePaint);
            canvas.restore();
        } else {
            // fallback to a darkened background
            BitmapShader shaderDarken = new BitmapShader(toTransform, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            imagePaint.setShader(shaderDarken);

            // create a matrix to darken the image
            final ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setScale(mFallbackDarken, mFallbackDarken, mFallbackDarken, 1);
            imagePaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            canvas.save();

            final float scaleX = ((float) outWidth) / toTransform.getWidth();
            final float scaleY = ((float) outHeight) / toTransform.getHeight();
            canvas.scale(scaleX, scaleY);
            canvas.drawRoundRect(
                    new RectF(0,
                            0,
                            toTransform.getWidth(),
                            toTransform.getHeight()),
                    mCornerRadius,
                    mCornerRadius,
                    imagePaint);
            canvas.restore();
            imagePaint.setColorFilter(null);
        }


        // draw the original picture in the middle of the bitmap within a circle
        BitmapShader shaderOriginal = new BitmapShader(toTransform, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        imagePaint.setShader(shaderOriginal);
        // scale so the original image is displayed within the circle we want
        final float sx1 = ((float) outWidth) / toTransform.getWidth() * ORIGINAL_IMAGE_SCALE;
        final float sy1 = ((float) outHeight) / toTransform.getHeight() * ORIGINAL_IMAGE_SCALE;
        canvas.save();
        // translate so the image is centered
        canvas.translate(outWidth * (1 - ORIGINAL_IMAGE_SCALE) / 2, outHeight * (1 - ORIGINAL_IMAGE_SCALE) / 2);
        canvas.scale(sx1, sy1);
        canvas.drawCircle(toTransform.getWidth() / 2, toTransform.getHeight() / 2, toTransform.getWidth() / 2, imagePaint);
        canvas.restore();

        // draw border
        if (mBorderWidth > 0) {
            canvas.drawCircle(outWidth / 2, outHeight / 2, outWidth / 2 * ORIGINAL_IMAGE_SCALE + mBorderWidth / 2, mBorderPaint);
        }

        // recycler bitmaps used for the transformation
        // but not toTransform because it's already done by Glide
        recycleBitmap(pool, blurredBitmap);

        return outputBitmap;
    }

    private void recycleBitmap(BitmapPool pool, Bitmap bitmapToRecycle) {
        if (!pool.put(bitmapToRecycle)) {
            bitmapToRecycle.recycle();
        }
    }

    @Override
    public String getId() {
        return "com.deezer.RoundBlurTransformation" + mCornerRadius + "/" + mBorderWidth + "/" + mBorderColor + "/" + mDarkenFactor + "/" + mBlurRadius;
    }


}
