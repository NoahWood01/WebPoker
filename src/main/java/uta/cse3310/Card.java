package uta.cse3310;

public class Card {
    public enum Suite {
        HEARTS, CLUBS, DIAMONDS, SPADES
    }

    public enum Value {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
        TEN, JACK, QUEEN, KING
    }

    public static Suite getSuite(Suite suite) {
        return suite;
    }

    public static Value getValue(Value value) {
        return value;
    }

    public Suite suite;
    public Value value;

    public Card() {

    }

}
