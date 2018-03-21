package com.application.shwetapa.sudokupuzzle.view;


import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.application.shwetapa.sudokupuzzle.R;
import com.application.shwetapa.sudokupuzzle.controller.IEdit;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SudokuCellFragment extends DialogFragment {


    @BindView(R.id.oneTV)
    TextView oneTV;
    @BindView(R.id.twoTV)
    TextView twoTV;
    @BindView(R.id.threeTV)
    TextView threeTV;
    @BindView(R.id.fourTV)
    TextView fourTV;
    @BindView(R.id.fiveTV)
    TextView fiveTV;
    @BindView(R.id.sixTV)
    TextView sixTV;
    @BindView(R.id.sevenTV)
    TextView sevenTV;
    @BindView(R.id.eightTV)
    TextView eightTV;
    @BindView(R.id.nineTV)
    TextView nineTV;
    @BindView(R.id.resetBtn)
    Button resetBtn;
    @BindView(R.id.selectBtn)
    Button selectBtn;
    @BindView(R.id.doneBtn)
    Button doneBtn;

    int mNumber;
    int mCellPosition;

    ArrayList<TextView> mNumberKeys = new ArrayList<>();

    static SudokuCellFragment newInstance(int number, int cellPosition) {
        SudokuCellFragment sudokuCellFragment = new SudokuCellFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        bundle.putInt("cell_position", cellPosition);
        sudokuCellFragment.setArguments(bundle);

        return sudokuCellFragment;
    }

    public SudokuCellFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNumber = getArguments().getInt("number", -1);
        mCellPosition = getArguments().getInt("cell_position", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_sudoku_cell, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNumberKeys.add(oneTV);
        mNumberKeys.add(twoTV);
        mNumberKeys.add(threeTV);
        mNumberKeys.add(fourTV);
        mNumberKeys.add(fiveTV);
        mNumberKeys.add(sixTV);
        mNumberKeys.add(sevenTV);
        mNumberKeys.add(eightTV);
        mNumberKeys.add(nineTV);

        for (int i = 0; i < mNumberKeys.size(); i++) {
            final int value = i;
            mNumberKeys.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mNumber >= 0)
                        setCellSelected(mNumberKeys.get(mNumber), false);
                    if (value >= 0)
                        setCellSelected(mNumberKeys.get(value), true);
                    mNumber = value;
                }
            });
        }

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNumber >= 0)
                    setCellSelected(mNumberKeys.get(mNumber), false);
                mNumber = -1;
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((IEdit) getActivity()).onEditCells(mNumber + 1, mCellPosition);
                dismiss();
            }
        });
    }

    private void setCellSelected(TextView textView, boolean isSelected) {
        if (isSelected) {
            textView.setTextColor(Color.RED);
            textView.setTypeface(null, Typeface.BOLD);
        } else {
            textView.setTextColor(Color.BLACK);
            textView.setTypeface(null, Typeface.NORMAL);
        }
    }

}
