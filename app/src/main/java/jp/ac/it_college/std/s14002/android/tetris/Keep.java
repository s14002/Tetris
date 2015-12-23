package jp.ac.it_college.std.s14002.android.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by s14002 on 15/11/11.
 */
public class Keep extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private Thread thread;

    public Keep(Context context) {
        super(context);
        initialize(context);
//        getHolder().addCallback(this);
//        holder = getHolder();
    }

    public Keep(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    public Keep(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    private void initialize(Context context) {
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.holder = holder;
        thread = new Thread();
        thread.start();
        Canvas canvas = null;
        canvas = holder.lockCanvas(null);
        draw(canvas);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (canvas == null) {
            return;
        }

        canvas.drawColor(Color.MAGENTA);
        holder.unlockCanvasAndPost(canvas);

    }
}
