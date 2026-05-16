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
    // IDEA: HASHMAP + MATH
    /** NOTE !!!
     *
     *  map : { product_of_two_numbers: pair_cnt }
     *
     *
     *  e.g.
     *
     *   // product -> number of pairs producing that product
     *
     */
    public int tupleSameProduct(int[] nums) {

        Map<Integer, Integer> map = new HashMap<>();

        // count pair products
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {

                int product = nums[i] * nums[j];

                map.put(product, map.getOrDefault(product, 0) + 1);
            }
        }

        int res = 0;

        // for each product frequency
        for (int cnt : map.values()) {

            /** NOTE !!!
             *
             *  `when` there is a `tuple with same product` exists
             */
            if (cnt >= 2) {

                // choose 2 pairs
                // choose ANY 2 pairs from "count" pairs
                //
                // combination formula:
                // C(n,2) = n * (n - 1) / 2
                //
                 /**
                  *     // if a product appears k times,
                  *     // choose any 2 pairs: C(k,2)
                  */
                int combinations = cnt * (cnt - 1) / 2;

                // each combination contributes 8 tuples
                res += combinations * 8;
            }
        }

        return res;
    }


    // V0-1
    // IDEA: HASHMAP + MATH (gpt)
    /** NOTE !!!
     *
     *  map : { product_of_two_numbers: pair_cnt }
     *
     */
    /** Core idea:
     *
     * The key idea:
     *
     * * Compute every `pair product`
     * * Count `how many times` each product appears
     * * If a product appears `n` times, then:
     *
     *   * choose any 2 pairs: `C(n,2)`
     *   * each pair combination creates `8` tuples
     *
     *   !!!
     *
     *
     * Why `8`?
     * Because `(a,b,c,d)` has permutations:
     *
     * * `(a,b,c,d)`
     * * `(b,a,c,d)`
     * * `(a,b,d,c)`
     * * etc.
     *
     * -------
     *
     * Example:
     *
     * ```text
     * nums = [2,3,4,6]
     *
     * 2*6 = 12
     * 3*4 = 12
     * ```
     *
     * Two pairs share same product:
     *
     * * `(2,6)`
     * * `(3,4)`
     *
     * They generate 8 tuples.
     *
     */
    /**  Dry run:
     *
     * Let's begin. Here's the dry run for LeetCode 1726.
     *
     * ---
     *
     * # Input
     *
     * ```java
     * nums = [2, 3, 4, 6]
     * ```
     * ---
     *
     * # Step 1 — Generate All Pairs
     *
     * We iterate through every unique pair.
     *
     * ---
     *
     * # Pair Computation Table
     *
     * | Pair Index | nums[i] | nums[j] | Product | productCount Map After Update  |
     * | ---------- | ------- | ------- | ------- | ------------------------------ |
     * | (0,1)      | 2       | 3       | 6       | `{6=1}`                        |
     * | (0,2)      | 2       | 4       | 8       | `{6=1, 8=1}`                   |
     * | (0,3)      | 2       | 6       | 12      | `{6=1, 8=1, 12=1}`             |
     * | (1,2)      | 3       | 4       | 12      | `{6=1, 8=1, 12=2}`             |
     * | (1,3)      | 3       | 6       | 18      | `{6=1, 8=1, 12=2, 18=1}`       |
     * | (2,3)      | 4       | 6       | 24      | `{6=1, 8=1, 12=2, 18=1, 24=1}` |
     *
     * ---
     *
     * # Final HashMap
     *
     * ```java
     * {
     *   6=1,
     *   8=1,
     *   12=2,
     *   18=1,
     *   24=1
     * }
     * ```
     *
     * Meaning:
     *
     * ```text
     * Product 12 appears twice:
     * (2,6)
     * (3,4)
     * ```
     *
     * ---
     *
     * # Step 2 — Count Valid Tuple Groups
     *
     * We iterate through map frequencies.
     *
     * ---
     *
     * # Frequency Processing Table
     *
     * | Product | Frequency (count) | Formula C(n,2)   | Pair Combinations | Tuples Added |
     * | ------- | ----------------- | ---------------- | ----------------- | ------------ |
     * | 6       | 1                 | Not enough pairs | 0                 | 0            |
     * | 8       | 1                 | Not enough pairs | 0                 | 0            |
     * | 12      | 2                 | `2 * (2-1) / 2`  | 1                 | `1 * 8 = 8`  |
     * | 18      | 1                 | Not enough pairs | 0                 | 0            |
     * | 24      | 1                 | Not enough pairs | 0                 | 0            |
     *
     * ---
     *
     * # Running Answer Table
     *
     * | Step       | Product | Tuples Added | Running ans |
     * | ---------- | ------- | ------------ | ----------- |
     * | Initial    | -       | 0            | 0           |
     * | Product 6  | 0       | 0            | 0           |
     * | Product 8  | 0       | 0            | 0           |
     * | Product 12 | 8       | 8            | 8           |
     * | Product 18 | 0       | 0            | 8           |
     * | Product 24 | 0       | 0            | 8           |
     *
     * ---
     *
     * # Why Multiply by 8?
     *
     * For product `12`:
     *
     * Pairs:
     *
     * ```text
     * (2,6)
     * (3,4)
     * ```
     *
     * Possible tuples:
     *
     * | Tuple # | Tuple     |
     * | ------- | --------- |
     * | 1       | (2,6,3,4) |
     * | 2       | (2,6,4,3) |
     * | 3       | (6,2,3,4) |
     * | 4       | (6,2,4,3) |
     * | 5       | (3,4,2,6) |
     * | 6       | (4,3,2,6) |
     * | 7       | (3,4,6,2) |
     * | 8       | (4,3,6,2) |
     *
     * Total:
     *
     * ```text
     * 8 tuples
     * ```
     *
     * ---
     *
     * # Final Output
     *
     * ```java
     * return 8;
     * ```
     *
     * ---
     *
     * # Time Complexity
     *
     * | Operation          | Complexity |
     * | ------------------ | ---------- |
     * | Generate all pairs | O(n²)      |
     * | Traverse hashmap   | O(n²)      |
     * | Total              | O(n²)      |
     *
     * ---
     *
     * # Space Complexity
     *
     * | Data Structure | Complexity |
     * | -------------- | ---------- |
     * | HashMap        | O(n²)      |
     *
     */
    public int tupleSameProduct_0_1(int[] nums) {

        /** NOTE !!!
         *
         *  map : { product_of_two_numbers: pair_cnt }
         *
         */
        // key   = product of two numbers
        // value = how many pairs produce this product
        Map<Integer, Integer> productCount = new HashMap<>();

        /** NOTE !!!
         *
         *  get ALL possible pairs
         *
         */
        // ---------------------------------------------------
        // Step 1:
        // Generate ALL possible pairs
        // ---------------------------------------------------
        for (int i = 0; i < nums.length; i++) {

            // j starts from i + 1
            // because we only want unique pairs
            for (int j = i + 1; j < nums.length; j++) {

                // calculate product of current pair
                int product = nums[i] * nums[j];

                // increase frequency count
                productCount.put(
                        product,
                        productCount.getOrDefault(product, 0) + 1);
            }
        }

        // final answer
        int ans = 0;

        /** NOTE !!!
         *
         *  for each `product`, get count and update result
         *
         */
        // ---------------------------------------------------
        // Step 2:
        // For each product frequency:
        // if count >= 2,
        // we can form tuples
        // ---------------------------------------------------
        for (int count : productCount.values()) {

            // need at least 2 pairs
            // to form a valid tuple
            if (count >= 2) {

                // choose ANY 2 pairs from "count" pairs
                //
                // combination formula:
                // C(n,2) = n * (n - 1) / 2
                //
                int pairCombinations = count * (count - 1) / 2;

                // each pair combination creates 8 tuples
                ans += pairCombinations * 8;
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


    // V0-3
    // IDEA: Mathematical Combination During Pair Generation (GPT)
    public int tupleSameProduct_0_3(int[] nums) {

        Map<Integer, Integer> map = new HashMap<>();

        int ans = 0;

        for (int i = 0; i < nums.length; i++) {

            for (int j = i + 1; j < nums.length; j++) {

                int product = nums[i] * nums[j];

                int count = map.getOrDefault(product, 0);

                ans += count * 8;

                map.put(product, count + 1);
            }
        }

        return ans;
    }


    // V0-4
    // IDEA: SORT + 2 POINTERS (gemini)
    public int tupleSameProduct_0_4(int[] nums) {
        int n = nums.length;
        // 1. Total number of pairs is (n * (n - 1)) / 2
        int pairCount = n * (n - 1) / 2;
        int[] products = new int[pairCount];

        // 2. Generate all products and store them in an array
        int idx = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                products[idx++] = nums[i] * nums[j];
            }
        }

        // 3. Sort the products: O(M log M) where M is n^2
        Arrays.sort(products);

        int totalTuples = 0;
        int i = 0;

        // 4. Use two pointers to find groups of identical products
        while (i < products.length) {
            int j = i;
            // Move j to the end of the current group of identical products
            while (j < products.length && products[j] == products[i]) {
                j++;
            }

            // The number of pairs with the same product is (j - i)
            int count = j - i;
            if (count >= 2) {
                // Combinations of 2 pairs: n * (n - 1) / 2
                // Permutations of those pairs into tuples: * 8
                // Simplified: 4 * count * (count - 1)
                totalTuples += 4 * count * (count - 1);
            }

            // Move i to the start of the next product group
            i = j;
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
