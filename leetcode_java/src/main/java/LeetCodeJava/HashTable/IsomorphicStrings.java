package LeetCodeJava.HashTable;

// https://leetcode.com/problems/isomorphic-strings/

import java.util.HashMap;

public class IsomorphicStrings {

    public boolean isIsomorphic(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        // NOTE : we have to do both case
        return check(s, t) && check(t, s);
    }

    private boolean check(String s, String t){
        HashMap<String, String> map = new HashMap();
        for (int i = 0; i < s.length(); i++){
            String sValue = String.valueOf(s.charAt(i));
            String tValue = String.valueOf(t.charAt(i));
            if (!map.containsKey(sValue)){
                map.put(sValue, tValue);
            }else{
                if (! tValue.equals(map.get(sValue))){
                    //System.out.println("tValue =  " + tValue + " map.get(sValue) = " + map.get(sValue));
                    return false;
                }
            }
        }
        return true;
    }

}
