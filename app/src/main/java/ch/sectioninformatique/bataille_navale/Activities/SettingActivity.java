package ch.sectioninformatique.bataille_navale.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.PopupMenu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.Random;

import ch.sectioninformatique.bataille_navale.R;

public class SettingActivity extends AppCompatActivity {
    Button ButtonP1;
    Button ButtonP2;
    int P1Color;
    int P2Color;
    
    /*final Button randomColor = (Button) findViewById(R.id.ColorPlayer1Button);
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Bundle extras = getIntent().getExtras();

        ButtonP1 = (Button) findViewById(R.id.ColorPlayer1Button);
        ButtonP1.setOnClickListener(new ButtonP1Listener());

        ButtonP2 = (Button) findViewById(R.id.ColorPlayer2Button);
        ButtonP2.setOnClickListener(new ButtonP2Listener());

        if(extras != null && extras.getInt("P1Color") != 0 && extras.getInt("P2Color") != 0){
            ButtonP1.setBackgroundResource(extras.getInt("P1Color"));
            ButtonP2.setBackgroundResource(extras.getInt("P2Color"));
            P1Color = extras.getInt("P1Color");
            P2Color = extras.getInt("P2Color");
        }

        Button returnButton = (Button) findViewById(R.id.ReturnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("P1Color",P1Color);
                intent.putExtra("P2Color",P2Color);
                startActivity(intent);
                finish();
            }
        });


/*
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
        */
    }

    public void changeColor(){

    }

    public class ButtonP1Listener implements OnClickListener{

        public void onClick(View v){
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(SettingActivity.this, ButtonP1);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.color_popup_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch((item.getTitle().charAt(0))){
                        case 'R':
                            ButtonP1.setBackgroundResource(R.color.color1);
                            P1Color = R.color.color1;
                            break;
                        case 'G':
                            ButtonP1.setBackgroundResource(R.color.color2);
                            P1Color = R.color.color2;
                            break;
                        case 'B':
                            ButtonP1.setBackgroundResource(R.color.color3);
                            P1Color = R.color.color3;
                            break;
                        case 'Y':
                            ButtonP1.setBackgroundResource(R.color.color4);
                            P1Color = R.color.color4;
                            break;
                        default:
                            ButtonP1.setBackgroundResource(R.color.colorPlayer1);

                    }
                    //Toast.makeText(SettingActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            popup.show();//showing popup menu
        }
    }

    public class ButtonP2Listener implements OnClickListener{

        public void onClick(View v){
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(SettingActivity.this, ButtonP2);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.color_popup_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch((item.getTitle().charAt(0))){
                        case 'R':
                            ButtonP2.setBackgroundResource(R.color.color1);
                            P2Color = R.color.color1;
                            break;
                        case 'G':
                            ButtonP2.setBackgroundResource(R.color.color2);
                            P2Color = R.color.color2;
                            break;
                        case 'B':
                            ButtonP2.setBackgroundResource(R.color.color3);
                            P2Color = R.color.color3;
                            break;
                        case 'Y':
                            ButtonP2.setBackgroundResource(R.color.color4);
                            P2Color = R.color.color4;
                            break;
                        default:
                            ButtonP2.setBackgroundResource(R.color.colorPlayer2);

                    }
                    //Toast.makeText(SettingActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            popup.show();//showing popup menu
        }
    }
}
