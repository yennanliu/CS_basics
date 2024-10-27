package LeetCodeJava.Heap;

// https://leetcode.com/problems/hand-of-straights/description/

import java.util.*;

/**
 * 846. Hand of Straights
 * Medium
 * Topics
 * Companies
 * Alice has some number of cards and she wants to rearrange the cards into groups so that each group is of size groupSize, and consists of groupSize consecutive cards.
 *
 * Given an integer array hand where hand[i] is the value written on the ith card and an integer groupSize, return true if she can rearrange the cards, or false otherwise.
 *
 *
 *
 * Example 1:
 *
 * Input: hand = [1,2,3,6,2,3,4,7,8], groupSize = 3
 * Output: true
 * Explanation: Alice's hand can be rearranged as [1,2,3],[2,3,4],[6,7,8]
 * Example 2:
 *
 * Input: hand = [1,2,3,4,5], groupSize = 4
 * Output: false
 * Explanation: Alice's hand can not be rearranged into groups of 4.
 *
 *
 *
 * Constraints:
 *
 * 1 <= hand.length <= 104
 * 0 <= hand[i] <= 109
 * 1 <= groupSize <= hand.length
 *
 *
 * Note: This question is the same as 1296: https://leetcode.com/problems/divide-array-in-sets-of-k-consecutive-numbers/
 *
 */
public class HandOfStraights {

    // V0
//    public boolean isNStraightHand(int[] hand, int groupSize) {
//
//    }

    // V0'
    // TODO : fix below
    // NOTE !!! PriorityQueue in Java by default is a min-heap (not a max-heap). So, the negation and handling logic need to be fixed.
//    public boolean isNStraightHand(int[] hand, int groupSize) {
//
//        if (hand.length % groupSize != 0){
//            return false;
//        }
//
//        if (groupSize==1){
//            return true;
//        }
//
//        if (hand.length == groupSize){
//            // sort
//            Arrays.sort(hand);
//            //int min = hand[0];
//            for (int k = 0; k < hand.length-1; k++){
//                if (hand[k]+1 != hand[k+1]){
//                    return false;
//                }
//            }
//            return true;
//        }
//
//
//        // PQ (max heap???)
//        PriorityQueue<Integer> pq = new PriorityQueue();
//        for (int i = 0; i < hand.length; i++){
//            // NOTE !! we put "-1 * i"
//            // so can create a "small" PQ
//            pq.add(-1 * hand[i]);
//        }
//
//        System.out.println(">>> pq = " + pq);
//
//        // check
//        while (!pq.isEmpty()){
//            // get cur min element
//            int cnt = 0;
//            int min = pq.poll();
//            Queue<Integer> tmpQ = new LinkedList<>();
//            if (!pq.contains(min+1) || !pq.contains(min+2)){
//                return false;
//            }
//            // till collect groupSize count of element
//            for (int j = 0; j < groupSize && !pq.isEmpty(); j++){
//                int cur = pq.poll();
//                // found consecutive element
//                if (cur == min+1){
//                    min += 1;
//                    // if not found, add to tmp queue
//                }else{
//                    tmpQ.add(cur);
//                }
//            }
//
//            // put tmp element back to pq
//            while (!tmpQ.isEmpty()){
//                pq.add(tmpQ.poll());
//            }
//
//            // re-ordering PQ ??? needed ?
//        }
//
//        return true;
//    }


    // V1-1
    // IDEA: TreeMap (gpt)
    public boolean isNStraightHand_1_1(int[] hand, int groupSize) {
        // If the total number of cards is not divisible by groupSize, return false
        if (hand.length % groupSize != 0) {
            return false;
        }

        // TreeMap to count occurrences of each card
        /**
         * TreeMap for Counting: We use a TreeMap to count occurrences of each card.
         * TreeMap maintains the natural order of the keys, making it easier
         * to process the smallest card and form consecutive groups.
         */
        TreeMap<Integer, Integer> cardCountMap = new TreeMap<>();
        for (int card : hand) {
            cardCountMap.put(card, cardCountMap.getOrDefault(card, 0) + 1);
        }

        // Form groups starting with the smallest card in the map
        while (!cardCountMap.isEmpty()) {
            /**
             * 	Group Formation:
             * 	•	For each smallest card (retrieved using firstKey()),
             * 	    we try to form a group of size groupSize by checking consecutive cards.
             *
             * 	•	For each card in the group, we reduce its count.
             * 	    If the count becomes zero, we remove that card from the TreeMap.
             */
            int firstCard = cardCountMap.firstKey();  // Get the smallest card

            // Try to form a group starting from the smallest card
            for (int i = 0; i < groupSize; i++) {
                int currentCard = firstCard + i;
                if (!cardCountMap.containsKey(currentCard)) {
                    // Early Return: If at any point we cannot form a group (because a required card is missing), we return false.
                    return false;  // Cannot form a consecutive group
                }

                // Decrease the count of the current card
                int count = cardCountMap.get(currentCard);
                if (count == 1) {
                    cardCountMap.remove(currentCard);  // Remove the card if the count is zero
                } else {
                    cardCountMap.put(currentCard, count - 1);
                }
            }
        }

        return true;
    }

