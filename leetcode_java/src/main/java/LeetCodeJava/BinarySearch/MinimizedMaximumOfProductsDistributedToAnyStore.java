package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimized-maximum-of-products-distributed-to-any-store/description/

import java.util.PriorityQueue;

/**
 * 2064. Minimized Maximum of Products Distributed to Any Store
 * Medium
 *
 * You are given an integer n indicating there are n specialty retail stores. There are m product types
 * of varying amounts, which are given as a 0-indexed integer array quantities, where quantities[i]
 * represents the number of products of the ith product type.
 *
 * You need to distribute all products to the retail stores following these rules:
 *
 * A store can only be given at most one product type but can be given any amount of it.
 * After distribution, each store will have been given some number of products (possibly 0). Let x
 * represent the maximum number of products given to any store. You want x to be as small as possible,
 * i.e., you want to minimize the maximum number of products that are given to any store.
 *
 * Return the minimum possible x.
 *
 *
 * Example 1:
 *
 * Input: n = 6, quantities = [11,6]
 * Output: 3
 * Explanation: One optimal way is:
 * - The 11 products of type 0 are distributed to the first four stores in these amounts: 2, 3, 3, 3
 * - The 6 products of type 1 are distributed to the other two stores in these amounts: 3, 3
 * The maximum number of products given to any store is max(2, 3, 3, 3, 3, 3) = 3.
 *
 * Example 2:
 *
 * Input: n = 7, quantities = [15,10,10]
 * Output: 5
 * Explanation: One optimal way is:
 * - The 15 products of type 0 are distributed to the first three stores in these amounts: 5, 5, 5
 * - The 10 products of type 1 are distributed to the next two stores in these amounts: 5, 5
 * - The 10 products of type 2 are distributed to the last two stores in these amounts: 5, 5
 * The maximum number of products given to any store is max(5, 5, 5, 5, 5, 5, 5) = 5.
 *
 * Example 3:
 *
 * Input: n = 1, quantities = [100000]
 * Output: 100000
 * Explanation: The only optimal way is:
 * - The 100000 products of type 0 are distributed to the only store.
 * The maximum number of products given to any store is max(100000) = 100000.
 *
 *
 * Constraints:
 *
 * m == quantities.length
 * 1 <= m <= n <= 10^5
 * 1 <= quantities[i] <= 10^5
 *
 */
public class MinimizedMaximumOfProductsDistributedToAnyStore {

    // V0
    // IDEA: BINARY SEARCH ON THE ANSWER (MINIMISE THE MAXIMUM)
    /**
     *  If every store may hold at most x products, type i needs ceil(quantities[i] / x)
     *  stores. The total stores needed never grows as x grows, so binary search the
     *  smallest x in [1, max(quantities)] whose total is <= n.
     */
    /**
     * time = O(M log(max(quantities)))
     * space = O(1)
     */
    public int minimizedMaximum(int n, int[] quantities) {
        int left = 1;
        int right = 0;
        for (int quantity : quantities) {
            right = Math.max(right, quantity);
        }

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (storesNeeded(quantities, mid) <= n) {
                // mid is enough; a smaller cap may be too
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    /** Stores needed when each store holds at most `cap` products of a single type. */
    private long storesNeeded(int[] quantities, int cap) {
        long stores = 0;
        for (int quantity : quantities) {
            // ceil(quantity / cap)
            stores += (quantity + cap - 1) / cap;
        }
        return stores;
    }

    // V1
    // IDEA: GREEDY WITH A MAX HEAP
    /**
     *  Give every type one store, then hand the remaining n - m stores out one at a time,
     *  always to the type whose current load per store, quantity / stores, is the largest.
     *  The answer is the largest ceil(quantity / stores) at the end. Slower than V0 when
     *  n is large, but it shows the greedy intuition.
     *
     *  heap entry: {quantity, storesAssigned}; ratios compared by cross-multiplication
     *  (a / b > c / d  <=>  a * d > c * b) so no floating point is needed.
     */
    /**
     * time = O(N log M)
     * space = O(M)
     */
    public int minimizedMaximum_1(int n, int[] quantities) {
        PriorityQueue<long[]> maxHeap = new PriorityQueue<>(
                (a, b) -> Long.compare(b[0] * a[1], a[0] * b[1]));
        for (int quantity : quantities) {
            maxHeap.offer(new long[]{quantity, 1});
        }

        // hand out the stores left after every type got one
        for (int extra = 0; extra < n - quantities.length; extra++) {
            long[] heaviest = maxHeap.poll();
            heaviest[1]++;
            maxHeap.offer(heaviest);
        }

        // the top of the heap has the largest load per store
        long[] heaviest = maxHeap.peek();
        long quantity = heaviest[0];
        long stores = heaviest[1];
        return (int) ((quantity + stores - 1) / stores);
    }
}
