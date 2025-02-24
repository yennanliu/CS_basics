package LeetCodeJava.Design;

// https://leetcode.com/problems/design-twitter/
/**
 * 355. Design Twitter
 * Design a simplified version of Twitter where
 * users can post tweets, follow/unfollow another
 * user and is able to see the 10 most recent tweets
 * in the user's news feed. Your design should support the following methods:
 *
 *
 * postTweet(userId, tweetId): Compose a new tweet.
 * getNewsFeed(userId): Retrieve the 10 most recent tweet ids in the user's news feed. Each item in the news feed must be posted by users who the user followed or by the user herself. Tweets must be ordered from most recent to least recent.
 * follow(followerId, followeeId): Follower follows a followee.
 * unfollow(followerId, followeeId): Follower unfollows a followee.
 * Example:
 *
 * Twitter twitter = new Twitter();
 *
 * // User 1 posts a new tweet (id = 5).
 * twitter.postTweet(1, 5);
 *
 * // User 1's news feed should return a list with 1 tweet id -> [5].
 * twitter.getNewsFeed(1);
 *
 * // User 1 follows user 2.
 * twitter.follow(1, 2);
 *
 * // User 2 posts a new tweet (id = 6).
 * twitter.postTweet(2, 6);
 *
 * // User 1's news feed should return a list with 2 tweet ids -> [6, 5].
 * // Tweet id 6 should precede tweet id 5 because it is posted after tweet id 5.
 * twitter.getNewsFeed(1);
 *
 * // User 1 unfollows user 2.
 * twitter.unfollow(1, 2);
 *
 * // User 1's news feed should return a list with 1 tweet id -> [5],
 * // since user 1 is no longer following user 2.
 * twitter.getNewsFeed(1);
 * Difficulty:
 * Medium
 * Lock:
 * Normal
 * Company:
 * Amazon DoorDash Twitter Yelp
 * Problem Solution
 * 355-Design-Twitter
 *
 */

//import javafx.util.Pair;

import java.util.*;

public class DesignTwitter {

    // V0
    // TODO : implement

    // V0-1
    // IDEA: HASHMAP + PQ (gpt)
    class Twitter_0_1 {

        private HashMap<Integer, Set<Integer>> followers; // followerId -> set of followeeIds
        private HashMap<Integer, List<Tweet>> userTweets; // userId -> list of tweets
        private int timestamp;

        private class Tweet {
            int tweetId;
            int timestamp;

            public Tweet(int tweetId, int timestamp) {
                this.tweetId = tweetId;
                this.timestamp = timestamp;
            }
        }

        /** Initialize your data structure here. */
        public Twitter_0_1() {
            this.followers = new HashMap<>();
            this.userTweets = new HashMap<>();
            this.timestamp = 0;
        }

        /** Compose a new tweet. */
        public void postTweet(int userId, int tweetId) {
            userTweets.putIfAbsent(userId, new ArrayList<>());
            userTweets.get(userId).add(0, new Tweet(tweetId, timestamp++)); // Add to the front for recent tweets
        }

        /**
         * Retrieve the 10 most recent tweet ids in the user's news feed. Each item in the news feed
         * must be posted by users who the user followed or by the user herself. Tweets must be ordered
         * from most recent to least recent.
         */
        public List<Integer> getNewsFeed(int userId) {
            PriorityQueue<Tweet> maxHeap = new PriorityQueue<>((a, b) -> b.timestamp - a.timestamp); // Max heap based on timestamp
            Set<Integer> followedUsers = new HashSet<>();
            followedUsers.add(userId); // Include the user's own tweets
            if (followers.containsKey(userId)) {
                followedUsers.addAll(followers.get(userId));
            }

            for (int followedUserId : followedUsers) {
                if (userTweets.containsKey(followedUserId)) {
                    maxHeap.addAll(userTweets.get(followedUserId));
                }
            }

            List<Integer> newsFeed = new ArrayList<>();
            int count = 0;
            while (!maxHeap.isEmpty() && count < 10) {
                newsFeed.add(maxHeap.poll().tweetId);
                count++;
            }
            return newsFeed;
        }

        /** Follower follows a followee. If the operation is invalid, it should be a no-op. */
        public void follow(int followerId, int followeeId) {
            if (followerId == followeeId) {
                return; // Cannot follow self
            }
            followers.putIfAbsent(followerId, new HashSet<>());
            followers.get(followerId).add(followeeId);
        }

        /** Follower unfollows a followee. If the operation is invalid, it should be a no-op. */
        public void unfollow(int followerId, int followeeId) {
            if (followers.containsKey(followerId)) {
                followers.get(followerId).remove(followeeId);
            }
        }
    }

