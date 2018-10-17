package ch.sectioninformatique.bataille_navale.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ch.sectioninformatique.bataille_navale.Models.Player;
import ch.sectioninformatique.bataille_navale.R;

public class MainActivity extends AppCompatActivity {
    Player Player1 = new Player();
    Player Player2 = new Player();
    String ServerURL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //default colors
        Player1.setColor(R.color.color1);
        Player2.setColor(R.color.color2);
        ServerURL = getResources().getText(R.string.defaultServerURL).toString();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Player1.setColor(extras.getInt("P1Color"));
            Player2.setColor(extras.getInt("P2Color"));
            ServerURL = extras.getString("ServerURL");
        }

        Button startGame = (Button) findViewById(R.id.playButton);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SetGameActivity.class);
                intent.putExtra("param", 0);

                intent.putExtra("player1", Player1);
                intent.putExtra("player2", Player2);

                startActivityForResult(intent, 1);
            }
        });

        Button multiGame = (Button) findViewById(R.id.multiButton);
        multiGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SetGameActivity.class);
                intent.putExtra("param", 2);
                if (Player1.getColor()==0 || Player2.getColor()==0){
                    Player1.setColor(R.color.color1);
                    Player2.setColor(R.color.color2);
                }
                intent.putExtra("playerColor", new int[]{Player1.getColor(),Player2.getColor()} );
                intent.putExtra("ServerURL", ServerURL);
                intent.putExtra("multiplayer", true);
                startActivityForResult(intent, 1);
            }
        });


        Button settingGame = (Button) findViewById(R.id.settingButton);
        settingGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                intent.putExtra("P1Color", Player1.getColor());
                intent.putExtra("P2Color", Player2.getColor());
                intent.putExtra("ServerURL", ServerURL);
                startActivity(intent) ;
                finish();
            }
        });

        Button closeApp = (Button) findViewById(R.id.closeButton);
        closeApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
