package uta.cse3310;

import java.util.*;

public class Pot{

    public Pot(){}

    /***********************************
                    Getters
    ***********************************/

    public int get_pot(){ return this.pot; }

    /***********************************
                    Setters
    ***********************************/

    public void add_to_pot(int amount)  { pot += amount; }
    public void empty_pot()             { pot = 0; }
    public int reward_pot()             { return pot; }

    /***********************************
                    Attributes
    ***********************************/

    private int pot;
}
