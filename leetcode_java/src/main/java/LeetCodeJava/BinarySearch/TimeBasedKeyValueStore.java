package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/time-based-key-value-store/description/
/**
 *
 * 981. Time Based Key-Value Store
 *
 * Design a time-based key-value data structure that can store multiple values for the same key at different time stamps and retrieve the key's value at a certain timestamp.
 *
 * Implement the TimeMap class:
 *
 * TimeMap() Initializes the object of the data structure.
 * void set(String key, String value, int timestamp) Stores the key key with the value value at the given time timestamp.
 * String get(String key, int timestamp) Returns a value such that set was called previously, with timestamp_prev <= timestamp. If there are multiple such values, it returns the value associated with the largest timestamp_prev. If there are no values, it returns "".
 *
 *
 * Example 1:
 *
 * Input
 * ["TimeMap", "set", "get", "get", "set", "get", "get"]
 * [[], ["foo", "bar", 1], ["foo", 1], ["foo", 3], ["foo", "bar2", 4], ["foo", 4], ["foo", 5]]
 * Output
 * [null, null, "bar", "bar", null, "bar2", "bar2"]
 *
 * Explanation
 * TimeMap timeMap = new TimeMap();
 * timeMap.set("foo", "bar", 1);  // store the key "foo" and value "bar" along with timestamp = 1.
 * timeMap.get("foo", 1);         // return "bar"
 * timeMap.get("foo", 3);         // return "bar", since there is no value corresponding to foo at timestamp 3 and timestamp 2, then the only value is at timestamp 1 is "bar".
 * timeMap.set("foo", "bar2", 4); // store the key "foo" and value "bar2" along with timestamp = 4.
 * timeMap.get("foo", 4);         // return "bar2"
 * timeMap.get("foo", 5);         // return "bar2"
 *
 *
 * Constraints:
 *
 * 1 <= key.length, value.length <= 100
 * key and value consist of lowercase English letters and digits.
 * 1 <= timestamp <= 107
 * All the timestamps timestamp of set are strictly increasing.
 * At most 2 * 105 calls will be made to set and get.
 *
 *
 */
//import javafx.util.Pair;
import java.util.*;

public class TimeBasedKeyValueStore {

    /**
     *  NOTE !!!
     *
     *   DON'T use PQ (priority queue) as the map value data structure.
     *   e.g.
     *    DON'T use below:
     *       - map_1: {k1: PQ[[t1, t2, t2 ...]], }
     *
     *
     *   -> use below instead:
     *
     *    - map_1: {k1: list(t1, t2,...) , }
     *    - map_2: { k1-t1: v1, k1-t2: v2, k2-t2: v3, .... }
     *
     *    -> since timestamp is added by increasing order by default
     *      (per this LC problem's description)
     *
     */

    // V0
    // IDEA : HASHMAP + BINARY SEARCH
    /**
     *  KEY POINT:
     *
     *  1) define 2 helper maps:
     *
     *     - keyValueMap : { key : [v_1, v_2,...] }
     *     - insertTimeMap : { key : [t_1, t_2, ... ] }
     *
     */
    class TimeMap {

        // attr
        /**
         *  NOTE !!!
         *
         * keyValueMap : { key : [v_1, v_2, ...] }
         */
        Map<String, List<String>> keyValueMap;
        /**
         * NOTE !!!
         *
         *   InsertTimeMap : { key : [t1, t2, ...] }
         */
        Map<String, List<Integer>> insertTimeMap;

        public TimeMap() {
            this.keyValueMap = new HashMap<>();
            this.insertTimeMap = new HashMap<>();
        }

        /**
         * time = O(1)
         * space = O(N)
         */
        public void set(String key, String value, int timestamp) {

            // update keyValueMap
            List<String> values = this.keyValueMap.getOrDefault(key, new ArrayList<>());
            //values = this.keyValueMap.get(key);
            values.add(value);
            this.keyValueMap.put(key, values);

            // update InsertTimeMap
            List<Integer> times = this.insertTimeMap.getOrDefault(key, new ArrayList<>());
            times.add(timestamp);
            this.insertTimeMap.put(key, times);
        }

