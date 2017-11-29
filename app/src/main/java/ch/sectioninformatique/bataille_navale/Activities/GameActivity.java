package ch.sectioninformatique.bataille_navale.Activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import ch.sectioninformatique.bataille_navale.Models.Player;
import ch.sectioninformatique.bataille_navale.Models.Ship;
import ch.sectioninformatique.bataille_navale.R;

import static ch.sectioninformatique.bataille_navale.R.id;
import static ch.sectioninformatique.bataille_navale.R.layout;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{
    String cols[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    String rows[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    int nbrShip = 5;
    Player[] player = {new Player(), new Player()};
    Ship[][] shipsPlayer = new Ship[player.length][nbrShip];
    int phase =1;
    GridLayout gameGrid;
    Button[][] gridButton = new Button[10][10];
    int playerTurn = 0;
    int playerNotTurn = 1;
    TextView infoText;


    public void onStart() {
        super.onStart();

        gameGrid = (GridLayout) findViewById(R.id.GameGrid);
        gridButton = SetGameActivity.constructGrid(gameGrid, this);
        CreatePlayers();

        gameGrid.setBackgroundColor(ContextCompat.getColor(this, player[playerTurn].getColor()));
        gameGrid.setVisibility(View.VISIBLE);
        infoText = (TextView) findViewById(id.InfoText);
        infoText.setText(getResources().getText(R.string.playMessage1)+" "+player[playerNotTurn].getName()+" "+getResources().getText(R.string.playMessage2));
    }

    @Override
    public void onClick(View v) {

        final Button thisButton = (Button) v;
        Button lunchButton = (Button) findViewById(R.id.LunchButton);
        if (phase == 1){
            if (((ColorDrawable) thisButton.getBackground()).getColor()==getResources().getColor(R.color.cellVoid)){
                CleanSelection();
                thisButton.setBackgroundColor(ContextCompat.getColor(this, R.color.cellSelectForLunch));

                lunchButton.setEnabled(true);
                lunchButton.setBackgroundColor(getResources().getColor(R.color.buttonLunch));


                lunchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        LunchMissile(player[playerTurn]);

                    }
                });
            }else if(((ColorDrawable) thisButton.getBackground()).getColor()==getResources().getColor(R.color.cellSelectForLunch)){
                CleanSelection();
                lunchButton.setEnabled(false);
                lunchButton.setBackgroundColor(getResources().getColor(R.color.buttonNoEnabled));
            }
        }
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

   /*// public void Transition() {

        Button lunchButton = (Button) findViewById(id.LunchButton);
        lunchButton.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonNext));
        lunchButton.setText(R.string.NextButton);



        lunchButton.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonNoEnabled));

        int tmp = playerNotTurn;
        playerNotTurn = playerTurn ;
        playerTurn = tmp;

        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        ObjectAnimator anim = ObjectAnimator.ofFloat(gameGrid, "alpha", 0, 255 );
        anim.setInterpolator(bounceInterpolator);
        anim.setDuration(2000);
        anim.start();


        LoadGrid(player[playerTurn]);
        gameGrid.setBackgroundColor(ContextCompat.getColor(this, player[playerTurn].getColor()));

        gameGrid.requestLayout();
         }*/

    public void LoadGrid(Player thisPlayer){
        for (int x = 0; x < rows.length;x++) {
            for (int y = 0; y < cols.length; y++) {
                if(!thisPlayer.getPlayerGrid().getCase(x,y).getTouched()){
                    gridButton[x][y].setBackgroundColor(ContextCompat.getColor(this,R.color.cellVoid));
                }else{
                    if (thisPlayer.getPlayerGrid().getCase(x,y).isShipPlaced()){
                        if(thisPlayer.getPlayerGrid().getCase(x,y).getShip().isSinking()) {
                            gridButton[x][y].setBackgroundColor(player[playerTurn].getPlayerGrid().getCase(x,y).getShip().getColorShip());

                        }else{
                            gridButton[x][y].setBackgroundColor(ContextCompat.getColor(this,R.color.cellTouchShip));

                        }
                    }else{
                        gridButton[x][y].setBackgroundColor(ContextCompat.getColor(this,R.color.cellMissed));

                    }
                }
            }
        }
    }

    public void CreatePlayers(){
        final Bundle extras = getIntent().getExtras();
        assert extras != null;

        byte[] tapShipLength1 = ((byte[]) extras.get("p1shipLength"));
        byte[] tapShipLength2 = ((byte[]) extras.get("p2shipLength"));
        char[] tmpShipOr1 =     ((char[]) extras.get("p1shipOrientation"));
        char[] tmpShipOr2 =     ((char[]) extras.get("p2shipOrientation"));
        int[] tmpShipColor1 =   ((int[]) extras.get("p1shipColor"));
        int[] tmpShipColor2 =   ((int[]) extras.get("p2shipColor"));
        int[] tmpShipStartX1 =  ((int[]) extras.get("p1shipStartX"));
        int[] tmpShipStartX2 =  ((int[]) extras.get("p2shipStartX"));
        int[] tmpShipStartY1 =  ((int[]) extras.get("p1shipStartY"));
        int[] tmpShipStartY2 =  ((int[]) extras.get("p2shipStartY"));

        assert tapShipLength1 != null;
        assert tapShipLength2 != null;
        assert tmpShipOr1 != null;
        assert tmpShipOr2 != null;
        assert tmpShipColor1 != null;
        assert tmpShipColor2 != null;
        assert tmpShipStartX1 != null;
        assert tmpShipStartX2 != null;
        assert tmpShipStartY1 != null;
        assert tmpShipStartY2 != null;

        for (int i = 0; i< nbrShip; i++){

            int[] tmp2 = ((int[])extras.get("playerColor"));
            assert tmp2 != null;
            int[] tmp3 = ((int[])extras.get("playerColor"));
            assert tmp3 != null;
            shipsPlayer[0][i]= new Ship(tapShipLength1[i],tmpShipOr1[i],tmpShipColor1[i],tmpShipStartX1[i],tmpShipStartY1[i]);
            shipsPlayer[1][i]= new Ship(tapShipLength2[i],tmpShipOr2[i],tmpShipColor2[i],tmpShipStartX2[i],tmpShipStartY2[i]);
            shipsPlayer[0][i].setNbHit((byte)0);
            shipsPlayer[1][i].setNbHit((byte)0);
        }
        player[0].setName((String) extras.get("player1Name"));
        player[1].setName((String) extras.get("player2Name"));
        TextView infoText = (TextView) findViewById(id.InfoText);

        int[] tmpColor =  ((int[])extras.get("playerColor"));

        if (tmpColor != null) {
            player[0].setColor(tmpColor[0]);
            player[1].setColor(tmpColor[1]);
        }else{
            player[0].setColor(getResources().getColor(R.color.color1));
            player[1].setColor(getResources().getColor(R.color.color2));
        }
        for (int i = 0; i < player.length; i++) {
            for (int s = 0; s < nbrShip; s++) {
                switch (shipsPlayer[i][s].getDefaultOrientation()) {
                    case 'U':
                        for (int y = shipsPlayer[i][s].getY(); y < (shipsPlayer[i][s].getY() + shipsPlayer[i][s].getNbCases()); y++) {
                            player[i].getPlayerGrid().getCase(shipsPlayer[i][s].getX(), y).setShip(shipsPlayer[i][s]);
                        }
                        break;
                    case 'R':
                        for (int x = shipsPlayer[i][s].getX(); x < (shipsPlayer[i][s].getX() + shipsPlayer[i][s].getNbCases()); x++) {
                            player[i].getPlayerGrid().getCase(x, shipsPlayer[i][s].getY()).setShip(shipsPlayer[i][s]);
                        }
                        break;
                    case 'D':
                        for (int y = shipsPlayer[i][s].getY(); y > (shipsPlayer[i][s].getY() - shipsPlayer[i][s].getNbCases()); y--) {
                            player[i].getPlayerGrid().getCase(shipsPlayer[i][s].getX(), y).setShip(shipsPlayer[i][s]);
                        }
                        break;
                    case 'L':
                        for (int x = shipsPlayer[i][s].getX(); x > (shipsPlayer[i][s].getX() - shipsPlayer[i][s].getNbCases()); x--) {
                            player[i].getPlayerGrid().getCase(x, shipsPlayer[i][s].getY()).setShip(shipsPlayer[i][s]);
                        }
                        break;
                    default://error;
                }
            }
        }
    }
    public boolean CheckWin(){
        int counter = 0;
        for (int x = 0; x < rows.length; x++){
            for(int y = 0; y < cols.length; y++){
                if (player[playerTurn].getPlayerGrid().getCase(x,y).isShipPlaced()){
                    if (((ColorDrawable) gridButton[x][y].getBackground()).getColor()==player[playerTurn].getPlayerGrid().getCase(x,y).getShip().getColorShip()){
                        counter++;
                    }
                }
            }
        }
        return counter == 17;

    }
    public void EnableButton(boolean enabled){
        for (int x = 0; x < rows.length; x++){
            for(int y = 0; y < cols.length; y++) {
                gridButton[x][y].setEnabled(enabled);
            }
        }
    }
    public void LunchMissile(Player targetPlayer) {
        Button lunchButton = (Button) findViewById(id.LunchButton);
        if (lunchButton.getText() == getResources().getText(R.string.NextButton)){
            if (CheckWin()){
                Intent intent = new Intent(GameActivity.this, EndGameActivity.class);
                intent.putExtra("WinnerName", "");
                intent.putExtra("WinnerColor", "");
                intent.putExtra("NumberOfHit", "");
                intent.putExtra("NumberOfHit", "");
                startActivityForResult(intent, 1);
                finish();
            }else {
                int tmp = playerNotTurn;
                playerNotTurn = playerTurn;
                playerTurn = tmp;


                BounceInterpolator bounceInterpolator = new BounceInterpolator();
                ObjectAnimator anim;
                if (playerTurn != 0){
                    anim = ObjectAnimator.ofFloat(gameGrid, "translationX", 1000, 0 );
                }else{
                    anim = ObjectAnimator.ofFloat(gameGrid, "translationX", -1000, 0 );
                }
                anim.setInterpolator(bounceInterpolator);
                anim.setDuration(1000);
                anim.start();

                lunchButton.setText(R.string.LunchButtonText);
                lunchButton.setBackgroundColor(getResources().getColor(R.color.buttonNoEnabled));
                lunchButton.setEnabled(false);


                LoadGrid(player[playerTurn]);
                infoText.setText(getResources().getText(R.string.playMessage1)+" "+player[playerNotTurn].getName()+" "+getResources().getText(R.string.playMessage2));
                gameGrid.setBackgroundColor(ContextCompat.getColor(this, player[playerTurn].getColor()));
                EnableButton(true);
            }
        }else{
            lunchButton.setBackgroundColor(getResources().getColor(R.color.buttonNoEnabled));
            BounceInterpolator bounceInterpolator = new BounceInterpolator();
            ObjectAnimator anim;
            anim = ObjectAnimator.ofFloat(gameGrid, "rotation", 5 , 0);
            for (int x = 0; x < rows.length;x++){
                for(int y = 0; y < cols.length;y++){
                    if (((ColorDrawable)gridButton[x][y].getBackground()).getColor() == getResources().getColor(R.color.cellSelectForLunch)) {
                        if (targetPlayer.getPlayerGrid().getCase(x, y).isShipPlaced()) {
                            targetPlayer.getPlayerGrid().getCase(x, y).touchedCase();

                            TextView infoText = (TextView) findViewById(id.InfoText);

                            if (targetPlayer.getPlayerGrid().getCase(x, y).getShip().isSinking()) {
                                infoText.setText(R.string.cast);
                                anim = ObjectAnimator.ofFloat(gameGrid, "rotationX", 360, 100, 50,10,0);
                                anim.setDuration(2000);
                            } else {
                                infoText.setText(R.string.touch);
                                anim = ObjectAnimator.ofFloat(gameGrid, "rotationX", 0, 20, 50,10,0);
                                anim.setDuration(1000);
                            }
                        } else {
                            targetPlayer.getPlayerGrid().getCase(x, y).touchedCase();
                            infoText.setText(R.string.missed);
                            anim = ObjectAnimator.ofFloat(gameGrid, "alpha",  1,0,1);
                            anim.setDuration(1000);
                        }
                    }
                }
            }

            //anim.setInterpolator(bounceInterpolator);
            anim.start();
            LoadGrid(player[playerTurn]);
            lunchButton.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonNext));
            lunchButton.setText(R.string.NextButton);
            EnableButton(false);
        }

    }
    public void CleanSelection(){
        for (int x = 0; x < rows.length;x++){
            for(int y = 0; y < cols.length;y++){
                if (((ColorDrawable) gridButton[x][y].getBackground()).getColor()==getResources().getColor(R.color.cellSelectForLunch)){
                    gridButton[x][y].setBackgroundColor(getResources().getColor(R.color.cellVoid));
                }
            }
        }
    }
}
