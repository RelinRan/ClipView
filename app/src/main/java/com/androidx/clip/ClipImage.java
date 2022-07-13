package com.androidx.clip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 剪切图片
 */
public class ClipImage extends View implements ScaleGestureDetector.OnScaleGestureListener {

    private static String TAG = ClipImage.class.getSimpleName();
    /**
     * 资源Bitmap
     */
    private Bitmap source;
    /**
     * 手势
     */
    private ScaleGestureDetector detector;
    /**
     * 缩放
     */
    private float scale = 1.77f;
    /**
     * 最大缩放值
     */
    private float maxScale = 3.0f;
    /**
     * 最小缩放值
     */
    private float minScale = 0.65F;
    /**
     * 按下坐标
     */
    private float downX, downY;
    /**
     * 移动坐标
     */
    private float moveX = 0, moveY = 0;
    /**
     * 缩放开始
     */
    private boolean scaleBegin;
    /**
     * 裁剪宽度
     */
    private int clipWidth = 702;
    /**
     * 裁剪高度
     */
    private int clipHeight = 702;
    /**
     * 剪裁图片
     */
    private Bitmap cropBitmap;
    /**
     * 裁剪图形类型，0：Rect 1:Circle 2:Round
     */
    private ClipShape clipShape = ClipShape.RECT;
    /**
     * 圆角矩形圆角大小
     */
    private float roundRadius = 20;

    public ClipImage(Context context) {
        super(context);
        initAttributeSet(context);
    }

