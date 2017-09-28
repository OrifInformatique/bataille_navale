package ch.sectioninformatique.bataille_navale.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import static ch.sectioninformatique.bataille_navale.R.*;

public class SetGameActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_set_game);

        final TableLayout grid = (TableLayout) findViewById(id.Grid);

        Button returnButton = (Button) findViewById(id.ReturnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button nextButton = (Button) findViewById(id.NextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v){
                grid.getLayoutParams().height = grid.getWidth();
                grid.requestLayout();
                //grid.setBackgroundColor(getResources().getColor(color.colorPlayer1)) ;
            }
        });
        }
        /*final TableLayout grid = (TableLayout) findViewById(id.Grid);
        grid.setBackgroundColor(getResources().getColor(color.colorPlayer2)) ;*/
}
