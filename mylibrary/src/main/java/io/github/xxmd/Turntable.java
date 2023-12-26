package io.github.xxmd;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Turntable extends View {
    private List<TableOption> tableOptionList = new ArrayList<>();
    private int weightSum;
    private Paint dotPaint;
    private Float centerDotRadius;
    private Paint sectorBgPaint;
    private Paint labelPaint;
    private Paint indicatorPaint;
    private TurntableListener listener;
    private float addedAngle = 0;
    private Disposable interval;
    private float preRotateAngle;
    private Paint borderPaint;
    private float indicatorPointAngle = -45;
    private TypedArray typedArray;
    private Drawable centerIndicator;
    private float labelMarinCenter;
    private int rotateDuration;

    public TurntableListener getListener() {
        return listener;
    }

    public void setListener(TurntableListener listener) {
        this.listener = listener;
    }

    public List<TableOption> getTableOptionList() {
        return tableOptionList;
    }

    public void setTableOptionList(List<TableOption> tableOptionList) {
        this.tableOptionList = tableOptionList;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            weightSum = this.tableOptionList.stream().mapToInt(it -> it.weight).sum();
        }
        invalidate();
    }

    public Turntable(Context context) {
        super(context);
        init();
    }

    public Turntable(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.Turntable);
        init();
    }

    private void init() {
        initPaint();
        if (typedArray != null) {
            initByTypeArray();
        }
    }

    private void initByTypeArray() {
        int borderColor = typedArray.getColor(R.styleable.Turntable_borderColor, Color.parseColor("#ddd5df"));
        borderPaint.setColor(borderColor);

        int labelColor = typedArray.getColor(R.styleable.Turntable_labelColor, Color.BLACK);
        labelPaint.setColor(labelColor);

        centerIndicator = typedArray.getDrawable(R.styleable.Turntable_centerIndicator);

        indicatorPointAngle = typedArray.getFloat(R.styleable.Turntable_indicatorPointAngle, 0f);

        float labelTextSize = typedArray.getDimension(R.styleable.Turntable_labelTextSize, 28f);
        labelPaint.setTextSize(labelTextSize);

        labelMarinCenter = typedArray.getFloat(R.styleable.Turntable_labelMarginCenter, 0.75f);

        rotateDuration = typedArray.getInt(R.styleable.Turntable_rotateDuration, 2 * 1000);
    }

    private void initPaint() {
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(Color.parseColor("#ddd5df"));

        sectorBgPaint = new Paint();
        sectorBgPaint.setAntiAlias(true);

        labelPaint = new TextPaint();
        labelPaint.setAntiAlias(true);
        labelPaint.setTextAlign(Paint.Align.CENTER);

        dotPaint = new Paint();
        dotPaint.setColor(Color.WHITE);
        dotPaint.setAntiAlias(true);

        indicatorPaint = new Paint();
        indicatorPaint.setColor(Color.RED);
        indicatorPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.getMode(widthMeasureSpec));
        int height = MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.getMode(heightMeasureSpec));
        int size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (tableOptionList == null || tableOptionList.size() == 0) {
            return;
        }
        drawStroke(canvas);
        canvas.scale(0.95f, 0.95f, canvas.getWidth() / 2, canvas.getHeight() / 2);
        drawSectors(canvas);
        drawCenterDot(canvas);
        drawIndicator(canvas);
    }

    private void drawStroke(Canvas canvas) {
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() / 2, borderPaint);
    }


    public void rotate() {
        // prevent multiply click in short time
        stopRotate();

        long period = 1000 / 30;
        int totalAngle = ThreadLocalRandom.current().nextInt(270, 360 + 1) * 5;
        AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();

        interval = Observable.interval(period, TimeUnit.MILLISECONDS)
                .takeUntil(times -> times * period >= rotateDuration)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    if (listener != null) {
                        listener.onRotateStart(preRotateAngle);
                    }
                })
                .subscribe(times -> {
                    float input = times * period * 1.0f / rotateDuration;
                    float interpolation = interpolator.getInterpolation(input);
                    addedAngle = interpolation * totalAngle;
                    invalidate();
                }, throwable -> {}, () -> {
                    Handler handler = new Handler();
                    handler.post(() -> {
                        preRotateAngle = (preRotateAngle + addedAngle) % 360;
                        if (listener != null) {
                            listener.onRotateEnd(preRotateAngle);
                        }
                    });
                });
    }

    private void stopRotate() {
        if (interval != null) {
            interval.dispose();
        }
    }

    public TableOption getResult() {
        float relativePointAngle = getRelativeIndicatorPointAngle();
        int preWeightAngle = 360 / weightSum;

        for (TableOption tableOption : tableOptionList) {
            int maxAngleRange = tableOption.weight * preWeightAngle;
            if (relativePointAngle > maxAngleRange) {
                relativePointAngle -= maxAngleRange;
            } else {
                return tableOption;
            }
        }
        return null;
    }

    private float getRelativeIndicatorPointAngle() {
        float relativePointAngle = (indicatorPointAngle - preRotateAngle) % 360;
        // make relativeAngle is positive
        if (relativePointAngle < 0) {
            relativePointAngle = relativePointAngle + 360;
        }
        return relativePointAngle;
    }

    private void drawIndicator(Canvas canvas) {
        canvas.save();
        canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);

        Bitmap indicator;
        if (centerIndicator != null) {
            indicator = getBitmapFromDrawable(centerIndicator);
        } else {
            indicator = BitmapFactory.decodeResource(getResources(), R.mipmap.center_indicator);
        }

        int width = indicator.getWidth();
        int height = indicator.getHeight();
        canvas.drawBitmap(indicator, -width / 2, -height / 2, null);
        canvas.restore();
    }

    static private Bitmap getBitmapFromDrawable(Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

//    private void drawTriangleIndicator(Canvas canvas) {
//        Path path = new Path();
//        canvas.save();
//        canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
//        path.moveTo(0, -canvas.getHeight() / 2 + indicatorHeight);
//        path.lineTo(-indicatorWeight / 2, -canvas.getHeight() / 2);
//        path.lineTo(indicatorWeight / 2, -canvas.getHeight() / 2);
//        canvas.drawPath(path, indicatorPaint);
//        canvas.restore();
//    }

    private void drawCenterDot(Canvas canvas) {
        if (centerDotRadius == null) {
            centerDotRadius = canvas.getWidth() * 0.03f;
        }
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, centerDotRadius, dotPaint);
    }

    private void drawSectors(Canvas canvas) {
        float startAngle = preRotateAngle + addedAngle;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        for (TableOption tableOption : tableOptionList) {
            float sweepAngle = drawSector(canvas, startAngle, tableOption);
            startAngle += sweepAngle;
        }
    }

    private float drawSector(Canvas canvas, float startAngle, TableOption tableOption) {
        // draw background
        RectF rectF = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
        sectorBgPaint.setColor(tableOption.backgroundColor);
        float sweepAngle = tableOption.weight * 1.0f / weightSum * 360;
        canvas.save();
        canvas.rotate((startAngle + sweepAngle / 2), canvas.getWidth() / 2, canvas.getHeight() / 2);
        canvas.drawArc(rectF, -sweepAngle / 2, sweepAngle, true, sectorBgPaint);

        // draw label
        canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        canvas.rotate(90);
        labelPaint.setColor(tableOption.labelColor);
        canvas.drawText(tableOption.label, 0, -canvas.getHeight() / 2 * labelMarinCenter, labelPaint);

        canvas.restore();
        return sweepAngle;
    }

}
