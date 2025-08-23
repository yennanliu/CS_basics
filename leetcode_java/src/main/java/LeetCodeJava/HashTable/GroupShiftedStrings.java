package LeetCodeJava.HashTable;

// https://leetcode.com/problems/group-shifted-strings/
/**
 * 249. Group Shifted Strings
 * Given a string, we can "shift" each of its letter to its successive letter, for example: "abc" -> "bcd". We can keep "shifting" which forms the sequence:
 *
 * "abc" -> "bcd" -> ... -> "xyz"
 * Given a list of strings which contains only lowercase alphabets, group all strings that belong to the same shifting sequence.
 *
 * Example:
 *
 * Input: ["abc", "bcd", "acef", "xyz", "az", "ba", "a", "z"],
 * Output:
 * [
 *   ["abc","bcd","xyz"],
 *   ["az","ba"],
 *   ["acef"],
 *   ["a","z"]
 * ]
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Facebook Google Uber
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class GroupShiftedStrings {

    // V0

    // V1
    // https://leetcode.ca/2016-08-05-249-Group-Shifted-Strings/
    public List<List<String>> groupStrings_1(String[] strings) {
        Map<String, List<String>> mp = new HashMap<>();
        for (String s : strings) {
            int diff = s.charAt(0) - 'a';
            char[] t = s.toCharArray();
            for (int i = 0; i < t.length; ++i) {
                char d = (char) (t[i] - diff);
                if (d < 'a') {
                    d += 26;
                }
                t[i] = d;
            }
            String key = new String(t);
            mp.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }
        return new ArrayList<>(mp.values());
    }

    // V2
    // https://leetcode.com/problems/group-shifted-strings/solutions/799563/java-10-hash-map-and-explainations/
    public List<List<String>> groupStrings_2(String[] strings) {
        Map<String,List<String>> group_map = new HashMap<>();
        for(String s: strings){
            StringBuilder key  = new StringBuilder();
            for(int i = 0; i < s.length();i++){
                int relative_distance = (s.charAt(i) - s.charAt(0) + 26) % 26;
                // in case [0,1,11] [0,11,1], so i add "." to key.
                key.append(".");
                key.append(Integer.toString(relative_distance));
            }
            String k = key.toString();
            if(!group_map.containsKey(k)){
                List<String> value = new ArrayList<>();
                group_map.put(k,value);
            }
            group_map.get(k).add(s);
        }
        System.out.println(group_map);

        List<List<String>> output = new ArrayList<>();
        for(String key: group_map.keySet()){
            List<String> value = new ArrayList<>();
            value = group_map.get(key);
            output.add(value);
        }
        return output;
    }

    // V3
    // https://leetcode.com/problems/group-shifted-strings/solutions/799563/java-10-hash-map-and-explainations/
    public List<List<String>> groupStrings_3(String[] strs) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        for(String str: strs) {
            String key = getKey(str);
            List<String> temp = new ArrayList<String>();
            if(map.containsKey(key)) {
                temp = map.get(key);
            }
            temp.add(str);
            map.put(key, temp);
        }
        List<List<String>> result = new ArrayList(map.values());//This is good
        return result;
    }

    private String getKey(String strs) {
        if(strs.length() == 1) return "-,";
        int[] diffs = new int[strs.length() - 1];
        char[] chs = strs.toCharArray();
        for(int i = 1; i < chs.length; i++) {
            int diff = chs[i] - chs[i - 1];
            if(diff < 0) diff += 26;
            diffs[i - 1] = diff;
        }
        StringBuilder sb = new StringBuilder();
        for(int num: diffs) {
            sb.append(num + ",");
        }
        return sb.toString();
    }

}