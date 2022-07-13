package com.androidx.clip;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 裁剪
 */
public class ClipView extends FrameLayout {

    /**
     * 图片操作对象
     */
    private ClipImage clipImage;
    /**
     * 蒙版对象
     */
    private ClipMask clipMask;
    /**
     * 裁剪宽度
     */
    private int clipWidth = 702;
    /**
     * 裁剪高度
     */
    private int clipHeight = 702;
    /**
     * 蒙版颜色
     */
    private int maskColor = Color.parseColor("#8F000000");
    /**
     * 最大缩放值
     */
    private float maxScale = 3.0f;
    /**
     * 最小缩放值
     */
    private float minScale = 0.65F;
    /**
     * 裁剪图形类型，0：Rect 1:Circle 2:Round
     */
    private ClipShape clipShape = ClipShape.RECT;
    /**
     * 圆角矩形圆角大小
     */
    private float roundRadius = 20;
    /**
     * 图片资源
     */
    private int resId;


    public ClipView(@NonNull Context context) {
        super(context);
        initAttributeSet(context, null);
    }

    public ClipView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttributeSet(context, attrs);
    }

    public ClipView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(context, attrs);
    }

    public ClipView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttributeSet(context, attrs);
    }

    protected void initAttributeSet(Context context, AttributeSet attrs) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        LayoutInflater.from(context).inflate(R.layout.view_clip, this, true);
        clipImage = findViewById(R.id.clip_img);
        clipMask = findViewById(R.id.clip_mask);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ClipView);
            clipWidth = array.getDimensionPixelOffset(R.styleable.ClipView_clipWidth, clipWidth);
            clipHeight = array.getDimensionPixelOffset(R.styleable.ClipView_clipHeight, clipHeight);
            maskColor = array.getColor(R.styleable.ClipView_maskColor, maskColor);
            maxScale = array.getFloat(R.styleable.ClipView_maxScale, maxScale);
            minScale = array.getFloat(R.styleable.ClipView_minScale, minScale);
            int shape = array.getInt(R.styleable.ClipView_clipShape, 0);
            if (shape == 0) {
                clipShape = ClipShape.RECT;
            }
            if (shape == 1) {
                clipShape = ClipShape.CIRCLE;
            }
            if (shape == 2) {
                clipShape = ClipShape.ROUND;
            }
            roundRadius = array.getDimension(R.styleable.ClipView_roundRadius, roundRadius);
            resId = array.getResourceId(R.styleable.ClipView_clipSrc, 0);
            array.recycle();
        }
        setClipWidth(clipWidth);
        setClipHeight(clipHeight);
        setMaskColor(maskColor);
        setMaxScale(maxScale);
        setMinScale(minScale);
        setClipShape(clipShape);
        setRoundRadius(roundRadius);
        setImageResource(resId);
    }

    /**
     * 设置蒙版颜色
     *
     * @param color
     */
    public void setMaskColor(int color) {
        clipMask.setMaskColor(color);
    }

    /**
     * 设置裁剪宽度
     *
     * @param width
     */
    public void setClipWidth(int width) {
        clipImage.setClipWidth(width);
        clipMask.setClipWidth(width);
    }

    /**
     * 设置裁剪高度
     *
     * @param height
     */
    public void setClipHeight(int height) {
        clipImage.setClipHeight(height);
        clipMask.setClipHeight(height);
    }

    /**
     * 设置裁剪图形
     *
     * @param shape
     */
    public void setClipShape(ClipShape shape) {
        clipImage.setClipShape(shape);
        clipMask.setClipShape(shape);
    }

    /**
     * 设置裁剪矩形图的圆角大小
     *
     * @param radius 圆角大小
     */
    public void setRoundRadius(float radius) {
        clipImage.setRoundRadius(radius);
        clipMask.setRoundRadius(radius);
    }

    /**
     * 设置最大缩放值
     *
     * @param maxScale
     */
    public void setMaxScale(float maxScale) {
        clipImage.setMaxScale(maxScale);
    }

    /**
     * 设置最小缩放值
     *
     * @param minScale
     */
    public void setMinScale(float minScale) {
        clipImage.setMinScale(minScale);
    }

    /**
     * 设置资源
     *
     * @param resId
     */
    public void setImageResource(int resId) {
        clipImage.setImageResource(resId);
    }

    /**
     * 设置资源
     *
     * @param bitmap
     */
    public void setImageBitmap(Bitmap bitmap) {
        clipImage.setImageBitmap(bitmap);
    }

    /**
     * 设置资源
     *
     * @param path
     */
    public void setImagePath(String path) {
        clipImage.setImagePath(path);
    }

    /**
     * @return 图片
     */
    public Bitmap getBitmap() {
        return clipImage.getBitmap();
    }

    /**
     * @return 裁剪的图片
     */
    public Bitmap getClipBitmap() {
        return clipImage.getClipBitmap();
    }

    /**
     * @return 裁剪图片对象
     */
    public ClipImage getClipImage() {
        return clipImage;
    }

    /**
     * @return 裁剪蒙版对象
     */
    public ClipMask getClipMask() {
        return clipMask;
    }

}
