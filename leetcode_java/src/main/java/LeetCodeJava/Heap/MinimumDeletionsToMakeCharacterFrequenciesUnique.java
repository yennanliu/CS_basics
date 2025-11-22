package LeetCodeJava.Heap;

// https://leetcode.com/problems/minimum-deletions-to-make-character-frequencies-unique/description/

import java.util.*;

/**
 * 1647. Minimum Deletions to Make Character Frequencies Unique
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * A string s is called good if there are no two different characters in s that have the same frequency.
 *
 * Given a string s, return the minimum number of characters you need to delete to make s good.
 *
 * The frequency of a character in a string is the number of times it appears in the string. For example, in the string "aab", the frequency of 'a' is 2, while the frequency of 'b' is 1.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "aab"
 * Output: 0
 * Explanation: s is already good.
 * Example 2:
 *
 * Input: s = "aaabbbcc"
 * Output: 2
 * Explanation: You can delete two 'b's resulting in the good string "aaabcc".
 * Another way it to delete one 'b' and one 'c' resulting in the good string "aaabbc".
 * Example 3:
 *
 * Input: s = "ceabaacb"
 * Output: 2
 * Explanation: You can delete both 'c's resulting in the good string "eabaab".
 * Note that we only care about characters that are still in the string at the end (i.e. frequency of 0 is ignored).
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 105
 * s contains only lowercase English letters.
 *
 */
public class MinimumDeletionsToMakeCharacterFrequenciesUnique {

    // V0
//    public int minDeletions(String s) {
//
//    }

    // V1-1
    // IDEA: GREEDY
    // https://leetcode.com/problems/minimum-deletions-to-make-character-frequencies-unique/solutions/4033214/9932greedyheapbeginner-friendlyfull-expl-gdkl/
    public int minDeletions_1_1(String s) {
        int[] freq = new int[26]; // Create an array to store character frequencies

        for (char c : s.toCharArray()) {
            freq[c - 'a']++; // Count the frequency of each character
        }

        Arrays.sort(freq); // Sort frequencies in ascending order

        int del = 0; // Initialize the deletion count

        for (int i = 24; i >= 0; i--) {
            if (freq[i] == 0) {
                break; // No more characters with this frequency
            }

            if (freq[i] >= freq[i + 1]) {
                int prev = freq[i];
                freq[i] = Math.max(0, freq[i + 1] - 1);
                del += prev - freq[i]; // Update the deletion count
            }
        }

        return del; // Return the minimum deletions required
    }

    // V1-2
    // IDEA: PQ
    // https://leetcode.com/problems/minimum-deletions-to-make-character-frequencies-unique/solutions/4033214/9932greedyheapbeginner-friendlyfull-expl-gdkl/
    public int minDeletions_1_2(String s) {
        // Create a HashMap to count the frequency of each character.
        Map<Character, Integer> frequencyMap = new HashMap<>();

        // Iterate through the characters in the input string 's'.
        for (char c : s.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        // Create a max-heap (PriorityQueue) to store character frequencies in decreasing order.
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);

        // Populate the max-heap with character frequencies from the map.
        maxHeap.addAll(frequencyMap.values());

        // Initialize a variable to keep track of the minimum number of deletions needed.
        int count = 0;

        // Continue as long as there are at least two frequencies in the max-heap.
        while (maxHeap.size() > 1) {
            int top = maxHeap.poll(); // Get the character frequency with the highest count.

            // Check if the next character in the max-heap has the same frequency as 'top' (and it's not zero).
            if (maxHeap.peek() != null && maxHeap.peek() == top && top != 0) {
                count++; // Increment the deletion count.
                maxHeap.add(top - 1); // Decrease 'top' frequency by 1 and push it back into the max-heap.
            }
        }

        // Return the minimum number of deletions required to make the string "good."
        return count;
    }


    // V2-1
    // IDEA: GREEDY
    // https://leetcode.com/problems/minimum-deletions-to-make-character-frequencies-unique/solutions/4033205/9818-greedy-heap-sorting-by-vanamsen-vqis/
    public int minDeletions_2_1(String s) {
        HashMap<Character, Integer> cnt = new HashMap<>();
        int deletions = 0;
        HashSet<Integer> used_frequencies = new HashSet<>();

        for (char c : s.toCharArray()) {
            cnt.put(c, cnt.getOrDefault(c, 0) + 1);
        }

        for (int freq : cnt.values()) {
            while (freq > 0 && used_frequencies.contains(freq)) {
                freq--;
                deletions++;
            }
            used_frequencies.add(freq);
        }

        return deletions;
    }


    // V2-2
    // IDEA: PQ
    // https://leetcode.com/problems/minimum-deletions-to-make-character-frequencies-unique/solutions/4033205/9818-greedy-heap-sorting-by-vanamsen-vqis/


    // V3
    // https://leetcode.com/problems/minimum-deletions-to-make-character-frequencies-unique/solutions/927654/cjavapython3-simple-time-on-space-o1-a-s-9xa8/
    public int minDeletions_3(String s) {
        int freq[] = new int[26];
        for (char c : s.toCharArray())
            freq[c - 'a']++;
        Arrays.sort(freq);
        int keep = freq[25], prev = keep;
        for (int i = 24; i >= 0 && freq[i] != 0 && prev != 0; i--) {
            prev = Math.min(freq[i], prev - 1);
            keep += prev;
        }
        return s.length() - keep;
    }



}
