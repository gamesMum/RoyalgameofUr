package com.example.android.royalgameofur;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    //the board dimensions
    private final int COLUMNS = 3;
    private final int ROWS = 8;
    private final int STONESNUM = 7;
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
    private RelativeLayout.LayoutParams layoutParams2;
    private ArrayList<String> whiteRout;
    private ArrayList<String> blackRout;
    private ArrayList<String> specialCells;
    //how many stones on the board and outside it
    int blackStonesOutCount;
    int blackStonesInCount;
    int whiteStonesOutCount;
    int whiteStonesInCount;
    int blackStoneFinishCount;
    int whiteStoneFinishCount;
    private Rout rout;

    int routSize;
    private View.OnClickListener whiteStoneClickListener;
    private View.OnClickListener blackStoneClickListener;

    //for OnClickListener
    String  targetCellIDName;
    ImageView targetCellImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        //for OnClickListener
        targetCellIDName = null;
        targetCellImage = null;
        //initilize how many stones in and out
        blackStonesInCount = 0;
        blackStonesOutCount = 7;
        whiteStonesInCount = 0;
        whiteStonesOutCount = 7;
        blackStoneFinishCount = 0;
        whiteStonesOutCount = 0;
        routSize = 14;
        rout = new Rout();
        //create new xml element
        mainLayout = (RelativeLayout) findViewById( R.id.relativeLayout_1 );
        blackLayout = (LinearLayout) findViewById( R.id.black_layout );
        whiteLayout = (LinearLayout) findViewById( R.id.white_layout );
        boardLayout = (GridLayout) findViewById( R.id.boardLayout );
        //restore GridView
        boardLayout.removeAllViews();

        layoutParams = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT ); // or wrap_content
        layoutParams2 = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT ); // or wrap_content
        //Store the white rout and black
        whiteRout = new ArrayList<String>();
        blackRout = new ArrayList<String>();
        specialCells = new ArrayList<String>();
        //define the white and black stones onClickListener
        //This will be called for each player in thier turn and check for available moves
        blackStoneClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeTheMove( v, "black" );
            }
        };

        whiteStoneClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeTheMove( v, "white" );
            }
        };


        //set up the board on the screen
        drawBoard();

        //find the views in xml file
        //boardCells = new ImageView(  )

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
        disableClicks( "black" );
    }

    //arrange the cells on the screen in grid layout
    public void drawBoardCells() {
        //step4: draw main board (I have to draw each square)
        boardLayout.setColumnCount( COLUMNS );
        boardLayout.setRowCount( ROWS );
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                boardCell = new ImageView( this );

                //cell_0_0, cell_0_1, cell_0_2, etc...
                String imageResourceIdUrl = "drawable/" + "image_cell_" + i + "_" + j;
                int imageKey = getResources().getIdentifier( imageResourceIdUrl, "drawable", getPackageName() );

                boardCell.setImageResource( imageKey );
                boardCell.setLayoutParams( new FrameLayout.LayoutParams( 100, 100 ) );
                GridLayout.Spec rowSpan = GridLayout.spec( GridLayout.UNDEFINED, 1 );
                GridLayout.Spec colspan = GridLayout.spec( GridLayout.UNDEFINED, 1 );
                GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams( rowSpan, colspan );
                boardLayout.addView( boardCell, gridParam );
                //generate Id for each ImageView
                String imageIdUrl = "cell_" + i + "_" + j;
                int imageId = getResources().getIdentifier( imageIdUrl, "id", getPackageName() );
                boardCell.setId( imageId );

            }

        }
    }

    //draw each player stones on the screen
    public void drawStones() {
        //draw black stones
        for (int i = 0; i < STONESNUM; i++) {
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
        for (int i = 0; i < STONESNUM; i++) {
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

    private void makeTheMove(View stone, String currentPlayer) {
       // int startFromHere = 0;
       // String  targetCellIDName = null;
        //ImageView targetCellImage;
        //String stoneOldtPosition = null;
       // int oldCellID =0;
        if (diceValue > 0) {
            ArrayList<String> currentRout = new ArrayList<>();
            //set current rout fo the player
            if (currentPlayer == "black") {
                currentRout = rout.getBlackRout() ;
            } else
            {
                currentRout = rout.getWhiteRout();
            }
            //check the stone current position (get the stone's tag and continue from there

            if (stone.getTag() != null) {
               String stoneOldtPosition = stone.getTag().toString();//if the stone is already on the board
                int startFromHere = 0;
                for (String cellId : currentRout) {

                    if (cellId.equals( stoneOldtPosition )) startFromHere = currentRout.indexOf( cellId );//start counting from this id
                }
                //the target cell is current index plus the dice value
                if((startFromHere + diceValue) < routSize) {
                    targetCellIDName = currentRout.get( startFromHere + diceValue );
                }
                else if((startFromHere + diceValue) == routSize)
                {
                    stone.setVisibility( View.GONE );//the stone finish the rout successfully
                    stoneFinishedPlusOne( currentPlayer );
                    //swipe the dice to the left (black) or right (white)
                    if (currentPlayer == "black") swapTo( "white" );
                    else swapTo( "black" );
                    //roll the dice for the other player
                    diceValue = rollDice();
                    return;//do not execute the rest
                }
                else
                    Log.v(CONTEXT, "Try Again!");
                //else check if the player has another stones out
                //play them or pass it to the other player

            }
            //TODO: if it is special cell then play again
            //TODO: if there ia another player DESTROY!
            //TODO: if the player finish the rout check if stonesOutCount is 0
            //TODO: this case the player finish the game and VICTORY!
            //TODO: play with computer
            //TODO: change the target cell image to colored one
            else { //else the player picks a new stone and starts from 0 index
                //store the cell id the stone will go to
                targetCellIDName = currentRout.get( diceValue - 1 );
            }
            //find the cell imageView
            int imageId = getResources().getIdentifier( targetCellIDName, "id", getPackageName() );
            targetCellImage = findViewById( imageId );

            //check if cell is free
            if (targetCellImage.getTag() == null) {
                //if the cell is free find its position
                int[] cellLocation = new int[2];
                targetCellImage.getLocationOnScreen( cellLocation );
                int posX = cellLocation[0];
                int posY = cellLocation[1];
                //change the tag for the cell (black or white)
                targetCellImage.setTag( currentPlayer );
                //set the stone tag to the Id of the current cell
                stone.setTag( targetCellIDName );
                // move the stone to that cell
                layoutParams2.removeRule( RelativeLayout.ALIGN_PARENT_RIGHT );
                layoutParams2.removeRule( RelativeLayout.ALIGN_PARENT_END );
                //reset padding
                stone.setPadding( 0, 0, 0, 0 );
                stone.setX( posX + 35 );
                stone.setY( posY - 60 );
                //check if the stone is new or not(only if it is new change the count)
               /* if(stoneOldtPosition == null)//the stone is picked from outside of the board
                {
                    //change the count (one stone is in) out is minus one
                    stoneInPlusOne( currentPlayer );
                }*/
                Log.v(CONTEXT, "Stones on the board :" + getInStonesCount( currentPlayer )
                + " Stones out the board : " + getOutStonesCount( currentPlayer ));
                //restore the old position so it is free
             /*   if (stoneOldtPosition != null) {
                    //find the cell imageView
                    oldCellID = getResources().getIdentifier( stoneOldtPosition, "id", getPackageName() );
                    ImageView oldCellImage = findViewById( oldCellID );
                    oldCellImage.setTag( null );
                }*/
                // TEXTVIEW
                if (stone.getParent() != null) ((ViewGroup) stone.getParent()).removeView( stone );
                mainLayout.addView( stone, layoutParams2 );

            } else if (targetCellImage.getTag() == currentPlayer) {
                Toast.makeText( getApplicationContext(), "it is busy!", Toast.LENGTH_SHORT ).show();

            }
            //another player is on the cell
            else if (targetCellImage.getTag() != currentPlayer) {
                //TODO : kill the pther player
                Toast.makeText( getApplicationContext(), "other player resting here!", Toast.LENGTH_SHORT ).show();
            }

            //swipe the dice to the left (black) or right (white)
            if (currentPlayer == "black") swapTo( "white" );
            else swapTo( "black" );
            //roll the dice for the other player
            diceValue = rollDice();

        } else {
            Toast.makeText( getApplicationContext(), "You've got 0!", Toast.LENGTH_SHORT ).show();
            //swipe the dice to the left (black) or right (white)
            if (currentPlayer == "black") swapTo( "white" );
            else swapTo( "black" );
            //roll the dice for the other player
            diceValue = rollDice();

        }
        //...............

        //2: check available moves for that player5
        //3: perform the move
        //is the game finished yet

    }

    private void swapTo(String player) {
        if (player == "white") {
            layoutParams.removeRule( RelativeLayout.ALIGN_PARENT_LEFT );
            layoutParams.addRule( RelativeLayout.ALIGN_PARENT_RIGHT );
            mainLayout.removeView( dice );
            mainLayout.addView( dice, layoutParams );
            //disable clicks for black and enable it for the white
            disableClicks( "black" );
        } else {
            layoutParams.removeRule( RelativeLayout.ALIGN_PARENT_RIGHT );
            layoutParams.addRule( RelativeLayout.ALIGN_PARENT_LEFT );
            mainLayout.removeView( dice );
            mainLayout.addView( dice, layoutParams );
            //disable clicks for white and enable it for the black
            disableClicks( "white" );
        }

    }


    private void disableClicks(String player) {
        if (player == "black") //disable black & enable white
        {
            for (int i = 0; i < 7; i++) {
                //generate Id for each ImageView
                String blackImageIdUrl = "black_" + i;
                String whiteImageIdUrl = "white_" + i;
                int blackImageId = getResources().getIdentifier( blackImageIdUrl, "id", getPackageName() );
                int whiteImageId = getResources().getIdentifier( whiteImageIdUrl, "id", getPackageName() );
                ImageView blackStone = findViewById( blackImageId );
                ImageView whitekStone = findViewById( whiteImageId );
                blackStone.setClickable( false );//can't play now
                whitekStone.setClickable( true );//it is whit's turn

            }
        } else //disable white and enable black
        {
            for (int i = 0; i < 7; i++) {
                //generate Id for each ImageView
                String blackImageIdUrl = "black_" + i;
                String whiteImageIdUrl = "white_" + i;
                int blackImageId = getResources().getIdentifier( blackImageIdUrl, "id", getPackageName() );
                int whiteImageId = getResources().getIdentifier( whiteImageIdUrl, "id", getPackageName() );
                ImageView blackStone = findViewById( blackImageId );
                ImageView whitekStone = findViewById( whiteImageId );
                blackStone.setClickable( true );//it is whit's turn
                whitekStone.setClickable( false );//can't play now

            }
        }
    }

    private void stoneInPlusOne(String player) {
        if (player == "black") {
            blackStonesOutCount--;
            blackStonesInCount++;
        } else {
            whiteStonesOutCount--;
            whiteStonesInCount++;
        }
    }

    //if a stone is either killed or finished the rout successfully
    private void stoneOutPlusOne(String player) {
        if (player == "black") {
            blackStonesOutCount++;
            blackStonesInCount--;
        } else {
            whiteStonesOutCount++;
            whiteStonesInCount--;
        }
    }

    private void stoneFinishedPlusOne(String player)
    {
        switch(player)
        {
            case "black":
                blackStoneFinishCount++;
                break;

            case "white":
                whiteStoneFinishCount++;
                break;
        }
    }

    //how many stones are on the board
    private int getInStonesCount(String player)
    {
        if(player == "black")
            return blackStonesInCount;
        else
            return whiteStonesInCount;

    }
    //how many stones are out
    private int getOutStonesCount(String player)
    {
        if(player == "black")
            return blackStonesOutCount;
        else
            return whiteStonesOutCount;

    }
}
