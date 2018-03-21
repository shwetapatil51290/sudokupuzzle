package com.application.shwetapa.sudokupuzzle.model;

import java.util.ArrayList;

public class Cell {

    private int r = 1, c = 1, b = 1, n = 4;
    private boolean isKnown = false;
    private ArrayList<Integer> potentials = new ArrayList<>(); // From 1 to 9

    Cell() {

    }

    Cell(int n, int r, int c, ArrayList<Integer> potentials, boolean isKnown) {
        this.n = n;
        this.r = ++r;
        this.c = ++c;
        this.b = getBoxFromColumnAndRow(c, r);
        this.isKnown = isKnown;
        if (potentials == null || potentials.isEmpty())
            initPotential(n);
        else
            setPotentials(potentials);
    }

    public int getNumber() {
        if (this.potentials != null && this.potentials.size() == 1)
            return potentials.get(0);
        else
            return -1;
    }

    public void setNumber(int a) {
        if (a <= 0) {
            setKnown(false);
            initPotential(n);
            return;
        }
        ArrayList<Integer> number = new ArrayList<>();
        number.add(a);
        setPotentials(number);
    }

    private void setPotentials(ArrayList<Integer> potentials) {
        this.potentials = new ArrayList<>(potentials);
    }

    void setPotentials(int[] potentials) {
        this.potentials = new ArrayList<>();
        for (int i = 0; i < potentials.length; i++)
            if (potentials[i] == 0)
                this.potentials.add(i + 1);
    }

    ArrayList<Integer> getPotentials() {
        return this.potentials;
    }


    private void initPotential(int n) {
        potentials.clear();
        for (int i = 1; i <= n; i++)
            potentials.add(i);
    }

    private int getBoxFromColumnAndRow(int c, int r) {
        int temp = (int) Math.sqrt(n);
        int k = c % temp == 0 ? 0 : 1, l = r % temp == 0 ? -1 : 0;
        return c / temp + k + temp * (r / temp + l);
    }

    int getRow() {
        return r - 1;
    }

    int getColumn() {
        return c - 1;
    }

    int getBox() {
        return b - 1;
    }

    int getPositionInBox() {
        int temp = (int) Math.sqrt(n);
        int i = r % temp, j = c % temp;
        if (i == 0)
            i = temp;
        if (j == 0)
            j = temp;
        return (i - 1) * temp + j - 1;
    }

    int getPositionInSudokuGrid() {
        return (r - 1) * n + c - 1;
    }

    int getSize() {
        return n;
    }

    public boolean isKnown() {
        return isKnown;
    }

    void setKnown(boolean known) {
        isKnown = known;
    }

}
