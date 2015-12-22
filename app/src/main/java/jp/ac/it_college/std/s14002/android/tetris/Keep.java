package jp.ac.it_college.std.s14002.android.tetris;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by s14002 on 15/11/11.
 */
public class Keep extends SurfaceView implements SurfaceHolder.Callback {


    public Keep(Context context) {
        super(context);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.holder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void statThread() {
        stopThread();

        thread = new DrawThread();
        thread.start();
    }

    private void stopThread() {
        if (thread != null) {
            thread.isFinished = true;
            thread = null;
        }
    }
}
