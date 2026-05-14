package LeetCodeJava.HashTable;

// https://leetcode.com/problems/tuple-with-same-product/description/

import java.util.*;

/**
 * 1726. Tuple with Same Product
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given an array nums of distinct positive integers, return the number of tuples (a, b, c, d) such that a * b = c * d where a, b, c, and d are elements of nums, and a != b != c != d.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3,4,6]
 * Output: 8
 * Explanation: There are 8 valid tuples:
 * (2,6,3,4) , (2,6,4,3) , (6,2,3,4) , (6,2,4,3)
 * (3,4,2,6) , (4,3,2,6) , (3,4,6,2) , (4,3,6,2)
 * Example 2:
 *
 * Input: nums = [1,2,4,5,10]
 * Output: 16
 * Explanation: There are 16 valid tuples:
 * (1,10,2,5) , (1,10,5,2) , (10,1,2,5) , (10,1,5,2)
 * (2,5,1,10) , (2,5,10,1) , (5,2,1,10) , (5,2,10,1)
 * (2,10,4,5) , (2,10,5,4) , (10,2,4,5) , (10,2,5,4)
 * (4,5,2,10) , (4,5,10,2) , (5,4,2,10) , (5,4,10,2)
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 1 <= nums[i] <= 104
 * All elements in nums are distinct.
 *
 * Seen this question in a real interview before?
 * 1/6
 * Yes
 * No
 * Accepted
 * 199,472/284.6K
 * Acceptance Rate
 * 70.1%
 *
 */
public class TupleWithSameProduct {

    // V0
//    public int tupleSameProduct(int[] nums) {
//
//    }


    // V0-1
    // IDEA: HASHMAP (gpt)
    public int tupleSameProduct_0_1(int[] nums) {

        Map<Integer, Integer> map = new HashMap<>();

        // count pair products
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {

                int product = nums[i] * nums[j];

                map.put(product, map.getOrDefault(product, 0) + 1);
            }
        }

        int ans = 0;

        // if a product appears n times:
        // number of tuple groups = C(n,2)
        // each contributes 8 tuples
        for (int count : map.values()) {

            if (count >= 2) {
                ans += count * (count - 1) / 2 * 8;
            }
        }

        return ans;
    }


    // V0-2
    // IDEA: HASHMAP (gemini)
    public int tupleSameProduct_0_2(int[] nums) {
        // 1. Map to store: Product -> Frequency of that product
        Map<Integer, Integer> productCounts = new HashMap<>();
        int n = nums.length;

        // 2. Generate all possible pairs and count their products
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int product = nums[i] * nums[j];
                productCounts.put(product, productCounts.getOrDefault(product, 0) + 1);
            }
        }

        int totalTuples = 0;

        // 3. For each product that appeared more than once, calculate tuples
        for (int count : productCounts.values()) {
            if (count >= 2) {
                // Combinations of 2 pairs: (count * (count - 1)) / 2
                // Multiply by 8 for all permutations of those 2 pairs
                // Simplified: 4 * count * (count - 1)
                totalTuples += 4 * count * (count - 1);
            }
        }

        return totalTuples;
    }

    

    // V1-1
    // IDEA: Optimized Brute Force
    // https://leetcode.com/problems/tuple-with-same-product/editorial/
    public int tupleSameProduct_1_1(int[] nums) {
        int numsLength = nums.length;
        Arrays.sort(nums);

        int totalNumberOfTuples = 0;

        // Iterate over all possible values for 'a'
        for (int aIndex = 0; aIndex < numsLength; aIndex++) {
            // Iterate over all possible values for 'b', starting from the end
            // of the array
            for (int bIndex = numsLength - 1; bIndex >= aIndex + 1; bIndex--) {
                int product = nums[aIndex] * nums[bIndex];

                // Use a hash set to store possible values of 'd'
                Set<Integer> possibleDValues = new HashSet<>();

                // Iterate over all possible values for 'c' between 'a' and 'b'
                for (int cIndex = aIndex + 1; cIndex < bIndex; cIndex++) {
                    // Check if the product is divisible by nums[cIndex]
                    if (product % nums[cIndex] == 0) {
                        int dValue = product / nums[cIndex];

                        // If 'dValue' is in the set, increment the tuple count
                        // by 8
                        if (possibleDValues.contains(dValue)) {
                            totalNumberOfTuples += 8;
                        }

                        // Add nums[cIndex] to the set for future checks
                        possibleDValues.add(nums[cIndex]);
                    }
                }
            }
        }

        return totalNumberOfTuples;
    }



    // V1-2
    // IDEA: Count Product Frequency
    // https://leetcode.com/problems/tuple-with-same-product/editorial/
    public int tupleSameProduct_1_2(int[] nums) {
        int numsLength = nums.length;

        List<Integer> pairProducts = new ArrayList<>();

        int totalNumberOfTuples = 0;

        // Iterate over nums to calculate all pairwise products
        for (int firstIndex = 0; firstIndex < numsLength; firstIndex++) {
            for (int secondIndex = firstIndex + 1; secondIndex < numsLength; secondIndex++) {
                pairProducts.add(nums[firstIndex] * nums[secondIndex]);
            }
        }

        Collections.sort(pairProducts);

        int lastProductSeen = -1;
        int sameProductCount = 0;

        // Iterate over pairProducts to count how many times each product occurs
        for (int productIndex = 0; productIndex < pairProducts.size(); productIndex++) {
            if (pairProducts.get(productIndex) == lastProductSeen) {
                // Increment the count of same products
                sameProductCount++;
            } else {
                // Calculate how many pairs had the previous product value
                int pairsOfEqualProduct = ((sameProductCount - 1) * sameProductCount) / 2;

                totalNumberOfTuples += 8 * pairsOfEqualProduct;

                // Update lastProductSeen and reset sameProductCount
                lastProductSeen = pairProducts.get(productIndex);
                sameProductCount = 1;
            }
        }

        // Handle the last group of products (since the loop ends without adding
        // it)
        int pairsOfEqualProduct = ((sameProductCount - 1) * sameProductCount) / 2;
        totalNumberOfTuples += 8 * pairsOfEqualProduct;

        return totalNumberOfTuples;
    }




    // V1-3
    // IDEA: Product Frequency Hash Map
    // https://leetcode.com/problems/tuple-with-same-product/editorial/
    public int tupleSameProduct_1_3(int[] nums) {
        int numsLength = nums.length;

        // Initialize a map to store the frequency of each product of pairs
        Map<Integer, Integer> pairProductsFrequency = new HashMap<>();

        int totalNumberOfTuples = 0;

        // Iterate through each pair of numbers in `nums`
        for (int firstIndex = 0; firstIndex < numsLength; firstIndex++) {
            for (int secondIndex = firstIndex + 1; secondIndex < numsLength; secondIndex++) {
                // Increment the frequency of the product of the current pair
                pairProductsFrequency.put(
                        nums[firstIndex] * nums[secondIndex],
                        pairProductsFrequency.getOrDefault(
                                nums[firstIndex] * nums[secondIndex],
                                0) +
                                1);
            }
        }

        // Iterate through each product value and its frequency in the map
        for (int productValue : pairProductsFrequency.keySet()) {
            int productFrequency = pairProductsFrequency.get(productValue);
            // Calculate the number of ways to choose two pairs with the same product
            int pairsOfEqualProduct = ((productFrequency - 1) * productFrequency) / 2;

            // Add the number of tuples for this product to the total (each pair
            // can form 8 tuples)
            totalNumberOfTuples += 8 * pairsOfEqualProduct;
        }

        return totalNumberOfTuples;
    }




    // V2



    // V3





}
