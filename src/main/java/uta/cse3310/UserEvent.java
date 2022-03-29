package uta.cse3310;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserEvent {

    public enum UserEventType {
        NAME, STAND, DRAW, BET, FOLD, ANTE;

        private UserEventType(){}
    };

    UserEventType event;
    // play_game assumes playerID will start at 0
    int playerID;
    String name;

    int amount_to_bet;
    // exception will be thrown if
    // amount_to_draw and the same number
    // of indexes are given
    int amount_to_draw;
    // 3 represents the max amount of cards to
    // trade in
    int give_card_indexes[];

    public UserEvent() {
    }

}
