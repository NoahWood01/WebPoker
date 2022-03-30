package uta.cse3310;

import java.util.*;

public class Pot{

    public Pot(){}

    public void add_to_pot(int amount){
        pot += amount;
    }

    public void empty_pot(){
        pot = 0;
    }

    public int reward_pot(){
        return pot;
    }

    private int pot;
}
