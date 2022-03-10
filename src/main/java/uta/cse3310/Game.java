package uta.cse3310;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Game {
    Player players[];
    int turn; // player ID that has the current turn
    String exportStateAsJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void addPlayer(Player p) {
        System.out.println("player is " + p);
        players[p.Id] = p; // dangerous use of an array index from an untrusted source
    }

    public void processMessage(String msg) {

    }

}
