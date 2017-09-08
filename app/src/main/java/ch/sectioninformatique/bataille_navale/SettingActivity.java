package ch.sectioninformatique.bataille_navale;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class SettingActivity extends AppCompatActivity {
    final Button randomColor = (Button) findViewById(R.id.ColorPlayer1Button);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button returnButton = (Button) findViewById(R.id.ReturnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        randomColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rand = new Random();
                int A = 255;
                int R = rand.nextInt(255);
                int G = rand.nextInt(255);
                int B = rand.nextInt(255);
                int color = (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);



            }
        });
    }


}
