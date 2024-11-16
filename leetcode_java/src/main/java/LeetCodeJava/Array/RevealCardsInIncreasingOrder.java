package LeetCodeJava.Array;

// https://leetcode.com/problems/reveal-cards-in-increasing-order/description/

import java.util.*;

/**
 * 950. Reveal Cards In Increasing Order
 * Medium
 * Topics
 * Companies
 * You are given an integer array deck. There is a deck of cards where every card has a unique integer. The integer on the ith card is deck[i].
 *
 * You can order the deck in any order you want. Initially, all the cards start face down (unrevealed) in one deck.
 *
 * You will do the following steps repeatedly until all cards are revealed:
 *
 * Take the top card of the deck, reveal it, and take it out of the deck.
 * If there are still cards in the deck then put the next top card of the deck at the bottom of the deck.
 * If there are still unrevealed cards, go back to step 1. Otherwise, stop.
 * Return an ordering of the deck that would reveal the cards in increasing order.
 *
 * Note that the first entry in the answer is considered to be the top of the deck.
 *
 *
 *
 * Example 1:
 *
 * Input: deck = [17,13,11,2,3,5,7]
 * Output: [2,13,3,11,5,17,7]
 * Explanation:
 * We get the deck in the order [17,13,11,2,3,5,7] (this order does not matter), and reorder it.
 * After reordering, the deck starts as [2,13,3,11,5,17,7], where 2 is the top of the deck.
 * We reveal 2, and move 13 to the bottom.  The deck is now [3,11,5,17,7,13].
 * We reveal 3, and move 11 to the bottom.  The deck is now [5,17,7,13,11].
 * We reveal 5, and move 17 to the bottom.  The deck is now [7,13,11,17].
 * We reveal 7, and move 13 to the bottom.  The deck is now [11,17,13].
 * We reveal 11, and move 17 to the bottom.  The deck is now [13,17].
 * We reveal 13, and move 17 to the bottom.  The deck is now [17].
 * We reveal 17.
 * Since all the cards revealed are in increasing order, the answer is correct.
 * Example 2:
 *
 * Input: deck = [1,1000]
 * Output: [1,1000]
 *
 *
 * Constraints:
 *
 * 1 <= deck.length <= 1000
 * 1 <= deck[i] <= 106
 * All the values of deck are unique.
 *
 */
public class RevealCardsInIncreasingOrder {

    // V0
    // TODO: implement
//    public int[] deckRevealedIncreasing(int[] deck) {
//
//    }

    // V1-1
    // IDEA : Simulation with Queue
    // https://leetcode.com/problems/reveal-cards-in-increasing-order/editorial/
    /**
     *  IDEA :
     *
     * In this solution, we also start by sorting the deck and creating a result array.
     *
     * How do we know what order to put the cards in?
     *
     * The result array will not be revealed in order. Instead, the indexes of the result array will be revealed in a certain order.
     *
     * Input: [17,13,11,2,3,5,7]
     * Output: [2,13,3,11,5,17,7]
     *
     * Order of indexes revealed: 0, 2, 4, 6, 3, 1, 5
     *
     * We can work backward from the sorted order since we can easily sort the deck in ascending order.
     *
     * Sorted Order: [2,3,5,7,11,13,17]
     *
     * We can simulate the revealing process using a queue of indices to find the order the indices will be revealed. We do this by removing the front card from the queue and then moving the next index in the queue to the back. A deque could alternatively be used to simulate this process, but we have chosen to use a queue since we only need to remove cards from the front and add cards to the back.
     *
     * From the sorted order, we can place each card at the correct index to get the desired output:
     *
     * Put card 2 at index 0
     * Put card 3 at index 2
     * Put card 5 at index 4
     * Put card 7 at index 6
     * Put card 11 at index 3
     * Put card 13 at index 1
     * Put card 17 at index 5
     * We can add cards to the result as we simulate the revealing process with the queue. Each time we remove an index from the queue to reveal a card, we add the next card from the deck to the result at that index.
     *
     *
     */
    /**
     * Algorithm
     *
     *
     *  step 1) Initialize N to the length of the deck.
     *
     *  step 2) Create a queue to store the indices of the cards,
     *          and add the indices 0 to N to the queue.
     *
     *  step 3) Sort the deck.
     *
     *  step 4) Initialize an array result of size N to store the answer.
     *
     *  step 5) Loop through the cards, placing each one in the correct spot in result:
     *      - Set result at the front index in the queue to deck[i].
     *      - Take the next index in the queue and move it to the back of the queue.
     *
     * step 6) Return result.
     *
     */
    public int[] deckRevealedIncreasing_1_1(int[] deck) {
        int N = deck.length;
        Queue<Integer> queue = new LinkedList<>();

        // Create a queue of indexes
        /**
         * NOTE !!!
         *
         *  put INDEXES into queue
         */
        for (int i = 0; i < N; i++) {
            queue.add(i);
        }

        Arrays.sort(deck);

        // Put cards at correct index in result
        int[] result = new int[N];
        for (int i = 0; i < N; i++) {
            // Reveal Card and place in result
            result[queue.poll()] = deck[i];

            // Move next card to bottom
            queue.add(queue.poll());
        }
        return result;
    }

    // V1-2
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/reveal-cards-in-increasing-order/editorial/
    public int[] deckRevealedIncreasing_1_2(int[] deck) {
        int N = deck.length;
        int[] result = new int[N];
        boolean skip = false;
        int indexInDeck = 0;
        int indexInResult = 0;

        Arrays.sort(deck);

        while (indexInDeck < N) {
            // There is an available gap in result
            if (result[indexInResult] == 0) {

                // Add a card to result
                if (!skip) {
                    result[indexInResult] = deck[indexInDeck];
                    indexInDeck++;
                }

                // Toggle skip to alternate between adding and skipping cards
                skip = !skip;
            }
            // Progress to next index of result array
            indexInResult = (indexInResult + 1) % N;
        }
        return result;
    }

    // V2
    // https://leetcode.com/problems/reveal-cards-in-increasing-order/solutions/5001469/faster-lesser-detailed-explaination-sorting-deque-step-by-step-explaination-python-java-c/
    public int[] deckRevealedIncreasing_2(int[] deck) {
        Arrays.sort(deck); // Sort the deck in increasing order

        int n = deck.length;
        int[] result = new int[n];
        Deque<Integer> indices = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            indices.add(i); // Initialize deque with indices 0, 1, 2, ..., n-1
        }

        for (int card : deck) {
            int idx = indices.poll(); // Get the next available index
            result[idx] = card; // Place the card in the result array
            if (!indices.isEmpty()) {
                indices.add(indices.poll()); // Move the used index to the end of deque
            }
        }

        return result;
    }

    // V3
    // https://leetcode.com/problems/reveal-cards-in-increasing-order/submissions/1454215743/
    public int[] deckRevealedIncreasing_3(int[] deck) {
        int n=deck.length;
        Arrays.sort(deck);
        Deque <Integer> st=new ArrayDeque<>();
        st.addFirst(deck[n-1]);
        for(int i=n-2;i>=0;i--){
            st.addFirst(st.removeLast());
            st.addFirst(deck[i]);
        }
        //we can either create a new array or change the existing since we dont need it right??but it is not recommended

        for(int i=0;i<n;i++){
            deck[i]=(int)st.removeFirst();
        }
        return deck;
    }

}
