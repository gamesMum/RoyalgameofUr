package com.example.android.royalgameofur;

import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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
    private final boolean YOU_WON = true;
    private RelativeLayout.LayoutParams layoutParams;
    private RelativeLayout.LayoutParams layoutParams2;
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
    int imageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        //for OnClickListener
        targetCellIDName = null;
        targetCellImage = null;
        imageId = 0;
        //initilize how many stones in and out
        blackStonesInCount = 0;
        blackStonesOutCount = 7;
        whiteStonesInCount = 0;
        whiteStonesOutCount = 7;
        blackStoneFinishCount = 0;
        whiteStonesOutCount = 7;
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
        //to prevent the image from changing size (this is for the stone created on the board)
        layoutParams2.height = 40;
        layoutParams2.width = 40;
        //////////////////////////////////////////////////////////////////////////////////////
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

        //rol dice for the first time & disable clicks for the black
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
            blackLayout.addView( blackStone);

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
            whiteLayout.addView( whiteStone);

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
        //use this to attack the enemy
        //get the stone ID to pass it to the new added stone
        int stoneId = stone.getId();
        String stoneIDNameLong  = getResources().getResourceName( stoneId );
        String stoneIDName = stoneIDNameLong.substring( stoneIDNameLong.indexOf( "/" ) + 1 );
        String opponent = null;
        LinearLayout opponentLayout = null;
        if(currentPlayer == "black") {
            opponent = "white";
            opponentLayout = (LinearLayout)findViewById( R.id.white_layout );
        }
        else
        {
            opponent = "black";
            opponentLayout = (LinearLayout)findViewById( R.id.black_layout );
        }
        ////////////////////////////////////////////////////////
        if(diceValue == 0 || numberOfMoves(currentPlayer, diceValue)==0)//swap to the other player
        {
            //swap the dice to the left (black) or right (white)
            if (currentPlayer == "black") swapTo( "white" );
            else swapTo( "black" );
            //roll the dice for the other player
            diceValue = rollDice();
        }

        else {
            Log.v(CONTEXT, "you have " + numberOfMoves(currentPlayer, diceValue) + "number of moves");
                //if the dice value is not 0 and there is available move
                ArrayList<String> currentRout;
                LinearLayout currentLayout;
                //set current rout fo the player
                if (currentPlayer == "black"){
                    //get the origional position for the stone
                     currentLayout = (LinearLayout) findViewById(R.id.black_layout);
                    currentRout = rout.getBlackRout();
                }
                else {
                    //get the origional position for the stone
                     currentLayout = (LinearLayout) findViewById(R.id.white_layout);
                    currentRout = rout.getWhiteRout();
                }
                //check the stone current position (get the stone's tag and continue from there

                if (stone.getTag() == null)//new picked out stone from the shelf
                {
                    targetCellIDName = currentRout.get( diceValue - 1 );
                    //find the cell imageView
                    int imageId = getResources().getIdentifier( targetCellIDName, "id", getPackageName() );
                    targetCellImage = findViewById( imageId );

                    //check if cell is free
                    if (targetCellImage.getTag() == null) {
                        //get the cell position
                        int[] cellLocation = new int[2];
                        targetCellImage.getLocationOnScreen( cellLocation );
                        int posX = cellLocation[0];
                        int posY = cellLocation[1];
                        //change the tag for the cell (black or white)
                        targetCellImage.setTag( stoneIDName );
                        //
                        //add new stone on the board representing the removed stone
                        addStoneAt( currentPlayer, posX, posY, targetCellIDName, stoneIDName );
                        stoneInPlusOne( currentPlayer );//new stone added on the board
                        if (stone.getParent() != null) ((ViewGroup) stone.getParent()).removeView( stone );
                        //check if it is a special cell
                        if (CheckSpeciLCell(targetCellIDName))
                        {
                           //roll the dice again for the same player
                            //don't swap
                            diceValue = rollDice();
                        }
                        //it is not special cell swap it for the other player
                        else {
                            //swap the dice to the left (black) or right (white)
                            if (currentPlayer == "black") swapTo( "white" );
                            else swapTo( "black" );
                            //roll the dice for the other player
                            diceValue = rollDice();
                        }
                        //+++++++++++++++++++++++++++++++++++++++++++++++++

                    }
                    //the target cell is busy
                    //keep the player turn unless there are no available moves
                    else if (targetCellImage.getTag() == currentPlayer && numberOfMoves( currentPlayer, diceValue ) != 0) {
                        Toast.makeText( getApplicationContext(), "it is busy!", Toast.LENGTH_SHORT ).show();

                    }

                    //TODO: play with computer
                    //TODO: change the target cell image to colored one
                    // TEXTVIEW


                }
                //if the stone is already on the board
                else {

                    String stoneOldtPosition = stone.getTag().toString();
                    int startFromHere = 0;
                    int moveThisNumberOfStones = 0;
                    for (String cellId : currentRout) {
                        //start counting from this id
                        if (cellId.equals( stoneOldtPosition )) startFromHere = currentRout.indexOf( cellId );                    }
                    //the target cell is current index plus the dice value
                    moveThisNumberOfStones = startFromHere + diceValue;
                    if (moveThisNumberOfStones < routSize) {
                        targetCellIDName = currentRout.get( startFromHere + diceValue );
                        //find the cell imageView
                        imageId = getResources().getIdentifier( targetCellIDName, "id", getPackageName() );
                        targetCellImage = findViewById( imageId );

                        //check if cell is free
                        if (targetCellImage.getTag() == null) {
                            //if the cell is free find its position
                            int[] cellLocation = new int[2];
                            targetCellImage.getLocationOnScreen( cellLocation );
                            int posX = cellLocation[0];
                            int posY = cellLocation[1];
                            //change the tag for the cell (black or white)
                            targetCellImage.setTag( stoneIDName );
                            //set the stone tag to the Id of the current cell
                            stone.setTag( targetCellIDName );
                            // move the stone to that cell
                           /* layoutParams2.removeRule( RelativeLayout.ALIGN_PARENT_RIGHT );
                            layoutParams2.removeRule( RelativeLayout.ALIGN_PARENT_END );*/
                            stone.setX( posX + 35 );
                            stone.setY( posY - 60 );
                            // //restore the old position so it is free
                            int oldCellID = getResources().getIdentifier( stoneOldtPosition, "id", getPackageName() );
                            ImageView oldCellImage = findViewById( oldCellID );
                            oldCellImage.setTag( null );
                            if(CheckSpeciLCell( targetCellIDName ))
                            {
                                //roll the dice for the same player
                                diceValue = rollDice();
                            }
                            //swap it it is not aspecial cell
                            else {
                                //swap the dice to the left (black) or right (white)
                                if (currentPlayer == "black") swapTo( "white" );
                                else swapTo( "black" );
                                //roll the dice for the other player
                                diceValue = rollDice();
                                //+++++++++++++++++++++++++++++++++++++++++++++++++
                            }

                        }

                        //the target cell is busy
                        //keep the player turn unless there are no available moves
                        else if (targetCellImage.getTag().toString().charAt( 0 ) == currentPlayer.charAt( 0 )) {
                            Log.v( CONTEXT, "first letter in targetCellImage :" + targetCellImage.getTag().toString().charAt( 0 ));
                            Log.v( CONTEXT, "first letter in currentPlayer :" +  currentPlayer.charAt( 0 ));
                            Toast.makeText( getApplicationContext(), "it is busy!", Toast.LENGTH_SHORT ).show();


                        }
                        //another player is on the cell
                        //need to kill the other player (add the logic later)
                        else if (targetCellImage.getTag() != null &&
                                targetCellImage.getTag().toString().charAt( 0 ) != currentPlayer.charAt( 0 )) {
                            Log.v( CONTEXT, "first letter in targetCellImage :" + targetCellImage.getTag().toString().charAt( 0 ));
                            Log.v( CONTEXT, "first letter in currentPlayer :" +  currentPlayer.charAt( 0 ));
                            if(CheckSpeciLCell( targetCellIDName ))
                            {
                                //the enemy is protected
                                Toast.makeText( getApplicationContext(), "can't touch this!", Toast.LENGTH_SHORT ).show();
                            }
                            //he is dead
                            else {
                                //TODO : kill the pther player
                                Toast.makeText( getApplicationContext(), "Sorry you are dead!", Toast.LENGTH_SHORT ).show();
                                //the player is killed and back out
                                //remove  stone from the board
                                //1:get the ID of the enemy stone
                                String enemyStoneName = targetCellImage.getTag().toString();
                                int enemyStoneImageID = getResources().getIdentifier( enemyStoneName, "id", getPackageName() );
                                ImageView enemyStone = (ImageView)findViewById( enemyStoneImageID );
                                if(enemyStone != null)
                                {
                                   //
                                    mainLayout.removeView( enemyStone );//remove the enemy stone
                                    goBackToLinear(opponent, opponentLayout, enemyStoneName);
                                    stoneOutPlusOne( opponent );
                                    int[] cellLocation = new int[2];
                                    targetCellImage.getLocationOnScreen( cellLocation );
                                    int posX = cellLocation[0];
                                    int posY = cellLocation[1];
                                    //change the tag for the cell (black or white)
                                    targetCellImage.setTag( stoneIDName );
                                    //set the stone tag to the Id of the current cell
                                    stone.setTag( targetCellIDName );

                                    stone.setX( posX + 35 );
                                    stone.setY( posY - 60 );
                                    // //restore the old position so it is free
                                    int oldCellID = getResources().getIdentifier( stoneOldtPosition, "id", getPackageName() );
                                    ImageView oldCellImage = findViewById( oldCellID );
                                    oldCellImage.setTag( null );

                                    //swipe the dice to the left (black) or right (white)
                                    if (currentPlayer == "black") swapTo( "white" );
                                    else swapTo( "black" );
                                    //roll the dice for the other player
                                    diceValue = rollDice();
                                }
                                }

                        }
                    } else if (moveThisNumberOfStones == routSize) {
                        //the stone finish the rout successfully
                       //if (stone.getParent() != null) ((ViewGroup) stone.getParent()).removeView( stone );
                        stone.setVisibility( View.GONE );
                        stoneFinishedPlusOne( currentPlayer );
                        int oldCellID = getResources().getIdentifier( stoneOldtPosition, "id", getPackageName() );
                        ImageView oldCellImage = findViewById( oldCellID );
                        oldCellImage.setTag( null );
                        if(getStoneFinishCount( currentPlayer ) == 7)//all stones finished
                        {
                            //state the winner and stop the game
                            // custom dialog
                            final Dialog dialog = new Dialog(this);
                            dialog.setContentView(R.layout.win_dialog);

                            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                            TextView text = (TextView) dialog.findViewById( R.id.text );
                            text.setText( "Congratulation!!!\n" + currentPlayer + " wins!!" );
                            // if button is clicked, close the custom dialog
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                   finish();//close the app
                                   System.exit( 0 );
                                }
                            });
                            dialog.show();
                        }
                        //swipe the dice to the left (black) or right (white)
                        if (currentPlayer == "black") swapTo( "white" );
                        else swapTo( "black" );
                        //roll the dice for the other player
                        diceValue = rollDice();

                    }else if(moveThisNumberOfStones > routSize)
                    {
                        Toast.makeText( getApplicationContext(), "can't move this one!", Toast.LENGTH_SHORT ).show();
                    }
                    //else the value is bigger than what it left
                    else Log.v( CONTEXT, "Try Again!" );
                    //else check if the player has another stones out
                    //play them or pass it to the other player

                }




        }

        //2: check available moves for that player
        //3: perform the move
        //is the game finished yet

    }

    //takes cell Id and checks if it is special (playtwice) or not
    private boolean CheckSpeciLCell(String targetCellIDName) {
        ArrayList<String> specialCells = rout.getSpecialCells();
        for(String cell : specialCells)
        {
            if(cell.equals( targetCellIDName ) )
            {
                return true;
            }
        }
        return false;
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
            for (int i = 0; i < STONESNUM; i++) {
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
            for (int i = 0; i < STONESNUM; i++) {
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
                blackStonesInCount--;//not on the board
                break;

            case "white":
                whiteStoneFinishCount++;
                whiteStonesInCount--;//not on the board
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

    private  int getStoneFinishCount(String player)
    {
        if(player == "black")
            return blackStoneFinishCount;
        else
            return whiteStoneFinishCount;

    }

    public void addStoneAt(String player, int x, int y, String tag, String ID)
    {
        int posX = x + 35;
        int posY = y - 60;
        ImageView stone = new ImageView( this );
        if(player == "black")
        {
            //remove any previous rules related to the stone previous layout

            stone.setX(posX);
            stone.setY(posY);
            stone.setTag( tag );
            int imageId = getResources().getIdentifier( ID, "id", getPackageName() );
            stone.setId( imageId);
            stone.setImageResource( R.drawable.black );
            mainLayout.addView( stone , layoutParams2);
            stone.setOnClickListener( blackStoneClickListener );

        }
        else{
            //remove any previous rules related to the stone previous layout
            stone.setX(posX);
            stone.setY(posY);
            stone.setTag( tag );
            int imageId = getResources().getIdentifier( ID, "id", getPackageName() );
            stone.setId( imageId);
            stone.setImageResource( R.drawable.white );
            mainLayout.addView( stone, layoutParams2 );
            stone.setOnClickListener( whiteStoneClickListener );

        }

    }

    public void goBackToLinear(String opponent, LinearLayout opponentLayout, String oppenentStoneID)
    {
        ImageView stone = new ImageView( this );
        int imageId = getResources().getIdentifier( oppenentStoneID, "id", getPackageName() );
        stone.setId( imageId);

        if(opponent == "black") {
            stone.setImageResource( R.drawable.black );
            stone.setPadding( 16, 0, 0, 16 );
            opponentLayout.addView( stone );
            stone.setOnClickListener( blackStoneClickListener );
        }
        else{
            stone.setImageResource( R.drawable.white );
            stone.setPadding( 0, 0, 16, 16 );
            opponentLayout.addView( stone );
            stone.setOnClickListener( whiteStoneClickListener );
        }

    }

    public int numberOfMoves(String player, int diceValue) {
        int movesNumber = 0;
        int startFromHere = 0;
        if (player == "black") {
            //check each stone position
            for (int i = 0; i < STONESNUM; i++) {
                String blackImageIdUrl = "black_" + i;

                int blackImageId = getResources().getIdentifier( blackImageIdUrl, "id", getPackageName() );
                ImageView blackStone = findViewById( blackImageId );
                if (blackStone.getVisibility() == View.VISIBLE) { //if the stone is not finished
                    if (blackStone.getTag() == null) {
                        startFromHere = -1;
                    } else//if the stone is on the board
                    {
                        String blackStoneCurrentCell = blackStone.getTag().toString();
                        for (String cellId : rout.getBlackRout()) {
                            //start counting from this id
                            if (cellId.equals( blackStoneCurrentCell )) startFromHere = rout.getBlackRout().indexOf( cellId );

                        }

                    }
                    //add the dice value to the current place of
                    int nextRoutIndex = startFromHere + diceValue;
                    if (nextRoutIndex < routSize) {
                        String nextCell = rout.getBlackRout().get( nextRoutIndex );
                        int nextCellIDName = getResources().getIdentifier( nextCell, "id", getPackageName() );
                        ImageView nextCellImage = findViewById( nextCellIDName );
                        if (nextCellImage.getTag() == null)//it is free
                        {
                            movesNumber++;//one available move
                        } else if ((nextCellImage.getTag() != null && nextCellImage.getTag().toString().charAt( 0 ) == 'w') && !CheckSpeciLCell( nextCell ))//if the cell has enemy stone and it is not protected
                        {
                            movesNumber++;//you got another move
                        }
                    }
                    if(nextRoutIndex == routSize)
                    {
                        movesNumber++;
                    }
                }
            }
        }
        else
            {
                //check each stone position
                for (int i = 0; i < STONESNUM; i++) {
                    String whiteImageIdUrl = "white_" + i;

                    int whiteImageId = getResources().getIdentifier( whiteImageIdUrl, "id", getPackageName() );
                    ImageView whiteStone = findViewById( whiteImageId );
                    if (whiteStone.getVisibility() == View.VISIBLE) {  //if the stone is not finished
                        if (whiteStone.getTag() == null) {
                            startFromHere = -1;
                        } else//if the stone is on the board
                        {
                            String whiteStoneCurrentCell = whiteStone.getTag().toString();
                            for (String cellId : rout.getWhiteRout()) {
                                //start counting from this id
                                if (cellId.equals( whiteStoneCurrentCell )) startFromHere = rout.getWhiteRout().indexOf( cellId );

                            }

                        }

                        //add the dice value to the current place of
                        int nextRoutIndex = startFromHere + diceValue;
                        if (nextRoutIndex < routSize) {
                            String nextCell = rout.getWhiteRout().get( nextRoutIndex );
                            int nextCellIDName = getResources().getIdentifier( nextCell, "id", getPackageName() );
                            ImageView nextCellImage = findViewById( nextCellIDName );
                            if (nextCellImage.getTag() == null)//it is free
                            {
                                movesNumber++;//one available move
                            } else if ((nextCellImage.getTag() != null && nextCellImage.getTag().toString().charAt( 0 ) == 'b') && !CheckSpeciLCell( nextCell ))//if the cell has enemy stone and it is not protected
                            {
                                movesNumber++;//you got another move
                            }
                        }
                        if(nextRoutIndex == routSize)
                        {
                            movesNumber++;
                        }
                    }
                }
            }
            return movesNumber;
    }
}