        /**
         * time = O(log N)
         * space = O(1)
         */
        public String get(String key, int timestamp) {
            if (!this.keyValueMap.containsKey(key)){
                return "";
            }

            // V1 : linear search (TLE)
            List<Integer> times = this.insertTimeMap.get(key);
//            while (timestamp >= 0){
//                if (times.contains(timestamp)){
//                    int idx = times.indexOf(timestamp);
//                    return this.keyValueMap.get(key).get(idx);
//                }
//                timestamp -= 1;
//            }

            // V2 : binary search (OK)
            int idx = this.binaryGetLatestTime(timestamp, times);

            return idx >= 0 ? this.keyValueMap.get(key).get(idx) : "";
        }

        private int binaryGetLatestTime(int timestamp, List<Integer> times){
            int left = 0;
            int right = times.size() - 1;
            // NOTE !!!! right >= left
            while (right >= left){
                int mid = (left + right) / 2;
                Integer val = times.get(mid);
                /**
                 *  NOTE !!!
                 *
                 *   Returns a value such that set(key, value, timestamp_prev) was called previously, with timestamp_prev <= timestamp.
                 *   If there are multiple such values, it returns the one with the largest timestamp_prev.
                 *
                 *
                 *   -> so what we want is `the biggest time`  that <= input timestamp
                 *   -> so if `val.equals(timestamp)`, it's the affordable solution
                 */
                if (val.equals(timestamp)){
                    return mid;
                }
                if (val > timestamp){
                    /**
                     * NOTE !!!
                     *
                     *  (binary search pattern)
                     *  right  = mid - 1;
                     */
                    right = mid - 1;
                }else{
                    /**
                     * NOTE !!!
                     *
                     *  (binary search pattern)
                     *  left = mid + 1;
                     */
                    left = mid + 1;
                }
            }

            //return right <= timestamp ? right : -1;
            //return -1;
            /**
             * NOTE !!!!
             *
             *  need to have below handling logic:
             *  if right is a valid idx (>=0), then return it
             *  as binary search result, otherwise return -1
             */
            return right >= 0 ? right : -1;
        }
    }

    // V0-0-1
    // IDEA: 2 HASHMAP + PQ (fixed by gpt)
    class TimeMap_0_0_1 {

        Map<String, List<Integer>> map1; // key -> sorted timestamps
        Map<String, String> map2; // key-timestamp -> value

        public TimeMap_0_0_1() {
            this.map1 = new HashMap<>();
            this.map2 = new HashMap<>();
        }

        /**
         * time = O(1)
         * space = O(N)
         */
        public void set(String key, String value, int timestamp) {
            map1.putIfAbsent(key, new ArrayList<>());
            map1.get(key).add(timestamp); // timestamps are naturally increasing

            map2.put(key + "-" + timestamp, value);
        }

        /**
         * time = O(log N)
         * space = O(1)
         */
        public String get(String key, int timestamp) {
            if (!map1.containsKey(key)) {
                return "";
            }

            List<Integer> list = map1.get(key);

            // binary search
            /** NOTE !!!
             *
             *  `Collections.binarySearch` offer the default binary search algo
             *   implemented by java, we can just import and use it
             */
            int idx = Collections.binarySearch(list, timestamp);

            if (idx >= 0) {
                // found exact timestamp
                return map2.get(key + "-" + list.get(idx));
            }

            // not exact match → compute floor index
            int insertPoint = -idx - 1;
            int floorIdx = insertPoint - 1;

            if (floorIdx < 0) {
                return "";
            }

            int floorTs = list.get(floorIdx);
            return map2.get(key + "-" + floorTs);
        }
    }

    // V0-0-2
    // IDEA: HASHMAP + PQ (fixed by gemini)
    class TimeMap_0_0_2 {

