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
    // 8.51 - 10.02 am
    /**
     *  -> return the longest string in the
     *  dictionary that can be formed
     *  by deleting some of the
     *  given string characters.
     *
     *
     *
     *   string s
     *   arr[] dictionary
     *
     *  NOTE:
     *      - If there is more than one possible result,
     *        return the longest word with the
     *        smallest lexicographical order.
     *
     *      - if no result, return ""
     *
     *  -----------
     *
     *   IDEA 1) BRUTE FORCE 2 POINTERS ??
     *    -> collect candidates
     *    -> if a tie,
     *        return longest word with the
     *        smallest lexicographical order
     *
     *   IDEA 2) HASHMAP + 1 PASS ???
     *
     *  -----------
     */
    public String findLongestWord(String s, List<String> dictionary) {
//        if (dictionary.size() == 0 && s != null){
//            return "";
//        }
        // edge
        if (s == null || dictionary == null || dictionary.isEmpty()) {
            return "";
        }

        List<String> list = new ArrayList<>();

        for(String d: dictionary){
            if(canForm(d, s)){
                list.add(d);
            }
        }

        // sort ???
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
//                int diff = o1.compareTo(o2); // ???
//                if(diff == 0){
//                  //  return o1.length() - o2.length();
//                    return o2.length() - o1.length();
//                }
//                return diff;
                if (o1.length() != o2.length()) {
                    return o2.length() - o1.length(); // Longest first
                }
                return o1.compareTo(o2); // Alphabetical tie-breaker
            }
        });



        return list.isEmpty() ? "" : list.get(0);
    }

    private boolean canForm(String x, String s){
        // edge
        if(x.isEmpty()){
            return false;
        }
        if(x.equals(s)){
            return true;
        }
        // ???
        // 2 pointers ???
        int i = 0;
        int j = 0;
        while (i < s.length()){
            if(x.charAt(i) == s.charAt(j)){
                // early quit
                if(j == s.length() - 1){
                    return true;
                }
                // move s's pointer
               // i += 1;
                j += 1;
            }

            // move x's pointer anyway
            i += 1;

        }

        return j == s.length() - 1;
    }










    public String findLongestWord_99(String s, List<String> dictionary) {
        // edge

        // { val : [idx_1, idx_2, ..] }
        Map<String, List<Integer>> map = new HashMap<>();
        int i = 0;
        for(String x: s.split("")){
            if(!map.containsKey(x)){
                map.put(x, new ArrayList<>());
            }
            List<Integer> list = map.get(x);
            list.add(i);
            map.put(x, list);
            //map.put(x, map.getOrDefault(x, 0) + 1);
            i += 1;
        }

        String res = "";

        // ???
        for(String d: dictionary){
            // ???
            if(map.containsKey(d)){
                if(d.length() > res.length()){
                    res = d;
                }
            }

            // ???
        }






        return res;
    }








}
