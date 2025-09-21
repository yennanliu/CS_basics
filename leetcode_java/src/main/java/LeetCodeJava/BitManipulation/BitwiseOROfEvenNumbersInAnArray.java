package LeetCodeJava.BitManipulation;

import java.util.ArrayList;
import java.util.List;

public class BitwiseOROfEvenNumbersInAnArray {

    // V0
    // IDEA: BIT OP
    // https://leetcode.com/contest/weekly-contest-468/problems/bitwise-or-of-even-numbers-in-an-array/description/
    public int evenNumberBitwiseORs(int[] nums) {
        // edge
        if (nums.length == 0) {
            return 0;
        }
        List<Integer> list = new ArrayList<>();
        for (int x : nums) {
            if (x % 2 == 0) {
                list.add(x);
            }
        }
        if (list.isEmpty()) {
            return 0;
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        int tmp = 0;
        for (int i = 0; i < list.size(); i++) {
            // tmp = tmp | (list.get(i) | list.get(i+1));
            tmp = (tmp | list.get(i));
        }

        return tmp;
    }

    // V1
    // https://leetcode.com/problems/bitwise-or-of-even-numbers-in-an-array/solutions/7209597/clean-easy-5-lines-beats-100-easy-explan-w9qu/
    public int evenNumberBitwiseORs_1(int[] nums) {
        int count = 0;
        for (int num : nums) {
            if (num % 2 == 0) {
                count |= num;
            }
        }
        return count;
    }


}
