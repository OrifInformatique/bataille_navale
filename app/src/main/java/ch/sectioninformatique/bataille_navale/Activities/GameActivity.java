package ch.sectioninformatique.bataille_navale.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import ch.sectioninformatique.bataille_navale.R;

public class GameActivity extends AppCompatActivity {

    /*@Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        TableLayout grid = (TableLayout) findViewById(R.id.Grid);

        Log.d("onResume","zz1");
        grid.getLayoutParams().height = grid.getWidth();
        Log.d("onResume","zz2");
        grid.requestLayout();
        Log.d("onResume","zz3");
        Log.d("Width",grid.getWidth()+"");
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        final Button returnButton = (Button) findViewById(R.id.ReturnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
