// Copyright (c) 2014-10-15 wanghb@dearcoin.com. All rights reserved.
package com.xyn.appdetial.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.xyn.appdetial.App;
import com.xyn.appdetial.Config;
import com.xyn.appdetial.R;

import java.io.File;
import java.util.Locale;


public class ImageLoaderHelper {
    private static final String TAG = ImageLoaderHelper.class.getSimpleName();
    private static BitmapUtils bitmapUtils;


    public static <T extends View> void loadBlurBitmapTo(String path, T v) {
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(App.get());
        }
        BitmapLoadCallBack<T> callback = new DefaultBitmapLoadCallBack<T>() {

            @Override
            public void onLoadCompleted(T container, String uri, Bitmap bitmap,
                                        BitmapDisplayConfig config, BitmapLoadFrom from) {
                bitmap = BlurHelper.blur(bitmap, uri, true);
                super.onLoadCompleted(container, uri, bitmap, config, from);
            }

        };
        bitmapUtils.display(v, path, callback);
    }


    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    //   先不考虑本地图片, Note 0 is null resource
    public static void loadPicByXutil(Fragment fragment, String picUri, ImageView iv_pic, int placeHolderId) {
        if (TextUtils.isEmpty(picUri)) {
            Log.e(TAG, "picUri is empty");
            iv_pic.setImageResource(placeHolderId);
            return;
        }
//        网络图片
        if (!picUri.startsWith("http")) {
//            Log.i(TAG, "传到ImageLoader的原始地址=" + picUri);
            picUri = Config.Consts.host + "/" + picUri;
        }
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(fragment.getActivity());
        }
        bitmapUtils.display(iv_pic, picUri);
    }


    //  加载头像 先不考虑本地图片, Note 0 is null resource
    public static void loadAvatar(Fragment frag, String picUri, ImageView iv_pic, int placeHolderId) {
        if (TextUtils.isEmpty(picUri)) {
            Log.e(TAG, "头像为空");
            iv_pic.setImageResource(R.drawable.avatar_default);
            return;
        }
//        Log.i(TAG, "加载头像 By fragment 原始地址=" + picUri);
//        网络图片
        if (picUri.contains("http")) {
            picUri = picUri.substring(picUri.lastIndexOf("http"));
//            Log.i(TAG, "subString 后 picUri:" + picUri);
        } else {
            picUri = Config.Consts.host + "/" + picUri;
        }

        try {
            Glide.with(frag).load(picUri).dontTransform().error(R.drawable.avatar_default).into(iv_pic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  加载头像 先不考虑本地图片, Note 0 is null resource
    public static void loadAvatar(Context context, String picUri, ImageView iv_pic, int placeHolderId) {
        if (TextUtils.isEmpty(picUri)) {
            Log.e(TAG, "头像为空");
            return;
        }
//        Log.i(TAG, "加载头像 By fragment 原始地址=" + picUri);
//        网络图片
        if (picUri.contains("http")) {
            picUri = picUri.substring(picUri.lastIndexOf("http"));
//            Log.i(TAG, "subString 后 picUri:" + picUri);
        } else {
            picUri = Config.Consts.host + "/" + picUri;
        }

        Glide.with(context).load(picUri).dontTransform().error(R.drawable.avatar_default).into(iv_pic);
    }

    //  上线之前不设置placeholder ,这个现在当error 图片使用
    //   先不考虑本地图片, Note 0 is null resource
    public static void loadPic(Fragment frag, String picUri, ImageView iv_pic, int placeHolderId) {
        if (TextUtils.isEmpty(picUri)) {
            Log.e(TAG, "图片地址为空");
            Glide.with(frag).load(placeHolderId).into(iv_pic);
            return;
        }
//      网络图片
//        Log.i(TAG, "loadPic by fragment的原始地址=" + picUri);
        //        网络图片
        if (picUri.contains("http")) {
            picUri = picUri.substring(picUri.lastIndexOf("http"));
        } else {
            if (picUri.startsWith(File.separator)) {
                picUri = Config.Consts.host + picUri;
            } else {
                picUri = Config.Consts.host +File.separator + picUri;
            }
        }

        try {
            Glide.with(frag).load(picUri).into(iv_pic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //   先不考虑本地图片 Note 0 is null resource
    public static void loadPic(Activity activity, String picUri, ImageView iv_pic, int placeHolderId) {
        if (TextUtils.isEmpty(picUri)) {
            Log.e(TAG, "picUri is empty");
            Glide.with(activity).load(placeHolderId).into(iv_pic);
            return;
        }
//        Log.i(TAG, "loadPic by Activity的原始地址=" + picUri);
//        网络图片
        if (picUri.contains("http")) {
            picUri = picUri.substring(picUri.lastIndexOf("http"));
        } else {
            picUri = Config.Consts.host + "/" + picUri;
        }
        Glide.with(activity).load(picUri).into(iv_pic);
    }

    //   先不考虑本地图片 Note 0 is null resource
    public static void loadPic(Context context, String picUri, ImageView iv_pic, int placeHolderId) {
        if (TextUtils.isEmpty(picUri)) {
            Log.e(TAG, "picUri is empty");
            Glide.with(context).load(placeHolderId).into(iv_pic);
            return;
        }
//        Log.i(TAG, "loadPic by context的原始地址=" + picUri);
//        网络图片
        if (picUri.contains("http")) {
            picUri = picUri.substring(picUri.lastIndexOf("http"));
        } else {
            picUri = Config.Consts.host + "/" + picUri;
        }
        Glide.with(context).load(picUri).into(iv_pic);
    }


    /**
     * Represents supported schemes(protocols) of URI. Provides convenient methods for work with schemes and URIs.
     */
    public enum Scheme {
        HTTP("http"), HTTPS("https"), FILE("file"), CONTENT("content"), ASSETS("assets"), DRAWABLE("drawable"), UNKNOWN("");

        private String scheme;
        private String uriPrefix;

        Scheme(String scheme) {
            this.scheme = scheme;
            uriPrefix = scheme + "://";
        }

        /**
         * Defines scheme of incoming URI
         *
         * @param uri URI for scheme detection
         * @return Scheme of incoming URI
         */
        public static Scheme ofUri(String uri) {
            if (uri != null) {
                for (Scheme s : values()) {
                    if (s.belongsTo(uri)) {
                        return s;
                    }
                }
            }
            return UNKNOWN;
        }

        private boolean belongsTo(String uri) {
            return uri.toLowerCase(Locale.US).startsWith(uriPrefix);
        }

        /**
         * Appends scheme to incoming path
         */
        public String wrap(String path) {
            return uriPrefix + path;
        }

        /**
         * Removed scheme part ("scheme://") from incoming URI
         */
        public String crop(String uri) {
            if (!belongsTo(uri)) {
                throw new IllegalArgumentException(String.format("URI [%1$s] doesn't have expected scheme [%2$s]", uri, scheme));
            }
            return uri.substring(uriPrefix.length());
        }
    }

    private static void testUri() {
        Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "");
    }
}
