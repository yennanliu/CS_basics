package LeetCodeJava.Design;

// https://leetcode.com/problems/throne-inheritance/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  1600. Throne Inheritance
 *  Medium
 *
 *  A kingdom consists of a king, his children, his grandchildren, and so on. Every once in a
 *  while, someone in the family dies or a child is born.
 *
 *  The kingdom has a well-defined order of inheritance that consists of the king as the first
 *  member. Let's define the recursive function Successor(x, curOrder), which given a person x
 *  and the inheritance order so far, returns who should be the next person after x:
 *
 *  Successor(x, curOrder):
 *      if x has no children or all of x's children are in curOrder:
 *          if x is the king return null
 *          else return Successor(x's parent, curOrder)
 *      else return x's oldest child who's not in curOrder
 *
 *  Implement the ThroneInheritance class:
 *
 *   - ThroneInheritance(String kingName) Initializes an object of the ThroneInheritance
 *     class. The name of the king is given as part of the constructor.
 *   - void birth(String parentName, String childName) Indicates that parentName gave birth
 *     to childName.
 *   - void death(String name) Indicates the death of name. The death of the person doesn't
 *     affect the Successor function nor the current inheritance order. You can treat it as
 *     just marking the person as dead.
 *   - List<String> getInheritanceOrder() Returns a list representing the current order of
 *     inheritance excluding dead people.
 *
 *  Example 1:
 *
 *  Input
 *  ["ThroneInheritance", "birth", "birth", "birth", "birth", "birth", "birth",
 *   "getInheritanceOrder", "death", "getInheritanceOrder"]
 *  [["king"], ["king", "andy"], ["king", "bob"], ["king", "catherine"],
 *   ["andy", "matthew"], ["bob", "alex"], ["bob", "asha"], [null], ["bob"], [null]]
 *  Output
 *  [null, null, null, null, null, null, null,
 *   ["king","andy","matthew","bob","alex","asha","catherine"], null,
 *   ["king","andy","matthew","alex","asha","catherine"]]
 *
 *  Constraints:
 *
 *   1 <= kingName.length, parentName.length, childName.length, name.length <= 15
 *   kingName, parentName, childName, and name consist of lowercase English letters only.
 *   All arguments childName and kingName are distinct.
 *   All name arguments of death will be passed to either the constructor or as childName
 *   to birth first.
 *   For each call to birth(parentName, childName), parentName is guaranteed to be alive.
 *   At most 10^5 calls will be made to birth and death.
 *   At most 10 calls will be made to getInheritanceOrder.
 */
public class ThroneInheritance {

    // V0
    // IDEA: N-ARY TREE + PRE-ORDER DFS (the inheritance order IS a pre-order walk)
    //       Successor(...) is exactly "visit node, then recurse into children oldest-first,
    //       then back up to the parent" -> a pre-order traversal of the family tree.
    //       So birth() only appends the child to its parent's list (append keeps the age
    //       ordering for free), and death() only marks a name in a `dead` set.
    //       getInheritanceOrder() walks the tree pre-order, skipping dead names (but still
    //       recursing into their children -- a dead person's line still inherits).
    //       NOTE: done ITERATIVELY with an explicit stack, since the family chain can be
    //             10^5 deep and recursion would blow the JVM stack.
    /**
     * time = O(1) per birth / death, O(N) per getInheritanceOrder
     * space = O(N)
     */
    private final String king;
    private final Map<String, List<String>> children;
    private final Set<String> dead;

    public ThroneInheritance(String kingName) {
        this.king = kingName;
        this.children = new HashMap<>();
        this.dead = new HashSet<>();
    }

    public void birth(String parentName, String childName) {
        List<String> kids = children.get(parentName);
        if (kids == null) {
            kids = new ArrayList<>();
            children.put(parentName, kids);
        }
        kids.add(childName);
    }

    public void death(String name) {
        dead.add(name);
    }

    public List<String> getInheritanceOrder() {
        List<String> res = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        stack.push(king);
        while (!stack.isEmpty()) {
            String cur = stack.pop();
            if (!dead.contains(cur)) {
                res.add(cur);
            }
            List<String> kids = children.get(cur);
            if (kids != null) {
                // push youngest first so the oldest child is popped (visited) first
                for (int i = kids.size() - 1; i >= 0; i--) {
                    stack.push(kids.get(i));
                }
            }
        }
        return res;
    }

}
