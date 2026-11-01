package LeetCodeJava.Sort;

// https://leetcode.com/problems/filter-restaurants-by-vegan-friendly-price-and-distance/description/

import java.util.*;

/**
 * 1333. Filter Restaurants by Vegan-Friendly, Price and Distance
 * Medium
 *
 * Given the array restaurants where
 * restaurants[i] = [id_i, rating_i, veganFriendly_i, price_i, distance_i].
 * You have to filter the restaurants using three filters.
 *
 * The veganFriendly filter will be either true (meaning you should only include
 * restaurants with veganFriendly_i set to true) or false (meaning you can include
 * any restaurant). In addition, you have the filters maxPrice and maxDistance
 * which are the maximum value for price and distance of restaurants you should
 * consider respectively.
 *
 * Return the array of restaurant IDs after filtering, ordered by rating from
 * highest to lowest. For restaurants with the same rating, order them by id from
 * highest to lowest. For simplicity veganFriendly_i and veganFriendly take
 * value 1 when it is true, and 0 when it is false.
 *
 * Example 1:
 *
 * Input: restaurants = [[1,4,1,40,10],[2,8,0,50,5],[3,8,1,30,4],[4,10,0,10,3],[5,1,1,15,1]], veganFriendly = 1, maxPrice = 50, maxDistance = 10
 * Output: [3,1,5]
 * Explanation:
 * The restaurants are:
 * Restaurant 1 [id=1, rating=4, veganFriendly=1, price=40, distance=10]
 * Restaurant 2 [id=2, rating=8, veganFriendly=0, price=50, distance=5]
 * Restaurant 3 [id=3, rating=8, veganFriendly=1, price=30, distance=4]
 * Restaurant 4 [id=4, rating=10, veganFriendly=0, price=10, distance=3]
 * Restaurant 5 [id=5, rating=1, veganFriendly=1, price=15, distance=1]
 * After filter restaurants with veganFriendly = 1, maxPrice = 50 and
 * maxDistance = 10 we have restaurant 3, restaurant 1 and restaurant 5
 * (ordered by rating from highest to lowest).
 *
 * Example 2:
 *
 * Input: restaurants = [[1,4,1,40,10],[2,8,0,50,5],[3,8,1,30,4],[4,10,0,10,3],[5,1,1,15,1]], veganFriendly = 0, maxPrice = 50, maxDistance = 10
 * Output: [4,3,2,1,5]
 * Explanation: The restaurants are the same as in example 1, but in this case
 * the filter veganFriendly = 0, therefore all restaurants are considered.
 *
 * Example 3:
 *
 * Input: restaurants = [[1,4,1,40,10],[2,8,0,50,5],[3,8,1,30,4],[4,10,0,10,3],[5,1,1,15,1]], veganFriendly = 0, maxPrice = 30, maxDistance = 3
 * Output: [4,5]
 *
 * Constraints:
 *
 * 1 <= restaurants.length <= 10^4
 * restaurants[i].length == 5
 * 1 <= id_i, rating_i, price_i, distance_i <= 10^5
 * 1 <= maxPrice, maxDistance <= 10^5
 * veganFriendly_i and veganFriendly are 0 or 1.
 * All id_i are distinct.
 *
 */
public class FilterRestaurantsByVeganFriendlyPriceAndDistance {

    // V0
    // IDEA: FILTER + SORT (rating desc, then id desc)
    //   `vegan >= veganFriendly` covers both modes: veganFriendly = 0 keeps
    //   everything, veganFriendly = 1 keeps only vegan == 1
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public List<Integer> filterRestaurants(int[][] restaurants, int veganFriendly, int maxPrice, int maxDistance) {
        // restaurants[i] = [id, rating, veganFriendly, price, distance]
        List<int[]> kept = new ArrayList<>();
        for (int[] restaurant : restaurants) {
            int vegan = restaurant[2];
            int price = restaurant[3];
            int distance = restaurant[4];
            if (vegan >= veganFriendly && price <= maxPrice && distance <= maxDistance) {
                kept.add(restaurant);
            }
        }

        kept.sort((a, b) -> compareByRatingThenId(a, b));

        List<Integer> result = new ArrayList<>();
        for (int[] restaurant : kept) {
            result.add(restaurant[0]);
        }
        return result;
    }

    /** Higher rating first; on a tie, higher id first. */
    private int compareByRatingThenId(int[] a, int[] b) {
        if (a[1] != b[1]) {
            return Integer.compare(b[1], a[1]);
        }
        return Integer.compare(b[0], a[0]);
    }
}
