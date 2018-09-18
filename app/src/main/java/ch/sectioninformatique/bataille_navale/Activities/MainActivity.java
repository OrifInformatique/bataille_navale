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

    public static final String BUNDLE_WINNER_NAME = "WinnerName";
    public static final String BUNDLE_STAT_SHOT = "StatShot";
    public static final String BUNDLE_STAT_TIME = "StatTime";
    public final static String BUNDLE_P1_COLOR = "P1Color";
    public final static String BUNDLE_P2_COLOR = "P2Color";
    public final static String BUNDLE_SERVER_URL = "ServerURL";
    public final static String BUNDLE_PLAYERS_COLOR = "PlayersColor";
    public final static String BUNDLE_MULTIPLAYER = "Multiplayer";

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
            Player1.setColor(extras.getInt(BUNDLE_P1_COLOR));
            Player2.setColor(extras.getInt(BUNDLE_P2_COLOR));
            ServerURL = extras.getString(BUNDLE_SERVER_URL);
        }

        Button startGame = (Button) findViewById(R.id.playButton);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SetGameActivity.class);
                intent.putExtra("param", 0);
                if (Player1.getColor()==0 || Player2.getColor()==0){
                    Player1.setColor(R.color.color1);
                    Player2.setColor(R.color.color2);
                }
                intent.putExtra(BUNDLE_PLAYERS_COLOR, new int[]{Player1.getColor(),Player2.getColor()} );
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
                intent.putExtra(BUNDLE_PLAYERS_COLOR, new int[]{Player1.getColor(),Player2.getColor()} );
                intent.putExtra(BUNDLE_SERVER_URL, ServerURL);
                intent.putExtra(BUNDLE_MULTIPLAYER, true);
                startActivityForResult(intent, 1);
            }
        });


        Button settingGame = (Button) findViewById(R.id.settingButton);
        settingGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                intent.putExtra(BUNDLE_P1_COLOR, Player1.getColor());
                intent.putExtra(BUNDLE_P2_COLOR, Player2.getColor());
                intent.putExtra(BUNDLE_SERVER_URL, ServerURL);
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
