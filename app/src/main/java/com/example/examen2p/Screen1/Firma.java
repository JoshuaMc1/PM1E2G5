package com.example.examen2p.Screen1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Firma extends View {
    float posx = 0, posy = 0;
    Path path;
    Paint paint;
    List<Path> paths;
    List<Paint> paints;

    public Firma(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paths = new ArrayList<>();
        paints = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int i = 0;
        for(Path trazo:paths){
            canvas.drawPath(trazo,paints.get(i++));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        posx = event.getX();
        posy = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                paint = new Paint();
                paint.setStrokeWidth(5);
                paint.setARGB(255,140,0,0);
                paint.setStyle(Paint.Style.STROKE);
                paints.add(paint);
                path = new Path();
                path.moveTo(posx, posy);
                paths.add(path);
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                int puntosHistoricos =event.getHistorySize();
                for(int i=0; i<puntosHistoricos;i++){
                    path.lineTo(event.getHistoricalX(i),
                            event.getHistoricalY(i));
                }
                break;
        }
        invalidate();
        return true;
    }
}
