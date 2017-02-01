package com.blindtest.deezer.deezerblindtest.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blindtest.deezer.deezerblindtest.Constant.ChatConstant;
import com.blindtest.deezer.deezerblindtest.Model.Message;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pierre-louisbertheau on 30/01/2017.
 */

public class ChatUtils {


    /**
     * Ici on parse le JSON reçu du serveur
     *
     */
    public static Message jsonToMessage(String json) {


        Message message = null;

        try {
            JSONObject jsonObject = new JSONObject(json);

            message = new Message(
                    jsonObject.getString(ChatConstant.TAG_MESSAGE),
                    // On met "false" car ce n'est pas notre message
                    false
            );

            return message;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String messageToJson(Message message) {

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put(ChatConstant.TAG_MESSAGE, message.getMessage());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    @NonNull
    public static Bitmap getBitmapFromPoolOrCreate(@Nullable BitmapPool pool, int outputBitmapWidth, int outputBitmapHeight, Bitmap.Config config) {
        Bitmap canvasBitmap = null;
        if (pool != null) {
            canvasBitmap = pool.get(outputBitmapWidth, outputBitmapHeight, config);
        }
        if (canvasBitmap == null) {
            canvasBitmap = Bitmap.createBitmap(outputBitmapWidth, outputBitmapHeight, config);
        }
        return canvasBitmap;
    }

    @NonNull
    public static Bitmap getScaledBitmapFromPoolOrCreate(@Nullable BitmapPool pool, Bitmap toTransform, int outputBitmapWidth, int outputBitmapHeight) {
        Bitmap output = getBitmapFromPoolOrCreate(pool, outputBitmapWidth, outputBitmapHeight, toTransform.getConfig());
        return getScaledBitmap(toTransform, outputBitmapWidth, outputBitmapHeight, output);
    }

    @NonNull
    private static Bitmap getScaledBitmap(Bitmap toTransform, int outputBitmapWidth, int outputBitmapHeight, Bitmap output) {
        final float scaleX = outputBitmapWidth / (float) toTransform.getWidth();
        final float scaleY = outputBitmapHeight / (float) toTransform.getHeight();
        Canvas transformCanvas = new Canvas(output);
        transformCanvas.scale(scaleX, scaleY);
        transformCanvas.drawBitmap(toTransform, 0, 0, new Paint());
        return output;
    }




}
