package LeetCodeJava.Set;

// https://leetcode.com/problems/destination-city/description/

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  1436. Destination City
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given the array paths, where paths[i] = [cityAi, cityBi] means there exists a direct path going from cityAi to cityBi. Return the destination city, that is, the city without any path outgoing to another city.
 *
 * It is guaranteed that the graph of paths forms a line without any loop, therefore, there will be exactly one destination city.
 *
 *
 *
 * Example 1:
 *
 * Input: paths = [["London","New York"],["New York","Lima"],["Lima","Sao Paulo"]]
 * Output: "Sao Paulo"
 * Explanation: Starting at "London" city you will reach "Sao Paulo" city which is the destination city. Your trip consist of: "London" -> "New York" -> "Lima" -> "Sao Paulo".
 * Example 2:
 *
 * Input: paths = [["B","C"],["D","B"],["C","A"]]
 * Output: "A"
 * Explanation: All possible trips are:
 * "D" -> "B" -> "C" -> "A".
 * "B" -> "C" -> "A".
 * "C" -> "A".
 * "A".
 * Clearly the destination city is "A".
 * Example 3:
 *
 * Input: paths = [["A","Z"]]
 * Output: "Z"
 *
 *
 * Constraints:
 *
 * 1 <= paths.length <= 100
 * paths[i].length == 2
 * 1 <= cityAi.length, cityBi.length <= 10
 * cityAi != cityBi
 * All strings consist of lowercase and uppercase English letters and the space character.
 *
 *
 */
public class DestinationCity {

    // V0
    // IDEA: HASH SET
    public String destCity(List<List<String>> paths) {
        // edge

        Set<String> all = new HashSet<>();
        Set<String> outgoing = new HashSet<>();

        for (List<String> p : paths) {
            outgoing.add(p.get(0));

            // ??
            for (String x : p) {
                all.add(x);
            }
        }

        // get res
        for (String y : all) {
            if (!outgoing.contains(y)) {
                return y;
            }
        }

        return null;
    }


    // V0-1
    // IDEA: HASHSET (gemini)
    public String destCity_0_1(List<List<String>> paths) {
        // 1. Store all cities that have an OUTGOING path
        Set<String> outgoingCities = new HashSet<>();
        for (List<String> path : paths) {
            outgoingCities.add(path.get(0));
        }

        // 2. Check each DESTINATION city
        // If a destination city is NOT in the outgoing set, it is the final city.
        for (List<String> path : paths) {
            String candidate = path.get(1);
            if (!outgoingCities.contains(candidate)) {
                return candidate;
            }
        }

        return "";
    }


    // V1-1
    // IDEA: BRUTE FORCE
    // https://leetcode.com/problems/destination-city/editorial/
    public String destCity_1_1(List<List<String>> paths) {
        for (int i = 0; i < paths.size(); i++) {
            String candidate = paths.get(i).get(1);
            boolean good = true;

            for (int j = 0; j < paths.size(); j++) {
                if (paths.get(j).get(0).equals(candidate)) {
                    good = false;
                    break;
                }
            }

            if (good) {
                return candidate;
            }
        }

        return "";
    }


    // V1-2
    // IDEA: HASHSET
    // https://leetcode.com/problems/destination-city/editorial/
    public String destCity_1_2(List<List<String>> paths) {
        Set<String> hasOutgoing = new HashSet();
        for (int i = 0; i < paths.size(); i++) {
            hasOutgoing.add(paths.get(i).get(0));
        }

        for (int i = 0; i < paths.size(); i++) {
            String candidate = paths.get(i).get(1);
            if (!hasOutgoing.contains(candidate)) {
                return candidate;
            }
        }

        return "";
    }




    // V2




}
