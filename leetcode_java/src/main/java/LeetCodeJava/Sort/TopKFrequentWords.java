package LeetCodeJava.Sort;

// https://leetcode.com/problems/top-k-frequent-words/description/

import java.util.*;

/**
 * 692. Top K Frequent Words
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an array of strings words and an integer k, return the k most frequent strings.
 *
 * Return the answer sorted by the frequency from highest to lowest. Sort the words with the same frequency by their lexicographical order.
 *
 *
 *
 * Example 1:
 *
 * Input: words = ["i","love","leetcode","i","love","coding"], k = 2
 * Output: ["i","love"]
 * Explanation: "i" and "love" are the two most frequent words.
 * Note that "i" comes before "love" due to a lower alphabetical order.
 * Example 2:
 *
 * Input: words = ["the","day","is","sunny","the","the","the","sunny","is","is"], k = 4
 * Output: ["the","is","sunny","day"]
 * Explanation: "the", "is", "sunny" and "day" are the four most frequent words, with the number of occurrence being 4, 3, 2 and 1 respectively.
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 500
 * 1 <= words[i].length <= 10
 * words[i] consists of lowercase English letters.
 * k is in the range [1, The number of unique words[i]]
 *
 *
 * Follow-up: Could you solve it in O(n log(k)) time and O(n) extra space?
 *
 */
public class TopKFrequentWords {

    // V0
    // IDEA: sort map on value and key length (gpt)
    // TODO: validate & fix
//    public List<String> topKFrequent(String[] words, int k) {
//
//        // edge
//        if (words.length == 0) {
//            return new ArrayList<>();
//        }
//
//        List<String> res = new ArrayList<>();
//        Map<String, Integer> map = new HashMap<>();
//        // biggest queue
//        // Queue<Integer> cntQueue = new LinkedList<>();
//
//        for (String x : words) {
//
//            map.putIfAbsent(x, 1);
//            Integer cur = map.get(x);
//            map.put(x, cur + 1);
//        }
//
//        System.out.println(">>> (before sort) map = " + map);
//
//        // sort map by value and key lenth
//        // Convert the map to a list of entries
//        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());
//
//        // Sort the entry list
//        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
//            @Override
//            public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
//                // First, compare by value in decreasing order
//                int valueCompare = entry2.getValue().compareTo(entry1.getValue());
//
//                // If values are equal, compare by key length in increasing order
//                if (valueCompare == 0) {
//                    return entry1.getKey().length() - entry2.getKey().length();
//                }
//
//                return valueCompare;
//            }
//        });
//
//        System.out.println(">>> (after sort) map = " + map);
//        for (Map.Entry<String, Integer> x : entryList) {
//            if (k == 0){
//                break;
//            }
//            res.add((String) x.getKey());
//            k -= 1;
//        }
//
//        return res;
//    }

    // V1
    // V2
}
