package LeetCodeJava.HashTable;

// https://leetcode.com/problems/strobogrammatic-number/

import java.util.HashMap;
import java.util.Map;

public class StrobogrammaticNumber {

    // V0

    // V1
    // https://leetcode.com/problems/strobogrammatic-number/solutions/67188/4-lines-in-java/
    public boolean isStrobogrammatic(String num) {
        for (int i=0, j=num.length()-1; i <= j; i++, j--)
            if (!"00 11 88 696".contains(num.charAt(i) + "" + num.charAt(j)))
                return false;
        return true;
    }

    // V2
    // https://leetcode.com/problems/strobogrammatic-number/solutions/3350263/java-simple-solution/
    public boolean isStrobogrammatic_2(String num) {
        Map<Character, Character> map = new HashMap<Character, Character>();
        map.put('6', '9');
        map.put('9', '6');
        map.put('0', '0');
        map.put('1', '1');
        map.put('8', '8');

        // NOTE : use 2 pointers here
        int l = 0, r = num.length() - 1;
        while (l <= r) {
            if (!map.containsKey(num.charAt(l)))
                return false;
            if (map.get(num.charAt(l)) != num.charAt(r))
                return false;
            l++;
            r--;
        }

        return true;
    }

}
