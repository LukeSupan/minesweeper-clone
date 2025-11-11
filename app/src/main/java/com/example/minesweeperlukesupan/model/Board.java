package com.example.minesweeperlukesupan.model;

import java.util.Random;

// ai is used for a lot of logic here. but it did not function nearly how it was supposed to
// i have manually corrected it to a working level
public class Board {
    private int rows, cols, mineCount;
    private Cell[][] cells;

    // generates the board for playing with the specified rows, cols, and mine percentage
    public void generateBoard(int rows, int cols, int minePercent) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];

        // create all needed cells
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new Cell();
            }
        }

        // find number of mines
        this.mineCount = (rows * cols * minePercent) / 100;

        // place mines in random cells
        Random random = new Random();
        int placedMines = 0;
        while (placedMines < mineCount) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);

            // if the random cell isn't a mine, make it a mine
            if (!cells[r][c].isMine) {
                cells[r][c].isMine = true;
                placedMines++;
            }
        }

        // calculate mine surroundingMines for each cell to determine number (countAdjacentMines)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!cells[r][c].isMine) {
                    cells[r][c].surroundingMines = countAdjacentMines(r, c);
                }
            }
        }
    }

    // count mines around each individual cell
    // deltaRow and deltaCol are the change/offset in the row/col
    // this makes it check everything around the cell
    private int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
            for (int deltaCol = -1; deltaCol <= 1; deltaCol++) {
                int r = row + deltaRow;
                int c = col + deltaCol;
                if (r >= 0 && r < rows && c >= 0 && c < cols) {
                    if (cells[r][c].isMine) count++;
                }
            }
        }
        return count;
    }

    // on click, reveal the cell
    public void revealCell(int row, int col) {
        Cell cell = cells[row][col];

        // cancel if it's these, it was an accident
        if (cell.isRevealed || cell.isFlagged) return;

        // otherwise reveal it
        cell.isRevealed = true;

        // if no adjacent mines, recursively reveal the neighbors
        if (cell.surroundingMines == 0 && !cell.isMine) {
            for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
                for (int deltaCol = -1; deltaCol <= 1; deltaCol++) {
                    int r = row + deltaRow;
                    int c = col + deltaCol;
                    if (r >= 0 && r < rows && c >= 0 && c < cols) {
                        if (!cells[r][c].isRevealed) {
                            revealCell(r, c);
                        }
                    }
                }
            }
        }
    }

    // toggle the flag for suspicion
    public void toggleFlag(int row, int col) {
        Cell cell = cells[row][col];
        if (!cell.isRevealed) {
            cell.isFlagged = !cell.isFlagged;
        }
    }

    // check for win, if all non-mines are revealed its a win
    // better way to do this is just a counter. i might come back and do that. but. not breaking anything
    // if you are reading this. i suppose i did not have enough time
    public boolean checkWin() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = cells[r][c];
                // not a win if there are unrevealed cells
                if (!cell.isMine && !cell.isRevealed) return false;
            }
        }
        return true;
    }

    // when you lose, reveal all mines
    public void revealAllMines() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (cells[r][c].isMine) {
                    cells[r][c].isRevealed = true;
                }
            }
        }
    }

    // get cells.
    public Cell[][] getCells() {
        return cells;
    }
}
