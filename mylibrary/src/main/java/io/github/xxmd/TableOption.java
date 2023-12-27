package io.github.xxmd;

import android.graphics.Color;

import androidx.annotation.ColorInt;

public class TableOption<T> {
    public String label;
    public T value;
    public int weight = 1;
    @ColorInt
    public int labelColor = Color.BLACK;
    @ColorInt
    public int backgroundColor;
}
