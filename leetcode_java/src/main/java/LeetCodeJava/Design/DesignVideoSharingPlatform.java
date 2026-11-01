package LeetCodeJava.Design;

// https://leetcode.com/problems/design-video-sharing-platform/description/
// https://leetcode.ca/2022-01-31-2254-Design-Video-Sharing-Platform/

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * 2254. Design Video Sharing Platform
 * Hard
 *
 * You have a video sharing platform where users can upload and delete videos. Each video is a
 * string of digits, where the ith digit of the string represents the content of the video at
 * minute i. For example, the first digit represents the content at minute 0 in the video, the
 * second digit represents the content at minute 1 in the video, and so on. Viewers of videos
 * can also like and dislike videos. Internally, the platform keeps track of the number of
 * views, likes, and dislikes on each video.
 *
 * When a video is uploaded, it is associated with the smallest available integer videoId
 * starting from 0. Once a video is deleted, the videoId associated with that video becomes
 * available again to be associated with a future video.
 *
 * Implement the VideoSharingPlatform class:
 *
 * VideoSharingPlatform() Initializes the object.
 * int upload(String video) The user uploads a video. Return the videoId associated with the video.
 * void remove(int videoId) If there is a video associated with videoId, remove the video.
 * String watch(int videoId, int startMinute, int endMinute) If there is a video associated
 * with videoId, increase the number of views on the video by 1 and return the substring of
 * the video string starting at startMinute and ending at min(endMinute, video.length - 1)
 * (inclusive). Otherwise, return "-1".
 * void like(int videoId) Increases the number of likes on the video associated with videoId
 * by 1 if there is a video associated with videoId.
 * void dislike(int videoId) Increases the number of dislikes on the video associated with
 * videoId by 1 if there is a video associated with videoId.
 * int[] getLikesAndDislikes(int videoId) Return a 0-indexed integer array values of length 2
 * where values[0] is the number of likes and values[1] is the number of dislikes on the video
 * associated with videoId. If there is no video associated with videoId, return [-1].
 * int getViews(int videoId) Return the number of views on the video associated with videoId.
 * If there is no video associated with videoId, return -1.
 *
 *
 * Example 1:
 *
 * Input
 * ["VideoSharingPlatform", "upload", "upload", "remove", "remove", "upload", "watch", "watch", "like", "dislike", "dislike", "getLikesAndDislikes", "getViews"]
 * [[], ["123"], ["456"], [4], [0], ["789"], [1, 0, 5], [1, 0, 1], [1], [1], [1], [1], [1]]
 * Output
 * [null, 0, 1, null, null, 0, "456", "45", null, null, null, [1, 2], 2]
 *
 * Explanation
 * videoSharingPlatform.upload("123");          // The smallest available videoId is 0, so return 0.
 * videoSharingPlatform.upload("456");          // The smallest available videoId is 1, so return 1.
 * videoSharingPlatform.remove(4);              // There is no video associated with videoId 4, so do nothing.
 * videoSharingPlatform.remove(0);              // Remove the video associated with videoId 0.
 * videoSharingPlatform.upload("789");          // Since the video associated with videoId 0 was deleted,
 *                                              // 0 is the smallest available videoId, so return 0.
 * videoSharingPlatform.watch(1, 0, 5);         // The video from minute 0 to min(5, 3 - 1) = 2 is "456".
 * videoSharingPlatform.watch(1, 0, 1);         // The video from minute 0 to min(1, 3 - 1) = 1 is "45".
 * videoSharingPlatform.like(1);                // 1 like on video 1.
 * videoSharingPlatform.dislike(1);             // 1 dislike on video 1.
 * videoSharingPlatform.dislike(1);             // 2 dislikes on video 1.
 * videoSharingPlatform.getLikesAndDislikes(1); // return [1, 2].
 * videoSharingPlatform.getViews(1);            // video 1 was watched twice, so return 2.
 *
 *
 * Constraints:
 *
 * 1 <= video.length <= 10^5
 * The sum of video.length over all calls to upload does not exceed 10^5
 * video consists of digits.
 * 0 <= videoId <= 10^5
 * 0 <= startMinute < endMinute < 10^5
 * startMinute < video.length
 * The sum of endMinute - startMinute over all calls to watch does not exceed 10^5.
 * At most 10^5 calls in total will be made to all functions.
 *
 */
public class DesignVideoSharingPlatform {

    // V0
    // IDEA: HASH MAP OF LIVE VIDEOS + A MIN-HEAP OF RECYCLED IDs
    //       "smallest available videoId" is the only interesting part. ids come from two places:
    //         * ids freed by remove() -> a MIN-HEAP, so the smallest comes back first
    //         * never-used ids        -> a counter that only grows
    //       a recycled id always wins, because every freed id is below the counter.
    //       everything else is bookkeeping on a per-video record, and every accessor
    //       short-circuits on a missing id.

    /** Everything the platform tracks about one uploaded video. */
    private static class Video {
        final String content;
        int views;
        int likes;
        int dislikes;

        Video(String content) {
            this.content = content;
        }
    }

    private final Map<Integer, Video> videos;
    // ids released by remove(), smallest first
    private final PriorityQueue<Integer> freeIds;
    // the smallest id that has never been handed out
    private int nextId;

    /**
     * time = O(1)
     * space = O(1)
     */
    public DesignVideoSharingPlatform() {
        this.videos = new HashMap<>();
        this.freeIds = new PriorityQueue<>();
        this.nextId = 0;
    }

    /**
     * time = O(log n)
     * space = O(L), L = video length
     */
    public int upload(String video) {
        int videoId;
        if (!this.freeIds.isEmpty()) {
            // reuse the smallest freed id
            videoId = this.freeIds.poll();
        } else {
            videoId = this.nextId;
            this.nextId++;
        }
        this.videos.put(videoId, new Video(video));
        return videoId;
    }

    /**
     * time = O(log n)
     * space = O(1)
     */
    public void remove(int videoId) {
        if (this.videos.containsKey(videoId)) {
            this.videos.remove(videoId);
            // the id becomes available for a future upload
            this.freeIds.add(videoId);
        }
    }

    /**
     * time = O(endMinute - startMinute)
     * space = O(endMinute - startMinute)
     */
    public String watch(int videoId, int startMinute, int endMinute) {
        Video video = this.videos.get(videoId);
        if (video == null) {
            return "-1";
        }
        video.views++;
        // the end is inclusive and clipped to the last minute of the video
        int lastMinute = Math.min(endMinute, video.content.length() - 1);
        return video.content.substring(startMinute, lastMinute + 1);
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    public void like(int videoId) {
        Video video = this.videos.get(videoId);
        if (video != null) {
            video.likes++;
        }
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    public void dislike(int videoId) {
        Video video = this.videos.get(videoId);
        if (video != null) {
            video.dislikes++;
        }
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    public int[] getLikesAndDislikes(int videoId) {
        Video video = this.videos.get(videoId);
        if (video == null) {
            return new int[] {-1};
        }
        return new int[] {video.likes, video.dislikes};
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    public int getViews(int videoId) {
        Video video = this.videos.get(videoId);
        if (video == null) {
            return -1;
        }
        return video.views;
    }
}
