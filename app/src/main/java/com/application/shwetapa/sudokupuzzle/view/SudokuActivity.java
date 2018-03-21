package com.application.shwetapa.sudokupuzzle.view;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.shwetapa.sudokupuzzle.R;
import com.application.shwetapa.sudokupuzzle.controller.IEdit;
import com.application.shwetapa.sudokupuzzle.model.Cell;
import com.application.shwetapa.sudokupuzzle.model.SudokuUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SudokuActivity extends AppCompatActivity implements IEdit {

    @BindView(R.id.sudokuLL)
    LinearLayout sudokuLayout;

    @BindView(R.id.checkSudokuTV)
    TextView checkSudokuTV;

    private SudokuUtils sudokuUtils;
    private ArrayList<TextView> cells = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        ButterKnife.bind(this);

        int totalBox = 9;
        sudokuUtils = new SudokuUtils(totalBox);
        initializeGridFormat(totalBox);
        initializeCells();
    }

    private void initializeGridFormat(final int noOfCell) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        for (int i = 0; i < noOfCell; i++) {
            LinearLayout linearRow = new LinearLayout(this, null, R.style.HorizontalRow);
            linearRow.setWeightSum(noOfCell);
            linearRow.setOrientation(LinearLayout.HORIZONTAL);
            linearRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            linearRow.setGravity(Gravity.CENTER);

            for (int j = 0; j < noOfCell; j++) {
                TextView cell = new TextView(this, null, R.style.Text);
                cell.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        width / noOfCell, 1f));
                cell.setGravity(Gravity.CENTER);
                cell.setBackgroundResource(R.drawable.cell_border);

                final int traverseI = i;
                final int traverseJ = j;

                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = traverseI * noOfCell + traverseJ;

                        android.app.FragmentManager fm = getFragmentManager();
                        int number = sudokuUtils.completeCells.get(position).getNumber() - 1;
                        SudokuCellFragment sudokuCellFragment = SudokuCellFragment.newInstance(number, position);
                        sudokuCellFragment.show(fm, "");

                    }
                });
                cells.add(cell);
                linearRow.addView(cell);
            }
            sudokuLayout.addView(linearRow);
        }
    }

    private void initializeCells() {
        setTotalCellBox(1, 5, true);
        setTotalCellBox(4, 2, true);
        setTotalCellBox(7, 3, true);
        setTotalCellBox(9, 2, true);
        setTotalCellBox(14, 1, true);
        setTotalCellBox(15, 7, true);
        setTotalCellBox(17, 8, true);
        setTotalCellBox(18, 4, true);
        setTotalCellBox(20, 7, true);
        setTotalCellBox(21, 6, true);
        setTotalCellBox(32, 5, true);
        setTotalCellBox(36, 5, true);
        setTotalCellBox(37, 2, true);
        setTotalCellBox(43, 4, true);
        setTotalCellBox(44, 7, true);

        setTotalCellBox(48, 7, true);
        setTotalCellBox(59, 3, true);
        setTotalCellBox(60, 5, true);
        setTotalCellBox(62, 4, true);
        setTotalCellBox(63, 3, true);
        setTotalCellBox(65, 6, true);
        setTotalCellBox(66, 5, true);
        setTotalCellBox(71, 1, true);
        setTotalCellBox(73, 9, true);
        setTotalCellBox(76, 7, true);
        setAllSudoku();
    }

    private void setTotalCellBox(int box, int no, boolean isTrue) {
        TextView cell = cells.get(box);

        if (no < 1) {
            cell.setText("");
            cell.setTextColor(Color.BLACK);
            cell.setTypeface(null, Typeface.NORMAL);
            return;
        }

        String string = "" + no;
        cell.setText(string);
        if (isTrue) {
            cell.setTextColor(Color.RED);
            cell.setTypeface(null, Typeface.BOLD);
        } else {
            cell.setTextColor(Color.BLACK);
            cell.setTypeface(null, Typeface.NORMAL);
        }
    }

    private void setAllSudoku() {
        boolean isFirstSetup = sudokuUtils.setCells.isEmpty();
        if (isFirstSetup) {
            for (int i = 0; i < cells.size(); i++) {
                String temp = cells.get(i).getText().toString();
                if (temp.length() != 1)
                    continue;

                int number = Integer.parseInt(cells.get(i).getText().toString());
                sudokuUtils.setCell(i, number, true, true);
            }
        } else {
            for (int i = 0; i < cells.size(); i++) {
                String temp = cells.get(i).getText().toString();
                if (temp.length() != 1)
                    continue;

                int number = Integer.parseInt(cells.get(i).getText().toString());
                boolean isCellKnown = sudokuUtils.completeCells.get(i).isKnown();
                sudokuUtils.setCell(i, number, isCellKnown, false);
            }
        }
    }

    private void setupGridFromSudoku() {
        ArrayList<Cell> cells;
        if (sudokuUtils.cellOperations.isEmpty())
            cells = sudokuUtils.completeCells;
        else
            cells = sudokuUtils.cellOperations.get(0).allCells;
        for (int i = 0; i < cells.size(); i++) {
            Cell cell = cells.get(i);
            setTotalCellBox(i, cell.getNumber(), cell.isKnown());
        }
    }

    public void onNew(View view) {
        sudokuUtils = new SudokuUtils(sudokuUtils.getSize());
        setupGridFromSudoku();
    }

    public void onReset(View view) {
        sudokuUtils.resetAll(sudokuUtils.getSize());
        setupGridFromSudoku();
    }

    public void onSolve(View view) {
        setAllSudoku();
        sudokuUtils.solveSudoku();
        setupGridFromSudoku();
        String statement = "Solutions found: " + sudokuUtils.cellOperations.size();
        checkSudokuTV.setText(statement);
    }

    @Override
    public void onEditCells(int number, int cellPosition) {
        sudokuUtils.completeCells.get(cellPosition).setNumber(number);
        setTotalCellBox(cellPosition, number, sudokuUtils.completeCells.get(cellPosition).isKnown());
    }
}
