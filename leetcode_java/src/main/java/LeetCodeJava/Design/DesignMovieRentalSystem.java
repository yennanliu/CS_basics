package LeetCodeJava.Design;

// https://leetcode.com/problems/design-movie-rental-system/

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 *  1912. Design Movie Rental System
 *  Hard
 *
 *  You have a movie renting company consisting of n shops. You want to implement a
 *  renting system that supports searching for, booking, and returning movies. The system
 *  should also support generating a report of the currently rented movies.
 *
 *  Each movie is given as a 2D integer array entries where entries[i] = [shop, movie,
 *  price] indicates that there is a copy of movie at shop with a rental price of price.
 *  Each shop carries at most one copy of a movie.
 *
 *  Implement the MovieRentingSystem class:
 *
 *   - MovieRentingSystem(int n, int[][] entries) Initializes the object with n shops and
 *     the movies in entries.
 *   - List<Integer> search(int movie) Finds the cheapest 5 shops that have an UNRENTED
 *     copy of the given movie, sorted by price ascending, ties broken by smaller shop.
 *     Fewer than 5 -> all of them; none -> an empty list.
 *   - void rent(int shop, int movie) Rents an unrented copy of a given movie from a shop.
 *   - void drop(int shop, int movie) Drops off a previously rented copy at a shop.
 *   - List<List<Integer>> report() Returns the cheapest 5 RENTED movies as res[j] =
 *     [shop, movie], sorted by price ascending, then smaller shop, then smaller movie.
 *
 *  Note: rent will only be called if the shop has an unrented copy of the movie, and drop
 *  will only be called if the shop had previously rented out the movie.
 *
 *  Example 1:
 *    Input
 *      ["MovieRentingSystem","search","rent","rent","report","drop","search"]
 *      [[3,[[0,1,5],[0,2,6],[0,3,7],[1,1,4],[1,2,7],[2,1,5]]],[1],[0,1],[1,2],[],[1,2],[2]]
 *    Output
 *      [null,[1,0,2],null,null,[[0,1],[1,2]],null,[0,1]]
 *    Explanation
 *      search(1) -> [1,0,2]; shop 1 is cheapest, shops 0 and 2 tie on price so the
 *                   smaller shop number comes first.
 *      report()  -> [[0,1],[1,2]]; movie 1 from shop 0 is cheapest, then movie 2 from shop 1.
 *
 *  Constraints:
 *    1 <= n <= 3 * 10^5
 *    1 <= entries.length <= 10^5
 *    0 <= shop_i < n
 *    1 <= movie_i, price_i <= 10^4
 *    Each shop carries at most one copy of a movie.
 *    At most 10^5 calls in total will be made to search, rent, drop and report.
 */
public class DesignMovieRentalSystem {

    // V0
    // IDEA: ORDERED SETS -- one per movie for the shelf, one global for the rentals
    //
    //   available[movie] : TreeSet of {price, shop} still on the shelf
    //                      -> search(movie) is just the first 5 entries.
    //   rented           : ONE global TreeSet of {price, shop, movie}
    //                      -> report() is just the first 5 entries.
    //   priceOf[(shop, movie)] : lookup so rent/drop know which key to move.
    //
    //   rent = remove from available[movie], insert into rented
    //   drop = remove from rented,           insert into available[movie]
    //
    //   NOTE: the tuple orderings (price, shop) and (price, shop, movie) are EXACTLY
    //         LeetCode's required tie-break orders, so the comparators are the whole
    //         answer to the sorting requirements.
    /**
     * time = O(M log M) init, O(log M) per rent / drop, O(1) per search / report
     * space = O(M), M = entries.length
     */
    private static final Comparator<int[]> BY_PRICE_SHOP = new Comparator<int[]>() {
        @Override
        public int compare(int[] a, int[] b) {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(a[1], b[1]);
        }
    };

    private static final Comparator<int[]> BY_PRICE_SHOP_MOVIE = new Comparator<int[]>() {
        @Override
        public int compare(int[] a, int[] b) {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            if (a[1] != b[1]) {
                return Integer.compare(a[1], b[1]);
            }
            return Integer.compare(a[2], b[2]);
        }
    };

    private final Map<Integer, TreeSet<int[]>> available = new HashMap<>();
    private final TreeSet<int[]> rented = new TreeSet<>(BY_PRICE_SHOP_MOVIE);
    private final Map<Long, Integer> priceOf = new HashMap<>();

    public DesignMovieRentalSystem(int n, int[][] entries) {
        for (int[] e : entries) {
            int shop = e[0];
            int movie = e[1];
            int price = e[2];
            TreeSet<int[]> set = available.get(movie);
            if (set == null) {
                set = new TreeSet<>(BY_PRICE_SHOP);
                available.put(movie, set);
            }
            set.add(new int[]{price, shop});
            priceOf.put(key(shop, movie), price);
        }
    }

    private static long key(int shop, int movie) {
        return (long) shop * 100000L + movie;
    }

    public List<Integer> search(int movie) {
        List<Integer> res = new ArrayList<>();
        TreeSet<int[]> set = available.get(movie);
        if (set == null) {
            return res;
        }
        for (int[] e : set) {
            res.add(e[1]);
            if (res.size() == 5) {
                break;
            }
        }
        return res;
    }

    public void rent(int shop, int movie) {
        int p = priceOf.get(key(shop, movie));
        available.get(movie).remove(new int[]{p, shop});
        rented.add(new int[]{p, shop, movie});
    }

    public void drop(int shop, int movie) {
        int p = priceOf.get(key(shop, movie));
        rented.remove(new int[]{p, shop, movie});
        available.get(movie).add(new int[]{p, shop});
    }

    public List<List<Integer>> report() {
        List<List<Integer>> res = new ArrayList<>();
        for (int[] e : rented) {
            List<Integer> one = new ArrayList<>();
            one.add(e[1]);
            one.add(e[2]);
            res.add(one);
            if (res.size() == 5) {
                break;
            }
        }
        return res;
    }
}
