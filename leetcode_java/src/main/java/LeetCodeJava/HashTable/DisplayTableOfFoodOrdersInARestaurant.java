package LeetCodeJava.HashTable;

// https://leetcode.com/problems/display-table-of-food-orders-in-a-restaurant/description/

import java.util.*;

/**
 * 1418. Display Table of Food Orders in a Restaurant
 * Medium
 *
 * Given the array orders, which represents the orders that customers have done in a restaurant.
 * More specifically orders[i]=[customerNamei,tableNumberi,foodItemi] where customerNamei is the name
 * of the customer, tableNumberi is the table customer sit at, and foodItemi is the item customer orders.
 *
 * Return the restaurant's "display table". The "display table" is a table whose row entries denote
 * how many of each food item each table ordered. The first column is the table number and the
 * remaining columns correspond to each food item in alphabetical order. The first row should be a
 * header whose first column is "Table", followed by the names of the food items. Note that the
 * customer names are not part of the table. Additionally, the rows should be sorted in numerically
 * increasing order.
 *
 *
 * Example 1:
 *
 * Input: orders = [["David","3","Ceviche"],["Corina","10","Beef Burrito"],["David","3","Fried Chicken"],["Carla","5","Water"],["Carla","5","Ceviche"],["Rous","3","Ceviche"]]
 * Output: [["Table","Beef Burrito","Ceviche","Fried Chicken","Water"],["3","0","2","1","0"],["5","0","1","0","1"],["10","1","0","0","0"]]
 *
 * Example 2:
 *
 * Input: orders = [["James","12","Fried Chicken"],["Ratesh","12","Fried Chicken"],["Amadeus","12","Fried Chicken"],["Adam","1","Canadian Waffles"],["Brianna","1","Canadian Waffles"]]
 * Output: [["Table","Canadian Waffles","Fried Chicken"],["1","2","0"],["12","0","3"]]
 *
 * Example 3:
 *
 * Input: orders = [["Laura","2","Bean Burrito"],["Jhon","2","Beef Burrito"],["Melissa","2","Soda"]]
 * Output: [["Table","Bean Burrito","Beef Burrito","Soda"],["2","1","1","1"]]
 *
 *
 * Constraints:
 *
 * 1 <= orders.length <= 5 * 10^4
 * orders[i].length == 3
 * 1 <= customerNamei.length, foodItemi.length <= 20
 * customerNamei and foodItemi consist of lowercase and uppercase English letters and the space character.
 * tableNumberi is a valid integer between 1 and 500.
 *
 */
public class DisplayTableOfFoodOrdersInARestaurant {

    // V0
    // IDEA: HASH TABLE (table -> item -> count) + SORTING (items alphabetically, tables numerically)
    /**
     * time = O(N + T log T + F log F + T * F), T = tables, F = food items
     * space = O(N)
     */
    public List<List<String>> displayTable(List<List<String>> orders) {
        // TreeMap on the int table number gives numeric order ("3" before "10")
        Map<Integer, Map<String, Integer>> countByTable = new TreeMap<>();
        // TreeSet keeps the food items in alphabetical order for the header
        Set<String> foodItems = new TreeSet<>();

        for (List<String> order : orders) {
            // the customer name (index 0) is not part of the table
            int table = Integer.parseInt(order.get(1));
            String food = order.get(2);
            foodItems.add(food);

            Map<String, Integer> tableCounts = countByTable.computeIfAbsent(table, x -> new HashMap<>());
            tableCounts.put(food, tableCounts.getOrDefault(food, 0) + 1);
        }

        List<List<String>> display = new ArrayList<>();

        List<String> header = new ArrayList<>();
        header.add("Table");
        header.addAll(foodItems);
        display.add(header);

        for (Map.Entry<Integer, Map<String, Integer>> entry : countByTable.entrySet()) {
            List<String> row = new ArrayList<>();
            row.add(String.valueOf(entry.getKey()));
            // every cell is printed, zeros included
            for (String food : foodItems) {
                row.add(String.valueOf(entry.getValue().getOrDefault(food, 0)));
            }
            display.add(row);
        }
        return display;
    }
}
