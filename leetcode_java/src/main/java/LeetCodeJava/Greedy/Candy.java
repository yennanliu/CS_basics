package LeetCodeJava.Greedy;

// https://leetcode.com/problems/candy/

import java.util.Arrays;

public class Candy {

    // V0
    // IDEA : GREEDY
    // https://www.bilibili.com/video/BV1ev4y1r7wN/?share_source=copy_web&vd_source=49348a1975630e1327042905e52f488a
    // TODO : fix it
//    public int candy(int[] ratings) {
//
//        if (ratings == null || ratings.length == 0){
//            return 0;
//        }
//
//        int ans = 0;
//        int[] tmp = new int[]{};
//        for (int i = 0; i < ratings.length; i++){
//            tmp[i] = 1;
//        }
//
//        /** TIPS : DEAL WITH 1 DIRECTION AT ONCE */
//
//        // step 1) loop from left (compare element left)
//        for (int j = 1; j < ratings.length-1; j++){
//            if (ratings[j] > ratings[j-1]){
//                tmp[j] += 1;
//            }
//        }
//
//        // step 2) loop from right
//        for (int k = ratings.length-2; k >= 0; k--){
//            if (ratings[k] > ratings[k+1]){
//                tmp[k] += 1;
//            }
//        }
//
//        // step 3) get sum
//        for (int z=0; z < tmp.length; z ++){
//            ans += tmp[z];
//        }
//
//        return ans;
//    }


    // V1
    // IDEA : BRUTE FORCE
    // https://leetcode.com/problems/candy/editorial/
    public int candy_2(int[] ratings) {
        int[] candies = new int[ratings.length];
        Arrays.fill(candies, 1);
        boolean hasChanged = true;
        while (hasChanged) {
            hasChanged = false;
            for (int i = 0; i < ratings.length; i++) {
                if (i != ratings.length - 1 && ratings[i] > ratings[i + 1] && candies[i] <= candies[i + 1]) {
                    candies[i] = candies[i + 1] + 1;
                    hasChanged = true;
                }
                if (i > 0 && ratings[i] > ratings[i - 1] && candies[i] <= candies[i - 1]) {
                    candies[i] = candies[i - 1] + 1;
                    hasChanged = true;
                }
            }
        }
        int sum = 0;
        for (int candy : candies) {
            sum += candy;
        }
        return sum;
    }

    // V2
    // IDEA :  Using two arrays
    // https://leetcode.com/problems/candy/editorial/
    public int candy_3(int[] ratings) {
        int sum = 0;
        int[] left2right = new int[ratings.length];
        int[] right2left = new int[ratings.length];
        Arrays.fill(left2right, 1);
        Arrays.fill(right2left, 1);
        for (int i = 1; i < ratings.length; i++) {
            if (ratings[i] > ratings[i - 1]) {
                left2right[i] = left2right[i - 1] + 1;
            }
        }
        for (int i = ratings.length - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                right2left[i] = right2left[i + 1] + 1;
            }
        }
        for (int i = 0; i < ratings.length; i++) {
            sum += Math.max(left2right[i], right2left[i]);
        }
        return sum;
    }

    // V3
    // IDEA : Using one array
    // https://leetcode.com/problems/candy/editorial/
    public int candy_4(int[] ratings) {
        int[] candies = new int[ratings.length];
        Arrays.fill(candies, 1);
        for (int i = 1; i < ratings.length; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            }
        }
        int sum = candies[ratings.length - 1];
        for (int i = ratings.length - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = Math.max(candies[i], candies[i + 1] + 1);
            }
            sum += candies[i];
        }
        return sum;
    }

    // V4
    // IDEA : Single Pass Approach with Constant Space
    // https://leetcode.com/problems/candy/editorial/
    public int count(int n) {
        return (n * (n + 1)) / 2;
    }
    public int candy_5(int[] ratings) {
        if (ratings.length <= 1) {
            return ratings.length;
        }
        int candies = 0;
        int up = 0;
        int down = 0;
        int oldSlope = 0;
        for (int i = 1; i < ratings.length; i++) {
            int newSlope = (ratings[i] > ratings[i - 1]) ? 1
                    : (ratings[i] < ratings[i - 1] ? -1
                    : 0);

            if ((oldSlope > 0 && newSlope == 0) || (oldSlope < 0 && newSlope >= 0)) {
                candies += count(up) + count(down) + Math.max(up, down);
                up = 0;
                down = 0;
            }
            if (newSlope > 0) {
                up++;
            } else if (newSlope < 0) {
                down++;
            } else {
                candies++;
            }

            oldSlope = newSlope;
        }
        candies += count(up) + count(down) + Math.max(up, down) + 1;
        return candies;
    }

}
