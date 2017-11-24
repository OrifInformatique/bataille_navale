package ch.sectioninformatique.bataille_navale.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

import ch.sectioninformatique.bataille_navale.Models.Player;
import ch.sectioninformatique.bataille_navale.Models.Ship;
import ch.sectioninformatique.bataille_navale.R;

import static ch.sectioninformatique.bataille_navale.R.color;
import static ch.sectioninformatique.bataille_navale.R.id;
import static ch.sectioninformatique.bataille_navale.R.layout;
import static ch.sectioninformatique.bataille_navale.R.string;

public class SetGameActivity extends AppCompatActivity implements View.OnClickListener {
    
    //region variable declaration
    Player player= new Player();
    Ship ships[] = new Ship[5];
    int numColor = 0;
    boolean placed[] = {false, false, false, false, false};
    String cols[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    String rows[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    int gridLength = cols.length;
    Button[][] gridButton = new Button[rows.length][cols.length];
    public int phase = 1;
    public int nbrPlayer;
    //[no Player][x/y][which ship][]
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_set_game);

        // for (int i = 0; i < ships.length;i++) placed[i] = false;

        // region ship creation
        ships[0] = new Ship((byte) 2, 'U', color.ship1,0,0);
        ships[1] = new Ship((byte) 3, 'U', color.ship2,0,0);
        ships[2] = new Ship((byte) 3, 'U', color.ship3,0,0);
        ships[3] = new Ship((byte) 4, 'U', color.ship4,0,0);
        ships[4] = new Ship((byte) 5, 'U', color.ship5,0,0);
        //endregion

        //region variable declaration
        final EditText pseudoText = (EditText) findViewById(id.PseudoEditText);
        final TextView helpText = (TextView) findViewById(id.SetShipHelpMessagePlayer);
        final GridLayout gameGrid = (GridLayout) findViewById(id.GameGrid);
        Button returnButton = (Button) findViewById(id.ReturnButton);
        Button nextButton = (Button) findViewById(id.NextButton);
        //endregion

        //region recuperation of intent
        final Bundle extras = getIntent().getExtras();
        assert extras != null;
        nbrPlayer = (int) extras.get("param");
        int[] tmpPlayerColor = ((int[]) extras.get("playerColor"));
        assert tmpPlayerColor != null;

        player.setColor(ContextCompat.getColor(this, tmpPlayerColor[nbrPlayer]));
        //endregion

        //region player who plays properties
        gameGrid.setBackgroundColor(player.getColor());
        gameGrid.requestLayout();
        if (nbrPlayer == 1) {
            helpText.setText(getResources().getText(string.InputNamePlayer2));
            pseudoText.setHint(getResources().getText(string.Player2));
        } else if (nbrPlayer != 0){
            helpText.setText(nbrPlayer);
            //gameGrid.setBackgroundColor(Color.BLACK);
            gameGrid.requestLayout();

        }
        //endregion

        gridButton = constructGrid(gameGrid, this);

        //region set action of returnButton
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!placed[0] && !placed[1] && !placed[2] && !placed[3] && !placed[4]) {
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
                    if (nbrPlayer == 0) {

                        Intent intent = getIntent();
                        finish();
                        intent.putExtra("param", 1);

                        byte[] shipLength = new byte[ships.length];
                        char[] shipOrientation = new char[ships.length];
                        int[] shipColor = new int[ships.length];
                        int[] shipStartX = new int[ships.length];
                        int[] shipStartY = new int[ships.length];

                        for (int i = 0; i < ships.length; i++) {
                            shipLength[i] = ships[i].getNbCases();
                            shipOrientation[i] = ships[i].getDefaultOrientation();
                            shipColor[i] = ships[i].getColorShip();
                            shipStartX[i] = ships[i].getX();
                            shipStartY[i] = ships[i].getY();
                        }

                        intent.putExtra("playerColor", (int[]) extras.get("playerColor"));
                        intent.putExtra("playerName", player.getName());
                        intent.putExtra("shipLength", shipLength);
                        intent.putExtra("shipOrientation", shipOrientation);
                        intent.putExtra("shipColor", shipColor);
                        intent.putExtra("shipStartX", shipStartX);
                        intent.putExtra("shipStartY", shipStartY);

                        startActivityForResult(intent, 1);
                    } else {
                        Bundle extras = getIntent().getExtras();
                        assert extras != null;
                        Intent intent = new Intent(SetGameActivity.this, GameActivity.class);


                        byte[] shipLength = new byte[ships.length];
                        char[] shipOrientation = new char[ships.length];
                        int[] shipColor = new int[ships.length];
                        int[] shipStartX = new int[ships.length];
                        int[] shipStartY = new int[ships.length];

                        for (int i = 0; i < ships.length; i++) {
                            shipLength[i] = ships[i].getNbCases();
                            shipOrientation[i] = ships[i].getDefaultOrientation();
                            shipColor[i] = ships[i].getColorShip();
                            shipStartX[i] = ships[i].getX();
                            shipStartY[i] = ships[i].getY();
                        }

                        intent.putExtra("playerColor",  (int[]) extras.get("playerColor"));
                        intent.putExtra("player1Name",  (String) extras.get("p1_Name"));
                        intent.putExtra("player2Name",  player.getName());
                        intent.putExtra("p1shipLength", (byte[]) extras.get("shipLength"));
                        intent.putExtra("p2shipLength", shipLength);
                        intent.putExtra("p1shipOrientation", (char[]) extras.get("shipOrientation"));
                        intent.putExtra("p2shipOrientation", shipOrientation);
                        intent.putExtra("p1shipColor",  (int[]) extras.get("shipColor"));
                        intent.putExtra("p2shipColor",  shipColor);
                        intent.putExtra("p1shipStartX", (int[]) extras.get("shipStartX"));
                        intent.putExtra("p2shipStartX", shipStartX);
                        intent.putExtra("p1shipStartY", (int[]) extras.get("shipStartY"));
                        intent.putExtra("p2shipStartY", shipStartY);

                        startActivityForResult(intent, 1);
                        finish();
                    }
                } else if (pseudoText.isEnabled()) {
                    if (!(pseudoText.getText().toString().equals(""))) {
                        player.setName(""+pseudoText.getText());
                        gameGrid.setVisibility(View.VISIBLE);
                        gameGrid.requestLayout();
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
    public void onClick(View v) {
        Button thisButton = (Button) v;
        if (phase == 1) {
            if (getResources().getColor(color.cellVoid) == ((ColorDrawable) thisButton.getBackground()).getColor()) {
                thisButton.setBackgroundResource(color.cellStartShip);
                for (int x = 0; x < gridLength; x++) {
                    for (int y = 0; y < gridLength; y++) {
                        if (thisButton.getId() == gridButton[x][y].getId()) {
                            setAuthorizedCases(x, y);
                            phase = 2;
                        }
                    }
                }
            }

        } else if (phase == 2) {
            if (((ColorDrawable) thisButton.getBackground()).getColor() == getResources().getColor(color.cellStartShip) || ((ColorDrawable) thisButton.getBackground()).getColor() == getResources().getColor(color.cellVoid)) {
                GridClean();
                phase = 1;
            } else if (((ColorDrawable) thisButton.getBackground()).getColor() == getResources().getColor(color.cellProposal)) {
                completeShip(thisButton);
                GridClean();
                phase = 1;
            }

        }
    }

    public boolean testPossibleShip(int x1, int y1, int x2, int y2) {// cette fonction vérifie que toutes les cases entre le point x1 y1 et x2 y2 ne sont pas des cases ship
        boolean ok = true;
        while (x2 != x1 || y2 != y1) {
            String a = "" + ((ColorDrawable) gridButton[x2][y2].getBackground()).getColor();
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
        for (int i = ships.length - 1; i >= 0; i--) {
            int shipLength = ships[i].getNbCases();
            if ((x + shipLength - 1) <= gridLength - 1 && (x + shipLength - 1) >= 0) {
                if (testPossibleShip(x, y, (x + shipLength - 1), y)) {
                    if (placed[i]) {
                        gridButton[x + shipLength - 1][y].setBackgroundResource(color.cellNoProposal);
                    } else {
                        gridButton[x + shipLength - 1][y].setBackgroundResource(color.cellProposal);
                    }
                } else if (((ColorDrawable) gridButton[x + shipLength - 1][y].getBackground()).getColor() == getResources().getColor(color.cellVoid)) {
                    gridButton[x + shipLength - 1][y].setBackgroundResource(color.cellNoAccessible);
                }
            }
            if ((x + 1 - shipLength) <= gridLength - 1 && (x + 1 - shipLength) >= 0) {
                if (testPossibleShip(x, y, (x - shipLength + 1), y)) {
                    if (placed[i]) {
                        gridButton[x + (1 - shipLength)][y].setBackgroundResource(color.cellNoProposal);
                    } else {
                        gridButton[x + (1 - shipLength)][y].setBackgroundResource(color.cellProposal);
                    }
                } else if (((ColorDrawable) gridButton[x + (1 - shipLength)][y].getBackground()).getColor() == getResources().getColor(color.cellVoid)) {
                    gridButton[x + (1 - shipLength)][y].setBackgroundResource(color.cellNoAccessible);
                }
            }
            if ((y + shipLength - 1) <= gridLength - 1 && (y + shipLength - 1) >= 0) {
                if (testPossibleShip(x, y, x, (y + shipLength - 1))) {
                    if (placed[i]) {
                        gridButton[x][y + (shipLength - 1)].setBackgroundResource(color.cellNoProposal);
                    } else {
                        gridButton[x][y + (shipLength - 1)].setBackgroundResource(color.cellProposal);
                    }
                } else if (((ColorDrawable) gridButton[x][y + (shipLength - 1)].getBackground()).getColor() == getResources().getColor(color.cellVoid)) {
                    gridButton[x][y + (shipLength - 1)].setBackgroundResource(color.cellNoAccessible);
                }
            }
            if ((y + 1 - shipLength) <= gridLength - 1 && (y + 1 - shipLength) >= 0) {
                if (testPossibleShip(x, y, x, (y - shipLength + 1))) {
                    if (placed[i]) {
                        gridButton[x][y + (1 - shipLength)].setBackgroundResource(color.cellNoProposal);
                    } else {
                        gridButton[x][y + (1 - shipLength)].setBackgroundResource(color.cellProposal);
                    }
                } else if (((ColorDrawable) gridButton[x][y + (1 - shipLength)].getBackground()).getColor() == getResources().getColor(color.cellVoid)) {
                    gridButton[x][y + (1 - shipLength)].setBackgroundResource(color.cellNoAccessible);
                }
            }
        }
    }

    public void GridClean() {
        for (int y = 0; y < gridLength; y++) {
            for (int x = 0; x < gridLength; x++) {
                int colorCase = ((ColorDrawable) gridButton[x][y].getBackground()).getColor();
                if (colorCase == getResources().getColor(color.cellProposal)
                        || colorCase == getResources().getColor(color.cellStartShip)
                        || colorCase == getResources().getColor(color.cellNoAccessible)
                        || colorCase == getResources().getColor(color.cellNoProposal)) {
                    gridButton[x][y].setBackgroundResource(color.cellVoid);
                }
            }
        }
    }

    public void GridReset() {
        for (int y = 0; y < gridLength; y++) {
            for (int x = 0; x < gridLength; x++) {
                int colorCase = ((ColorDrawable) gridButton[x][y].getBackground()).getColor();
                if (colorCase != getResources().getColor(color.cellVoid)) {
                    gridButton[x][y].setBackgroundResource(color.cellVoid);
                }
            }
        }
        phase = 1;
        for (int i = 0; i < placed.length; i++) {
            placed[i] = false;
        }
    }

    public void completeShip(Button thisButton) {
        int shipStartX = 0;
        int shipStartY = 0;
        int shipEndX = 0;
        int shipEndY = 0;

        for (int y = 0; y < gridLength; y++) {
            for (int x = 0; x < gridLength; x++) {
                if (((ColorDrawable) gridButton[x][y].getBackground()).getColor() == getResources().getColor(color.cellStartShip)) {
                    shipStartX = x;
                    shipStartY = y;
                } else if (gridButton[x][y].getId() == thisButton.getId()) {
                    shipEndX = x;
                    shipEndY = y;
                }
            }
        }
        Ship theShip = null;
        int shipLength = Math.abs((shipStartX - shipEndX) + (shipStartY - shipEndY)) + 1;
        int test = 0;

        for (int i = 0; i < ships.length; i++) {
            Ship aShip = ships[i];
            if (aShip.getNbCases() == shipLength) {
                if (!placed[i]) {
                    theShip = ships[i];
                    test = i;
                }
            }
        }
        assert theShip != null;
        if (shipStartX == shipEndX) {
            if (shipStartY < shipEndY) {
                theShip.setX(shipStartX);
                theShip.setY(shipStartY);
                for (int i = shipStartY; i <= shipEndY; i++) {
                    gridButton[shipStartX][i].setBackgroundResource(theShip.getColorShip());
                    theShip.setDefaultOrientation('U');
                    placed[test] = true;
                }
                numColor++;
            } else {
                theShip.setX(shipStartX);
                theShip.setY(shipStartY);
                for (int i = shipStartY; i >= shipEndY; i--) {
                    gridButton[shipStartX][i].setBackgroundResource(theShip.getColorShip());
                    theShip.setDefaultOrientation('D');
                    placed[test] = true;
                }
                numColor++;
            }
        } else {
            if (shipStartX < shipEndX) {
                theShip.setX(shipStartX);
                theShip.setY(shipStartY);
                for (int i = shipStartX; i <= shipEndX; i++) {
                    gridButton[i][shipStartY].setBackgroundResource(theShip.getColorShip());
                    theShip.setDefaultOrientation('R');
                    placed[test] = true;
                }
                numColor++;
            } else {
                theShip.setX(shipStartX);
                theShip.setY(shipStartY);
                for (int i = shipStartX; i >= shipEndX; i--) {
                    gridButton[i][shipStartY].setBackgroundResource(theShip.getColorShip());
                    theShip.setDefaultOrientation('L');
                    placed[test] = true;
                }
                numColor++;
            }
        }
    }

    public boolean testGrid() {
        final TextView helpText = (TextView) findViewById(id.SetShipHelpMessagePlayer);
        int i = 0;
        for (boolean aPlaced : placed) {
            if (aPlaced) {
                i++;
            }
        }
        //retourne True si il n'y a plus de bateaux a placer
        helpText.setText(i + "/" + ships.length);
        return ships.length - i == 0;
    }

    public static Button[][] constructGrid(GridLayout gameGrid,Activity thisActivity) {
        String cols[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String rows[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        Button[][] gridButton = new Button[rows.length][cols.length];
        //region variable declaration
        int indexCell = 0;
        int cellSideNumber = rows.length + 1;

        float margin = thisActivity.getResources().getDimension(R.dimen.cellSetMargin);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        thisActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float screenWidth = displaymetrics.widthPixels;

        float side = ((screenWidth) - (margin * cellSideNumber)) / cellSideNumber;/////////////

        TextView space = new TextView(thisActivity);
        TextView rowOfChar[] = new TextView[rows.length];
        TextView columnOfNumber[] = new TextView[cols.length];


        View cellGrid[] = new View[(rows.length + 1) * (cols.length + 1)];
        LinearLayout.LayoutParams cellLP = new LinearLayout.LayoutParams((int) side, (int) side);
        cellLP.setMargins((int) margin/2,(int) margin/2, (int) margin/2, (int) margin/2);
        screenWidth = (cellLP.width+(cellLP.rightMargin+cellLP.leftMargin))*cellSideNumber;
        LinearLayout.LayoutParams gridLP = new LinearLayout.LayoutParams((int) screenWidth, (int) screenWidth);

        //endregion

        //region grid properties
        gameGrid.setColumnCount(cellSideNumber);
        gameGrid.setRowCount(cellSideNumber);
        gameGrid.setLayoutParams(gridLP);

        //endregion

        //region cell properties
        space.setVisibility(View.GONE);
        cellGrid[indexCell] = space;
        indexCell++;
        for (int Col = 0; Col < rows.length; Col++) {
            rowOfChar[Col] = new TextView(thisActivity);
            rowOfChar[Col].setTag("TextView_" + rows[Col]);   // = android:id="@+id/TextView_A"
            rowOfChar[Col].setText(rows[Col]);
            rowOfChar[Col].setHeight((int) side);
            rowOfChar[Col].setWidth((int) side);
            rowOfChar[Col].setGravity(Gravity.CENTER);
            rowOfChar[Col].setBackgroundColor(thisActivity.getResources().getColor(R.color.cellText));
            rowOfChar[Col].setLayoutParams(cellLP);
            cellGrid[indexCell] = rowOfChar[Col];
            indexCell++;
        }
        for (int Row = 0; Row < rows.length; Row++) {
            columnOfNumber[Row] = new TextView(thisActivity);
            columnOfNumber[Row].setTag("TextView_" + cols[Row]);
            columnOfNumber[Row].setText(cols[Row]);
            columnOfNumber[Row].setHeight((int) side);
            columnOfNumber[Row].setWidth((int) side);
            columnOfNumber[Row].setGravity(Gravity.CENTER);
            columnOfNumber[Row].setBackgroundColor(thisActivity.getResources().getColor(R.color.cellText));
            columnOfNumber[Row].setLayoutParams(cellLP);
            cellGrid[indexCell] = columnOfNumber[Row];
            indexCell++;
            for (int Col = 0; Col < cols.length; Col++) {
                gridButton[Row][Col] = new Button(thisActivity);
                gridButton[Row][Col].setTag("btnGridSet_" + cols[Row] + rows[Col]);
                gridButton[Row][Col].setId(Integer.parseInt(Row + "00" + Col));
                gridButton[Row][Col].setHeight((int) side);
                gridButton[Row][Col].setWidth((int) side);
                gridButton[Row][Col].setGravity(Gravity.CENTER);
                gridButton[Row][Col].setBackgroundColor(thisActivity.getResources().getColor(color.cellVoid));
                gridButton[Row][Col].setOnClickListener((View.OnClickListener) thisActivity);
                gridButton[Row][Col].setLayoutParams(cellLP);

                cellGrid[indexCell] = gridButton[Row][Col];
                indexCell++;
            }
        }

        //endregion

        //region cell add to game grid
        for (View aCellGrid : cellGrid) {
            aCellGrid.setPadding(0, 0, 0, 0);
            aCellGrid.setTop(0);
            aCellGrid.setBottom(0);
            aCellGrid.setLayoutParams(cellLP);
            aCellGrid.requestLayout();
            gameGrid.addView(aCellGrid);
        }

        gameGrid.requestLayout();
        gameGrid.setVisibility(View.INVISIBLE);
        //endregion
        return gridButton;
    }
}