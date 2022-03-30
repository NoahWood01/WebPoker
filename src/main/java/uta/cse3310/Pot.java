package uta.cse3310;

import java.util.*;

public class Pot{

    public Pot(){}

    public void addToPot(int amount){
        prize += amount;
    }

    public void emptyPot(){
        prize = 0;
    }

    public int rewardPot(){
        return prize;
    }

    private int prize;
}
