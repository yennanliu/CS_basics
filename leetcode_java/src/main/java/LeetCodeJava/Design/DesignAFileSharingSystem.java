package LeetCodeJava.Design;

// https://leetcode.com/problems/design-a-file-sharing-system/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

/**
 *  1500. Design a File Sharing System
 *  Medium
 *
 *  We will use a file-sharing system to share a very large file which consists of m small
 *  chunks with IDs from 1 to m.
 *
 *  When users join the system, the system should assign a unique ID to them. The unique ID
 *  should be used once for each user, but when a user leaves the system, the ID can be reused.
 *
 *  Users can request a certain chunk of the file, the system should return a list of IDs of
 *  all the users who have this chunk. After that, if at least one other user has this chunk,
 *  the user who requested the chunk will get it.
 *
 *  Implement the FileSharing class:
 *
 *   - FileSharing(int m) Initializes the object with the number of chunks of the file m.
 *   - int join(List<Integer> ownedChunks) A new user joined owning some chunks; assign the
 *     smallest positive integer id not taken by any other user and return it.
 *   - void leave(int userID) The user leaves; their chunks are no longer available.
 *   - List<Integer> request(int userID, int chunkID) Return the ids of all users that own
 *     this chunk, sorted ascending.
 *
 *  Example 1:
 *
 *  Input:
 *  ["FileSharing","join","join","join","request","request","leave","request","leave","join"]
 *  [[4],[[1,2]],[[2,3]],[[4]],[1,3],[2,2],[1],[2,1],[2],[[]]]
 *  Output:
 *  [null,1,2,3,[2],[1,2],null,[],null,1]
 *
 *  Constraints:
 *
 *   1 <= m <= 10^5
 *   0 <= ownedChunks.length <= min(100, m)
 *   1 <= ownedChunks[i] <= m
 *   1 <= chunkID <= m
 *   At most 10^4 calls will be made to join, leave and request.
 */
public class DesignAFileSharingSystem {

    // V0
    // IDEA: 2 HASH MAPS (chunk -> owner ids as TreeSet, user -> owned chunks)
    //       + MIN HEAP recycling the freed user ids so join() gets the smallest free id
    /**
     * time = O(c * log u) for join / leave (c = owned chunks), O(u) for request (copying owners)
     * space = O(m + total owned chunks)
     */
    private final Map<Integer, TreeSet<Integer>> chunkToUsers;
    private final Map<Integer, Set<Integer>> userToChunks;
    private final PriorityQueue<Integer> freeIds;
    private int nextId;

    public DesignAFileSharingSystem(int m) {
        this.chunkToUsers = new HashMap<>();
        this.userToChunks = new HashMap<>();
        this.freeIds = new PriorityQueue<>();
        this.nextId = 1;
    }

    public int join(List<Integer> ownedChunks) {
        int userID;
        if (!freeIds.isEmpty()) {
            userID = freeIds.poll();
        } else {
            userID = nextId;
            nextId++;
        }
        Set<Integer> owned = new HashSet<>();
        for (Integer chunk : ownedChunks) {
            owned.add(chunk);
            TreeSet<Integer> users = chunkToUsers.get(chunk);
            if (users == null) {
                users = new TreeSet<>();
                chunkToUsers.put(chunk, users);
            }
            users.add(userID);
        }
        userToChunks.put(userID, owned);
        return userID;
    }

    public void leave(int userID) {
        Set<Integer> owned = userToChunks.remove(userID);
        if (owned == null) {
            return;
        }
        for (Integer chunk : owned) {
            TreeSet<Integer> users = chunkToUsers.get(chunk);
            if (users != null) {
                users.remove(userID);
                if (users.isEmpty()) {
                    chunkToUsers.remove(chunk);
                }
            }
        }
        freeIds.add(userID);
    }

    public List<Integer> request(int userID, int chunkID) {
        List<Integer> res = new ArrayList<>();
        TreeSet<Integer> users = chunkToUsers.get(chunkID);
        if (users == null || users.isEmpty()) {
            return res;
        }
        res.addAll(users);

        // the requester actually gets the chunk only if someone else owns it
        Set<Integer> owned = userToChunks.get(userID);
        if (owned != null) {
            owned.add(chunkID);
            users.add(userID);
        }
        return res;
    }
}
