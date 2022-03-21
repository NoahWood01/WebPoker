package uta.cse3310;

import java.util.ArrayList;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uta.cse3310.UserEvent.UserEventType;

public class Game {

    ArrayList<Player> players = new ArrayList<>();
    int turn; // player ID that has the current turn

    // stored cards not in players hands
    public ArrayList<Card> deck = new ArrayList<>();

    // total chips betted
    int pot;

    String exportStateAsJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    public void processMessage(String msg) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        // take the string we just received, and turn it into a user event
        UserEvent event = gson.fromJson(msg, UserEvent.class);

        if (event.event == UserEventType.NAME) {
            players.get(event.playerID).SetName(event.name);
        }

    }

    public Game() {
        System.out.println("creating a Game Object");
        create_deck(deck);
        shuffle_deck(deck);
    }

    // start and play entire game in this method
    // will be called from the Game Object

    // right now set up with just command line
    // this will need to be multi threaded
    // most likely a player per thread
    // returns the winner
    public void play_game()
    {
        // deal cards for each player
        // and remove the from the deck
        // to prevent duplicates
        // cards will be added back to deck when
        // shuffling or drawing
        for(Player p: players)
        {
            for(int i = 0; i < p.Cards.length; i++)
            {
                p.Cards[i] = deck.get(0);
                deck.remove(0);
            }
        }
        // 1st betting round

        // Draw round

        // 2nd betting round

        // showdown
    }

    // gets a random card from the passed in deck
    private Card get_random_card(ArrayList<Card> deck)
    {
        Card card = deck.get(0);
        deck.remove(0);
        return card;
    }

    // remove all player's Cards
    // and randomize the order in the shuffle_deck
    // will be 52 cards in size
    private void shuffle_deck(ArrayList<Card> deck)
    {
        // remove player's cards for cases
        // when game is replayed
        for(Player p: players)
        {
            for(int i = 0; i < p.Cards.length; i++)
            {
                // add card back to deck
                deck.add(p.Cards[i]);
                // remove card from hand
                //p.Cards[i] = NULL;
            }
        }

        // shuffle current deck
        Collections.shuffle(deck);
        if(deck.size() != 52)
        {
            System.out.println("Error in deck shuffle, not 52 cards in deck.");
        }

        // print cards
        System.out.println();
        for(Card card: deck)
        {
            System.out.println(card.suite.toString() +" "+card.value.toString());
        }

    }

    // this adds all 52 cards to the deck in order
    // will need to shuffle in game
    private void create_deck(ArrayList<Card> deck)
    {
        // for each suite and each value add the card
        for(Card.Suite suite: Card.Suite.values())
        {
            for(Card.Value value: Card.Value.values())
            {
                Card card = new Card();
                card.suite = suite;
                card.value = value;
                deck.add(card);
                // print all cards for error checking
                //System.out.println(card.suite.toString() +" "+card.value.toString());
            }
        }
    }


}
