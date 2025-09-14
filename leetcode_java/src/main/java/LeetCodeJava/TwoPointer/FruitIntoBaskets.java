package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/fruit-into-baskets/description/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *  904. Fruit Into Baskets
 * Medium
 * Topics
 * Companies
 * You are visiting a farm that has a single row of fruit trees arranged from left to right. The trees are represented by an integer array fruits where fruits[i] is the type of fruit the ith tree produces.
 *
 * You want to collect as much fruit as possible. However, the owner has some strict rules that you must follow:
 *
 * You only have two baskets, and each basket can only hold a single type of fruit. There is no limit on the amount of fruit each basket can hold.
 * Starting from any tree of your choice, you must pick exactly one fruit from every tree (including the start tree) while moving to the right. The picked fruits must fit in one of your baskets.
 * Once you reach a tree with fruit that cannot fit in your baskets, you must stop.
 * Given the integer array fruits, return the maximum number of fruits you can pick.
 *
 *
 *
 * Example 1:
 *
 * Input: fruits = [1,2,1]
 * Output: 3
 * Explanation: We can pick from all 3 trees.
 * Example 2:
 *
 * Input: fruits = [0,1,2,2]
 * Output: 3
 * Explanation: We can pick from trees [1,2,2].
 * If we had started at the first tree, we would only pick from trees [0,1].
 * Example 3:
 *
 * Input: fruits = [1,2,3,2,2]
 * Output: 4
 * Explanation: We can pick from trees [2,3,2,2].
 * If we had started at the first tree, we would only pick from trees [1,2].
 *
 *
 * Constraints:
 *
 * 1 <= fruits.length <= 105
 * 0 <= fruits[i] < fruits.length
 *
 *
 */
public class FruitIntoBaskets {

    // V0
    // IDEA: slide window
    /**
     *  int l = 0;
     *
     *  for(int r = 0; r < nums.length(); r++){
     *      if(isValid()){
     *       // do sth
     *       l += 1;
     *  }
     *    // do sth
     */
    public int totalFruit(int[] fruits) {
        // edge
        if (fruits.length <= 2) {
            return fruits.length; // ??
        }
        HashSet<Integer> set = new HashSet<>();
        for (int x : fruits) {
            set.add(x);
        }
        if (set.size() <= 2) {
            return fruits.length;
        }

        int ans = 0;
        // slide window
        /**
         *  int l = 0;
         *
         *  for(int r = 0; r < nums.length(); r++){
         *      if(isValid()){
         *       // do sth
         *       l += 1;
         *  }
         *    // do sth
         */
        int l = 0;
        // {val: cnt}
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int r = 0; r < fruits.length; r++) {

            int rightVal = fruits[r];
            map.put(rightVal, map.getOrDefault(rightVal, 0) + 1);

            while (map.size() > 2) {
                int leftVal = fruits[l];
                map.put(leftVal, map.getOrDefault(leftVal, 0) - 1);
                if (map.get(leftVal) == 0) {
                    map.remove(leftVal);
                }
                l += 1;
            }

            ans = Math.max(ans, r - l + 1);

        }

        return ans;
    }


    // V0-1
    // IDEA: 2 POINTERS (gpt)
    public int totalFruit_0_1(int[] fruits) {
        if (fruits.length == 0) {
            return 0;
        }

        int res = 0;
        int l = 0; // left pointer
        Map<Integer, Integer> fruitCount = new HashMap<>();

        // Iterate with the right pointer
        for (int r = 0; r < fruits.length; r++) {
            // Add current fruit to the map and increment its count
            fruitCount.put(fruits[r], fruitCount.getOrDefault(fruits[r], 0) + 1);

            // If we have more than 2 types of fruits, move the left pointer
            while (fruitCount.size() > 2) {
                fruitCount.put(fruits[l], fruitCount.get(fruits[l]) - 1);
                if (fruitCount.get(fruits[l]) == 0) {
                    fruitCount.remove(fruits[l]);
                }
                l++; // Move the left pointer
            }

            // Update the result (window size)
            res = Math.max(res, r - l + 1);
        }

        return res;
    }

    // V0-2
    // IDEA: SLIDE WINDOW (gpt)
    public int totalFruit_0_2(int[] fruits) {
        // edge case
        if (fruits == null || fruits.length == 0) {
            return 0;
        }
        if (fruits.length <= 2) {
            return fruits.length;
        }

        int ans = 0;
        int l = 0;
        Map<Integer, Integer> map = new HashMap<>(); // fruit -> count

        for (int r = 0; r < fruits.length; r++) {
            int rightVal = fruits[r];
            map.put(rightVal, map.getOrDefault(rightVal, 0) + 1);

            // shrink window if more than 2 fruit types
            while (map.size() > 2) {
                int leftVal = fruits[l];
                map.put(leftVal, map.get(leftVal) - 1);
                if (map.get(leftVal) == 0) {
                    map.remove(leftVal);
                }
                l++;
            }

            ans = Math.max(ans, r - l + 1);
        }

        return ans;
    }


    // V1
    // https://leetcode.com/problems/fruit-into-baskets/solutions/3153809/clean-codes-full-explanation-hashtable-c-dd57/
    public int totalFruit_1(int[] tree) {
        int ans = 0;
        Map<Integer, Integer> count = new HashMap<>();

        for (int l = 0, r = 0; r < tree.length; ++r) {
            count.put(tree[r], count.getOrDefault(tree[r], 0) + 1);
            while (count.size() > 2) {
                count.put(tree[l], count.get(tree[l]) - 1);
                count.remove(tree[l], 0);
                ++l;
            }
            ans = Math.max(ans, r - l + 1);
        }

        return ans;
    }

    // V2
    // https://leetcode.com/problems/fruit-into-baskets/solutions/3153492/easy-explanation-with-animation-and-vide-mo3t/
    public int totalFruit_2(int[] fruits) {
        Map<Integer, Integer> basket = new HashMap<>();
        int j = 0;
        int i = 0;
        int res = 0;
        for (i = 0; i < fruits.length; i++) {
            basket.put(fruits[i], basket.getOrDefault(fruits[i], 0) + 1);
            while (basket.size() > 2) {
                basket.put(fruits[j], basket.get(fruits[j]) - 1);
                basket.remove(fruits[j], 0);
                j++;
            }
            res = Math.max(res, i - j + 1);
        }
        return res;
    }

    // V3
    // https://leetcode.com/problems/fruit-into-baskets/solutions/3153971/crystal-clear-explanation-c-java-by-myst-73x0/
    public int totalFruit_3(int[] fruits) {
        // Initializing a map for keeping count of distinct fruits
        Map<Integer, Integer> fruitCount = new HashMap<>();

        // i is front pointer of sliding window
        // j is rear pointer of sliding window
        // ans will store the maximum subarray length
        int i = 0, j = 0, ans = 0;
        while (i < fruits.length) {
            // picking up the fruit
            fruitCount.put(fruits[i], fruitCount.getOrDefault(fruits[i], 0) + 1);
            // if no. of distinct fruits is more than two
            // we will move our rear pointer and unpick the fruits
            while (fruitCount.size() > 2) {
                int count = fruitCount.get(fruits[j]) - 1;
                fruitCount.put(fruits[j], count);
                // if fuit of a particular type are exhausted
                // clearing the index of that fruit
                if (count == 0) {
                    fruitCount.remove(fruits[j]);
                }
                // moving rear pointer
                j++;
            }
            // Storing maximum subarray length with at most 2 distinct fruits
            ans = Math.max(ans, i - j + 1);
            // moving front pointer
            i++;
        }
        // returning the maximum subarray length
        return ans;
    }


}
