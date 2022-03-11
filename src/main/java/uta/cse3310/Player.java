package uta.cse3310;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Player {
    int Id;
    String Name;
    uta.cse3310.Card Cards[];

    public Player(int id) {
        Id = id;
        Name = "not set";
        // there is a lot smarter way to do this,
        // but at least this is obvious
        Cards = new Card[5];
        for (int i = 0; i < 5; i++) {
            Cards[i] = new Card();
            Cards[i].suite = Card.Suite.valueOf("SPADES");
            Cards[i].value = Card.Value.valueOf("FIVE");
        }

    }

    public String asJSONString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