        /**
         * Data Structure: Map<Key (String), TreeMap<Timestamp (int), Value (String)>>
         * - HashMap provides O(1) lookup for the key.
         * - TreeMap automatically sorts timestamps and provides O(log N) lookup (floorKey).
         */
        private Map<String, TreeMap<Integer, String>> store;

        public TimeMap_0_0_2() {
            // Initialize the main data structure
            this.store = new HashMap<>();
        }

        /**
         * Stores the key, value, and timestamp.
         * Time Complexity: O(log N) for TreeMap insertion.
         */
        /**
         * time = O(1)
         * space = O(N)
         */
        public void set(String key, String value, int timestamp) {
            // 1. Ensure the TreeMap exists for the key.
            // If the key is new, putIfAbsent creates a new TreeMap.
            this.store.putIfAbsent(key, new TreeMap<>());

            // 2. Get the TreeMap and insert the (timestamp, value) pair.
            // TreeMap handles the sorting and insertion in O(log N).
            this.store.get(key).put(timestamp, value);
        }

        /**
         * Retrieves the value associated with the largest timestamp less than or equal to the given timestamp.
         * Time Complexity: O(log N) using TreeMap's floorKey().
         */
        /**
         * time = O(log N)
         * space = O(1)
         */
        public String get(String key, int timestamp) {

            // 1. Check if the key exists in the outer map.
            if (!this.store.containsKey(key)) {
                return "";
            }

            // 2. Get the TreeMap of timestamps for the key.
            TreeMap<Integer, String> timeMap = this.store.get(key);

            // 3. Find the largest timestamp <= the given timestamp using floorKey().
            // This is the O(log N) operation that replaces the slow PriorityQueue logic.
            Integer floorTimestamp = timeMap.floorKey(timestamp);

            // 4. Check if a valid timestamp was found.
            if (floorTimestamp == null) {
                // floorKey returns null if no key is less than or equal to the target.
                return "";
            }

            // 5. Return the value associated with the found timestamp.
            return timeMap.get(floorTimestamp);
        }
    }



    // V0_1
    // IDEA : HASHMAP + BINARY SEARCH (fixed by GPT)
    class TimeMap_0_1 {

        // Map that stores key -> (timestamps -> values)
        private Map<String, List<Integer>> keyToTimestamps;
        private Map<String, List<String>> keyToValues;

        public TimeMap_0_1() {
            keyToTimestamps = new HashMap<>();
            keyToValues = new HashMap<>();
        }

        // Set a new value at the given timestamp
        /**
         * time = O(1)
         * space = O(N)
         */
        public void set(String key, String value, int timestamp) {
            // Add the timestamp and value to their respective lists for the given key
            keyToTimestamps.putIfAbsent(key, new ArrayList<>());
            keyToValues.putIfAbsent(key, new ArrayList<>());

            keyToTimestamps.get(key).add(timestamp);
            keyToValues.get(key).add(value);
        }

        // Get the value associated with the largest timestamp <= given timestamp
        /**
         * time = O(log N)
         * space = O(1)
         */
        public String get(String key, int timestamp) {
            if (!keyToTimestamps.containsKey(key)) {
                return "";  // Key does not exist
            }

            List<Integer> timestamps = keyToTimestamps.get(key);
            List<String> values = keyToValues.get(key);

            // Perform binary search to find the right timestamp <= given timestamp
            int idx = binarySearch(timestamps, timestamp);
            if (idx == -1) {
                return "";  // No valid timestamp found
            }

            return values.get(idx);  // Return the value corresponding to the found timestamp
        }

        // Helper method to perform binary search on the timestamps list
        private int binarySearch(List<Integer> timestamps, int target) {
            int left = 0, right = timestamps.size() - 1;

            // Binary search to find the largest timestamp <= target
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (timestamps.get(mid) == target) {
                    return mid;
                } else if (timestamps.get(mid) < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            // The largest timestamp <= target is at index 'right' after the search
            return right >= 0 ? right : -1;
        }
    }

