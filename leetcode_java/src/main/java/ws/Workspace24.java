package ws;

import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

public class Workspace24 {

    // LC 953
    // 7.22 - 32 am
    /**
     *  ->  return true if and only
     *  if the given words are sorted
     *  lexicographically in this alien language.
     *
     *  permutation: 排列
     *
     *
     *
     *  ---------------
     *
     *  IDEA 1) HASHMAP + BRUTE FORCE ???
     *
     *   { val : idx }
     *   -> {'a': 1, 'b': 2, .....}
     *
     *   -> map word to above
     *   -> sort, compare to the original input, check if they are the same.
     *
     *
     *  ---------------
     *
     */
    public boolean isAlienSorted(String[] words, String order) {
        // edge

        // V2
        // 1. Map each character to its alien rank for O(1) lookup
        int[] alienOrder = new int[26];
        for (int i = 0; i < order.length(); i++) {
            alienOrder[order.charAt(i) - 'a'] = i;
        }


        Map<Character, Integer> orderMap = new HashMap<>();
        int i = 0;
        for(char ch: order.toCharArray()){
            orderMap.put(ch, i);
            i += 1;
        }

        // cache original words
        String[] words2 = words; // ???

        // map
        // ????
        Arrays.sort(words, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int diff = orderMap.get(o1) - orderMap.get(o2);
                // ????
                if(diff == 0){
                    return o1.length() - o2.length();
                }
                return diff;
            }
        });

        for(int j = 0; j < words.length; j++){
            if(!words[j].equals(words2[j])){
                return false;
            }
        }


        return true;
    }








}
