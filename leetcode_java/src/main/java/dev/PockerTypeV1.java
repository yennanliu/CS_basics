//package dev;
//
//public class PokerTypeV1 {
//}
//
//
//// Create a function which determines the value of a poker hand
//// Input: a poker hand of five playing cards
////          -The representation of the cards is up to you
////          -Suits: Hearts, Diamonds, Spades, Clubs
////          -Values: 2, 3, 4, 5, 6, 7, 8, 9, 10, J (jack), Q (queen), K (king), A (ace)
////          -You can assume the hand is legal (no hands of five aces, for example)
//// Output: the value of the hand
////          -The type of the output is up to you
//
///**
// *  1. input is size=5 card, (suit, value)
// *  2. return the type with `highest` score (early match)
// *
// * --------------
// *
// *  IDEA 1) PATTERN MATCH
// *
// *  j -> 11
// *  Q -> 12
// *  k -> 14
// *  a -> 15
// *
// *
// *
// */
//class Card{
//    String suit;
//    String value
//
//    Card(String suit, int value){
//        this.suit = suit;
//        int.value = value;
//    }
//
//}
//public int getType(List<Card> input){
//
//    // transform
//
//    // reorder (small -> big)
//    // input.sort(new Comparator<Card>{
//    // })
//
//    // prepare map
//    // { val : cnt }
//    Map<Integer, Integer> cntMap = new HashMap<>;
//    // {suit : cnt}
//    Map<String, Integer> suitMap = new HashMap<>;
//
//    for(Card c: input){
//        cntMap.put(c.value, map.getorDefault(c.value, 0) + 1);
//    }
//
//    for(Card c: input){
//        suitMap.put(c.suit, map.getorDefault(c.suit, 0) + 1);
//    }
//
//    if(isRoyal(input)){
//        return 1;
//    }else if(isStraightFlush(input,cntMap )){
//        return 2;
//    }else if(isFullHouse(cntMap)){
//        return 3;
//    }
//    // else if(){
//    // }
//    // ....
//
//
//    return 10;
//}
//
//
//private boolean isRoyal(List<Card> input){
//    //int i = 0;
//    //String[] expected = String[]{"10", "j", "q", "k", "a"};
//    Integer[] expected = Integer[]{10, 11, 12,13,14};
//    Integer[] collected = new Integer[5];
//    for(int i = 0; i < input.size(); i++){
//        collected[i] = card.value;
//    }
//    return expected.equals(collected);
//}
//
//
//// map : {suit : cnt }
//private boolean isStraightFlush(List<Card> input, Map<String, Integer> map){
//
//    // for(int i = 1; i < input.size(); i++){
//    //     if(!input.get(i).value.isdigit()){
//    //         return false;
//    //     }
//    //     if(input.get(i).value - input.get(i-1).value != 1 ){
//    //         return false;
//    //     }
//    // }
//    // return true &&
//    if(! isStraight(input)){return false;}
//
//    // {suit : cnt }
//    return map.size() == 1;
//}
//
//// {val: cnt}
//private boolean isFourOfKind(Map<Integer, Integer> map){
//    if(map.size() > 2){
//        return false;
//    }
//    // can improve
//    List<Integer> cnts = map.values();
//    return cnts.get(0) == 4 || cnts.get(1) == 4;
//}
//
//// map : { val : cnt }
//private boolean isFullHouse(Map<Integer, Integer> map){
//    if(map.size() != 2){
//        return false;
//    }
//    List<Integer> counts = map.values();
//    // can improve
//    // PQ
//    return (count.get(0) == 3  && count.get(1) == 2 ) ||
//            (count.get(0) == 2  && count.get(1) == 3 );
//
//}
//
//// map : {suit : cnt }
//private boolean isFlush(Map<String, Integer> map){
//    return map.size() == 1;
//}
//
//
//private boolean isStraight(List<Card> input){
//
//    for(int i = 1; i < input.size(); i++){
//        if(input.get(i).value - input.get(i-1).value != 1 ){
//            return false;
//        }
//    }
//    return true;
//
//}
//
//
//
//private boolean isThreeKind(Map<Integer, Integer> map){
//    if(map.size() != 3){
//        return false;
//    }
//    List<Integer> counts = map.values();
//    return counts.get(0) == 3 || counts.get(1) == 3 || counts.get(2) == 3;
//}
//
//private boolean isTwoPair(Map<Integer, Integer> map){
//    if(map.size() != 3){
//        return false;
//    }
//    List<Integer> counts = map.values();
//    // sort  (small -> big)
//    counts.sort();
//    //return  counts.get(0) == 2 && counts.get(1) == 2;
//    return counts.get(1) == 2 && counts.get(2) == 2;
//}
//
//
//private boolean onePair(Map<Integer, Integer> map){
//    if(map.size() != 4){
//        return false;
//    }
//    List<Integer> counts = map.values();
//    // sort  (small -> big)
//    counts.sort();
//    // return  count.get(0) == 2 || count.get(1) == 2  || count.get(2) == 2;
//    return count.get(2) == 2;
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