    // V0_2
    // IDEA :  TREEMAP +  floorKey
    /**
     *
     * floorKey() is a method provided by the TreeMap class in Java,
     * which returns the greatest key that is less than or equal to the given key.
     * It effectively performs a binary search on the keys of the TreeMap,
     * allowing efficient retrieval of the closest key that is not greater
     * than the one specified.
     *
     *
     * Key Points about floorKey():
     *
     * 	•	Purpose: To find the largest key that is smaller than or equal to the given key.
     * 	•	Return Value: It returns the closest matching key, or null if no such key exists.
     * 	•	Usage: It is useful when you’re working with sorted data and need to find the nearest lower value for a given key.
     *
     */
    class TimeMap_0_2 {

        // Use TreeMap to maintain order of timestamps for binary search
        private Map<String, TreeMap<Integer, String>> keyTimeMap;

        public TimeMap_0_2() {
            this.keyTimeMap = new HashMap<>();
        }

        /**
         * time = O(1)
         * space = O(N)
         */
        public void set(String key, String value, int timestamp) {
            // Initialize TreeMap if key is new
            keyTimeMap.putIfAbsent(key, new TreeMap<>());
            // Insert the value with the given timestamp
            keyTimeMap.get(key).put(timestamp, value);
        }

        /**
         * time = O(log N)
         * space = O(1)
         */
        public String get(String key, int timestamp) {
            // Return empty string if the key is not found
            if (!keyTimeMap.containsKey(key)) {
                return "";
            }

            // Binary search using TreeMap's floorKey method to find the closest timestamp <= given timestamp
            Integer closestTimestamp = keyTimeMap.get(key).floorKey(timestamp);
            if (closestTimestamp == null) {
                return ""; // No valid timestamp found
            }

            return keyTimeMap.get(key).get(closestTimestamp);
        }
    }

    // V0_3
    // IDEA : DICT + BINARY SEARCH (fixed by GPT)
    class TimeMap_0_3 {

        private Map<String, List<String>> map; // save key-value info
        private Map<String, List<Integer>> timeArray; // save key-insert time history info

        public TimeMap_0_3() {
            this.map = new HashMap<>();
            this.timeArray = new HashMap<>();
        }

        /**
         * time = O(1)
         * space = O(N)
         */
        public void set(String key, String value, int timestamp) {
            List<String> values = this.map.getOrDefault(key, new ArrayList<>());
            List<Integer> timeList = this.timeArray.getOrDefault(key, new ArrayList<>());

            values.add(value);
            timeList.add(timestamp);

            this.map.put(key, values);
            this.timeArray.put(key, timeList);
        }

        /**
         * time = O(log N)
         * space = O(1)
         */
        public String get(String key, int timestamp) {
            if (!this.map.containsKey(key)) {
                return "";
            }

            List<Integer> timeList = this.timeArray.get(key);
            int idx = getRecentAddedValue(timeList, timestamp);

            if (idx == -1) {
                return "";  // No valid timestamp found
            }

            return this.map.get(key).get(idx);
        }

        private Integer getRecentAddedValue(List<Integer> input, int target) {
            int left = 0;
            int right = input.size() - 1;

            // Perform binary search to find the largest timestamp <= target
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (input.get(mid) <= target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            // 'right' will point to the largest index with timestamp <= target
            return right >= 0 ? right : -1; // Return -1 if no valid timestamp is found
        }
    }

