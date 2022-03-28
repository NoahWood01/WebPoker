package uta.cse3310;

public class Card {
    enum Suite {
        HEARTS('H'), CLUBS('C'), DIAMONDS('D'), SPADES('S');

        Suite(char s){ this.suite = s; }
        char suite;
    }

    enum Value {
        ACE('A'), TWO('2'), THREE('3'), FOUR('4'), FIVE('5'), SIX('6'), SEVEN('7'), EIGHT('8'), 
        NINE('9'),TEN('T'), JACK('J'), QUEEN('Q'), KING('K');

        Value(char v){ this.value = v; }
        char value;
    }

    char suite;
    char value;
}