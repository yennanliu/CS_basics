"""

2034. Stock Price Fluctuation
Medium

You are given a stream of records about a particular stock. Each record contains a timestamp and the corresponding price of the stock at that timestamp.

Unfortunately due to the volatile nature of the stock market, the records do not come in order. Even worse, some records may be incorrect. Another record with the same timestamp may appear later in the stream correcting the price of the previous wrong record.

Design an algorithm that:

Updates the price of the stock at a particular timestamp, correcting the price from any previous records at the timestamp.
Finds the latest price of the stock based on the current records. The latest price is the price at the latest timestamp recorded.
Finds the maximum price the stock has been based on the current records.
Finds the minimum price the stock has been based on the current records.

Implement the StockPrice class:

StockPrice() Initializes the object with no price records.
void update(int timestamp, int price) Updates the price of the stock at the given timestamp.
int current() Returns the latest price of the stock.
int maximum() Returns the maximum price of the stock.
int minimum() Returns the minimum price of the stock.


Example 1:

Input
["StockPrice", "update", "update", "current", "maximum", "update", "maximum", "update", "minimum"]
[[], [1, 10], [2, 5], [], [], [1, 3], [], [4, 2], []]
Output
[null, null, null, 5, 10, null, 5, null, 2]

Explanation
StockPrice stockPrice = new StockPrice();
stockPrice.update(1, 10); // Timestamps are [1] with corresponding prices [10].
stockPrice.update(2, 5);  // Timestamps are [1,2] with corresponding prices [10,5].
stockPrice.current();     // return 5, the latest timestamp is 2 with the price being 5.
stockPrice.maximum();     // return 10, the maximum price is 10 at timestamp 1.
stockPrice.update(1, 3);  // The previous timestamp 1 had a wrong price, so it is updated to 3.
                          // Timestamps are [1,2] with corresponding prices [3,5].
stockPrice.maximum();     // return 5, the maximum price is 5 after the correction.
stockPrice.update(4, 2);  // Timestamps are [1,2,4] with corresponding prices [3,5,2].
stockPrice.minimum();     // return 2, the minimum price is 2 at timestamp 4.


Constraints:

1 <= timestamp, price <= 10^9
At most 10^5 calls will be made in total to update, current, maximum, and minimum.

"""


# V0
# IDEA : HASH MAP (timestamp -> price) + 2 LAZY PQ (gpt)
"""
NOTE !!!

1. use 2 PQ (small, big PQ)
     
     - self.big_values (Max PQ)

     - self.small_values (Small PQ)



2. PQ save (price, time)

    -> save `price` first,  since we need to sort by price



3. the `lazy deletion` pattern:
    

    ```
    _price, _time = pq[0]

    while pq:
        if _price == kv_map[_time]:
                break

        heapq.heappop(pq)

    ```


4. we add `new` `time, price` to PQ anyway

"""
import heapq

class StockPrice(object):

    def __init__(self):

        # Max heap:
        # (-price, timestamp)
        self.big_values = []

        # Min heap:
        # (price, timestamp)
        self.small_values = []

        # timestamp -> latest price
        self.kv_map = {}

        # Latest timestamp
        self.max_time = 0

    def update(self, timestamp, price):
        """
        :type timestamp: int
        :type price: int
        :rtype: None
        """

        # Update latest price for this timestamp
        self.kv_map[timestamp] = price

        # Track latest timestamp
        self.max_time = max(self.max_time, timestamp)


        """
        NOTE !!!


        we add new time, price to PQ anyway
        """

        # Add new price to both heaps
        heapq.heappush(
            self.big_values,
            (-price, timestamp)
        )

        heapq.heappush(
            self.small_values,
            (price, timestamp)
        )

        # Lazy delete stale entries from max heap
        while self.big_values:
            neg_price, ts = self.big_values[0]

            if -neg_price == self.kv_map[ts]:
                break

            heapq.heappop(self.big_values)

        # Lazy delete stale entries from min heap
        while self.small_values:
            price_at_ts, ts = self.small_values[0]

            if price_at_ts == self.kv_map[ts]:
                break

            heapq.heappop(self.small_values)

    def current(self):
        """
        :rtype: int
        """
        return self.kv_map[self.max_time]

    def maximum(self):
        """
        :rtype: int
        """
        return -self.big_values[0][0]

    def minimum(self):
        """
        :rtype: int
        """
        return self.small_values[0][0]



