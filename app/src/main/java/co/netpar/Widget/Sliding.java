package co.netpar.Widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class Sliding extends LinearLayout {
    private Paint borderPaint;
    private Paint innerPaint;

    public Sliding(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Sliding(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.innerPaint = new Paint();
        this.innerPaint.setARGB(225, 75, 75, 75);
        this.innerPaint.setAntiAlias(true);
        this.borderPaint = new Paint();
        this.borderPaint.setARGB(255, 255, 255, 255);
        this.borderPaint.setAntiAlias(true);
        this.borderPaint.setStyle(Style.STROKE);
        this.borderPaint.setStrokeWidth(2.0f);
    }

    public void setInnerPaint(Paint innerPaint) {
        this.innerPaint = innerPaint;
    }

    public void setBorderPaint(Paint borderPaint) {
        this.borderPaint = borderPaint;
    }

    protected void dispatchDraw(Canvas canvas) {
        RectF drawRect = new RectF();
        drawRect.set(0.0f, 0.0f, (float) getMeasuredWidth(), (float) getMeasuredHeight());
        canvas.drawRoundRect(drawRect, 5.0f, 5.0f, this.innerPaint);
        canvas.drawRoundRect(drawRect, 5.0f, 5.0f, this.borderPaint);
        super.dispatchDraw(canvas);
    }
}
