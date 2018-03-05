package com.petko.memorija;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.View.generateViewId;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private GridLayout theGrid;
    TextView matchesRemainingDisplay;

    private int matchesRemaining;


    private SlikiKopce firstChoice;
    private SlikiKopce secondChoice;
    private boolean delayFactor;
    private boolean flashDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle bundle = getIntent().getExtras();
        String nivo = "pocetno";

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        int difficulty = 1;
        int size = 0;

        delayFactor = false;
        flashDelay = false;



        theGrid = (GridLayout) findViewById(R.id.memory_grid);

        theGrid.setDrawingCacheBackgroundColor(Color.TRANSPARENT);

        int color = 0x00FFFFFF;




        if (nivo.equals("pocetno")) {
            matchesRemaining = 6;
            size = 80;
            difficulty = 2;
            theGrid.setRowCount(3);
            theGrid.setColumnCount(4);
        }


        int numColumns = theGrid.getColumnCount();
        int numRows = theGrid.getRowCount();

        //the number of unique cards is half the total number cards
        int numCards = (numColumns * numRows);
        SlikiKopce[] cardButtons = new SlikiKopce[numCards];
        int[] cardPictures = {R.drawable.bubackazelena,R.drawable.lastovica,R.drawable.petkotrca,R.drawable.polzav,R.drawable.zvezda,R.drawable.zhelka};

        List<Integer> pictureList = getPictureList(cardPictures , difficulty);




        int index = 0;
        for(int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {

                SlikiKopce theButton = new SlikiKopce(this, pictureList.get(index), i , j , size);
                theButton.setId(generateViewId());
                theButton.setOnClickListener(this);
                theGrid.addView(theButton);
                cardButtons[index] = theButton;
                index++;
            }
        }


    }

    @Override
    public void onClick(View view) {
        if(delayFactor || flashDelay) {
            return;
        }

        final Handler handler = new Handler();

        SlikiKopce button = (SlikiKopce) view;
        if(button.isFlipped() || button.getIsMatched())
            return;

        if(firstChoice == null) {

            firstChoice = button;
            firstChoice.flip();

            return;

        }

        if (firstChoice.getCardPictureId() == button.getCardPictureId()) {
            secondChoice = button;
            secondChoice.flip();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    firstChoice.flashGreen();
                    secondChoice.flashGreen();


                    firstChoice.setMatched();
                    secondChoice.setMatched();

                    firstChoice.setEnabled(false);
                    secondChoice.setEnabled(false);

                    firstChoice = null;
                    secondChoice = null;


                }
            }, 100);


            matchesRemaining--;
//

        }

        else {
            secondChoice = button;
            secondChoice.flip();
            delayFactor = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    firstChoice.flashRed();
                    secondChoice.flashRed();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            firstChoice.flip();
                            secondChoice.flip();
                            firstChoice = null;
                            secondChoice = null;
                            delayFactor = false;

                        }
                    }, 500);


                }
            }, 500);


        }



        if(matchesRemaining == 0) {
            int duration = Toast.LENGTH_SHORT;
            final Toast toast = Toast.makeText(this, "ПОБЕДНИК !!!",
                    duration);


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.show();

                }
            }, 1000);


            final Intent intent = new Intent(this, MainActivity.class);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);

                }
            }, 3000);

        }



    }


    private static List<Integer> getPictureList(int[] cardPictures , int difficulty) {
        int numberOfPictures = 1;
        switch(difficulty) {
            case 1: numberOfPictures = 3;
                break;
            case 2: numberOfPictures = 6;
                break;
            case 3: numberOfPictures = 12;
                break;
        }
        List<Integer> pictureList = new ArrayList<Integer>();
        for (int i = 0; i < numberOfPictures; i++) {
            int randomIndex = (int) Math.floor((Math.random() * cardPictures.length));
            if(pictureList.contains(cardPictures[randomIndex]))
                i--;
            else{

                pictureList.add(cardPictures[randomIndex]);
                pictureList.add(cardPictures[randomIndex]);
            }
        }
        Collections.shuffle(pictureList);

        return pictureList;
    }



}
