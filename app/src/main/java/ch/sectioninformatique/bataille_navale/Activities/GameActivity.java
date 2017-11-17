package ch.sectioninformatique.bataille_navale.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.sectioninformatique.bataille_navale.R;

import static ch.sectioninformatique.bataille_navale.R.id;
import static ch.sectioninformatique.bataille_navale.R.layout;

public class GameActivity extends AppCompatActivity {


    public void onStart() {
        super.onStart();
        constructGrid();
    }

    public void constructGrid(){
        //region variable declaration
        final GridLayout GameGrid = (GridLayout) findViewById(id.GameGrid);
        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String[] cols = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        int indexCell = 0;
        int cellSideNumber = rows.length+1;
        int margin = (int) (getResources().getDimension(R.dimen.cellSetMargin) / getResources().getDisplayMetrics().density);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        int side = ((screenWidth)-(2*margin*cellSideNumber))/cellSideNumber ;
        TextView space = new TextView(this);
        TextView rowOfChar[]= new TextView[rows.length];
        TextView columnOfNumber[]= new TextView[cols.length];
        Button gridButton[]= new Button[rows.length*cols.length];
        View cellGrid[] = new View[(rows.length+1)*(cols.length+1)];
        LinearLayout.LayoutParams gridLP = new LinearLayout.LayoutParams(screenWidth,screenWidth);
        LinearLayout.LayoutParams cellLP = new LinearLayout.LayoutParams(side,side);
        //endregion

        //region grid properties
        GameGrid.setColumnCount(cellSideNumber);
        GameGrid.setRowCount(cellSideNumber);
        GameGrid.setLayoutParams(gridLP);
        //endregion

        //region cell properties
        space.setVisibility(View.GONE);
        cellGrid[indexCell]=space;indexCell++;

        cellLP.setMargins(margin,margin,margin,margin);
        for (int Col = 0;Col< rows.length;Col++)
        {
            rowOfChar[Col] = new TextView(this);
            rowOfChar[Col].setTag("TextView_"+rows[Col]);   // = android:id="@+id/TextView_A"
            rowOfChar[Col].setText(rows[Col]);
            rowOfChar[Col].setHeight(side);
            rowOfChar[Col].setWidth(side);
            rowOfChar[Col].setGravity(Gravity.CENTER);
            rowOfChar[Col].setBackgroundColor(getResources().getColor(R.color.cellText));
            cellGrid[indexCell]=rowOfChar[Col];indexCell++;
        }
        int i=0;
        for (int Row = 0; Row < rows.length; Row++)
        {
            columnOfNumber[Row] = new TextView(this);
            columnOfNumber[Row].setTag("TextView_"+cols[Row]);
            columnOfNumber[Row].setText(cols[Row]);
            columnOfNumber[Row].setHeight(side);
            columnOfNumber[Row].setWidth(side);
            columnOfNumber[Row].setGravity(Gravity.CENTER);
            columnOfNumber[Row].setBackgroundColor(getResources().getColor(R.color.cellText));
            cellGrid[indexCell]=columnOfNumber[Row];indexCell++;
            for (String col : cols) {
                gridButton[i] = new Button(this);
                gridButton[i].setTag("btnGridSet_" + rows[Row] + col);
                gridButton[i].setHeight(side);
                gridButton[i].setWidth(side);
                gridButton[i].setGravity(Gravity.CENTER);
                gridButton[i].setBackgroundColor(getResources().getColor(R.color.cellVoid));
                cellGrid[indexCell] = gridButton[i];
                indexCell++;
                i++;
            }
        }
        //endregion

        //region cell add to game grid
        for (View aCellGrid : cellGrid) {
            aCellGrid.setPadding(0,0,0,0);
            aCellGrid.setTop(0);
            aCellGrid.setBottom(0);
            aCellGrid.setLayoutParams(cellLP);
            aCellGrid.requestLayout();
            GameGrid.addView(aCellGrid);
        }
        GameGrid.setBackgroundColor(getResources().getColor(R.color.color4));
        GameGrid.requestLayout();
        //endregion
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_game);

        final Button returnButton = (Button) findViewById(id.ReturnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
