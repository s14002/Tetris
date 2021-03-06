package jp.ac.it_college.std.s14002.android.tetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by s14002 on 15/11/11.
 */
public class Board extends SurfaceView implements SurfaceHolder.Callback {
    public static final int FPS = 60;
    public Tetromino fallingTetromino;
    private SurfaceHolder holder;
    private DrawThread thread;
    private Callback callback;
    private Bitmap blocks;
    private Bitmap background;
    private ArrayList<Tetromino> tetrominoList = new ArrayList<>();
    private long count = 0;
    private Tetromino.Type type;
    private int id;
    private LinkedList<Tetromino.Type> queue;


    public Board(Context context) {
        super(context);
        initialize(context);
    }

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public Board(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }


    private void initialize(Context context) {
        getHolder().addCallback(this);
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        blocks = BitmapFactory.decodeResource(context.getResources(), R.drawable.block);
        spawnTetromino();
    }

    public void sendId(int id) {
        this.id = id;
    }

    public void spawnTetromino() {
        fallingTetromino = new Tetromino(this);
        fallingTetromino.setPosition(5, 23);
    }


    public boolean fallTetromino() {
        fallingTetromino.move(Input.Down);
        if (!isValidPosition()) {
            fallingTetromino.undo(Input.Down);
            return false;
        }
        return true;
    }

    public boolean isValidPosition() {
        boolean overlapping = false;
        for (Tetromino fixedTetromino : tetrominoList) {
            if (fallingTetromino.intersect(fixedTetromino)) {
                overlapping = true;
                break;
            }
        }

        return !(overlapping || fallingTetromino.isOutOfBounds());

    }

    private List<Integer> findFullRows() {
        int[] rowCounts = new int[21];
        for (Tetromino fixedTetromino : tetrominoList) {
            for (Coordinate coordinate : fixedTetromino.getCoordinates()) {
                rowCounts[coordinate.y]++;
            }
        }
        ArrayList<Integer> list = new ArrayList<>();
        for (int cy = 0; cy < rowCounts.length; cy++) {
            if (rowCounts[cy] == 10) {
                list.add(cy);
            }
        }
        return list;
    }

    public void getQueue(LinkedList<Tetromino.Type> queue) {
        if (queue.size() != 0) {
            String m = String.valueOf(queue.get(0));
            Log.e("queue :", m);
            this.queue = queue;
        }
    }



    private void clearRows(List<Integer> list) {
        callback.scoreAdd(list.size());
        Collections.reverse(list);
        for (int row : list) {
            clearRow(row);
        }
    }

    private void clearRow(int row) {
        ArrayList<Tetromino> deleteTetromino = new ArrayList<>();
        for (Tetromino tetromino : tetrominoList) {
            if (tetromino.clearRowAndAdjustDown(row) == 0) {
                deleteTetromino.add(tetromino);
            }
        }
        tetrominoList.removeAll(deleteTetromino);
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
        stopThread();
    }

    public void translateCanvasCoordinate(Canvas canvas, RectF rectF, int gx, int gy) {
        float side = canvas.getWidth() / 10.0f;
        gy = 20 - gy;
        rectF.set(side * gx, side * gy, side * (gx + 1), side * (gy + 1));
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas == null) {
            return;
        }

        if (!Tetromino.Type.isBitmapinitialized()) {
            Tetromino.Type.setBlockBitmap(blocks);

        }
        updateGame();
        Paint paint = new Paint();


        // 背景画像の描画処理
        canvas.drawBitmap(background, 0, 0, paint);

//        canvas.drawColor(Color.LTGRAY); // 画面クリア（単色塗りつぶし）

        for (Tetromino tetromino : tetrominoList) {
            tetromino.draw(canvas);
        }
        fallingTetromino.draw(canvas);
    }

    public void send(Input input) {
        fallingTetromino.move(input);
        if (!isValidPosition()) {
            fallingTetromino.undo(input);
        } else if (input == Input.Down) {
            count = 0;
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private void updateGame() {
        if (count++ / (FPS / 2) == 0) {
            return;
        }
        count = 0;

        if (!fallTetromino()) {
            tetrominoList.add(fallingTetromino); //積み重なるようにする
            clearRows(findFullRows());  //ブロックが一行揃ったら消す
            spawnTetromino();
        }
            callback.nextTetromino(queue);

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

    public interface Callback {
//        void stockId(int id);
        void scoreAdd(int score);
        void nextTetromino(LinkedList<Tetromino.Type> queue);

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
