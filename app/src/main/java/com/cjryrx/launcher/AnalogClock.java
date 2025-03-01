package com.cjryrx.launcher;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class AnalogClock extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int centerX, centerY, radius;

    private int borderColor = 0xFFFFFFFF;
    private int hourHandColor = 0xFFFFFFFF;
    private int minuteHandColor = 0xFFFFFFFF;
    private int secondColor = 0xFFFFFFFF;
    private int backgroundColor = 0xFFFFFFFF;

    private int borderWidth = 20;
    private int secondSize = 20;


    public AnalogClock(Context context) {
        super(context);
        startClock();
    }


    public AnalogClock(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        startClock();
    }

    private void startClock(){
        postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
                startClock();
            }
        }, 1000);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas){
        super.onDraw(canvas);
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        radius = Math.min(centerX, centerY) - 10;
        drawHands(canvas);
    }
    private void drawHands(Canvas canvas){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        float hourAngle = (float) (((hour % 12) * Math.PI / 6 + Math.PI / 360 * minute) - Math.PI / 2);
        float minuteAngle = (float) (minute * Math.PI / 30 - Math.PI / 2);
        float secondAngle = (float) (second * Math.PI / 30 - Math.PI / 2);

        paint.setStrokeCap(Paint.Cap.ROUND);

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(hourHandColor);
        float hourHandLength = radius * 0.3f;
        float hourX = (float) (centerX + Math.cos(hourAngle) * hourHandLength);
        float hourY = (float) (centerY + Math.sin(hourAngle) * hourHandLength);
        canvas.drawLine(centerX, centerY, hourX, hourY, paint);

        paint.setColor(minuteHandColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(borderColor);
        paint.setStrokeJoin(Paint.Join.ROUND);
        float minuteHandLength = radius * 0.5f;
        float minuteX = (float) (centerX + Math.cos(minuteAngle) * minuteHandLength);
        float minuteY = (float) (centerY + Math.sin(minuteAngle) * minuteHandLength);
        canvas.drawLine(centerX, centerY, minuteX, minuteY, paint);

        paint.setColor(secondColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(secondSize);
        paint.setStrokeJoin(Paint.Join.ROUND);
        float secondX = (float) (centerX + Math.cos(secondAngle) * radius * 0.66);
        float secondY = (float) (centerY + Math.sin(secondAngle) * radius * 0.66);
        canvas.drawCircle(secondX, secondY, (float) secondSize / 2, paint);
    }
    public void setBorderColor(int color){
        borderColor = color;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public int getHourHandColor() {
        return hourHandColor;
    }

    public void setHourHandColor(int hourHandColor) {
        this.hourHandColor = hourHandColor;
    }

    public int getMinuteHandColor() {
        return minuteHandColor;
    }

    public void setMinuteHandColor(int minuteHandColor) {
        this.minuteHandColor = minuteHandColor;
    }

    public int getSecondColor() {
        return secondColor;
    }

    public void setSecondColor(int secondColor) {
        this.secondColor = secondColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getSecondSize() {
        return secondSize;
    }

    public void setSecondSize(int secondSize) {
        this.secondSize = secondSize;
    }
}
