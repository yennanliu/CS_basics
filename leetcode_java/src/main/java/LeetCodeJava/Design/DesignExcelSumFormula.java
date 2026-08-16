package LeetCodeJava.Design;

// https://leetcode.com/problems/design-excel-sum-formula/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 631. Design Excel Sum Formula
 * Hard
 * Lock: Prime
 *
 * Design the basic function of Excel and implement the function of the sum formula.
 *
 * Implement the Excel class:
 *
 * - Excel(int height, char width) Initializes the object with the height and the width of
 *   the sheet. The sheet is an integer matrix mat of size height x width with the row index
 *   in the range [1, height] and the column index in the range ['A', width].
 *   All the values should be zero initially.
 * - void set(int row, char column, int val) Changes the value at mat[row][column] to be val.
 * - int get(int row, char column) Returns the value at mat[row][column].
 * - int sum(int row, char column, List<String> numbers) Sets the value at mat[row][column]
 *   to be the sum of cells represented by numbers and returns the value at mat[row][column].
 *   This sum formula should exist until this cell is overlapped by another value or another
 *   sum formula. numbers[i] could be on the format:
 *     - "ColRow" that represents a single cell.
 *       For example, "F7" represents the cell mat[7]['F'].
 *     - "ColRow1:ColRow2" that represents a range of cells. The range will always be a
 *       rectangle where "ColRow1" represent the position of the top-left cell, and
 *       "ColRow2" represents the position of the bottom-right cell.
 *       For example, "B3:F7" represents the cells mat[i][j] for 3 <= i <= 7
 *       and 'B' <= j <= 'F'.
 *
 * Note: You could assume that there will not be any circular sum reference.
 * For example, mat[1]['A'] == sum(1, "B") and mat[1]['B'] == sum(1, "A").
 *
 * Example 1:
 *
 * Input
 * ["Excel", "set", "sum", "set", "get"]
 * [[3, "C"], [1, "A", 2], [3, "C", ["A1", "A1:B2"]], [2, "B", 2], [3, "C"]]
 * Output
 * [null, null, 4, null, 6]
 *
 * Explanation
 * Excel excel = new Excel(3, "C");
 * excel.set(1, "A", 2);
 * excel.sum(3, "C", ["A1", "A1:B2"]); // return 4
 * excel.set(2, "B", 2);               // note mat[3]["C"] should also be changed
 * excel.get(3, "C");                  // return 6
 *
 * Constraints:
 *
 * 1 <= height <= 26
 * 'A' <= width <= 'Z'
 * 1 <= row <= height
 * 'A' <= column <= width
 * -100 <= val <= 100
 * 1 <= numbers.length <= 5
 * numbers[i] has the format "ColRow" or "ColRow1:ColRow2".
 * At most 100 calls will be made to set, get, and sum.
 *
 */
public class DesignExcelSumFormula {

    /**
     * Your Excel object will be instantiated and called as such:
     * Excel obj = new Excel(height, width);
     * obj.set(row, column, val);
     * int param_2 = obj.get(row, column);
     * int param_3 = obj.sum(row, column, numbers);
     */

    // V0
    // IDEA: LAZY EVALUATION -- store the formula, resolve it recursively on get()
    /**
     *   TWO pieces of state per cell:
     *     - mat[r][c]     : a plain LITERAL value
     *     - formula[r][c] : a map {referenced cell -> HOW MANY TIMES it is referenced}
     *
     *   NOTE !!! the count matters -- a cell can legitimately appear TWICE,
     *            e.g. ["A1", "A1:B2"] references A1 two times.
     *
     *   set()  WIPES any formula on that cell and writes a literal.
     *   sum()  RECORDS the formula (so later edits to referenced cells propagate).
     *   get()  walks the formula tree RECURSIVELY. No circular reference is
     *          guaranteed by the problem, so the recursion always terminates.
     *
     *   The sheet is at most 26 x 26 and at most 100 calls are made, so the naive
     *   recursive re-evaluation is plenty fast (no topological sort needed).
     *
     *   time  = O(1) for set, O(cells referenced transitively) for get / sum
     *   space = O(height * width)
     */
    class Excel {

