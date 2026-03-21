package dev;

import java.util.*;

enum Suit {
    HEARTS, DIAMONDS, SPADES, CLUBS
}

enum Rank {
    TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6),
    SEVEN(7), EIGHT(8), NINE(9), TEN(10),
    JACK(11), QUEEN(12), KING(13), ACE(14);

    int value;

    Rank(int value) {
        this.value = value;
    }
}

enum HandType {
    ROYAL_FLUSH,
    STRAIGHT_FLUSH,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    FLUSH,
    STRAIGHT,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD
}

class Card {
    Suit suit;
    Rank rank;

    Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }
}

public class PokerTypeV3 {

    public HandType evaluate(List<Card> hand) {
        // Sort by rank value
        hand.sort(Comparator.comparingInt(c -> c.rank.value));

        Map<Integer, Integer> valueCount = new HashMap<>();
        Map<Suit, Integer> suitCount = new HashMap<>();

        for (Card c : hand) {
            valueCount.put(c.rank.value,
                    valueCount.getOrDefault(c.rank.value, 0) + 1);

            suitCount.put(c.suit,
                    suitCount.getOrDefault(c.suit, 0) + 1);
        }

        boolean flush = isFlush(suitCount);
        boolean straight = isStraight(hand);

        // 1. Royal Flush
        if (flush && isRoyal(hand)) return HandType.ROYAL_FLUSH;

        // 2. Straight Flush
        if (flush && straight) return HandType.STRAIGHT_FLUSH;

        // 3. Four of a Kind
        if (valueCount.containsValue(4)) return HandType.FOUR_OF_A_KIND;

        // 4. Full House
        if (valueCount.size() == 2 && valueCount.containsValue(3))
            return HandType.FULL_HOUSE;

        // 5. Flush
        if (flush) return HandType.FLUSH;

        // 6. Straight
        if (straight) return HandType.STRAIGHT;

        // 7. Three of a Kind
        if (valueCount.size() == 3 && valueCount.containsValue(3))
            return HandType.THREE_OF_A_KIND;

        // 8. Two Pair
        if (isTwoPair(valueCount)) return HandType.TWO_PAIR;

        // 9. One Pair
        if (valueCount.size() == 4) return HandType.ONE_PAIR;

        // 10. High Card
        return HandType.HIGH_CARD;
    }

    private boolean isFlush(Map<Suit, Integer> suitCount) {
        return suitCount.size() == 1;
    }

    private boolean isStraight(List<Card> hand) {
        for (int i = 1; i < hand.size(); i++) {
            if (hand.get(i).rank.value != hand.get(i - 1).rank.value + 1) {
                return false;
            }
        }
        return true;
    }

    private boolean isRoyal(List<Card> hand) {
        int[] expected = {10, 11, 12, 13, 14};
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).rank.value != expected[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean isTwoPair(Map<Integer, Integer> valueCount) {
        if (valueCount.size() != 3) return false;

        int pairCount = 0;
        for (int count : valueCount.values()) {
            if (count == 2) pairCount++;
        }
        return pairCount == 2;
    }
}