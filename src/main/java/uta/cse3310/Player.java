package uta.cse3310;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
 
import java.util.ArrayList;

public class Player {
    public Player(int id) {
        this.Id = id;
        this.Name = "not set";
    }

    /*************************************
    
                    Setters

    *************************************/

    public void setId(int id){ this.Id = id; }
    public void setName(String N) {
        Name = N;
        LastMessageToPlayer="Welcome " + N + " to the game.";
    }

    public void add_card(Card card){ this.hand.add(card); }

    /*************************************
    
                    Getters

    *************************************/

    public int get_id(){ return this.Id; }
    public String get_name(){ return this.Name; }
    public Card get_card(int i){ return this.hand.get(i); }

    /*************************************
    
                Other methods

    *************************************/

    public String asJSONString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    // This is redundant but I didnt feel like removing array list logic
    public void set_cards(){
        for(int i = 0; i < 5; i++) Cards[i] = hand.get(i);
    }

    /*************************************
                                        
                    Attributes

    *************************************/

    private int Id;
    private String Name;
    ArrayList<Card> hand = new ArrayList<>();
    uta.cse3310.Card Cards[] = new uta.cse3310.Card[5];
    private String LastMessageToPlayer;
    private boolean folded = false; // boolean to check if the player has folded in the round
    private int totalBet;   // amount player has bet in the game
    // used to make sure all players not
    // folded bet the same amount
    private int currentBet; // amount player has bet this round


}
