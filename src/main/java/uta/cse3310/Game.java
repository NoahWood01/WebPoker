package uta.cse3310;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Game {
    //Player players[];
    ArrayList<Player> players = new ArrayList<>();
    int turn; // player ID that has the current turn
    String exportStateAsJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void addPlayer(Player p) {
        System.out.println("player is " + p);
        players.add(p);
    }

    public void processMessage(String msg) {

    }
    public Game(){
        System.out.println("creating a Game Object");
    }

}
