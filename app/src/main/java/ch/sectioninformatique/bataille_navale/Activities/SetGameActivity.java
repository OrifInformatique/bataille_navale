package ch.sectioninformatique.bataille_navale.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.Objects;

import static ch.sectioninformatique.bataille_navale.R.*;

public class SetGameActivity extends AppCompatActivity implements View.OnClickListener {

    //final TableLayout grid = (TableLayout) findViewById(id.Grid);
    public int phase = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_set_game);



        //les différents éléments graphiques modifiés
        final EditText pseudoText = (EditText) findViewById(id.PseudoEditText);
        final TextView helpText = (TextView) findViewById(id.SetShipHelpMessagePlayer);
        final TableLayout grid = (TableLayout) findViewById(id.Grid);

        //le retour du Intent (variable paramètre transmise entre 2 activities)
        final int nbrPlayer = getIntent().getIntExtra("param", 0);

        switch (nbrPlayer) {
            case 1:
                grid.setBackgroundColor(getResources().getColor(color.colorPlayer1));
                grid.requestLayout();
                break;
            case 2:
                helpText.setText(getResources().getText(string.InputNamePlayer2));
                pseudoText.setHint(getResources().getText(string.Player2));
                grid.setBackgroundColor(getResources().getColor(color.colorPlayer2));
                grid.requestLayout();

                break;
            default:
                helpText.setText(nbrPlayer);
                grid.setBackgroundColor(Color.BLACK);
                grid.requestLayout();
                break;
        }

        Button returnButton = (Button) findViewById(id.ReturnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button nextButton = (Button) findViewById(id.NextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(testGrid()){ //phase 2 (le tableau et le nom du joueur 1 est validé on passe au deuxième joueur)
                    //utiliser les objets pour créer le joueur, la grie et les bateaux du joueur



                    if(nbrPlayer == 1){
                        RestartActivity();

                    } else {//ou si c'est déja le joueur 2 close and open game activity
                        Intent intent = new Intent(SetGameActivity.this, GameActivity.class);
                        intent .putExtra("Width", grid.getWidth());
                        startActivityForResult(intent,1);
                        finish();
                    }

                    //

                }else if (pseudoText.isEnabled()) {
                    if(!(pseudoText.getText().toString().equals(""))){ //phase 1 (cache le textview et montre la grid)

                        grid.getLayoutParams().height = grid.getWidth();
                        grid.requestLayout();

                        //pseudoText.setVisibility(View.INVISIBLE);
                        pseudoText.getLayoutParams().height = 0;
                        pseudoText.setEnabled(false);
                        pseudoText.requestLayout();

                        helpText.setText(getResources().getText(string.SetShipHelpMessagePlayerStart) + " " + pseudoText.getText() + " " + getResources().getText(string.SetShipHelpMessagePlayerEnd));

                    }else{
                        helpText.setText(getResources().getText(string.NoTextPlaced));
                    }

                }
            }
        });
    }
    //fonction de clic sur un des bouttons de la grie
    @Override
    public void onClick(View v) {
        Button thisButton = (Button) findViewById(v.getId());
        String col[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String row[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        switch (phase) {
            //Phase 1 :   le joueur click pour placer le premier point du bateau
            case 1:

                //si la cellule est vide :
                if (((ColorDrawable) thisButton.getBackground()).getColor() == getResources().getColor(color.cellVoid)) {
                    //elle devient StartShip
                    thisButton.setBackgroundColor(getResources().getColor(color.cellStartShip));

                    for (int y=0;y<10;y++){
                        for (int x=0;x<10;x++){
                            if (Objects.equals(getResources().getResourceEntryName(thisButton.getId()), "btnGridSet_" + col[x] + "" + row[y])){
                                setOtorisedCases(x, y, col, row);
                                phase = 2;
                            }
                        }
                    }
                }
                break;

            //Phase 2 :    le joueur choisis la case pour compléter le bateau ou annuler en re clicant sur le mème
            case 2:

                if (((ColorDrawable) thisButton.getBackground()).getColor() == getResources().getColor(color.cellStartShip)) {
                    GridClean();
                    phase=1;
                }else if (((ColorDrawable) thisButton.getBackground()).getColor() == getResources().getColor(color.cellProposal)) {
                    completShip(thisButton);
                    GridClean();
                    phase=1;
                }
                break;

        }




    }
    public void setOtorisedCases(int x, int y, String col[], String row[]){
        int nbrCaseShip[] = {2, 3, 3, 4, 5};
        for (int aNbrCaseShip : nbrCaseShip) {

            //définit la couleur des 4 (ou moins) cases à i case du button x y
            //si elle existe (index entre 0 et 9)
                //si la couleur de la case est libre (getResources().getColor(color.cellVoid))
                //si la couleur de la case d'avant est : (getResources().getColor(color.cellShip))
                //                                     ou(getResources().getColor(color.cellStartShip))

            if (x + aNbrCaseShip - 1 <= 9 && x + aNbrCaseShip - 1 >= 0){
                if(((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x + (aNbrCaseShip - 1)] + row[y], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellVoid) &&(
                   ((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x + (aNbrCaseShip - 2)] + row[y], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellProposal) ||
                   ((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x + (aNbrCaseShip - 2)] + row[y], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellStartShip))) {

                    (findViewById(getResources().getIdentifier("btnGridSet_" + col[x + (aNbrCaseShip - 1)] + row[y], "id", getPackageName()))).setBackgroundColor(getResources().getColor(color.cellProposal));
                }
            }
            if (x + 1 - aNbrCaseShip <= 9 && x + 1 - aNbrCaseShip >= 0) {
                if (((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x + (1 - aNbrCaseShip)] + row[y], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellVoid) &&(
                        ((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x + (2 - aNbrCaseShip)] + row[y], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellProposal)||
                        ((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x + (2 - aNbrCaseShip)] + row[y], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellStartShip))) {

                    (findViewById(getResources().getIdentifier("btnGridSet_" + col[x + (1 - aNbrCaseShip)] + row[y], "id", getPackageName()))).setBackgroundColor(getResources().getColor(color.cellProposal));
                }
            }
            if (y + aNbrCaseShip - 1 <= 9 && y + aNbrCaseShip - 1 >= 0) {
                if (((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y + (aNbrCaseShip - 1)], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellVoid) &&(
                        ((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y + (aNbrCaseShip - 2)], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellProposal)||
                        ((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y + (aNbrCaseShip - 2)], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellStartShip))){

                    (findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y + (aNbrCaseShip - 1)], "id", getPackageName()))).setBackgroundColor(getResources().getColor(color.cellProposal));
                }
            }
            if (y + 1 - aNbrCaseShip <= 9 && y + 1 - aNbrCaseShip >= 0) {
                if (((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y + (1 - aNbrCaseShip)], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellVoid) &&(
                        ((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y + (2 - aNbrCaseShip)], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellProposal)||
                        ((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y + (2 - aNbrCaseShip)], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellStartShip))){

                    (findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y + (1 - aNbrCaseShip)], "id", getPackageName()))).setBackgroundColor(getResources().getColor(color.cellProposal));
                }
            }
        }
    }
    public void GridClean(){
        String col[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String row[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        for (int y=0;y<10;y++){
            for (int x=0;x<10;x++){
                if (((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellProposal)) {
                    (findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y], "id", getPackageName()))).setBackgroundColor(getResources().getColor(color.cellVoid));
                }else if(((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellStartShip)) {
                    (findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y], "id", getPackageName()))).setBackgroundColor(getResources().getColor(color.cellVoid));
                }
            }
        }
    }
    public void completShip(Button thisButton){
        //thisButton.setBackgroundColor(getResources().getColor(color.cellShip));
        String col[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String row[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        int shipStartX = 0;
        int shipStartY = 0;
        int shipEndX = 0;
        int shipEndY = 0;
        //on traduit le ThisButton et le ButtonStart par des coordonnées col[] et row[] dans shipStartX/Y et shipEndX/Y
        for (int y=0;y<10;y++) {
            for (int x = 0; x < 10; x++) {
                if (((ColorDrawable) findViewById(getResources().getIdentifier("btnGridSet_" + col[x] + row[y], "id", getPackageName())).getBackground()).getColor() == getResources().getColor(color.cellStartShip)){
                    shipStartX = x;
                    shipStartY = y;
                }else if(Objects.equals(getResources().getResourceEntryName(thisButton.getId()), "btnGridSet_" + col[x] + row[y])){
                    shipEndX = x;
                    shipEndY = y;
                }
            }
        }
        ((TextView) findViewById(id.SetShipHelpMessagePlayer)).setText(getResources().getResourceEntryName(thisButton.getId())+"/r/n "+shipStartX+":"+shipStartY+" || "+shipEndX+":"+shipEndY);
        if (shipStartX==shipEndX){
            if (shipStartY<shipEndY){
                for (int i = shipStartY;i<=shipEndY;i++){
                    findViewById(getResources().getIdentifier("btnGridSet_" + col[shipStartX] + row[i], "id", getPackageName())).setBackgroundColor(getResources().getColor(color.cellShip));
                }
            }else{
                for (int i = shipStartY;i>=shipEndY;i--){
                    findViewById(getResources().getIdentifier("btnGridSet_" + col[shipStartX] + row[i], "id", getPackageName())).setBackgroundColor(getResources().getColor(color.cellShip));
                }
            }
        }else if (shipStartY==shipEndY){
            if (shipStartX<shipEndX){
                for (int i = shipStartX;i<=shipEndX;i++){
                    findViewById(getResources().getIdentifier("btnGridSet_" + col[i] + row[shipStartY], "id", getPackageName())).setBackgroundColor(getResources().getColor(color.cellShip));
                }
            }else{
                for (int i = shipStartX;i>=shipEndX;i--){
                    findViewById(getResources().getIdentifier("btnGridSet_" + col[i] + row[shipStartY], "id", getPackageName())).setBackgroundColor(getResources().getColor(color.cellShip));
                }
            }
        }
    }
    public void RestartActivity(){
        Intent intent = getIntent();
        finish();
        intent.putExtra("param", 2);
        startActivityForResult(intent, 1);
    }
    public boolean testGrid() {
        String col[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String row[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        final TextView helpText = (TextView) findViewById(id.SetShipHelpMessagePlayer);
        int nbrShipCases = 0;

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                // on reconstruit les 100 id des boutons
                int ID = getResources().getIdentifier("btnGridSet_"+col[x]+row[y], "id", getPackageName());
                // on regarde tous ceux qui sont de la couleur "bateaux"
                if(((ColorDrawable)findViewById(ID).getBackground()).getColor()==(getResources().getColor(color.cellShip))){
                    nbrShipCases++;
                }
            }
        }
        helpText.setText(nbrShipCases+"/17");
        //retourne True si il y a 17 cases grises sinon false
        return nbrShipCases == 17;
    }
}

