package uta.cse3310;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uta.cse3310.Card;
import uta.cse3310.Card.Value;
import uta.cse3310.Card.Suite;
import java.util.Map;
import java.util.EnumMap;
import java.util.HashMap;

public class Hand
{
    public Card []cards = new Card[5];
    public Value first_compare = null;
    public Value second_compare = null;
    public Value third_compare = null;
    public Value fourth_compare = null;
    public Value fifth_compare = null;
    // first variable for comparision if both hands are the same
    // represents tiebrackers
    // ex - both hands have a pair, the higher pair wins


    // This solution assumes that tiebreakers go down all five cards
    // meaning in two high card hands that have the same five valued cards
    // this program will run through all five cards with tests.

    // also assumes that input will be valid (no duplicates or invalid cards)

    public Hand(Card cards[])
    {
        for(int i = 0; i < cards.length; i++)
        {
            this.cards[i] = cards[i];
        }

    }

    public static void sortHand(Card cards[])
    {
        Card temp;
        for(int i = 0; i < cards.length; i++)
        {
            for(int j = i+1; j < cards.length; j++)
            {
                if(cards[i].value.ordinal() > cards[j].value.ordinal())
                {
                    temp = cards[i];
                    cards[i] = cards[j];
                    cards[j] = temp;
                }
            }
        }
    }

    public void mapping(EnumMap<Value,Integer> cardValueMap,HashMap<String,Integer>handValue) // hashmaps to hold enum "value" for comparisons
    {
        // use hashmap to put value to "values"
        cardValueMap.put(Value.ACE,1);
        cardValueMap.put(Value.TWO,2);
        cardValueMap.put(Value.THREE,3);
        cardValueMap.put(Value.FOUR,4);
        cardValueMap.put(Value.FIVE,5);
        cardValueMap.put(Value.SIX,6);
        cardValueMap.put(Value.SEVEN,7);
        cardValueMap.put(Value.EIGHT,8);
        cardValueMap.put(Value.NINE,9);
        cardValueMap.put(Value.TEN,10);
        cardValueMap.put(Value.JACK,11);
        cardValueMap.put(Value.QUEEN,12);
        cardValueMap.put(Value.KING,13);

        //use hashmap to put values to hand names
        handValue.put("High Card",1);
        handValue.put("Pair",2);
        handValue.put("Two-Pair",3);
        handValue.put("Three-of-a-kind",4);
        handValue.put("Straight",5);
        handValue.put("Flush",6);
        handValue.put("Full House",7);
        handValue.put("Four-of-a-kind",8);
        handValue.put("Straight Flush",9);
        handValue.put("Royal Flush",10);
    }


