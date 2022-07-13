package com.androidx.clip;

/**
 * 裁剪图形
 */
public enum ClipShape {

    /**
     * 矩形
     */
    RECT(0),
    /**
     * 圆形
     */
    CIRCLE(1),
    /**
     * 圆角
     */
    ROUND(2);

    private int value;

    ClipShape(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
