package jp.ac.it_college.std.s14002.android.tetris;

import android.content.Context;
import android.graphics.Canvas;
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
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.holder = holder;
        thread = new Thread();
        thread.start();

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

        canvas = SurfaceHolder.lockCanvas();
        if (canvas == null) {
            return;
        }
        holder.unlockCanvasAndPost(canvas);

    }
}