    public ClipImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttributeSet(context);
    }

    public ClipImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(context);
    }

    private void initAttributeSet(Context context) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        detector = new ScaleGestureDetector(context, this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setImageBitmap(source);
    }

    /**
     * @return 图片
     */
    public Bitmap getBitmap() {
        return source;
    }

    /**
     * 设置资源
     *
     * @param resId
     */
    public void setImageResource(int resId) {
        setImageBitmap(BitmapFactory.decodeResource(getResources(), resId));
        invalidate();
    }

    /**
     * 设置资源
     *
     * @param path
     */
    public void setImagePath(String path) {
        setImageBitmap(BitmapFactory.decodeFile(path));
        invalidate();
    }

    /**
     * 设置最大缩放值
     *
     * @param maxScale
     */
    public void setMaxScale(float maxScale) {
        this.maxScale = maxScale;
        invalidate();
    }

    /**
     * 设置最小缩放值
     * @param minScale
     */
    public void setMinScale(float minScale) {
        this.minScale = minScale;
        invalidate();
    }

    /**
     * 设置裁剪宽度
     *
     * @param clipWidth
     */
    public void setClipWidth(int clipWidth) {
        this.clipWidth = clipWidth;
        invalidate();
    }

    /**
     * 设置裁剪高度
     *
     * @param clipHeight
     */
    public void setClipHeight(int clipHeight) {
        this.clipHeight = clipHeight;
        invalidate();
    }

    /**
     * 设置裁剪图形
     *
     * @param shape
     */
    public void setClipShape(ClipShape shape) {
        this.clipShape = shape;
        invalidate();
    }

    /**
     * 设置裁剪矩形图的圆角大小
     *
     * @param radius 圆角大小
     */
    public void setRoundRadius(float radius) {
        this.roundRadius = radius;
        invalidate();
    }

    /**
     * 加载资源
     *
     * @param source
     */
    public void setImageBitmap(Bitmap source) {
        if (getBitmap() == null) {
            if (source.getWidth() < clipWidth) {
                source = scale(source, clipWidth / source.getWidth());
            }
            this.source = source;
        }
        scale = calculateRatio(source, getMeasuredWidth());
        minScale = calculateRatio(source, clipWidth);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        if (!scaleBegin) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    downY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_MOVE:
                    moveX += (event.getX() - downX) / 10;
                    moveY += (event.getY() - downY) / 10;
                    invalidate();
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        Bitmap bitmap = scale(source, scale);
        float bitmapW = bitmap.getWidth();
        float bitmapH = bitmap.getHeight();
        //默认显示中间位置
        float left = (getMeasuredWidth() - bitmapW) / 2.0F;
        float top = (getMeasuredHeight() - bitmapH) / 2.0F;
        //左右边限制
        float leftEdge = (bitmapW - clipWidth) / 2.0F;
        float rightEdge = -(bitmapW - clipWidth) / 2.0F;
        moveX = moveX > leftEdge ? leftEdge : moveX;
        moveX = moveX < rightEdge ? rightEdge : moveX;
        //上下边限制
        float topEdge = (bitmapH - clipHeight) / 2.0F;
        float bottomEdge = -(bitmapH - clipHeight) / 2.0F;
        moveY = moveY > topEdge ? topEdge : moveY;
        moveY = moveY < bottomEdge ? bottomEdge : moveY;
        //位移显示
        float x = left + moveX;
        float y = top + moveY;
        canvas.drawBitmap(bitmap, x, y, paint);
        //剪切图片
        int cropX = (int) (leftEdge - moveX);
        int cropY = (int) (topEdge - moveY);
        cropX = cropX < 0 ? 0 : cropX;
        cropY = cropY < 0 ? 0 : cropY;
        Log.i(TAG, "->onDraw cropX=" + cropX + ",cropY=" + cropY);
        cropBitmap = Bitmap.createBitmap(bitmap, cropX, cropY, clipWidth, clipHeight);
    }

    /**
     * @param src 原图片
     * @return 圆形图片
     */
    private Bitmap drawCircleBitmap(Bitmap src) {
        if (src == null) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawARGB(0, 0, 0, 0);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawCircle(width / 2, height / 2, width / 2F, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Rect srcRect = new Rect(0, 0, width, height);
        RectF dest = new RectF(0, 0, width, height);
        canvas.drawBitmap(src, srcRect, dest, paint);
        return target;
    }

    /**
     * @param src    原图片
     * @param radius 圆角
     * @return 矩形圆角图片
     */
    private Bitmap drawRoundBitmap(Bitmap src, float radius) {
        if (src == null) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawARGB(0, 0, 0, 0);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Rect srcRect = new Rect(0, 0, width, height);
        RectF dest = new RectF(0, 0, width, height);
        canvas.drawRoundRect(dest, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, srcRect, dest, paint);
        return target;
    }

    /**
     * @return 裁剪的图片
     */
    public Bitmap getClipBitmap() {
        if (clipShape == ClipShape.RECT) {
            return getRectBitmap();
        }
        if (clipShape == ClipShape.CIRCLE) {
            return getCircleBitmap();
        }
        if (clipShape == ClipShape.ROUND) {
            return getRoundBitmap(roundRadius);
        }
        return getRectBitmap();
    }

    /**
     * 剪切图片
     *
     * @return
     */
    private Bitmap getRectBitmap() {
        return cropBitmap;
    }

    /**
     * @return 圆形图片
     */
    private Bitmap getCircleBitmap() {
        return drawCircleBitmap(cropBitmap);
    }

    /**
     * @param radius
     * @return
     */
    private Bitmap getRoundBitmap(float radius) {
        return drawRoundBitmap(cropBitmap, radius);
    }

    /**
     * 缩放Bitmap
     *
     * @param source 资源
     * @param scale  缩放比例
     * @return
     */
    private Bitmap scale(Bitmap source, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
    }

    /**
     * 计算缩放比例
     *
     * @param bitmap   图片
     * @param reqWidth 需要宽度
     * @return
     */
    private float calculateRatio(Bitmap bitmap, float reqWidth) {
        int bitmapWidth = bitmap.getWidth();
        float ratio = reqWidth * 1.0F / bitmapWidth * 1.0F;
        return ratio;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float factor = detector.getScaleFactor();
        if (factor > 1.0F) {
            scale += factor / 6;
        } else {
            scale -= factor / 6;
        }
        scale = scale < minScale ? minScale : scale;
        scale = scale > maxScale ? maxScale : scale;
        invalidate();
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        scaleBegin = true;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        scaleBegin = false;
    }

}

