package ch.sectioninformatique.bataille_navale.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Objects;

import ch.sectioninformatique.bataille_navale.Models.Ship;

import static ch.sectioninformatique.bataille_navale.R.color;
import static ch.sectioninformatique.bataille_navale.R.id;
import static ch.sectioninformatique.bataille_navale.R.layout;
import static ch.sectioninformatique.bataille_navale.R.string;

public class SetGameActivity extends AppCompatActivity implements View.OnClickListener {

    Ship ships[] =new Ship[5];
    boolean placed[]= {false,false,false,false,false};
    /*ArrayList<Integer> colorShip = new ArrayList<>(Arrays.asList(
            color.ship1,
            color.ship2,
            color.ship3,
            color.ship4,
            color.ship5));*/
    int numColor = 0;
    //int nbrCaseShip[] = {2, 3, 3, 4, 5};
    String col[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    String row[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};


    public int phase = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_set_game);

        ships[0]=new Ship((byte)2,'U', color.ship1);
        ships[1]=new Ship((byte)3,'U', color.ship2);
        ships[2]=new Ship((byte)3,'U', color.ship3);
        ships[3]=new Ship((byte)4,'U', color.ship4);
        ships[4]=new Ship((byte)5,'U', color.ship5);

        //les différents éléments graphiques modifiés
        final EditText pseudoText = (EditText) findViewById(id.PseudoEditText);
        final TextView helpText = (TextView) findViewById(id.SetShipHelpMessagePlayer);
        final TableLayout grid = (TableLayout) findViewById(id.Grid);

        //le retour du Intent (variable paramètre transmise entre 2 activities)
        Bundle extras = getIntent().getExtras();
        final int nbrPlayer = extras.getInt("param");

        int P1Color = (int) extras.get("colorP1");
        int P2Color = (int) extras.get("colorP2");

        switch (nbrPlayer) {
            case 1:
                grid.setBackgroundResource(P1Color);
                grid.requestLayout();
                break;
            case 2:
                helpText.setText(getResources().getText(string.InputNamePlayer2));
                pseudoText.setHint(getResources().getText(string.Player2));
                grid.setBackgroundResource(P2Color);
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
                if (testGrid()) { //phase 2 (le tableau et le nom du joueur 1 est validé on passe au deuxième joueur)
                    //utiliser les objets pour créer le joueur, la grie et les bateaux du joueur

                    if (nbrPlayer == 1) {
                        RestartActivity();
                    } else {//ou si c'est déja le joueur 2 close and open game activity
                        Intent intent = new Intent(SetGameActivity.this, GameActivity.class);
                        intent.putExtra("Width", grid.getWidth());
                        startActivityForResult(intent, 1);
                        finish();
                    }
                } else if (pseudoText.isEnabled()) {
                    if (!(pseudoText.getText().toString().equals(""))) { //phase 1 (cache le textview et montre la grid)

                        grid.getLayoutParams().height = grid.getWidth();
                        grid.requestLayout();

                        //pseudoText.setVisibility(View.INVISIBLE);
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
    }
    //fonction de clic sur un des bouttons de la grie
    @Override
    public void onClick(View v) {
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

            //Phase 2 :    le joueur choisis la case pour compléter le bateau ou annuler en re clicant sur le mème
            case 2:

                if (((ColorDrawable) thisButton.getBackground()).getColor() == getResources().getColor(color.cellStartShip)) {
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
    // cette fonction vérifie que toutes les cases entre le point x1 y1 et x2 y2 ne sont pas des cases ship
    public boolean testPossibleShip(int x1, int y1, int x2, int y2) {
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
                /*for (int y = i; y < ships.length - 1&&!placed[y]; y++) {
                    Log.d("asdf", "3");
                }*/
            }
        }
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
        for (int i = 0;i <= ships.length-1;i++) {
            Log.d("qwe 2  ship+"+i,ships[i].getNbCases()+"+"+ placed[i]+" + ("+shipLength);
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
        /*//ancienement on comptais le nombre de case pas de bateaux...
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
        }*/
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
}

