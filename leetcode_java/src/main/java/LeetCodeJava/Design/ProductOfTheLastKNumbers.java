package LeetCodeJava.Design;

// https://leetcode.com/problems/product-of-the-last-k-numbers/

import java.util.ArrayList;
import java.util.List;

/**
 *  1352. Product of the Last K Numbers
 *  Medium
 *
 *  Design an algorithm that accepts a stream of integers and retrieves the product
 *  of the last k integers of the stream.
 *
 *  Implement the ProductOfNumbers class:
 *    ProductOfNumbers() Initializes the object with an empty stream.
 *    void add(int num) Appends the integer num to the stream.
 *    int getProduct(int k) Returns the product of the last k numbers in the current
 *      list. You can assume that the current list always has at least k numbers.
 *
 *  The test cases are generated so that, at any time, the product of any contiguous
 *  sequence of numbers will fit into a single 32-bit integer without overflowing.
 *
 *  Example 1:
 *    Input
 *      ["ProductOfNumbers","add","add","add","add","add","getProduct","getProduct",
 *       "getProduct","add","getProduct"]
 *      [[],[3],[0],[2],[5],[4],[2],[3],[4],[8],[2]]
 *    Output
 *      [null,null,null,null,null,null,20,40,0,null,32]
 *    Explanation
 *      stream = [3,0,2,5,4]
 *      getProduct(2) -> 5*4  = 20
 *      getProduct(3) -> 2*5*4 = 40
 *      getProduct(4) -> 0*2*5*4 = 0
 *      add(8); getProduct(2) -> 4*8 = 32
 *
 *  Constraints:
 *    0 <= num <= 100
 *    1 <= k <= 4 * 10^4
 *    At most 4 * 10^4 calls will be made to add and getProduct.
 *    The product of the stream at any point in time will fit in a 32-bit integer.
 */
public class ProductOfTheLastKNumbers {

    // V0
    // IDEA: PREFIX PRODUCTS, RESET ON EVERY ZERO
    //
    //       keep prefix[i] = product of the first i numbers SINCE THE LAST ZERO,
    //       starting with prefix[0] = 1. then
    //         product of the last k = prefix[last] / prefix[last - k]
    //       which is O(1) -- what the follow-up asks for.
    //
    //       a 0 would make every later division meaningless (and by zero), so treat
    //       it as a hard reset: throw the prefix list away and start over.
    //       after a reset, prefix.size() - 1 counts how many non-zero numbers trail
    //       the stream; if k reaches past that, a 0 sits inside the window and the
    //       product is 0 -- that is the `prefix.size() <= k` test.
    /**
     * time = O(1) per add / getProduct
     * space = O(n), n = numbers since the last zero
     */
    private final List<Integer> prefix;

    public ProductOfTheLastKNumbers() {
        this.prefix = new ArrayList<>();
        this.prefix.add(1);
    }

    public void add(int num) {
        if (num == 0) {
            prefix.clear();
            prefix.add(1);
            return;
        }
        prefix.add(prefix.get(prefix.size() - 1) * num);
    }

    public int getProduct(int k) {
        int sz = prefix.size();
        if (sz <= k) {
            return 0; // a zero lies inside the last k numbers
        }
        return prefix.get(sz - 1) / prefix.get(sz - 1 - k);
    }
}
