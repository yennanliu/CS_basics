package LeetCodeJava.Design;

// https://leetcode.com/problems/stock-price-fluctuation/

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *  2034. Stock Price Fluctuation
 *  Medium
 *
 *  You are given a stream of records about a particular stock. Each record contains a
 *  timestamp and the corresponding price of the stock at that timestamp.
 *
 *  Unfortunately due to the volatile nature of the stock market, the records do not come in
 *  order. Even worse, some records may be incorrect. Another record with the same timestamp
 *  may appear later in the stream correcting the price of the previous wrong record.
 *
 *  Implement the StockPrice class:
 *
 *   - StockPrice() Initializes the object with no price records.
 *   - void update(int timestamp, int price) Updates the price of the stock at the given
 *     timestamp.
 *   - int current() Returns the latest price of the stock (the price at the latest
 *     timestamp recorded).
 *   - int maximum() Returns the maximum price of the stock.
 *   - int minimum() Returns the minimum price of the stock.
 *
 *  Example 1:
 *
 *  Input
 *  ["StockPrice", "update", "update", "current", "maximum", "update", "maximum",
 *   "update", "minimum"]
 *  [[], [1, 10], [2, 5], [], [], [1, 3], [], [4, 2], []]
 *  Output
 *  [null, null, null, 5, 10, null, 5, null, 2]
 *
 *  Explanation
 *  StockPrice stockPrice = new StockPrice();
 *  stockPrice.update(1, 10); // timestamps [1]     prices [10]
 *  stockPrice.update(2, 5);  // timestamps [1,2]   prices [10,5]
 *  stockPrice.current();     // return 5
 *  stockPrice.maximum();     // return 10
 *  stockPrice.update(1, 3);  // timestamp 1 corrected -> prices [3,5]
 *  stockPrice.maximum();     // return 5
 *  stockPrice.update(4, 2);  // timestamps [1,2,4] prices [3,5,2]
 *  stockPrice.minimum();     // return 2
 *
 *  Constraints:
 *
 *   1 <= timestamp, price <= 10^9
 *   At most 10^5 calls will be made in total to update, current, maximum, and minimum.
 */
public class StockPriceFluctuation {

    // V0
    // IDEA: HASH MAP (timestamp -> price) + TWO LAZY HEAPS
    //       `prices` is the source of truth, and `latest` tracks the biggest timestamp
    //       seen so far so current() is O(1).
    //
    //       for maximum()/minimum() keep a max-heap and a min-heap of (price, ts) and push
    //       on EVERY update -- never try to delete. on a query, pop from the top while the
    //       entry is STALE (prices[ts] != price). the top that survives is the true extreme.
    /**
     * time = O(log N) amortized per call, O(1) for current()
     * space = O(N)
     */
    private final Map<Integer, Integer> prices;
    private int latest;
    // each entry = {price, timestamp}
    private final PriorityQueue<int[]> maxHeap;
    private final PriorityQueue<int[]> minHeap;

    public StockPriceFluctuation() {
        this.prices = new HashMap<>();
        this.latest = 0;
        this.maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b[0], a[0]));
        this.minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
    }

    public void update(int timestamp, int price) {
        prices.put(timestamp, price);
        this.latest = Math.max(this.latest, timestamp);
        int[] entry = new int[]{price, timestamp};
        maxHeap.add(entry);
        minHeap.add(entry);
    }

    public int current() {
        return prices.get(this.latest);
    }

    public int maximum() {
        while (!isFresh(maxHeap.peek())) {
            maxHeap.poll();
        }
        return maxHeap.peek()[0];
    }

    public int minimum() {
        while (!isFresh(minHeap.peek())) {
            minHeap.poll();
        }
        return minHeap.peek()[0];
    }

    private boolean isFresh(int[] entry) {
        return prices.get(entry[1]) == entry[0];
    }
}
