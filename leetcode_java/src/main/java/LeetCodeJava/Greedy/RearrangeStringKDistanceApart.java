package LeetCodeJava.Greedy;

// https://leetcode.com/problems/rearrange-string-k-distance-apart/description/
// https://leetcode.ca/2016-11-22-358-Rearrange-String-k-Distance-Apart/

import java.util.*;

/**
 * 358. Rearrange String k Distance Apart
 * Given a non-empty string s and an integer k, rearrange the string such that the same characters are at least distance k from each other.
 *
 * All input strings are given in lowercase letters. If it is not possible to rearrange the string, return an empty string "".
 *
 * Example 1:
 *
 * Input: s = "aabbcc", k = 3
 * Output: "abcabc"
 * Explanation: The same letters are at least distance 3 from each other.
 * Example 2:
 *
 * Input: s = "aaabc", k = 3
 * Output: ""
 * Explanation: It is not possible to rearrange the string.
 * Example 3:
 *
 * Input: s = "aaadbbcc", k = 2
 * Output: "abacabcd"
 * Explanation: The same letters are at least distance 2 from each other.
 * Difficulty:
 * Hard
 * Lock:
 * Prime
 * Company:
 * Amazon Facebook Google
 */
public class RearrangeStringKDistanceApart {

    // V0
//    public String rearrangeString(String s, int k) {
//
//    }

    // V0-1
    // IDEA: PQ + LAST IDX CHECK + HASH MAP (fixed by gpt)
    // TODO: validate
    public String rearrangeString_0_1(String s, int k) {
        if (k <= 1) return s;        // no distance constraint
        if (s == null || s.length() == 0) return "";
        if (s.length() == 1) return k == 0 ? s : "";

        // frequency map
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        // Max heap by remaining count
        PriorityQueue<Character> pq =
                new PriorityQueue<>((a, b) -> freq.get(b) - freq.get(a));

        pq.addAll(freq.keySet());

        // Cooldown queue: (char, releaseTime)
        Queue<int[]> cooldown = new LinkedList<>();

        StringBuilder res = new StringBuilder();
        int time = 0;

        while (!pq.isEmpty() || !cooldown.isEmpty()) {

            // if empty PQ but cooldown has locked chars → impossible
            if (pq.isEmpty()) return "";

            char cur = pq.poll();
            res.append(cur);
            freq.put(cur, freq.get(cur) - 1);

            // put into cooldown for k steps
            cooldown.offer(new int[]{cur, time + k});

            time++;

            // release cooldown chars whose time expired
            if (!cooldown.isEmpty() && cooldown.peek()[1] == time) {
                int[] entry = cooldown.poll();
                char ch = (char) entry[0];
                if (freq.get(ch) > 0) {
                    pq.offer(ch);
                }
            }
        }

        return res.toString();
    }

    // V0-2
    // IDEA: PQ + LAST IDX CHECK + HASH MAP (fixed by gpt)
    // TODO: validate
    public String rearrangeString_0_2(String s, int k) {
        if (k <= 1) {
            // If k=0 or k=1, no constraint, return original string.
            return s;
        }

        int n = s.length();

        // 1. Calculate Frequencies
        // Array index 0='a', 1='b', ..., 25='z'
        int[] counts = new int[26];
        for (char c : s.toCharArray()) {
            counts[c - 'a']++;
        }

        // 2. Build Max Heap
        // Stores character indices (0-25). Prioritize based on count (DESCENDING).
        // The comparator reads the count from the external 'counts' array.
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> counts[b] - counts[a]);

        for (int i = 0; i < 26; i++) {
            if (counts[i] > 0) {
                maxHeap.add(i);
            }
        }

        // 3. Cooldown Queue (Size k)
        // Stores [character index, remaining count] pairs that cannot be used yet.
        // It acts as the k-slot "cooldown" window.
        // Size k means the item at the front has been sitting there for k steps.
        Queue<int[]> cooldownQueue = new LinkedList<>();

        // 4. Greedy Rearrangement
        StringBuilder result = new StringBuilder();

        while (!maxHeap.isEmpty()) {
            // Get the most frequent available character index
            int charIndex = maxHeap.poll();

            // Append the character to the result
            result.append((char) ('a' + charIndex));

            // Decrement its count and add it to the cooldown queue
            counts[charIndex]--;
            cooldownQueue.offer(new int[]{charIndex, counts[charIndex]});

            // --- Cooldown Logic ---
            // If the queue has accumulated k elements, the oldest element (at the front)
            // has been out of the heap for k steps and is ready to be re-used.
            if (cooldownQueue.size() == k) {
                int[] readyChar = cooldownQueue.poll();
                int readyIndex = readyChar[0];
                int remainingCount = readyChar[1];

                // If the character still has remaining occurrences, re-add it to the maxHeap
                if (remainingCount > 0) {
                    maxHeap.add(readyIndex);
                }
            }
        }

