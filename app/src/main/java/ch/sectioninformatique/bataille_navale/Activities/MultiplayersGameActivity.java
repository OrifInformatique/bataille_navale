package ch.sectioninformatique.bataille_navale.Activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import ch.sectioninformatique.bataille_navale.Models.Player;
import ch.sectioninformatique.bataille_navale.Models.Ship;
import ch.sectioninformatique.bataille_navale.R;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import static ch.sectioninformatique.bataille_navale.Activities.MainActivity.BUNDLE_PLAYERS_COLOR;
import static ch.sectioninformatique.bataille_navale.Activities.MainActivity.BUNDLE_SERVER_URL;
import static ch.sectioninformatique.bataille_navale.Activities.MainActivity.BUNDLE_STAT_SHOT;
import static ch.sectioninformatique.bataille_navale.Activities.MainActivity.BUNDLE_STAT_TIME_VALUE;
import static ch.sectioninformatique.bataille_navale.Activities.MainActivity.BUNDLE_WINNER_NAME;
import static ch.sectioninformatique.bataille_navale.R.id;
import static ch.sectioninformatique.bataille_navale.R.layout;

public class MultiplayersGameActivity extends AppCompatActivity implements View.OnClickListener{
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
    TextView[] infoText = new TextView[2];
    int statShot = 0;
    long statTime;
    FrameLayout[] colorPlayer = new FrameLayout[2];
    Button lunchButton;

    ProgressDialog dialog;
    int randomNumber;
    int x, y;
    String ServerURL;
    Socket mSocket;

