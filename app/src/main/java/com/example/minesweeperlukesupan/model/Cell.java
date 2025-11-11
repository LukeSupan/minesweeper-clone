package com.example.minesweeperlukesupan.model;

// ai is used for a lot of logic here. but it did not function nearly how it was supposed to
// i have manually corrected it to a working level
public class Cell {

    // if the cell is a mine this is true
    public boolean isMine;

    // if the cell is revealed this is true (show either bomb or nice color and number)
    public boolean isRevealed;

    // if the cell is flagged this is true, display flag icon
    public boolean isFlagged;

    // number of surrounding mines, show this when isRevealed is true
    // if its zero, on reveal reveal all tiles touching it
    public int surroundingMines;

    // Optional: constructor
    public Cell() {
        this.isMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.surroundingMines = 0;
    }
}