        private int w;
        private int[][] mat;
        // (row, colIdx) encoded as row * 32 + colIdx -> {referenced cell -> times}
        private Map<Integer, Map<Integer, Integer>> formula;

        public Excel(int height, char width) {
            this.w = width - 'A' + 1;
            // rows are 1-INDEXED -> allocate height + 1 rows and ignore row 0
            this.mat = new int[height + 1][this.w];
            this.formula = new HashMap<>();
        }

        public void set(int row, char column, int val) {
            int c = column - 'A';
            // a LITERAL write overrides any formula previously attached to this cell
            formula.remove(key(row, c));
            mat[row][c] = val;
        }

        public int get(int row, char column) {
            return eval(row, column - 'A');
        }

        public int sum(int row, char column, List<String> numbers) {
            int c = column - 'A';

            Map<Integer, Integer> refs = new HashMap<>();
            for (String token : numbers) {
                if (token.contains(":")) {
                    String[] parts = token.split(":");
                    int[] s = parse(parts[0]); // {colIdx, row}
                    int[] e = parse(parts[1]);
                    for (int rr = s[1]; rr <= e[1]; rr++) {
                        for (int cc = s[0]; cc <= e[0]; cc++) {
                            int k = key(rr, cc);
                            refs.put(k, refs.getOrDefault(k, 0) + 1);
                        }
                    }
                } else {
                    int[] p = parse(token);
                    int k = key(p[1], p[0]);
                    refs.put(k, refs.getOrDefault(k, 0) + 1);
                }
            }

            formula.put(key(row, c), refs);
            return eval(row, c);
        }

        /** resolve a cell: follow its formula if it has one, else read the literal */
        private int eval(int r, int c) {
            Map<Integer, Integer> refs = formula.get(key(r, c));
            if (refs != null) {
                int total = 0;
                for (Map.Entry<Integer, Integer> e : refs.entrySet()) {
                    int rr = e.getKey() / 32;
                    int cc = e.getKey() % 32;
                    total += eval(rr, cc) * e.getValue();
                }
                return total;
            }
            return mat[r][c];
        }

        private int key(int row, int col) {
            return row * 32 + col;
        }

        /** "F7" -> {col index 5, row 7} */
        private int[] parse(String token) {
            return new int[] { token.charAt(0) - 'A', Integer.parseInt(token.substring(1)) };
        }
    }


    // V1
    // IDEA: EAGER PROPAGATION (push updates to dependents on set)
    /**
     *  V0 is LAZY: get() re-walks the formula tree every time. This version is
     *  EAGER -- every cell stores a concrete value, and set() pushes the delta out
     *  to everything that references it, transitively.
     *
     *  -> get() becomes O(1); the cost moves to set(), which is the right trade
     *     when reads dominate writes (the usual spreadsheet profile).
     *
     *  time  = O(1) get, O(affected cells) set / sum
     *  space = O(height * width + references)
     */
    class Excel_1 {

        private int[][] mat;
        private Map<Integer, Map<Integer, Integer>> formula;  // cell -> refs (with multiplicity)
        private Map<Integer, Set<Integer>> dependents;        // cell -> who reads it
        private int w;

        public Excel_1(int height, char width) {
            this.w = width - 'A' + 1;
            this.mat = new int[height + 1][w];
            this.formula = new HashMap<>();
            this.dependents = new HashMap<>();
        }

        public void set(int row, char column, int val) {
            int c = column - 'A';
            detach(key(row, c));
            formula.remove(key(row, c));
            write(row, c, val);
        }

        public int get(int row, char column) {
            return mat[row][column - 'A'];   // always up to date
        }

