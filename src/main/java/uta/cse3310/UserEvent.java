package uta.cse3310;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserEvent {

    public enum UserEventType {
        READY, NAME, STAND, DRAW, BET, CALL, FOLD, SORT;

        private UserEventType(){}
    };

    UserEventType event;
    
    int give_card_indexes[];
    int playerID;       // play_game assumes playerID will start at 0
    int amount_to_bet;
    int amount_to_draw;

    Player player;
    String name;

    // exception will be thrown if
    // amount_to_draw and the same number
    // of indexes are given
    
    // 3 represents the max amount of cards to
    // trade in
    public UserEvent(){}
}
