package com.example.minesweeperlukesupan;

// requirement 2 is met by this settings screen. it meets them all

// there are options for:
/*
    Number of Rows 5-10 (SPINNER)
    Number of columns 5-10 (SPINNER)
    % mines 10 15 and 20 (SPINNER)
    Cell colors with 5 options each (SPINNER FOR EACH)
        covered cell (not revealed)
        uncovered cell (revealed non mine)
        suspected color (not revealed, player says mine)
        mine color (the player revealed a mine, or all mines have been revealed

    Save button (button)

    1x Text View
    7x Spinner
    2x Button
 */


import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    // spinner references
    private Spinner spinnerRows, spinnerCols, spinnerPercentMines,
            spinnerCoveredColor, spinnerUncoveredColor,
            spinnerFlaggedColor, spinnerMineCellColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        // || SECTION TO SET SPINNER COLORS
        // ids for each spinner
        int[] spinnerIds = {
                R.id.spinnerRows,
                R.id.spinnerCols,
                R.id.spinnerPercentMines,
                R.id.spinnerCoveredColor,
                R.id.spinnerUncoveredColor,
                R.id.spinnerFlaggedColor,
                R.id.spinnerMineCellColor
        };

        // resources for each spinner
        int[] arrayResources = {
                R.array.row_column_options,    // rows
                R.array.row_column_options,    // columns
                R.array.mine_percentage_options,  // % mines
                R.array.cell_color_options,    // covered
                R.array.cell_color_options,    // uncovered
                R.array.cell_color_options,    // suspected
                R.array.cell_color_options     // mine
        };

        // loop through all spinners and apply the adapter
        for (int i = 0; i < spinnerIds.length; i++) {
            setupSpinner(spinnerIds[i], arrayResources[i]);
        }


        // || SPINNER SAVING
        // find all spinners
        spinnerRows = findViewById(R.id.spinnerRows);
        spinnerCols = findViewById(R.id.spinnerCols);
        spinnerPercentMines = findViewById(R.id.spinnerPercentMines);
        spinnerCoveredColor = findViewById(R.id.spinnerCoveredColor);
        spinnerUncoveredColor = findViewById(R.id.spinnerUncoveredColor);
        spinnerFlaggedColor = findViewById(R.id.spinnerFlaggedColor);
        spinnerMineCellColor = findViewById(R.id.spinnerMineCellColor);

        // load saved values into spinners
        loadSavedSettings();

        // save button
        Button buttonSave = findViewById(R.id.buttonSettingsSave);

        // on click, save settings then go back to MainActivity
        buttonSave.setOnClickListener(v -> {
            saveSettings();
            finish();
        });

    }

    // apply the colors to the spinner. not sure why this is so difficult. maybe i did it wrong
    // take the spinner id and the list of entries and apply the color to them
    private void setupSpinner(int spinnerId, int arrayResourceId) {
        Spinner spinner = findViewById(spinnerId);
        String[] options = getResources().getStringArray(arrayResourceId);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                options
        );

        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
    }

    // use shared preferences to save the settings, they will be retrieved upon generation of the board
    private void saveSettings() {
        SharedPreferences prefs = getSharedPreferences("MinesweeperSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("numRows", spinnerRows.getSelectedItemPosition());
        editor.putInt("numCols", spinnerCols.getSelectedItemPosition());
        editor.putInt("minePercent", spinnerPercentMines.getSelectedItemPosition());
        editor.putInt("coveredColor", spinnerCoveredColor.getSelectedItemPosition());
        editor.putInt("uncoveredColor", spinnerUncoveredColor.getSelectedItemPosition());
        editor.putInt("flaggedColor", spinnerFlaggedColor.getSelectedItemPosition());
        editor.putInt("mineCellColor", spinnerMineCellColor.getSelectedItemPosition());

        editor.apply();
    }

    // load saved values into the spinners
    private void loadSavedSettings() {
        SharedPreferences prefs = getSharedPreferences("MinesweeperSettings", MODE_PRIVATE);

        spinnerRows.setSelection(prefs.getInt("numRows", 0));
        spinnerCols.setSelection(prefs.getInt("numCols", 0));
        spinnerPercentMines.setSelection(prefs.getInt("minePercent", 0));
        spinnerCoveredColor.setSelection(prefs.getInt("coveredColor", 0));
        spinnerUncoveredColor.setSelection(prefs.getInt("uncoveredColor", 0));
        spinnerFlaggedColor.setSelection(prefs.getInt("flaggedColor", 0));
        spinnerMineCellColor.setSelection(prefs.getInt("mineCellColor", 0));
    }
}