    private String determineHand(EnumMap<Value,Integer> cardValueMap,HashMap<String,Integer>handValue) // only works with sorted hands ascending
    {
        String handName = null;
        // test for flush - continue testing for straight flush and royal flush
        if(cards[0].suite == cards[1].suite && cards[1].suite == cards[2].suite &&
            cards[2].suite == cards[3].suite && cards[3].suite == cards[4].suite)
        {
            // tests for royal straight
            if(cardValueMap.get(cards[0].value) == 1 && cardValueMap.get(cards[1].value) == 10 &&
                cardValueMap.get(cards[2].value) == 11 && cardValueMap.get(cards[3].value) == 12
                && cardValueMap.get(cards[4].value) == 13)
            {
                handName = "Royal Flush";
            }
            else if(cardValueMap.get(cards[0].value) == cardValueMap.get(cards[1].value)-1 &&
                cardValueMap.get(cards[1].value) == cardValueMap.get(cards[2].value)-1 &&
                cardValueMap.get(cards[2].value) == cardValueMap.get(cards[3].value)-1 &&
                cardValueMap.get(cards[3].value) == cardValueMap.get(cards[4].value)-1)
            {
                // test for straight within the flush
                handName = "Straight Flush";
                first_compare = cards[4].value;
            }
            else
            {
                // anything else is within a flush
                handName = "Flush";
                first_compare = cards[4].value;
                second_compare = cards[3].value;
                third_compare = cards[2].value;
                fourth_compare = cards[1].value;
            }
        }
        else if(cardValueMap.get(cards[0].value) == cardValueMap.get(cards[1].value)-1 &&
            cardValueMap.get(cards[1].value) == cardValueMap.get(cards[2].value)-1 &&
            cardValueMap.get(cards[2].value) == cardValueMap.get(cards[3].value)-1 &&
            cardValueMap.get(cards[3].value) == cardValueMap.get(cards[4].value)-1)
        {
            // test for straight excluding a straight flush
            handName = "Straight";
            first_compare = cards[4].value;
        }
        else if(cardValueMap.get(cards[0].value) == cardValueMap.get(cards[1].value) &&
            cardValueMap.get(cards[1].value) == cardValueMap.get(cards[2].value) &&
            cardValueMap.get(cards[2].value) == cardValueMap.get(cards[3].value))
        {
            // test 4 of a kind with a lower card than the 4
            handName = "Four-of-a-kind";
            first_compare = cards[0].value;
            second_compare = cards[4].value;
        }
        else if(cardValueMap.get(cards[1].value) == cardValueMap.get(cards[2].value) &&
            cardValueMap.get(cards[2].value) == cardValueMap.get(cards[3].value) &&
            cardValueMap.get(cards[3].value) == cardValueMap.get(cards[4].value))
        {
            // test 4 of a kind with a higher card than the 4
            handName = "Four-of-a-kind";
            first_compare = cards[1].value;
            second_compare = cards[0].value;
        }
        else if(cardValueMap.get(cards[0].value) == cardValueMap.get(cards[1].value) &&
            cardValueMap.get(cards[1].value) == cardValueMap.get(cards[2].value) &&
            cardValueMap.get(cards[3].value) == cardValueMap.get(cards[4].value))
        {
            // test for full house with 3 of a kind higher
            handName = "Full House";
            first_compare = cards[0].value;
            second_compare = cards[3].value;
        }
        else if(cardValueMap.get(cards[0].value) == cardValueMap.get(cards[1].value) &&
            cardValueMap.get(cards[2].value) == cardValueMap.get(cards[3].value) &&
            cardValueMap.get(cards[3].value) == cardValueMap.get(cards[4].value))
        {
            // test for full house with pair higher
            handName = "Full House";
            first_compare = cards[2].value;
            second_compare = cards[0].value;
        }
        else if(cardValueMap.get(cards[0].value) == cardValueMap.get(cards[1].value) &&
            cardValueMap.get(cards[2].value) == cardValueMap.get(cards[3].value))
        {
            // test for 3 of a kind at first 3 cards
            handName = "Two-Pair";
            first_compare = cards[3].value;
            second_compare = cards[1].value;
            third_compare = cards[4].value;
        }
        else if(cardValueMap.get(cards[0].value) == cardValueMap.get(cards[1].value) &&
            cardValueMap.get(cards[3].value) == cardValueMap.get(cards[4].value))
        {
            // test for 3 of a kind at first 3 cards
            handName = "Two-Pair";
            first_compare = cards[3].value;
            second_compare = cards[1].value;
            third_compare = cards[2].value;
        }
        else if(cardValueMap.get(cards[1].value) == cardValueMap.get(cards[2].value) &&
            cardValueMap.get(cards[3].value) == cardValueMap.get(cards[4].value))
        {
            // test for 3 of a kind at first 3 cards
            handName = "Two-Pair";
            first_compare = cards[3].value;
            second_compare = cards[1].value;
            third_compare = cards[0].value;
        }
        else if(cardValueMap.get(cards[0].value) == cardValueMap.get(cards[1].value) &&
            cardValueMap.get(cards[1].value) == cardValueMap.get(cards[2].value))
        {
            // test for 3 of a kind at first 3 cards
            handName = "Three-of-a-kind";
            first_compare = cards[0].value;
            second_compare = cards[4].value;
            third_compare = cards[3].value;
        }
        else if(cardValueMap.get(cards[1].value) == cardValueMap.get(cards[2].value) &&
            cardValueMap.get(cards[2].value) == cardValueMap.get(cards[3].value))
        {
            // test for 3 of a kind at middle 3 cards
            handName = "Three-of-a-kind";
            first_compare = cards[1].value;
            second_compare = cards[4].value;
            third_compare = cards[0].value;
        }
        else if(cardValueMap.get(cards[2].value) == cardValueMap.get(cards[3].value) &&
            cardValueMap.get(cards[3].value) == cardValueMap.get(cards[4].value))
        {
            // test for 3 of a kind at last 3 cards
            handName = "Three-of-a-kind";
            first_compare = cards[2].value;
            second_compare = cards[1].value;
            third_compare = cards[0].value;
        }
        else if(cardValueMap.get(cards[0].value) == cardValueMap.get(cards[1].value))
        {
            // test for pair at indeces 0-1
            handName = "Pair";
            first_compare = cards[0].value;
            second_compare = cards[4].value;
            third_compare = cards[3].value;
            fourth_compare = cards[2].value;
        }
        else if(cardValueMap.get(cards[1].value) == cardValueMap.get(cards[2].value))
        {
            // test for pair at indeces 1-2
            handName = "Pair";
            first_compare = cards[1].value;
            second_compare = cards[4].value;
            third_compare = cards[3].value;
            fourth_compare = cards[0].value;
        }
        else if(cardValueMap.get(cards[2].value) == cardValueMap.get(cards[3].value))
        {
            // test for pair at indeces 2-3
            handName = "Pair";
            first_compare = cards[2].value;
            second_compare = cards[4].value;
            third_compare = cards[1].value;
            fourth_compare = cards[0].value;
        }
        else if(cardValueMap.get(cards[3].value) == cardValueMap.get(cards[4].value))
        {
            // test for pair at indeced 3-4
            handName = "Pair";
            first_compare = cards[3].value;
            second_compare = cards[2].value;
            third_compare = cards[1].value;
            fourth_compare = cards[0].value;
        }
        else
        {
            // all else is just a high card
            handName = "High Card";
            first_compare = cards[4].value;
            second_compare = cards[3].value;
            third_compare = cards[2].value;
            fourth_compare = cards[1].value;
            fifth_compare = cards[0].value;
        }

        return handName;
    }

