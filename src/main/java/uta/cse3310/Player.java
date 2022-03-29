package uta.cse3310;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class Player {
    public Player(int id) {
        this.id = id;
        this.name = "not set";
        this.wallet = 100;
    }

    /*************************************

                    Setters

    *************************************/

    public void setId(int id){ this.id = id; }
    public void setName(String name){
        this.name = name;
        LastMessageToPlayer="Welcome " + name + " to the game.";
    }

    public void add_card(Card card){ this.hand.add(card); }

    /*************************************

                    Getters

    *************************************/

    public int get_id(){ return this.id; }
    public String get_name(){ return this.name; }
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

    private int id;                                         // Identifiication so the game knows who the player is
    int wallet;                                             // Players starting dollar amount
    private int currentBet;                                 // amount player has bet this round
    private int totalBet;                                   // amount player has bet in the game

    private String name;                                    // Player name

    ArrayList<Card> hand = new ArrayList<>();               // Array list for players hand to be passed to cards
    uta.cse3310.Card Cards[] = new uta.cse3310.Card[5];

    private String LastMessageToPlayer;
    private boolean folded = false;                         // boolean to check if the player has folded in the round
}
