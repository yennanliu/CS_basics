import java.util.Arrays;

/**
 *  QUICK FIND -- the eager union-find
 *
 *  Union-Find (a.k.a. Disjoint Set Union) answers one question: are p
 *  and q in the same group? Quick Find is the simplest possible answer.
 *
 *  INVARIANT: id[p] == id[q] exactly when p and q are connected.
 *  The id value is the group label; it carries no other meaning.
 *
 *      start        id = [0, 1, 2, 3, 4, 5]     6 separate groups
 *      union(1, 3)  id = [0, 3, 2, 3, 4, 5]     every 1 becomes a 3
 *      union(0, 3)  id = [3, 3, 2, 3, 4, 5]     every 0 becomes a 3
 *      connected(0, 1) -> id[0] == id[1] -> true
 *
 *  THE TRADE-OFF: find() is a single array read, but union() has to
 *  RELABEL the whole array. Building N-1 unions therefore costs O(N^2),
 *  which is too slow for large inputs -- see QuickUnionUF.java for the
 *  lazy alternative that fixes it.
 *
 *  Time  : constructor O(N), find O(1), connected O(1), union O(N)
 *  Space : O(N)
 *
 *  Reference: https://www.coursera.org/learn/algorithms-part1/lecture/OLXM8/union-find-applications
 */
public class QuickFindUF {

    private final int[] id;   // id[i] = the group label of element i
    private int count;        // number of distinct groups

    /** Start with N elements, each in its own group. */
    public QuickFindUF(int n) {
        id = new int[n];
        count = n;
        for (int i = 0; i < n; i++) {
            id[i] = i;
        }
    }

    /** Number of groups remaining. */
    public int count() {
        return count;
    }

    /** The group label of p -- one array read. */
    public int find(int p) {
        validate(p);
        return id[p];
    }

    /** True when p and q are in the same group. */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    /**
     *  Merge the groups of p and q.
     *
     *  The expensive part: EVERY element carrying p's label has to be
     *  relabelled, so this is a full scan of the array.
     */
    public void union(int p, int q) {
        int pid = find(p);
        int qid = find(q);
        if (pid == qid) {
            return;                       // already together, nothing to do
        }
        for (int i = 0; i < id.length; i++) {
            if (id[i] == pid) {           // NOTE: compare against the CACHED pid.
                id[i] = qid;              // Reading id[p] inside the loop would
            }                             // break once id[p] itself is rewritten.
        }
        count--;
    }

    @Override
    public String toString() {
        return Arrays.toString(id);
    }

    private void validate(int p) {
        if (p < 0 || p >= id.length) {
            throw new IndexOutOfBoundsException("element " + p + " is not in 0.." + (id.length - 1));
        }
    }

    public static void main(String[] args) {
        QuickFindUF uf = new QuickFindUF(6);
        assertThat(uf.count() == 6, "every element starts alone");
        assertThat(!uf.connected(1, 3), "nothing is connected yet");

        uf.union(1, 3);
        assertThat(uf.toString().equals("[0, 3, 2, 3, 4, 5]"), "every 1 relabelled to 3");
        assertThat(uf.connected(1, 3) && uf.count() == 5, "1 and 3 joined");

        uf.union(0, 3);
        assertThat(uf.toString().equals("[3, 3, 2, 3, 4, 5]"), "every 0 relabelled to 3");
        assertThat(uf.connected(0, 1), "connection is transitive: 0-3 and 1-3 means 0-1");

        uf.union(0, 1);                   // already together
        assertThat(uf.count() == 4, "a redundant union does not change the count");

        uf.union(4, 5);
        assertThat(uf.count() == 3, "{0,1,3} {2} {4,5}");
        assertThat(!uf.connected(0, 4), "the two groups are still separate");

        try {
            uf.find(99);
            assertThat(false, "expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // ok
        }

        System.out.println("id    : " + uf);
        System.out.println("groups: " + uf.count());
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
