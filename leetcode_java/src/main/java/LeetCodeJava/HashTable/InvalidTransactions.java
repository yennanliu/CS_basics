package LeetCodeJava.HashTable;

// https://leetcode.com/problems/invalid-transactions/description/

import java.util.*;

/**
 * 1169. Invalid Transactions
 * Medium
 *
 * A transaction is possibly invalid if:
 *
 * the amount exceeds $1000, or;
 * if it occurs within (and including) 60 minutes of another transaction with the same name in a different city.
 *
 * You are given an array of strings transaction where transactions[i] consists of comma-separated values
 * representing the name, time (in minutes), amount, and city of the transaction.
 *
 * Return a list of transactions that are possibly invalid. You may return the answer in any order.
 *
 * Example 1:
 *
 * Input: transactions = ["alice,20,800,mtv","alice,50,100,beijing"]
 * Output: ["alice,20,800,mtv","alice,50,100,beijing"]
 * Explanation: The first transaction is invalid because the second transaction occurs within a difference of
 * 60 minutes, have the same name and is in a different city. Similarly the second one is invalid too.
 *
 * Example 2:
 *
 * Input: transactions = ["alice,20,800,mtv","alice,50,1200,mtv"]
 * Output: ["alice,50,1200,mtv"]
 *
 * Example 3:
 *
 * Input: transactions = ["alice,20,800,mtv","bob,50,1200,mtv"]
 * Output: ["bob,50,1200,mtv"]
 *
 * Constraints:
 *
 * transactions.length <= 1000
 * Each transactions[i] takes the form "{name},{time},{amount},{city}"
 * Each {name} and {city} consist of lowercase English letters, and have lengths between 1 and 10.
 * Each {time} consist of digits, and represent an integer between 0 and 1000.
 * Each {amount} consist of digits, and represent an integer between 0 and 2000.
 *
 */
public class InvalidTransactions {

    // V0
    // IDEA: HASH TABLE (group by name) + PAIRWISE CHECK
    //       parse every transaction once, group indexes by name, then a
    //       transaction is invalid if amount > 1000 or some other transaction of
    //       the same name is in a different city within 60 minutes.
    //       duplicates are judged by index, so each occurrence is returned on its own.
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public List<String> invalidTransactions(String[] transactions) {
        int n = transactions.length;
        String[] names = new String[n];
        int[] times = new int[n];
        int[] amounts = new int[n];
        String[] cities = new String[n];

        // name -> indexes of that person's transactions
        Map<String, List<Integer>> byName = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String[] parts = transactions[i].split(",");
            names[i] = parts[0];
            times[i] = Integer.parseInt(parts[1]);
            amounts[i] = Integer.parseInt(parts[2]);
            cities[i] = parts[3];
            byName.computeIfAbsent(names[i], x -> new ArrayList<>()).add(i);
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            boolean invalid = amounts[i] > 1000;
            if (!invalid) {
                // compare only against the same name's transactions
                for (int j : byName.get(names[i])) {
                    if (j == i) {
                        continue;
                    }
                    boolean differentCity = !cities[j].equals(cities[i]);
                    boolean withinHour = Math.abs(times[j] - times[i]) <= 60;
                    if (differentCity && withinHour) {
                        invalid = true;
                        break;
                    }
                }
            }
            if (invalid) {
                result.add(transactions[i]);
            }
        }
        return result;
    }
}
