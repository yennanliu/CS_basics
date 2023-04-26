package LeetCodeJava.HashTable;

// https://leetcode.com/problems/happy-number/

import java.util.HashSet;
import java.util.Set;

public class HappyNumber {

    public boolean isHappy(int n) {

        Set<Integer> seen = new HashSet<>();

        while(n != 1 && !seen.contains(n)){
            seen.add(n);
            System.out.println("n = " + n);
            n = getSquareSum(n);
        }
        return n == 1;
    }

    private int getSquareSum(int input){
        int res = 0;
        while (input != 0){
            int tmp = input % 10;
            res += tmp * tmp;
            input = input / 10;
        }
        return res;
    }

}
