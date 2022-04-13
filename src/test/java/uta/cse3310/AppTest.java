package uta.cse3310;

import uta.cse3310.Card;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    /**
     * Rigorous Test :-)
     */
     // test if Game constructor is working
     // very basic but most important aspects
    @Test
    public void shouldAnswerWithTrue()
    {
        Game game = new Game();
        assertTrue( game != null);
    }

    // test the create_deck method
    // true if correct size
    @Test
    public void shuffleTest()
    {
        Game game = new Game();

        assertTrue( game.deck.size() == 52 );
    }

    // test the sortHand method in Hand.java
    @Test
    public void sortHandTest()
    {
        Card card1 = new Card();
        Card card2 = new Card();
        Card card3 = new Card();
        Card card4 = new Card();
        Card card5 = new Card();
        card1.value = Card.Value.FIVE;
        card1.suite = Card.Suite.SPADES;
        card2.value = Card.Value.SIX;
        card2.suite = Card.Suite.SPADES;
        card3.value = Card.Value.SEVEN;
        card3.suite = Card.Suite.SPADES;
        card4.value = Card.Value.EIGHT;
        card4.suite = Card.Suite.SPADES;
        card5.value = Card.Value.NINE;
        card5.suite = Card.Suite.SPADES;
        Card cards[] = {card3,card4,card2,card5,card1};
        Card sortedCards[] = {card1,card2,card3,card4,card5};
        Hand.sortHand(cards);
        assertTrue( cards[0] == sortedCards[0] && cards[1] == sortedCards[1] &&
            cards[2] == sortedCards[2] && cards[3] == sortedCards[3] &&
            cards[4] == sortedCards[4]);
    }

    // test empty_hand method in Game.java
    @Test
    public void emptyHandTest()
    {
        Game game = new Game();
        Player player = new Player(0);
        game.addPlayer(player);
        game.empty_hand(player);
        assertTrue( player.hand.size() == 0 );
    }

    // test set_players_notReady method in Game.java
    @Test
    public void setPlayersNotReadyTest()
    {
        Game game = new Game();
        Player p0 = new Player(0);
        p0.ready = true;
        Player p1 = new Player(1);
        p1.ready = true;
        Player p2 = new Player(2);
        p2.ready = true;
        Player p3 = new Player(3);
        p3.ready = true;
        game.players.add(p0);
        game.players.add(p1);
        game.players.add(p2);
        game.players.add(p3);
        game.set_players_notReady();

        assertTrue( !p0.ready && !p1.ready && !p2.ready && !p3.ready );
    }

    // test num_players_Ready method in Game.java
    @Test
    public void numPlayersReadyTest()
    {
        Game game = new Game();
        Player p0 = new Player(0);
        p0.ready = true;
        Player p1 = new Player(1);
        p1.ready = true;
        Player p2 = new Player(2);
        Player p3 = new Player(3);
        game.players.add(p0);
        game.players.add(p1);
        game.players.add(p2);
        game.players.add(p3);

        assertTrue( game.num_players_ready() == 2 );
    }

    // test all_players_Ready method in Game.java
    @Test
    public void allPlayersReadyTest()
    {
        Game game = new Game();
        Player p0 = new Player(0);
        p0.ready = true;
        Player p1 = new Player(1);
        p1.ready = true;
        Player p2 = new Player(2);
        p2.ready = true;
        Player p3 = new Player(3);
        p3.ready = true;
        game.players.add(p0);
        game.players.add(p1);
        game.players.add(p2);
        game.players.add(p3);

        assertTrue( game.all_players_ready() );
    }

    // test all_players_Ready method in Game.java
    @Test
    public void allPlayersReadyTest2()
    {
        Game game = new Game();
        Player p0 = new Player(0);
        p0.ready = true;
        Player p1 = new Player(1);
        p1.ready = true;
        Player p2 = new Player(2);
        p2.ready = true;
        Player p3 = new Player(3);
        game.players.add(p0);
        game.players.add(p1);
        game.players.add(p2);
        game.players.add(p3);

        assertTrue( !game.all_players_ready() );
    }

    // test all_bets_equal method in Game.java
    @Test
    public void allBetEqualTest()
    {
        Game game = new Game();
        Player p0 = new Player(0);
        p0.currentBet = 10;
        Player p1 = new Player(1);
        p1.currentBet = 10;
        Player p2 = new Player(2);
        p2.currentBet = 10;
        Player p3 = new Player(3);
        p3.currentBet = 10;
        game.players.add(p0);
        game.nonFoldedPlayers.add(p0);
        game.players.add(p1);
        game.nonFoldedPlayers.add(p1);
        game.players.add(p2);
        game.nonFoldedPlayers.add(p2);
        game.players.add(p3);
        game.nonFoldedPlayers.add(p3);

        assertTrue( game.all_bets_equal() );
    }

    // test fold_current_player() method in Game.java
    @Test
    public void foldCurrentPlayerTest()
    {
        Game game = new Game();

        Player p0 = new Player(0);
        Player p1 = new Player(1);

        game.players.add(p0);
        game.players.add(p1);
        game.nonFoldedPlayers.add(p0);
        game.nonFoldedPlayers.add(p1);

        game.currentplayer = p0;
        game.fold_current_player();

        assertTrue( game.players.size() == 2 && game.nonFoldedPlayers.size() == 1 );
    }
}
