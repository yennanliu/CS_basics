"""

1236. Web Crawler
Medium

Given a url startUrl and an interface HtmlParser, implement a web crawler to crawl all links that are under the same hostname as startUrl.

Return all urls obtained by your web crawler in any order.

Your crawler should:

- Start from the page: startUrl
- Call HtmlParser.getUrls(url) to get all urls from a webpage of given url.
- Do not crawl the same link twice.
- Explore only the links that are under the same hostname as startUrl.

As shown in the example url above, the hostname is example.org. For simplicity sake, you may assume all urls use http protocol without any port specified. For example, the urls http://leetcode.com/problems and http://leetcode.com/contest are under the same hostname, while urls http://example.org/test and http://example.com/abc are not under the same hostname.

The HtmlParser interface is defined as such:

interface HtmlParser {
  // Return a list of all urls from a webpage of given url.
  public List<String> getUrls(String url);
}

Below are two examples explaining the functionality of the problem, for custom testing purposes you'll have three variables urls, edges and startUrl. Notice that you will only have access to startUrl in your code, while urls and edges are not directly accessible to you in code.

Note: Consider the same URL with the trailing slash "/" as a different URL. For example, "http://news.yahoo.com", and "http://news.yahoo.com/" are different urls.


Example 1:

Input:
urls = [
  "http://news.yahoo.com",
  "http://news.yahoo.com/news",
  "http://news.yahoo.com/news/topics/",
  "http://news.google.com",
  "http://news.yahoo.com/us"
]
edges = [[2,0],[2,1],[3,2],[3,1],[0,4]]
startUrl = "http://news.yahoo.com/news/topics/"
Output: [
  "http://news.yahoo.com",
  "http://news.yahoo.com/news",
  "http://news.yahoo.com/news/topics/",
  "http://news.yahoo.com/us"
]

Example 2:

Input:
urls = [
  "http://news.yahoo.com",
  "http://news.yahoo.com/news",
  "http://news.yahoo.com/news/topics/",
  "http://news.google.com"
]
edges = [[0,2],[2,1],[3,2],[3,1],[3,0]]
startUrl = "http://news.google.com"
Output: ["http://news.google.com"]
Explanation: The startUrl links to all other pages that do not share the same hostname.


Constraints:

1 <= urls.length <= 1000
1 <= urls[i].length <= 300
startUrl is one of the urls.
Hostname label must be from 1 to 63 characters long, including the dots, may contain only the ASCII letters from 'a' to 'z', digits from '0' to '9' and the hyphen-minus character ('-').
The hostname may not start or end with the hyphen-minus character ('-').
You may assume there're no duplicates in url library.

"""

# """
# This is HtmlParser's API interface.
# You should not implement it, or speculate about its implementation
# """
# class HtmlParser(object):
#    def getUrls(self, url):
#        """
#        :type url: str
#        :rtype List[str]
#        """

# V0
# IDEA : BFS + visited set (iterative, avoids deep recursion)
# time = O(V + E)
# space = O(V)
from collections import deque
class Solution(object):
    def crawl(self, startUrl, htmlParser):
        """
        NOTE !!!
            all urls use `http://` prefix (7 chars),
            so hostname = text after index 7, up to the next '/'
        """
        def host(url):
            return url[7:].split('/')[0]

        target = host(startUrl)
        visited = set([startUrl])
        q = deque([startUrl])
        while q:
            cur = q.popleft()
            for nxt in htmlParser.getUrls(cur):
                if nxt not in visited and host(nxt) == target:
                    visited.add(nxt)
                    q.append(nxt)
        return list(visited)


# V1
# IDEA : DFS (recursive)
# time = O(V + E)
# space = O(V)
class Solution(object):
    def crawl(self, startUrl, htmlParser):
        def host(url):
            return url[7:].split('/')[0]

        def dfs(url):
            if url in res:
                return
            res.add(url)
            for nxt in htmlParser.getUrls(url):
                if host(nxt) == target:
                    dfs(nxt)

        target = host(startUrl)
        res = set()
        dfs(startUrl)
        return list(res)
