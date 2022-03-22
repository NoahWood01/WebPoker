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

    public String exportStateAsJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    public void removePlayer(int playerid) {
        // given it's player number, this method
        // deletes the player from the players array
        // and does whatever else is needed to remove
        // the player from the game.
        players.remove(playerid - 1);
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

    public boolean update() {


        // this function is called on a periodic basis (once a second) by a timer
        // it is to allow time based situations to be handled in the game
        // if the game state is changed, it returns a true.
        return false;
        // expecting that returning a true will trigger a send of the game
        // state to everyone

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
    // where each thread get the UserEvents
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
        // note: this is not tested

        // max of 3 cards exchanged
        // VARIABLE IN UserEvent TO KEEP TRACK
        // this starts with player zero in the list
        for( turn = 0; turn < players.size(); turn++)
        {
            // only legal actions here are DRAW or STAND
            // get user event and check if it was from the player.
            UserEvent event = new UserEvent();
            while(true)
            {
                // get the event HERE
                // add code to get it
                if(event.playerID == turn && (event.event != UserEventType.DRAW
                    || event.event != UserEventType.STAND))
                {
                    if(event.event == UserEventType.STAND)
                    {
                        // this will end turn and proceed to next player
                        break;
                    }
                    else
                    {
                        // iterate for the amount to draw specified
                        // from the event and based on the indeces
                        // that is in the event
                        for(int i = 0; i < event.amount_to_draw; i++)
                        {
                            try
                            {
                                // add current card at given index to end of deck
                                deck.add(players.get(turn).Cards[event.give_card_indexes[i]]);
                                players.get(turn).Cards[event.give_card_indexes[i]] = get_card(deck);
                            }
                            catch (ArrayIndexOutOfBoundsException e)
                            {
                                // debug
                                System.out.println("DRAW Card error: -1 in index");
                            }
                        }
                        // this will end turn and proceed to next player
                        break;
                    }
                }
            }
        }


        // 2nd betting round


        // showdown



    }

    // gets a card from the front of passed in deck
    private Card get_card(ArrayList<Card> deck)
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
        // debug
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
