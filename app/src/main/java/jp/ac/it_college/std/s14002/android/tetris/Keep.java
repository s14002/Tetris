package jp.ac.it_college.std.s14002.android.tetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by s14002 on 15/11/11.
 */

public class Keep extends SurfaceView implements SurfaceHolder.Callback {
    private int FPS = 60;
    public Tetromino fallingTetromino;
    private SurfaceHolder holder;
    private DrawThread thread;
    private Bitmap blocks;
    private Callback callback;
    private ArrayList<Tetromino> tetrominoList = new ArrayList<>();


    public Keep(Context context) {
        super(context);
        initialize(context);

    }

    public Keep(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);

    }

    public Keep(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);

    }

    private void initialize(Context context) {
        getHolder().addCallback(this);
        blocks = BitmapFactory.decodeResource(context.getResources(), R.drawable.block);
        spawnTetromino();
    }

    private void spawnTetromino() {
        fallingTetromino = new Tetromino(this);
        fallingTetromino.setPosition(0, 0);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.holder = holder;
        startThread();

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
        if (!Tetromino.Type.isBitmapinitialized()) {
            Tetromino.Type.setBlockBitmap(blocks);
        }
        for (Tetromino tetromino : tetrominoList) {
            tetromino.draw(canvas);
        }
        getFallingTetromino();
    }

    public Tetromino getFallingTetromino() {
        return fallingTetromino;
    }

    private void startThread() {
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

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
    }

    private class DrawThread extends Thread {
        private boolean isFinished;

        @Override
        public void run() {
            long prevTime = 0;
            while (!isFinished) {
                if (holder == null ||
                        System.currentTimeMillis() - prevTime < 1000 / FPS) {
                    try {
                        sleep(1000 / FPS / 3);
                    } catch (InterruptedException e) {
                        Log.w("DrawThread", e.getMessage(), e);
                    }
                    continue;
                }

                Canvas c = null;
                try {
                    c = holder.lockCanvas(null);
                    synchronized (holder) {
                        draw(c);
                    }
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }
                prevTime = System.currentTimeMillis();
            }
        }
    }
}
