package ch.sectioninformatique.bataille_navale.Activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

import ch.sectioninformatique.bataille_navale.R;

public class EndGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        TextView statsNbrHit = findViewById(R.id.statNbrHit);
        TextView statsTime = findViewById(R.id.statTime);
        TextView tvWin = findViewById(R.id.WinMessage);
        TextView tvNbrHit = findViewById(R.id.tvNbrHit);
        TextView tvTime = findViewById(R.id.tvTime);
        TextView tvHitList = findViewById(R.id.tvHitList);

        final Bundle extras = getIntent().getExtras();
        assert extras != null;

        String NameWinner = (String) extras.get("WinnerName");
        int statShot = (int) extras.get("StatShot");
        long statTimeValue = (long) extras.get("StatTime");
        int delay = 0;
        int duration = 3000;
        int between = 500;


        if (statTimeValue>86400000){
            statTimeValue=statTimeValue%86400000;
        }

        ObjectAnimator animatorWin = ObjectAnimator.ofFloat(tvWin, "translationY", 2000,100,15,20, 0);
        animatorWin.setDuration(duration);
        animatorWin.start();

        delay+=duration;
        AnimNumber(statShot, statsNbrHit,tvNbrHit, duration, delay, between);
        delay+=duration;
        ObjectAnimator animatorLabel1 = ObjectAnimator.ofFloat(tvHitList, "translationX", -delay, 0);
        animatorLabel1.setDuration(between+delay+duration/3);
        animatorLabel1.start();
        delay+=duration;
        animTimer(statTimeValue, statsTime,tvTime, duration, delay, between);



        tvWin.setText(NameWinner+" "+ getResources().getText(R.string.WinMessage));
        final Button returnButton = findViewById(R.id.ReturnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertReturnButton();
            }
        });
    }

    public void AnimNumber(int value, final TextView tv,final TextView label, int duration, int delay, int between){

        ObjectAnimator animatorLabel1 = ObjectAnimator.ofFloat(label, "translationX", -delay, 0);
        animatorLabel1.setDuration(between+delay+duration/3);
        animatorLabel1.start();


        ValueAnimator animator = ValueAnimator.ofInt(0, value);
        animator.setDuration(duration);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                tv.setText(valueAnimator.getAnimatedValue().toString());
            }
        });
        animator.setStartDelay(delay+between+between+between);
        animator.start();

    }
    public void animTimer(long value, final TextView tv,final TextView label, int duration, int delay,int between){
        ObjectAnimator animatorLabel = ObjectAnimator.ofFloat(label, "translationX", -delay, 0);
        animatorLabel.setDuration(between+delay+duration/3);
        animatorLabel.start();


        ValueAnimator animator= ValueAnimator.ofInt(0, (int) value);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                tv.setText((Integer.parseInt(valueAnimator.getAnimatedValue().toString())/3600000)
                        +"° "+
                        new DecimalFormat("00").format((Integer.parseInt(valueAnimator.getAnimatedValue().toString())/60000)%60)
                        +"' "+
                        new DecimalFormat("00").format((Integer.parseInt(valueAnimator.getAnimatedValue().toString())/1000)%60)
                        +"."+
                        new DecimalFormat("000").format(Integer.parseInt(valueAnimator.getAnimatedValue().toString())%1000)
                        +"\"");
        }});

        animator.setStartDelay(delay+between+between+between);

        animator.start();

    }
    public void AlertReturnButton(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setMessage(R.string.returnAlert)
                .setNegativeButton(R.string.returnNo,null)
                .setPositiveButton(R.string.returnYes, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .create()
                .show();
    }

}
