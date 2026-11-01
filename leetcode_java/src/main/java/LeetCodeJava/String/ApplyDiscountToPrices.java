package LeetCodeJava.String;

// https://leetcode.com/problems/apply-discount-to-prices/description/

/**
 * 2288. Apply Discount to Prices
 * Medium
 *
 * A sentence is a string of single-space separated words where each word can contain digits, lowercase letters, and the dollar sign '$'. A word represents a price if it is a sequence of digits preceded by a dollar sign.
 *
 * For example, "$100", "$23", and "$6" represent prices while "100", "$", and "$1e5" do not.
 *
 * You are given a string sentence representing a sentence and an integer discount. For each word representing a price, apply a discount of discount% on the price and update the word in the sentence. All updated prices should be represented with exactly two decimal places.
 *
 * Return a string representing the modified sentence.
 *
 * Note that all prices will contain at most 10 digits.
 *
 *
 * Example 1:
 *
 * Input: sentence = "there are $1 $2 and 5$ candies in the shop", discount = 50
 * Output: "there are $0.50 $1.00 and 5$ candies in the shop"
 * Explanation:
 * The words which represent prices are "$1" and "$2".
 * - A 50% discount on "$1" yields "$0.50", so "$1" is replaced by "$0.50".
 * - A 50% discount on "$2" yields "$1". Since we need to have exactly 2 decimal places after a price, we replace "$2" with "$1.00".
 *
 * Example 2:
 *
 * Input: sentence = "1 2 $3 4 $5 $6 7 8$ $9 $10$", discount = 100
 * Output: "1 2 $0.00 4 $0.00 $0.00 7 8$ $0.00 $10$"
 * Explanation:
 * Applying a 100% discount on any price will result in 0.
 * The words representing prices are "$3", "$5", "$6", "$9", and "$10$".
 * Each of them is replaced by "$0.00".
 *
 *
 * Constraints:
 *
 * 1 <= sentence.length <= 10^5
 * sentence consists of lowercase English letters, digits, ' ', and '$'.
 * sentence does not have leading or trailing spaces.
 * All words in sentence are separated by a single space.
 * All prices will be positive numbers without leading zeros.
 * All prices will have at most 10 digits.
 *
 */
public class ApplyDiscountToPrices {

    // V0
    // IDEA: TOKENIZE ON SPACES AND REWRITE ONLY THE WORDS THAT ARE PRICES
    //       a word is a price iff it is '$' followed by one or more digits,
    //       which rejects "$", "5$", "$1e5" and "$10$"
    /**
     * time = O(N)
     * space = O(N)
     */
    public String discountPrices(String sentence, int discount) {
        // the sentence uses single spaces, so splitting on " " keeps the layout exactly
        String[] words = sentence.split(" ");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                result.append(' ');
            }
            String word = words[i];
            if (isPrice(word)) {
                // at most 10 digits -> fits in a long, never in an int
                long price = Long.parseLong(word.substring(1));
                // work in cents to avoid floating point: price * (100 - discount) cents
                long cents = price * (100 - discount);
                long dollars = cents / 100;
                long remainder = cents % 100;
                result.append('$').append(dollars).append('.');
                // always two decimal places, e.g. 5 cents -> ".05"
                if (remainder < 10) {
                    result.append('0');
                }
                result.append(remainder);
            } else {
                result.append(word);
            }
        }
        return result.toString();
    }

    /** True iff word is '$' followed by at least one digit and nothing else. */
    private boolean isPrice(String word) {
        if (word.length() < 2 || word.charAt(0) != '$') {
            return false;
        }
        for (int i = 1; i < word.length(); i++) {
            if (!Character.isDigit(word.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
