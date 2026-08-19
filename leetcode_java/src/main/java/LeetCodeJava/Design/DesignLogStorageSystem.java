package LeetCodeJava.Design;

// https://leetcode.com/problems/design-log-storage-system/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  635. Design Log Storage System
 *  Medium
 *
 *  You are given several logs, where each log contains a unique ID and timestamp.
 *  Timestamp is a string that has the following format: Year:Month:Day:Hour:Minute:Second,
 *  for example, "2017:01:01:23:59:59". All domains are zero-padded decimal numbers.
 *
 *  Implement the LogSystem class:
 *   - void put(int id, String timestamp) Stores the given log (id, timestamp).
 *   - List<Integer> retrieve(String start, String end, String granularity) Returns the IDs of
 *     the logs whose timestamps are within the range from start to end inclusive.
 *     start and end have the same format as timestamp, and granularity means the time
 *     level to consider (e.g. "Day" means compare only Year:Month:Day).
 *
 *  Example 1:
 *    put(1, "2017:01:01:23:59:59");
 *    put(2, "2017:01:01:22:59:59");
 *    put(3, "2016:01:01:00:00:00");
 *    retrieve("2016:01:01:01:01:01", "2017:01:01:23:00:00", "Year");  // [1,2,3]
 *    retrieve("2016:01:01:01:01:01", "2017:01:01:23:00:00", "Hour");  // [1,2]
 *
 *  Constraints:
 *    There will be at most 300 operations of put or retrieve.
 *    Year ranges from [2000, 2017]. Hour ranges from [00, 23].
 *    Output for retrieve has no order required.
 */
public class DesignLogStorageSystem {

    // V0
    // IDEA: keep the logs as (id, timestamp) pairs and truncate every timestamp to the
    //       requested granularity. Zero-padded "Y:M:D:H:M:S" strings compare correctly
    //       LEXICOGRAPHICALLY, so the range test is just start <= cur <= end.
    /**
     * time = O(1) put, O(n) retrieve
     * space = O(n)
     */
    private final List<Integer> ids;        // parallel to `stamps`
    private final List<String> stamps;
    private final Map<String, Integer> cut; // granularity -> prefix length

    public DesignLogStorageSystem() {
        this.ids = new ArrayList<>();
        this.stamps = new ArrayList<>();
        this.cut = new HashMap<>();
        this.cut.put("Year", 4);
        this.cut.put("Month", 7);
        this.cut.put("Day", 10);
        this.cut.put("Hour", 13);
        this.cut.put("Minute", 16);
        this.cut.put("Second", 19);
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    public void put(int id, String timestamp) {
        this.ids.add(id);
        this.stamps.add(timestamp);
    }

    /**
     * time = O(n)
     * space = O(n)
     */
    public List<Integer> retrieve(String start, String end, String granularity) {

        int k = this.cut.get(granularity);

        String lo = start.substring(0, k);
        String hi = end.substring(0, k);

        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < this.stamps.size(); i++) {
            String cur = this.stamps.get(i).substring(0, k);
            if (lo.compareTo(cur) <= 0 && cur.compareTo(hi) <= 0) {
                res.add(this.ids.get(i));
            }
        }

        return res;
    }
}
