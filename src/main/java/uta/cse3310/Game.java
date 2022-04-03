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
        playerStandFold = 0;
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
    public void create_player(int playerId, UserEvent event)
    {
        players.get(playerId).setName(event.name);
    }

    public Player getStartingPlayer() {
        startingplayer = players.get(0);
        currentplayer = startingplayer;
        return startingplayer;
    }

    public void nextPlayer() // swap players
    {
        if (currentplayer == startingplayer || players.indexOf(currentplayer) < players.size())
        {
            currentplayer = players.get(players.indexOf(currentplayer) + 1); // next player
        }
        else // go back to starting player / next round
        {
            currentplayer = startingplayer;
        }
    }

    public void player_stand(Player p){ p.set_stand(); }
    public void player_fold(Player p){ empty_hand(p); }

    // determine tthe winner between two players
    // print to console the winner.
    // only checks between first two players
    // will need to expand for more players
    public void determine_winner(){
        //String winner = "";
        for(int i = 0; i < players.size(); i++){
            if(!players.get(i).get_fold())
            {
                Hand h = new Hand(players.get(i).Cards);
                hands.add(h);
            }

        }

        /*
        for(int i = 0; i < hands.size(); i++){
            if(hands.get(i).is_equal(hands.get(i+1)) == true)
            {
                System.out.println("TIE");
                // will add tie logic shortly
                winner = -1;
            }
            else if(hands.get(i).is_better_than(hands.get(i+1)))
            {
                System.out.println("player " + i + " wins");
                winner = i;
            }
            else
            {
                System.out.println("Player " + (i+1) + " wins");
                winner = i + 1;
            }
        }
        */

        // only compare when there is not 1 player
        if(nonFoldedPlayers.size() > 1)
        {
            if(hands.get(0).is_equal(hands.get(1)) == true)
            {
                System.out.println("TIE");
                // will add tie logic shortly
                winner = -1;
            }
            else if(hands.get(0).is_better_than(hands.get(1)))
            {
                System.out.println("player " + "0" + " wins");
                winner = 0;
            }
            else
            {
                System.out.println("Player " + "1" + " wins");
                winner = 1;
            }
        }

        // update winner wallet
        winnings = pot.reward_pot();
        if(winner != -1)
        {
            players.get(winner).add_wallet(pot.reward_pot());
            pot.empty_pot();
        }
        else
        {
            players.get(0).add_wallet(pot.reward_pot()/2);
            players.get(1).add_wallet(pot.reward_pot()/2);
            pot.empty_pot();
        }


        // After we determine the winner we need to
        // Save the winner
        // Save the hand
        // Clear hands
        // Broadcast Winner and the winning hand
        // Update winner wallet
        // add cards back to deck (call create_deck())
        // shuffle (call shuffle_deck())
        // give players new cards starting essentially a new game
    }

    /**************************************
     *
     * Game Logic
     *
     **************************************/

    public String exportStateAsJSON(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void processMessage(int playerId, String msg){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        // take the string we just received, and turn it into a user event
        UserEvent event = gson.fromJson(msg, UserEvent.class);

        System.out.println("\n\n" + msg + "\n\n");

        // any player can sort at any time
        if(event.event == UserEventType.SORT)  sort_cards(event.playerID, event);
        if(event.event == UserEventType.READY)
        {
            players.get(event.playerID).set_ready();
        }

        if(nonFoldedPlayers.size() == 1 && phase != 0)
        {
            // if all other players fold
            // last one standing wins
            winner = nonFoldedPlayers.get(0).get_id();
            determine_winner();
            phase = 5;
            nonFoldedPlayers.clear();
            turn = -1;
            set_players_notReady();
        }

        if(phase == 0)
        {
            // Pre Game Phase

            if(event.event == UserEventType.NAME)
            {
                create_player(event.playerID, event);
            }
            if(players.size() >= 2  && all_players_ready())
            {
                for(int i = 0; i < players.size(); i++)
                {
                    for(int j = 0; j < 5; j++)
                    {
                        players.get(i).add_card(draw_card());
                    }
                    place_ante(i);
                    players.get(i).set_cards();
                    nonFoldedPlayers.add(players.get(i));
                }
                getStartingPlayer();
                phase = 1;
                turn = 0;
            }
        }
        else if(phase == 1)
        {
            // First Bet Phase

            // only do something if its the correct players turn
            if(event.playerID == turn)
            {
                boolean moveOn = false;
                if(event.event == UserEventType.BET)
                {
                    place_bet(event.playerID, event);
                    moveOn = true;
                }
                else if(event.event == UserEventType.STAND)
                {
                    player_stand(players.get(event.playerID));
                    moveOn = true;
                }
                else if(event.event == UserEventType.FOLD)
                {
                    player_fold(players.get(event.playerID));
                    nonFoldedPlayers.remove(event.playerID);
                    moveOn = true;
                }

                if(moveOn == true && all_bets_equal() && nonFoldedPlayers.size() >= 1)
                {
                    //nextPlayer();
                    turn++;
                    // every player made a single turn
                    if(turn >= players.size())
                    {
                        turn = 0;
                        phase++;
                    }
                }
                else if(moveOn == true)
                {
                    turn = next_player_bet();
                }
            }
        }
        else if(phase == 2)
        {
            // Draw Phase

            if(event.playerID == turn)
            {
                boolean moveOn = false;
                if(event.event == UserEventType.DRAW)
                {
                    new_cards(event.playerID, event);
                    moveOn = true;
                }
                else if(event.event == UserEventType.STAND)
                {
                    player_stand(players.get(event.playerID));
                    moveOn = true;
                }
                else if(event.event == UserEventType.FOLD)
                {
                    player_fold(players.get(event.playerID));
                    nonFoldedPlayers.remove(event.playerID);
                    moveOn = true;
                }

                if(moveOn == true)
                {
                    //nextPlayer();
                    turn++;
                    // every player made a single turn
                    if(turn >= players.size())
                    {
                        turn = 0;
                        phase++;
                    }
                }
            }

        }
        else if(phase == 3)
        {
            // Second Bet Phase

            // only do something if its the correct players turn
            if(event.playerID == turn)
            {
                boolean moveOn = false;
                if(event.event == UserEventType.BET)
                {
                    place_bet(event.playerID, event);
                    moveOn = true;
                }
                else if(event.event == UserEventType.STAND)
                {
                    player_stand(players.get(event.playerID));
                    moveOn = true;
                }
                else if(event.event == UserEventType.FOLD)
                {
                    player_fold(players.get(event.playerID));
                    nonFoldedPlayers.remove(event.playerID);
                    moveOn = true;
                }

                if(moveOn == true && all_bets_equal() && nonFoldedPlayers.size() >= 1)
                {
                    //nextPlayer();
                    turn++;
                    // every player made a single turn
                    if(turn >= players.size())
                    {
                        turn = 0;
                        phase = 5;
                        determine_winner();
                        turn = -1;
                        set_players_notReady();
                    }
                }
                else if(moveOn == true)
                {
                    turn = next_player_bet();
                }
            }
        }
        else if(phase == 4)
        {
            // Showdown Phase
            phase++;
        }

        else if(phase == 5)
        {
            // reset phase
            nonFoldedPlayers.clear();
            for(int i = 0; i < players.size(); i++)
            {
                players.get(i).folded = false;
                empty_hand(players.get(i));
            }
            shuffle_deck();
            winnings = 0;
            phase = 0;
        }

        if(nonFoldedPlayers.size() == 1 && phase != 0)
        {
            // if all other players fold
            // last one standing wins
            winner = nonFoldedPlayers.get(0).get_id();
            determine_winner();
            phase = 5;
            nonFoldedPlayers.clear();
            turn = -1;
            set_players_notReady();
        }

    }


    /*
     * this method is called on a periodic basis (once a second) by a timer
     * it is to allow time based situations to be handled in the game
     * if the game state is changed, it returns a true.
     *
     * expecting that returning a true will trigger a send of the game
     * state to everyone
     */
    public boolean update()
    {

        return false;
    }

    public boolean stand_fold_check(){
        int count = 0;

        for(int i = 0; i < players.size(); i++)
            if(players.get(i).get_stand()) count++;           // if stand is true increment count

        if(count == players.size()) return true;

        return false;
    }

    public boolean all_bets_equal()
    {
        int tempBet = nonFoldedPlayers.get(0).get_bet();
        for(int i = 1; i < nonFoldedPlayers.size(); i++)
        {
            if(tempBet != nonFoldedPlayers.get(i).get_bet())
            {
                return false;
            }
            tempBet = nonFoldedPlayers.get(i).get_bet();
        }
        return true;
    }
    public int next_player_bet()
    {
        Player temp = nonFoldedPlayers.get(0);
        for(int i = 1; i < nonFoldedPlayers.size(); i++)
        {
            if(temp.get_bet() < nonFoldedPlayers.get(i).get_bet())
            {
                return temp.get_id();
            }
            else if(temp.get_bet() > nonFoldedPlayers.get(i).get_bet())
            {
                return nonFoldedPlayers.get(i).get_id();
            }
            temp = nonFoldedPlayers.get(i);
        }
        return 0;
    }

    public boolean all_players_ready()
    {
        for(int i = 0; i < players.size(); i++)
        {
            if(players.get(i).get_ready() == false)
            {
                return false;
            }
        }
        return true;
    }

    public void set_players_notReady()
    {
        for(int i = 0; i < players.size(); i++)
        {
            players.get(i).ready = false;
            players.get(i).currentBet = 0;
        }
        return;
    }

    /**************************************
     *
     * Deck Builders
     *
     **************************************/

    public void sort_cards(int playerId, UserEvent event){
        Hand.sortHand(players.get(playerId).Cards);     // This actually sorts the cards
        // This sorts the ArrayList hand so if Draw is clicked the correct order is sent back to the client
        for(int i = 0; i < 5; i++) players.get(playerId).hand.set(i, players.get(playerId).Cards[i]);
    }

    public void new_cards(int playerId, UserEvent event){
        int indexes[] = event.give_card_indexes;
        for (int i = 0; i < indexes.length; i++){
            if (indexes[i] > 0){ // 0 is default stating the card shouldnt change
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

    public void empty_hand(Player p)
    {
        for(int i = 0; i < p.hand.size(); i++)
        {
            deck.add(p.hand.get(i));
        }
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
    private ArrayList<Hand> hands = new ArrayList<>();

    // store non folded players seperately
    private ArrayList<Player> nonFoldedPlayers = new ArrayList<>();

    // turns
    Player startingplayer;
    Player currentplayer;


    // round - these are used with javascript to determine certain displays
    public int phase = 0;
    int turn = -1;
    // do not change these or display will break
    int winner = -1;
    int winnings;
    // 0 will be pregame
    // 1 will be first bet phase
    // 2 wil be draw phase
    // 3 will be second bet phase
    // 4 will be showdown
    // 5 is winner screen

    // Count for number of players who have stand/fold
    // This is necessary to determine when showdown begins
    private int playerStandFold;
    private Pot pot; // total of chips being bet
}
