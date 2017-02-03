package com.blindtest.deezer.deezerblindtest.Transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import static com.blindtest.deezer.deezerblindtest.Utils.ChatUtils.getBitmapFromPoolOrCreate;

/**
 * Created by pierre-louisbertheau on 02/02/2017.
 */

public class RoundFilledBackgroundTransformation extends BitmapTransformation {

    /**
     * The scale of the circle
     */
    private static final float ORIGINAL_IMAGE_SCALE = 0.70f;

    private final int mCornerRadius;

    private final Paint mBackgroundPaint;

    // avoid concurrency problems on the paint as the lib makes no guaranty on the number of Transformation threads
    private final ThreadLocal<Paint> mPaintThreadLocal = new ThreadLocal<Paint>() {

        @Override
        protected Paint initialValue() {
            return new Paint(Paint.ANTI_ALIAS_FLAG);
        }
    };

    public RoundFilledBackgroundTransformation(Context context,
                                               int cornerRadius,
                                               @ColorInt int backgroundColor) {
        super(context);
        mCornerRadius = cornerRadius;

        // background paint
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(backgroundColor);
        mBackgroundPaint.setAntiAlias(true);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform,
                               int outWidth, int outHeight) {

        final Paint imagePaint = mPaintThreadLocal.get();

        // create the bitmap to return
        Bitmap outputBitmap = getBitmapFromPoolOrCreate(pool, outWidth, outHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputBitmap);

        canvas.drawRoundRect(
                new RectF(0,
                        0,
                        outWidth,
                        outHeight),
                mCornerRadius,
                mCornerRadius,
                mBackgroundPaint);


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

        return outputBitmap;
    }

    @Override
    public String getId() {
        return "com.deezer.RoundFilledBackgroundTransformation" + mCornerRadius;
    }


}
