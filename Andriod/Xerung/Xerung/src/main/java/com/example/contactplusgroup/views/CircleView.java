package com.example.contactplusgroup.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.mityung.contactdirectory.R;

/**
 * Created by ten-thousand-hours on 8/6/16.
 */
public class CircleView extends View {

    private static final int RED = 0xFFD72D77;
    private static final int WHITE = 0xFFFFFFFF;

    private Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint circlePaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int textPaintBaseLine;
    private Rect targetRect;

    private float circleRadiusProgress = 0f;

    private int maxCircleSize;

    private Bitmap tempBitmap;
    private Canvas tempCanvas;

    private String message = "1";

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init (Context context) {
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(context.getResources().getColor(R.color.red_1));

        circlePaint2.setStyle(Paint.Style.STROKE);
        circlePaint2.setStrokeWidth(1);
        circlePaint2.setColor(WHITE);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxCircleSize = w / 2;
        tempBitmap = Bitmap.createBitmap(getWidth(), getWidth(), Bitmap.Config.ARGB_8888);
        tempCanvas = new Canvas(tempBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        tempCanvas.drawCircle(maxCircleSize, maxCircleSize, maxCircleSize - 4, circlePaint);


        tempCanvas.drawCircle(maxCircleSize, maxCircleSize, maxCircleSize - 4, circlePaint2);

        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(WHITE);
        textPaint.setTextSize(maxCircleSize*8/5);
        Paint.FontMetricsInt fontMetricsInt = textPaint.getFontMetricsInt();
        targetRect = new Rect(0, 0, 2*maxCircleSize,2*maxCircleSize);
        textPaintBaseLine = (targetRect.bottom + targetRect.top - fontMetricsInt.bottom - fontMetricsInt.top) / 2;
        textPaint.setTextAlign(Paint.Align.CENTER);
        tempCanvas.drawText(""+message, targetRect.centerX(), textPaintBaseLine, textPaint);

        canvas.drawBitmap(tempBitmap, 0, 0, null);

    }

    public void setMessageNum (int messageNum) {
        if (messageNum > 0 && messageNum < 10) {
            message = messageNum+"";
        } else {
            message = "0";
        }
        invalidate();
    }

}
