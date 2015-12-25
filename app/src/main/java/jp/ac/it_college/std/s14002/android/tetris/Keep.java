package jp.ac.it_college.std.s14002.android.tetris;

import android.util.Log;

/**
 * Created by s14002 on 15/11/11.
 */

public class Keep {
    private int id;

    public Keep(int id) {
        this.id = id;
        viewTetromino(id);
    }

    public void viewTetromino(int id) {
        String m = String.valueOf(id);
        Log.e("Log :", m);
    }
}