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
    private final int  COLUMNS = 3;
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
    private View.OnClickListener whiteStoneClickListener;
    private View.OnClickListener blackStoneClickListener;
    private  boolean whiteBusy;
    private  boolean blackBusy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        //initiating booleans
        whiteBusy = true;
        blackBusy = false;
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
        whiteRout = new ArrayList<String>(  );
        blackRout = new ArrayList<String>(  );

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
        //TODO 1: diable the Clicks on all black stones
        disableClicks("black");
        Log.v(CONTEXT, "dice value for the first time is"+ diceValue );

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

    //arrange the cells on the screen in grid layout
    public void drawBoardCells() {
        //step4: draw main board (I have to draw each square)
        boardLayout.setColumnCount(COLUMNS);
        boardLayout.setRowCount(ROWS);
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                boardCell = new ImageView( this );

                //cell_0_0, cell_0_1, cell_0_2, etc...
                String imageResourceIdUrl = "drawable/" + "image_cell_" + i + "_" + j;
                int imageKey = getResources().getIdentifier( imageResourceIdUrl, "drawable", getPackageName() );

                boardCell.setImageResource( imageKey );
                boardCell.setLayoutParams(  new FrameLayout.LayoutParams( 100, 100 ) );
                GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1);
                GridLayout.Spec colspan = GridLayout.spec(GridLayout.UNDEFINED, 1);
                GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(
                        rowSpan, colspan);
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
        for (int i = 0; i <= STONESNUM; i++) {
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

    private void makeTheMove(View stone, String currentPlayer)
    {

        ArrayList<String> currentRout = new ArrayList<>(  );
        //set current rout fo the player
        if(currentPlayer == "black")
        {
            currentRout = blackRout;
        }
        else currentRout = whiteRout;

            //++get the current location of the clicked stone
            //++start the currentRout from that location
            //Check available moves and make the move
            //get the cell the dice value represents
            if(diceValue > 0) {
                //store the cell id the stone will go to
                String targetCellIDName = currentRout.get( diceValue - 1 );
                int imageId = getResources().getIdentifier( targetCellIDName,
                        "id", getPackageName() );
                ImageView targetCellImage = findViewById( imageId );
                int[] cellLocation = new int[2];
                targetCellImage.getLocationOnScreen( cellLocation );
                int posX = cellLocation[0];
                int posY = cellLocation[1];
                //check if cell is free
                if(targetCellImage.getTag() == null)
                {
                    //targetCellImage.setVisibility( View.INVISIBLE );
                    //1: make the move: move one stone and remove it from stones column
                    //store the current x position
                    //change the stone position and change the tag
                    //3: move the stone to that cell
                    layoutParams2.removeRule( RelativeLayout.ALIGN_PARENT_RIGHT );
                    layoutParams2.removeRule( RelativeLayout.ALIGN_PARENT_END );
                    //reset padding
                    stone.setPadding(0, 0, 0, 0);
                    stone.setX(posX); // -   250
                    stone.setY(posY); //- 70

                    //for testing++++++++++++++++++++++++++++++++++++++
                    int[] stoneLocation = new int[2];
                    stone.getLocationOnScreen( stoneLocation );
                    int stoneX = stoneLocation[0];
                    int stoneY = stoneLocation[1];
                    ///////////////////////////////////
                    Log.v(CONTEXT, "This is cell x, y: " + posX +
                            ", "+ posY);
                    Log.v(CONTEXT, "This is stone x, y: " + stoneX +
                            ", "+ stoneY);
                    //change the tag for the cell (black or white)
                    targetCellImage.setTag( currentPlayer );
                    // TEXTVIEW
                    if(stone.getParent()!=null)
                        ((ViewGroup)stone.getParent()).removeView(stone);
                    mainLayout.addView( stone, layoutParams2 );

                }else if(targetCellImage.getTag() == "black") {
                    Toast.makeText( getApplicationContext(), "other player resting here!",
                            Toast.LENGTH_SHORT).show();
                }else if(targetCellImage.getTag() == "white") {
                    Toast.makeText( getApplicationContext(), "it is busy!",
                            Toast.LENGTH_SHORT).show();
                }


                //add delay to get player attention
            }else{
                Toast.makeText( getApplicationContext(), "You've got 0!",
                        Toast.LENGTH_SHORT).show();
            }
            //...............
            //swipe the dice to the left (black) or right (white)
            if(currentPlayer == "black")
            swapTo("white");
            else swapTo("black");

        //2: check available moves for that player5
        //3: perform the move
        //is the game finished yet

    }
    private void swapTo(String player)
    {
        if(player == "white") {
            whiteBusy = true;
            blackBusy = false; //he is not playing
            layoutParams.removeRule( RelativeLayout.ALIGN_PARENT_LEFT );
            layoutParams.addRule( RelativeLayout.ALIGN_PARENT_RIGHT );
            mainLayout.removeView( dice );
            mainLayout.addView( dice, layoutParams );
            //roll the dice for the other player
            diceValue = rollDice();
            //disable clicks for black and enable it for the white
            disableClicks( "black" );
        }
        else
        {
            blackBusy = true;
            whiteBusy = false; //he is not playing
            layoutParams.removeRule( RelativeLayout.ALIGN_PARENT_RIGHT );
            layoutParams.addRule( RelativeLayout.ALIGN_PARENT_LEFT );
            mainLayout.removeView( dice );
            mainLayout.addView( dice, layoutParams );
            //roll the dice for the other player
            diceValue = rollDice();
            //disable clicks for white and enable it for the black
            disableClicks("white");
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
                blackStone.setClickable(false);//can't play now
                whitekStone.setClickable(true);//it is whit's turn

            }
        }
        else //disable white and enable black
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

}