        public int sum(int row, char column, List<String> numbers) {
            int c = column - 'A';
            detach(key(row, c));

            Map<Integer, Integer> refs = new HashMap<>();
            for (String token : numbers) {
                if (token.contains(":")) {
                    String[] parts = token.split(":");
                    int[] s = parseCell(parts[0]);
                    int[] e = parseCell(parts[1]);
                    for (int rr = s[1]; rr <= e[1]; rr++) {
                        for (int cc = s[0]; cc <= e[0]; cc++) {
                            refs.merge(key(rr, cc), 1, Integer::sum);
                        }
                    }
                } else {
                    int[] p = parseCell(token);
                    refs.merge(key(p[1], p[0]), 1, Integer::sum);
                }
            }

            formula.put(key(row, c), refs);
            for (int ref : refs.keySet()) {
                dependents.computeIfAbsent(ref, x -> new HashSet<>()).add(key(row, c));
            }

            int total = 0;
            for (Map.Entry<Integer, Integer> e : refs.entrySet()) {
                total += mat[e.getKey() / 32][e.getKey() % 32] * e.getValue();
            }
            write(row, c, total);
            return total;
        }

        /** write a value and cascade to every cell whose formula reads this one */
        private void write(int row, int c, int val) {
            mat[row][c] = val;
            for (int dep : dependents.getOrDefault(key(row, c), Collections.emptySet())) {
                recompute(dep / 32, dep % 32);
            }
        }

        private void recompute(int row, int c) {
            Map<Integer, Integer> refs = formula.get(key(row, c));
            if (refs == null) {
                return;
            }
            int total = 0;
            for (Map.Entry<Integer, Integer> e : refs.entrySet()) {
                total += mat[e.getKey() / 32][e.getKey() % 32] * e.getValue();
            }
            write(row, c, total);
        }

        /** drop this cell from the dependent lists of everything it used to read */
        private void detach(int cell) {
            Map<Integer, Integer> old = formula.get(cell);
            if (old == null) {
                return;
            }
            for (int ref : old.keySet()) {
                Set<Integer> deps = dependents.get(ref);
                if (deps != null) {
                    deps.remove(cell);
                }
            }
        }

        private int key(int row, int col) {
            return row * 32 + col;
        }

        private int[] parseCell(String token) {
            return new int[] { token.charAt(0) - 'A', Integer.parseInt(token.substring(1)) };
        }
    }

    // V2
    // IDEA: LAZY, BUT EVALUATED BY AN EXPLICIT TOPOLOGICAL SWEEP (no recursion)
    /**
     *  Same lazy model as V0, except get() resolves the dependency sub-DAG with an
     *  ITERATIVE post-order walk over an explicit stack rather than by recursing.
     *
     *  The problem guarantees no cycles, but an iterative sweep also makes a cycle
     *  DETECTABLE (a node still grey when revisited) instead of blowing the stack.
     *
     *  time  = O(cells referenced transitively) per get
     *  space = O(height * width)
     */
    class Excel_2 {

        private int[][] mat;
        private Map<Integer, Map<Integer, Integer>> formula;
        private int w;

        public Excel_2(int height, char width) {
            this.w = width - 'A' + 1;
            this.mat = new int[height + 1][w];
            this.formula = new HashMap<>();
        }

        public void set(int row, char column, int val) {
            int c = column - 'A';
            formula.remove(key(row, c));
            mat[row][c] = val;
        }

        public int get(int row, char column) {
            return evalIterative(key(row, column - 'A'));
        }

        public int sum(int row, char column, List<String> numbers) {
            int c = column - 'A';
            Map<Integer, Integer> refs = new HashMap<>();
            for (String token : numbers) {
                if (token.contains(":")) {
                    String[] parts = token.split(":");
                    int[] s = parseCell(parts[0]);
                    int[] e = parseCell(parts[1]);
                    for (int rr = s[1]; rr <= e[1]; rr++) {
                        for (int cc = s[0]; cc <= e[0]; cc++) {
                            refs.merge(key(rr, cc), 1, Integer::sum);
                        }
                    }
                } else {
                    int[] p = parseCell(token);
                    refs.merge(key(p[1], p[0]), 1, Integer::sum);
                }
            }
            formula.put(key(row, c), refs);
            return evalIterative(key(row, c));
        }

