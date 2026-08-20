package LeetCodeJava.Design;

// https://leetcode.com/problems/apply-discount-every-n-orders/

import java.util.HashMap;
import java.util.Map;

/**
 *  1357. Apply Discount Every n Orders
 *  Medium
 *
 *  There is a supermarket that is frequented by many customers. The products sold at
 *  the supermarket are represented as two parallel integer arrays products and prices,
 *  where the ith product has an ID of products[i] and a price of prices[i].
 *
 *  When a customer is paying, their bill is represented as two parallel integer arrays
 *  product and amount, where the jth product they purchased has an ID of product[j],
 *  and amount[j] is how much of the product they bought. Their subtotal is calculated
 *  as the sum of each amount[j] * (price of the jth product).
 *
 *  The supermarket decided to have a sale. Every nth customer paying for their groceries
 *  will be given a percentage discount. The discount amount is given by discount, where
 *  they will be given discount percent off their subtotal. More formally, if their
 *  subtotal is bill, then they would actually pay bill * ((100 - discount) / 100).
 *
 *  Implement the Cashier class:
 *
 *   - Cashier(int n, int discount, int[] products, int[] prices) Initializes the object
 *     with n, the discount, and the products and their prices.
 *   - double getBill(int[] product, int[] amount) Returns the final total of the bill
 *     with the discount applied (if any). Answers within 10^-5 of the actual value
 *     will be accepted.
 *
 *  Example 1:
 *    Input
 *      ["Cashier","getBill","getBill","getBill"]
 *      [[3,50,[1,2,3,4,5,6,7],[100,200,300,400,300,200,100]],[[1,2],[1,2]],
 *       [[3,7],[10,10]],[[1,2,3,4,5,6,7],[1,1,1,1,1,1,1]]]
 *    Output
 *      [null,500.0,4000.0,800.0]
 *    Explanation
 *      1st customer, no discount -> 1 * 100 + 2 * 200 = 500.0
 *      2nd customer, no discount -> 10 * 300 + 10 * 100 = 4000.0
 *      3rd customer, 50% off     -> 1600 * ((100 - 50) / 100) = 800.0
 *
 *  Constraints:
 *    1 <= n <= 10^4
 *    0 <= discount <= 100
 *    1 <= products.length <= 200
 *    prices.length == products.length
 *    1 <= products[i] <= 200
 *    1 <= prices[i] <= 1000
 *    The elements in products are unique.
 *    1 <= product.length <= products.length
 *    amount.length == product.length
 *    product[j] exists in products.
 *    1 <= amount[j] <= 1000
 *    The elements of product are unique.
 *    At most 1000 calls will be made to getBill.
 */
public class ApplyDiscountEveryNOrders {

    // V0
    // IDEA: HASH TABLE (product id -> price) + CUSTOMER COUNTER MOD n
    //       build {product id : price} once in the constructor, then keep a
    //       counter bumped as i = (i + 1) % n on every getBill call, so i == 0
    //       happens on exactly every n-th customer -> that is when we discount.
    /**
     * time = O(K) init, O(M) per getBill  (K = #products, M = #items in the bill)
     * space = O(K)
     */
    private final Map<Integer, Integer> price = new HashMap<>();
    private final int n;
    private final int discount;
    private int i = 0;

    public ApplyDiscountEveryNOrders(int n, int discount, int[] products, int[] prices) {
        this.n = n;
        this.discount = discount;
        for (int k = 0; k < products.length; k++) {
            price.put(products[k], prices[k]);
        }
    }

    public double getBill(int[] product, int[] amount) {
        this.i = (this.i + 1) % this.n;
        long subtotal = 0;
        for (int k = 0; k < product.length; k++) {
            subtotal += (long) price.get(product[k]) * amount[k];
        }
        if (this.i == 0) {
            return subtotal * (100.0 - this.discount) / 100.0;
        }
        return (double) subtotal;
    }
}