    // V_0_3
    // IDEA : HASHMAP + LINEAR SEARCH (TLE)
//    class TimeMap_0_3 {
//
//        // attr
//        Map<String, Map<Integer, String>> keyTimeMap;
//
//        public TimeMap_0_3() {
//            this.keyTimeMap = new HashMap<>();
//        }
//
/**
 * time = O(1)
 * space = O(N)
 */
//        public void set(String key, String value, int timestamp) {
//            Map<Integer, String> valueMap = null;
//            if (!this.keyTimeMap.containsKey(key)){
//                valueMap = new HashMap<>();
//            }else{
//                valueMap = this.keyTimeMap.get(key);
//            }
//            valueMap.put(timestamp, value);
//            this.keyTimeMap.put(key, valueMap);
//        }
//
/**
 * time = O(log N)
 * space = O(1)
 */
//        public String get(String key, int timestamp) {
//            // linear search ???
//            if (!this.keyTimeMap.containsKey(key)){
//                return "";
//            }
//            // binary search
//            for (int x = timestamp; x >= 1; x-=1){
//                if (this.keyTimeMap.get(key).containsKey(x)){
//                    return this.keyTimeMap.get(key).get(x);
//                }
//            }
//            return "";
//        }
//    }

    // V-4
    // IDEA: TREE MAP + FLOOR KEY (gpt)
    class TimeMap_0_4 {

        // Use TreeMap to store timestamp -> value for each key
        /**
         *  NOTE !!!
         *
         *   we use `TreeMap` data structure
         *
         */
        private Map<String, TreeMap<Integer, String>> keyTimeMap;

        public TimeMap_0_4() {
            keyTimeMap = new HashMap<>();
        }

        /**
         * time = O(1)
         * space = O(N)
         */
        public void set(String key, String value, int timestamp) {
            keyTimeMap.putIfAbsent(key, new TreeMap<>());
            keyTimeMap.get(key).put(timestamp, value);
        }

        /**
         * time = O(log N)
         * space = O(1)
         */
        public String get(String key, int timestamp) {
            if (!keyTimeMap.containsKey(key)) return "";

            TreeMap<Integer, String> timeMap = keyTimeMap.get(key);
            /**
             *  NOTE !!!
             *
             *   via `floorEntry`,
             *   we can get the biggest val that <= timestamp  from TreeMap
             *
             */
            Map.Entry<Integer, String> entry = timeMap.floorEntry(timestamp);
            return entry == null ? "" : entry.getValue();
        }
    }

    // V0-5
    // IDEA: HASH MAP + PQ (big -> small)
    // TODO: fix below
//    class TimeMap_0_5 {
//
//        // attr
//        Map<String, PriorityQueue<Integer>> keyTimeListMap;
//
//        Map<String, String> keyValueMap;
//
//        public TimeMap_0_4() {
//
//            keyTimeListMap = new HashMap<>();
//            keyValueMap = new HashMap<>();
//        }
//
/**
 * time = O(1)
 * space = O(N)
 */
//        public void set(String key, String value, int timestamp) {
//
//            // update hashmap 1
//            // ?? fix to use `comparator`
//            PriorityQueue<Integer> pq_new = new PriorityQueue(Comparator.reverseOrder());
//            PriorityQueue<Integer> pq = keyTimeListMap.getOrDefault(key, pq_new);
//            pq.add(timestamp);
//            keyTimeListMap.put(key, pq);
//
//            // update hashmap 2
//            //String new_key = key + timestamp;
//            keyValueMap.put(key + timestamp, value);
//        }
//
/**
 * time = O(log N)
 * space = O(1)
 */
//        public String get(String key, int timestamp) {
//            // edge
//            if(keyValueMap.isEmpty() || !keyTimeListMap.containsKey(key)){
//                return "";
//            }
//
//            // get `latest timestamp`
//            List<Integer> cache = new ArrayList<>();
//            PriorityQueue<Integer> saved_pq = keyTimeListMap.get(key);
//            int target_timestamp = 0;  // ??
//            while(!saved_pq.isEmpty()){
//                Integer pop_timestamp = saved_pq.poll();
//                cache.add(pop_timestamp);
//                if(pop_timestamp <= timestamp){
//                    target_timestamp = pop_timestamp;
//                    break;
//                }
//            }
//
//            // handle edge case
//            if(target_timestamp == 0){
//                return "";
//            }
//
//            // put pop element back to PQ (reset the PQ `state`)
//            for(Integer x: cache){
//                saved_pq.add(x);
//            }
//            keyTimeListMap.put(key, saved_pq);
//
//            return keyValueMap.get(key + target_timestamp);
//        }
//    }

