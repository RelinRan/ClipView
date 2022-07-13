package com.androidx.clip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 剪切蒙版
 */
public class ClipMask extends View {

    private Paint paint;
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
     * 矩形位置
     */
    private float left, top, right, bottom;
    /**
     * View宽高
     */
    private float width, height;
    /**
     * 图形类型，0：Rect 1:Circle 2:Round
     */
    private ClipShape clipShape = ClipShape.RECT;
    /**
     * 圆角矩形圆角大小
     */
    private float roundRadius = 20;

    public ClipMask(Context context) {
        super(context);
        initAttributeSet(context);
    }

    public ClipMask(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttributeSet(context);
    }

    public ClipMask(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(context);
    }

    private void initAttributeSet(Context context) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(maskColor);
        canvas.drawRect(0, 0, width, height, paint);
        left = (width - clipWidth) / 2.0F;
        top = (height - clipHeight) / 2.0F;
        right = width / 2.0F + clipWidth / 2.0F;
        bottom = height / 2.0F + clipHeight / 2.0F;
        if (clipShape == ClipShape.RECT) {
            drawRect(canvas);
        }
        if (clipShape == ClipShape.CIRCLE) {
            drawCircle(canvas);
        }
        if (clipShape == ClipShape.ROUND) {
            drawRoundRect(canvas, roundRadius);
        }
    }

    /**
     * 绘制矩形
     *
     * @param canvas
     */
    private void drawRect(Canvas canvas) {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawRect(left, top, right, bottom, paint);
    }

    /**
     * 绘制圆形
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        float radius = (clipWidth <= clipHeight ? clipWidth : clipHeight) / 2;
        canvas.drawCircle(width / 2, height / 2, radius, paint);
    }

    /**
     * 绘制圆角矩形
     *
     * @param canvas
     * @param radius
     */
    private void drawRoundRect(Canvas canvas, float radius) {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawRoundRect(left, top, right, bottom, radius, radius, paint);
    }

    /**
     * 设置剪切图形类型
     *
     * @param clipShape 图形类型
     */
    public void setClipShape(ClipShape clipShape) {
        this.clipShape = clipShape;
        invalidate();
    }

    /**
     * 设置圆角矩形大小
     *
     * @param roundRadius 圆角矩形大小
     */
    public void setRoundRadius(float roundRadius) {
        this.roundRadius = roundRadius;
        invalidate();
    }

    /**
     * 设置裁剪宽度
     *
     * @param width
     */
    public void setClipWidth(int width) {
        this.clipWidth = width;
        invalidate();
    }

    /**
     * 设置蒙版高度
     *
     * @param height
     */
    public void setClipHeight(int height) {
        this.clipHeight = height;
        invalidate();
    }

    /**
     * 设置蒙版颜色
     *
     * @param maskColor
     */
    public void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
        invalidate();
    }

}

