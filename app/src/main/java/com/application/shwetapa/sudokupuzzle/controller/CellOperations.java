package com.application.shwetapa.sudokupuzzle.controller;

import com.application.shwetapa.sudokupuzzle.model.Cell;
import com.application.shwetapa.sudokupuzzle.model.SudokuUtils;

import java.util.ArrayList;

public class CellOperations {

    public ArrayList<Cell> allCells = new ArrayList<>();

    public CellOperations(ArrayList<Cell> allCells) {
        this.allCells = SudokuUtils.copyArray(allCells);
    }

    public CellOperations(CellOperations cellOperations) {
        this.allCells = SudokuUtils.copyArray(cellOperations.allCells);
    }

}
