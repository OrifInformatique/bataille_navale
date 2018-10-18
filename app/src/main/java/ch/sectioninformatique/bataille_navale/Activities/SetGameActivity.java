package ch.sectioninformatique.bataille_navale.Activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.sectioninformatique.bataille_navale.Models.Case;
import ch.sectioninformatique.bataille_navale.Models.CustomButton;
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
    int numColor = 0;
    String cols[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    String rows[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    List<Ship> noPlacedShips = new ArrayList<Ship>();
    int shipsNumber = 0;
    int gridLength = cols.length;
    CustomButton[][] gridButton = new CustomButton[rows.length][cols.length];
    public int phase = 1;
    public int nbrPlayer;
    //[no Player][x/y][which ship][]
    private EditText pseudoText = null;
    private TextView helpText = null;
    private GridLayout gameGrid = null;
    private ImageButton returnButton = null;
    private Button nextButton = null;

    private int xCenter = 0, yCenter = 0;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_set_game);

        noPlacedShips.add(new Ship((byte)2, 'U', color.ship1, 0, 0));
        noPlacedShips.add(new Ship((byte)3, 'U', color.ship2, 0, 0));
        noPlacedShips.add(new Ship((byte)3, 'U', color.ship3, 0, 0));
        noPlacedShips.add(new Ship((byte)4, 'U', color.ship4, 0, 0));
        noPlacedShips.add(new Ship((byte)5, 'U', color.ship5, 0, 0));
        shipsNumber = noPlacedShips.size();

        // for (int i = 0; i < ships.length;i++) placed[i] = false;

        //region variable declaration
        pseudoText = (EditText) findViewById(id.PseudoEditText);
        helpText = (TextView) findViewById(id.SetShipHelpMessagePlayer);
        gameGrid = (GridLayout) findViewById(id.GameGrid);
        returnButton = (ImageButton) findViewById(id.ReturnButton);
        nextButton = (Button) findViewById(id.NextButton);
        //endregion

        //region recuperation of intent
        final Bundle extras = getIntent().getExtras();
        assert extras != null;
        nbrPlayer = (int) extras.get("param");
        player = (Player)extras.get("player"+(nbrPlayer+1));
        assert player != null;
        //endregion

        //region player who plays properties
        gameGrid.setBackgroundColor(ContextCompat.getColor(this,player.getColor()));
        gameGrid.requestLayout();
        if (nbrPlayer == 1) {
            helpText.setText(getResources().getText(string.InputNamePlayer2));
            pseudoText.setHint(getResources().getText(string.Player2));
        } /*else if (nbrPlayer != 0){
            helpText.setText(nbrPlayer);
            //gameGrid.setBackgroundColor(Color.BLACK);
            gameGrid.requestLayout();

        }*/
        //endregion
        gridButton = constructGrid(gameGrid, this);


        //region set action of returnButton
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player.getPlayerGrid().getShips().size() == 0){
                    if (pseudoText.isEnabled()) {
                        AlertReturnButton();
                    }
                } else {
                    GridReset();
                    gameGrid.setVisibility(View.INVISIBLE);
                    pseudoText.setEnabled(true);
                }
            }
        });
        //endregion

        //region set actions of nextButton
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (testGrid()) {

                    FinaliseGrid();

                    if (nbrPlayer == 0) {

                        Intent intent = new Intent(SetGameActivity.this, SetGameActivity.class);
                        intent.putExtra("param", 1);

                        intent.putExtra("player1", player);
                        intent.putExtra("player2", (Player) extras.get("player2"));

                        startActivityForResult(intent, 1);
                        finish();
                    } else if (nbrPlayer == 2) {

                        Intent intent = new Intent(SetGameActivity.this, MultiplayersGameActivity.class);

                        intent.putExtra("player", player);

                        startActivityForResult(intent, 1);
                        finish();
                    } else {
                        Bundle extras = getIntent().getExtras();
                        assert extras != null;
                        Intent intent = new Intent(SetGameActivity.this, GameActivity.class);

                        intent.putExtra("player1", (Player) extras.get("player1"));
                        intent.putExtra("player2", player);

                        startActivityForResult(intent, 1);
                        finish();
                    }
                } else if (pseudoText.isEnabled()) {
                    String pseudoPlayer = pseudoText.getText().toString();
                    if (!Objects.equals(pseudoPlayer, "")) {

                        player.setName(pseudoPlayer);
                        gameGrid.setVisibility(View.VISIBLE);
                        gameGrid.requestLayout();
                        pseudoText.getLayoutParams().height = 0;
                        pseudoText.setEnabled(false);
                        pseudoText.requestLayout();

                        helpText.setText(getResources().getText(string.SetShipHelpMessagePlayerStart) + " " + player.getName() + " " + getResources().getText(string.SetShipHelpMessagePlayerEnd));

                    } else {
                        helpText.setText(getResources().getText(string.NoTextPlaced));
                    }
                }
            }
        });
        //endregion
    }

    public void FinaliseGrid(){
        for (Ship ship : player.getPlayerGrid().getShips()) {
            switch (ship.getDefaultOrientation()){
                case 'R':
                    for(int x = ship.getX(); x < ship.getX()+ship.getNbCases(); x++){
                        player.getPlayerGrid().getCase(x,ship.getY()).setShip(ship);
                    }
                    break;
                case 'L':
                    for(int x = ship.getX()-ship.getNbCases()+1; x <= ship.getX(); x++){
                        player.getPlayerGrid().getCase(x,ship.getY()).setShip(ship);
                    }
                    break;
                case 'D':
                    for(int y = ship.getY(); y < ship.getY()+ship.getNbCases(); y++){
                        player.getPlayerGrid().getCase(ship.getX(), y).setShip(ship);
                    }
                    break;
                case 'U':
                    for(int y = ship.getY()-ship.getNbCases()+1; y <= ship.getY(); y++){
                        player.getPlayerGrid().getCase(ship.getX(), y).setShip(ship);
                    }
                    break;
            }
        }
    }


    @Override
    public void onClick(View v) {
        CustomButton thisButton = (CustomButton) v;
        if (phase == 1) {
            if (player.getPlayerGrid().getCase(thisButton.getGridX(), thisButton.getGridY()).getEtat() == Case.Etat.Libre) {
                thisButton.setBackgroundResource(color.cellStartShip);
                xCenter = thisButton.getGridX();
                yCenter = thisButton.getGridY();
                player.getPlayerGrid().getCase(xCenter, yCenter).setEtat(Case.Etat.Center);
                setAuthorizedCases(thisButton.getGridX(), thisButton.getGridY());
                phase = 2;
            }

        } else if (phase == 2) {
            if (player.getPlayerGrid().getCase(thisButton.getGridX(), thisButton.getGridY()).getEtat() == Case.Etat.Placable) {
                completeShip(xCenter, yCenter, thisButton.getGridX(), thisButton.getGridY());
                CleanBackground();
            } else if (player.getPlayerGrid().getCase(thisButton.getGridX(), thisButton.getGridY()).getEtat() == Case.Etat.Libre
                    || player.getPlayerGrid().getCase(thisButton.getGridX(), thisButton.getGridY()).getEtat() == Case.Etat.Center) {
                CleanBackground();
            }
            phase = 1;
        }
    }

    public void onBackPressed(){
        returnButton.performClick();
    }

    public void TotalGridReset(){
        while(player.getPlayerGrid().getShips().size() > 0) {
            Ship tempShip = player.getPlayerGrid().getShips().get(0);
            noPlacedShips.add(tempShip);
            player.getPlayerGrid().getShips().remove(0);
        }
    }

    public void AlertReturnButton(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setMessage(R.string.returnAlert)
                .setNegativeButton(R.string.returnNo,null)
                .setPositiveButton(R.string.returnYes, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .create()
                .show();
    }

    public void setAuthorizedCases(int x, int y) {

        for(int xa = Ship.getNbMinCases() + x - 1; xa <= Ship.getNbMaxCases() + x - 1; xa++){
            if(xa >= 0 && xa < cols.length){
                if(player.getPlayerGrid().getCase(xa, y).getEtat() == Case.Etat.Libre){
                    gridButton[xa][y].setBackgroundResource(color.cellNoProposal);
                    player.getPlayerGrid().getCase(xa,y).setEtat(Case.Etat.NoPlacable);
                }
            }
        }
        for(int xa = x - Ship.getNbMinCases() + 1; xa >= x - Ship.getNbMaxCases() + 1; xa--){
            if(xa >= 0 && xa < cols.length){
                if(player.getPlayerGrid().getCase(xa, y).getEtat() == Case.Etat.Libre){
                    gridButton[xa][y].setBackgroundResource(color.cellNoProposal);
                    player.getPlayerGrid().getCase(xa,y).setEtat(Case.Etat.NoPlacable);
                }
            }
        }
        for(int ya = Ship.getNbMinCases() + y - 1; ya <= Ship.getNbMaxCases() + y - 1; ya++){
            if(ya >= 0 && ya < rows.length){
                if(player.getPlayerGrid().getCase(x, ya).getEtat() == Case.Etat.Libre){
                    gridButton[x][ya].setBackgroundResource(color.cellNoProposal);
                    player.getPlayerGrid().getCase(x,ya).setEtat(Case.Etat.NoPlacable);
                }
            }
        }
        for(int ya = y - Ship.getNbMinCases() + 1; ya >= y - Ship.getNbMaxCases() + 1; ya--){
            if(ya >= 0 && ya < rows.length){
                if(player.getPlayerGrid().getCase(x, ya).getEtat() == Case.Etat.Libre){
                    gridButton[x][ya].setBackgroundResource(color.cellNoProposal);
                    player.getPlayerGrid().getCase(x,ya).setEtat(Case.Etat.NoPlacable);
                }
            }
        }
        for (Ship ship : noPlacedShips) {
            boolean collision = false;
            for(int xa = Ship.getNbMinCases() + x - 1; xa <= ship.getNbCases() + x - 1 && !collision; xa++){
                if(xa >= 0 && xa < cols.length) {
                    if(player.getPlayerGrid().getCase(xa, y).getEtat() == Case.Etat.Placed){
                        collision = true;
                    }
                } else {
                    collision = true;
                }
            }
            if(!collision){
                gridButton[ship.getNbCases() + x - 1][y].setBackgroundResource(color.cellProposal);
                player.getPlayerGrid().getCase(ship.getNbCases() + x - 1, y).setEtat(Case.Etat.Placable);
            }
            //--------------------------------------------------------------------------------------
            collision = false;
            for(int xa = x - Ship.getNbMinCases() + 1; xa >= x - ship.getNbCases() + 1 && !collision; xa--){
                if(xa >= 0 && xa < cols.length) {
                    if(player.getPlayerGrid().getCase(xa, y).getEtat() == Case.Etat.Placed){
                        collision = true;
                    }
                } else {
                    collision = true;
                }
            }
            if(!collision){
                gridButton[x - ship.getNbCases() + 1][y].setBackgroundResource(color.cellProposal);
                player.getPlayerGrid().getCase(x - ship.getNbCases() + 1, y).setEtat(Case.Etat.Placable);
            }
            //--------------------------------------------------------------------------------------
            collision = false;
            for(int ya = Ship.getNbMinCases() + y - 1; ya <= ship.getNbCases() + y - 1 && !collision; ya++){
                if(ya >= 0 && ya < cols.length) {
                    if(player.getPlayerGrid().getCase(x, ya).getEtat() == Case.Etat.Placed){
                        collision = true;
                    }
                } else {
                    collision = true;
                }
            }
            if(!collision){
                gridButton[x][ship.getNbCases() + y - 1].setBackgroundResource(color.cellProposal);
                player.getPlayerGrid().getCase(x, ship.getNbCases() + y - 1).setEtat(Case.Etat.Placable);
            }
            //--------------------------------------------------------------------------------------
            collision = false;
            for(int ya = y - Ship.getNbMinCases() + 1; ya >= y - ship.getNbCases() + 1 && !collision; ya--){
                if(ya >= 0 && ya < cols.length) {
                    if(player.getPlayerGrid().getCase(x, ya).getEtat() == Case.Etat.Placed){
                        collision = true;
                    }
                } else {
                    collision = true;
                }
            }
            if(!collision){
                gridButton[x][y - ship.getNbCases() + 1].setBackgroundResource(color.cellProposal);
                player.getPlayerGrid().getCase(x, y - ship.getNbCases() + 1).setEtat(Case.Etat.Placable);
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
        player.getPlayerGrid().getShips().clear();
    }

    public void GridClean() {
        TotalGridReset();

        for (int y = 0; y < gridLength; y++) {
            for (int x = 0; x < gridLength; x++) {
                player.getPlayerGrid().getCase(x,y).setEtat(Case.Etat.Libre);
                gridButton[x][y].setBackgroundResource(color.cellVoid);
            }
        }

        phase = 1;
    }

    public void CleanBackground(){
        for (int y = 0; y < gridLength; y++) {
            for (int x = 0; x < gridLength; x++) {
                if(player.getPlayerGrid().getCase(x,y).getEtat() == Case.Etat.Placable
                || player.getPlayerGrid().getCase(x,y).getEtat() == Case.Etat.NoPlacable
                || player.getPlayerGrid().getCase(x,y).getEtat() == Case.Etat.Center){
                        player.getPlayerGrid().getCase(x,y).setEtat(Case.Etat.Libre);
                        gridButton[x][y].setBackgroundResource(color.cellVoid);
                }
            }
        }
        phase = 1;
    }

    public void completeShip(int x1, int y1, int x2, int y2) {
        int shipLength = 0;
        char shipOrientation = ' ';
        if(x1 < x2){ // Right
            shipLength = x2 - x1 + 1;
            shipOrientation = 'R';
        } else if(x1 > x2){ // Left
            shipLength = x1 - x2 + 1;
            shipOrientation = 'L';
        } else if(y1 < y2){ // Down
            shipLength = y2 - y1 + 1;
            shipOrientation = 'D';
        } else if(y1 > y2){ // Up
            shipLength = y1 - y2 + 1;
            shipOrientation = 'U';
        }

        int index = 0;
        for(int i = 0; i < noPlacedShips.size(); i++) {
            if (shipLength == noPlacedShips.get(i).getNbCases()) {
                index = i;
            }
        }

        Ship tempShip = noPlacedShips.get(index);
        tempShip.setDefaultOrientation(shipOrientation);
        tempShip.setX(x1);
        tempShip.setY(y1);
        player.getPlayerGrid().getShips().add(tempShip);
        noPlacedShips.remove(index);

        switch (shipOrientation){
            case 'R':
                for(int x = x1; x <= x2; x++){
                    player.getPlayerGrid().getCase(x,y1).setEtat(Case.Etat.Placed);
                    gridButton[x][y1].setBackgroundResource(tempShip.getColorShip());
                }
                break;
            case 'L':
                for(int x = x1; x >= x2; x--){
                    player.getPlayerGrid().getCase(x,y1).setEtat(Case.Etat.Placed);
                    gridButton[x][y1].setBackgroundResource(tempShip.getColorShip());
                }
                break;
            case 'D':
                for(int y = y1; y <= y2; y++){
                    player.getPlayerGrid().getCase(x1,y).setEtat(Case.Etat.Placed);
                    gridButton[x1][y].setBackgroundResource(tempShip.getColorShip());
                }
                break;
            case 'U':
                for(int y = y1; y >= y2; y--){
                    player.getPlayerGrid().getCase(x1,y).setEtat(Case.Etat.Placed);
                    gridButton[x1][y].setBackgroundResource(tempShip.getColorShip());
                }
                break;
        }
    }

    public boolean testGrid() {
        int i = player.getPlayerGrid().getShips().size();
        //retourne True si il n'y a plus de bateaux a placer
        helpText.setText(i + "/" + shipsNumber);
        return i == shipsNumber;
    }

    public static CustomButton[][] constructGrid(GridLayout gameGrid,Activity thisActivity) {
        String cols[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String rows[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        CustomButton[][] gridButton = new CustomButton[rows.length][cols.length];
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

        LinearLayout.LayoutParams cellLP = new LinearLayout.LayoutParams((int)side, (int)side);


        LinearLayout.LayoutParams LabelLP = new LinearLayout.LayoutParams((int)side+(int)margin, (int)side+(int)margin);


        screenWidth = (LabelLP.width)*cellSideNumber;


        LinearLayout.LayoutParams gridLP = new LinearLayout.LayoutParams((int) screenWidth, (int) screenWidth);

        //endregion

        //region grid properties
        gameGrid.setColumnCount(cellSideNumber);
        gameGrid.setRowCount(cellSideNumber);
        gameGrid.setLayoutParams(gridLP);

        //endregion

        //region cell properties
        space.setVisibility(View.GONE);
        space.setLayoutParams(LabelLP);
        cellGrid[indexCell] = space;
        indexCell++;
        for (int Col = 0; Col < rows.length; Col++) {
            rowOfChar[Col] = new TextView(thisActivity);
            rowOfChar[Col].setTag("TextView_" + rows[Col]);   // = android:id="@+id/TextView_A"
            rowOfChar[Col].setText(rows[Col]);
            rowOfChar[Col].setHeight((int) side+(int)margin);
            rowOfChar[Col].setWidth((int) side+(int)margin);
            rowOfChar[Col].setGravity(Gravity.CENTER);
            rowOfChar[Col].setBackgroundColor(thisActivity.getResources().getColor(R.color.cellText));
            rowOfChar[Col].setLayoutParams(LabelLP);
            cellGrid[indexCell] = rowOfChar[Col];
            indexCell++;
        }
        for (int Row = 0; Row < rows.length; Row++) {
            columnOfNumber[Row] = new TextView(thisActivity);
            columnOfNumber[Row].setTag("TextView_" + cols[Row]);
            columnOfNumber[Row].setText(cols[Row]);
            columnOfNumber[Row].setHeight((int) side+(int)margin);
            columnOfNumber[Row].setWidth((int) side+(int)margin);
            columnOfNumber[Row].setGravity(Gravity.CENTER);
            columnOfNumber[Row].setBackgroundColor(thisActivity.getResources().getColor(R.color.cellText));
            columnOfNumber[Row].setLayoutParams(LabelLP);
            cellGrid[indexCell] = columnOfNumber[Row];
            indexCell++;
            for (int Col = 0; Col < cols.length; Col++) {
                gridButton[Row][Col] = new CustomButton(thisActivity);
                gridButton[Row][Col].setTag("btnGridSet_" + cols[Row] + rows[Col]);
                gridButton[Row][Col].setId(Integer.parseInt(Row + "00" + Col));
                gridButton[Row][Col].setHeight((int) side);
                gridButton[Row][Col].setWidth((int) side);
                gridButton[Row][Col].setGravity(Gravity.FILL);
                gridButton[Row][Col].setBackgroundColor(thisActivity.getResources().getColor(color.cellVoid));
                gridButton[Row][Col].setOnClickListener((View.OnClickListener) thisActivity);
                gridButton[Row][Col].setLayoutParams(cellLP);
                gridButton[Row][Col].setGridX(Row);
                gridButton[Row][Col].setGridY(Col);
                cellGrid[indexCell] = gridButton[Row][Col];
                indexCell++;
            }
        }

        //endregion

        //region cell add to game grid
        for (View aCellGrid : cellGrid) {

            aCellGrid.setPadding((int) margin/2,(int) margin/2, (int) margin/2, (int) margin/2);
            aCellGrid.setTop(0);
            aCellGrid.setBottom(0);
            aCellGrid.requestLayout();
            gameGrid.addView(aCellGrid);
        }

        gameGrid.requestLayout();
        gameGrid.setVisibility(View.INVISIBLE);

        ObjectAnimator anim = ObjectAnimator.ofFloat(gridButton, "translationY", 1000, 100, 50,10,0);
        anim.setDuration(5000);
        anim.setStartDelay(3000);
        anim.start();

        //endregion

        return gridButton;
    }
}