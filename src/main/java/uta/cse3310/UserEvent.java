package uta.cse3310;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserEvent {

    public enum UserEventType {
        NAME, STAND, HIT, CALL
    };

    String exportAsJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public UserEvent() {
    }

}