    public void onStart() {
        super.onStart();

        mSocket.connect();

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
            if(playerNotTurn == 0){
                thisButton.setBackgroundColor(ContextCompat.getColor(this, R.color.cellSelectForLunch));
            } else {
                if (((ColorDrawable) thisButton.getBackground()).getColor() == getResources().getColor(R.color.cellVoid)) {
                    CleanSelection();

                    thisButton.setBackgroundColor(ContextCompat.getColor(this, R.color.cellSelectForLunch));

                    lunchButton.setEnabled(true);
                    lunchButton.setBackgroundColor(getResources().getColor(R.color.buttonLunch));
                    lunchButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LunchMissile(player[playerNotTurn]);
                        }
                    });
                } else if (((ColorDrawable) thisButton.getBackground()).getColor() == getResources().getColor(R.color.cellSelectForLunch)) {
                    CleanSelection();
                    lunchButton.setEnabled(false);
                    lunchButton.setBackgroundColor(getResources().getColor(R.color.buttonNoEnabled));
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_game);

        dialog = new ProgressDialog(MultiplayersGameActivity.this);
        dialog.setMessage(getResources().getText(R.string.WaitPlayer));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialog.hide();
                if(mSocket.connected()) {
                    statTime = System.currentTimeMillis();
                    RandomSelectPlayer();
                }
            }
        });
        dialog.show();

        Bundle extras = getIntent().getExtras();
        ServerURL = getResources().getText(R.string.defaultServerURL).toString();
        if(extras != null && !extras.getString(BUNDLE_SERVER_URL).isEmpty()) {
            ServerURL = extras.getString(BUNDLE_SERVER_URL);
        }

        try {
            mSocket = IO.socket(ServerURL);

            /*mSocket.once(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    mSocket.disconnect();
                    mSocket.off();

                    dialog.cancel();

                    Looper.prepare();

                    AlertDialog.Builder alert  = new AlertDialog.Builder(MultiplayersGameActivity.this);
                    alert.setMessage(getResources().getText(R.string.Timeout));
                    alert.setPositiveButton(getResources().getText(R.string.returnOk), new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
                    alert.setCancelable(false);
                    alert.create().show();

                    Looper.loop();
                }
            });*/

            mSocket.once("player", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject dataReceive = (JSONObject) args[0];
                    randomNumber = (int)args[1];

                    CreatePlayers2(dataReceive);
                }
            });

            mSocket.on("lunchMissile", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject coordinates = (JSONObject) args[0];

                    try {
                        x = coordinates.getInt("x");
                        y = coordinates.getInt("y");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    dialog.cancel();
                }
            });

            mSocket.on("next", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    dialog.cancel();
                }
            });

            mSocket.once("deco", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    mSocket.disconnect();
                    mSocket.off();

                    dialog.cancel();

                    Looper.prepare();

                    AlertDialog.Builder alert  = new AlertDialog.Builder(MultiplayersGameActivity.this);
                    alert.setMessage(getResources().getText(R.string.DecoPlayer));
                    alert.setPositiveButton(getResources().getText(R.string.returnOk), new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
                    alert.setCancelable(false);
                    alert.create().show();

                    Looper.loop();
                }
            });

        } catch (URISyntaxException e) {}

        final ImageButton returnButton =  findViewById(id.ReturnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertReturnButton();
            }
        });

        colorPlayer[0] = findViewById(id.colorPlayerLeft);
        colorPlayer[1] = findViewById(id.colorPlayerRight);
        gameGrid = findViewById(R.id.GameGrid);
        gridButton = SetGameActivity.constructGrid(gameGrid, this);
        lunchButton = findViewById(R.id.LunchButton);


        CreatePlayers1();

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
                        mSocket.disconnect();
                        mSocket.off();
                        finish();
                    }
                })
                .create()
                .show();
    }

    public void RandomSelectPlayer(){

        infoText[0] = findViewById(id.InfoTextLeft);
        infoText[1] = findViewById(id.InfoTextRight);
        lunchButton = (Button) findViewById(id.LunchButton);

        final int color[] = {
                ContextCompat.getColor(this,player[0].getColor()),
                Color.GRAY,
                ContextCompat.getColor(this,player[1].getColor())};
        ValueAnimator animator = ValueAnimator.ofInt(0, randomNumber);

        animator.setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int i=(Integer.parseInt(valueAnimator.getAnimatedValue().toString())+1)%2;
                colorPlayer[0].setBackgroundColor(color[i]);
                colorPlayer[1].setBackgroundColor(color[i+1]);
            }
        });
        animator.setStartDelay(2000);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                infoText[0].setText(getResources().getText(R.string.startMessageYou));
                infoText[1].setText(getResources().getText(R.string.startMessageHim));
            }
            @Override
            public void onAnimationEnd(Animator animator) {
            }
            @Override
            public void onAnimationCancel(Animator animator) {
            }
            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.start();

        ObjectAnimator anim = ObjectAnimator.ofFloat(lunchButton, "alpha",  0,0,0,1);
        anim.setDuration(9000);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                EndAnimationRandomPlayer();
            }
            @Override
            public void onAnimationCancel(Animator animator) {
            }
            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        anim.start();

        playerTurn = randomNumber%2;
        playerNotTurn = (randomNumber+1)%2;



    }

    public void EndAnimationRandomPlayer(){
        LoadGrid(player[playerNotTurn]);
        lunchButton.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonNext));
        lunchButton.setText(R.string.NextButton);
        lunchButton.setEnabled(true);
        EnableButton(false);
        lunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LunchMissile(player[playerNotTurn]);
            }
        });
        LunchMissile(player[playerNotTurn]);
    }
    public void LoadGrid(Player thisPlayer){
        for (int x = 0; x < rows.length;x++) {
            for (int y = 0; y < cols.length; y++) {
                if (playerNotTurn == 0) {
                    if (!thisPlayer.getPlayerGrid().getCase(x, y).getTouched()) {
                        if (thisPlayer.getPlayerGrid().getCase(x, y).isShipPlaced()) {
                            gridButton[x][y].setBackgroundColor(ContextCompat.getColor(this, player[playerNotTurn].getPlayerGrid().getCase(x, y).getShip().getColorShip()));
                        } else {
                            gridButton[x][y].setBackgroundColor(ContextCompat.getColor(this, R.color.cellVoid));
                        }
                    } else {
                        if (thisPlayer.getPlayerGrid().getCase(x, y).isShipPlaced()) {
                            gridButton[x][y].setBackgroundColor(ContextCompat.getColor(this, R.color.cellTouchShip));
                        } else {
                            gridButton[x][y].setBackgroundColor(ContextCompat.getColor(this, R.color.cellMissed));

                        }
                    }
                } else {
                    if (!thisPlayer.getPlayerGrid().getCase(x, y).getTouched()) {
                        gridButton[x][y].setBackgroundColor(ContextCompat.getColor(this, R.color.cellVoid));
                    } else {
                        if (thisPlayer.getPlayerGrid().getCase(x, y).isShipPlaced()) {
                            if (thisPlayer.getPlayerGrid().getCase(x, y).getShip().isSinking()) {
                                gridButton[x][y].setBackgroundColor(ContextCompat.getColor(this, thisPlayer.getPlayerGrid().getCase(x, y).getShip().getColorShip()));
                            } else {
                                gridButton[x][y].setBackgroundColor(ContextCompat.getColor(this, R.color.cellTouchShip));

                            }
                        } else {
                            gridButton[x][y].setBackgroundColor(ContextCompat.getColor(this, R.color.cellMissed));

                        }
                    }
                }
            }
        }
    }

    public void CreatePlayers1(){
        final Bundle extras = getIntent().getExtras();
        assert extras != null;

        byte[] tapShipLength1 = ((byte[]) extras.get("p1shipLength"));
        char[] tmpShipOr1 =     ((char[]) extras.get("p1shipOrientation"));
        int[] tmpShipColor1 =   ((int[]) extras.get("p1shipColor"));
        int[] tmpShipStartX1 =  ((int[]) extras.get("p1shipStartX"));
        int[] tmpShipStartY1 =  ((int[]) extras.get("p1shipStartY"));

        assert tapShipLength1 != null;
        assert tmpShipOr1 != null;
        assert tmpShipColor1 != null;
        assert tmpShipStartX1 != null;
        assert tmpShipStartY1 != null;

        for (int i = 0; i< nbrShip; i++){
            shipsPlayer[0][i]= new Ship(tapShipLength1[i],tmpShipOr1[i],tmpShipColor1[i],tmpShipStartX1[i],tmpShipStartY1[i]);
            shipsPlayer[0][i].setNbHit((byte)0);
        }
        player[0].setName((String) extras.get("player1Name"));
        String player1Name = (String)extras.get("player1Name");

        int[] tmpColor =  ((int[])extras.get(BUNDLE_PLAYERS_COLOR));

        if (tmpColor != null) {
            player[0].setColor(tmpColor[0]);
            player[1].setColor(tmpColor[1]);
        }else{
            player[0].setColor(getResources().getColor(R.color.color1));
            player[1].setColor(getResources().getColor(R.color.color2));
        }

        for (int s = 0; s < nbrShip; s++) {
            switch (shipsPlayer[0][s].getDefaultOrientation()) {
                case 'U':
                    for (int y = shipsPlayer[0][s].getY(); y < (shipsPlayer[0][s].getY() + shipsPlayer[0][s].getNbCases()); y++) {
                        player[0].getPlayerGrid().getCase(shipsPlayer[0][s].getX(), y).setShip(shipsPlayer[0][s]);
                    }
                    break;
                case 'R':
                    for (int x = shipsPlayer[0][s].getX(); x < (shipsPlayer[0][s].getX() + shipsPlayer[0][s].getNbCases()); x++) {
                        player[0].getPlayerGrid().getCase(x, shipsPlayer[0][s].getY()).setShip(shipsPlayer[0][s]);
                    }
                    break;
                case 'D':
                    for (int y = shipsPlayer[0][s].getY(); y > (shipsPlayer[0][s].getY() - shipsPlayer[0][s].getNbCases()); y--) {
                        player[0].getPlayerGrid().getCase(shipsPlayer[0][s].getX(), y).setShip(shipsPlayer[0][s]);
                    }
                    break;
                case 'L':
                    for (int x = shipsPlayer[0][s].getX(); x > (shipsPlayer[0][s].getX() - shipsPlayer[0][s].getNbCases()); x--) {
                        player[0].getPlayerGrid().getCase(x, shipsPlayer[0][s].getY()).setShip(shipsPlayer[0][s]);
                    }
                    break;
                default://error;
            }
        }

        JSONObject obj = new JSONObject();
        try {
            obj.put("playerName", player1Name);
            obj.put("tapShipLength", new JSONArray(tapShipLength1));
            obj.put("tmpShipOr", new JSONArray(tmpShipOr1));
            obj.put("tmpShipColor", new JSONArray(tmpShipColor1));
            obj.put("tmpShipStartX", new JSONArray(tmpShipStartX1));
            obj.put("tmpShipStartY", new JSONArray(tmpShipStartY1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("player", obj);
    }

    public void CreatePlayers2(JSONObject extras){

        for (int i = 0; i< nbrShip; i++){
            try {
                shipsPlayer[1][i]= new Ship((byte)extras.getJSONArray("tapShipLength").getInt(i),extras.getJSONArray("tmpShipOr").getString(i).charAt(0),extras.getJSONArray("tmpShipColor").getInt(i),extras.getJSONArray("tmpShipStartX").getInt(i),extras.getJSONArray("tmpShipStartY").getInt(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            shipsPlayer[1][i].setNbHit((byte)0);
        }
        try {
            player[1].setName((String) extras.getString("playerName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int s = 0; s < nbrShip; s++) {
            switch (shipsPlayer[1][s].getDefaultOrientation()) {
                case 'U':
                    for (int y = shipsPlayer[1][s].getY(); y < (shipsPlayer[1][s].getY() + shipsPlayer[1][s].getNbCases()); y++) {
                        player[1].getPlayerGrid().getCase(shipsPlayer[1][s].getX(), y).setShip(shipsPlayer[1][s]);
                    }
                    break;
                case 'R':
                    for (int x = shipsPlayer[1][s].getX(); x < (shipsPlayer[1][s].getX() + shipsPlayer[1][s].getNbCases()); x++) {
                        player[1].getPlayerGrid().getCase(x, shipsPlayer[1][s].getY()).setShip(shipsPlayer[1][s]);
                    }
                    break;
                case 'D':
                    for (int y = shipsPlayer[1][s].getY(); y > (shipsPlayer[1][s].getY() - shipsPlayer[1][s].getNbCases()); y--) {
                        player[1].getPlayerGrid().getCase(shipsPlayer[1][s].getX(), y).setShip(shipsPlayer[1][s]);
                    }
                    break;
                case 'L':
                    for (int x = shipsPlayer[1][s].getX(); x > (shipsPlayer[1][s].getX() - shipsPlayer[1][s].getNbCases()); x--) {
                        player[1].getPlayerGrid().getCase(x, shipsPlayer[1][s].getY()).setShip(shipsPlayer[1][s]);
                    }
                    break;
                default://error;
            }
        }

        dialog.cancel();
    }

    public boolean CheckWin(){
        boolean allSinking = true;
        for (Ship aShip : shipsPlayer[playerNotTurn]) {
            if (!aShip.isSinking()) {
                allSinking = false;
            }
        }
        return allSinking;
    }
    public void EnableButton(boolean enabled){
        for (int x = 0; x < rows.length; x++){
            for(int y = 0; y < cols.length; y++) {
                gridButton[x][y].setEnabled(enabled);
            }
        }
    }
    public void LunchMissile(Player targetPlayer) {
        final Button lunchButton = (Button) findViewById(id.LunchButton);
        if (lunchButton.getText() == getResources().getText(R.string.NextButton)){
            if (CheckWin()){
                statTime = System.currentTimeMillis() - statTime ;
                Intent intent = new Intent(MultiplayersGameActivity.this, EndGameActivity.class);
                intent.putExtra(BUNDLE_WINNER_NAME, player[playerTurn].getName());
                intent.putExtra("WinnerColor", player[playerTurn].getColor());
                intent.putExtra(BUNDLE_STAT_SHOT, statShot);
                intent.putExtra("ListOfHit", "");
                intent.putExtra(BUNDLE_STAT_TIME_VALUE, statTime);
                startActivityForResult(intent, 1);
                finish();
            } else {
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
                        if(playerTurn == 0) {
                            JSONObject coordinates = new JSONObject();
                            try {
                                coordinates.put("x", x);
                                coordinates.put("y", y);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mSocket.emit("lunchMissile", coordinates);
                        }
                        if (targetPlayer.getPlayerGrid().getCase(x, y).isShipPlaced()) {
                            targetPlayer.getPlayerGrid().getCase(x, y).touchedCase();
                            statShot++;

                            if (targetPlayer.getPlayerGrid().getCase(x, y).getShip().isSinking()) {
                                infoText[playerTurn].setText(R.string.cast);
                                anim = ObjectAnimator.ofFloat(gameGrid, "rotationX", 360, 100, 50, 10, 0);
                                anim.setDuration(2000);
                            } else {
                                infoText[playerTurn].setText(R.string.touch);
                                anim = ObjectAnimator.ofFloat(gameGrid, "rotationX", 0, 20, 50, 10, 0);
                                anim.setDuration(1000);
                            }
                        } else {
                            targetPlayer.getPlayerGrid().getCase(x, y).touchedCase();
                            infoText[playerTurn].setText(R.string.missed);
                            anim = ObjectAnimator.ofFloat(gameGrid, "alpha", 1, 0, 1);
                            anim.setDuration(1000);
                        }
                    }
                }
            }
            anim.start();
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }
                @Override
                public void onAnimationEnd(Animator animator) {
                    lunchButton.performClick();
                }
                @Override
                public void onAnimationCancel(Animator animator) {
                }
                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });

            //lunchButton.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonNext));
            lunchButton.setText(R.string.NextButton);
            lunchButton.setEnabled(false);
            lunchButton.setBackgroundColor(getResources().getColor(R.color.buttonNoEnabled));
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
        if (playerTurn == 0){
            colorPlayer[1].setBackgroundColor(Color.GRAY);
            colorPlayer[0].setBackgroundColor(ContextCompat.getColor(this, player[0].getColor()));
            anim1 = ObjectAnimator.ofFloat(gameGrid, "translationX", 800, 600,300,0 );
        }else{
            colorPlayer[0].setBackgroundColor(Color.GRAY);
            colorPlayer[1].setBackgroundColor(ContextCompat.getColor(this, player[1].getColor()));
            anim1 = ObjectAnimator.ofFloat(gameGrid, "translationX", -800, -600,-300,0 );
        }
        AnimatorSet as = new AnimatorSet();
        as.playTogether(anim1, anim2);
        as.setDuration(1000);
        as.start();

        lunchButton.setText(R.string.LunchButtonText);
        lunchButton.setBackgroundColor(getResources().getColor(R.color.buttonNoEnabled));
        lunchButton.setEnabled(false);



        LoadGrid(player[playerNotTurn]);
        gameGrid.setBackgroundColor(ContextCompat.getColor(this,player[playerTurn].getColor()));
        infoText[0].setText(getResources().getText(R.string.playMessageYou));
        infoText[1].setText(getResources().getText(R.string.playMessageHim));

        if(playerTurn == 1) {

            lunchButton.setBackgroundColor(getResources().getColor(R.color.buttonNoEnabled));

            EnableButton(false);
            dialog = new ProgressDialog(MultiplayersGameActivity.this);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    gridButton[x][y].performClick();
                    lunchButton.performClick();
                    mSocket.emit("next");
                }
            });
        } else {
            EnableButton(true);
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
