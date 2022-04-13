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
     // test if build is working
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
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
}
