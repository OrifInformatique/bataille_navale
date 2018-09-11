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

import ch.sectioninformatique.bataille_navale.R;

public class SettingActivity extends AppCompatActivity {

    Button ButtonP1;
    Button ButtonP2;
    EditText ServerURL;
    int P1Color;
    int P2Color;

    final String BUNDLE_P1_COLOR = "P1Color";
    final String BUNDLE_P2_COLOR = "P2Color";
    final String BUNDLE_SERVER_URL = "ServerURL";

    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra(BUNDLE_P1_COLOR,P1Color);
        intent.putExtra(BUNDLE_P2_COLOR,P2Color);
        intent.putExtra(BUNDLE_SERVER_URL, ServerURL.getText().toString());
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

        if(savedInstanceState != null){
            P1Color = savedInstanceState.getInt(BUNDLE_P1_COLOR);
            P2Color = savedInstanceState.getInt(BUNDLE_P2_COLOR);
            ServerURL.setText(savedInstanceState.getString(BUNDLE_SERVER_URL));

        }else{
            if(extras != null && extras.getInt("P1Color") != 0 && extras.getInt("P2Color") != 0){
                P1Color = extras.getInt("P1Color");
                P2Color = extras.getInt("P2Color");
            }

            if(extras != null && !extras.getString("ServerURL").isEmpty()){
                ServerURL.setText(extras.getString("ServerURL"));
            }
        }

        ButtonP1.setBackgroundResource(P1Color);
        ButtonP2.setBackgroundResource(P2Color);

        ImageButton returnButton = (ImageButton) findViewById(R.id.ReturnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("P1Color",P1Color);
                intent.putExtra("P2Color",P2Color);
                intent.putExtra("ServerURL", ServerURL.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putInt(BUNDLE_P1_COLOR,P1Color);
        outState.putInt(BUNDLE_P2_COLOR,P2Color);
        outState.putString(BUNDLE_SERVER_URL, ServerURL.getText().toString());

        super.onSaveInstanceState(outState);
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
                    switch((item.getTitle().charAt(0))){
                        case 'R':
                            chosenColor = R.color.color1;
                            break;
                        case 'Y':
                            chosenColor = R.color.color2;
                            break;
                        case 'G':
                            chosenColor = R.color.color3;
                            break;
                        case 'B':
                            chosenColor = R.color.color4;
                            break;
                        default:
                            ButtonP1.setBackgroundResource(R.color.color1);
                    }

                    if(chosenColor != P2Color){
                        P1Color = chosenColor;
                        ButtonP1.setBackgroundResource(P1Color);
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
                    switch((item.getTitle().charAt(0))){
                        case 'R':
                            chosenColor = R.color.color1;
                            break;
                        case 'Y':
                            chosenColor = R.color.color2;
                            break;
                        case 'G':
                            chosenColor = R.color.color3;
                            break;
                        case 'B':
                            chosenColor = R.color.color4;
                            break;
                        default:
                            ButtonP2.setBackgroundResource(R.color.color2);
                    }

                    if(chosenColor != P1Color){
                        P2Color = chosenColor;
                        ButtonP2.setBackgroundResource(P2Color);
                    }

                    //Toast.makeText(SettingActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            popup.show();//showing popup menu
        }
    }
}