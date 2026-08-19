package LeetCodeJava.HashTable;

// https://leetcode.com/problems/subdomain-visit-count/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  811. Subdomain Visit Count
 *  Medium
 *
 *  A website domain "discuss.leetcode.com" consists of various subdomains.
 *  At the top level, we have "com", at the next level, we have "leetcode.com" and at
 *  the lowest level, "discuss.leetcode.com". When we visit a domain like
 *  "discuss.leetcode.com", we will also visit the parent domains "leetcode.com" and "com"
 *  implicitly.
 *
 *  A count-paired domain is a domain that has one of the two formats
 *  "rep d1.d2.d3" or "rep d1.d2" where rep is the number of visits to the domain.
 *
 *  Given an array of count-paired domains cpdomains, return an array of the
 *  count-paired domains of each subdomain in the input. You may return the answer
 *  in any order.
 *
 *  Example 1:
 *  Input: cpdomains = ["9001 discuss.leetcode.com"]
 *  Output: ["9001 leetcode.com","9001 discuss.leetcode.com","9001 com"]
 *
 *  Example 2:
 *  Input: cpdomains = ["900 google.mail.com","50 yahoo.com","1 intel.mail.com","5 wiki.org"]
 *  Output: ["901 mail.com","50 yahoo.com","900 google.mail.com","5 wiki.org","5 org","1 intel.mail.com","951 com"]
 *
 *  Constraints:
 *  1 <= cpdomain.length <= 100
 *  1 <= cpdomain[i].length <= 100
 */
public class SubdomainVisitCount {

    // V0
    // IDEA: HASHMAP. for each entry, add its count to every suffix of the domain
    /**
     * time = O(n * L)
     * space = O(n * L)
     */
    public List<String> subdomainVisits(String[] cpdomains) {
        Map<String, Integer> cnt = new HashMap<>();

        for (String cp : cpdomains) {
            if (cp == null || cp.trim().isEmpty()) {
                continue;
            }
            String[] parts = cp.split(" ");
            int visits = Integer.parseInt(parts[0]);
            String domain = parts[1];

            // add count to the full domain and to every parent domain
            cnt.put(domain, cnt.getOrDefault(domain, 0) + visits);
            for (int i = 0; i < domain.length(); i++) {
                if (domain.charAt(i) == '.') {
                    String sub = domain.substring(i + 1);
                    cnt.put(sub, cnt.getOrDefault(sub, 0) + visits);
                }
            }
        }

        List<String> res = new ArrayList<>();
        for (Map.Entry<String, Integer> e : cnt.entrySet()) {
            res.add(e.getValue() + " " + e.getKey());
        }
        return res;
    }
}
