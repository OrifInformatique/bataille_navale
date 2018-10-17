package ch.sectioninformatique.bataille_navale.Activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import ch.sectioninformatique.bataille_navale.Models.Case;
import ch.sectioninformatique.bataille_navale.Models.CustomButton;
import ch.sectioninformatique.bataille_navale.Models.Player;
import ch.sectioninformatique.bataille_navale.Models.Ship;
import ch.sectioninformatique.bataille_navale.R;

import static ch.sectioninformatique.bataille_navale.R.id;
import static ch.sectioninformatique.bataille_navale.R.layout;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{
    String cols[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    String rows[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    Player[] player = {new Player(), new Player()};
    int phase =1;
    GridLayout gameGrid;
    CustomButton[][] gridButton = new CustomButton[10][10];
    int playerTurn = 0;
    int playerNotTurn = 1;
    TextView infoText;
    int statShot = 0;
    long statTime;
    FrameLayout colorPlayer;
    Button lunchButton;




    public void onStart() {
        super.onStart();

        statTime = System.currentTimeMillis();
        colorPlayer = findViewById(id.colorPlayerLeft);
        gameGrid = findViewById(R.id.GameGrid);
        gridButton = SetGameActivity.constructGrid(gameGrid, this);
        lunchButton = findViewById(R.id.LunchButton);
        CreatePlayers();


        RandomSelectPlayer();
        /*

        gameGrid.setBackgroundColor(ContextCompat.getColor(this, player[playerNotTurn].getColor()));
        gameGrid.setVisibility(View.VISIBLE);
        infoText = findViewById(id.InfoText);
        infoText.setText(getResources().getText(R.string.playMessage1)+" "+player[playerTurn].getName()+" "+getResources().getText(R.string.playMessage2));
        colorPlayer.setBackgroundColor(ContextCompat.getColor(this,player[playerTurn].getColor()));*/
    }

    @Override
    public void onClick(View v) {

        final Button thisButton = (Button) v;

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

        final ImageButton returnButton =  findViewById(id.ReturnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertReturnButton();
            }
        });
    }

    public void onBackPressed(){
        AlertReturnButton();
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

    public void RandomSelectPlayer(){

        int randomNumber = (int)(Math.random()*10)+20;//(between 20 et 30)
        infoText = findViewById(id.InfoTextLeft);
        colorPlayer  = findViewById(id.colorPlayerLeft);
        lunchButton = (Button) findViewById(id.LunchButton);

        final int color[] = {
                ContextCompat.getColor(this,player[0].getColor()),
                ContextCompat.getColor(this,player[1].getColor())};
        ValueAnimator animator = ValueAnimator.ofInt(0, randomNumber);


        animator.setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int i=Integer.parseInt(valueAnimator.getAnimatedValue().toString())%2;
                infoText.setText(getResources().getText(R.string.startMessage1)+" "+player[i].getName()+"\n "+getResources().getText(R.string.startMessage2));
                colorPlayer.setBackgroundColor(color[i]);
            }
        });
        animator.setStartDelay(2000);
        animator.start();

        ObjectAnimator anim = ObjectAnimator.ofFloat(lunchButton, "alpha",  0,0,0,1);
        anim.setDuration(9000);

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                endAnimationRandomPlayer();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        anim.start();

        playerNotTurn = randomNumber%2;
        playerTurn = (randomNumber+1)%2;

    }

    public void endAnimationRandomPlayer(){
        LoadGrid(player[playerTurn]);
        lunchButton.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonNext));
        lunchButton.setText(R.string.NextButton);
        lunchButton.setEnabled(true);
        EnableButton(false);
        lunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LunchMissile(player[playerTurn]);
            }
        });
    }

    public void LoadGrid(Player thisPlayer){
        for (int x = 0; x < rows.length;x++) {
            for (int y = 0; y < cols.length; y++) {
                if(thisPlayer.getPlayerGrid().getCase(x,y).getEtat() != Case.Etat.Touched){
                    gridButton[x][y].setBackgroundColor(ContextCompat.getColor(this,R.color.cellVoid));
                }else{
                    if (thisPlayer.getPlayerGrid().getCase(x,y).isShipPlaced()){
                        if(thisPlayer.getPlayerGrid().getCase(x,y).getShip().isSinking()) {
                            gridButton[x][y].setBackgroundColor(ContextCompat.getColor(this,player[playerTurn].getPlayerGrid().getCase(x,y).getShip().getColorShip()));

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

        player[0] = (Player)extras.get("player1");
        player[1] = (Player)extras.get("player2");
    }
    public boolean CheckWin(){
        return player[playerTurn].getPlayerGrid().getShips().size() == 0;

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
                statTime = System.currentTimeMillis() - statTime ;
                Intent intent = new Intent(GameActivity.this, EndGameActivity.class);
                intent.putExtra("WinnerName", "Laurent");
                intent.putExtra("WinnerColor", R.color.color1);
                intent.putExtra("StatShot", statShot);
                intent.putExtra("ListOfHit", "");
                intent.putExtra("StatTime", statTime);
                startActivityForResult(intent, 1);
                finish();
            }else {
                Play();
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
                            statShot++;
                            TextView infoText = (TextView) findViewById(id.InfoTextLeft);

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
            targetPlayer.getPlayerGrid().getCase()
            anim.start();

            LoadGrid(player[playerTurn]);
            lunchButton.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonNext));
            lunchButton.setText(R.string.NextButton);
            EnableButton(false);
        }

    }
    public void Play(){
        int tmp = playerNotTurn;
        playerNotTurn = playerTurn;
        playerTurn = tmp;
        if (gameGrid.getVisibility()==View.INVISIBLE){
            gameGrid.setVisibility(View.VISIBLE);
        }

        ObjectAnimator anim1;
        ObjectAnimator anim2 =  ObjectAnimator.ofFloat(gameGrid, "translationY", -600,-100,15,20,0);
        if (playerTurn != 0){
            anim1 = ObjectAnimator.ofFloat(gameGrid, "translationX", 800, 600,300,0 );
        }else{
            anim1 = ObjectAnimator.ofFloat(gameGrid, "translationX", -800, -600,-300,0 );
        }
        AnimatorSet as = new AnimatorSet();
        as.playTogether(anim1, anim2);
        as.setDuration(1000);
        as.start();

        lunchButton.setText(R.string.LunchButtonText);
        lunchButton.setBackgroundColor(getResources().getColor(R.color.buttonNoEnabled));
        lunchButton.setEnabled(false);



        LoadGrid(player[playerTurn]);
        gameGrid.setBackgroundColor(ContextCompat.getColor(this,player[playerNotTurn].getColor()));
        infoText.setText(getResources().getText(R.string.playMessage1)+" "+player[playerTurn].getName()+" "+getResources().getText(R.string.playMessage2));
        colorPlayer.setBackgroundColor(ContextCompat.getColor(this,player[playerTurn].getColor()));

        EnableButton(true);
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
