//package dev;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class PokerTypeV2 {
//
//    public enum HandRank {
//        ROYAL_FLUSH(10), STRAIGHT_FLUSH(9), FOUR_OF_A_KIND(8), FULL_HOUSE(7),
//        FLUSH(6), STRAIGHT(5), THREE_OF_A_KIND(4), TWO_PAIR(3),
//        ONE_PAIR(2), HIGH_CARD(1);
//
//        private final int value;
//        HandRank(int value) { this.value = value; }
//    }
//
//    // Modern Java Record to replace the Card class
//    public record Card(int value, String suit) {}
//
//    public HandRank getHandType(List<Card> hand) {
//        if (hand == null || hand.size() != 5) throw new IllegalArgumentException("Invalid hand size");
//
//        // 1. Sort cards by value (2 through 14)
//        hand.sort(Comparator.comparingInt(Card::value));
//
//        // 2. Extract key metrics
//        boolean isFlush = hand.stream().map(Card::suit).distinct().count() == 1;
//        boolean isStraight = isConsecutive(hand);
//
//        // Get counts of each rank, sorted descending (e.g., [3, 2] for Full House)
//        List<Long> counts = hand.stream()
//                .collect(Collectors.groupingBy(Card::value, Collectors.counting()))
//                .values().stream()
//                .sorted(Comparator.reverseOrder())
//                .toList();
//
//        // 3. Pattern Matching Logic
//        if (isFlush && isStraight) {
//            return (hand.get(0).value() == 10) ? HandRank.ROYAL_FLUSH : HandRank.STRAIGHT_FLUSH;
//        }
//
//        if (counts.get(0) == 4) return HandRank.FOUR_OF_A_KIND;
//        if (counts.get(0) == 3 && counts.get(1) == 2) return HandRank.FULL_HOUSE;
//        if (isFlush) return HandRank.FLUSH;
//        if (isStraight) return HandRank.STRAIGHT;
//        if (counts.get(0) == 3) return HandRank.THREE_OF_A_KIND;
//        if (counts.get(0) == 2 && counts.get(1) == 2) return HandRank.TWO_PAIR;
//        if (counts.get(0) == 2) return HandRank.ONE_PAIR;
//
//        return HandRank.HIGH_CARD;
//    }
//
//    private boolean isConsecutive(List<Card> hand) {
//        // Normal Straight
//        boolean normal = true;
//        for (int i = 0; i < 4; i++) {
//            if (hand.get(i + 1).value() - hand.get(i).value() != 1) {
//                normal = false;
//                break;
//            }
//        }
//        // Ace-Low Straight (5, 4, 3, 2, Ace)
//        // If Ace is 14, check for [2, 3, 4, 5, 14]
//        boolean wheel = (hand.get(0).value() == 2 && hand.get(1).value() == 3 &&
//                hand.get(2).value() == 4 && hand.get(3).value() == 5 &&
//                hand.get(4).value() == 14);
//
//        return normal || wheel;
//    }
//}