# V0
# IDEA : HASH MAP (timestamp -> price) + 2 LAZY HEAPS
#
#   prices[timestamp] is the ONLY source of truth. an update just overwrites
#   it, and `latest` tracks the biggest timestamp seen, so current() is O(1).
#
#   for maximum()/minimum() we push (price, timestamp) on EVERY update and
#   never delete. on a query, pop from the top while the top is STALE, i.e.
#   prices[ts] != price -> that record was corrected later. the first top
#   that agrees with prices is the true extreme.
#
#   NOTE : heapq is a min-heap, so the max-heap negates the price.
import heapq


class StockPrice(object):

    # time = O(1), space = O(n)
    def __init__(self):
        self.prices = {}
        self.latest = 0
        self.max_heap = []  # (-price, timestamp)
        self.min_heap = []  # (price, timestamp)

    # time = O(log n), space = O(n)
    def update(self, timestamp, price):
        """
        :type timestamp: int
        :type price: int
        :rtype: None
        """
        self.prices[timestamp] = price
        self.latest = max(self.latest, timestamp)
        heapq.heappush(self.max_heap, (-price, timestamp))
        heapq.heappush(self.min_heap, (price, timestamp))

    # time = O(1), space = O(1)
    def current(self):
        """
        :rtype: int
        """
        return self.prices[self.latest]

    # time = O(log n) amortized, space = O(1)
    def maximum(self):
        """
        :rtype: int
        """
        # lazy deletion : drop tops that were corrected by a later update
        while -self.max_heap[0][0] != self.prices[self.max_heap[0][1]]:
            heapq.heappop(self.max_heap)
        return -self.max_heap[0][0]

    # time = O(log n) amortized, space = O(1)
    def minimum(self):
        """
        :rtype: int
        """
        while self.min_heap[0][0] != self.prices[self.min_heap[0][1]]:
            heapq.heappop(self.min_heap)
        return self.min_heap[0][0]


# V0-1
# IDEA: BIG, SMALL PQ + Lazy Deletion (GEMINI)
import heapq


class StockPrice(object):

  def __init__(self):
    # 記錄每個時間戳對應的最新價格：{timestamp: price}
    self.price_map = {}

    # 追蹤當前最大的時間戳（對應最新時間）
    self.max_time = 0

    # 最大堆疊（Max-Heap 模擬）：存入 (-price, timestamp)
    # Python 預設為 Min-Heap，透過價格取負號來實現最大堆頂
    self.max_heap = []

    # 最小堆疊（Min-Heap）：存入 (price, timestamp)
    self.min_heap = []

  def update(self, timestamp, price):
    """
        :type timestamp: int
        :type price: int
        :rtype: None
        """
    # 1. 更新或覆寫該時間點的最新價格
    self.price_map[timestamp] = price

    # 2. 維護全域最新時間戳
    self.max_time = max(self.max_time, timestamp)

    # 3. 將新的價格與時間戳直接推入雙堆疊（允許重複/過期數據累積）
    heapq.heappush(self.max_heap, (-price, timestamp))
    heapq.heappush(self.min_heap, (price, timestamp))

    """
    NOTE !!!


        -> Lazy Deletion
    """
    # 4. 進行 Lazy Deletion（在 update 時順便清掉堆頂過期/被覆寫的資料）
    # 條件：如果堆頂紀錄的價格與 price_map 裡的真實最新價格不符，代表過期
    while self.max_heap and -self.max_heap[0][0] != self.price_map[
        self.max_heap[0][1]
    ]:
      heapq.heappop(self.max_heap)

    while self.min_heap and self.min_heap[0][0] != self.price_map[
        self.min_heap[0][1]
    ]:
      heapq.heappop(self.min_heap)

  def current(self):
    """
        回傳最新時間戳的價格
        :rtype: int
        """
    return self.price_map[self.max_time]

  def maximum(self):
    """
        回傳最大價格（因在 update 已做清理，堆頂必為合法最新極值）
        :rtype: int
        """
    return -self.max_heap[0][0]

  def minimum(self):
    """
        回傳最小價格（同上，堆頂必為合法最新極值）
        :rtype: int
        """
    return self.min_heap[0][0]


