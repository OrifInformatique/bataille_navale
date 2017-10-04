package ch.sectioninformatique.bataille_navale.Activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.TaskStackBuilder;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_set_game);

        final TableLayout grid = (TableLayout) findViewById(id.Grid);
        Button returnButton = (Button) findViewById(id.ReturnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button nextButton = (Button) findViewById(id.NextButton);
        final EditText pseudoText = (EditText) findViewById(id.PseudoEditText);
        final TextView helpText = (TextView) findViewById(id.SetShipHelpMessagePlayer);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(testGrid()){ //phase 2 (le tableau et le nom du joueur 1 est validé on passe au deuxième joueur)
                    final TextView helpText = (TextView) findViewById(id.SetShipHelpMessagePlayer);
                    helpText.setText("in coming");

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

    /*// pour empécher la rotation d'écran de vider l'activity
    protected void onSaveInstanceState(Bundle savedInstanceState){

    }*/
    @Override
    public void onClick(View v) {
        Button thisButton = (Button) findViewById(v.getId());
        if (((ColorDrawable) thisButton.getBackground()).getColor() == getResources().getColor(color.cellShip)) {
            thisButton.setBackgroundColor(getResources().getColor(color.cellVoid));
        }
        else {
            thisButton.setBackgroundColor(getResources().getColor(color.cellShip));
        }
    }

    /*//élément de test
        ConstraintLayout constraintLayout3 = (ConstraintLayout) findViewById(id.constraintLayout3);
        constraintLayout3.setBackgroundColor(getResources().getColor(color.colorPlayer2));*/
    //grid.setBackgroundColor(getResources().getColor(color.colorPlayer2));
        /*TextView text = (TextView) findViewById(id.SetShipHelpMessagePlayer);
        ConstraintLayout ll1 =(ConstraintLayout) findViewById(id.GlobalLayout);
        text.setText(ll1.getHeight()+ " : "+ll1.getWidth());
        grid.getLayoutParams().height = (int) ll1.getWidth();

        grid.requestLayout();*/


    public boolean testGrid() {
        String col[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String row[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
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
        final TextView helpText = (TextView) findViewById(id.SetShipHelpMessagePlayer);
        helpText.setText(nbrShipCases+"/17");
        //retourne True si il y a 17 cases grises sinon false
        return nbrShipCases == 17;
    }
}

