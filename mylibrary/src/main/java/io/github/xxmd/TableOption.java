package io.github.xxmd;

import androidx.annotation.ColorInt;

public class TableOption<T> {
    public String label;
    public T value;
    public int weight;
    @ColorInt
    public int labelColor;
    @ColorInt
    public int backgroundColor;
}