    public boolean is_better_than(Hand H)
    {
        // test if hand1 is better than passed in hand2.

        // create maps for card and hand "values"
        EnumMap <Value,Integer>cardValueMap = new EnumMap<Value,Integer>(Value.class);
        HashMap <String,Integer>handValue = new HashMap<>();

        String H1; // for "this" object
        String H2; // for H
        mapping(cardValueMap, handValue); // pass in maps to map all values
        Hand.sortHand(this.cards); // sort hand1 based on card values
        Hand.sortHand(H.cards); // sort hand2
        H1 = this.determineHand(cardValueMap, handValue);
        H2 = H.determineHand(cardValueMap, handValue);

        if(handValue.get(H1) > handValue.get(H2))
        {
            return true;
        }
        else if(handValue.get(H1) == handValue.get(H2))
        {
            if(first_compare != null && cardValueMap.get(first_compare) > cardValueMap.get(H.first_compare))
            {
                return true;
            }
            else if(first_compare != null && cardValueMap.get(first_compare) < cardValueMap.get(H.first_compare))
            {
                return false;
            }
            else if(second_compare != null && cardValueMap.get(second_compare) > cardValueMap.get(H.second_compare))
            {
                return true;
            }
            else if(second_compare != null && cardValueMap.get(second_compare) < cardValueMap.get(H.second_compare))
            {
                return false;
            }
            else if(third_compare != null && cardValueMap.get(third_compare) > cardValueMap.get(H.third_compare))
            {
                return true;
            }
            else if(third_compare != null && cardValueMap.get(third_compare) < cardValueMap.get(H.third_compare))
            {
                return false;
            }
            else if(fourth_compare != null && cardValueMap.get(fourth_compare) > cardValueMap.get(H.fourth_compare))
            {
                return true;
            }
            else if(fourth_compare != null && cardValueMap.get(fourth_compare) < cardValueMap.get(H.fourth_compare))
            {
                return false;
            }
            else if(fifth_compare != null && cardValueMap.get(fifth_compare) > cardValueMap.get(H.fifth_compare))
            {
                return true;
            }
            else if(fifth_compare != null && cardValueMap.get(fifth_compare) < cardValueMap.get(H.fifth_compare))
            {
                return false;
            }

        }
        return false;
    }

    public boolean is_equal(Hand H)
    {
        // test if hand1 is equal to passed in hand2.

        // create maps for card and hand "values"
        EnumMap <Value,Integer>cardValueMap = new EnumMap<Value,Integer>(Value.class);
        HashMap <String,Integer>handValue = new HashMap<>();

        String H1; // for "this" object
        String H2; // for H
        mapping(cardValueMap, handValue); // pass in maps to map all values
        Hand.sortHand(this.cards); // sort hand1 based on card values
        Hand.sortHand(H.cards); // sort hand2
        H1 = this.determineHand(cardValueMap, handValue);
        H2 = H.determineHand(cardValueMap, handValue);

        if(handValue.get(H1) != handValue.get(H2))
        {
            return false;
        }
        else
        {
            if(first_compare != null && cardValueMap.get(first_compare) != cardValueMap.get(H.first_compare))
            {
                return false;
            }
            else if(second_compare != null && cardValueMap.get(second_compare) != cardValueMap.get(H.second_compare))
            {
                return false;
            }
            else if(third_compare != null && cardValueMap.get(third_compare) != cardValueMap.get(H.third_compare))
            {
                return false;
            }
            else if(fourth_compare != null && cardValueMap.get(fourth_compare) != cardValueMap.get(H.fourth_compare))
            {
                return false;
            }
            else if(fifth_compare != null && cardValueMap.get(fifth_compare) != cardValueMap.get(H.fifth_compare))
            {
                return false;
            }
            else if(fifth_compare != null && cardValueMap.get(fifth_compare) != cardValueMap.get(H.fifth_compare))
            {
                return false;
            }
            else
            {
                return true; // all necessary comparisons are equal
            }
        }
    }

}
