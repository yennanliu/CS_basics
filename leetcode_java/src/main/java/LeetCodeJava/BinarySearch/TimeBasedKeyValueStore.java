package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/time-based-key-value-store/description/

//import javafx.util.Pair;
import java.util.*;

public class TimeBasedKeyValueStore {

    // V0
    // IDEA : DICT + BINARY SEARCH
    // TODO : fix this
//    class TimeMap {
//
//        private HashMap<String, String> map = new HashMap<>();
//
//        public TimeMap() {
//            this.map = new HashMap<>();
//        }
//
//        public void set(String key, String value, int timestamp) {
//            String _time = String.valueOf(timestamp);
//            String _key = key + "_" + _time;
//            if (!this.map.containsKey(_key)){
//                this.map.put(_key, value);
//            }
//        }
//
//        public String get(String key, int timestamp) {
//            Set<String> k_set = this.map.keySet();
//            //List<Integer> tmp = Arrays.asList();
//            Integer[] tmp = new Integer[k_set.size()];
//            int j = 0;
//            for (String k : k_set){
//                if (k.contains(key)){
//                    //tmp.add(Integer.valueOf(k.split("_")[1]));
//                    tmp[j] =  Integer.valueOf(k.split("_")[1]);
//                    j += 1;
//                }
//            }
//
//            if (!tmp.equals(null) && tmp.length != 0){
//
//                System.out.println("before -------- ");
//                Arrays.stream(tmp).forEach(System.out::println);
//                System.out.println("after --");
//                if (tmp.length > 1){
//                    Arrays.sort(tmp, (a,b) -> Integer.compare(-a, -b));
//                }
//                Arrays.stream(tmp).forEach(System.out::println);
//                System.out.println("after -------- ");
//
//                System.out.println(">>> timestamp = " + timestamp + " tmp[tmp.length-1] = " + tmp[tmp.length-1] + " timestamp >= tmp[tmp.length-1] "  +(timestamp >= tmp[tmp.length-1]) );
//
//                for (int x : tmp){
//                    if (timestamp >= x){
//                        String _k = key + "_" + String.valueOf(x);
//                        return this.map.get(_k);
//                    }
//                }
//
//            }
//
//            return "";
//        }
//
//    }

    // V1
    // IDEA : Hashmap + Linear Search
    // https://leetcode.com/problems/time-based-key-value-store/editorial/
    class TimeMap_2 {
        HashMap<String, HashMap<Integer, String>> keyTimeMap;
        public TimeMap_2() {
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
    class TimeMap_3 {
        HashMap<String, TreeMap<Integer, String>> keyTimeMap;

        public TimeMap_3() {
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
