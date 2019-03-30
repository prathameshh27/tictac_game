package com.grayhawk.test.animate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public AlertDialog.Builder alertBuilder;
    public AlertDialog alertDialog;
    public TextView statusText;
    public Button resetButton;
    public String textViewMessage, alertMessage;
    Drawable statusBackground;
    public int activePlayer = 1, activeSession=1, count=0, stateTracker=0;
    public int imageRes, coinPosition, alertCallOption=0;
    public int gameState[]={2,2,2,2,2,2,2,2,2};
    public int winningCondition[][]={   {0,1,2}, {3,4,5}, {6,7,8},
                                        {0,3,6}, {1,4,7}, {2,5,8},
                                        {0,4,8}, {2,4,6}};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alertMessage="Press to play again";
        statusText=(TextView)findViewById(R.id.statusTextView);

        resetButton=findViewById(R.id.resetButton);

        statusBackground= ContextCompat.getDrawable(this, R.drawable.status_shape_red);
        statusText.setBackground(statusBackground);
    }

    public void gameOver(){
        resetButton.setText(alertMessage);
        statusText.setTranslationY(1500);
        statusText.animate().translationYBy(-1500).setDuration(400).setStartDelay(800);
        resetButton.setTranslationY(-1500);
        resetButton.animate().translationYBy(1500).setDuration(400).setStartDelay(1200);
    }

    public void resetActivity(View view)
    {
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void alertCall()
    {
        alertBuilder = new AlertDialog.Builder(MainActivity.this);

        alertBuilder
                .setMessage(alertMessage) //"Do you want to quit?"
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(alertCallOption==1){ MainActivity.this.finish(); }
                        else resetActivity(getCurrentFocus());
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

    public void dropDown(View view)
    {
        ImageView coins = (ImageView) view;
        coinPosition=Integer.parseInt(coins.getTag().toString());
        Log.i("Info", coins.getTag().toString());

        if(gameState[coinPosition]==2 && activeSession==1)
        {
            if(activePlayer==1)
            {
                //now playing: Ironman
                imageRes=R.drawable.reactor_img;
                gameState[coinPosition]=activePlayer;
                activePlayer=0;
                statusBackground=ContextCompat.getDrawable(this, R.drawable.status_shape_blue);
                textViewMessage="Captain's Turn";
            }
            else
            {
                //now playing: Captain
                imageRes=R.drawable.shield_img;
                gameState[coinPosition]=activePlayer;
                activePlayer=1;
                statusBackground=ContextCompat.getDrawable(this, R.drawable.status_shape_red);
                textViewMessage="Ironman's Turn";
            }

            if(count==8)
            {   /////////Draw/////////
                textViewMessage="It's a Draw!";
                gameOver();
                //alertCallOption=2;
                //alertCall();
            }

            for(int win[]: winningCondition)
            {
                if(gameState[win[0]]==gameState[win[1]] && gameState[win[1]]==gameState[win[2]] && gameState[win[1]]!=2)
                {
                    activeSession=0;
                    if(activePlayer==0){
                        textViewMessage="Ironman Won!";
                        gameOver();
                        //alertCall();
                    }

                    else{
                        textViewMessage="Captain Won!"; // /nPress Reset to play again.
                        gameOver();
                        //alertCall();
                    }

                    break;
                }
            }

            count++;
            statusText.setText(textViewMessage);
            statusText.setBackground(statusBackground);
            coins.setTranslationY(-1500);
            coins.setImageResource(imageRes);
            coins.animate().translationYBy(1500).setDuration(400);
        }
    }



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        alertMessage = "Do you want to quit?";
        alertCallOption=1;
        alertCall();
    }
}
