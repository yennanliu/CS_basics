package LeetCodeJava.Design;

// https://leetcode.com/problems/web-crawler/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  1236. Web Crawler
 *  Medium
 *
 *  Given a url startUrl and an interface HtmlParser, implement a web crawler to crawl all
 *  links that are under the same hostname as startUrl.
 *
 *  Return all urls obtained by your web crawler in any order.
 *
 *  Your crawler should:
 *   - Start from the page: startUrl
 *   - Call HtmlParser.getUrls(url) to get all urls from a webpage of given url.
 *   - Do not crawl the same link twice.
 *   - Explore only the links that are under the same hostname as startUrl.
 *
 *  For simplicity sake, you may assume all urls use http protocol without any port
 *  specified. For example, http://leetcode.com/problems and http://leetcode.com/contest are
 *  under the same hostname, while http://example.org/test and http://example.com/abc are not.
 *
 *  The HtmlParser interface is defined as such:
 *
 *  interface HtmlParser {
 *    // Return a list of all urls from a webpage of given url.
 *    public List<String> getUrls(String url);
 *  }
 *
 *  Note: Consider the same URL with the trailing slash "/" as a different URL. For example,
 *  "http://news.yahoo.com" and "http://news.yahoo.com/" are different urls.
 *
 *  Example 1:
 *
 *  Input:
 *  urls = [
 *    "http://news.yahoo.com",
 *    "http://news.yahoo.com/news",
 *    "http://news.yahoo.com/news/topics/",
 *    "http://news.google.com",
 *    "http://news.yahoo.com/us"
 *  ]
 *  edges = [[2,0],[2,1],[3,2],[3,1],[0,4]]
 *  startUrl = "http://news.yahoo.com/news/topics/"
 *  Output: [
 *    "http://news.yahoo.com",
 *    "http://news.yahoo.com/news",
 *    "http://news.yahoo.com/news/topics/",
 *    "http://news.yahoo.com/us"
 *  ]
 *
 *  Example 2:
 *
 *  Input:
 *  urls = [
 *    "http://news.yahoo.com",
 *    "http://news.yahoo.com/news",
 *    "http://news.yahoo.com/news/topics/",
 *    "http://news.google.com"
 *  ]
 *  edges = [[0,2],[2,1],[3,2],[3,1],[3,0]]
 *  startUrl = "http://news.google.com"
 *  Output: ["http://news.google.com"]
 *  Explanation: The startUrl links to all other pages that do not share the same hostname.
 *
 *  Constraints:
 *
 *   1 <= urls.length <= 1000
 *   1 <= urls[i].length <= 300
 *   startUrl is one of the urls.
 *   Hostname labels are 1..63 chars of [a-z0-9-] (may not start/end with '-').
 *   You may assume there're no duplicates in url library.
 */
public class WebCrawler {

    /**
     *  This is HtmlParser's API interface.
     *  You should not implement it, or speculate about its implementation.
     */
    public static interface HtmlParser {
        public List<String> getUrls(String url);
    }

    // V0
    // IDEA: BFS OVER THE LINK GRAPH + A "visited" SET
    //       the pages form a directed graph; crawling it is a plain graph traversal where
    //       getUrls(u) plays the role of "neighbours of u".
    //
    //       two filters on every neighbour:
    //          1) same hostname as startUrl  -> stay inside the site
    //          2) not visited yet            -> never crawl a link twice
    //       hostname = the chunk between "http://" and the next '/', so it is just
    //       indexOf('/', 7) (7 = length of "http://"); -1 means the url IS the host.
    //       NOTE: urls are compared VERBATIM (a trailing '/' makes a different url), so the
    //             visited set needs no normalisation.
    /**
     * time = O(V + E)   // one getUrls call per crawled page
     * space = O(V)
     */
    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        String host = hostOf(startUrl);

        Set<String> visited = new HashSet<>();
        Deque<String> queue = new ArrayDeque<>();
        visited.add(startUrl);
        queue.add(startUrl);

        while (!queue.isEmpty()) {
            String cur = queue.poll();
            for (String next : htmlParser.getUrls(cur)) {
                if (visited.contains(next)) {
                    continue;
                }
                if (!host.equals(hostOf(next))) {
                    continue;
                }
                visited.add(next);
                queue.add(next);
            }
        }
        return new ArrayList<>(visited);
    }

    // "http://news.yahoo.com/news/topics/" -> "news.yahoo.com"
    private String hostOf(String url) {
        final int protocolLen = 7; // "http://"
        int slash = url.indexOf('/', protocolLen);
        return slash == -1 ? url.substring(protocolLen) : url.substring(protocolLen, slash);
    }
}
