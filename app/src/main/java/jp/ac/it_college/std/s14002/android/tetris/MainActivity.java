package jp.ac.it_college.std.s14002.android.tetris;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements Board.Callback {
    private Board board;
    private Handler handler;
//    private Tetromino.Type type;
//    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        setContentView(R.layout.activity_main);

        Bitmap srcImage = BitmapFactory.decodeResource(getResources(),
                android.R.drawable.ic_media_play);
        Matrix matrix = new Matrix();

        matrix.postRotate(90);
        Bitmap fallImage = Bitmap.createBitmap(srcImage, 0, 0,
                srcImage.getWidth(), srcImage.getHeight(), matrix, true);
        ((ImageButton) findViewById(R.id.fall)).setImageBitmap(fallImage);

        matrix.postRotate(90);
        Bitmap leftImage = Bitmap.createBitmap(srcImage, 0, 0,
                srcImage.getWidth(), srcImage.getHeight(), matrix, true);
        ((ImageButton) findViewById(R.id.left)).setImageBitmap(leftImage);

        board = (Board) findViewById(R.id.board);
        board.setCallback(this);

    }

    public void gameButtonClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                board.send(Input.Left);
                break;
            case R.id.right:
                board.send(Input.Right);
                break;
            case R.id.fall:
                board.send(Input.Down);
                break;
            case R.id.rotate:
                board.send(Input.Rotate);
                break;
          /*  case R.id.keep:
                board.send(Input.Keep);
                break;*/
        }
    }

    public void nextTetromino(final LinkedList<Tetromino.Type> queue) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                String message = String.valueOf(queue.get(0));
                ImageView nextTetromino = (ImageView) findViewById(R.id.nextTetromino);
                switch (message) {
                    case "I":
                        nextTetromino.setImageResource(R.drawable.i_tetro);
                        Log.e("Log :", message + ": case I");
                        break;
                    case "O":
                        nextTetromino.setImageResource(R.drawable.o_tetro);
                        Log.e("Log :", message + ": case O");
                        break;
                    case "S":
                        nextTetromino.setImageResource(R.drawable.s_tetro);
                        Log.e("Log :", message + ": case S");
                        break;
                    case "Z":
                        nextTetromino.setImageResource(R.drawable.z_tetro);
                        Log.e("Log :", message + ": case Z");
                        break;
                    case "L":
                        nextTetromino.setImageResource(R.drawable.l_tetro);
                        Log.e("Log :", message + ": case L");
                        break;
                    case "J":
                        nextTetromino.setImageResource(R.drawable.j_tetro);
                        Log.e("Log :", message + ": case J");
                        break;
                    case "T":
                        nextTetromino.setImageResource(R.drawable.t_tetro);
                        Log.e("Log :", message + ": case T");
                        break;
                }

            }
        });
//        this.id = type.getId();
//        String m = String.valueOf(id);
//        Log.e("Log :", m);
    }

    @Override
    public void scoreAdd(final int score) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                TextView scoreVIew = (TextView) findViewById(R.id.score);
                int current = Integer.parseInt(scoreVIew.getText().toString());
                current += score;
                scoreVIew.setText(String.valueOf(current));
            }
        });
    }
}
