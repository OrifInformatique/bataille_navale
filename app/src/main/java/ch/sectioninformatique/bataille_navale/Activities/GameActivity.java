package ch.sectioninformatique.bataille_navale.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import ch.sectioninformatique.bataille_navale.R;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        TableLayout grid = (TableLayout) findViewById(R.id.Grid);
        final Button returnButton = (Button) findViewById(R.id.ReturnButton);
        //grid.getLayoutParams().height =
                getIntent().getIntExtra("Width", 0);
        grid.requestLayout();

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
