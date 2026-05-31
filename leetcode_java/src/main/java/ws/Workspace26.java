package ws;

//import LeetCodeJava.DataStructure.TreeNode;
//import com.sun.org.apache.bcel.internal.generic.IINC;
//import com.sun.org.apache.bcel.internal.generic.SIPUSH;


import com.sun.org.apache.bcel.internal.generic.IINC;

import java.math.BigInteger;
import java.util.*;


public class Workspace26 {

    // LC 362
    // 3.34 - 44 pm
    // IDEA:TREE MAP ????
    /**
     *  -> Design a hit counter
     *    which counts the number of
     *    hits received in the `past 5 minutes.`
     *
     *
     *    --------
     *
     *     // IDEA 1) TREE MAP ????
     *
     *
     */
    class HitCounter {

        // attr
        // map: { timestamp : cnt }
        Map<Integer, Integer> map;

//        /** Initialize your data structure here. */
        public HitCounter() {
            //counter = new HashMap<>(); // ?? check
            // ??? via below, we can `loop` over tree map's
            // key in reverse order ?? (e.g. big -> small)
            map = new TreeMap<>(Comparator.reverseOrder()); // ???
        }

        /**
         Record a hit.
         @param timestamp - The current timestamp (in seconds granularity).
         */
        public void hit(int timestamp) {
            // ??
            map.put(timestamp, map.getOrDefault(timestamp, 0) + 1);
        }

        /**
         Return the number of hits in the past 5 minutes.
         @param timestamp - The current timestamp (in seconds granularity).
         */
        public int getHits(int timestamp) {
            // ???
            int cnt = 0;
            for(int k: map.keySet()){
                // if(k < timestamp - 5 * 60){ ???
//                if(k < timestamp - (5 * 60 - 1)){
//                    break;
//                }
                // outside 5-minute window
                if (k <= timestamp - 300) {
                    break;
                }

                cnt += map.get(k);
            }

            return cnt;
        }
    }






}
