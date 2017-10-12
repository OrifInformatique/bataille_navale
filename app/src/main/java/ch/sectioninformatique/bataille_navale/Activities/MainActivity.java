package ch.sectioninformatique.bataille_navale.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashSet;

import ch.sectioninformatique.bataille_navale.Models.ModelsTest;
import ch.sectioninformatique.bataille_navale.Models.Player;
import ch.sectioninformatique.bataille_navale.R;

public class MainActivity extends AppCompatActivity {

    Player Player1 = new Player();
    Player Player2 = new Player();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Player1.setColor(extras.getInt("P1Color"));
            Player2.setColor(extras.getInt("P2Color"));
        }

      //  ModelsTest TestM1 = new ModelsTest();

        Button startGame = (Button) findViewById(R.id.playButton);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SetGameActivity.class);
                intent.putExtra("param", 1);
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
                startActivity(intent) ;
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
