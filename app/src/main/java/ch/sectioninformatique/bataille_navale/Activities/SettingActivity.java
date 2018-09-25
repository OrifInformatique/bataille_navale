package ch.sectioninformatique.bataille_navale.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.view.MenuItem;

import ch.sectioninformatique.bataille_navale.Models.Player;
import ch.sectioninformatique.bataille_navale.R;

public class SettingActivity extends AppCompatActivity {

    Button ButtonP1;
    Button ButtonP2;
    EditText ServerURL;
    Player Player1 = new Player();
    Player Player2 = new Player();

    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("P1Color", Player1.getColor());
        intent.putExtra("P2Color", Player2.getColor());
        intent.putExtra("ServerURL", ServerURL.getText().toString());
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Bundle extras = getIntent().getExtras();

        ButtonP1 = (Button) findViewById(R.id.ColorPlayer1Button);
        ButtonP1.setOnClickListener(new ButtonP1Listener());

        ButtonP2 = (Button) findViewById(R.id.ColorPlayer2Button);
        ButtonP2.setOnClickListener(new ButtonP2Listener());

        ServerURL = (EditText) findViewById(R.id.ServerURL);

        if(extras != null && extras.getInt("P1Color") != 0 && extras.getInt("P2Color") != 0){
            Player1.setColor(extras.getInt("P1Color"));
            Player2.setColor(extras.getInt("P2Color"));
            ButtonP1.setBackgroundResource(Player1.getColor());
            ButtonP2.setBackgroundResource(Player2.getColor());
        }

        if(extras != null && !extras.getString("ServerURL").isEmpty()){
            ServerURL.setText(extras.getString("ServerURL"));
        }

        ImageButton returnButton = (ImageButton) findViewById(R.id.ReturnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("P1Color", Player1.getColor());
                intent.putExtra("P2Color", Player2.getColor());
                intent.putExtra("ServerURL", ServerURL.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }

    public void changeColor(){

    }

    private class ButtonP1Listener implements OnClickListener{

        public void onClick(View v){
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(SettingActivity.this, ButtonP1);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.color_popup_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    int chosenColor = 0;

                    if(item.getItemId() == R.id.red){
                        chosenColor = R.color.color1;
                    } else if(item.getItemId() == R.id.yellow){
                        chosenColor = R.color.color2;
                    } else if(item.getItemId() == R.id.green){
                        chosenColor = R.color.color3;
                    } else if(item.getItemId() == R.id.blue){
                        chosenColor = R.color.color4;
                    } else {
                        chosenColor = R.color.color1;
                    }

                    if(chosenColor != Player2.getColor()){
                        Player1.setColor(chosenColor);
                        ButtonP1.setBackgroundResource(Player1.getColor());
                    }

                    //Toast.makeText(SettingActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            popup.show();//showing popup menu
        }
    }

    private class ButtonP2Listener implements OnClickListener{

        public void onClick(View v){
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(SettingActivity.this, ButtonP2);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.color_popup_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    int chosenColor = 0;

                    if(item.getItemId() == R.id.red){
                        chosenColor = R.color.color1;
                    } else if(item.getItemId() == R.id.yellow){
                        chosenColor = R.color.color2;
                    } else if(item.getItemId() == R.id.green){
                        chosenColor = R.color.color3;
                    } else if(item.getItemId() == R.id.blue){
                        chosenColor = R.color.color4;
                    } else {
                        chosenColor = R.color.color2;
                    }

                    if(chosenColor != Player1.getColor()){
                        Player2.setColor(chosenColor);
                        ButtonP2.setBackgroundResource(Player2.getColor());
                    }

                    //Toast.makeText(SettingActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            popup.show();//showing popup menu
        }
    }
}