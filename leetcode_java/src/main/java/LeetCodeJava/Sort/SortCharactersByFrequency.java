package LeetCodeJava.Sort;

// https://leetcode.com/problems/sort-characters-by-frequency/description/

import java.util.*;

/**
 * 451. Sort Characters By Frequency
 * Solved
 * N/A
 * Topics
 * premium lock icon
 * Companies
 * Given a string s, sort it in decreasing order based on the frequency of the characters. The frequency of a character is the number of times it appears in the string.
 *
 * Return the sorted string. If there are multiple answers, return any of them.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "tree"
 * Output: "eert"
 * Explanation: 'e' appears twice while 'r' and 't' both appear once.
 * So 'e' must appear before both 'r' and 't'. Therefore "eetr" is also a valid answer.
 * Example 2:
 *
 * Input: s = "cccaaa"
 * Output: "aaaccc"
 * Explanation: Both 'c' and 'a' appear three times, so both "cccaaa" and "aaaccc" are valid answers.
 * Note that "cacaca" is incorrect, as the same characters must be together.
 * Example 3:
 *
 * Input: s = "Aabb"
 * Output: "bbAa"
 * Explanation: "bbaA" is also a valid answer, but "Aabb" is incorrect.
 * Note that 'A' and 'a' are treated as two different characters.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 5 * 105
 * s consists of uppercase and lowercase English letters and digits.
 *
 */
public class SortCharactersByFrequency {

    // V0
//    public String frequencySort(String s) {
//
//    }

    // V1
    // https://leetcode.ca/2017-02-23-451-Sort-Characters-By-Frequency/
    public String frequencySort_1(String s) {
        Map<Character, Integer> cnt = new HashMap<>(52);
        for (int i = 0; i < s.length(); ++i) {
            cnt.merge(s.charAt(i), 1, Integer::sum);
        }
        List<Character> cs = new ArrayList<>(cnt.keySet());
        cs.sort((a, b) -> cnt.get(b) - cnt.get(a));
        StringBuilder ans = new StringBuilder();
        for (char c : cs) {
            for (int v = cnt.get(c); v > 0; --v) {
                ans.append(c);
            }
        }
        return ans.toString();
    }

    // V2
    // IDEA: PQ
    // https://leetcode.com/problems/sort-characters-by-frequency/solutions/4689154/beats-100-users-cjavapythonjavascript-2-ye8s3/
    public String frequencySort_2(String s) {
        Map<Character, Integer> hm = new HashMap<>();

        for (char c : s.toCharArray()) {
            hm.put(c, hm.getOrDefault(c, 0) + 1);
        }

        PriorityQueue<Map.Entry<Character, Integer>> pq = new PriorityQueue<>(
                (a, b) -> b.getValue() - a.getValue());

        pq.addAll(hm.entrySet());

        StringBuilder result = new StringBuilder();
        // TODO: fix below for java 8
//        while (!pq.isEmpty()) {
//            Map.Entry<Character, Integer> entry = pq.poll();
//            result.append(String.valueOf(entry.getKey()).repeat(entry.getValue()));
//        }

        return result.toString();
    }

    // V3-1
    // IDEA: HASHSET + SORT
    // https://leetcode.com/problems/sort-characters-by-frequency/solutions/615846/pythonjava-hashtable-heapsort-easy-to-un-89y3/
    public String frequencySort_3_1(String s) {
        // Count the occurence on each character
        HashMap<Character, Integer> cnt = new HashMap<>();
        for (char c : s.toCharArray()) {
            cnt.put(c, cnt.getOrDefault(c, 0) + 1);
        }

        // Sorting
        List<Character> chars = new ArrayList(cnt.keySet());
        Collections.sort(chars, (a, b) -> (cnt.get(b) - cnt.get(a)));

        // Build string
        StringBuilder sb = new StringBuilder();
        for (Object c : chars) {
            for (int i = 0; i < cnt.get(c); i++) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // V3-2
    // https://leetcode.com/problems/sort-characters-by-frequency/solutions/615846/pythonjava-hashtable-heapsort-easy-to-un-89y3/
    // IDEA: HASHTABLE + PQ
    public String frequencySort_3_2(String s) {
        // Count the occurence on each character
        HashMap<Character, Integer> cnt = new HashMap<>();
        for (char c : s.toCharArray()) {
            cnt.put(c, cnt.getOrDefault(c, 0) + 1);
        }

        // Build heap
        PriorityQueue<Character> heap = new PriorityQueue<>((a, b) -> (cnt.get(b) - cnt.get(a)));
        heap.addAll(cnt.keySet());

        // Build string
        StringBuilder sb = new StringBuilder();
        while (!heap.isEmpty()) {
            char c = heap.poll();
            for (int i = 0; i < cnt.get(c); i++) {
                sb.append(c);
            }
        }
        return sb.toString();
    }


}
