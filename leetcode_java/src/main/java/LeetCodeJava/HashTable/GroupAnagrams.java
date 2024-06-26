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

    // V0'
    // IDEA : HASH TABLE
    public List<List<String>> groupAnagrams_0_1(String[] strs) {

        List<List<String>> res = new ArrayList<>();
        if (strs.length == 0){
            List<String> val = new ArrayList<>();
            val.add("");
            res.add(val);
            return res;
        }

        Map<String, List<String>> map = new HashMap<>();
        for (String x : strs){
            /** NOTE !!!
             *
             *  We sort String via below op
             *
             *  step 1) string to char array
             *  step 2) sort char array via "Arrays.sort"
             *  step 3) char array to string (String x_str  = new String(x_array))
             *
             */
            char[] x_array = x.toCharArray();
            Arrays.sort(x_array);
            String x_str  = new String(x_array);
            //System.out.println("x = " + x + " x_str = " + x_str);
            if(!map.containsKey(x_str)){
                List<String> val = new ArrayList<>();
                val.add(x);
                map.put(x_str, val);
            }else{
                List<String> cur = map.get(x_str);
                cur.add(x);
                map.put(x_str, cur);
            }
        }

        //System.out.println("map = " + map);

        for (String k : map.keySet()){
            res.add(map.get(k));
        }

        //System.out.println("res = " + res);

        return res;
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