# V0-2
# IDEA : HASH MAP (timestamp -> price) + TWO LAZY HEAPS (CLAUDE CODE)
#
#   `prices[t]` is the source of truth. `latest` tracks the biggest timestamp
#   seen so far, so current() is O(1).
#
#   for maximum()/minimum() keep a max-heap and a min-heap of (price, ts)
#   and push on EVERY update — never try to delete. on a query, pop from the
#   top while the entry is STALE (prices[ts] != price). the top that survives
#   is the true extreme.
#
#   NOTE : python's heapq is a min-heap, so the max-heap stores negated prices.
#
# time = O(log n) amortized per call, space = O(n)
import heapq


class StockPrice(object):

    def __init__(self):
        self.prices = {}
        self.latest = 0
        self.max_heap = []   # (-price, ts)
        self.min_heap = []   # (price, ts)

    def update(self, timestamp, price):
        self.prices[timestamp] = price
        self.latest = max(self.latest, timestamp)
        heapq.heappush(self.max_heap, (-price, timestamp))
        heapq.heappush(self.min_heap, (price, timestamp))

    def current(self):
        return self.prices[self.latest]

    def maximum(self):
        while True:
            price, ts = self.max_heap[0]
            if self.prices[ts] == -price:
                return -price
            heapq.heappop(self.max_heap)

    def minimum(self):
        while True:
            price, ts = self.min_heap[0]
            if self.prices[ts] == price:
                return price
            heapq.heappop(self.min_heap)


# Your StockPrice object will be instantiated and called as such:
# obj = StockPrice()
# obj.update(timestamp,price)
# param_2 = obj.current()
# param_3 = obj.maximum()
# param_4 = obj.minimum()


# V0-3
# IDEA: BIG, SMALL PQ (gpt)
import heapq


class StockPrice(object):

    def __init__(self):
        # { timestamp: current price }
        self.price_map = {}

        # Latest timestamp
        self.max_time = 0

        """
        NOTE !!!


        PQ save info: (-price, timestamp)
        """
        # Max heap: (-price, timestamp)
        self.max_prices = []


        """
        NOTE !!!


        PQ save info: (price, timestamp)
        """
        # Min heap: (price, timestamp)
        self.min_prices = []

    def update(self, timestamp, price):
        """
        :type timestamp: int
        :type price: int
        :rtype: None
        """
        # Update the latest price for this timestamp
        self.price_map[timestamp] = price

        # Track latest timestamp
        self.max_time = max(self.max_time, timestamp)

        # Add new price to both heaps
        heapq.heappush(
            self.max_prices,
            (-price, timestamp)
        )

        heapq.heappush(
            self.min_prices,
            (price, timestamp)
        )

    def current(self):
        """
        :rtype: int
        """
        return self.price_map[self.max_time]

    def maximum(self):
        """
        :rtype: int
        """
        # Remove stale prices
        while self.max_prices:
            neg_price, timestamp = self.max_prices[0]
            price = -neg_price

            if self.price_map[timestamp] == price:
                return price

            heapq.heappop(self.max_prices)

    def minimum(self):
        """
        :rtype: int
        """
        # Remove stale prices
        while self.min_prices:
            price, timestamp = self.min_prices[0]

            if self.price_map[timestamp] == price:
                return price

            heapq.heappop(self.min_prices)