    // V1-2
    // IDEA: HashMap (gpt)
    public boolean isNStraightHand_1_2(int[] hand, int groupSize) {
        // If the total number of cards is not divisible by groupSize, return false
        if (hand.length % groupSize != 0) {
            return false;
        }

        // HashMap to count occurrences of each card
        HashMap<Integer, Integer> cardCountMap = new HashMap<>();
        for (int card : hand) {
            cardCountMap.put(card, cardCountMap.getOrDefault(card, 0) + 1);
        }

        // Sort the unique cards
        int[] uniqueCards = cardCountMap.keySet().stream().mapToInt(Integer::intValue).toArray();
        Arrays.sort(uniqueCards);

        // Try to form groups starting from the smallest card
        for (int card : uniqueCards) {
            int count = cardCountMap.get(card);
            if (count > 0) {
                // Try to form a group of size `groupSize` starting from `card`
                for (int i = 0; i < groupSize; i++) {
                    int currentCard = card + i;
                    if (cardCountMap.getOrDefault(currentCard, 0) < count) {
                        return false;  // Not enough cards to form the group
                    }
                    cardCountMap.put(currentCard, cardCountMap.get(currentCard) - count);
                }
            }
        }

        return true;
    }

    // V2-1
    // IDEA :  Using Map
    // https://leetcode.com/problems/hand-of-straights/editorial/
    public boolean isNStraightHand_2_1(int[] hand, int groupSize) {
        int handSize = hand.length;
        if (handSize % groupSize != 0) {
            return false;
        }

        // TreeMap to store the count of each card value
        Map<Integer, Integer> cardCount = new TreeMap<>();
        for (int i = 0; i < handSize; i++) {
            cardCount.put(hand[i], cardCount.getOrDefault(hand[i], 0) + 1);
        }

        // Process the cards until the map is empty
        while (cardCount.size() > 0) {
            // Get the smallest card value
            int current_card = cardCount.entrySet().iterator().next().getKey();
            // Check each consecutive sequence of groupSize cards
            for (int i = 0; i < groupSize; i++) {
                // If a card is missing or has exhausted its occurrences
                if (!cardCount.containsKey(current_card + i)) return false;
                cardCount.put(
                        current_card + i,
                        cardCount.get(current_card + i) - 1
                );
                // Remove the card value if its occurrences are exhausted
                if (cardCount.get(current_card + i) == 0) cardCount.remove(
                        current_card + i
                );
            }
        }

        return true;
    }

    // V2-2
    // IDEA : TreeMap + Queue (Optimal with hashMap)
    // https://leetcode.com/problems/hand-of-straights/editorial/
    public boolean isNStraightHand_2_2(int[] hand, int groupSize) {
        // Map to store the count of each card value
        Map<Integer, Integer> cardCount = new TreeMap<>();

        for (int card : hand) {
            cardCount.put(card, cardCount.getOrDefault(card, 0) + 1);
        }

        // Queue to keep track of the number of new groups
        // starting with each card value
        Queue<Integer> groupStartQueue = new LinkedList<>();
        int lastCard = -1, currentOpenGroups = 0;
        for (Map.Entry<Integer, Integer> entry : cardCount.entrySet()) {
            int currentCard = entry.getKey();
            // Check if there are any discrepancies in the sequence
            // or more groups are required than available cards
            if (
                    (currentOpenGroups > 0 && currentCard > lastCard + 1) ||
                            currentOpenGroups > cardCount.get(currentCard)
            ) {
                return false;
            }
            // Calculate the number of new groups starting with the current card
            groupStartQueue.offer(
                    cardCount.get(currentCard) - currentOpenGroups
            );
            lastCard = currentCard;
            currentOpenGroups = cardCount.get(currentCard);
            // Maintain the queue size to be equal to groupSize
            if (groupStartQueue.size() == groupSize) {
                currentOpenGroups -= groupStartQueue.poll();
            }
        }

        // All groups should be completed by the end
        return currentOpenGroups == 0;
    }

}
