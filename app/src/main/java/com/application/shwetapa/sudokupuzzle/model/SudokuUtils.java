package com.application.shwetapa.sudokupuzzle.model;

import com.application.shwetapa.sudokupuzzle.controller.CellOperations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SudokuUtils {

    public ArrayList<Cell> completeCells = new ArrayList<>();
    public ArrayList<CellOperations> cellOperations = new ArrayList<>();
    public ArrayList<Cell> setCells = new ArrayList<>();
    private ArrayList<Cell> incompleteCells = new ArrayList<>();
    private ArrayList<ArrayList<Cell>> rows = new ArrayList<>();
    private ArrayList<ArrayList<Cell>> columns = new ArrayList<>();
    private ArrayList<ArrayList<Cell>> boxes = new ArrayList<>();
    private int noCells = 4;

    public SudokuUtils(int n) {
        newSudoku(n);
    }

    private SudokuUtils(SudokuUtils sudokuUtils) {
        this.noCells = sudokuUtils.noCells;
        this.incompleteCells = SudokuUtils.copyArray(sudokuUtils.incompleteCells);
        this.completeCells = SudokuUtils.copyArray(sudokuUtils.completeCells);
        this.initializeAll();
        this.addAllCell();
        for (Cell cell : sudokuUtils.completeCells)
            if (cell.isKnown())
                setCells.add(cell);
    }

    public static ArrayList<Cell> copyArray(ArrayList<Cell> cells) {
        ArrayList<Cell> result = new ArrayList<>();
        for (Cell cell : cells) {
            Cell temp = new Cell(cell.getSize(), cell.getRow(), cell.getColumn(), cell.getPotentials(), cell.isKnown());
            result.add(temp);
        }
        return result;
    }

    // A cell belonging to one row, column and box
    private void initializeAll() {
        initialize(rows);
        initialize(columns);
        initialize(boxes);
    }

    private void initialize(ArrayList<ArrayList<Cell>> cells) {
        for (int i = 0; i < noCells; i++) {
            ArrayList<Cell> arrayCell = new ArrayList<>();
            for (int j = 0; j < noCells; j++)
                arrayCell.add(new Cell());
            cells.add(arrayCell);
        }
    }

    private void newSudoku(int n) {
        resetAll(n);
        setCells.clear();
    }

    private void addAllCell(Cell cell) {
        rows.get(cell.getRow()).set(cell.getColumn(), cell);
        columns.get(cell.getColumn()).set(cell.getRow(), cell);
        boxes.get(cell.getBox()).set(cell.getPositionInBox(), cell);
    }

    private void addAllCell() {
        for (Cell cell : completeCells)
            addAllCell(cell);
    }


    public void resetAll(int no) {
        this.noCells = no;

        cellOperations.clear();
        incompleteCells.clear();
        setCells.clear();
        initializeAll();

        ArrayList<Cell> tempAllCells = new ArrayList<>();

        for (int i = 0; i < noCells; i++) {
            for (int j = 0; j < noCells; j++) {
                boolean isKnown = !this.completeCells.isEmpty() && this.completeCells.get(i * noCells + j).isKnown();
                ArrayList<Integer> potentials = new ArrayList<>();
                if (isKnown && this.completeCells.get(i * noCells + j).getNumber() != -1)
                    potentials.add(this.completeCells.get(i * noCells + j).getNumber());
                Cell cell = new Cell(noCells, i, j, potentials, isKnown);
                tempAllCells.add(cell);
                addAllCell(cell);
                if (cell.isKnown())
                    setCells.add(cell);
                else
                    incompleteCells.add(cell);
            }
        }

        if (!this.completeCells.isEmpty())
            completeCells.clear();

        completeCells = tempAllCells;

    }

    public void setCell(int cellBox, int number, boolean isKnown, boolean isFirstSetup) {
        completeCells.get(cellBox).setKnown(isKnown);
        completeCells.get(cellBox).setNumber(number);
        if (isFirstSetup)
            setCells.add(completeCells.get(cellBox));
    }

    public int getSize() {
        return noCells;
    }

    private void updateCells(Cell cell) {
        if (cell.getNumber() != -1)
            return;

        int[] potentials = new int[noCells];
        int row = cell.getRow(), column = cell.getColumn(), box = cell.getBox();

        // Check cell row
        ArrayList<Cell> rowCells = rows.get(row);
        for (int i = 0; i < rowCells.size(); i++) {
            Cell rowCell = rowCells.get(i);
            if (rowCell.getNumber() != -1)
                potentials[rowCell.getNumber() - 1]++;
        }

        // Check cell column
        ArrayList<Cell> colCells = columns.get(column);
        for (int i = 0; i < colCells.size(); i++) {
            Cell colCell = colCells.get(i);
            if (colCell.getNumber() != -1)
                potentials[colCell.getNumber() - 1]++;
        }

        // Check cell box
        ArrayList<Cell> boxCells = boxes.get(box);
        for (int i = 0; i < boxCells.size(); i++) {
            Cell boxCell = boxCells.get(i);
            if (boxCell.getNumber() != -1)
                potentials[boxCell.getNumber() - 1]++;
        }

        cell.setPotentials(potentials);
    }


    public void solveSudoku() {

        int flag = incompleteCells.size(); // flag to exit loop if we can't solve the sudoku
        do {
            for (int i = 0; i < completeCells.size(); i++) {
                Cell cell = completeCells.get(i);
                updateCells(cell);
                if (cell.getNumber() != -1) {
                    // Completed
                    for (int j = 0; j < incompleteCells.size(); j++) {
                        Cell temp = incompleteCells.get(j);
                        if (temp.getRow() == cell.getRow() && temp.getColumn() == cell.getColumn()) {
                            incompleteCells.remove(j);
                            break;
                        }
                    }
                }
            }

            Collections.sort(incompleteCells, new Comparator<Cell>() {
                public int compare(Cell one, Cell other) {
                    return one.getPotentials().size() - (other.getPotentials().size());
                }
            });
            for (Cell cell : incompleteCells) {
                for (int potential : cell.getPotentials()) {
                    if (checkSingleNoInRow(cell, potential) || checkSingleNoInColumn(cell, potential) ||
                            checkUniqueInNineBox(cell, potential)) {
                        cell.setNumber(potential);
                        break;
                    }
                }
            }
            if (flag == incompleteCells.size())
                break;
            else
                flag = incompleteCells.size();
        } while (incompleteCells.size() > 0);

        if (incompleteCells.size() > 0) {
            // Create multiple states of the Sudoku with different solutions
            // Each solution has a number taken randomly from a cell with a least number of potentials
            Cell cell = incompleteCells.get(0);
            for (int i : cell.getPotentials()) {
                SudokuUtils sudoku = new SudokuUtils(this);
                sudoku.completeCells.get(cell.getPositionInSudokuGrid()).setNumber(i);
                sudoku.solveSudoku();
                for (CellOperations operations : sudoku.cellOperations) {
                    CellOperations cellOp = new CellOperations(operations);
                    cellOperations.add(cellOp);
                    break;
                }
            }

        } else {
            // Check if our solution is correct
            if (checkSolveSudoku()) {
                CellOperations operations = new CellOperations(completeCells);
                cellOperations.add(operations);
            }
        }

    }

    // Check if we have solved the sudoku
    private boolean checkSolveSudoku() {

        // Check Rows
        for (int i = 0; i < rows.size(); i++) {
            ArrayList<Cell> row = rows.get(i);
            int[] temp = new int[getSize()];

            for (int j = 0; j < row.size(); j++) {
                if (row.get(j).getNumber() == -1)
                    return false;
                temp[row.get(j).getNumber() - 1]++;
            }

            for (int j : temp)
                if (j == 0)
                    return false;
        }

        // Check Columns
        for (int i = 0; i < columns.size(); i++) {
            ArrayList<Cell> column = columns.get(i);
            int[] temp = new int[getSize()];

            for (int j = 0; j < column.size(); j++)
                temp[column.get(j).getNumber() - 1]++;

            for (int j : temp)
                if (j == 0)
                    return false;
        }

        // Check Boxes
        for (int i = 0; i < boxes.size(); i++) {
            ArrayList<Cell> box = rows.get(i);
            int[] temp = new int[getSize()];

            for (int j = 0; j < box.size(); j++)
                temp[box.get(j).getNumber() - 1]++;

            for (int j : temp)
                if (j == 0)
                    return false;
        }

        return true;
    }

    private boolean checkSingleNoInRow(Cell cell, int potential) {
        for (Cell tempCell : rows.get(cell.getRow())) {
            if (tempCell.getColumn() != cell.getColumn())
                if (tempCell.getNumber() == potential || tempCell.getPotentials().contains(potential))
                    return false;
        }
        return true;
    }

    private boolean checkSingleNoInColumn(Cell cell, int potential) {
        for (Cell tempCell : columns.get(cell.getColumn())) {
            if (tempCell.getRow() != cell.getRow())
                if (tempCell.getNumber() == potential || tempCell.getPotentials().contains(potential))
                    return false;
        }
        return true;
    }

    private boolean checkUniqueInNineBox(Cell cell, int potential) {
        for (Cell tempCell : boxes.get(cell.getBox())) {
            if (tempCell.getRow() != cell.getRow() || tempCell.getColumn() != cell.getColumn())
                if (tempCell.getNumber() == potential || tempCell.getPotentials().contains(potential))
                    return false;
        }
        return true;
    }
}
