package ch.sectioninformatique.bataille_navale.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Objects;

import ch.sectioninformatique.bataille_navale.Models.Ship;
import ch.sectioninformatique.bataille_navale.R;

import static ch.sectioninformatique.bataille_navale.R.color;
import static ch.sectioninformatique.bataille_navale.R.id;
import static ch.sectioninformatique.bataille_navale.R.layout;
import static ch.sectioninformatique.bataille_navale.R.string;

public class SetGameActivity extends AppCompatActivity implements View.OnClickListener {

    //region variable declaration
    Ship ships[] =new Ship[5];
    boolean placed[]= {false, false, false, false, false};
    int numColor = 0;
    String col[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    String row[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    public int phase = 1;
    //endregion



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_set_game);

        // region ship creation
        ships[0]=new Ship((byte)2,'U', color.ship1);
        ships[1]=new Ship((byte)3,'U', color.ship2);
        ships[2]=new Ship((byte)3,'U', color.ship3);
        ships[3]=new Ship((byte)4,'U', color.ship4);
        ships[4]=new Ship((byte)5,'U', color.ship5);
        //endregion

        //region variable declaration
        final EditText pseudoText = (EditText) findViewById(id.PseudoEditText);
        final TextView helpText = (TextView) findViewById(id.SetShipHelpMessagePlayer);
        final TableLayout grid = (TableLayout) findViewById(id.Grid);
        final int nbrPlayer;
        int p1Color;
        int p2Color;
        Button returnButton = (Button) findViewById(id.ReturnButton);
        Button nextButton = (Button) findViewById(id.NextButton);
        //endregion

        //region recuperation of intent
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        nbrPlayer = (int) extras.get("param");
        p1Color = (int) extras.get("colorP1");
        p2Color = (int) extras.get("colorP2");
        //endregion

        //region player who plays properties
        switch (nbrPlayer) {
            case 1:
                grid.setBackgroundResource(p1Color);
                grid.requestLayout();
                break;
            case 2:
                helpText.setText(getResources().getText(string.InputNamePlayer2));
                pseudoText.setHint(getResources().getText(string.Player2));

                grid.setBackgroundResource(p2Color);
                grid.requestLayout();
                break;
            default:
                helpText.setText(nbrPlayer);
                grid.setBackgroundColor(Color.BLACK);
                grid.requestLayout();
                break;
        }
        //endregion

        //region set action of returnButton
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!placed[0]&&!placed[1]&&!placed[2]&&!placed[3]&&!placed[4]) {
                    finish();
                } else {

                    GridReset();
                }
            }
        });
        //endregion

        //region set actions of nextButton
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (testGrid()) {
                    if (nbrPlayer == 1) {
                        RestartActivity();
                    } else {
                        Intent intent = new Intent(SetGameActivity.this, GameActivity.class);
                        startActivityForResult(intent, 1);
                        finish();
                    }
                } else if (pseudoText.isEnabled()) {
                    if (!(pseudoText.getText().toString().equals(""))) {
                        grid.getLayoutParams().height = grid.getWidth();
                        grid.requestLayout();
                        pseudoText.getLayoutParams().height = 0;
                        pseudoText.setEnabled(false);
                        pseudoText.requestLayout();

                        helpText.setText(getResources().getText(string.SetShipHelpMessagePlayerStart) + " " + pseudoText.getText() + " " + getResources().getText(string.SetShipHelpMessagePlayerEnd));

                    } else {
                        helpText.setText(getResources().getText(string.NoTextPlaced));
                    }
                }
            }
        });
        //endregion
    }

    @Override
    public void onClick(View v) {  //fonction de clic sur un des bouttons de la grie
        Button thisButton = (Button) findViewById(v.getId());
        switch (phase) {
            //Phase 1 :   le joueur click pour placer le premier point du bateau
            case 1:
                //si la cellule est vide :
                if (((ColorDrawable) thisButton.getBackground()).getColor() == getResources().getColor(color.cellVoid)) {
                    //elle devient StartShip
                    thisButton.setBackgroundResource(color.cellStartShip);
                    for (int y = 0; y < 10; y++) {
                        for (int x = 0; x < 10; x++) {
                            if (Objects.equals(getResources().getResourceEntryName(thisButton.getId()), "btnGridSet_" + col[x] + "" + row[y])) {
                                setAuthorizedCases(x, y);
                                phase = 2;
                            }
                        }
                    }
                }
                break;

            //Phase 2 :    le joueur choisis la case pour compléter le bateau ou annuler en re clicant sur le mème ou une case void
            case 2:

                if (((ColorDrawable) thisButton.getBackground()).getColor() == getResources().getColor(color.cellStartShip) || ((ColorDrawable) thisButton.getBackground()).getColor() == getResources().getColor(color.cellVoid)) {
                    GridClean();
                    phase = 1;
                } else if (((ColorDrawable) thisButton.getBackground()).getColor() == getResources().getColor(color.cellProposal)) {
                    completeShip(thisButton);
                    GridClean();
                    phase = 1;
                }
                break;
        }
    }

   /* public void onStart() {
        super.onStart();
        constructGrid();
    }*/

    public boolean testPossibleShip(int x1, int y1, int x2, int y2) {// cette fonction vérifie que toutes les cases entre le point x1 y1 et x2 y2 ne sont pas des cases ship
            boolean ok = true;
        while (x2 != x1 || y2 != y1) {
            String a = "" + ((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x2] + row[y2], "id", getPackageName())).getBackground()).getColor();
            for (Ship ship : ships) {
                String b = "" + getResources().getColor(ship.getColorShip());
                if (Objects.equals(a, b)) {
                    ok = false;
                }
            }
            if (x2 > x1) {
                x2--;
            } else if (x2 < x1) {
                x2++;
            } else if (y2 > y1) {
                y2--;
            } else if (y2 < y1) {
                y2++;
            }
        }
        return ok;
    }

    public void setAuthorizedCases(int x, int y) {
        for (int i = ships.length-1;i>=0;i--) {
            int shipLength= ships[i].getNbCases();
            if ((x + shipLength - 1) <= 9 && (x + shipLength - 1) >= 0) {
                if (testPossibleShip(x, y, (x + shipLength - 1), y)) {
                    if (placed[i]){
                        findViewById(getResources().getIdentifier("btnGridSet_" + col[x + shipLength - 1] + row[y], "id", getPackageName())).setBackgroundResource(color.cellNoProposal);
                    }else{
                        findViewById(getResources().getIdentifier("btnGridSet_" + col[x + shipLength - 1] + row[y], "id", getPackageName())).setBackgroundResource(color.cellProposal);
                    }
                }else if(((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x + (shipLength - 1)] + row[y], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellVoid)){
                    findViewById(getResources().getIdentifier("btnGridSet_" + col[x + shipLength - 1] + row[y], "id", getPackageName())).setBackgroundResource(color.cellNoAccessible);
                }
            }
            if ((x + 1 - shipLength) <= 9 && (x + 1 - shipLength) >= 0) {
                if (testPossibleShip(x, y, (x - shipLength + 1), y)) {
                    if (placed[i]){
                        (findViewById(getResources().getIdentifier("btnGridSet_" + col[x + (1 - shipLength)] + row[y], "id", getPackageName()))).setBackgroundResource(color.cellNoProposal);
                    }else{
                        findViewById(getResources().getIdentifier("btnGridSet_" + col[x + 1 - shipLength] + row[y], "id", getPackageName())).setBackgroundResource(color.cellProposal);
                    }
                }else if(((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x + (1 - shipLength)] + row[y], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellVoid)){
                    findViewById(getResources().getIdentifier("btnGridSet_" + col[x + 1 - shipLength] + row[y], "id", getPackageName())).setBackgroundResource(color.cellNoAccessible);
                }
            }
            if ((y + shipLength - 1) <= 9 && (y + shipLength - 1) >= 0) {
                if (testPossibleShip(x, y, x, (y + shipLength - 1))) {

                    if (placed[i]){
                        (findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y + (shipLength - 1)], "id", getPackageName()))).setBackgroundResource(color.cellNoProposal);
                    }else{
                        (findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y + (shipLength - 1)], "id", getPackageName()))).setBackgroundResource(color.cellProposal);
                    }
                }else if(((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y + (shipLength - 1)], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellVoid)){
                    (findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y + (shipLength - 1)], "id", getPackageName()))).setBackgroundResource(color.cellNoAccessible);
                }
            }
            if ((y + 1 - shipLength) <= 9 && (y + 1 - shipLength) >= 0) {
                if (testPossibleShip(x, y, x, (y - shipLength + 1))) {
                    if ( placed[i]) {
                        (findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y + (1 - shipLength)], "id", getPackageName()))).setBackgroundResource(color.cellNoProposal);
                    } else{
                        (findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y + (1 - shipLength)], "id", getPackageName()))).setBackgroundResource(color.cellProposal);
                    }
                }else if(((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y + (1 - shipLength)], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellVoid)){
                    (findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y + (1 - shipLength)], "id", getPackageName()))).setBackgroundResource(color.cellNoAccessible);
                }
            }
        }
    }

    public void GridClean() {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                int colorCase =((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y], "id", getPackageName())).getBackground()).getColor();
                if (  colorCase == getResources().getColor(color.cellProposal)
                        || colorCase == getResources().getColor(color.cellStartShip)
                        || colorCase == getResources().getColor(color.cellNoAccessible)
                        || colorCase == getResources().getColor(color.cellNoProposal)) {
                    (findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y], "id", getPackageName()))).setBackgroundResource(color.cellVoid);
                }
            }
        }
    }

    public void GridReset() {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                int colorCase =((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y], "id", getPackageName())).getBackground()).getColor();
                if (colorCase != getResources().getColor(color.cellVoid)) {
                    (findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y], "id", getPackageName()))).setBackgroundResource(color.cellVoid);
                }
            }
        }
        phase = 1;
        for (int i = 0; i < placed.length; i++) {
            placed[i] = false;
        }
    }

    public void completeShip(Button thisButton) {
        //thisButton.setBackgroundResources(color.cellShip);
        for (int i = 0;i <= ships.length-1;i++) {
            Log.d("qwe    ship "+i,ships[i].getNbCases()+":"+ placed[i]);
        }
        int shipStartX = 0;
        int shipStartY = 0;
        int shipEndX = 0;
        int shipEndY = 0;
        //on traduit le ThisButton et le ButtonStart par des coordonnées col[] et row[] dans shipStartX/Y et shipEndX/Y
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                if (((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellStartShip)) {
                    shipStartX = x;
                    shipStartY = y;
                    Log.d("xy ", x+":"+y);
                } else if (Objects.equals(getResources().getResourceEntryName(thisButton.getId()), "btnGridSet_" + col[x] + row[y])) {
                    shipEndX = x;
                    shipEndY = y;
                    Log.d("xy ", x+":"+y);
                }
            }
        }
        Ship theShip= null;
        int shipLength=Math.abs((shipStartX-shipEndX)+(shipStartY-shipEndY))+1;
        Log.d("asdf    test0","absolue(("+shipStartX+"-"+shipEndX+")+("+shipStartY+"-"+shipEndY+"))+1 = "+shipLength+"(shipLength)");

        int test=0;
        for (int i = 0; i < ships.length; i++) {
            Ship aship = ships[i];//on parcour les 5 bateaux,
            Log.d("asdf", "1  " + aship.getColorShip());
            if (aship.getNbCases() == shipLength) {//si il est égal a la taille du bateau,
                Log.d("asdf", "2");
                if (!placed[i]){
                    Log.d("asdf", "3");
                    theShip = ships[i];//on redéfini le bateau
                    test = i;
                }
            }
        }
        assert theShip != null;
        Log.d("asdf    test1","Ship :"+theShip.getNbCases()+"size:"+shipLength);
        if (shipStartX == shipEndX) {
            if (shipStartY < shipEndY) {
                for (int i = shipStartY; i <= shipEndY; i++) {
                    findViewById(getResources().getIdentifier("btnGridSet_" + col[shipStartX] + row[i], "id", getPackageName())).setBackgroundResource(theShip.getColorShip());
                    theShip.setDefaultOrientation('D');
                    placed[test]=true;
                }
                numColor++;
            } else {
                for (int i = shipStartY; i >= shipEndY; i--) {
                    findViewById(getResources().getIdentifier("btnGridSet_" + col[shipStartX] + row[i], "id", getPackageName())).setBackgroundResource(theShip.getColorShip());
                    theShip.setDefaultOrientation('U');
                    placed[test]=true;
                }
                numColor++;
            }
        } else {
            if (shipStartX < shipEndX) {
                for (int i = shipStartX; i <= shipEndX; i++) {
                    findViewById(getResources().getIdentifier("btnGridSet_" + col[i] + row[shipStartY], "id", getPackageName())).setBackgroundResource(theShip.getColorShip());
                    theShip.setDefaultOrientation('R');
                    placed[test]=true;
                }
                numColor++;
            } else {
                for (int i = shipStartX; i >= shipEndX; i--) {
                    findViewById(getResources().getIdentifier("btnGridSet_" + col[i] + row[shipStartY], "id", getPackageName())).setBackgroundResource(theShip.getColorShip());
                    theShip.setDefaultOrientation('L');
                    placed[test]=true;
                }
                numColor++;
            }
        }
    }

    public void RestartActivity() {
        Intent intent = getIntent();
        finish();
        intent.putExtra("param", 2);
        startActivityForResult(intent, 1);
    }

    public boolean testGrid() {
        final TextView helpText = (TextView) findViewById(id.SetShipHelpMessagePlayer);
        int i = 0;
        for (boolean aPlaced : placed){
            if(aPlaced){
                i++;
            }
        }
        //retourne True si il n'y a plus de bateaux a placer
        helpText.setText(i + "/5");
        return 5-i == 0;
    }

    /*public void constructGrid(){
        //region variable declaration
        final GridLayout GameGrid = (GridLayout) findViewById(id.GameGrid);
        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String[] cols = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        int indexCell = 0;
        int cellSideNumber = rows.length+1;
        int margin = (int) (getResources().getDimension(R.dimen.cellSetMargin) / getResources().getDisplayMetrics().density);
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
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
            for (int Col = 0; Col < cols.length; Col++)
            {
                gridButton[i] = new Button(this);
                gridButton[i].setTag("btnGridSet_" + rows[Row] + cols[Col]);
                gridButton[i].setHeight(side);
                gridButton[i].setWidth(side);
                gridButton[i].setGravity(Gravity.CENTER);
                gridButton[i].setBackgroundColor(getResources().getColor(R.color.cellVoid));
                cellGrid[indexCell]=gridButton[i];indexCell++;
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
    }*/
}

