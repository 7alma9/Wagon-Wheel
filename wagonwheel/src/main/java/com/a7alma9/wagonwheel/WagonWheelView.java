package com.a7alma9.wagonwheel;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import java.util.ArrayList;

public class WagonWheelView extends View {
    ArrayList<WheelData> wagonWheelData;
    private int GROUND_COLOR = Color.parseColor("#000000");
    private int PITCH_COLOR = Color.parseColor("#EFC88D");
    private int BOUNDARY_COLOR = Color.parseColor("#FFEB3B");
    private int RUNS_COLOR = Color.parseColor("#FFFFFF");
    private int SIXES_COLOR = Color.parseColor("#FF5252");
    private int FOUR_COLOR = Color.parseColor("#E040FB");
    private Paint paint;
    private int radius;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    public WagonWheelView(Context context) {
        super(context);
        init(context, null);
    }


    public WagonWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());


        paint = new Paint();
        paint.setAntiAlias(true);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WagonWheelView);
            GROUND_COLOR = typedArray.getColor(R.styleable.WagonWheelView_ground_color, GROUND_COLOR);
            BOUNDARY_COLOR = typedArray.getColor(R.styleable.WagonWheelView_boundary_color, BOUNDARY_COLOR);
            PITCH_COLOR = typedArray.getColor(R.styleable.WagonWheelView_pitch_color, PITCH_COLOR);
            RUNS_COLOR = typedArray.getColor(R.styleable.WagonWheelView_runs_color, RUNS_COLOR);
            SIXES_COLOR = typedArray.getColor(R.styleable.WagonWheelView_sixes_color, SIXES_COLOR);
            FOUR_COLOR = typedArray.getColor(R.styleable.WagonWheelView_fours_color, FOUR_COLOR);

            typedArray.recycle();
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        int w = getWidth();
        int h = getHeight();

        int pl = getPaddingLeft();
        int pr = getPaddingRight();
        int pt = getPaddingTop();
        int pb = getPaddingBottom();

        int usableWidth = w - (pl + pr);
        int usableHeight = h - (pt + pb);

        radius = Math.min(usableWidth, usableHeight) / 2;
        int cx = pl + (usableWidth / 2);
        int cy = pt + (usableHeight / 2);
        canvas.scale(mScaleFactor, mScaleFactor, cx, cy);
        //main wagon wheel
        paint.setColor(GROUND_COLOR);
        canvas.drawCircle(cx, cy, radius, paint);

        //boundary
        paint.setColor(BOUNDARY_COLOR);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        canvas.drawCircle(cx, cy, (float) (radius * 0.9), paint);


        //pitch on wagon wheel
        Point centerOfCanvas = new Point(w / 2, h / 2);
        int rectW = 80;//pitch width
        int rectH = 200;//pitch height
        int left = centerOfCanvas.x - (rectW / 2);
        int top = centerOfCanvas.y - (rectH / 2);
        int right = centerOfCanvas.x + (rectW / 2);
        int bottom = centerOfCanvas.y + (rectH / 2);
        Rect rect = new Rect(left, top, right, bottom);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(PITCH_COLOR);
        canvas.drawRect(rect, paint);


        //Draw data on wagon wheel
        if (wagonWheelData != null) {
            cy = top + (rectW / 2);
            for (WheelData data : wagonWheelData) {
                int length = data.mag;

                int endX = cx + (int) (Math.cos(Math.toRadians(data.angle)) * length);
                int endY = cy + (int) (Math.sin(Math.toRadians(data.angle)) * length);

                paint.setColor(getColorOfBall(data.run));
                paint.setStrokeWidth(3);
                canvas.drawLine(cx, cy, endX, endY, paint);
            }
        }


        canvas.restore();

    }

    public void setData(ArrayList<WheelData> wagonWheelData) {
        this.wagonWheelData = wagonWheelData;
    }

    public int getRadius() {
        return radius;
    }

    private int getColorOfBall(int run) {
        int color = RUNS_COLOR;
        switch (run) {

            case 4:
                color = FOUR_COLOR;

                break;

            case 6:
                color = SIXES_COLOR;

                break;
            default:
                color = RUNS_COLOR;

                break;
        }

        return color;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(1f, Math.min(mScaleFactor, 5.0f));

            invalidate();
            return true;
        }
    }

}
