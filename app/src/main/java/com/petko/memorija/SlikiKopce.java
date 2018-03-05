package com.petko.memorija;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.widget.GridLayout;

/**
 * Created by Anti on 3/5/2018.
 */

public class SlikiKopce   extends android.support.v7.widget.AppCompatButton {
    private static int size;
    private String nameId;
    private boolean isFlipped;
    private boolean isMatched;
    private int row;
    private int col;

    private Drawable background;
    private Drawable front;
    private int cardPictureId;


    public SlikiKopce (Context context, int cardPictureId , int row, int col, int size ) {

        super(context);
        isFlipped = false;
        isMatched = false;
        this.nameId = nameId;
        this.row = row;
        this.col = col;
        this.size = size;

        this.cardPictureId = cardPictureId;

        GridLayout.LayoutParams theParams = new GridLayout.LayoutParams(GridLayout.spec(row) , GridLayout.spec(col));
        theParams.width = (int) getResources().getDisplayMetrics().density * size;
        theParams.height = (int) getResources().getDisplayMetrics().density * size;
        theParams.setMargins(size*30/100,size*30/100,size*30/100,size*30/100);


        setLayoutParams(theParams);
        front = ContextCompat.getDrawable(context, cardPictureId);
        background = ContextCompat.getDrawable(context, R.drawable.prednaslika  );
        setBackground(background);




    }

    public void flip() {

        if(isMatched) {
            return;
        }

        if(isFlipped) {
            setBackground(background);
            isFlipped = false;
        }
        else {
            setBackground(front);
            isFlipped = true;
        }


    }

    public void flashGreen() {
        setBackgroundColor(Color.TRANSPARENT);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setBackground(front);

            }
        }, 500);
    }


    public void flashRed() {

        setBackgroundColor(Color.TRANSPARENT);




    }




    public boolean isFlipped() {
        return isFlipped;
    }


    public boolean getIsMatched() {
        return isMatched;
    }

    public void setMatched() {
        isMatched = !isMatched;
    }

    public int getCardPictureId() {
        return cardPictureId;
    }
}

