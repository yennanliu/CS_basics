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


    // LC 165
    // 8.11 - 21 am
    /**
     * ->
     *  Return the following:
     *
     * If version1 < version2, return -1.
     * If version1 > version2, return 1.
     * Otherwise, return 0.
     *
     * -------------
     *
     *  IDEA 1) string op (split) + compare
     *
     *
     * -------------
     *
     *
     *
     */
    // IDEA 1) string op (split) + compare
    // time: O(N)  // ????
    // space: O(N)
    public int compareVersion(String version1, String version2) {
        // edge
        if (version1.equals(version2)) {
            return 0;
        }

        List<Integer> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();

        for(String x: version1.split("\\.")){
            System.out.println(" x = " + x);
            if(!x.equals("0")){
                if(x.startsWith("0")){
                    l1.add(Integer.parseInt(removeZeroPrefix(x)));
                }
               // l1.add(Integer.parseInt(removeZeroPrefix(x)));
            }
        }

        for(String x: version2.split("\\.")){
            if(!x.equals("0")){
                if(x.startsWith("0")){
                    l2.add(Integer.parseInt(removeZeroPrefix(x)));
                }
            }
        }

        System.out.println(">>> l1 = " + l1 +
        ", l2 = " + l2);

        int size = Math.min(l1.size(), l2.size());
        for(int j = 0; j < size; j++){
            if(l1.get(j) > l2.get(j)){
                return 1;
            }else{
                return -1;
            }
        }


        return 0;
    }

    private String removeZeroPrefix(String x){
        // ???
//        String[] arr = x.split("0");
//        int idx = arr[0].length();
        int idx = 0;
        for(String str: x.split("")){
            if(!str.equals("0")){
                return x.substring(idx);
            }
            idx += 1;
        }
      // return x.substring(idx + 1); //???
        return x;
    }


    // LC 524
    // 8.51 - 9.01 am
    /**
     *
     *
     */
    public String findLongestWord(String s, List<String> dictionary) {

        return null;
    }








}
