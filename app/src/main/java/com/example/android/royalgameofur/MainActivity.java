package com.example.android.royalgameofur;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    //define the views variables
    private final String CONTEXT = "MainActivity";
    private RelativeLayout mainLayout;
    private LinearLayout blackLayout;
    private LinearLayout whiteLayout;
    private GridLayout boardLayout;
    private ImageView boardCell;
    private ImageView blackStone;
    private ImageView whiteStone;
    private ImageView dice;
    private int diceValue;
    private boolean busy;
    private final boolean YOU_WON = true;
    private RelativeLayout.LayoutParams layoutParams;
    private ArrayList<String> whiteRout;
    private ArrayList<String> blackRout;
    private ArrayList<String> busyCells;
    private View.OnClickListener whiteStoneClickListener;
    private View.OnClickListener blackStoneClickListener;

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
        boardLayout = (GridLayout) findViewById( R.id.boardLayout );

        layoutParams = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT ); // or wrap_content

        //Store the white rout
        whiteRout = new ArrayList<String>(  );
        blackRout = new ArrayList<String>(  );
        busyCells = new ArrayList<String>(  );

        //define the white and black stones onClickListener
        //This will be called for each player in thier turn and check for available moves
        blackStoneClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1: check the dice position (which player will play now)
                //get the location of the dice
                int[] outLocation = new int[2];
                dice.getLocationOnScreen( outLocation );
                int x = outLocation[0];
                //check if the dice is on the left or the right of the screen
                if (x != 488)//the dice is on the left(black)
                {

                    //Check available moves and make the move
                    //get the cell the dice value represents
                    if(diceValue > 0) {
                        //check if cell is free (not busy)
                        if(!(cellIsBusy(blackRout.get( diceValue - 1 ))));
                        {
                            //1: make the move
                            //2: set the cell as busy
                            busyCells.add(blackRout.get( diceValue - 1 ));
                            //3: move the stone to that cell
                        }
                        Log.v( CONTEXT, "the cell id is: " + blackRout.get( diceValue - 1 ) );
                        //mark this cell as busy so hte other player won't ne able to move there

                        //add delay to get player attention
                    }else
                        Log.v( CONTEXT, "You got 0...try again next time!!" );

                    //...............
                    //swipe the dice to the right (white)
                    layoutParams.removeRule( RelativeLayout.ALIGN_PARENT_LEFT );
                    layoutParams.addRule( RelativeLayout.ALIGN_PARENT_RIGHT );
                    mainLayout.removeView( dice );
                    mainLayout.addView( dice, layoutParams );
                    //roll the dice for the other player
                    diceValue = rollDice();

                }
                else
                {
                    //the dice is on the left
                    Toast.makeText( getApplicationContext(), "Wait for your turn!",
                            Toast.LENGTH_SHORT ).show();
                }


                //2: check available moves for that player5
                //3: perform the move
                //is the game finished yet

            }
        };

        whiteStoneClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //1: check the dice position (which player will play now)
                //get the location of the dice
                int[] outLocation = new int[2];
                dice.getLocationOnScreen( outLocation );
                int x = outLocation[0];
                //check if the dice is on the left or the right of the screen
                if (x == 488)//the dice is on the right(white)
                {

                    //Check available moves and make the move
                    //get the cell the dice value represents
                    if(diceValue > 0) {
                        //check if cell is free (not busy)
                        if(!(cellIsBusy(whiteRout.get( diceValue - 1 ))));
                        {
                            //1: make the move
                            //2: set the cell as busy
                            busyCells.add(whiteRout.get( diceValue - 1 ));
                            //3: move the stone to that cell
                        }
                        Log.v( CONTEXT, "the cell id is: " + whiteRout.get( diceValue - 1 ) );
                        //mark this cell as busy so hte other player won't ne able to move there

                        //add delay to get player attention
                    }else

                    Log.v( CONTEXT, "You got 0!!" );

                    //...............
                    //swipe the dice to the left (black)
                    layoutParams.removeRule( RelativeLayout.ALIGN_PARENT_RIGHT );
                    layoutParams.addRule( RelativeLayout.ALIGN_PARENT_LEFT );
                    mainLayout.removeView( dice );
                    mainLayout.addView( dice, layoutParams );
                   //roll the dice for the other player
                   diceValue = rollDice();

                }
                else
                    {
                    //the dice is on the left
                        Toast.makeText( getApplicationContext(), "Wait for your turn!",
                                Toast.LENGTH_SHORT ).show();
                }


                //2: check available moves for that player5
                //3: perform the move
                //is the game finished yet
               // Log.v( CONTEXT, "YOU CLICKED ME!!...I'm " + v.getResources().getResourceName( v.getId() ) );
            }
        };



        //set up the board on the screen
        drawBoard();



        //find the views in xml file
        //boardCells = new ImageView(  )

    }

    //check busyCells
    public boolean cellIsBusy(String cellID)
    {
        //check if the cell is busy
        for(String cell : busyCells)
        {
            if(cellID == cell)
            {
                return true;
            }
        }
        return false;
    }

    //arrange the board elements on the screen
    private void drawBoard() {

        //set thr image at the bottom right of the relative layout
        layoutParams.addRule( RelativeLayout.ALIGN_PARENT_BOTTOM );
        layoutParams.addRule( RelativeLayout.ALIGN_PARENT_RIGHT );
        //-------------------------------------------------------------------------------
        //draw the dice
        dice = new ImageView( this );
        dice.setImageResource( R.drawable.dice_num_1 );
        mainLayout.addView( dice, layoutParams );

        //draw the stones (black and white)
        drawStones();

        //Draw the cells
        drawBoardCells();

        //rol dice for the first time
        diceValue = rollDice();
        Log.v(CONTEXT, "dice value for the first time is"+ diceValue );

    }

    //arrange the cells on the screen in grid layout
    public void drawBoardCells() {
        //step4: draw main board (I have to draw each square)
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 2; j++) {
                boardCell = new ImageView( this );

                //cell_0_0, cell_0_1, cell_0_2, etc...
                String imageResourceIdUrl = "drawable/" + "image_cell_" + i + "_" + j;
                int imageKey = getResources().getIdentifier( imageResourceIdUrl, "drawable", getPackageName() );

                boardCell.setImageResource( imageKey );
                boardLayout.addView( boardCell );

                //generate Id for each ImageView
                String imageIdUrl = "cell_" + i + "_" + j;
                int imageId = getResources().getIdentifier( imageIdUrl, "id", getPackageName() );
                boardCell.setId( imageId );
                //store white rout in an array

                whiteRout.add("cell_3_2");
                whiteRout.add("cell_2_2");
                whiteRout.add("cell_1_2");
                whiteRout.add("cell_0_2");
                whiteRout.add("cell_0_1");
                whiteRout.add("cell_1_1");
                whiteRout.add("cell_2_1");
                whiteRout.add("cell_3_1");
                whiteRout.add("cell_4_1");
                whiteRout.add("cell_5_1");
                whiteRout.add("cell_6_1");
                whiteRout.add("cell_7_1");
                whiteRout.add("cell_7_2");
                whiteRout.add("cell_6_2");

                //store black rout in an array

                blackRout.add("cell_3_0");
                blackRout.add("cell_2_0");
                blackRout.add("cell_1_0");
                blackRout.add("cell_0_0");
                blackRout.add("cell_0_1");
                blackRout.add("cell_1_1");
                blackRout.add("cell_2_1");
                blackRout.add("cell_3_1");
                blackRout.add("cell_4_1");
                blackRout.add("cell_5_1");
                blackRout.add("cell_6_1");
                blackRout.add("cell_7_1");
                blackRout.add("cell_7_0");
                blackRout.add("cell_6_0");

            }
        }
    }

    //draw each player stones on the screen
    public void drawStones() {
        //draw black stones
        for (int i = 0; i <= 6; i++) {
            blackStone = new ImageView( this );

            blackStone.setImageResource( R.drawable.black );

            //add bottom padding
            blackStone.setPadding( 16, 0, 0, 16 );
            blackLayout.addView( blackStone );

            //attach the onClickListener to each image cell
            blackStone.setOnClickListener( blackStoneClickListener );
            //generate Id for each ImageView
            String imageIdUrl = "black_" + i;
            int imageId = getResources().getIdentifier( imageIdUrl, "id", getPackageName() );
            blackStone.setId( imageId );

        }

        //draw white stones
        for (int i = 0; i <= 6; i++) {
            whiteStone = new ImageView( this );

            whiteStone.setImageResource( R.drawable.white );

            //add bottom padding
            whiteStone.setPadding( 0, 0, 16, 16 );
            whiteLayout.addView( whiteStone );

            //attach the onClickListener to each image cell
            whiteStone.setOnClickListener( whiteStoneClickListener );
            //generate Id for each ImageView
            String imageIdUrl = "white_" + i;
            int imageId = getResources().getIdentifier( imageIdUrl, "id", getPackageName() );
            whiteStone.setId( imageId );

        }

    }

    //manage the process of rolling the dice
    private int rollDice() {
        Random rand = new Random();
        int num = rand.nextInt( 5 );
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
