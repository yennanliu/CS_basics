package LeetCodeJava.Array;

// https://leetcode.com/problems/count-mentions-per-user/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 3433. Count Mentions Per User
 * Medium
 *
 * You are given an integer numberOfUsers representing the total number of users
 * and an array events of size n x 3.
 *
 * Each events[i] can be either of the following two types:
 *
 * Message Event: ["MESSAGE", "timestamp_i", "mentions_string_i"]
 *
 * This event indicates that a set of users was mentioned in a message at
 * timestamp_i.
 * The mentions_string_i string can contain one of the following tokens:
 *
 * id<number>: where <number> is an integer in range [0,numberOfUsers - 1]. There
 * can be multiple ids separated by a single whitespace and may contain duplicates.
 * This can mention even the offline users.
 * ALL: mentions all users.
 * HERE: mentions all online users.
 *
 * Offline Event: ["OFFLINE", "timestamp_i", "id_i"]
 *
 * This event indicates that the user id_i had become offline at timestamp_i for 60
 * time units. The user will automatically be online again at time timestamp_i +
 * 60.
 *
 * Return an array mentions where mentions[i] represents the number of mentions the
 * user with id i has across all MESSAGE events.
 *
 * All users are initially online, and if a user goes offline or comes back online,
 * their status change is processed before handling any message event that occurs
 * at the same timestamp.
 *
 * Note that a user can be mentioned multiple times in a single message event, and
 * each mention should be counted separately.
 *
 * Example 1:
 *
 * Input: numberOfUsers = 2, events = [["MESSAGE","10","id1 id0"],["OFFLINE","11","0"],["MESSAGE","71","HERE"]]
 *
 * Output: [2,2]
 *
 * Explanation:
 *
 * Initially, all users are online.
 *
 * At timestamp 10, id1 and id0 are mentioned. mentions = [1,1]
 *
 * At timestamp 11, id0 goes offline.
 *
 * At timestamp 71, id0 comes back online and "HERE" is mentioned. mentions = [2,2]
 *
 * Example 2:
 *
 * Input: numberOfUsers = 2, events = [["MESSAGE","10","id1 id0"],["OFFLINE","11","0"],["MESSAGE","12","ALL"]]
 *
 * Output: [2,2]
 *
 * Explanation:
 *
 * Initially, all users are online.
 *
 * At timestamp 10, id1 and id0 are mentioned. mentions = [1,1]
 *
 * At timestamp 11, id0 goes offline.
 *
 * At timestamp 12, "ALL" is mentioned. This includes offline users, so both id0
 * and id1 are mentioned. mentions = [2,2]
 *
 * Example 3:
 *
 * Input: numberOfUsers = 2, events = [["OFFLINE","10","0"],["MESSAGE","12","HERE"]]
 *
 * Output: [0,1]
 *
 * Explanation:
 *
 * Initially, all users are online.
 *
 * At timestamp 10, id0 goes offline.
 *
 * At timestamp 12, "HERE" is mentioned. Because id0 is still offline, they will
 * not be mentioned. mentions = [0,1]
 *
 * Constraints:
 *
 * 1 <= numberOfUsers <= 100
 * 1 <= events.length <= 100
 * events[i].length == 3
 * events[i][0] will be one of MESSAGE or OFFLINE.
 * 1 <= int(events[i][1]) <= 10^5
 * The number of id<number> mentions in any "MESSAGE" event is between 1 and 100.
 * 0 <= <number> <= numberOfUsers - 1
 * It is guaranteed that the user id referenced in the OFFLINE event is online at
 * the time the event occurs.
 *
 */
public class CountMentionsPerUser {

    private static final int OFFLINE_DURATION = 60;

    // V0
    // IDEA: SORT EVENTS BY (time, OFFLINE before MESSAGE) + AN "OFFLINE UNTIL" CLOCK PER USER
    /**
     * time = O(E log E + E * U)
     * space = O(U + E)
     */
    public int[] countMentions(int numberOfUsers, List<List<String>> events) {
        // put the events on a timeline; at equal timestamps a status change lands first
        List<List<String>> ordered = new ArrayList<>(events);
        ordered.sort((a, b) -> compareEvents(a, b));

        int[] mentions = new int[numberOfUsers];
        // user u is online at time t iff offlineUntil[u] <= t
        int[] offlineUntil = new int[numberOfUsers];

        for (List<String> event : ordered) {
            String type = event.get(0);
            int time = Integer.parseInt(event.get(1));
            String payload = event.get(2);

            if (type.equals("OFFLINE")) {
                int user = Integer.parseInt(payload);
                offlineUntil[user] = time + OFFLINE_DURATION;
            } else if (payload.equals("ALL")) {
                // ALL mentions everyone, online or not
                for (int u = 0; u < numberOfUsers; u++) {
                    mentions[u]++;
                }
            } else if (payload.equals("HERE")) {
                // HERE mentions only the users online right now
                for (int u = 0; u < numberOfUsers; u++) {
                    if (offlineUntil[u] <= time) {
                        mentions[u]++;
                    }
                }
            } else {
                // explicit "id<k>" tokens; duplicates each count
                for (String token : payload.split(" ")) {
                    int user = Integer.parseInt(token.substring(2));
                    mentions[user]++;
                }
            }
        }
        return mentions;
    }

    /** Orders events by timestamp, then OFFLINE before MESSAGE. */
    private int compareEvents(List<String> a, List<String> b) {
        int timeA = Integer.parseInt(a.get(1));
        int timeB = Integer.parseInt(b.get(1));
        if (timeA != timeB) {
            return Integer.compare(timeA, timeB);
        }
        return Integer.compare(typeRank(a.get(0)), typeRank(b.get(0)));
    }

    /** OFFLINE sorts before MESSAGE at the same timestamp. */
    private int typeRank(String type) {
        if (type.equals("OFFLINE")) {
            return 0;
        }
        return 1;
    }
}
