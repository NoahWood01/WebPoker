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

    public Player(){
        this.name = "not set";
        this.wallet = 100;
    }

    /*************************************

                    Setters

    *************************************/

    public void setId(int id){ this.id = id; }
    public void set_current_bet(int bet){ this.currentBet += bet; }
    public void set_name(String name){ this.name = name; }
    // This is redundant but I didnt feel like removing array list logic
    public void set_cards(){
        for(int i = 0; i < 5; i++) Cards[i] = hand.get(i);
    }
    public void set_stand(){ this.stand = true; }
    public void set_ready(){ this.ready = true; }
    public void set_hasBet(boolean has_bet){ hasBet = true; }
    public void set_hasRaised(boolean has_raised){ hasRaised = true; }

    public void add_card(Card card){ this.hand.add(card); }
    public void add_wallet(int bet){ this.wallet += bet; }
    public void set_wallet(int amount){ wallet = amount; }
    public void subtract_wallet(int bet){ this.wallet -= bet; }

    /*************************************

                    Getters

    *************************************/

    public int get_id(){ return this.id; }
    public String get_name(){ return this.name; }
    public Card get_card(int i){ return this.hand.get(i); }
    public boolean get_stand(){ return this.stand; }
    public boolean get_fold(){ return this.folded; }
    public int get_bet(){ return this.currentBet; }
    public boolean get_ready(){ return this.ready; }
    public boolean get_hasBet(){ return hasBet; }
    public boolean get_hasRaised(){ return hasRaised; }
    public int get_wallet(){ return wallet; }

    /*************************************

                Other methods

    *************************************/

    public String asJSONString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /*************************************

                    Attributes

    *************************************/

    public int id;                                         // Identifiication so the game knows who the player is
    public int wallet;                                     // Players starting dollar amount
    public int currentBet;                                 // amount player has bet this round
    private int totalBet;                                   // amount player has bet in the game

    private String name;                                    // Player name

    public Hand pHand;

    ArrayList<Card> hand = new ArrayList<>();               // Array list for players hand to be passed to cards
    uta.cse3310.Card Cards[] = new uta.cse3310.Card[5];

    public boolean folded = false;                         // boolean to check if the player has folded in the round
    private boolean stand = false;
    public boolean ready = false;
    public boolean hasBet = false;
    public boolean hasRaised = false;
}