    // V1
    // https://leetcode.com/problems/design-twitter/solutions/2720611/java-simple-hashmap-stack/
//    class Twitter_1 {
//        Stack<Pair<Integer,Integer>> tweets;
//        HashMap<Integer, Set<Integer>> network;
//
//        public Twitter_1() {
//            tweets = new Stack<>();
//            network = new HashMap<>();
//        }
//
//        public void postTweet(int userId, int tweetId) {
//            tweets.push(new Pair(userId, tweetId));
//        }
//
//        public List<Integer> getNewsFeed(int userId) {
//            Stack<Pair<Integer, Integer>> store = new Stack<>();
//            List<Integer> ans = new ArrayList<>();
//            Set<Integer> following = network.getOrDefault(userId, new HashSet<>());
//
//            while(tweets.size()!=0 && ans.size()!=10){
//                Pair<Integer, Integer> curr = tweets.pop();
//                if((following.contains(curr.getKey()) || curr.getKey()==userId)
//                        && ans.size()!=10) ans.add(curr.getValue());
//                store.push(curr);
//            }
//            while(store.size()!=0) tweets.push(store.pop());
//            return ans;
//        }
//
//        public void follow(int followerId, int followeeId) {
//            Set<Integer> following = network.getOrDefault(followerId, new HashSet<>());
//            following.add(followeeId);
//            network.put(followerId, following);
//        }
//
//        public void unfollow(int followerId, int followeeId) {
//            Set<Integer> following = network.get(followerId);
//            if(following == null) return;
//            following.remove(followeeId);
//            network.put(followerId, following);
//        }
//    }


    // V1_1
    // https://leetcode.com/problems/design-twitter/solutions/3869194/beats-100-8ms-java/
    class Twitter_1_1 {

        ArrayList<Tweet> tweets;
        HashMap<Integer, User> users;

        public Twitter_1_1() {
            tweets = new ArrayList<>();
            users = new HashMap<>();
        }

        public void postTweet(int userId, int tweetId) {
            if(!users.containsKey(userId))
                users.put(userId, new User(userId));
            tweets.add(new Tweet(tweetId, userId));
        }

        public List<Integer> getNewsFeed(int userId) {
            if(!users.containsKey(userId))
                users.put(userId, new User(userId));
            List<Integer> ans = new ArrayList<Integer>();
            int i = tweets.size()-1;
            while(ans.size()<10 && i>=0) {
                if(tweets.get(i).userId==userId || users.get(userId).follows.contains(tweets.get(i).userId))
                    ans.add(tweets.get(i).tweetId);
                i--;
            }
            return ans;
        }

        public void follow(int followerId, int followeeId) {
            if(!users.containsKey(followerId))
                users.put(followerId, new User(followerId));
            if(!users.containsKey(followeeId))
                users.put(followeeId, new User(followeeId));
            User user = users.get(followerId);
            user.follows.add(followeeId);
        }

        public void unfollow(int followerId, int followeeId) {
            if(!users.containsKey(followerId))
                users.put(followerId, new User(followerId));
            if(!users.containsKey(followeeId))
                users.put(followeeId, new User(followeeId));
            User user = users.get(followerId);
            user.follows.remove(followeeId);
        }
    }

    class Tweet {
        int tweetId;
        int userId;
        Tweet(int t, int u) {
            tweetId = t;
            userId = u;
        }
    }

    class User {
        int userId;
        HashSet<Integer> follows;
        User(int u) {
            userId = u;
            follows = new HashSet<>();
        }
    }

    // V2
    // https://leetcode.com/problems/design-twitter/solutions/3400455/simple-solution-for-beginners-with-step-by-step-explanation/
//    class Twitter_3 {
//
//        HashMap<Integer,ArrayList<Pair<Integer,Integer>>> posttweet ; // map<userid, < tweetId, time>>
//        HashMap<Integer,Set<Integer>> followers ;
//        int time;
//
//        public Twitter_3() {
//            posttweet = new HashMap<>();
//            followers = new HashMap<>();
//            time=0;
//        }
//
//        public void postTweet(int userId, int tweetId) {
//            Pair<Integer,Integer> pair = new Pair<>(tweetId,time);
//            ArrayList temp = posttweet.getOrDefault(userId,new ArrayList<>());
//            temp.add(pair);
//            posttweet.put(userId,temp);
//            time++;
//        }
//
//        public List<Integer> getNewsFeed(int userId) {
//            List<Integer> list = new ArrayList<>();
//            Set<Integer> set = followers.getOrDefault(userId,new HashSet<>());
//            set.add(userId);
//            list.addAll(set);
//            HashMap<Integer,Integer> map = new HashMap<>();
//            for(int i : list){
//                if(posttweet.get(i)!=null)
//                    for(Pair<Integer,Integer> j : posttweet.get(i)){
//                        map.put(j.getKey(),j.getValue());
//                    }
//            }
//
//            PriorityQueue<Map.Entry<Integer,Integer>> pq = new PriorityQueue<>(((x,y)->x.getValue()-y.getValue()));
//            pq.addAll(map.entrySet());
//            while(pq.size()>10){
//                pq.poll();
//            }
//            List<Integer> ans = new ArrayList<>();
//            while(pq.size()!=0){
//                ans.add(pq.poll().getKey());
//            }Collections.reverse(ans);
//            return ans;
//        }
//
//        public void follow(int followerId, int followeeId) {
//            Set<Integer> set = followers.getOrDefault(followerId, new HashSet<>());
//            set.add(followeeId);
//            followers.put(followerId,set);
//        }
//
//        public void unfollow(int followerId, int followeeId) {
//            Set<Integer> set = followers.getOrDefault(followerId, new HashSet<>());
//            if(set.contains(followeeId))
//                set.remove(followeeId);
//            followers.put(followerId,set);
//        }
//    }

}
