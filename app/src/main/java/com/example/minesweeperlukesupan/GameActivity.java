package com.example.minesweeperlukesupan;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.minesweeperlukesupan.model.Board;
import com.example.minesweeperlukesupan.model.Cell;





// ai is used for a lot of logic here. but it did not function nearly how it was supposed to
// i have manually corrected it to a working level
public class GameActivity extends AppCompatActivity {

    // get board, cellButtons, and the colors we will be using for this game
    private Board board;
    private Button[][] cellButtons;

    // colors from settings
    private int coveredColor, uncoveredColor, flaggedColor, mineColor;

    private int numRows, numCols;

    private int minePercent;

    private boolean gameIsOver = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // locate the grid and quit button
        GridLayout grid = findViewById(R.id.gridBoard);
        Button quitButton = findViewById(R.id.buttonQuit);

        loadSettings();

        // create the board to the specs
        board = new Board();
        board.generateBoard(numRows, numCols, minePercent);

        // create the button grid
        cellButtons = new Button[numRows][numCols];

        // configure GridLayout
        grid.setRowCount(numRows);
        grid.setColumnCount(numCols);

        grid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remove listener so it only runs once
                grid.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // Now that the grid has a size, create the buttons
                createButtonGrid(grid);
            }
        });


        // quit button to finish, can be used whenever
        quitButton.setOnClickListener(v -> finish());
    }

    // ai basically does this whole thing. makes it show up so much better.
    private void createButtonGrid(GridLayout grid) {
        // get the measured width and height of the grid
        int gridWidth = grid.getWidth() - grid.getPaddingLeft() - grid.getPaddingRight();
        int gridHeight = grid.getHeight() - grid.getPaddingTop() - grid.getPaddingBottom();

        // calculate the space for one cell, based on rows AND cols
        int cellSizeW = gridWidth / numCols;
        int cellSizeH = gridHeight / numRows;

        // use the smaller dimension to make sure all cells are square and fit
        int cellSize = Math.min(cellSizeW, cellSizeH);

        // define the margin for each button (e.g., 4dp)
        int marginDp = 4;
        int marginPx = (int) (marginDp * getResources().getDisplayMetrics().density);

        // calculate the final button size (cell space - margins)
        int buttonSize = cellSize - (marginPx * 2);

        // generate the buttons
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Button btn = new Button(this);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = buttonSize;
                params.height = buttonSize;
                params.setMargins(marginPx, marginPx, marginPx, marginPx);
                btn.setLayoutParams(params);

                btn.setBackgroundColor(coveredColor);
                btn.setTypeface(Typeface.MONOSPACE);

                // remove the button's internal padding
                btn.setPadding(0, 0, 0, 0);

                // set text size dynamically (e.g., 60% of the button height)
                btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonSize * 0.6f);

                final int r = row;
                final int c = col;

                // click to reveal
                btn.setOnClickListener(v -> revealCell(r, c));

                // long click to flag
                btn.setOnLongClickListener(v -> {
                    toggleFlag(r, c);
                    return true;
                });

                // add button to grid
                cellButtons[row][col] = btn;
                grid.addView(btn);
            }
        }
    }

    // load user prefs
    private void loadSettings() {
        SharedPreferences prefs = getSharedPreferences("MinesweeperSettings", MODE_PRIVATE);

        int[] mineValues = {10, 15, 20};

        int[] colorValues = {
                Color.RED,
                Color.GRAY,
                Color.GREEN,
                Color.YELLOW,
                Color.MAGENTA
        };

        int rowsIndex = prefs.getInt("numRows", 0);
        int colsIndex = prefs.getInt("numCols", 0);
        int minePercentIndex = prefs.getInt("minePercent", 0);

        int coveredIndex = prefs.getInt("coveredColor", 1);
        int uncoveredIndex = prefs.getInt("uncoveredColor", 2);
        int flaggedIndex = prefs.getInt("flaggedColor", 3);
        int mineCellIndex = prefs.getInt("mineCellColor", 0);

        numRows = rowsIndex + 5;
        numCols = colsIndex + 5;

        minePercent = mineValues[minePercentIndex];
        coveredColor = colorValues[coveredIndex];
        uncoveredColor = colorValues[uncoveredIndex];
        flaggedColor = colorValues[flaggedIndex];
        mineColor = colorValues[mineCellIndex];
    }

    // reveal cell on click
    private void revealCell(int row, int col) {
        if (gameIsOver) return;
        Cell cell = board.getCells()[row][col];

        if (cell.isRevealed || cell.isFlagged) return;

        // board revealCell function
        board.revealCell(row, col);

        // update all buttons to match the new cell states
        updateAllButtons();

        // check for mine or win
        if (cell.isMine) {
            Toast.makeText(this, "Boom! Game Over!", Toast.LENGTH_LONG).show();
            revealAllMines();
            gameIsOver = true;
        } else if (board.checkWin()) {
            Toast.makeText(this, "You Win!", Toast.LENGTH_LONG).show();
            gameIsOver = true;
        }
    }

    // toggle the flag on long click
    private void toggleFlag(int row, int col) {
        if (gameIsOver) return;
        board.toggleFlag(row, col);
        updateAllButtons();

        if (board.checkWin()) {
            Toast.makeText(this, "You Win!", Toast.LENGTH_LONG).show();
            gameIsOver = true;
        }
    }


    // update them buttons
    private void updateAllButtons() {
        Cell[][] cells = board.getCells();
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[0].length; c++) {
                Button btn = cellButtons[r][c];
                Cell cell = cells[r][c];

                if (cell.isMine && cell.isRevealed) {
                    btn.setBackgroundColor(mineColor);
                } else if (cell.isRevealed) {
                    btn.setBackgroundColor(uncoveredColor);
                    if (cell.surroundingMines > 0)
                        btn.setText(String.valueOf(cell.surroundingMines));
                } else if (cell.isFlagged) {
                    btn.setBackgroundColor(flaggedColor);
                } else {
                    btn.setBackgroundColor(coveredColor);
                }
            }
        }
    }

    // on a loss, reveal all mines
    private void revealAllMines() {
        Cell[][] cells = board.getCells();
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[0].length; c++) {
                Cell cell = cells[r][c];
                if (cell.isMine) {
                    cell.isRevealed = true;
                }
            }
        }
        updateAllButtons();
    }
}