    // V1-1
    // https://neetcode.io/problems/time-based-key-value-store
    // IDEA: BRUTE FORCE
    public class TimeMap_1_1 {
        private Map<String, Map<Integer, List<String>>> keyStore;

        public TimeMap_1_1() {
            keyStore = new HashMap<>();
        }

        /**
         * time = O(1)
         * space = O(N)
         */
        public void set(String key, String value, int timestamp) {
            if (!keyStore.containsKey(key)) {
                keyStore.put(key, new HashMap<>());
            }
            if (!keyStore.get(key).containsKey(timestamp)) {
                keyStore.get(key).put(timestamp, new ArrayList<>());
            }
            keyStore.get(key).get(timestamp).add(value);
        }

        /**
         * time = O(log N)
         * space = O(1)
         */
        public String get(String key, int timestamp) {
            if (!keyStore.containsKey(key)) {
                return "";
            }
            int seen = 0;

            for (int time : keyStore.get(key).keySet()) {
                if (time <= timestamp) {
                    seen = Math.max(seen, time);
                }
            }
            if (seen == 0) return "";
            int back = keyStore.get(key).get(seen).size() - 1;
            return keyStore.get(key).get(seen).get(back);
        }
    }

    // V1-2
    // https://neetcode.io/problems/time-based-key-value-store
    // IDEA: Binary Search (Sorted Map)
    public class TimeMap_1_2 {
        private Map<String, TreeMap<Integer, String>> m;

        public TimeMap_1_2() {
            m = new HashMap<>();
        }

        /**
         * time = O(1)
         * space = O(N)
         */
        public void set(String key, String value, int timestamp) {
            m.computeIfAbsent(key, k -> new TreeMap<>()).put(timestamp, value);
        }

        public String get(String key, int timestamp) {
            if (!m.containsKey(key)) return "";
            TreeMap<Integer, String> timestamps = m.get(key);
            Map.Entry<Integer, String> entry = timestamps.floorEntry(timestamp);
            return entry == null ? "" : entry.getValue();
        }
    }

    // V1-3
    // https://neetcode.io/problems/time-based-key-value-store
    // IDEA: Binary Search (Array)
    public class TimeMap_1_3 {

        private Map<String, List<Pair<Integer, String>>> keyStore;

        public TimeMap_1_3() {
            keyStore = new HashMap<>();
        }

        /**
         * time = O(1)
         * space = O(N)
         */
        public void set(String key, String value, int timestamp) {
            keyStore.computeIfAbsent(key, k -> new ArrayList<>()).add(new Pair<>(timestamp, value));
        }

        public String get(String key, int timestamp) {
            List<Pair<Integer, String>> values = keyStore.getOrDefault(key, new ArrayList<>());
            int left = 0, right = values.size() - 1;
            String result = "";

            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (values.get(mid).getKey() <= timestamp) {
                    result = values.get(mid).getValue();
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            return result;
        }

        private class Pair<K, V> {
            private final K key;
            private final V value;

            public Pair(K key, V value) {
                this.key = key;
                this.value = value;
            }

            public K getKey() {
                return key;
            }

            public V getValue() {
                return value;
            }
        }
    }


    // V2-1
    // IDEA : Hashmap + Linear Search
    // https://leetcode.com/problems/time-based-key-value-store/editorial/
    class TimeMap_2_1 {
        HashMap<String, HashMap<Integer, String>> keyTimeMap;
        public TimeMap_2_1() {
            keyTimeMap = new HashMap<String, HashMap<Integer, String>>();
        }

