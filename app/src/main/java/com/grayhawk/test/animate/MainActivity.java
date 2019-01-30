package com.grayhawk.test.animate;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public AlertDialog.Builder alertBuilder;
    public AlertDialog alertDialog;
    int activePlayer = 1, activeSession=1, count=0;
    int imageRes, coinPosition;
    String textviewMessage;
    int gameState[]={2,2,2,2,2,2,2,2,2};
    int winningCondition[][]={  {0,1,2}, {3,4,5}, {6,7,8},
                                {0,3,6}, {1,4,7}, {2,5,8},
                                {0,4,8}, {2,4,6}};

    public void dropDown(View view)
    {
        TextView statusText=(TextView)findViewById(R.id.statusTextView);
        ImageView coins = (ImageView) view;
        coinPosition=Integer.parseInt(coins.getTag().toString());
        Log.i("Info", coins.getTag().toString());

        if(gameState[coinPosition]==2 && activeSession==1)
        {
            if(activePlayer==1)
            {
                //now playing: Ironman
                imageRes=R.drawable.reactor;
                gameState[coinPosition]=activePlayer;
                activePlayer=0;
                textviewMessage="Captain's Turn";
            }
            else
            {
                //now playing: Captain
                imageRes=R.drawable.shield;
                gameState[coinPosition]=activePlayer;
                activePlayer=1;
                textviewMessage="Ironman's Turn";
            }

            if(count==8)
            {
                textviewMessage="Draw!\nPress Reset to play again.";
            }

            for(int win[]: winningCondition)
            {
                if(gameState[win[0]]==gameState[win[1]] && gameState[win[1]]==gameState[win[2]] && gameState[win[1]]!=2)
                {
                    activeSession=0;
                    if(activePlayer==0)
                        textviewMessage="Ironman Won!\nPress Reset to play again.";
                    else
                        textviewMessage="Captain Won!\nPress Reset to play again.";
                    break;
                }
            }

            count++;
            statusText.setText(textviewMessage);
            coins.setTranslationY(-1500);
            coins.setImageResource(imageRes);
            coins.animate().translationYBy(1500).setDuration(400);
        }
    }

    public void resetActivity(View view)
    {
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        alertBuilder = new AlertDialog.Builder(MainActivity.this);

        alertBuilder
                .setMessage("Do you want to quit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        alertDialog = alertBuilder.create();
        alertDialog.show();
        //can also be done using
        //alertBuilder.create().show();
    }
}
