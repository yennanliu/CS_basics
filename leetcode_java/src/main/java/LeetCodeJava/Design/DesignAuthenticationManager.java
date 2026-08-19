package LeetCodeJava.Design;

// https://leetcode.com/problems/design-authentication-manager/

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *  1797. Design Authentication Manager
 *  Medium
 *
 *  There is an authentication system that works with authentication tokens. For each session,
 *  the user will receive a new authentication token that will expire timeToLive seconds after
 *  the currentTime. If the token is renewed, the expiry time will be extended to expire
 *  timeToLive seconds after the (potentially different) currentTime.
 *
 *  Implement the AuthenticationManager class:
 *
 *   - AuthenticationManager(int timeToLive) sets the timeToLive.
 *   - void generate(String tokenId, int currentTime) generates a new token.
 *   - void renew(String tokenId, int currentTime) renews the unexpired token with the given
 *     tokenId. If there is no unexpired token with that id, the request is ignored.
 *   - int countUnexpiredTokens(int currentTime) returns the number of unexpired tokens.
 *
 *  Note that if a token expires at time t, and another action happens at time t, the
 *  expiration takes place before the other action.
 *
 *  Example 1:
 *
 *  Input
 *  ["AuthenticationManager","renew","generate","countUnexpiredTokens","generate","renew",
 *   "renew","countUnexpiredTokens"]
 *  [[5],["aaa",1],["aaa",2],[6],["bbb",7],["aaa",8],["bbb",10],[15]]
 *  Output
 *  [null,null,null,1,null,null,null,0]
 *
 *  Constraints:
 *
 *   1 <= timeToLive <= 10^8
 *   1 <= currentTime <= 10^8
 *   1 <= tokenId.length <= 5, lowercase letters only
 *   All calls to generate contain unique tokenId values.
 *   currentTime across all calls is strictly increasing.
 *   At most 2000 calls will be made to all functions combined.
 */
public class DesignAuthenticationManager {

    // V0
    // IDEA: HASH MAP tokenId -> expiry time; lazily drop expired entries when counting
    /**
     * time = O(1) for generate / renew, O(n) for countUnexpiredTokens
     * space = O(n), n = number of live tokens
     */
    private final int timeToLive;
    private final Map<String, Integer> expireAt;

    public DesignAuthenticationManager(int timeToLive) {
        this.timeToLive = timeToLive;
        this.expireAt = new HashMap<>();
    }

    public void generate(String tokenId, int currentTime) {
        expireAt.put(tokenId, currentTime + timeToLive);
    }

    public void renew(String tokenId, int currentTime) {
        Integer exp = expireAt.get(tokenId);
        // token expires at `exp`, so it is still alive only if exp > currentTime
        if (exp != null && exp > currentTime) {
            expireAt.put(tokenId, currentTime + timeToLive);
        }
    }

    public int countUnexpiredTokens(int currentTime) {
        Iterator<Map.Entry<String, Integer>> it = expireAt.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getValue() <= currentTime) {
                it.remove();
            }
        }
        return expireAt.size();
    }
}
