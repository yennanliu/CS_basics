package LeetCodeJava.HashTable;

// https://leetcode.com/problems/group-anagrams/

import java.util.*;

public class GroupAnagrams {

    // V0 : IDEA : HASH MAP
    public List<List<String>> groupAnagrams(String[] strs) {

        List<List<String>> res = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();

        if (strs.length == 0 || strs.equals(null)) {
            return res;
        }

        for (int i = 0; i < strs.length; i++){
            String cur = strs[i];
            String curSorted = sortString(cur);
            List<String> strList = null;
            if (!map.containsKey(curSorted)){
                strList = new ArrayList<>();
                strList.add(cur);
            }else{
                strList = (List<String>) map.get(curSorted);
                strList.add(cur);
            }
            map.put(curSorted, strList);
        }

        for (String key: map.keySet()){
            List<String> cur = (List<String>) map.get(key);
            res.add(cur);
        }

        return res;
    }

    // https://www.scaler.com/topics/sort-a-string-in-java/
    private String sortString(String strs){

        if (strs.length() == 0 || strs.equals(null)) {
            return null;
        }

        char array [] = strs.toCharArray();
        Arrays.sort(array);
        return String.valueOf(array);
    }

    // V1
    // IDEA : Categorize by Sorted String
    // https://leetcode.com/problems/group-anagrams/editorial/
    public List<List<String>> groupAnagrams_2(String[] strs) {
        if (strs.length == 0) return new ArrayList();
        Map<String, List> ans = new HashMap<String, List>();
        for (String s : strs) {
            char[] ca = s.toCharArray();
            Arrays.sort(ca);
            String key = String.valueOf(ca);
            if (!ans.containsKey(key)) ans.put(key, new ArrayList());
            ans.get(key).add(s);
        }
        return new ArrayList(ans.values());
    }

    // V2
    // IDEA : Categorize by Count
    // https://leetcode.com/problems/group-anagrams/editorial/
    public List<List<String>> groupAnagrams_3(String[] strs) {
        if (strs.length == 0) return new ArrayList();
        Map<String, List> ans = new HashMap<String, List>();
        int[] count = new int[26];
        for (String s : strs) {
            Arrays.fill(count, 0);
            for (char c : s.toCharArray()) count[c - 'a']++;

            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < 26; i++) {
                sb.append('#');
                sb.append(count[i]);
            }
            String key = sb.toString();
            if (!ans.containsKey(key)) ans.put(key, new ArrayList());
            ans.get(key).add(s);
        }
        return new ArrayList(ans.values());
    }

}
