package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/number-of-1-bits/submissions/1264092870/

public class numberOfOneBits {

    // V0
    // IDEA : bit op
    public int hammingWeight(int n) {
        String bin = Integer.toBinaryString(n);
        int res = count_one(bin);
        return res;
    }

    public int count_one(String input){
        int res = 0;
        for (String x : input.split("")){
            //System.out.println("x = " + x);
            if (x.equals("1")) {
                res += 1;
            }
        }
        return res;
    }

}
