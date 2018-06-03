package com.example.android.royalgameofur;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    //define the views variables
    private final String CONTEXT = "MainActivity";
    private RelativeLayout mainLayout;
    private LinearLayout blackLayout;
    private LinearLayout whiteLayout;
    private ImageView urBoard;
    private ImageView blackStone;
    private ImageView whiteStone;
    private ImageView dice;
    private int diceValue;
    private RelativeLayout.LayoutParams layoutParams;

    private int blackDiceResult;
    private int whiteDiceResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        //create new xml element
        mainLayout = (RelativeLayout) findViewById( R.id.relativeLayout_1 );
        blackLayout = (LinearLayout) findViewById( R.id.black_layout );
        whiteLayout = (LinearLayout) findViewById( R.id.white_layout );

        //draw the dice
        dice = new ImageView( this );
        dice.setImageResource( R.drawable.dice_num_1 );
        layoutParams = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT ); // or wrap_content

        drawInitboard();
        //find the views in xml file
        urBoard = findViewById( R.id.main_board );

    }

    //draw the 7 black and white stones
    private void drawInitboard() {

        //set thr image at the bottom right of the relative layout
        layoutParams.addRule( RelativeLayout.ALIGN_PARENT_BOTTOM );
        layoutParams.addRule( RelativeLayout.ALIGN_PARENT_RIGHT );
        //-------------------------------------------------------------------------------

        mainLayout.addView( dice, layoutParams );

        for (int i = 0; i <= 6; i++) {
            blackStone = new ImageView( this );
            whiteStone = new ImageView( this );

            whiteStone.setImageResource( R.drawable.white );
            blackStone.setImageResource( R.drawable.black );


            //add bottom padding
            blackStone.setPadding( 16, 0, 0, 16 );
            whiteStone.setPadding( 0, 0, 16, 16 );
            blackLayout.addView( blackStone );
            whiteLayout.addView( whiteStone );

        }

        //roll the dice once the player touches the dice
        dice.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //store the value
                diceValue = rollDice();

                //Make the move for the player


                //get the location of the dice
                int[] outLocation = new int[2];
                dice.getLocationOnScreen( outLocation );
                int x = outLocation[0];
                //check if the dice is on the left or the right of the screen
                if (x == 488)//the dice is on the right
                {
                    //transfere it to the right
                    layoutParams.removeRule( RelativeLayout.ALIGN_PARENT_RIGHT );
                    layoutParams.addRule( RelativeLayout.ALIGN_PARENT_LEFT );
                    mainLayout.removeView( dice );
                    mainLayout.addView( dice, layoutParams );

                } else //the dice is on the left
                {
                    layoutParams.removeRule( RelativeLayout.ALIGN_PARENT_LEFT );
                    layoutParams.addRule( RelativeLayout.ALIGN_PARENT_RIGHT );
                    mainLayout.removeView( dice );
                    mainLayout.addView( dice, layoutParams );
                }


            }

        } );


    }

    private int rollDice() {
        Random rand = new Random();
        int num = rand.nextInt( 5 );
        Log.v( CONTEXT, "The random number is " + num );

        switch (num) {
            case 1:
                dice.setImageResource( R.drawable.dice_num_1 );
                break;
            case 2:
                dice.setImageResource( R.drawable.dice_num_2 );
                break;
            case 3:
                dice.setImageResource( R.drawable.dice_num_3 );
                break;
            case 4:
                dice.setImageResource( R.drawable.dice_num_4 );
                break;
            case 0:
                dice.setImageResource( R.drawable.dice_num_0 );

        }
        return num;

    }


}
