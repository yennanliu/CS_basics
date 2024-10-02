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

    // V0
    // IDEA : HASHMAP + BINARY SEARCH
    class TimeMap {

        // attr
        Map<String, List<String>> keyValueMap;
        Map<String, List<Integer>> InsertTimeMap;

        public TimeMap() {
            this.keyValueMap = new HashMap<>();
            this.InsertTimeMap = new HashMap<>();
        }

        public void set(String key, String value, int timestamp) {

            // update keyValueMap
            List<String> values = this.keyValueMap.getOrDefault(key, new ArrayList<>());
            //values = this.keyValueMap.get(key);
            values.add(value);
            this.keyValueMap.put(key, values);

            // update InsertTimeMap
            List<Integer> times = this.InsertTimeMap.getOrDefault(key, new ArrayList<>());
            times.add(timestamp);
            this.InsertTimeMap.put(key, times);
        }

        public String get(String key, int timestamp) {
            if (!this.keyValueMap.containsKey(key)){
                return "";
            }

            // V1 : linear search (TLE)
            List<Integer> times = this.InsertTimeMap.get(key);
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
                if (val.equals(timestamp)){
                    return mid;
                }
                if (val > timestamp){
                    // NOTE !!! right  = mid - 1;
                    right = mid - 1;
                }else{
                    // NOTE !!! left = mid + 1;
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
        public void set(String key, String value, int timestamp) {
            // Add the timestamp and value to their respective lists for the given key
            keyToTimestamps.putIfAbsent(key, new ArrayList<>());
            keyToValues.putIfAbsent(key, new ArrayList<>());

            keyToTimestamps.get(key).add(timestamp);
            keyToValues.get(key).add(value);
        }

        // Get the value associated with the largest timestamp <= given timestamp
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
     * floorKey() is a method provided by the TreeMap class in Java, which returns the greatest key that is less than or equal to the given key.
     * It effectively performs a binary search on the keys of the TreeMap, allowing efficient retrieval of the closest key that is not greater than the one specified.
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

        public void set(String key, String value, int timestamp) {
            // Initialize TreeMap if key is new
            keyTimeMap.putIfAbsent(key, new TreeMap<>());
            // Insert the value with the given timestamp
            keyTimeMap.get(key).put(timestamp, value);
        }

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

        public void set(String key, String value, int timestamp) {
            List<String> values = this.map.getOrDefault(key, new ArrayList<>());
            List<Integer> timeList = this.timeArray.getOrDefault(key, new ArrayList<>());

            values.add(value);
            timeList.add(timestamp);

            this.map.put(key, values);
            this.timeArray.put(key, timeList);
        }

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

    // V1
    // IDEA : Hashmap + Linear Search
    // https://leetcode.com/problems/time-based-key-value-store/editorial/
    class TimeMap_1 {
        HashMap<String, HashMap<Integer, String>> keyTimeMap;
        public TimeMap_1() {
            keyTimeMap = new HashMap<String, HashMap<Integer, String>>();
        }

        public void set(String key, String value, int timestamp) {
            // If the 'key' does not exist in map.
            if (!keyTimeMap.containsKey(key)) {
                keyTimeMap.put(key, new HashMap<Integer, String>());
            }

            // Store '(timestamp, value)' pair in 'key' bucket.
            keyTimeMap.get(key).put(timestamp, value);
        }

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

    // V2
    // IDEA : Sorted Map + Binary Search
    // https://leetcode.com/problems/time-based-key-value-store/editorial/
    class TimeMap_2 {
        HashMap<String, TreeMap<Integer, String>> keyTimeMap;

        public TimeMap_2() {
            keyTimeMap = new HashMap<String, TreeMap<Integer, String>>();
        }

        public void set(String key, String value, int timestamp) {
            if (!keyTimeMap.containsKey(key)) {
                keyTimeMap.put(key, new TreeMap<Integer, String>());
            }

            // Store '(timestamp, value)' pair in 'key' bucket.
            keyTimeMap.get(key).put(timestamp, value);
        }

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
//        public void set(String key, String value, int timestamp) {
//            if (!keyTimeMap.containsKey(key)) {
//                keyTimeMap.put(key, new ArrayList());
//            }
//
//            // Store '(timestamp, value)' pair in 'key' bucket.
//            keyTimeMap.get(key).add(new Pair(timestamp, value));
//        }
//
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
