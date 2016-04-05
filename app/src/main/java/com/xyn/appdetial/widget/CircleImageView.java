package com.xyn.appdetial.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author wanghb
 */
public class CircleImageView extends ImageView {
    public CircleImageView(Context context) {
        super(context);
        init(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        matrix = new Matrix();
    }

    @Override
    public void setImageResource(int resId) {
        isImageRound = false;
        super.setImageResource(resId);
    }

    private boolean isImageRound = false;

    @Override
    public void setImageURI(Uri uri) {
        isImageRound = false;
        super.setImageURI(uri);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        isImageRound = false;
        super.setImageDrawable(drawable);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        isImageRound = false;
        super.setImageBitmap(bm);
    }

    private Paint paint;

    private Matrix matrix;

    private int borderColor = Color.TRANSPARENT;

    private int border = 1;

    private Bitmap roundBitmap;

    @Override
    protected void onDraw(Canvas canvas) {
        if (isImageRound && roundBitmap != null) {
            canvas.drawBitmap(roundBitmap, matrix, paint);
        } else {
            if (getDrawable() != null) {
                Bitmap bitmap = toRoundBitmap(getDrawable(), getWidth(),
                        border, borderColor);
                canvas.drawBitmap(bitmap, matrix, paint);
                roundBitmap = bitmap;
                isImageRound = true;
            }
        }
    }

    public static Bitmap toRoundBitmap(Drawable drawable, int width,
            int border, int borderColor) {
        Bitmap bitmap = drawableToBitmap(drawable);
        return toRoundBitmap(bitmap, width, border, borderColor);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap toRoundBitmap(Bitmap bitmap, int width, int border,
            int borderColor) {
        width = width - 2 * border;
        Bitmap output = Bitmap.createBitmap(width + border * 2, width + border
                * 2, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        int min = Math.min(bitmap.getHeight(), bitmap.getWidth());
        Rect src = new Rect((bitmap.getWidth() - min) / 2,
                (bitmap.getHeight() - min) / 2, (bitmap.getWidth() + min) / 2,
                (bitmap.getHeight() + min) / 2);
        Rect dst = new Rect(border, border, border + width, border + width);
        final RectF rectF = new RectF(border, border, border + width, border
                + width);
        final float roundPx = width / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
        paint.setColor(borderColor);
        paint.setStrokeWidth(border);
        paint.setStyle(Style.STROKE);
        RectF borderRectF = new RectF(border, border, width + border, width
                + border);
        canvas.drawArc(borderRectF, 0, 360, true, paint);
        return output;
    }
}
