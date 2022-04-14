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
        phase = 0;
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
        Player workingPlayer = get_player(playerid);
        players.remove(workingPlayer);
    }

    // Technically the player will have alreay been created but this method simply
    // sets the player name
    // As well as gives the playe a hand of cards
    public void create_player(int playerId, UserEvent event)
    {
        Player workingPlayer = get_player(playerId);
        workingPlayer.set_name(event.name);
    }

    public Player getStartingPlayer() {
        startingplayer = nonFoldedPlayers.get(0);
        currentplayer = startingplayer;
        return startingplayer;
    }

    public void nextPlayer() // swap players
    {
        if (currentplayer == startingplayer || nonFoldedPlayers.indexOf(currentplayer) < nonFoldedPlayers.size()-1)
        {
            currentplayer = nonFoldedPlayers.get(nonFoldedPlayers.indexOf(currentplayer) + 1); // next player
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
    public void determine_winner()
    {
        //String winner = "";
        //winningPlayer = Hand.whoWins(nonFoldedPlayers);
        //winner = winningPlayer.get_id();



        // SETTING TO DEFAULT PLAYER IN ARRAY BC whoWinds doesnt work
        // so other code can be added

        winningPlayer = nonFoldedPlayers.get(0);


        /*
        for(int i = 0; i < players.size(); i++){
            if(!players.get(i).get_fold())
            {
                Hand h = new Hand(players.get(i).Cards);
                hands.add(h);
            }

        }


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
*/
        // update winner wallet
        winnings = pot.reward_pot();
        if(winner != -1)
        {
            Player workingPlayer = get_player(winner);
            workingPlayer.add_wallet(pot.reward_pot());
            pot.empty_pot();
        }
        else
        {
            nonFoldedPlayers.get(0).add_wallet(pot.reward_pot()/2);
            nonFoldedPlayers.get(1).add_wallet(pot.reward_pot()/2);
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

    /***************************************
                player_queue
    ***************************************/

    public void add_player_queue(Player p)  { player_queue.add(p); }
    public void remove_player_queue(int id) { player_queue.remove(id); }
    public Player get_player_queue()        { return player_queue.get(0); }
    public int get_player_queue_size()      { return player_queue.size(); }

    /**************************************
     *
     * Game Logic
     *
     **************************************/

    public String exportStateAsJSON(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void phase_00(UserEvent event){
        // Pre Game Phase

        if(event.event == UserEventType.NAME && event.playerID >= 5){
            // If there's currently more than 5 players add player 5's name into matching id in player_queue
            for(int i = 0; i < player_queue.size(); i++){
                if(player_queue.get(i).get_id() == event.playerID) player_queue.get(i).set_name(event.name);
            }
        }
        else if(event.event == UserEventType.NAME)
        {
            create_player(event.playerID, event);
        }
        if(players.size() >= 2  && all_players_ready())
        {
            for(Player p : players)
            {
                for(int j = 0; j < 5; j++)
                {
                    p.add_card(draw_card());
                }
                place_ante(p.id);
                p.set_cards();
                nonFoldedPlayers.add(p);
            }
            currentplayer = getStartingPlayer();
            phase = 1;
            turn = 0;
            timeRemaining = 30;
        }
        else if(players.size() >= 2 && num_players_ready() >= 2)
        {
            timeRemaining = 10;
        }

        determine_player_message();
    }

    public void phase_01(UserEvent event){
        // First Bet Phase
            System.out.println("\n\n" + "Entering Phase 01" + "\n\n");
        // only do something if its the correct players turn
        if(event.player.id == currentplayer.get_id())
        {
            boolean moveOn = false;
            if(event.event == UserEventType.BET)
            {
                place_bet(event.playerID, event);
                moveOn = true;
            }
            else if(event.event == UserEventType.STAND)
            {
                player_stand(currentplayer);
                moveOn = true;
            }
            else if(event.event == UserEventType.FOLD)
            {
                player_fold(currentplayer);
                nonFoldedPlayers.remove(currentplayer);
                moveOn = true;
            }
            if(moveOn == true && all_bets_equal() && nonFoldedPlayers.size() >= 1)
            {
                nextPlayer();
                turn++;
                // every player made a single turn
                if(turn >= players.size())
                {
                    turn = 0;
                    currentplayer = getStartingPlayer();
                    phase++;
                }
                timeRemaining = 30;
            }
            else if(moveOn == true)
            {
                currentplayer = next_player_bet_player();
                turn = next_player_bet();
                timeRemaining = 30;
            }
        }

        determine_player_message();
    }
    public void phase_02(UserEvent event){
        // Draw Phase

        if(event.player.id == currentplayer.get_id())
        {
            boolean moveOn = false;
            if(event.event == UserEventType.DRAW)
            {
                new_cards(event.playerID, event);
                moveOn = true;
            }
            else if(event.event == UserEventType.STAND)
            {
                player_stand(currentplayer);
                moveOn = true;
            }
            else if(event.event == UserEventType.FOLD)
            {
                player_fold(currentplayer);
                nonFoldedPlayers.remove(currentplayer);
                moveOn = true;
            }
            if(moveOn == true)
            {
                nextPlayer();
                turn++;
                // every player made a single turn
                if(turn >= players.size())
                {
                    turn = 0;
                    currentplayer = getStartingPlayer();
                    phase++;
                }
                timeRemaining = 30;
            }
        }

        determine_player_message();
    }
    public void phase_03(UserEvent event){
        // Second Bet Phase

        // only do something if its the correct players turn
        if(event.player.id == currentplayer.get_id())
        {
            boolean moveOn = false;
            if(event.event == UserEventType.BET)
            {
                place_bet(event.playerID, event);
                moveOn = true;
            }
            else if(event.event == UserEventType.STAND)
            {
                player_stand(currentplayer);
                moveOn = true;
            }
            else if(event.event == UserEventType.FOLD)
            {
                player_fold(currentplayer);
                nonFoldedPlayers.remove(currentplayer);
                moveOn = true;
            }
            if(moveOn == true && all_bets_equal() && nonFoldedPlayers.size() >= 1)
            {
                nextPlayer();
                turn++;
                // every player made a single turn
                if(turn >= players.size())
                {
                    currentplayer = getStartingPlayer();
                    turn = 0;
                    phase = 5;
                    determine_winner();
                    turn = -1;
                    set_players_notReady();
                    timeRemaining = -1;
                }
                if(phase != 5)
                {
                    timeRemaining = 30;
                }
            }
            else if(moveOn == true)
            {
                currentplayer = next_player_bet_player();
                turn = next_player_bet();
                timeRemaining = 30;
            }
        }

        determine_player_message();
    }
    public void phase_04(UserEvent event){
        // Showdown Phase
        phase++;
    }
    public void phase_05(UserEvent event){
        // reset phase
        timeRemaining = -1;
        nonFoldedPlayers.clear();
        for(int i = 0; i < players.size(); i++)
        {
            players.get(i).folded = false;
            empty_hand(players.get(i));
        }
        shuffle_deck();
        winnings = 0;
        phase = 0;

        determine_player_message();
    }

    public void processMessage(int playerId, String msg){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        // take the string we just received, and turn it into a user event
        UserEvent event = gson.fromJson(msg, UserEvent.class);

        System.out.println("\n\n" + msg + "\n\n");

        // any player can sort at any time
        if(event.event == UserEventType.SORT)  sort_cards(event.playerID, event);
        if(event.event == UserEventType.READY) players.get(event.playerID).set_ready();

        // if player is in queue and NAME event happens
        if( !is_id_in_players(event.playerID) && event.event == UserEventType.NAME)
        {
            // note this does not add the player to game.players
            // only sets their name
            create_player(event.playerID, event);
        }

        if(phase == 0) phase_00(event);                 // Phase 00 logic (Name)
        else if(phase == 1) phase_01(event);            // Phase 01 logic (Bet 01)
        else if(phase == 2) phase_02(event);            // Phase 02 logic (Draw)
        else if(phase == 3) phase_03(event);            // Phase 03 logic (Bet 02)
        else if(phase == 4) phase_04(event);            // Phase 04 logic (Showdown)
        else if(phase == 5) phase_05(event);            // Phase 05 logic (idk)

        if(event.event == UserEventType.FOLD){
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
                timeRemaining = -1;

                determine_player_message();
            }
        }
    }

    public void determine_player_message(){
        if(phase == 0) {
            playerMessage = "Phase: PreGame"
                          + "\n"
                          + "Waiting for players to ready up...";
        }
        else if(phase == 1){
            playerMessage = "Phase: First Bet Phase"
            + "\n"
            + "Turn: " + players.get(turn).get_name();
        }

        else if(phase == 2){
            playerMessage = "Phase: Draw Phase"
            + "\n"
            + "Turn: " + players.get(turn).get_name();
        }

        else if(phase == 3){
            playerMessage = "Phase: Second Bet Phase"
            + "\n"
            + "Turn: " + players.get(turn).get_name();
        }

        else if(phase == 4){
            playerMessage = "Phase: Showdown"
            + "\n"
            + "Turn: " + players.get(turn).get_name();
        }

        else if(phase == 5){
            if(winner != -1){
              playerMessage = "Winner: Player "
              + winner + " (" + players.get(winner).get_name() + ")"
              + " won " + winnings + " chips";
            }
            else{
              // tie situation
              playerMessage = "Tie! Both Players"
              + " won " + pot.get_pot()/2 + " chips";
            }
        }
        /*
            playerMessage = "Player Wallet: " + players[playerID].wallet
                                              + "\n"
                                              + "Player Name: " + players[playerID].name
                                              + "\n"
                                              + "Player Id: " + players[playerID].id
                                              + "Total Pot: " + pot.pot;

          var x = document.getElementById("stand_fold_display");

          if(x.style.display === "none")
          {
              x.style.display = "block";
          }
          else
          {
              x.style.display = "none";
          }
*/
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
        if(players.size() == 0 || timeRemaining == -1)
        {
            return false;
        }
        if(players.size() >= 2 && timeRemaining > 0)
        {
            timeRemaining--;
        }
        while(phase == 0 && players.size() < 5 && player_queue.size() > 0)
        {
            Player workingPlayer = player_queue.get(0);
            remove_player_queue(0);
            addPlayer(workingPlayer);
        }

        if(timeRemaining == 0 && phase != 0)
        {
            fold_current_player();
        }
        else if(timeRemaining == 0 && num_players_ready() >= 2 && phase == 0)
        {
            kick_not_ready();
            phase = 0;
            timeRemaining = -1;
            if(players.size() >= 2  && all_players_ready())
            {
                for(Player p : players)
                {
                    for(int j = 0; j < 5; j++)
                    {
                        p.add_card(draw_card());
                    }
                    place_ante(p.id);
                    p.set_cards();
                    nonFoldedPlayers.add(p);
                }
                currentplayer = getStartingPlayer();
                phase = 1;
                turn = 0;
                timeRemaining = 30;
            }
        }

        if(nonFoldedPlayers.size() == 1 && phase != 0)
        {
            // if all other players fold
            // last one standing wins
            winner = nonFoldedPlayers.get(0).get_id();
            winningPlayer = nonFoldedPlayers.get(0);
            determine_winner();
            phase = 5;
            nonFoldedPlayers.clear();
            turn = -1;
            set_players_notReady();
            timeRemaining = -1;
        }
        determine_player_message();
        return true;
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

    public Player next_player_bet_player()
    {
        Player temp = nonFoldedPlayers.get(0);
        for(int i = 1; i < nonFoldedPlayers.size(); i++)
        {
            if(temp.get_bet() < nonFoldedPlayers.get(i).get_bet())
            {
                return temp;
            }
            else if(temp.get_bet() > nonFoldedPlayers.get(i).get_bet())
            {
                return nonFoldedPlayers.get(i);
            }
            temp = nonFoldedPlayers.get(i);
        }
        return startingplayer;
    }

    public boolean all_players_ready()
    {
        for(int i = 0; i < players.size(); i++)
        {
            if(players.get(i).get_ready() != true)
            {
                return false;
            }
        }
        return true;
    }

    public int num_players_ready()
    {
        int count = 0;
        for(int i = 0; i < players.size(); i++)
        {
            if(players.get(i).get_ready() == true)
            {
                count++;
            }
        }
        return count;
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

    public void fold_current_player()
    {
        //players.get(turn).folded = true;
        currentplayer.folded = true;
        //nonFoldedPlayers.remove(players.get(turn));
        nonFoldedPlayers.remove(currentplayer);
        turn = next_player_bet();
        currentplayer = next_player_bet_player();
        timeRemaining = 30;
    }

    public void kick_not_ready()
    {
        ArrayList<Player> removeList = new ArrayList<>();
        int count = 0;
        synchronized(WebPoker.mutex)
        {
            for(int i = 0; i < players.size(); i++)
            {
                count++;
                if(players.get(i).ready == false)
                {
                    removeList.add(players.get(i));
                }
            }
            for(int i = 0; i < removeList.size(); i++)
            {
                players.remove(removeList.get(i));
            }
        }
        //WebPoker.numPlayers = count;
        //rearrange_ids();
    }

    public void rearrange_ids()
    {
        for(int i = 0; i < players.size(); i++)
        {
            players.get(i).setId(i);
        }
    }

    public int get_next_id()
    {
        // flag to signal id is currently used
        boolean flag = false;
        int id = 0;
        if(players.size() == 0)
        {
            return 0;
        }
        for(int i = 0; i < players.size(); i++)
        {
            for(Player p : players)
            {
                // id is found in players/cant use so move on
                if(p.get_id() != i)
                {
                    flag = false;
                }
                else
                {
                    flag = true;
                    break;
                }
            }
            if(flag == false)
            {
                return i;
            }
            if(i == players.size())
            {
                return i;
            }
            id = i;
        }
        // if all used add one to biggests id number
        return (id+1);
    }

    public Player get_player(int id)
    {
        for(int i = 0; i < players.size(); i++)
        {
            if(players.get(i).get_id() == id)
            {
                Player workingPlayer = players.get(i);
                return workingPlayer;
            }
        }
        return null;
    }

    public boolean is_id_in_players(int id)
    {
        for(Player p : players)
        {
            if(p.get_id() == id)
            {
                return true;
            }
        }
        return false;
    }

    /**************************************
     *
     * Deck Builders
     *
     **************************************/

    public void sort_cards(int playerId, UserEvent event)
    {
        Player workingPlayer = get_player(playerId);
        Hand.sortHand(workingPlayer.Cards);     // This actually sorts the cards
        // This sorts the ArrayList hand so if Draw is clicked the correct order is sent back to the client
        for(int i = 0; i < 5; i++)
        {
            workingPlayer.hand.set(i, workingPlayer.Cards[i]);
        }
    }

    public void new_cards(int playerId, UserEvent event){
        Player workingPlayer = get_player(playerId);
        int indexes[] = event.give_card_indexes;
        for (int i = 0; i < indexes.length; i++){
            if (indexes[i] > 0){ // 0 is default stating the card shouldnt change
                workingPlayer.hand.set(indexes[i] - 1, draw_card()); // Remove the card at the specified index
            }
        }

        workingPlayer.set_cards();
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
        Player workingPlayer = get_player(playerId);
        workingPlayer.subtract_wallet(20);
        pot.add_to_pot(20);
    }

    public void place_bet(int playerId, UserEvent event){
        Player workingPlayer = get_player(playerId);
        workingPlayer.subtract_wallet(event.amount_to_bet);
        workingPlayer.set_current_bet(event.amount_to_bet);
        pot.add_to_pot(event.amount_to_bet);
    }

    /**********************************

                Attributes

    **********************************/

    public ArrayList<Player> players = new ArrayList<>();               // players of the game
    public ArrayList<Player> nonFoldedPlayers = new ArrayList<>();
    public ArrayList<Player> player_queue = new ArrayList<>();         // Players who have not entere the game due to player cap

    public ArrayList<Card> deck = new ArrayList<>();                   // stored cards not in players hands
    private ArrayList<Hand> hands = new ArrayList<>();

    // store non folded players seperately


    // turns
    Player startingplayer;
    Player currentplayer;
    Player winningPlayer;

    private String playerMessage;


    // round - these are used with javascript to determine certain displays
    public int phase;
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

    public int timeRemaining = -1;

    // Count for number of players who have stand/fold
    // This is necessary to determine when showdown begins
    private int playerStandFold;
    private Pot pot; // total of chips being bet
}
