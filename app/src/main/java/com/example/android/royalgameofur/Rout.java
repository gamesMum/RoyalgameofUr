package com.example.android.royalgameofur;

import java.util.ArrayList;

public class Rout {
    private   ArrayList<String> whiteRout;
    private  ArrayList<String> blackRout;
    private  ArrayList<String> specialCells;

    Rout(){

        //store white rout in an array
        whiteRout = new ArrayList<>(  );
        blackRout = new ArrayList<>(  );
        specialCells = new ArrayList<>(  );
        whiteRout.add( "cell_3_2" );
        whiteRout.add( "cell_2_2" );
        whiteRout.add( "cell_1_2" );
        whiteRout.add( "cell_0_2" );
        whiteRout.add( "cell_0_1" );
        whiteRout.add( "cell_1_1" );
        whiteRout.add( "cell_2_1" );
        whiteRout.add( "cell_3_1" );
        whiteRout.add( "cell_4_1" );
        whiteRout.add( "cell_5_1" );
        whiteRout.add( "cell_6_1" );
        whiteRout.add( "cell_7_1" );
        whiteRout.add( "cell_7_2" );
        whiteRout.add( "cell_6_2" );

        //store black rout in an array

        blackRout.add( "cell_3_0" );
        blackRout.add( "cell_2_0" );
        blackRout.add( "cell_1_0" );
        blackRout.add( "cell_0_0" );
        blackRout.add( "cell_0_1" );
        blackRout.add( "cell_1_1" );
        blackRout.add( "cell_2_1" );
        blackRout.add( "cell_3_1" );
        blackRout.add( "cell_4_1" );
        blackRout.add( "cell_5_1" );
        blackRout.add( "cell_6_1" );
        blackRout.add( "cell_7_1" );
        blackRout.add( "cell_7_0" );
        blackRout.add( "cell_6_0" );

        //store special cells location
        specialCells.add( "cell_0_0" );
        specialCells.add( "cell_0_2" );
        specialCells.add( "cell_3_1" );
        specialCells.add( "cell_6_0" );
        specialCells.add( "cell_6_3" );
    }

    public  ArrayList<String> getBlackRout() {
        return blackRout;
    }

    public  ArrayList<String> getWhiteRout() {
        return whiteRout;
    }

    public  ArrayList<String> getSpecialCells() {
        return specialCells;
    }
}



