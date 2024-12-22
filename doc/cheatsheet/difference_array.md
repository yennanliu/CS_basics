# Difference Array 

- The main applicable scenario is to frequently query the cumulative sum of a certain interval when the original array will not be modified

## 0) Concept
- [fucking algorithm - Difference Array](https://labuladong.online/algo/data-structure/diff-array/)

### 0-1) Types
- LC 1109
- LC 370
- LC 1094

### 0-2) Pattern

```java

// java
// https://labuladong.online/algo/data-structure/diff-array/

class PrefixSum {
    // 前缀和数组
    private int[] preSum;

    // 输入一个数组，构造前缀和
    public PrefixSum(int[] nums) {
        // preSum[0] = 0，便于计算累加和
        preSum = new int[nums.length + 1];
        // 计算 nums 的累加和
        for (int i = 1; i < preSum.length; i++) {
            preSum[i] = preSum[i - 1] + nums[i - 1];
        }
    }
    
    // 查询闭区间 [left, right] 的累加和
    public int sumRange(int left, int right) {
        return preSum[right + 1] - preSum[left];
    }
}
```

## 1) General form

```java

// java
// https://labuladong.online/algo/data-structure/diff-array/

// 差分数组工具类
// V1
class Difference {
    // 差分数组
    private int[] diff;
    
    // 输入一个初始数组，区间操作将在这个数组上进行
    public Difference(int[] nums) {
        assert nums.length > 0;
        diff = new int[nums.length];
        // 根据初始数组构造差分数组
        diff[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            diff[i] = nums[i] - nums[i - 1];
        }
    }

    // 给闭区间 [i, j] 增加 val（可以是负数）
    public void increment(int i, int j, int val) {
        diff[i] += val;
        if (j + 1 < diff.length) {
            diff[j + 1] -= val;
        }
    }

    // 返回结果数组
    public int[] result() {
        int[] res = new int[diff.length];
        // 根据差分数组构造结果数组
        res[0] = diff[0];
        for (int i = 1; i < diff.length; i++) {
            res[i] = res[i - 1] + diff[i];
        }
        return res;
    }
}
```

```java
// V2
// https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/AlgorithmJava/DifferenceArray.java


// method
public int[] getDifferenceArray(int[][] input, int n) {

/** LC 1109. Corporate Flight Bookings input : [start, end, seats]
 *
 *  NOTE !!!
 *
 *   in java, index start from 0;
 *   but in LC 1109, index start from 1
 *
 */
int[] tmp = new int[n + 1];
for (int[] x : input) {
  int start = x[0];
  int end = x[1];
  int seats = x[2];

  // add
  tmp[start] += seats;

  // subtract
  if (end + 1 <= n) {
    tmp[end + 1] -= seats;
  }
}

for (int i = 1; i < tmp.length; i++) {
  //tmp[i] = tmp[i - 1] + tmp[i];
    tmp[i] += tmp[i - 1];
}

return Arrays.copyOfRange(tmp, 1, n+1);
}
```

### 1-1) Basic OP

## 2) LC Example

### 2-1) Range Addition

```java
// java
// LC 370


// V0
// IDEA : DIFFERENCE ARRAY
public static int[] getModifiedArray(int length, int[][] updates) {

int[] tmp = new int[length + 1]; // or new int[length]; both works
for (int[] x : updates) {
  int start = x[0];
  int end = x[1];
  int amount = x[2];

  // add
  tmp[start] += amount;

  // subtract (remove the "adding affect" on "NEXT" element)
  /**
   * NOTE !!!
   *
   * <p>we remove the "adding affect" on NEXT element (e.g. end + 1)
   */
  if (end + 1 < length) { // NOTE !!! use `end + 1`
    tmp[end + 1] -= amount;
  }
}

// prepare final result
for (int i = 1; i < tmp.length; i++) {
  tmp[i] += tmp[i - 1];
}

return Arrays.copyOfRange(tmp, 0, length); // return the sub array between 0, lengh index
}

// V1
class Solution {
    public int[] getModifiedArray(int length, int[][] updates) {
        // nums 初始化为全 0
        int[] nums = new int[length];
        // 构造差分解法
        Difference df = new Difference(nums);
        
        for (int[] update : updates) {
            int i = update[0];
            int j = update[1];
            int val = update[2];
            df.increment(i, j, val);
        }
        
        return df.result();
    }
}
```

### 2-2) Corporate Flight Bookings

```java
// java
// LC 1109

// V1
class Solution {
    public int[] corpFlightBookings(int[][] bookings, int n) {
        // nums 初始化为全 0
        int[] nums = new int[n];
        // 构造差分解法
        Difference df = new Difference(nums);

        for (int[] booking : bookings) {
            // 注意转成数组索引要减一哦
            int i = booking[0] - 1;
            int j = booking[1] - 1;
            int val = booking[2];
            // 对区间 nums[i..j] 增加 val
            df.increment(i, j, val);
        }
        // 返回最终的结果数组
        return df.result();
    }
}
```

### 2-3) Car Pooling

```java
// java
// LC 1094
// https://leetcode.com/problems/car-pooling/description/

class Solution {
    public boolean carPooling(int[][] trips, int capacity) {
        // 最多有 1001 个车站
        int[] nums = new int[1001];

        // 构造差分解法
        Difference df = new Difference(nums);

        for (int[] trip : trips) {
            // 乘客数量
            int val = trip[0];

            // 第 trip[1] 站乘客上车
            int i = trip[1];

            // 第 trip[2] 站乘客已经下车，
            // 即乘客在车上的区间是 [trip[1], trip[2] - 1]
            int j = trip[2] - 1;

            // 进行区间操作
            df.increment(i, j, val);
        }

        int[] res = df.result();

        // 客车自始至终都不应该超载
        for (int i = 0; i < res.length; i++) {
            if (capacity < res[i]) {
                return false;
            }
        }
        return true;
    }
}
```