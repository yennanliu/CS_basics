package LeetCodeJava.Array;

// https://leetcode.com/problems/number-of-unique-categories/description/

import java.util.*;

/**
 * 2782. Number of Unique Categories
 * Medium
 *
 * You are given an integer n and an object categoryHandler of class CategoryHandler.
 *
 * There are n elements, numbered from 0 to n - 1. Each element has a category, and your task is to find the number of unique categories.
 *
 * The class CategoryHandler contains the following function, which may help you:
 *
 * boolean haveSameCategory(integer a, integer b): Returns true if a and b are in the same category and false otherwise. Also, if either a or b is not a valid number (i.e. it's greater than or equal to n or less than 0), it returns false.
 *
 * Return the number of unique categories.
 *
 *
 * Example 1:
 *
 * Input: n = 6, categoryHandler = [1,1,2,2,3,3]
 * Output: 3
 * Explanation: There are 6 elements in this example. The first two elements belong to category 1, the second two belong to category 2, and the last two elements belong to category 3. So there are 3 unique categories.
 *
 * Example 2:
 *
 * Input: n = 5, categoryHandler = [1,2,3,4,5]
 * Output: 5
 * Explanation: There are 5 elements in this example. Each element belongs to a unique category. So there are 5 unique categories.
 *
 * Example 3:
 *
 * Input: n = 3, categoryHandler = [1,1,1]
 * Output: 1
 * Explanation: There are 3 elements in this example. All of them belong to one category. So there is only 1 unique category.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 100
 *
 */
public class NumberOfUniqueCategories {

    /**
     * The category oracle (provided by LeetCode).
     */
    public interface CategoryHandler {
        boolean haveSameCategory(int a, int b);
    }

    // V0
    // IDEA: REPRESENTATIVE SCAN (INTERACTIVE)
    //       "same category" is an equivalence relation, so keep one representative per
    //       category found so far. a new element that matches no representative opens a
    //       new category. transitivity makes matching the representative enough.
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int numberOfCategories(int n, CategoryHandler categoryHandler) {
        List<Integer> representatives = new ArrayList<>();
        for (int element = 0; element < n; element++) {
            boolean found = false;
            for (int representative : representatives) {
                if (categoryHandler.haveSameCategory(representative, element)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                representatives.add(element);
            }
        }
        return representatives.size();
    }
}
