package uta.cse3310;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Player {
    int Id;
    String Name;
    uta.cse3310.Card Cards[];
    String LastMessageToPlayer;
    // amount player has bet in the game
    int totalBet;
    // amount player has bet this round
    // used to make sure all players not
    // folded bet the same amount
    int currentBet;
    // boolean to check if the player has folded in the round
    boolean folded = false;

    public Player(int id) {
        Id = id;
        Name = "not set";
        // there is a lot smarter ways to do this,
        // but at least this is obvious
        Cards = new Card[5];
        for (int i = 0; i < 5; i++) {
            Cards[i] = new Card();
            Cards[i].suite = Card.Suite.valueOf("SPADES");
            Cards[i].value = Card.Value.valueOf("FIVE");
        }

    }

    public void SetName(String N) {
        Name = N;
        LastMessageToPlayer="Welcome " + N + " to the game.";
    }

    public String asJSONString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