        /**
         * time = O(1)
         * space = O(N)
         */
        public void set(String key, String value, int timestamp) {
            // If the 'key' does not exist in map.
            if (!keyTimeMap.containsKey(key)) {
                keyTimeMap.put(key, new HashMap<Integer, String>());
            }

            // Store '(timestamp, value)' pair in 'key' bucket.
            keyTimeMap.get(key).put(timestamp, value);
        }

        /**
         * time = O(log N)
         * space = O(1)
         */
        public String get(String key, int timestamp) {
            // If the 'key' does not exist in map we will return empty string.
            if (!keyTimeMap.containsKey(key)) {
                return "";
            }

            // Iterate on time from 'timestamp' to '1'.
            for (int currTime = timestamp; currTime >= 1; --currTime) {
                // If a value for current time is stored in key's bucket we return the value.
                if (keyTimeMap.get(key).containsKey(currTime)) {
                    return keyTimeMap.get(key).get(currTime);
                }
            }

            // Otherwise no time <= timestamp was stored in key's bucket.
            return "";
        }
    }

    // V2-2
    // IDEA : Sorted Map + Binary Search
    // https://leetcode.com/problems/time-based-key-value-store/editorial/
    class TimeMap_2_2 {
        HashMap<String, TreeMap<Integer, String>> keyTimeMap;

        public TimeMap_2_2() {
            keyTimeMap = new HashMap<String, TreeMap<Integer, String>>();
        }

        /**
         * time = O(1)
         * space = O(N)
         */
        public void set(String key, String value, int timestamp) {
            if (!keyTimeMap.containsKey(key)) {
                keyTimeMap.put(key, new TreeMap<Integer, String>());
            }

            // Store '(timestamp, value)' pair in 'key' bucket.
            keyTimeMap.get(key).put(timestamp, value);
        }

        /**
         * time = O(log N)
         * space = O(1)
         */
        public String get(String key, int timestamp) {
            // If the 'key' does not exist in map we will return empty string.
            if (!keyTimeMap.containsKey(key)) {
                return "";
            }

            Integer floorKey = keyTimeMap.get(key).floorKey(timestamp);
            // Return searched time's value, if exists.
            if (floorKey != null) {
                return keyTimeMap.get(key).get(floorKey);
            }

            return "";
        }
    }

    // V3
    // IDEA : Array + Binary Search
    // https://leetcode.com/problems/time-based-key-value-store/editorial/
//    class TimeMap_4 {
//        HashMap<String, ArrayList<Pair<Integer, String>>> keyTimeMap;
//
//        public TimeMap_4() {
//            keyTimeMap = new HashMap();
//        }
//
/**
 * time = O(1)
 * space = O(N)
 */
//        public void set(String key, String value, int timestamp) {
//            if (!keyTimeMap.containsKey(key)) {
//                keyTimeMap.put(key, new ArrayList());
//            }
//
//            // Store '(timestamp, value)' pair in 'key' bucket.
//            keyTimeMap.get(key).add(new Pair(timestamp, value));
//        }
//
/**
 * time = O(log N)
 * space = O(1)
 */
//        public String get(String key, int timestamp) {
//            // If the 'key' does not exist in map we will return empty string.
//            if (!keyTimeMap.containsKey(key)) {
//                return "";
//            }
//
//            if (timestamp < keyTimeMap.get(key).get(0).getKey()) {
//                return "";
//            }
//
//            // Using binary search on the list of pairs.
//            int left = 0;
//            int right = keyTimeMap.get(key).size();
//
//            while (left < right) {
//                int mid = (left + right) / 2;
//                if (keyTimeMap.get(key).get(mid).getKey() <= timestamp) {
//                    left = mid + 1;
//                } else {
//                    right = mid;
//                }
//            }
//
//            // If iterator points to first element it means, no time <= timestamp exists.
//            if (right == 0) {
//                return "";
//            }
//
//            return keyTimeMap.get(key).get(right - 1).getValue();
//        }
//    }




}