        /** iterative post-order: resolve children before the parent */
        private int evalIterative(int target) {
            Map<Integer, Integer> value = new HashMap<>();
            Deque<int[]> stack = new ArrayDeque<>();   // {cell, expandedFlag}
            stack.push(new int[] { target, 0 });

            while (!stack.isEmpty()) {
                int[] top = stack.peek();
                int cell = top[0];

                if (value.containsKey(cell)) {
                    stack.pop();
                    continue;
                }
                Map<Integer, Integer> refs = formula.get(cell);
                if (refs == null) {
                    value.put(cell, mat[cell / 32][cell % 32]);
                    stack.pop();
                    continue;
                }
                if (top[1] == 0) {
                    top[1] = 1;
                    for (int ref : refs.keySet()) {
                        if (!value.containsKey(ref)) {
                            stack.push(new int[] { ref, 0 });
                        }
                    }
                    continue;
                }
                int total = 0;
                for (Map.Entry<Integer, Integer> e : refs.entrySet()) {
                    total += value.get(e.getKey()) * e.getValue();
                }
                value.put(cell, total);
                stack.pop();
            }

            return value.get(target);
        }

        private int key(int row, int col) {
            return row * 32 + col;
        }

        private int[] parseCell(String token) {
            return new int[] { token.charAt(0) - 'A', Integer.parseInt(token.substring(1)) };
        }
    }

    // V3
    // IDEA: KEEP THE RAW FORMULA TOKENS AND RE-EXPAND THEM ON EVERY get
    /**
     *  Store the ORIGINAL strings ("A1", "A1:B2") instead of a resolved reference
     *  multiset, and expand the ranges again each time the cell is read.
     *
     *  Slower, but it keeps the user's formula verbatim -- so the sheet could
     *  round-trip, display, or edit the formula, which the pre-resolved versions
     *  have already thrown away.
     *
     *  time  = O(cells referenced transitively * tokens) per get
     *  space = O(height * width)
     */
    class Excel_3 {

        private int[][] mat;
        private Map<Integer, List<String>> formula;
        private int w;

        public Excel_3(int height, char width) {
            this.w = width - 'A' + 1;
            this.mat = new int[height + 1][w];
            this.formula = new HashMap<>();
        }

        public void set(int row, char column, int val) {
            int c = column - 'A';
            formula.remove(key(row, c));
            mat[row][c] = val;
        }

        public int get(int row, char column) {
            return evalTokens(row, column - 'A');
        }

        public int sum(int row, char column, List<String> numbers) {
            int c = column - 'A';
            formula.put(key(row, c), new ArrayList<>(numbers));
            return evalTokens(row, c);
        }

        private int evalTokens(int row, int c) {
            List<String> tokens = formula.get(key(row, c));
            if (tokens == null) {
                return mat[row][c];
            }
            int total = 0;
            for (String token : tokens) {
                if (token.contains(":")) {
                    String[] parts = token.split(":");
                    int[] s = parseCell(parts[0]);
                    int[] e = parseCell(parts[1]);
                    for (int rr = s[1]; rr <= e[1]; rr++) {
                        for (int cc = s[0]; cc <= e[0]; cc++) {
                            total += evalTokens(rr, cc);
                        }
                    }
                } else {
                    int[] p = parseCell(token);
                    total += evalTokens(p[1], p[0]);
                }
            }
            return total;
        }

        private int key(int row, int col) {
            return row * 32 + col;
        }

        private int[] parseCell(String token) {
            return new int[] { token.charAt(0) - 'A', Integer.parseInt(token.substring(1)) };
        }
    }

}
