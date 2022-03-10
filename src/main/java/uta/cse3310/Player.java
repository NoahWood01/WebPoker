package uta.cse3310;

import com.google.gson.Gson; 
import com.google.gson.GsonBuilder; 

public class Player {
    int Id;
    String Name;

    public Player(int id) {
        Id = id;
        Name = "not set";
    }

    public String asJSONString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
