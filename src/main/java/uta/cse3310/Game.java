package uta.cse3310;

import java.util.ArrayList;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uta.cse3310.UserEvent.UserEventType;

public class Game {
    public Game() {
        System.out.println("creating a Game Object");
        create_deck();
        shuffle_deck();
        pot = new Pot();
    }

    /**************************************
     *
     * Players
     *
     **************************************/

    public void addPlayer(Player p){ players.add(p); }

    public void removePlayer(int playerid) {
        // given it's player number, this method
        // deletes the player from the players array
        // and does whatever else is needed to remove
        // the player from the game.
        players.remove(playerid - 1);
    }

    // Technically the player will have alreay been created but this method simply
    // sets the player name
    // As well as gives the playe a hand of cards
    public void create_player(int playerId, UserEvent event) {
        players.get(playerId).setName(event.name);

        for (int i = 0; i < 5; i++) players.get(playerId).add_card(draw_card());

        place_ante(playerId);

        players.get(playerId).set_cards();
    }

    public Player getStartingPlayer() {
        startingplayer = players.get(0);
        currentplayer = startingplayer;
        return startingplayer;
    }

    public void nextPlayer() // swap players
    {
        if (currentplayer == startingplayer || players.indexOf(currentplayer) < players.size()) {
            currentplayer = players.get(players.indexOf(currentplayer) + 1); // next player
        } else // go back to starting player / next round
        {
            currentplayer = startingplayer;
        }
    }

    public void player_stand(int id){}
    public void player_fold(int id){
        empty_hand(players.get(id));
    }


    // determine tthe winner between two players
    // print to console the winner.
    // only checks between first two players
    // will need to expand for more players
    public void determineWinner()
    {
        Hand h0 = new Hand(players.get(0).Cards);
        Hand h1 = new Hand(players.get(1).Cards);
        if(h0.is_equal(h1) == true)
        {
            System.out.println("TIE");
        }
        else if(h0.is_better_than(h1))
        {
            System.out.println("player 0 wins");
        }
        else
        {
            System.out.println("Player 1 wins");
        }
    }

    /**************************************
     *
     * Game Logic
     *
     **************************************/

    public String exportStateAsJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void processMessage(int playerId, String msg) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        // take the string we just received, and turn it into a user event
        UserEvent event = gson.fromJson(msg, UserEvent.class);

        System.out.println("\n\n" + msg + "\n\n");

        if(event.event == UserEventType.NAME)  create_player(playerId, event);
        if(event.event == UserEventType.STAND) player_stand(playerId);
        if(event.event == UserEventType.FOLD)  player_fold(playerId);
        if(event.event == UserEventType.BET)   place_bet(playerId, event);
        if(event.event == UserEventType.DRAW)  new_cards(playerId, event);
    }

    /*
     * this method is called on a periodic basis (once a second) by a timer
     * it is to allow time based situations to be handled in the game
     * if the game state is changed, it returns a true.
     *
     * expecting that returning a true will trigger a send of the game
     * state to everyone
     */
    public boolean update() {
        return false;
    }

    /**************************************
     *
     * Deck Builders
     *
     **************************************/

    public void new_cards(int playerId, UserEvent event) {
        int indexes[] = event.give_card_indexes;
        for (int i = 0; i < indexes.length; i++) {
            if (indexes[i] > 0) { // 0 is default stating the card shouldnt change
                players.get(playerId).hand.set(indexes[i] - 1, draw_card()); // Remove the card at the specified index
            }
        }

        players.get(playerId).set_cards();
    }

    // gets a card from the front of passed in deck
    public Card draw_card() {
        Card card = deck.get(0);
        deck.remove(0);
        return card;
    }

    // remove all player's Cards
    // and randomize the order in the shuffle_deck
    // will be 52 cards in size

    public void empty_hand(Player p){
        p.hand.clear();
        p.Cards = new uta.cse3310.Card[5];
    }

    public void shuffle_deck() {
        // shuffle current deck
        try {
            Collections.shuffle(deck);
            if (deck.size() != 52)
                throw new Exception("Error in deck shuffle, not 52 cards in deck."); // Error handling for an incorrect
                                                                                     // deck
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // this adds all 52 cards to the deck in order
    // will need to shuffle in game
    public void create_deck() {
        // for each suite and each value add the card
        for (Card.Suite suite : Card.Suite.values()) {
            for (Card.Value value : Card.Value.values()) {
                Card card = new Card();
                card.suite = suite;
                card.value = value;
                deck.add(card);
                // print all cards for error checking
                // System.out.println(card.suite.toString() + " " + card.value.toString());
            }
        }
    }

    /**********************************
     *
     * Betting
     *
     **********************************/

    public void place_ante(int playerId){
        players.get(playerId).subtract_wallet(20);
        pot.add_to_pot(20);
    }

    public void place_bet(int playerId, UserEvent event){
        players.get(playerId).subtract_wallet(event.amount_to_bet);
        players.get(playerId).set_current_bet(event.amount_to_bet);
        pot.add_to_pot(event.amount_to_bet);
    }

    /**********************************

                Attributes

    **********************************/

    private ArrayList<Player> players = new ArrayList<>(); // players of the game
    private ArrayList<Card> deck = new ArrayList<>(); // stored cards not in players hands

    // turns
    Player startingplayer;
    Player currentplayer;

    private int turn; // player ID that has the current turn
    private Pot pot; // total of chips being bet
}