        // 5. Final Check
        // If the rearrangement failed due to the constraint, the result length will be < n.
        return result.length() == n ? result.toString() : "";
    }


    // V0-3
    // IDEA: PQ (gpt)
    // TODO: validate
    public String rearrangeString_0_3(String s, int k) {
        if (k <= 1) return s;           // no constraints
        if (s == null || s.length() <= 1) return s;

        // frequency count
        int[] freq = new int[26];
        for (char c : s.toCharArray()) {
            freq[c - 'a']++;
        }

        // max heap by remaining frequency
        PriorityQueue<Character> pq = new PriorityQueue<>(
                (a, b) -> freq[b - 'a'] - freq[a - 'a']
        );

        // add all distinct chars into heap
        for (int i = 0; i < 26; i++) {
            if (freq[i] > 0) {
                pq.offer((char) ('a' + i));
            }
        }

        // queue for cooldown: (char, nextTimeToUse)
        Queue<int[]> cooldown = new LinkedList<>();
        StringBuilder res = new StringBuilder();

        int time = 0;
        while (!pq.isEmpty() || !cooldown.isEmpty()) {

            // if heap is empty but cooldown still has values → impossible
            if (pq.isEmpty()) return "";

            char cur = pq.poll();
            res.append(cur);
            freq[cur - 'a']--;

            // put into cooldown with release time = time + k
            cooldown.offer(new int[]{cur, time + k});

            time++;

            // release chars whose cooldown expired
            if (!cooldown.isEmpty() && cooldown.peek()[1] == time) {
                int[] entry = cooldown.poll();
                char ch = (char) entry[0];
                if (freq[ch - 'a'] > 0) {
                    pq.offer(ch);
                }
            }
        }

        return res.toString();
    }

    // V0-4
    // IDEA: PQ (gemini)
    // TODO: validate
    public String rearrangeString_0_4(String s, int k) {
        if (k <= 1) {
            // If k is 0 or 1, no distance constraint exists, so return the original string.
            return s;
        }

        int n = s.length();

        // 1. Calculate Frequencies
        // Array index 0='a', 1='b', ..., 25='z'
        int[] counts = new int[26];
        for (char c : s.toCharArray()) {
            counts[c - 'a']++;
        }

        // 2. Build Max Heap
        // Store character indices (0-25) and prioritize based on count (DESCENDING).
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> counts[b] - counts[a]);

        for (int i = 0; i < 26; i++) {
            if (counts[i] > 0) {
                maxHeap.add(i);
            }
        }

        // 3. Cooldown Queue (Size k-1)
        // Stores {index, count} pairs that cannot be used for the next k-1 steps.
        // We use a LinkedList as a Queue for FIFO operation.
        Queue<int[]> cooldownQueue = new LinkedList<>();

        // 4. Greedy Rearrangement
        StringBuilder result = new StringBuilder();

        while (!maxHeap.isEmpty()) {
            // Get the most frequent available character index
            int charIndex = maxHeap.poll();

            // Append the character to the result
            result.append((char) ('a' + charIndex));

            // Decrement its count and add it to the cooldown queue
            counts[charIndex]--;
            cooldownQueue.offer(new int[]{charIndex, counts[charIndex]});

            // The size of the queue must be k-1 to enforce distance k.
            // After k-1 items are added, the oldest item (head) is ready to exit cooldown.
            if (cooldownQueue.size() == k) {
                int[] readyChar = cooldownQueue.poll();
                int readyIndex = readyChar[0];
                int remainingCount = readyChar[1];

                // If the character still has remaining occurrences, re-add it to the maxHeap
                if (remainingCount > 0) {
                    maxHeap.add(readyIndex);
                }
            }
        }

        // 5. Final Check
        // If the resulting string length equals the original length, rearrangement was successful.
        if (result.length() == n) {
            return result.toString();
        } else {
            // If the heap emptied before the string was completed (due to the distance constraint),
            // it means no valid arrangement exists.
            return "";
        }
    }


    // V1
    // IDEA: PQ
    // https://leetcode.ca/2016-11-22-358-Rearrange-String-k-Distance-Apart/
    public String rearrangeString_1(String s, int k) {
        int n = s.length();
        int[] cnt = new int[26];
        for (char c : s.toCharArray()) {
            ++cnt[c - 'a'];
        }
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[0] - a[0]);
        for (int i = 0; i < 26; ++i) {
            if (cnt[i] > 0) {
                pq.offer(new int[] {cnt[i], i});
            }
        }
        Deque<int[]> q = new ArrayDeque<>();
        StringBuilder ans = new StringBuilder();
        while (!pq.isEmpty()) {
            int[] p = pq.poll();
            int v = p[0], c = p[1];
            ans.append((char) ('a' + c));
            q.offer(new int[] {v - 1, c});
            if (q.size() >= k) {
                p = q.pollFirst();
                if (p[0] > 0) {
                    pq.offer(p);
                }
            }
        }
        return ans.length() == n ? ans.toString() : "";
    }

    // V2

    // V3


}
