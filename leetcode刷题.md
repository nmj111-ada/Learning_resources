# 数组 / Array

1. 53. Maximum Subarray / 最大子数组和
**难度**: Medium / 中等 | **标签**: Array, DP / 数组, 动态规划

## 原题 / Original Problem
Given an integer array `nums`, find the subarray with the largest sum, and return its sum.

给你一个整数数组 nums，请你找出一个具有最大和的连续子数组（至少包含一个元素），返回其最大和。

**示例**: nums = [-2,1,-3,4,-1,2,1,-5,4] → 6（子数组 [4,-1,2,1]）

## 代码 / Code
```java
class Solution {
    public int maxSubArray(int[] nums) {
        int curSum = nums[0];   // 以当前位置结尾的最大和
        int maxSum = nums[0];   // 全局最大值
        for (int i = 1; i < nums.length; i++) {
            curSum = Math.max(nums[i], curSum + nums[i]); // 接 or 不接
            maxSum = Math.max(maxSum, curSum);
        }
        return maxSum;
    }
}
```

## 核心决策：接不接？
走到 nums[i]，你有两个选择：
- 接到前面：curSum + nums[i]
- 另起炉灶：nums[i]（前面是累赘，不要了）
**选更大的那个** → curSum = max(nums[i], curSum + nums[i])

## 为什么叫 Kadane 算法？
本质是动态规划：dp[i] = 以 i 结尾的最大子数组和 = max(nums[i], dp[i-1] + nums[i])
空间优化到 O(1)：只用 curSum 代替整个 dp 数组。

## 易错点
- 初始值必须是 nums[0]，不能是 0（全是负数时 0 会错）
- 子数组至少包含一个元素，不能是空的

## 关键词触发
"最大子数组和" / "连续子数组最大和" → Kadane / DP

2. 56. Merge Intervals / 合并区间
**难度**: Medium / 中等 | **标签**: Array, Sorting / 数组, 排序

## 原题 / Original Problem
Given an array of `intervals` where `intervals[i] = [starti, endi]`, merge all overlapping intervals, and return an array of the non-overlapping intervals that cover all the intervals in the input.

以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi]。请你合并所有重叠的区间，并返回一个不重叠的区间数组。

**示例**: [[1,3],[2,6],[8,10],[15,18]] → [[1,6],[8,10],[15,18]]

## 代码 / Code
```java
class Solution {
    public int[][] merge(int[][] intervals) {
        // 1. 按左端点排序（必须！）
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        List<int[]> result = new ArrayList<>();
        for (int[] cur : intervals) {
            // 不重叠：上一个末尾 < 当前开头
            if (result.isEmpty() || result.get(result.size()-1)[1] < cur[0]) {
                result.add(cur);
            } else {
                // 重叠：合并，右端点取 max
                int[] last = result.get(result.size()-1);
                last[1] = Math.max(last[1], cur[1]);
            }
        }
        return result.toArray(new int[result.size()][]);
    }
}
```

## 判断重叠
当前 [c,d]，上一个 [a,b]：
- 不重叠：b < c → 新增
- 重叠：b ≥ c → 合并成 [a, max(b,d)]

## 为什么必须排序？
不排序的话输入 [[4,7],[1,4]] 无法直接遍历合并。排序保证左端点递增，只需线性扫描。

## Java 语法知识点
- `Arrays.sort(arr, (a,b) -> a[0] - b[0])`：按二维数组第一列排序
- `List<int[]> result = new ArrayList<>()`：动态数组
- `result.get(size()-1)`：取最后一个元素（Java 没有 [-1]）
- `result.toArray(new int[result.size()][])`：List 转二维数组

## 通用套路
排序 + 遍历 + 和结果里最后一个比较 → 很多"合并/去重"题都这么干。

3. 189. Rotate Array / 轮转数组
**难度**: Medium / 中等 | **标签**: Array, Math, Two Pointers / 数组, 数学, 双指针

## 原题 / Original Problem
Given an integer array `nums`, rotate the array to the right by `k` steps, where `k` is non-negative.

给定一个整数数组 nums，将数组中的元素向右轮转 k 个位置，其中 k 是非负数。

**示例**: nums = [1,2,3,4,5,6,7], k=3 → [5,6,7,1,2,3,4]

## 代码 / Code（三次反转，O(1) 空间）
```java
class Solution {
    public void rotate(int[] nums, int k) {
        int n = nums.length;
        k %= n;
        reverse(nums, 0, n - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, n - 1);
    }
    private void reverse(int[] nums, int l, int r) {
        while (l < r) {
            int tmp = nums[l];
            nums[l] = nums[r];
            nums[r] = tmp;
            l++; r--;
        }
    }
}
```

## 核心思路：三次反转
① 全反转 → 尾部 k 个元素到了前面，但顺序反了
② 前 k 个反转 → 纠正前面 k 个的顺序
③ 后 n-k 个反转 → 纠正后面的顺序
反转两次 = 回到原位，恰到好处只翻转一次。

## 三种解法
| 方法 | 时间 | 空间 |
|------|------|------|
| 逐位模拟 | O(n·k) | O(1) |
| 新数组拷贝 | O(n) | O(n) |
| **三次反转** | O(n) | **O(1)** |

## 易错点
- k %= n 不能忘，k 可能比 n 大
- reverse 边界：前 k 个 [0, k-1]，后面 [k, n-1]

## 关键词触发
"轮转数组" / "向右移动 k 位" / "原地 O(1)" → 三次反转

4. 238. Product of Array Except Self / 除自身以外数组的乘积
**难度**: Medium / 中等 | **标签**: Array, Prefix Sum / 数组, 前缀积

## 原题 / Original Problem
Given an integer array `nums`, return an array `answer` such that `answer[i]` is equal to the product of all the elements of `nums` except `nums[i]`. You must write an algorithm in O(n) time and without using division.

给你一个整数数组 nums，返回数组 answer，其中 answer[i] 等于 nums 中除了 nums[i] 之外其余各元素的乘积。不要使用除法，O(n) 时间。

**示例**: nums = [1,2,3,4] → [24,12,8,6]

## 代码 / Code
```java
class Solution {
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] answer = new int[n];
        // 第一遍 左→右：answer[i] = 左边所有数的乘积
        answer[0] = 1;
        for (int i = 1; i < n; i++) {
            answer[i] = answer[i - 1] * nums[i - 1];
        }
        // 第二遍 右→左：乘上右边所有数的乘积
        int right = 1;
        for (int i = n - 1; i >= 0; i--) {
            answer[i] *= right;
            right *= nums[i];
        }
        return answer;
    }
}
```

## 核心思路：前缀积 × 后缀积
answer[i] = 左边累乘 × 右边累乘
- 第一遍 左→右：answer[i] = 左边所有数的乘积
- 第二遍 右→左：answer[i] *= right（右边累乘），right 跟着更新

## 为什么 i 从 1 开始不是 0？
answer[0] 左边没有元素，积为 1，需要手动设。
如果 i=0 进去：answer[0] = answer[-1] × nums[-1] → 越界 💥

## 易错点
- 初始 answer[0] = 1，right = 1
- 不能用除法（题目禁止），不能两层循环（O(n²) 超时）
- 空间 O(1)（不算 answer 数组），只用一个 right 变量

## 关键词触发
"除自身外乘积" / "不用除法" / "O(n)" → 前缀积 + 后缀积

5. 73. Set Matrix Zeroes / 矩阵置零
**难度**: Medium / 中等 | **标签**: Array, Hash Table, Matrix / 数组, 哈希表, 矩阵

## 原题 / Original Problem
Given an m x n integer matrix, if an element is 0, set its entire row and column to 0's. You must do it **in place**.

给定一个 m×n 的矩阵，如果一个元素为 0，则将其所在行和列的所有元素都设为 0。使用原地算法。

**示例**: [[1,1,1],[1,0,1],[1,1,1]] → [[1,0,1],[0,0,0],[1,0,1]]

## 代码 / Code
```java
class Solution {
    public void setZeroes(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        boolean firstRowZero = false, firstColZero = false;
        for (int j = 0; j < n; j++) if (matrix[0][j] == 0) firstRowZero = true;
        for (int i = 0; i < m; i++) if (matrix[i][0] == 0) firstColZero = true;
        // 内部遇 0 → 标记到第一行和第一列
        for (int i = 1; i < m; i++)
            for (int j = 1; j < n; j++)
                if (matrix[i][j] == 0) { matrix[i][0] = 0; matrix[0][j] = 0; }
        // 根据标记把内部非零元素也清零（关键！）
        for (int i = 1; i < m; i++)
            for (int j = 1; j < n; j++)
                if (matrix[i][0] == 0 || matrix[0][j] == 0) matrix[i][j] = 0;
        // 最后处理第一行和第一列
        if (firstRowZero) for (int j = 0; j < n; j++) matrix[0][j] = 0;
        if (firstColZero) for (int i = 0; i < m; i++) matrix[i][0] = 0;
    }
}
```

## 核心思路：第一行 + 第一列当标记板，O(1) 额外空间
不能边遍历边改 → 分不清"原本是 0"还是"后来清的"。
① 两布尔记录第一行/列本身有无 0
② 内部遇 0 → 标记到 matrix[i][0] 和 matrix[0][j]
③ 走每个内部位置，查标记 → 非零也清零
④ 根据布尔处理第一行和第一列

## 易错点
- 第三步不是"把 0 改成 0"，是把和 0 同行同列的**非零元素**清零
- 第一行/列要先独立检查，最后单独处理（否则标记数据被污染）
- 顺序：标记 → 清零内部 → 清零第一行/列，不能乱

## 关键词触发
"矩阵置零" / "原地" / "行和列清零" → 首行首列标记法

6. 35. Search Insert Position / 搜索插入位置
**难度**: Easy / 简单 | **标签**: Array, Binary Search / 数组, 二分查找

## 原题 / Original Problem
Given a sorted array of distinct integers and a target value, return the index if the target is found. If not, return the index where it would be if it were inserted in order. Must run in O(log n).

给定一个排序数组和一个目标值，在数组中找到目标值并返回其索引。如果不存在，返回它将会被按顺序插入的位置。必须 O(log n)。

**示例**: [1,3,5,6], target=5 → 2；target=2 → 1；target=7 → 4

## 代码 / Code（标准二分模板）
```java
class Solution {
    public int searchInsert(int[] nums, int target) {
        int left = 0, right = nums.length;  // 开区间！right = 长度
        while (left < right) {
            int mid = (left + right) / 2;
            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
}
```

## 过程追踪 / Walkthrough
```
nums=[1,3,5,6], target=2（不存在，插入位置1）

第1轮: mid=2, nums[2]=5, 5<2? 否 → right=2
第2轮: mid=1, nums[1]=3, 3<2? 否 → right=1
第3轮: mid=0, nums[0]=1, 1<2? 是 → left=1
left=1, right=1 → 退出 → return 1 ✅
```

## 标准二分模板三要点
1. `right = nums.length`（开区间）→ 不用处理 length-1 边界
2. `nums[mid] < target` → `left = mid + 1`（left 必须前进）
3. 否则 → `right = mid`（right 必缩小）
循环结束 left==right 直接返回。存在和插入位置统一处理。

## 我犯的错 / My Mistakes
- 死循环：`left = 0` 重置回开头 + `left = min` 不+1 → 区间永不缩小
- `right = nums[i]` 把值当下标，min 用下标和值混着算
- 自己手写二分差一错一，最后背标准模板一次过

## 关键词触发 / Triggers
"排序数组" / "O(log n)" / "插入位置" → 标准二分模板

# 二分查找 / Binary Search

1. 74. Search a 2D Matrix / 搜索二维矩阵
**难度**: Medium / 中等 | **标签**: Array, Binary Search, Matrix / 数组, 二分查找, 矩阵

## 原题 / Original Problem

Write an algorithm that searches for a target value in an `m x n` integer matrix. The algorithm must have `O(log(m*n))` time complexity.

给定一个满足有序条件的 `m x n` 整数矩阵，判断 `target` 是否在矩阵中。

**示例**: `[[1,3,5,7],[10,11,16,20],[23,30,34,60]], target=3 → true`

## 代码 / Code

```java
class Solution {
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;

        int left = 0;
        int right = m * n - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            int row = mid / n;
            int col = mid % n;

            int value = matrix[row][col];

            if (value == target) {
                return true;
            } else if (value < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return false;
    }
}
```

## 核心思路：二维数组看成一维有序数组

题目保证：

```text
每一行从左到右递增
下一行第一个数 > 上一行最后一个数
```

所以：

```text
[1,3,5,7]
[10,11,16,20]
[23,30,34,60]
```

实际上等价于：

```text
[1,3,5,7,10,11,16,20,23,30,34,60]
```

可以直接把整个矩阵当成一个长度为 `m*n` 的有序数组做普通二分。

问题只剩：

> 一维下标 `mid` 怎么对应回二维下标？

每行有 `n` 个元素：

```text
row = mid / n
col = mid % n
```

例如：

```text
mid = 6
n = 4

row = 6 / 4 = 1
col = 6 % 4 = 2

matrix[1][2] = 16
```

## 过程追踪 / Walkthrough

```text
matrix =
[1,3,5,7]
[10,11,16,20]
[23,30,34,60]

target = 13

总元素 = 3×4 = 12
left=0, right=11

第1轮:
mid=5
row=1
col=1
matrix[1][1]=11

11 < 13
→ left=6

第2轮:
mid=8
row=2
col=0
matrix[2][0]=23

23 > 13
→ right=7

第3轮:
mid=6
row=1
col=2
matrix[1][2]=16

16 > 13
→ right=5

left=6 > right=5
→ 找不到
→ false
```

## 为什么必须整体二分？

如果一行一行搜索，最坏需要检查很多行。

题目要求：

```text
O(log(m*n))
```

所以要把整个矩阵当作一个整体进行二分。

本质：

```text
二维矩阵
↓
逻辑上一维有序数组
↓
普通二分查找
```

## 复杂度

```text
时间：O(log(m*n))
空间：O(1)
```

## 易错点 / My Mistakes

* `m = matrix.length` → 行数
* `n = matrix[0].length` → 列数
* `right = m*n-1`，不是 `m*n`
* `mid` 是一维下标，不能直接写 `matrix[mid]`
* 一维转二维必须记住：

```java
row = mid / n;
col = mid % n;
```

* `col` 是列，不要写成 `clo` 后自己看混
* 没有必要创建 `ArrayList`，题目传进来的 `matrix` 已经存在

## 关键词触发 / Triggers

"有序二维矩阵" / "O(log(m*n))" / "矩阵整体有序" → **二维转一维 + 二分**

7. 41. First Missing Positive / 缺失的第一个正数
       **难度**: Hard / 困难 | **标签**: Array, Hash Table, In-place, Cyclic Sort / 数组, 哈希表, 原地, 原地哈希

## 原题 / Original Problem

Given an unsorted integer array `nums`, return the smallest positive integer that does not appear in `nums`. The algorithm must run in `O(n)` time and use `O(1)` extra space.

给定一个未排序的整数数组 nums，请你找出其中没有出现的最小的正整数。要求时间复杂度 `O(n)`，并且只使用常数级别额外空间。

**示例**:

* `[1,2,0] → 3`
* `[3,4,-1,1] → 2`
* `[7,8,9,11,12] → 1`

## 代码 / Code（原地归位）

```java
class Solution {
    public int firstMissingPositive(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            while (nums[i] >= 1
                    && nums[i] <= nums.length
                    && nums[i] != nums[nums[i] - 1]) {

                int x = nums[i];

                int temp = nums[x - 1];
                nums[x - 1] = nums[i];
                nums[i] = temp;
            }
        }

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }

        return nums.length + 1;
    }
}
```

## 核心思路：数字归位

答案只可能在：

```text
1 ~ n+1
```

因为数组长度为 `n`。

所以只需要关心：

```text
1 <= nums[i] <= n
```

对于一个数字 `x`：

```text
x 应该放在下标 x-1
```

例如：

```text
1 → nums[0]
2 → nums[1]
3 → nums[2]
...
n → nums[n-1]
```

遍历数组时，如果当前数字满足：

```text
1 <= nums[i] <= n
```

而且它还没有在正确位置：

```text
nums[i] != nums[nums[i]-1]
```

就把它交换到应该去的位置。

交换以后，`nums[i]` 会换成一个新数字，所以继续 `while`，直到当前位置不能再归位。

## 过程追踪 / Walkthrough

```text
nums = [3,4,-1,1]

i=0:
nums[0]=3
3 应该去下标2
交换 → [-1,4,3,1]

nums[0]=-1
不在 1~n 中 → 停

i=1:
nums[1]=4
4 应该去下标3
交换 → [-1,1,3,4]

nums[1]=1
1 应该去下标0
交换 → [1,-1,3,4]

nums[1]=-1
停止

最终：
[1,-1,3,4]

检查：
i=0 → nums[0]=1 ✓
i=1 → nums[1]=-1 ≠ 2 ✗

答案 = 2 ✅
```

## 为什么不能“找1，再找2，再找3”？

这个思路本身是对的，但如果每找一个数字都重新遍历整个数组：

```text
找1 → O(n)
找2 → O(n)
找3 → O(n)
...
```

最坏：

```text
O(n) × O(n) = O(n²)
```

所以不能反复扫描。

原地归位后：

```text
下标 i 对应数字 i+1
```

只需要最后扫描一次即可知道第一个缺失的正整数。

## 为什么 while 里面很多次交换还是 O(n)？

虽然代码看起来是：

```text
for
 └── while
```

但每次交换都会让一个数字进入它应该在的位置。

整个数组中的数字最多被归位有限次，所以总交换次数是 `O(n)`。

因此：

```text
总时间 = O(n)
额外空间 = O(1)
```

## 为什么需要 nums[i] != nums[nums[i]-1]？

防止重复数字导致死循环。

例如：

```text
nums = [1,1]
```

第二个 `1` 也想去下标 `0`。

如果没有这个条件，就可能不断交换：

```text
1 ↔ 1
```

所以：

```text
nums[i] == nums[nums[i]-1]
```

说明目标位置已经有相同数字，不需要再交换。

## 易错点 / My Mistakes

* 一开始想用额外 `result` 数组保存归位后的结果，但题目要求 `O(1)` 额外空间，不能开 `O(n)` 数组。
* `nums[x-1] = x` 不是交换，会把原来目标位置的数字覆盖掉。
* 必须真正交换：

```java
int temp = nums[x - 1];
nums[x - 1] = nums[i];
nums[i] = temp;
```

* `while` 不是固定只执行一次，一次交换后当前位置可能出现新的数字，需要继续归位。
* 找答案时不能写：

```java
else return nums.length;
```

因为当前位置正确只代表当前正确，后面仍然需要继续检查。

* `Math.min()` 不是找“最小缺失正数”，因为答案不是数组中的最小值，而是第一个缺失的正整数。
* 如果 `1~n` 全部存在，答案才是 `n+1`。

## 关键词触发 / Triggers

"最小缺失正数" / "O(n)" / "O(1)空间" → **原地哈希 / 数字归位**

看到：

```text
数字 x
```

第一反应：

```text
x → 下标 x-1
```

然后：

````text
原地归位 → 再扫描第一个 nums[i] != i+1

# 栈 / Stack

1. 20. Valid Parentheses / 有效的括号
**难度**: Easy / 简单 | **标签**: Stack, String / 栈, 字符串

## 原题 / Original Problem
Given a string `s` containing just the characters `'('`, `')'`, `'{'`, `'}'`, `'['` and `']'`, determine if the input string is valid.

给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s，判断字符串是否有效。

**示例**: "()" → true；"()[]{}" → true；"(]" → false；"([)]" → false

## 代码 / Code
```java
class Solution {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        Map<Character, Character> map = new HashMap<>();
        map.put(')', '(');
        map.put(']', '[');
        map.put('}', '{');
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) return false;
                if (stack.pop() != map.get(c)) return false;
            }
        }
        return stack.isEmpty();
    }
}
```

## 过程追踪 / Walkthrough
```
"([)]" ← 无效

'(' 入栈 → [ ( ]
'[' 入栈 → [ (, [ ]
')' 弹出 '[' vs '(' ≠ → false ✅

"([])" ← 有效
'(' 入栈 → [ ( ]
'[' 入栈 → [ (, [ ]
']' 弹出 '[' == map.get(']')='[' ✓
')' 弹出 '(' == map.get(')')='(' ✓
栈空 → true ✅
```

## 核心思路
左括号入栈，右括号弹栈比对。栈 = "等配对"的队列，后进先出保证顺序正确。

## 易错点 / Pitfalls
- `map.put('(', ')')` 方向反了！必须 **右括号 → 左括号**（因为查的是 map.get(右括号)）
- 栈空遇到右括号 → false
- 最后必须 `stack.isEmpty()`（有未闭合的左括号也是 false）

## 关键词触发 / Triggers
"括号匹配" / "有效字符串" → 栈 + 右括号到左括号映射

2. 155. Min Stack / 最小栈 ❌ 做错过
**难度**: Medium / 中等 | **标签**: Stack, Design / 栈, 设计

## 原题 / Original Problem
Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.

设计一个支持 push、pop、top 操作，并能在常数时间内检索到最小元素的栈。

**示例**: push(-2), push(0), push(-3), getMin→-3, pop(), top→0, getMin→-2

## 代码 / Code（双栈）
```java
class MinStack {
    Stack<Integer> stack = new Stack<>();
    Stack<Integer> minStack = new Stack<>();

    public MinStack() { }  // 成员变量声明时已初始化，构造方法空着

    public void push(int value) {
        stack.push(value);
        if (minStack.isEmpty()) minStack.push(value);
        else minStack.push(Math.min(value, minStack.peek()));
    }
    public void pop() {
        stack.pop();
        minStack.pop();
    }
    public int top() { return stack.peek(); }
    public int getMin() { return minStack.peek(); }
}
```

## 过程追踪 / Walkthrough
```
push(-2):  主=[-2]      min=[-2]
push(0):   主=[-2,0]    min=[-2,-2]   ← 0 不小于 -2，再记 -2
push(-3):  主=[-2,0,-3] min=[-2,-2,-3] ← -3 更小，记 -3
getMin():  min栈顶 = -3 ✅
pop():     主=[-2,0]    min=[-2,-2]
top():     主栈顶 = 0 ✅
getMin():  min栈顶 = -2 ✅
```

## 核心思路
只存一个 min 变量不行——pop 掉最小值后不知道回退到哪。
辅助栈每个位置 = 主栈对应高度的最小值，pop 时同步弹，永远正确。

## 我犯的错 / My Mistakes
- 构造方法里 `Stack<Integer> stack = new Stack<>()` → 加类型声明 = 新建局部变量，把成员变量藏了！构造方法应该空着
- `if (minStack == null)` → 成员变量永远不为 null，应该 `isEmpty()`

## 关键词触发 / Triggers
"最小栈" / "O(1) 取最小" → 双栈（主栈 + 同步最小栈）

3. 394. Decode String / 字符串解码
**难度**: Medium / 中等 | **标签**: Stack, String / 栈, 字符串

## 原题 / Original Problem
Given an encoded string, return its decoded string. The encoding rule is `k[encoded_string]`, where the `encoded_string` inside the square brackets is repeated exactly k times.

给定一个经过编码的字符串，返回解码后的字符串。编码规则 k[encoded_string] 表示方括号内部字符串重复 k 次。

**示例**: "3[a]2[bc]" → "aaabcbc"；"3[a2[c]]" → "accaccacc"；"abc3[cd]xyz" → "abccdcdcdxyz"

## 代码 / Code（双栈）
```java
class Solution {
    public String decodeString(String s) {
        Stack<Integer> numStack = new Stack<>();
        Stack<String> strStack = new Stack<>();
        String cur = "";   // 当前拼接的串
        int k = 0;         // 当前数字
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                k = k * 10 + (c - '0');   // 处理多位数 "12"
            } else if (c == '[') {
                numStack.push(k);
                strStack.push(cur);
                cur = "";
                k = 0;
            } else if (c == ']') {
                int repeat = numStack.pop();
                String prev = strStack.pop();
                cur = prev + cur.repeat(repeat);
            } else {
                cur += c;
            }
        }
        return cur;
    }
}
```

## 过程追踪 / Walkthrough
```
"3[a2[c]]"

数字栈:[] 字符串栈:[] 当前串:""
'3' → k=3
'[' → 数字栈:[3] 字符串栈:[""] 当前串=""
'a' → 当前串 "a"
'2' → k=2
'[' → 数字栈:[3,2] 字符串栈:["","a"] 当前串=""
'c' → 当前串 "c"
']' → 弹2, 弹"a" → "a"+"cc" = "acc"
']' → 弹3, 弹"" → ""+"accaccacc" ✅
```

## 核心思路
嵌套括号从最里面解（后进先出 = 栈）。
- 数字栈：存重复次数 k
- 字符串栈：存已拼好的部分
- 遇到 `]` 弹栈拼接：prev + cur.repeat(k)

## c - '0' 是为什么
字符数字转真数字：'3' 的 ASCII 51 - '0'(48) = 3。
多位数：k*10 + 新数字，如 '1','2' → 1*10+2 = 12。

## 关键词触发 / Triggers
"字符串解码" / "k[字符串]" / "嵌套括号" → 双栈（数字栈 + 字符串栈）

4. 739. Daily Temperatures / 每日温度
**难度**: Medium / 中等 | **标签**: Array, Stack, Monotonic Stack / 数组, 栈, 单调栈

## 原题 / Original Problem
Given an array of integers `temperatures` representing daily temperatures, return an array `answer` such that `answer[i]` is the number of days you have to wait after the i-th day to get a warmer temperature.

给定一个整数数组 temperatures，返回数组 answer，answer[i] 是第 i 天之后几天会出现更高的温度。之后不升高则用 0。

**示例**: [73,74,75,71,69,72,76,73] → [1,1,4,2,1,1,0,0]

## 代码 / Code（单调栈）
```java
class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];
        Stack<Integer> stack = new Stack<>();  // 存下标！
        for (int i = 0; i < n; i++) {
            // 新温度比栈顶温度高 → 栈顶找到答案了
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int prev = stack.pop();
                answer[prev] = i - prev;
            }
            stack.push(i);  // 当前天入栈
        }
        return answer;  // 栈里剩下的永远等不到更高温 → 保持0
    }
}
```

## 过程追踪 / Walkthrough
```
[73,74,75,71,69,72,76,73]

i=0(73): 栈空 → 压0 → [0]
i=1(74): 74>73 → 弹0, answer[0]=1 → 压1 → [1]
i=2(75): 75>74 → 弹1, answer[1]=1 → 压2 → [2]
i=3(71): 71<75 → 压3 → [2,3]
i=4(69): 69<71 → 压4 → [2,3,4]
i=5(72): 72>69 → 弹4, answer[4]=1
          72>71 → 弹3, answer[3]=2
          72<75 → 停 → 压5 → [2,5]
i=6(76): 76>72 → 弹5, answer[5]=1
          76>75 → 弹2, answer[2]=4
          压6 → [6]
i=7(73): 73<76 → 压7 → [6,7]

answer = [1,1,4,2,1,1,0,0] ✅
```

## 为什么栈里存下标不是温度？
answer 要记录"几天后"（i - prev），存下标才能算出距离。

## 单调栈三要素
- 栈内温度从底到顶递减（存下标，比温度用 temperatures[peek]）
- 新温度 > 栈顶 → 弹栈并算答案
- 栈里剩下的 → 永远等不到更高温 → 0

## 关键词触发 / Triggers
"下一个更高" / "几天后升温" → 单调栈（存下标）

5. 84. Largest Rectangle in Histogram / 柱状图中最大的矩形 ❌ 做错过
**难度**: Hard / 困难 | **标签**: Array, Stack, Monotonic Stack / 数组, 栈, 单调栈

## 原题 / Original Problem
Given an array of integers `heights` representing the histogram's bar height where the width of each bar is 1, return the area of the largest rectangle in the histogram.

给定 n 个非负整数表示柱状图中各个柱子的高度，宽度为 1。求柱状图中能够勾勒出的矩形最大面积。

**示例**: [2,1,5,6,2,3] → 10（高度5×宽度2）

## 代码 / Code（单调栈）
```java
class Solution {
    public int largestRectangleArea(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        int maxArea = 0;
        for (int i = 0; i < heights.length; i++) {
            while (!stack.isEmpty() && heights[i] < heights[stack.peek()]) {
                int h = heights[stack.pop()];
                int left = stack.isEmpty() ? -1 : stack.peek();
                int width = i - left - 1;
                maxArea = Math.max(maxArea, h * width);
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            int h = heights[stack.pop()];
            int left = stack.isEmpty() ? -1 : stack.peek();
            int width = heights.length - left - 1;
            maxArea = Math.max(maxArea, h * width);
        }
        return maxArea;
    }
}
```

## 过程追踪 / Walkthrough
```
[2,1,5,6,2,3]

i=1(1): 1<2 → 弹0: 高2, 左=-1, 右=1 → 宽1, 面积2
i=4(2): 2<6 → 弹3: 高6, 左=2, 右=4 → 宽1, 面积6
         2<5 → 弹2: 高5, 左=1, 右=4 → 宽2, 面积10 ← 最大！
结束栈剩[1,4,5]:
  弹5: 高3, 左=4, 右=6 → 宽1, 面积3
  弹4: 高2, 左=1, 右=6 → 宽4, 面积8
  弹1: 高1, 左=-1, 右=6 → 宽6, 面积6

最大 = 10 ✅
```

## 核心机制
每个柱子当高度，向左右扩展到第一个更矮的柱子：
- 出栈时左右边界都确定：左 = 新栈顶（或-1），右 = 当前 i（或数组长度）
- 宽度 = 右 - 左 - 1，面积 = 高度 × 宽度
- 栈内高度递增，遇到更矮的柱子就弹栈算面积

## 和每日温度的区别
| | 每日温度 | 柱状图矩形 |
|---|---|---|
| 找 | 右边第一个更高 | 左右第一个更矮 |
| 弹栈条件 | 新温度 > 栈顶 | 新高度 < 栈顶 |
| 出栈算 | 距离 | 面积（左边界+右边界）|

## 我犯的错 / My Mistakes
- `for (int i : heights)` 遍历的是值不是下标 → `heights[i]` 越界 💥
- 弹栈用 if 不是 while → 只弹一个，漏掉连续弹出
- 栈空时左边界要取 -1（不能用 peek 会报错）

## 关键词触发 / Triggers
"最大矩形面积" / "柱状图" → 单调递增栈（弹栈时左右边界确定）

# 堆 / Heap

1. 215. Kth Largest Element in an Array / 数组中的第 K 个最大元素
**难度**: Medium / 中等 | **标签**: Array, Heap, Divide and Conquer / 数组, 堆, 分治

## 原题 / Original Problem
Given an integer array `nums` and an integer `k`, return the k-th largest element in the array. Note that it is the k-th largest element in the sorted order, not the k-th distinct element.

给定整数数组 nums 和整数 k，返回数组中第 k 个最大的元素（排序后的第 k 个，不是第 k 个不同元素）。要求 O(n)。

**示例**: [3,2,1,5,6,4], k=2 → 5

## 代码 / Code（最小堆，O(n log k)）
```java
class Solution {
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> heap = new PriorityQueue<>();  // 最小堆，堆顶最小
        for (int num : nums) {
            if (heap.size() < k) {
                heap.offer(num);
            } else if (num > heap.peek()) {
                heap.poll();
                heap.offer(num);
            }
        }
        return heap.peek();
    }
}
```

## 过程追踪 / Walkthrough
```
nums=[3,2,1,5,6,4], k=2  堆只留2个最大的

3 → 没满 → [3]
2 → 没满 → [2,3]  堆顶2
1 → 满, 1<堆顶2 → 忽略 → [2,3]
5 → 满, 5>堆顶2 → 弹2加5 → [3,5]  堆顶3
6 → 满, 6>堆顶3 → 弹3加6 → [5,6]  堆顶5
4 → 满, 4<堆顶5 → 忽略 → [5,6]

堆顶 5 = 第2大 ✅
```

## 核心思路
最小堆永远只存 k 个最大的数。堆顶 = 堆里最小的 = 第 k 大。
新数比堆顶大 → 淘汰堆顶换新数；比堆顶小 → 不可能是前 k 大，忽略。

## 为什么排序不行？
排序 O(n log n) 超时，堆 O(n log k)。k 远小于 n 时堆明显更快。

## 关键词触发 / Triggers
"第 K 大" / "第 K 小" → 最小堆（第K大）或 最大堆（第K小），堆只留 k 个

2. 347. Top K Frequent Elements / 前 K 个高频元素
**难度**: Medium / 中等 | **标签**: Array, Hash Table, Heap / 数组, 哈希表, 堆

## 原题 / Original Problem
Given an integer array `nums` and an integer `k`, return the k most frequent elements. You may return the answer in any order.

给你一个整数数组 nums 和一个整数 k，返回出现频率前 k 高的元素。

**示例**: [1,1,1,2,2,3], k=2 → [1,2]

## 代码 / Code（HashMap 统计 + 最小堆）
```java
class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        // ① 统计频率
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) freq.put(num, freq.getOrDefault(num, 0) + 1);
        // ② 最小堆，按频率排序，只留 k 个最高的
        PriorityQueue<Integer> heap = new PriorityQueue<>(
            (a, b) -> freq.get(a) - freq.get(b)
        );
        for (int key : freq.keySet()) {
            heap.offer(key);
            if (heap.size() > k) heap.poll();   // 超过 k 个踢掉频率最低的
        }
        // ③ 取出
        int[] result = new int[k];
        for (int i = 0; i < k; i++) result[i] = heap.poll();
        return result;
    }
}
```

## 过程追踪 / Walkthrough
```
nums=[1,1,1,2,2,3], k=2

① 频率: {1:3, 2:2, 3:1}
② 堆（按频率，堆顶最小）:
   key=1: offer → [1]  size=1≤2
   key=2: offer → [2,1]  size=2≤2
   key=3: offer → [3,2,1]  size=3>2 → poll 踢 3(频率1) → [1,2]
③ 倒出: [1,2] ✅
```

## 先加再踢的技巧
`heap.offer(key)` 先塞进去，`if (size > k) poll()` 塞多了就踢频率最低的。
比 215 的"判断要不要加"写法更省事，效果一样：堆里永远只留频率前 k 高。

## 比较器 (a,b) -> freq.get(a) - freq.get(b)
堆里存的是"数字"，比较器用 freq.get(数字) 查它的频率来排序。
这是最小堆：堆顶 = 频率最低的。

## 关键词触发 / Triggers
"前K高频" / "出现次数最多" → 频率统计 HashMap + 最小堆留 k 个

3. 295. Find Median from Data Stream / 数据流的中位数
**难度**: Hard / 困难 | **标签**: Heap, Design, Data Stream / 堆, 设计, 数据流

## 原题 / Original Problem
Design a data structure that supports adding integers from a data stream and finding the median of all elements seen so far.

给你一个数据流，每次加入一个整数 num，并且随时可以查询目前所有数字的中位数。

**示例**: 1 → 2 → findMedian = 1.5 → 3 → findMedian = 2.0

## 代码 / Code（大根堆 + 小根堆）
```java
class MedianFinder {
    PriorityQueue<Integer> left;   // 大根堆：较小的一半
    PriorityQueue<Integer> right;  // 小根堆：较大的一半

    public MedianFinder() {
        left = new PriorityQueue<>((a, b) -> b - a);
        right = new PriorityQueue<>();
    }

    public void addNum(int num) {
        // num 应该属于较小的一半
        if (left.isEmpty() || num <= left.peek()) {
            left.add(num);
        } else {
            right.add(num);
        }

        // left 最多只能比 right 多 1 个
        if (left.size() > right.size() + 1) {
            int x = left.poll();
            right.add(x);
        }

        // right 不能比 left 多
        if (right.size() > left.size()) {
            int y = right.poll();
            left.add(y);
        }
    }

    public double findMedian() {
        if (left.size() == right.size()) {
            return (left.peek() + right.peek()) / 2.0;
        }

        if (left.size() == right.size() + 1) {
            return left.peek();
        }

        return 0.0;   // 正常情况下不会执行，只是满足 Java 返回值要求
    }
}
```

## 过程追踪 / Walkthrough
```
依次加入：1, 2, 3, 4

add 1:
left=[1]        right=[]
                ↑
             1个 : 0个

add 2:
先判断 2 > left.peek(1) → 放 right
left=[1]        right=[2]

add 3:
3 > left.peek(1) → 放 right
left=[1]        right=[2,3]

right 多 1 个 → right.poll()=2 移到 left
left=[2,1]      right=[3]

add 4:
4 > left.peek(2) → 放 right
left=[2,1]      right=[3,4]

最终：
left  = [1,2]   ← 较小的一半，大根堆，堆顶2
right = [3,4]   ← 较大的一半，小根堆，堆顶3

findMedian:
(2+3)/2 = 2.5 ✅
```

## 核心思路：两个堆维护"前一半 + 后一半"
不能每次 findMedian() 都重新排序。

如果每次都排序：
- addNum：O(1)
- findMedian：O(n log n)

调用次数达到 5 * 10^4 时会很慢。

真正只需要知道中间的一个或两个数，所以把所有数字分成两半：

```
         较小的一半       较大的一半

             left | right
                  |
        [1,2,3]   |   [5,7,9]
              ↑         ↑
           最大值      最小值
```

因此：
- left 用大根堆 → left.peek() = 较小一半的最大值
- right 用小根堆 → right.peek() = 较大一半的最小值

只要保证 left.peek() <= right.peek()，那么两个堆就相当于把整个有序数组从中间切开。

## 两个堆为什么正好能找到中位数？
例如：

[1,2,3,4]

分成：

left  = [1,2]
right = [3,4]

中间两个数就是：

left.peek()  = 2
right.peek() = 3

所以：

median = (2+3)/2

如果有奇数个：

[1,2,3,4,5]

分成：

left  = [1,2,3]
right = [4,5]

此时：

left.peek() = 3

就是中位数。

## 为什么数据还没全部输入，也可以维护"前一半 / 后一半"？
这里的"前一半"不是原数组的位置，而是指：

按照数字大小排序后，较小的那一半。

数据是一个一个来的没关系。

每加入一个新数字，就重新把它放到应该属于的那一半，然后调整两个堆的数量。

例如：

已有：
left=[1,3]
right=[7,9]

加入 4：

4 <= left.peek(3) ? 否
→ 放 right

left=[1,3]
right=[4,7,9]

此时右边多了一个：

right.size() > left.size()

把 right 最小值 4 移回 left：

left=[1,3,4]
right=[7,9]

又恢复成：

较小的一半 | 较大的一半

所以不需要提前知道完整数组。

## 两个堆必须维持的规则
规则 1：大小关系
left.peek() <= right.peek()

left 的最大值不能比 right 的最小值大。

否则就不是"前一半 + 后一半"了。

规则 2：数量平衡

规定：

left.size() == right.size()

或者：

left.size() == right.size() + 1

也就是：

left 永远和 right 一样多，或者比 right 多一个。

因此：

偶数个元素：
left = n/2
right = n/2

奇数个元素：
left = n/2 + 1
right = n/2

这样中位数的位置永远固定在：

left.peek()

或者：

(left.peek() + right.peek()) / 2

## 为什么 left 要用大根堆，right 要用小根堆？
因为我们需要：

较小的一半 → 找最大值
较大的一半 → 找最小值

也就是：

left：大根堆 → 最大值在堆顶
right：小根堆 → 最小值在堆顶

这样不用排序，就能直接拿到整个数据集的"中间边界"。

## 复杂度
每次 addNum()：

堆插入 / 删除 → O(log n)

每次 findMedian()：

只取堆顶 → O(1)

总复杂度：

addNum：O(log n)
findMedian：O(1)
空间：O(n)

比"每次查询重新排序"的 findMedian：O(n log n) 高效得多。

## Java 语法知识点
- PriorityQueue<Integer>() 默认是小根堆
- 大根堆：new PriorityQueue<>((a, b) -> b - a);
- peek()：查看堆顶，不删除
- poll()：删除并返回堆顶
- add() / offer()：加入元素
- 空堆 peek() 返回 null，所以第一次加入时可以用 left.isEmpty() 判断
- PriorityQueue 没有 push() / pop()，这是 Stack 常见的方法，堆要用 add/offer 和 poll

## 易错点 / My Mistakes
- left.size() == right.size() + 1; 不是"设置条件"，只是计算一个 boolean，不会自动让两个堆平衡。
- 构造函数里不能重新写 `PriorityQueue<Integer> left;` `PriorityQueue<Integer> right;`，否则会变成新的局部变量，把成员变量遮住。
- PriorityQueue 用 poll() 删除堆顶，不是 pop()。
- findMedian() 里 `(left.peek() + right.peek()) / 2` 是整数除法，例如 (1+2)/2 = 1，必须写成 `/ 2.0`。
- findMedian() 不需要单独判断两个堆都为空，因为题目保证调用前至少有一个元素。
- left 不只是"存比较小的数字"，更重要的是它代表排序后前一半；right 代表排序后后一半。
- 平衡不是为了让两个堆永远一样大，而是 left.size() == right.size() 或 left.size() == right.size() + 1。

## 关键词触发 / Triggers
"数据流中位数" / "动态求中位数" / "不断加入数字 + 随时查询中位数" → 大根堆 + 小根堆

看到"前一半 + 后一半"，第一反应：前一半 → 大根堆，后一半 → 小根堆。

看到"动态加入 + 查询中间值"，不要想着每次排序 → 两个堆维护中间边界。

# 贪心 / Greedy

1. 121. Best Time to Buy and Sell Stock / 买卖股票的最佳时机
**难度**: Easy / 简单 | **标签**: Array, DP, Greedy / 数组, 动态规划, 贪心

## 原题 / Original Problem
You are given an array `prices` where `prices[i]` is the price of a given stock on the i-th day. You want to maximize profit by choosing a single day to buy and a different day in the future to sell.

给定一个数组 prices，prices[i] 表示第 i 天股票价格。只能某一天买入、未来某一天卖出，计算最大利润。不能获利则返回 0。

**示例**: [7,1,5,3,6,4] → 5（第2天买1，第5天卖6）

## 代码 / Code
```java
class Solution {
    public int maxProfit(int[] prices) {
        int bestBuy = prices[0];  // 至今最低买入价
        int maxProfit = 0;
        for (int i = 0; i < prices.length; i++) {
            bestBuy = Math.min(bestBuy, prices[i]);      // 更新历史最低价
            maxProfit = Math.max(maxProfit, prices[i] - bestBuy); // 今天卖赚多少
        }
        return maxProfit;
    }
}
```

## 过程追踪 / Walkthrough
```
prices = [7, 1, 5, 3, 6, 4]

第0天: bestBuy=7
第1天: 1<7 → bestBuy=1
第2天: 利润5-1=4, max=4
第3天: 利润3-1=2, max=4
第4天: 利润6-1=5, max=5
第5天: 利润4-1=3, max=5

答案 5 ✅
```

## 为什么不是滑动窗口？
滑动窗口要有容器和收缩逻辑。这题只有一个"历史最低价"变量——记住过去，面向未来。贪心：每一步局部最优（更新最低价），累积成全局最优。

## 关键词触发 / Triggers
"买卖股票" / "最大利润" / "单次交易" → 维护历史最低价 + 每次算利润

2. 55. Jump Game / 跳跃游戏
**难度**: Medium / 中等 | **标签**: Array, Greedy / 数组, 贪心

## 原题 / Original Problem

Given an integer array `nums`, determine if you can reach the last index. Each element represents the maximum jump length at that position.

给定一个非负整数数组 `nums`，你最初位于第一个下标，每个元素表示当前位置最多可以跳跃的长度，判断能否到达最后一个下标。

**示例**: `[2,3,1,1,4] → true`；`[3,2,1,0,4] → false`

## 代码 / Code（贪心）

```java
class Solution {
    public boolean canJump(int[] nums) {
        int maxReach = 0;

        for (int i = 0; i < nums.length - 1; i++) {
            // 当前下标都到不了，后面更不可能到
            if (i > maxReach) return false;

            // 更新目前最远能到的位置
            maxReach = Math.max(maxReach, i + nums[i]);

            // 已经到达终点
            if (maxReach >= nums.length - 1) return true;
        }

        return false;
    }
}
```

## 核心思路：只维护"最远能到哪里"

一开始很容易想成：

```text
当前这个位置能不能直接跳到终点？
```

其实不需要。

我们只维护：

```java
int maxReach;
```

表示：

> **从目前所有可达的位置出发，最远可以到哪个下标。**

遍历到位置 `i` 时：

```java
maxReach = Math.max(maxReach, i + nums[i]);
```

如果：

```java
i > maxReach
```

说明当前位置本身都到不了，因此后面的所有位置也都到不了。

直接：

```java
return false;
```

## 过程追踪 / Walkthrough

```text
nums=[2,3,1,1,4]

开始：
maxReach=0

i=0:
0<=0，可达
0+2=2
maxReach=2

i=1:
1<=2，可达
1+3=4
maxReach=4

4 >= 最后下标4
→ true
```

失败例子：

```text
nums=[3,2,1,0,4]

i=0:
maxReach=max(0,0+3)=3

i=1:
maxReach=max(3,1+2)=3

i=2:
maxReach=max(3,2+1)=3

i=3:
maxReach=max(3,3+0)=3

i=4:
4 > maxReach
→ 下标4不可达
→ false
```

## 为什么遇到 0 不一定失败？

例如：

```text
[2,0,1,1,4]
```

虽然下标 1 是：

```text
0
```

但从下标 0 可以直接跳到下标 2。

所以这个 `0` 不在必经路径上。

真正危险的是：

```text
maxReach
```

被某个 `0` 卡住以后再也无法扩大。

因此不能简单写：

```java
if (nums[i] == 0) return false;
```

## 核心贪心

每次只保留一个信息：

```text
当前最远可达位置 maxReach
```

不用记录：

```text
具体走了哪条路径
```

因为我们只关心能不能不断扩大可达范围。

## 复杂度

```text
时间：O(n)
空间：O(1)
```

## 易错点 / My Mistakes

* 原先用了双重循环，试图判断每个位置能不能直接到终点，思路太局部。
* `nums[i] == 0` 不代表一定失败，这个位置可能根本不是必经位置。
* 必须先判断：

```java
if (i > maxReach) return false;
```

否则可能把一个根本到不了的位置当成可以继续跳的位置。

* `maxReach` 表示的是**最远下标**，不是还能跳多少步。
* 最后一个下标是：

```java
nums.length - 1
```

不是 `nums.length`。

## 关键词触发 / Triggers

"能否到达最后" / "每个位置最多跳几步" / "跳跃游戏" → **贪心 + maxReach**

看到这种题不要想：

```text
"具体怎么跳？"
```

先想：

```text
"目前最远能到哪里？"
```

# 动态规划 / DP

1. 70. Climbing Stairs / 爬楼梯
**难度**: Easy / 简单 | **标签**: Math, DP / 数学, 动态规划

## 原题 / Original Problem
You are climbing a staircase. It takes `n` steps to reach the top. Each time you can either climb 1 or 2 steps. Return the number of distinct ways to climb to the top.

假设你正在爬楼梯，需要 n 阶才能到楼顶。每次可以爬 1 或 2 个台阶，有多少种不同方法？

**示例**: n=2 → 2（1+1, 2）；n=3 → 3（1+1+1, 1+2, 2+1）

## 代码 / Code（DP）
```java
class Solution {
    public int climbStairs(int n) {
        if (n <= 2) return n;
        int[] dp = new int[n + 1];
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }
}
```

## dp[i] 是什么意思
`dp[i]` = "爬到第 i 阶有多少种方法"
```
dp[1]=1  dp[2]=2  dp[3]=dp[2]+dp[1]=3  dp[4]=3+2=5 ...
这就是斐波那契数列
```

## 为什么 dp[i] = dp[i-1] + dp[i-2]？
到第 i 阶，最后一步只有两种可能：
① 从 i-1 爬 1 步上来 → 前面有 dp[i-1] 种方法
② 从 i-2 爬 2 步上来 → 前面有 dp[i-2] 种方法
两种情况相加。

## 为什么数组长度是 n+1？
你要访问 dp[n]，数组下标 0~n 共 n+1 个。长度 = 最大下标 + 1。

## 递归版的致命问题（重复计算）
```java
return dfs(n-1) + dfs(n-2);  // 逻辑对，但 dfs(2) 被重复算几亿次
```
n=45 时超时！DP 把每个中间结果存起来只算一次，空间换时间。这就是动态规划的核心。

## 关键词触发 / Triggers
"爬楼梯" / "斐波那契" / "1或2步" → dp[i] = dp[i-1] + dp[i-2]

2. 118. Pascal's Triangle / 杨辉三角
**难度**: Easy / 简单 | **标签**: Array, Dynamic Programming / 数组, 动态规划

## 原题 / Original Problem

Given an integer `numRows`, return the first `numRows` of Pascal's triangle.

给定一个非负整数 `numRows`，生成杨辉三角的前 `numRows` 行。

**示例**:

```text
numRows = 5

[
 [1],
 [1,1],
 [1,2,1],
 [1,3,3,1],
 [1,4,6,4,1]
]
```

## 代码 / Code

```java
class Solution {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> prev = new ArrayList<>();

        for (int i = 0; i < numRows; i++) {
            List<Integer> cur = new ArrayList<>();

            for (int j = 0; j <= i; j++) {
                if (j == 0 || j == i) {
                    cur.add(1);
                } else {
                    cur.add(prev.get(j - 1) + prev.get(j));
                }
            }

            result.add(cur);
            prev = cur;
        }

        return result;
    }
}
```

## 核心思路：当前行由上一行推出来

杨辉三角的规律：

```text
        1
       1 1
      1 2 1
     1 3 3 1
```

每一行：

```text
最左边 = 1
最右边 = 1
中间 = 左上 + 右上
```

例如上一行：

```text
[1,3,3,1]
```

下一行：

```text
1 4 6 4 1
```

中间部分：

```text
4 = 1 + 3
6 = 3 + 3
4 = 3 + 1
```

所以：

```java
cur.add(prev.get(j - 1) + prev.get(j));
```

## 过程追踪 / Walkthrough

```text
开始：
prev=[]

i=0:
cur=[]
j=0 → 边界 → add(1)

cur=[1]
result=[[1]]
prev=cur

i=1:
cur=[]
j=0 → add(1)
j=1 → add(1)

cur=[1,1]
result=[[1],[1,1]]
prev=cur

i=2:
j=0 → 1
j=1 → prev[0]+prev[1] = 1+1 = 2
j=2 → 1

cur=[1,2,1]

i=3:
j=0 → 1
j=1 → 1+2 = 3
j=2 → 2+1 = 3
j=3 → 1

cur=[1,3,3,1]
```

## 为什么 `cur` 放循环里面，`prev` 放循环外？

这是这题非常重要的变量作用域。

```text
cur = 当前这一行
prev = 上一行
```

每进入下一轮：

```text
新的 cur
```

必须重新创建。

所以：

```java
for (...) {
    List<Integer> cur = new ArrayList<>();
}
```

而 `prev` 必须把上一轮的结果带到下一轮：

```java
List<Integer> prev = new ArrayList<>();

for (...) {
    ...
    prev = cur;
}
```

如果把 `prev` 放进循环：

```java
for (...) {
    List<Integer> prev = new ArrayList<>();
}
```

每一轮都会重新变成空列表，就无法利用上一行。

## 为什么 `ArrayList` 用 `add()` 而不是 `cur[j]`？

`cur` 是：

```java
List<Integer>
```

不是数组。

所以：

```text
取值：get(j)
修改已有位置：set(j,x)
末尾添加：add(x)
```

这里 `cur` 一开始是空的，我们是在从左到右**创建这一行**，所以使用：

```java
cur.add(...)
```

## 复杂度

一共有：

```text
1 + 2 + 3 + ... + numRows
```

个元素。

所以：

```text
时间：O(numRows²)
空间：O(numRows²)
```

结果本身就要保存所有元素，因此 `O(numRows²)` 是合理的。

## 易错点 / My Mistakes

* `cur` 是当前行，所以放在外层循环里面，每一轮重新创建。
* `prev` 是上一行，所以放在外层循环外，跨轮次保存。
* 第一行没有 `prev`，但因为 `j=0` 同时满足左右边界条件，所以直接加入 `1`，不需要额外特判。
* `List<Integer>` 不能写：

```java
cur[j]
```

必须用 `add/get/set`。

* 中间元素：

```java
prev.get(j - 1) + prev.get(j)
```

* 每行结束后：

```java
result.add(cur);
prev = cur;
```

## 关键词触发 / Triggers

"杨辉三角" / "当前行由上一行计算" / "左上 + 右上" → **逐行构造 + 保存上一行**

看到：

```text
当前结果依赖上一轮结果
```

第一反应：

```text
cur  = 当前
prev = 上一轮
```

# 多维动态规划 / Multi-dimensional DP

1. 62. Unique Paths / 不同路径
**难度**: Medium / 中等 | **标签**: Math, DP, Grid / 数学, 动态规划, 网格

## 原题 / Original Problem

A robot is located at the top-left corner of an `m x n` grid and can only move down or right. Return the number of unique paths to reach the bottom-right corner.

机器人位于 `m x n` 网格左上角，每次只能向右或向下移动，求到达右下角的不同路径数量。

**示例**: `m=3, n=3 → 6`

## 代码 / Code

```java
class Solution {
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];

        // 第一行：只能一直向右
        for (int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }

        // 第一列：只能一直向下
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }

        // 中间区域
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }

        return dp[m - 1][n - 1];
    }
}
```

## dp[i][j] 是什么意思？

```text
dp[i][j] =
从左上角走到 (i,j) 的不同路径数量
```

机器人进入 `(i,j)` 时，最后一步只有两种可能：

```text
上面 ↓
```

或者：

```text
左边 →
```

所以：

```java
dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
```

## 核心思路：上面 + 左边

例如：

```text
1  1  1
1  2  3
1  3  6
```

右下角：

```text
6
```

就是：

```text
上方 3 + 左方 3
```

所以答案为：

```text
6
```

## 为什么第一行和第一列都是 1？

第一行：

```text
→ → → → →
```

只有一种走法：一直向右。

第一列：

```text
↓
↓
↓
```

只有一种走法：一直向下。

因此：

```text
第一行全部 = 1
第一列全部 = 1
```

## 过程追踪 / Walkthrough

```text
m=3,n=3

初始化：

1 1 1
1 ? ?
1 ? ?

计算 [1][1]:
上1 + 左1 = 2

1 1 1
1 2 ?
1 ? ?

计算 [1][2]:
上1 + 左2 = 3

1 1 1
1 2 3
1 ? ?

计算 [2][1]:
上1 + 左2 = 3

计算 [2][2]:
上3 + 左3 = 6

最终：

1 1 1
1 2 3
1 3 6

答案 = 6 ✅
```

## 为什么这是多维 DP？

因为这里的状态不是：

```text
dp[i]
```

而是：

```text
dp[i][j]
```

需要同时记录：

```text
第 i 行
第 j 列
```

所以是一个二维状态表。

同时每个状态依赖：

```text
dp[i-1][j]
dp[i][j-1]
```

形成从左上到右下的二维状态转移。

## 和杨辉三角的关系

两道题的状态转移非常像：

```text
杨辉三角：
当前 = 左上 + 右上

不同路径：
当前 = 上 + 左
```

共同特点：

```text
当前状态
    ↓
依赖已经计算过的前面状态
    ↓
逐行 / 逐格填表
```

## 复杂度

```text
时间：O(m*n)
空间：O(m*n)
```

## 易错点 / My Mistakes

* `m` 表示行数，`n` 表示列数。
* 第一行和第一列需要初始化为 `1`。
* 中间区域从：

```java
i=1, j=1
```

开始。

* 状态转移：

```java
dp[i][j] = dp[i-1][j] + dp[i][j-1];
```

* 最后一个格子的下标是：

```java
dp[m-1][n-1]
```

不是 `dp[m][n]`。

* 第一行、第一列的初始化应该和中间状态转移分开，否则容易重复计算。

## 关键词触发 / Triggers

"网格" / "只能向右或向下" / "有多少条路径" → **二维 DP**

看到：

```text
一个格子的答案
=
上面的答案 + 左边的答案
```

第一反应：

```text
dp[i][j] = dp[i-1][j] + dp[i][j-1]
```

# 子串 / Subarray

1. 560. Subarray Sum Equals K / 和为 K 的子数组
**难度**: Medium / 中等 | **标签**: Array, Hash Table, Prefix Sum / 数组, 哈希表, 前缀和

## 原题 / Original Problem
Given an array of integers `nums` and an integer `k`, return the total number of subarrays whose sum equals to `k`.

给你一个整数数组 nums 和一个整数 k，请你统计并返回该数组中和为 k 的连续子数组的个数。

**示例**: nums = [1,1,1], k = 2 → 2；nums = [1,2,3], k = 3 → 2

## 代码 / Code
```java
class Solution {
    public int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);  // 空前缀和算一次
        int prefixSum = 0, count = 0;
        for (int num : nums) {
            prefixSum += num;
            count += map.getOrDefault(prefixSum - k, 0);
            map.put(prefixSum, map.getOrDefault(prefixSum, 0) + 1);
        }
        return count;
    }
}
```

## 先搞懂前缀和
前缀和 = 从数组开头一直加到当前位置的总和。
公式：**子数组[i到j]的和 = 前缀和[j] - 前缀和[i-1]**

## 核心推导（移项）
前缀和[j] - 前缀和[i-1] = k → 前缀和[j] - k = 前缀和[i-1]
翻译：**走到 j，前缀和是 X。去表里查之前有几个 X-k，有几个就找到几个。**

## 为什么 HashMap value 是次数？
同一前缀和可能在不同位置出现多次，每个位置都能和当前 j 凑出一个子数组。

## 为什么 put(0, 1)？
替代"空前缀和"。当前缀和正好等于 k 时，需要 0 在表里才能计数。
例：[3], k=3 → prefixSum=3, 找 3-3=0 → 表里有 → count=1

## 和 Two Sum 的关系
| | Two Sum | 这题 |
|---|---|---|
| 表里存什么 | 数值 | 前缀和 |
| 找什么 | target-当前数 | 当前前缀和-k |
| value | 下标 | 出现次数 |

## 易错点
- 先查再放（k=0 时防把自己算进去）
- map.put(0, 1) 不能忘
# 哈希表 / Hash Table
1. 1. Two Sum / 两数之和（简单）❌ 做错过
**难度**: Easy / 简单 | **标签**: Array, Hash Table / 数组, 哈希表 | 日期: 2026-07-18

## 原题 / Original Problem
Given an array of integers `nums` and an integer `target`, return indices of the two numbers such that they add up to `target`. You may assume that each input would have exactly one solution, and you may not use the same element twice.

给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，并返回它们的数组下标。每种输入只会对应一个答案，不能使用两次相同的元素。

## 代码 / Code
```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        // key: 数值, value: 下标
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (seen.containsKey(complement)) {
                return new int[]{seen.get(complement), i};
            }
            seen.put(nums[i], i);
        }
        return new int[]{};
    }
}
```

## 解法对比
**暴力 O(n²) / O(1)**：两层循环，每对都试一遍。
坑：记得写 if 判断，别直接 return ← 我犯过的错

**哈希 O(n) / O(n)**：complement = target - nums[i]，去 HashMap 里找。
走到一个数，不问"前面谁能和我配对"，而是问"我需要的人之前出现过吗"。

## 关键词触发
"两数之和" / "找配对" → 第一反应 HashMap


2. 49. Group Anagrams / 字母异位词分组
**难度**: Medium / 中等 | **标签**: Array, Hash Table, String, Sorting / 数组, 哈希表, 字符串, 排序

## 原题 / Original Problem
Given an array of strings `strs`, group the anagrams together. You can return the answer in any order.

给你一个字符串数组，请你将字母异位词组合在一起。可以按任意顺序返回结果列表。

**示例**: strs = ["eat", "tea", "tan", "ate", "nat", "bat"] → [["bat"],["nat","tan"],["ate","eat","tea"]]

## 代码 / Code
### 排序法
```java
class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);   // "eat" → "aet"
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(s);
        }
        return new ArrayList<>(map.values());
    }
}
```
### 计数法（⭐ 加分，O(n·k)）
```java
class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            int[] count = new int[26];
            for (char c : s.toCharArray()) {
                count[c - 'a']++;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 26; i++) {
                sb.append('#').append(count[i]); // # 防歧义
            }
            String key = sb.toString();
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(s);
        }
        return new ArrayList<>(map.values());
    }
}
```

## 什么是字母异位词
字母种类和个数完全相同，仅是排列顺序不同。
"eat" 和 "tea" → 都是 a:1, e:1, t:1

## 核心思路
给每个单词生成一个"签名"，异位词的签名一定相同。
签名 → HashMap 的 key → 签名相同的单词自动归到同一组。

### 关键 API
- `s.toCharArray()` — String 不可变，拆成 char[] 才能排序
- `Arrays.sort(chars)` — 原地排序
- `new String(chars)` — char[] 拼回 String
- `c - 'a'`：字符相减 = ASCII 差值，'e' - 'a' = 4 → count[4]++
- 用 `#` 分隔每个数字，防止拼接歧义：#1#12 ≠ #11#2

## 易错点
- HashMap 的 key 第一次出现时要先 new ArrayList<>()
- 最后返回 `new ArrayList<>(map.values())`，别直接返回 map.values()

## 关键词触发
看到 "分组" / "归类" / "字母异位词" → HashMap + 签名
和 Two Sum 一样：找同类项 → HashMap

3. 128. Longest Consecutive Sequence / 最长连续序列
**难度**: Medium / 中等 | **标签**: Array, Hash Table / 数组, 哈希表

## 原题 / Original Problem
Given an unsorted array of integers `nums`, return the length of the longest consecutive elements sequence. Must run in O(n).

给定一个未排序的整数数组 nums，找出数字连续的最长序列的长度。要求 O(n)。

**示例**: [100,4,200,1,3,2] → 4（[1,2,3,4]）；[0,3,7,2,5,8,4,6,0,1] → 9

## 代码 / Code
```java
class Solution {
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) set.add(num);
        int longest = 0;
        for (int num : set) {
            if (set.contains(num - 1)) continue;  // 不是开头，跳过
            int cur = num, len = 1;
            while (set.contains(cur + 1)) { cur++; len++; }
            longest = Math.max(longest, len);
        }
        return longest;
    }
}
```

## 过程追踪 / Walkthrough
```
nums=[100,4,200,1,3,2] → set={100,4,200,1,3,2}

100: contains(99)? 否 → 开头！往后: 101? 无 → len=1
4:   contains(3)? 是 → 不是开头，跳过
200: contains(199)? 否 → 开头！往后无 → len=1
1:   contains(0)? 否 → 开头！1→2→3→4 → len=4 ← 最大
3:   contains(2)? 是 → 跳过
2:   contains(1)? 是 → 跳过

longest = 4 ✅
```

## 为什么 O(n)？
只有"开头"才往后数。每个数最多被数一次（只有它前面没数时才启动），所以总次数 O(n)。

## 怎么判断开头？
一个数是序列开头 ⟺ set 里没有 num-1。有 num-1 说明它是中间/末尾，不用从它数。

## 关键词触发 / Triggers
"最长连续序列" / "连续数字" / "O(n)" → HashSet + 只从开头数


# 链表 / Linked List

1. 160. Intersection of Two Linked Lists / 相交链表
**难度**: Easy / 简单 | **标签**: Hash Table, Linked List, Two Pointers / 哈希表, 链表, 双指针

## 原题 / Original Problem
Given the heads of two singly linked-lists `headA` and `headB`, return the node at which the two lists intersect. If the two linked lists have no intersection, return null.

给你两个单链表的头节点 headA 和 headB，找出并返回两个单链表相交的起始节点。如果不存在相交节点，返回 null。

**示例**: A=[4,1,8,4,5], B=[5,6,1,8,4,5] → 相交于 8

## 代码 / Code
```java
public class Solution {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode pA = headA, pB = headB;
        while (pA != pB) {
            pA = (pA == null) ? headB : pA.next;
            pB = (pB == null) ? headA : pB.next;
        }
        return pA;
    }
}
```

## 过程追踪 / Walkthrough
A=[4,1,8,4,5], B=[5,6,1,8,4,5]，交点=8

pA: 4→1→8→4→5→null→5(B头)→6→1→8 ← 相遇！
pB: 5→6→1→8→4→5→null→4(A头)→1→8 ← 相遇！

pA 走了 A+B 前半段，pB 走了 B+A 前半段，路程相同，必在交点相遇。
不相交？两个同时走到 null，pA==pB，返回 null。

## 易错点 / Pitfalls
- 比较的是节点引用（地址），不是 val
- O(m+n) 时间，O(1) 空间 — 不需要 HashSet

## 关键词触发 / Triggers
"链表相交" / "找交点" → 双指针各走 A+B

2. 206. Reverse Linked List / 反转链表
**难度**: Easy / 简单 | **标签**: Linked List, Recursion / 链表, 递归

## 原题 / Original Problem
Given the `head` of a singly linked list, reverse the list, and return the reversed list.

给你单链表的头节点 head，请你反转链表，并返回反转后的链表。

**示例**: [1,2,3,4,5] → [5,4,3,2,1]

## 代码 / Code（迭代）
```java
class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode prev = null, cur = head;
        while (cur != null) {
            ListNode next = cur.next; // 备份
            cur.next = prev;          // 掰箭头
            prev = cur;               // prev前进
            cur = next;               // cur前进
        }
        return prev;
    }
}
```

## 过程追踪 / Walkthrough
```
初始: prev=null, cur=1
      null    1→2→3→4→5→null

cur=1: next=2, 1→null, prev=1, cur=2  →  null←1  2→3→4→5
cur=2: next=3, 2→1,    prev=2, cur=3  →  null←1←2  3→4→5
cur=3: next=4, 3→2,    prev=3, cur=4  →  null←1←2←3  4→5
cur=4: next=5, 4→3,    prev=4, cur=5
cur=5: next=null, 5→4, prev=5, cur=null
cur=null → 结束，prev=5 是新头 ✅
```

## 关键词触发 / Triggers
"反转链表" → 迭代(N-1个箭头逐根掰) / 递归(从后往前翻)

3. 234. Palindrome Linked List / 回文链表
**难度**: Easy / 简单 | **标签**: Linked List, Two Pointers, Stack / 链表, 双指针, 栈

## 原题 / Original Problem
Given the `head` of a singly linked list, return `true` if it is a palindrome or `false` otherwise.

给你一个单链表的头节点 head，判断该链表是否为回文链表。O(n) 时间 + O(1) 空间。

**示例**: [1,2,2,1] → true；[1,2] → false

## 代码 / Code
```java
class Solution {
    public boolean isPalindrome(ListNode head) {
        // ① 快慢找中点
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        // ② 反转后半
        ListNode prev = null;
        while (slow != null) {
            ListNode next = slow.next;
            slow.next = prev;
            prev = slow;
            slow = next;
        }
        // ③ 前后对比
        ListNode left = head, right = prev;
        while (right != null) {
            if (left.val != right.val) return false;
            left = left.next;
            right = right.next;
        }
        return true;
    }
}
```

## 过程追踪 / Walkthrough
```
head = 1→2→2→1

① slow=1,fast=1 → slow=2,fast=2 → slow=2(第二个2),fast=null
   前半: 1→2  后半: 2→1

② 反转后半: 2→1 改成 1→2
   整条: 1→2→2←1（中间断开）

③ left=1, right=1 → 1==1 ✓
   left=2, right=2 → 2==2 ✓
   right=null → 比完 → true ✅
```

## 为什么单链表"从后往前"做不到？
单链表只有 next 箭头没有 prev。把后半段反转后，箭头方向变了，就能从头尾同时往中间走。

## 易错点 / Pitfalls
- 快慢指针找中点，slow 停在的是后半段的头
- 反转的是后半段引用，不新建节点

## 关键词触发 / Triggers
"回文链表" / "O(1) 空间" → 快慢找中 + 反转后半 + 双指针对比

4. 141. Linked List Cycle / 环形链表
**难度**: Easy / 简单 | **标签**: Hash Table, Linked List, Two Pointers / 哈希表, 链表, 双指针

## 原题 / Original Problem
Given `head`, the head of a linked list, determine if the linked list has a cycle in it. Return `true` if there is a cycle, otherwise `false`.

给你一个链表的头节点 head，判断链表中是否有环。

**示例**: [3,2,0,-4] 尾连到 2 → true；[1] → false

## 代码 / Code
```java
public class Solution {
    public boolean hasCycle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }
}
```

## 过程追踪 / Walkthrough
```
[3,2,0,-4] 尾连到 2:
slow 走1步 fast 走2步
初始: s=3 f=3 → s=2 f=0 → s=0 f=2 → s=-4 f=-4 → 相遇！✅

[1,2,3]:
s=1 f=1 → s=2 f=3 → s=3 f=null → fast到头，没环 ❌
```

## 为什么 `fast.next != null`？
fast 走两步：`fast.next.next`。如果 `fast.next` 是 null，那 `null.next` 就 💥。

## 关键词触发 / Triggers
"链表有环" / "判断环" → 快慢指针（龟兔赛跑）

5. 142. Linked List Cycle II / 环形链表 II
**难度**: Medium / 中等 | **标签**: Hash Table, Linked List, Two Pointers / 哈希表, 链表, 双指针

## 原题 / Original Problem
Given the `head` of a linked list, return the node where the cycle begins. If there is no cycle, return `null`.

给定一个链表的头节点 head，返回链表开始入环的第一个节点。如果链表无环，则返回 null。

**示例**: [3,2,0,-4] 尾连到 2 → 返回节点 2

## 代码 / Code
```java
public class Solution {
    public ListNode detectCycle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                slow = head;
                while (slow != fast) {
                    slow = slow.next;
                    fast = fast.next;
                }
                return slow;  // 入口
            }
        }
        return null;
    }
}
```

## 过程追踪 / Walkthrough
```
[3,2,0,-4] 尾连到 2，入口=2:

① 快慢找相遇: s=2 f=2 → 相遇在节点 0
② slow 回 head(3)，两个都走1步:
   slow=3→2  fast=0→-4→2  → 再次相遇在 2！这就是入口 🎯
```

## 数学直觉
head→入口 = a，入口→相遇点 = b，相遇点→入口 = c
2(a+b) = a+2b+c → a = c
所以 head 和 相遇点 同时走 1 步，刚好在入口碰头。

## 关键词触发 / Triggers
"找环入口" → 快慢相遇后 slow 回 head，两个都走 1 步直到相遇

6. 21. Merge Two Sorted Lists / 合并两个有序链表
**难度**: Easy / 简单 | **标签**: Linked List, Recursion / 链表, 递归

## 原题 / Original Problem
You are given the heads of two sorted linked lists `list1` and `list2`. Merge the two lists into one sorted list. Return the head of the merged linked list.

将两个升序链表合并为一个新的升序链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。

**示例**: list1=[1,2,4], list2=[1,3,4] → [1,1,2,3,4,4]

## 代码 / Code
```java
class Solution {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(-1);
        ListNode cur = dummy;
        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                cur.next = list1;
                list1 = list1.next;
            } else {
                cur.next = list2;
                list2 = list2.next;
            }
            cur = cur.next;
        }
        cur.next = (list1 != null) ? list1 : list2;
        return dummy.next;
    }
}
```

## 过程追踪 / Walkthrough
```
l1=1→2→4, l2=1→3→4

dummy → ...

1≤1 → 接l1的1 → l1移到2, cur移到1 → [1]
2>1 → 接l2的1 → l2移到3, cur移到1 → [1,1]
2≤3 → 接l1的2 → l1移到4 → [1,1,2]
4>3 → 接l2的3 → l2移到4 → [1,1,2,3]
4≤4 → 接l1的4 → l1=null → [1,1,2,3,4]
l1=null → cur.next=l2(剩下的4) → [1,1,2,3,4,4]

return dummy.next → 1→1→2→3→4→4 ✅
```

## dummy 是干嘛的？
不用 dummy：第一个节点要特殊处理"谁当 head"。
用 dummy：dummy 站桩当假头，cur 只管往后接。最后 return dummy.next 跳过假头。

## 易错点 / Pitfalls
- 比的是 `.val` 不是节点引用
- cur 每次接完要 `cur = cur.next` 往前走
- 最后把没走完的链表剩下的全部接上

## 关键词触发 / Triggers
"合并有序链表" → dummy + 比大小逐个接

7. 2. Add Two Numbers / 两数相加
**难度**: Medium / 中等 | **标签**: Linked List, Math, Recursion / 链表, 数学, 递归

## 原题 / Original Problem
You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order, and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.

给你两个非空的链表，表示两个非负的整数。每位数字都是按照逆序的方式存储的。将两个数相加，以相同形式返回一个表示和的链表。

**示例**: l1=[2,4,3], l2=[5,6,4] → [7,0,8]（342+465=807）

## 代码 / Code
```java
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(-1);
        ListNode cur = dummy;
        int carry = 0;
        while (l1 != null || l2 != null || carry != 0) {
            int sum = carry;
            if (l1 != null) { sum += l1.val; l1 = l1.next; }
            if (l2 != null) { sum += l2.val; l2 = l2.next; }
            cur.next = new ListNode(sum % 10); // 个位
            carry = sum / 10;                    // 进位
            cur = cur.next;
        }
        return dummy.next;
    }
}
```

## 过程追踪 / Walkthrough
```
l1=[2,4,3], l2=[5,6,4]

dummy → ...

位1(个): 2+5=7, carry=0 → 存7       [7]
位2(十): 4+6=10, carry=1 → 存0      [7,0]
位3(百): 3+4+1=8, carry=0 → 存8     [7,0,8]
都到头, carry=0 → return dummy.next → 7→0→8 ✅

l1=[9,9,9,9,9,9,9], l2=[9,9,9,9]
... 7轮后 carry=1 → 补一个节点存1 → [8,9,9,9,0,0,0,1]
```

## 为什么需要 dummy？
cur 负责画线接节点，接完最后停在链表尾。如果没有 dummy 站桩，return cur 只拿到最后一个节点。dummy 始终在开头不动，return dummy.next 拿整条链。

## 关键词触发 / Triggers
"链表加法" / "逆序数字" → 模拟竖式加法 + dummy + carry

8. 19. Remove Nth Node From End of List / 删除链表的倒数第 N 个结点
**难度**: Medium / 中等 | **标签**: Linked List, Two Pointers / 链表, 双指针

## 原题 / Original Problem
Given the `head` of a linked list, remove the n-th node from the end of the list and return its head.

给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。

**示例**: head=[1,2,3,4,5], n=2 → [1,2,3,5]；head=[1], n=1 → []

## 方法一：计数法（两趟扫描）
```java
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        int sz = 0;
        ListNode cur = head;
        while (cur != null) { sz++; cur = cur.next; } // 数总数
        if (sz == n) return head.next;  // 删的是头
        cur = head;
        for (int i = 0; i < sz - n - 1; i++) cur = cur.next; // 走到删点前一个
        cur.next = cur.next.next;  // 跳过
        return head;
    }
}
```

过程: head=[1,2,3,4,5], n=2, sz=5
sz-n-1=2步: cur 走到 ③
cur.next = cur.next.next → ③→⑤，跳过④
return head → ①→②→③→⑤ ✅

## 方法二：双指针（一趟扫描）⭐ 进阶
```java
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(-1, head);
        ListNode fast = dummy, slow = dummy;
        for (int i = 0; i <= n; i++) fast = fast.next; // fast 先走 n+1 步
        while (fast != null) { fast = fast.next; slow = slow.next; }
        slow.next = slow.next.next;
        return dummy.next;
    }
}
```

过程: head=[1,2,3,4,5], n=2
dummy→1→2→3→4→5
fast 先走 3步: fast=3, slow=dummy
一起走: fast=4,slow=1 → fast=5,slow=2 → fast=null,slow=3
slow.next=slow.next.next → ③→⑤ ✅

## 为什么 head 从头到尾没动但返回的是整个结果？
head 始终指着节点①。cur 改的是链中间的 next 挂钩，head 通过 next 顺藤摸瓜能找到整条被修改后的链。

## 关键词触发 / Triggers
"删除倒数第N个" / "一趟扫描" → 计数法 或 快慢指针(fast先走n步)

9. 24. Swap Nodes in Pairs / 两两交换链表中的节点
**难度**: Medium / 中等 | **标签**: Linked List, Recursion / 链表, 递归

## 原题 / Original Problem
Given a linked list, swap every two adjacent nodes and return its head. You must solve the problem without modifying the values in the list's nodes.

给你一个链表，两两交换其中相邻的节点，并返回交换后链表的头节点。只能进行节点交换，不能修改节点内部的值。

**示例**: [1,2,3,4] → [2,1,4,3]

## 代码 / Code（dummy + 指针）
```java
class Solution {
    public ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(-1, head);
        ListNode cur = dummy;
        while (cur.next != null && cur.next.next != null) {
            ListNode first = cur.next;      // 1
            ListNode second = cur.next.next; // 2
            first.next = second.next;   // 1 → 3
            second.next = first;        // 2 → 1
            cur.next = second;          // dummy → 2
            cur = first;                // 下一对起点
        }
        return dummy.next;
    }
}
```

## 过程追踪 / Walkthrough
```
dummy → 1 → 2 → 3 → 4 → null

第1对: first=1, second=2
  1→3, 2→1, dummy→2 → dummy→2→1→3→4
  cur=1
第2对: first=3, second=4
  3→null, 4→3, 1→4 → dummy→2→1→4→3
  cur=3

dummy.next → 2→1→4→3 ✅
```

## 核心要点
- first/second 只是指针变量，指现有的节点，不是新建
- 交换的是 next 箭头（不改 val）
- dummy 站桩返回头，cur 每轮移到本组末尾

## 易错点 / Pitfalls
- 别交换 cur 和 cur.next（那是 dummy 和 1），要交换 cur.next 和 cur.next.next
- 交换后 cur 移到 first（组尾），不是 second
- return 前 head 已经丢了，要用 dummy.next

## 关键词触发 / Triggers
"两两交换" / "相邻节点交换" → dummy + first/second 指针换箭头

10. 25. Reverse Nodes in k-Group / K 个一组翻转链表
**难度**: Hard / 困难 | **标签**: Linked List, Recursion / 链表, 递归

## 原题 / Original Problem
Given the head of a linked list, reverse the nodes of the list k at a time and return the modified list. If the number of nodes is not a multiple of k, the remaining nodes should stay in their original order.

给你链表的头节点 head，每 k 个节点一组进行翻转，返回修改后的链表。节点总数不是 k 的整数倍时，最后剩余的节点保持原有顺序。

**示例**: [1,2,3,4,5], k=2 → [2,1,4,3,5]；k=3 → [3,2,1,4,5]

## 代码 / Code
```java
class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(-1, head);
        ListNode cur = dummy;
        while (true) {
            ListNode tail = cur;              // ← 每轮重新数！
            for (int i = 0; i < k; i++) {
                if (tail.next == null) return dummy.next;  // 不够 k 个，结束
                tail = tail.next;
            }
            ListNode oldFirst = cur.next;     // 反转前记下组头
            ListNode nextGroup = tail.next;   // 下一组头
            tail.next = null;                 // 切断本组
            ListNode newHead = reverse(cur.next);
            cur.next = newHead;               // 接上头
            cur = oldFirst;                   // cur 移到组尾
            cur.next = nextGroup;             // 接上尾
        }
    }
    private ListNode reverse(ListNode head) {
        ListNode prev = null, now = head;
        while (now != null) {
            ListNode next = now.next;
            now.next = prev;
            prev = now;
            now = next;
        }
        return prev;
    }
}
```

## 过程追踪 / Walkthrough
```
[1,2,3,4,5], k=2

第1轮: tail 数到 2, oldFirst=1, nextGroup=3
  切断 1→2, reverse → 2→1, cur(组尾1)→3
  → 2→1→3→4→5
第2轮: cur=1, tail 从 1 数到 4, oldFirst=3, nextGroup=5
  切断 3→4, reverse → 4→3, 1→4, 3→5
  → 2→1→4→3→5
第3轮: cur=3, 数 k 个: 5 → null → 不够 → return dummy.next ✅
```

## 核心要点
- swapPairs 是 k=2 的特例！结构一模一样，只是 k 可变 + 需要先数够
- `tail = cur` 必须放 while 里面（每轮重新数）
- 三个指针：tail(数k个/切断), oldFirst(反转前存组头), nextGroup(下一组)
- 两个 next：`cur.next = newHead` 接上头，`cur.next = nextGroup` 接上尾

## 我犯的错 / My Mistakes
- `tail = cur` 放在 while 外面 → 第二轮 tail 不重置，数错位置
- 把 cur 当成交换对象（应该交换 cur.next 和 cur.next.next）
- 拼音变量名 + 未定义变量混进代码

## 关键词触发 / Triggers
"K个一组翻转" / "每k个反转" → dummy + 数k个 + reverse 子函数 + 头尾都接

11. 138. Copy List with Random Pointer / 复制带随机指针的链表
**难度**: Medium / 中等 | **标签**: Hash Table, Linked List / 哈希表, 链表

## 原题 / Original Problem
Given a linked list of length n, where each node has an additional `random` pointer that can point to any node in the list or null. Construct a **deep copy** of the list — every new node's `next` and `random` must point to new nodes in the copied list.

给你一个长度为 n 的链表，每个节点包含一个额外增加的随机指针 random。构造这个链表的深拷贝——复制链表的指针都不能指向原链表中的节点。

**示例**: [[7,null],[13,0],[11,4],[10,2],[1,0]] → 同样结构的新链表

## 代码 / Code（HashMap 两趟法）
```java
class Solution {
    public Node copyRandomList(Node head) {
        Map<Node, Node> map = new HashMap<>();
        Node oldhead = head;
        // 第一趟：只建节点 + 填 map（旧节点 → 新节点）
        while (head != null) {
            map.put(head, new Node(head.val));
            head = head.next;
        }
        // 第二趟：连接 next 和 random
        head = oldhead;
        while (head != null) {
            map.get(head).next = map.get(head.next);
            map.get(head).random = map.get(head.random);
            head = head.next;
        }
        return map.get(oldhead);
    }
}
```

## 为什么不能一次复制？
random 可能指向当前还没复制到的节点：
```
[7]→[13]→[11]→[10]→[1]
random: null  0    4    2   0
复制 [13] 时它的 random 指向 [0]（[7]），已建 ✓
但复制 [11] 时 random 指向 [4]，还没建！
```
所以必须先建完全部节点，再用 map 对照连接。

## map.get(head) 反复取当前新节点
第二趟不用额外变量，`map.get(head)` 直接拿到"head 对应的新节点"。
`map.get(head.random)` 里 head.random 为 null 时，HashMap 允许 null key，返回 null —— 正好符合要求。

## 我犯的错 / My Mistakes
- 用 ListNode 类型（这题是 Node）
- `map.put(head, cur)` cur 未定义
- 第二趟用了第一趟的 newNode（早没了），应该 `map.get(head)`
- return 要 `map.get(oldhead)`（原头对应的新头）

## 关键词触发 / Triggers
"深拷贝" / "随机指针" / "复制链表" → 两趟 + HashMap(旧→新)

12. 148. Sort List / 排序链表 ❌ 做错过
**难度**: Medium / 中等 | **标签**: Linked List, Merge Sort, Divide and Conquer / 链表, 归并排序, 分治

## 原题 / Original Problem
Given the `head` of a linked list, return the list after sorting it in ascending order. Follow up: sort in O(n log n) time and constant space.

给你链表的头结点 head，将其按升序排列并返回。进阶：O(n log n) 时间 + O(1) 空间。

**示例**: [4,2,1,3] → [1,2,3,4]

## 解法一：递归归并（O(n log n)，空间 O(log n) 递归栈）
```java
class Solution {
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) return head;
        // ① 快慢找中点
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode mid = slow.next;
        slow.next = null;  // 切断成两半
        // ② 递归排序两半
        ListNode left = sortList(head);
        ListNode right = sortList(mid);
        // ③ 合并（就是 21 题的 merge）
        ListNode dummy = new ListNode(-1);
        ListNode cur = dummy;
        while (left != null && right != null) {
            if (left.val <= right.val) { cur.next = left; left = left.next; }
            else { cur.next = right; right = right.next; }
            cur = cur.next;
        }
        cur.next = (left != null) ? left : right;  // 剩余整个接上！
        return dummy.next;
    }
}
```

## 过程追踪 / Walkthrough
```
[4,2,1,3]

sortList([4,2,1,3])
  切: [4,2] 和 [1,3]
  sortList([4,2]) → 切 [4],[2] → merge → [2,4]
  sortList([1,3]) → 切 [1],[3] → merge → [1,3]
  merge([2,4],[1,3]):
    2vs1→取1, 2vs3→取2, 4vs3→取3, right=null
    cur.next=left剩余[4] → [1,2,3,4] ✅
```

## 为什么最后一行 cur.next = 剩余链必须有？
while 条件是 left!=null && right!=null，其中一个先变 null 就退出，剩下那个可能还有节点，整个接上否则全部丢失，链表断在中间。

## 解法二：迭代归并（O(1) 空间）
不用递归，从段长 1 开始迭代合并，段长每轮翻倍：
```java
class Solution {
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) return head;
        int n = 0;
        for (ListNode p = head; p != null; p = p.next) n++;
        ListNode dummy = new ListNode(-1, head);
        for (int len = 1; len < n; len *= 2) {
            ListNode cur = dummy.next, tail = dummy;
            while (cur != null) {
                ListNode l1 = cur;
                cur = cut(l1, len);
                ListNode l2 = cur;
                cur = cut(l2, len);
                tail.next = merge(l1, l2);
                while (tail.next != null) tail = tail.next;
            }
        }
        return dummy.next;
    }
    // 从 head 走 k 步切断，返回下一段头
    ListNode cut(ListNode head, int k) {
        ListNode p = head;
        while (--k > 0 && p != null) p = p.next;
        ListNode next = (p != null) ? p.next : null;
        if (p != null) p.next = null;
        return next;
    }
    ListNode merge(ListNode l1, ListNode l2) { /* 同解法一 */ }
}
```

## 我犯的错 / My Mistakes
- 冒泡思路（相邻交换）→ O(n²) 超时，要归并
- `Node left` 类型错（应该 ListNode）
- `sortList()` 忘了传参数（应该 sortList(head) 和 sortList(mid)）
- 忘写 `cur.next = 剩余链` → 只输出第一个节点

## 关键词触发 / Triggers
"链表排序" / "O(n log n)" → 归并排序（快慢找中 + 切半 + merge）

13. 146. LRU Cache / LRU 缓存 ❌ 做错过
**难度**: Medium / 中等 | **标签**: Hash Table, Linked List, Design / 哈希表, 链表, 设计

## 原题 / Original Problem
Design a data structure that follows the constraints of a **Least Recently Used (LRU) cache**. Implement the `LRUCache` class: get(key) and put(key, value) both run in O(1) average time.

设计并实现满足 LRU（最近最少使用）缓存约束的数据结构。get 和 put 都要求 O(1)。

**示例**: 容量2，put(1,1), put(2,2), get(1)→1, put(3,3)踢掉2, get(2)→-1, ...

## 代码 / Code
```java
class LRUCache {
    class Node {
        int key, val;
        Node prev, next;
        Node(int k, int v) { key = k; val = v; }
    }
    Map<Integer, Node> map = new HashMap<>();
    int capacity;
    Node head = new Node(0, 0);  // 虚拟头（最左=最久没用）
    Node tail = new Node(0, 0);  // 虚拟尾（最右=最新）

    public LRUCache(int capacity) {
        this.capacity = capacity;
        head.next = tail;
        tail.prev = head;
    }

    void addToTail(Node node) {
        node.prev = tail.prev;
        node.next = tail;
        tail.prev.next = node;
        tail.prev = node;
    }
    void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    void moveToTail(Node node) { removeNode(node); addToTail(node); }
    Node removeHead() {
        Node node = head.next;
        removeNode(node);   // ← 必须真删！只返回不删会残留
        return node;
    }

    public int get(int key) {
        if (!map.containsKey(key)) return -1;
        moveToTail(map.get(key));   // 访问了 → 挪到最新
        return map.get(key).val;
    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.val = value;
            moveToTail(node);
        } else {
            if (map.size() == capacity) {
                Node old = removeHead();   // 踢最久没用
                map.remove(old.key);
            }
            Node node = new Node(key, value);
            map.put(key, node);
            addToTail(node);
        }
    }
}
```

## 核心机制：HashMap + 双向链表
```
HashMap: key → 节点（O(1) 查找）
双向链表: 维护使用顺序（O(1) 删/插）

访问（get/put已有key）→ 节点挪到链表尾部（最新）
容量满加新的 → 踢掉链表头部（最久没用）
```

## 为什么必须双向链表？
删除中间节点需要知道前一个：单向链表只能从 head 遍历找前驱 O(n)，双链表 `node.prev.next` 一步 O(1)。

## 虚拟头尾 head/tail 的作用
空链表时 head<->tail 互相指，操作不用特判"空链表"。Node(0,0) 的 0 只是占位，值不重要。

## 我犯的错 / My Mistakes
- removeHead 只 return head.next 不 removeNode → 链表残留淘汰节点 → 容量管理失效
- 以为构造方法要实现全部逻辑 → 其实只是初始化（框架会调用）
- 用 List/ListNode 混着写，机制没想清楚就动笔

## 关键词触发 / Triggers
"LRU" / "最近最少使用" / "O(1) get put" → HashMap + 双向链表 + 虚拟头尾

14. 23. Merge k Sorted Lists / 合并 K 个升序链表
**难度**: Hard / 困难 | **标签**: Linked List, Heap, Merge Sort / 链表, 堆, 归并

## 原题 / Original Problem
You are given an array of `k` linked-lists, each linked-list is sorted in ascending order. Merge all the linked-lists into one sorted linked-list and return it.

给你一个链表数组，每个链表都已经按升序排列。将所有链表合并到一个升序链表中返回。

**示例**: [[1,4,5],[1,3,4],[2,6]] → [1,1,2,3,4,4,5,6]

## 代码 / Code（分治归并）
```java
class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        return merge(lists, 0, lists.length - 1);
    }
    private ListNode merge(ListNode[] lists, int left, int right) {
        if (left == right) return lists[left];
        int mid = left + (right - left) / 2;
        ListNode l1 = merge(lists, left, mid);
        ListNode l2 = merge(lists, mid + 1, right);
        return mergeTwoLists(l1, l2);
    }
    private ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        if (l1.val <= l2.val) {
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }
    }
}
```

## 过程追踪 / Walkthrough
```
lists = [[1,4,5],[1,3,4],[2,6]]

merge(lists, 0, 2)  ← 第一次调用传 (0, length-1)
  mid=1
  merge(0,1): mid=0 → merge(0,0)=[1,4,5], merge(1,1)=[1,3,4]
              mergeTwoLists → [1,1,3,4,4,5]
  merge(2,2) = [2,6]
  mergeTwoLists([1,1,3,4,4,5],[2,6]) = [1,1,2,3,4,4,5,6] ✅
```

## left/right 是哪来的？
递归区间边界：第一次调用显式传 (0, length-1)，之后每层由 mid 分裂：
左半 (left, mid)，右半 (mid+1, right)，直到 left==right（只剩一个链表）。

## l1 l2 是链表头不是数组
lists 是 ListNode[]（装链表头的数组），l1/l2 是单个 ListNode。
mergeTwoLists 每次比较两个链表当前头的 val，取小的接上，递归比剩余。

## 关键词触发 / Triggers
"合并K个有序链表" → 分治归并（两两合并） 或 优先队列（k个头进堆）


# 二叉树 / Binary Tree

1. 94. Binary Tree Inorder Traversal / 二叉树的中序遍历
**难度**: Easy / 简单 | **标签**: Stack, Tree, DFS / 栈, 树, 深度优先

## 原题 / Original Problem
Given the `root` of a binary tree, return the inorder traversal of its nodes' values.

给定一个二叉树的根节点 root，返回它的中序遍历。

**示例**: root=[1,null,2,3] → [1,3,2]

## 三种遍历顺序
```
        2
       / \
      1   3

前序(根左右): [2, 1, 3]  见谁记谁
中序(左根右): [1, 2, 3]  从左往右扫
后序(左右根): [1, 3, 2]  从下往上扫
```

## 代码 / Code（递归）
```java
class Solution {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        dfs(root, result);
        return result;
    }
    private void dfs(TreeNode node, List<Integer> result) {
        if (node == null) return;
        dfs(node.left, result);   // ① 左边
        result.add(node.val);     // ② 自己
        dfs(node.right, result);  // ③ 右边
    }
}
```

递归过程: root=[1,null,2,3]
```
dfs(1): 1.left=null(return) → 记1 → dfs(2)
  dfs(2): 2.left=3 → dfs(3)
    dfs(3): 3.left=null → 记3 → 3.right=null
  记2 → 2.right=null
结果: [1,3,2]
```

## 代码 / Code（迭代/栈）
```java
class Solution {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {       // 一路向左压栈
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();           // 回头处理
            result.add(cur.val);
            cur = cur.right;             // 去右边
        }
        return result;
    }
}
```

## 关键理解
`root=[1,null,2,3]` 不是左根右的排列！
是层层写：根1 → 第二层左null右2 → 第三层2的左孩子3
树结构: 1→右→2→左→3。中序遍历: 1左边null→记1→右边2→2左边3→记3→记2=[1,3,2]

## 关键词触发 / Triggers
"中序遍历" / "二叉树遍历" → 递归(左根右三行) 或 栈迭代(一路向左压栈)

2. 104. Maximum Depth of Binary Tree / 二叉树的最大深度
**难度**: Easy / 简单 | **标签**: Tree, DFS, BFS / 树, 深度优先, 广度优先

## 原题 / Original Problem
Given the `root` of a binary tree, return its maximum depth. The maximum depth is the number of nodes along the longest path from the root node down to the farthest leaf node.

给定一个二叉树 root，返回其最大深度。最大深度是指从根节点到最远叶子节点的最长路径上的节点数。

**示例**: [3,9,20,null,null,15,7] → 3；[1,null,2] → 2

## 代码 / Code
```java
class Solution {
    public int maxDepth(TreeNode root) {
        if (root == null) return 0;
        int left = maxDepth(root.left);
        int right = maxDepth(root.right);
        return Math.max(left, right) + 1;
    }
}
```

## 递归过程 / Walkthrough
```
[3,9,20,null,null,15,7]
        3
       / \
      9  20
         / \
        15  7

maxDepth(3): left=9深度, right=20深度
  maxDepth(9): left=0, right=0 → return 1
  maxDepth(20): left=15深度, right=7深度
    maxDepth(15): → 1
    maxDepth(7): → 1
    right=1, left=1 → return 2
  left=1, right=2 → return max(1,2)+1 = 3 ✅
```

## 核心公式
树深度 = 1（自己） + 左右子树中较深的那个

## 易错点 / Pitfalls
- root 为 null 时深度是 0
- 叶子节点：左右都 null，return 0+0+1=1
- 别用 while 循环 — 树有分叉，while 只能走一条路

## 关键词触发 / Triggers
"二叉树深度" / "最大深度" → 递归 max(left, right) + 1

3. 226. Invert Binary Tree / 翻转二叉树 ❌ 做错过
**难度**: Easy / 简单 | **标签**: Tree, DFS, BFS / 树, 深度优先, 广度优先

## 原题 / Original Problem
Given the `root` of a binary tree, invert the tree, and return its root.

给你一棵二叉树的根节点 root，翻转这棵二叉树，并返回其根节点。

**示例**: [4,2,7,1,3,6,9] → [4,7,2,9,6,3,1]

## 代码 / Code（递归）
```java
class Solution {
    public TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        TreeNode temp = root.left;
        root.left = invertTree(root.right);
        root.right = invertTree(temp);
        return root;
    }
}
```

递归过程: 每个节点交换左右孩子，然后递归翻转孩子自己。
```
      4                   4
     / \                 / \
    2   7     →         7   2
   / \ / \             / \ / \
  1  3 6  9           9  6 3  1
```

## 代码 / Code（迭代/栈）
```java
class Solution {
    public TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            TreeNode temp = node.left;
            node.left = node.right;
            node.right = temp;
            if (node.left != null) stack.push(node.left);
            if (node.right != null) stack.push(node.right);
        }
        return root;
    }
}
```

## 我犯的错 / My Mistakes
- 把 `root.left` 改了之后又用 `root.left` 去取原来的值：`root.left = invert(root.right); root.right = invert(root.left)` ← root.left 已经是右子树了！
- 正确：用 temp 提前存好原来的 left，后面用 temp

## 关键词触发 / Triggers
"翻转二叉树" / "镜像" → 递归交换左右 + temp 暂存

4. 101. Symmetric Tree / 对称二叉树
**难度**: Easy / 简单 | **标签**: Tree, DFS, BFS / 树, 深度优先, 广度优先

## 原题 / Original Problem
Given the `root` of a binary tree, check whether it is a mirror of itself (i.e., symmetric around its center).

给你一个二叉树的根节点 root，检查它是否轴对称。

**示例**: [1,2,2,3,4,4,3] → true；[1,2,2,null,3,null,3] → false

## 代码 / Code（递归）
```java
class Solution {
    public boolean isSymmetric(TreeNode root) {
        return isMirror(root.left, root.right);
    }
    boolean isMirror(TreeNode left, TreeNode right) {
        if (left == null && right == null) return true;
        if (left != null && right != null && left.val == right.val)
            return isMirror(left.left, right.right) && isMirror(left.right, right.left);
        else return false;
    }
}
```

关键: 不是"左右各自对称"，是**左子树和右子树互为镜像** — 左边.left vs 右边.right，左边.right vs 右边.left。

## 代码 / Code（迭代/队列 BFS）
```java
class Solution {
    public boolean isSymmetric(TreeNode root) {
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root.left); q.offer(root.right);
        while (!q.isEmpty()) {
            TreeNode t1 = q.poll(), t2 = q.poll();
            if (t1 == null && t2 == null) continue;
            if (t1 == null || t2 == null || t1.val != t2.val) return false;
            q.offer(t1.left);  q.offer(t2.right); // 交叉配对！
            q.offer(t1.right); q.offer(t2.left);
        }
        return true;
    }
}
```

## 我犯的错 / My Mistakes
- 第一版写成判断"左右各自对称" → 完全错了
- `left != right` 比较的是对象地址不是值 → 应该是 `left.val != right.val`
- 递归调用写在 return 后面变成死代码

## 关键词触发 / Triggers
"对称二叉树" / "镜像" → 双参数递归(左.left vs 右.right) 或 队列交叉配对

5. 543. Diameter of Binary Tree / 二叉树的直径 ❌ 做错过
**难度**: Easy / 简单 | **标签**: Tree, DFS / 树, 深度优先

## 原题 / Original Problem
Given the `root` of a binary tree, return the length of the diameter of the tree. The diameter of a binary tree is the length of the longest path between any two nodes in a tree. This path may or may not pass through the root.

给你一棵二叉树的根节点，返回该树的直径。直径是任意两个节点之间最长路径的长度（边数），可能不经过根。

**示例**: [1,2,3,4,5] → 3（路径 [4,2,1,3] 或 [5,2,1,3]）

## 代码 / Code
```java
class Solution {
    int max = 0;
    public int diameterOfBinaryTree(TreeNode root) {
        depth(root);
        return max;
    }
    int depth(TreeNode root) {
        if (root == null) return 0;
        int left = depth(root.left);
        int right = depth(root.right);
        max = Math.max(max, left + right);  // 当前节点当拐点的直径候选
        return Math.max(left, right) + 1;   // 返回深度给上层
    }
}
```

过程: 每个节点算出左右深度，`left+right` 就是经过该节点的路径长度。全局 max 记最大值。

## 核心区别
| `max`（类成员） | `depth` 返回值 |
|---|---|
| 全局最大直径 | 当前节点深度 |
| `max(max, left+right)` | `max(left,right)+1` |

## 为什么 diameter 不一定经过根？
```
    1
   /
  2
 / \
4   5
     \
      6
根1: left=3, right=0 → 直径候选=3
节点2: left=2, right=2 → 直径候选=4 ← 更大！不经过根
```

## 我犯的错 / My Mistakes
- depth 里写了 `return max + 1` → max 是全局直径不是深度，应该是 `Math.max(left,right)+1`
- 忘了 `root==null` 终止条件导致死循环
- 不知道用类成员变量来跨递归层级记录全局最大值

## 关键词触发 / Triggers
"二叉树直径" / "最长路径" → maxDepth变体 + 全局变量记 left+right

6. 102. Binary Tree Level Order Traversal / 二叉树的层序遍历
**难度**: Medium / 中等 | **标签**: Tree, BFS / 树, 广度优先

## 原题 / Original Problem
Given the `root` of a binary tree, return the level order traversal of its nodes' values. (i.e., from left to right, level by level).

给你二叉树的根节点 root，返回其节点值的层序遍历（逐层从左到右）。

**示例**: [3,9,20,null,null,15,7] → [[3],[9,20],[15,7]]

## 代码 / Code（BFS 队列）
```java
class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        while (!q.isEmpty()) {
            int size = q.size();
            List<Integer> curLevel = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = q.poll();
                curLevel.add(node.val);
                if (node.left != null) q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }
            result.add(curLevel);
        }
        return result;
    }
}
```

过程: 队列排队，每轮 for 弹完当前层，同时下一层自动入队。
```
[3]       → 弹3,  9,20入队  → [[3]]
[9,20]    → 弹9,20, 15,7入队 → [[3],[9,20]]
[15,7]    → 弹15,7, 空      → [[3],[9,20],[15,7]]
```

## 关键理解
- `List<List<Integer>>`：外层List=所有层，内层List=某层的数值
- `q.size()` 在弹之前记：当前队列里全是同一层
- 队列工作原理：弹一个 → 它的孩子入队 → 队列自己变 → 下一轮

## 关键词触发 / Triggers
"层序遍历" / "逐层" → BFS + 队列 + size 控制层

7. 108. Convert Sorted Array to BST / 将有序数组转换为二叉搜索树
**难度**: Easy / 简单 | **标签**: Tree, BST, Divide and Conquer / 树, 二叉搜索树, 分治

## 原题 / Original Problem
Given an integer array `nums` where the elements are sorted in ascending order, convert it to a height-balanced binary search tree.

给你一个按升序排列的整数数组 nums，将其转换为一棵平衡的二叉搜索树。

**示例**: [-10,-3,0,5,9] → [0,-3,9,-10,null,5]

## 代码 / Code（递归）
```java
class Solution {
    public TreeNode sortedArrayToBST(int[] nums) {
        return build(nums, 0, nums.length - 1);
    }
    TreeNode build(int[] nums, int left, int right) {
        if (left > right) return null;
        int mid = (left + right) / 2;
        TreeNode root = new TreeNode(nums[mid]);
        root.left = build(nums, left, mid - 1);
        root.right = build(nums, mid + 1, right);
        return root;
    }
}
```

过程: [-10,-3,0,5,9]，mid=2取0当根 → 左边[-10,-3]取-3当左子 → 右边[5,9]取5当右子 → 递归到底。
```
       0
      / \
    -3   5
    /     \
  -10     9
```

## 为什么必定平衡？
每次取正中间，左右一半大小差≤1。递归下去高度自动 log n。

## 关键词触发 / Triggers
"有序数组转BST" / "平衡" → 递归取中间 + 分治


8. 98. Validate Binary Search Tree / 验证二叉搜索树
**难度**: Medium / 中等 | **标签**: Tree, DFS, BST, Recursion / 树, 深度优先, 二叉搜索树, 递归

## 原题 / Original Problem
Given the root of a binary tree, determine whether it is a valid binary search tree.

给定一个二叉树的根节点 root，判断其是否是一个有效的二叉搜索树。

有效 BST 要求：
- 左子树所有节点都严格小于当前节点
- 右子树所有节点都严格大于当前节点
- 左右子树自身也必须是有效 BST

**示例**:
- `[2,1,3] → true`
- `[5,1,4,null,null,3,6] → false`

## 代码 / Code（递归 + 上下界）
```java
class Solution {
    public boolean isValidBST(TreeNode root) {
        return check(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean check(TreeNode node, long min, long max) {
        if (node == null) return true;

        if (node.val <= min || node.val >= max) {
            return false;
        }

        return check(node.left, min, node.val)
            && check(node.right, node.val, max);
    }
}
````

## 为什么不能只比较父节点和左右孩子？

例如：

```text
      5
     / \
    1   7
       /
      3
```

如果只比较父子：

```text
1 < 5 < 7 ✓
3 < 7 ✓
```

看起来都没问题。

但实际上 `3` 在 `5` 的右子树中，而：

```text
3 < 5
```

违反了 BST 的要求：

```text
5 的整个右子树都必须 > 5
```

所以不能只检查：

```text
左孩子 < 父节点 < 右孩子
```

必须让每个节点都处于一个合法范围内。

## 核心思路：给每个节点维护合法区间

根节点没有限制：

```text
(-∞, +∞)
```

所以：

```java
check(root, Long.MIN_VALUE, Long.MAX_VALUE)
```

到了节点 `5`：

```text
左子树范围：(-∞, 5)
右子树范围：(5, +∞)
```

如果当前节点是 `7`：

```text
它本身范围：(5, +∞)

它的左子树范围：
(5, 7)

它的右子树范围：
(7, +∞)
```

所以递归的时候：

```java
左子树 → check(node.left, min, node.val)
右子树 → check(node.right, node.val, max)
```

每往下一层，合法范围都会进一步缩小。

## 过程追踪 / Walkthrough

```text
树：

      5
     / \
    1   7
       /
      3

check(5, -∞, +∞)
5 合法

左：
check(1, -∞, 5)
1 合法
→ true

右：
check(7, 5, +∞)
7 合法

7 的左：
check(3, 5, 7)

3 <= 5
→ 越界
→ false

最终：
false ✅
```

## 为什么边界是 long？

题目节点值可能达到：

```text
-2^31 ~ 2^31-1
```

如果直接使用：

```java
Integer.MIN_VALUE
Integer.MAX_VALUE
```

那么当节点本身就是极值时会被误判。

例如节点值：

```text
Integer.MIN_VALUE
```

如果下界也是：

```text
Integer.MIN_VALUE
```

那么：

```java
node.val <= min
```

成立，会错误返回 false。

所以使用：

```java
Long.MIN_VALUE
Long.MAX_VALUE
```

让初始范围真正覆盖所有 int 节点值。

## 为什么必须 return 左右递归结果？

不能只写：

```java
check(node.left, min, node.val);
check(node.right, node.val, max);
```

因为子树返回的 `false` 会被丢掉。

必须：

```java
return check(node.left, min, node.val)
    && check(node.right, node.val, max);
```

只要左子树或右子树有一个不合法，整棵树就是无效 BST。

## 易错点 / My Mistakes

* 一开始认为只检查“父节点 < 左右孩子”就够了，其实不够，必须限制**整个子树**。
* `root` 的初始边界应该是：

```java
Long.MIN_VALUE, Long.MAX_VALUE
```

* BST 要求的是**严格小于 / 严格大于**，所以判断必须是：

```java
node.val <= min || node.val >= max
```

* 左子树：

```java
(min, node.val)
```

* 右子树：

```java
(node.val, max)
```

* 递归调用必须 `return` 回去，否则子树的 false 会丢失。
* `null` 节点表示当前子树合法结束：

```java
if (node == null) return true;
```

## 关键词触发 / Triggers

"验证二叉搜索树" / "BST是否合法" / "左小右大" → **递归 + 上下界约束**

看到：

```text
不能只比较父子
```

第一反应：

```text
给每个节点一个合法区间
左子树缩小 upper
右子树抬高 lower
```

9. 230. Kth Smallest Element in a BST / 二叉搜索树中第 K 小的元素

难度: Medium / 中等 | 标签: Tree, BST, DFS, Stack / 树, 二叉搜索树, 深度优先, 栈

原题 / Original Problem

Given the root of a binary search tree, and an integer k, return the kth smallest value in the tree.

给定一个二叉搜索树的根节点 root 和整数 k，返回其中第 k 小的元素。

示例:

root = [3,1,4,null,2], k = 1 → 1
root = [5,3,6,2,4,null,null,1], k = 3 → 3
代码 / Code（BST 中序遍历）
class Solution {
    public int kthSmallest(TreeNode root, int k) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = root;


        while (cur != null || !stack.isEmpty()) {
            // 一路向左
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }


            // 当前未处理节点中最小的一个
            cur = stack.pop();
            k--;


            if (k == 0) {
                return cur.val;
            }


            // 转向右子树
            cur = cur.right;
        }


        return -1;
    }
}
为什么 BST 的中序遍历是递增的？

BST 满足：

左子树所有节点 < 当前节点 < 右子树所有节点

而中序遍历顺序正好是：

左 → 根 → 右

所以：

有序左子树
↓
当前节点
↓
有序右子树

最终得到完整的递增序列。

例如：

        5
       / \
      3   7
     / \ / \
    2  4 6  8

中序遍历：

2 3 4 5 6 7 8

因此第 k 个被访问的节点就是第 k 小。

过程追踪 / Walkthrough
root = [3,1,4,null,2], k=1


        3
       / \
      1   4
       \
        2


一路向左：
3 → 1


弹出1：
k = 1-1 = 0
→ return 1 ✅

再看 k=3：

中序顺序：
1 → 2 → 3 → 4


1：k=3→2
2：k=2→1
3：k=1→0
→ return 3 ✅
核心思路

普通做法可以：

中序遍历
→ 全部放进 List
→ 取 result[k-1]

但没必要。

因为中序遍历本身就是有序输出：

找到一个节点
→ k--
→ k==0
→ 直接返回

因此不需要保存整个结果数组。

为什么用栈？

递归版本本质上也是：

一路向左
↓
回到父节点
↓
处理右子树

显式 Stack<TreeNode> 就是在手动模拟递归调用栈。

stack = 还没处理、等待回来的节点
cur   = 当前正在处理的节点
复杂度
时间：O(H + k)

其中 H 为树高，最坏为：

O(n)

空间：

O(H)

栈最多保存一条从根到叶子的路径。

进阶：频繁修改 + 频繁查询第 K 小

普通中序遍历每次查询都需要走树。

如果 BST 经常插入、删除，而且频繁查询第 k 小，可以给每个节点增加：

int size;

表示：

以当前节点为根的子树节点总数

例如：

        5
       / \
      3   7
     / \
    2   4
2.size = 1
4.size = 1
3.size = 3
7.size = 1
5.size = 5

查询时只看左子树有多少节点：

leftSize = size(node.left)
k <= leftSize → 第 k 小在左子树
k == leftSize + 1 → 当前节点就是答案
k > leftSize + 1 → 去右子树，k -= leftSize + 1

如果再使用 AVL、红黑树、Treap 等平衡 BST：

插入：O(log n)
删除：O(log n)
第 K 小：O(log n)

这就是 Order Statistic Tree / 顺序统计树 的思想。

易错点 / My Mistakes
BST 中序遍历是递增序列，这是这题最关键的性质。
不需要把全部节点放入 List，找到第 k 个直接返回。
Stack 里存的是 TreeNode，cur 是当前节点。
k-- 应该发生在真正“访问节点”的时候。
k == 0 时直接返回当前节点值。
进阶的 size 版本需要提前维护子树大小，不能每次查询再重新遍历子树，否则还是 O(n)。
LeetCode 原题的 TreeNode 没有 size 字段；size 版属于增强 BST 的数据结构设计。
关键词触发 / Triggers

"BST 第 K 小" / "第 K 小元素" → 中序遍历 + k--

看到：

BST + 第K小

第一反应：

BST 中序 = 递增序列
→ 第 K 个访问到的节点就是答案
10. 199. Binary Tree Right Side View / 二叉树的右视图

难度: Medium / 中等 | 标签: Tree, BFS, Queue / 树, 广度优先, 队列

原题 / Original Problem

Given the root of a binary tree, imagine yourself standing on the right side of it. Return the values of the nodes you can see from top to bottom.

给定一棵二叉树，假设站在它的右侧，从上到下返回从右侧能够看到的节点值。

示例:

[1,2,3,null,5,null,4] → [1,3,4]
代码 / Code（BFS 层序遍历）
class Solution {
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();


        if (root == null) {
            return result;
        }


        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);


        while (!q.isEmpty()) {
            int size = q.size();


            for (int i = 0; i < size; i++) {
                TreeNode node = q.poll();


                // 当前层最后一个节点 = 右视图能看到的节点
                if (i == size - 1) {
                    result.add(node.val);
                }


                if (node.left != null) {
                    q.offer(node.left);
                }


                if (node.right != null) {
                    q.offer(node.right);
                }
            }
        }


        return result;
    }
}
核心思路：每层最后一个节点

BFS 天然是一层一层遍历：

第1层 → 第2层 → 第3层 → ...

如果每层按照：

左 → 右

遍历，那么：

这一层最后被访问到的节点
=
这一层最右边的节点

所以只需要：

if (i == size - 1)

把它加入答案。

过程追踪 / Walkthrough
        1
       / \
      2   3
       \   \
        5   4

第一层：

[1]


i=0
size=1
i == size-1
→ 加入 1

第二层：

[2,3]


i=0 → 2，不加入
i=1 → 3，加入

第三层：

[5,4]


i=0 → 5，不加入
i=1 → 4，加入

最终：

[1,3,4] ✅
为什么一定要保存 size = q.size()？

因为：

int size = q.size();

表示：

当前这一轮队列中的节点全部属于同一层。

然后：

for (int i = 0; i < size; i++)

只处理这一层。

处理当前节点时，它的左右孩子会进入队列，但不会影响当前 for 应该处理多少个节点。

所以 size 就是“这一层的边界”。

另一种写法

也可以：

右子树先入队
左子树后入队

这样每层第一个弹出来的节点就是最右节点。

但你现在这种：

左 → 右
取最后一个

更符合你之前已经掌握的层序遍历模板。

易错点 / My Mistakes
一开始把每个节点都加入 result，这样得到的是完整的层序遍历，不是右视图。
判断当前节点是不是这一层最后一个：
i == size - 1
size = q.size() 必须在处理当前层之前保存。
root == null 时不能直接 q.offer(root)，否则后面访问 node.val 会空指针。
当前层的孩子虽然会入队，但由于 size 已经固定，不会被错误地算进当前层。
关键词触发 / Triggers

"右视图" / "每层最右边" / "从右边看到" → BFS + 每层最后一个

看到：

每层第一个/最后一个节点

第一反应：

BFS
+
size = q.size()
+
控制当前层
11. 114. Flatten Binary Tree to Linked List / 二叉树展开为链表

难度: Medium / 中等 | 标签: Tree, DFS, Linked List, In-place / 树, 深度优先, 链表, 原地

原题 / Original Problem

Given the root of a binary tree, flatten the tree into a linked list in-place.

给定二叉树的根节点 root，将它展开为一个单链表。

要求：

仍然使用 TreeNode
right 指向链表下一个节点
所有 left = null
链表顺序与二叉树先序遍历相同
代码 / Code（原地 O(1) 空间）
class Solution {
    public void flatten(TreeNode root) {
        TreeNode cur = root;


        while (cur != null) {
            if (cur.left != null) {


                // 找左子树中最右边的节点
                TreeNode pre = cur.left;


                while (pre.right != null) {
                    pre = pre.right;
                }


                // 原来的右子树接到左子树最右节点后面
                pre.right = cur.right;


                // 左子树搬到右边
                cur.right = cur.left;


                // 清空左指针
                cur.left = null;
            }


            // 继续处理新的右节点
            cur = cur.right;
        }
    }
}
核心思路：根 → 左子树 → 原右子树

先序遍历：

根 → 左 → 右

假设当前：

        1
       / \
      2   5
     / \
    3   4

我们希望展开：

1 → 2 → 3 → 4 → 5

当前节点 1 有：

left = 2
right = 5

左子树展开后：

2 → 3 → 4

所以原来的右子树 5 应该接到：

左子树最右节点 4

后面。

于是：

pre.right = cur.right;

然后：

cur.right = cur.left;
cur.left = null;

当前结构就变成：

1
 \
  2
 / \
3  4
    \
     5

继续处理 2、3、4，最终变成：

1
 \
  2
   \
    3
     \
      4
       \
        5
过程追踪 / Walkthrough
原树：


        1
       / \
      2   5
     / \
    3   4


cur = 1


1. cur.left != null
   pre = 2


2. 找 pre 的最右节点
   2.right = 4
   → pre = 4


3. 原右子树接到 pre 后面
   4.right = 5


4. 左子树搬到右边
   1.right = 2


5. 清空左指针
   1.left = null


得到：


1
 \
  2
 / \
3  4
    \
     5


cur = cur.right
→ cur = 2


继续处理，最终：


1 → 2 → 3 → 4 → 5 ✅
为什么不需要“往上遍历”？

看起来像：

先走到最左边
→ 再往上返回

但迭代版实际上不用真的回到父节点。

每次只做：

当前节点有左子树
↓
找左子树最右节点 pre
↓
把当前原右子树接到 pre 后面
↓
左子树搬到右边
↓
当前节点继续向右

所以只需要一个：

TreeNode cur

不断往 right 走。

为什么 pre 要找“左子树最右节点”？

因为先序顺序是：

当前节点 → 左子树 → 右子树

左子树展开后，最后一个节点就是：

左子树中序意义上的最右节点

把原来的右子树接到它后面，就能得到：

当前节点
↓
完整左子树
↓
原右子树

顺序刚好符合先序遍历。

为什么不能直接 cur.right = cur.left？

因为原来的：

cur.right

会失去引用。

所以一定要先保存：

pre.right = cur.right;

把原右子树接起来，再：

cur.right = cur.left;
为什么代码只有一个 if？

不需要判断：

pre.right.left
pre.right.right

也不需要关心前驱到底是不是叶子。

我们真正关心的只有：

找到左子树最右边那个节点。

所以统一写：

TreeNode pre = cur.left;


while (pre.right != null) {
    pre = pre.right;
}

找到以后直接接线即可。

复杂度
额外空间：O(1)

时间复杂度可看作：

O(n)

每次找到前驱并重新连接，整体遍历树中的节点和边的次数是线性的。

易错点 / My Mistakes
一开始按“递归返回父节点”的方式思考，导致代码出现大量 if 判断。
不需要判断 pre.right.left、pre.right.right，只需要一直找最右节点。
必须先：
pre.right = cur.right;

否则原来的右子树会丢失。

然后：
cur.right = cur.left;
cur.left = null;
这题要求修改原树，不是返回 List<Integer>，方法是 void。
root == null 时直接让 cur = root，while 自然结束，不需要额外处理。
关键词触发 / Triggers

"二叉树展开为链表" / "先序顺序" / "原地 O(1)" → 找左子树最右节点 + 指针重连

看到：

根 → 左 → 右
+
原地修改

第一反应：

找左子树最右节点 pre
→ pre.right = 原右子树
→ cur.right = 左子树
→ cur.left = null
三题放在一起记
230 BST 第K小
→ 中序遍历 = 升序
→ k-- 找第K个


199 二叉树右视图
→ BFS 一层一层
→ 取每层最后一个


114 二叉树展开
→ 先序 根左中右
→ 左子树最右节点接原右子树
→ 左搬右，left清空

这三题可以连成一个很好的二叉树知识链：

BST 的特殊性质
        ↓
230：中序 = 有序


普通二叉树的层级结构
        ↓
199：BFS + size 控制层


普通二叉树的结构重连
        ↓
114：先序 + 原地指针操作

12. 105. Construct Binary Tree from Preorder and Inorder Traversal / 从前序与中序遍历序列构造二叉树

难度: Medium / 中等 | 标签: Tree, DFS, Hash Map, Divide and Conquer / 树, 深度优先, 哈希表, 分治

原题 / Original Problem

Given two integer arrays preorder and inorder, construct the binary tree and return its root.

给定二叉树的先序遍历 preorder 和中序遍历 inorder，构造二叉树并返回根节点。

示例:

preorder = [3,9,20,15,7]
inorder  = [9,3,15,20,7]


→


        3
       / \
      9   20
         /  \
        15   7
代码 / Code（递归 + HashMap）
class Solution {


    Map<Integer, Integer> map = new HashMap<>();


    public TreeNode buildTree(int[] preorder, int[] inorder) {


        // inorder中的“值 → 下标”
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }


        return build(
            preorder, 0, preorder.length - 1,
            inorder, 0, inorder.length - 1
        );
    }


    private TreeNode build(
        int[] preorder, int preLeft, int preRight,
        int[] inorder, int inLeft, int inRight
    ) {
        if (preLeft > preRight) {
            return null;
        }


        // 前序第一个元素 = 当前子树根
        TreeNode root = new TreeNode(preorder[preLeft]);


        // 找根在中序中的位置
        int inIndex = map.get(root.val);


        // 左子树节点数量
        int leftSize = inIndex - inLeft;


        // 构造左子树
        root.left = build(
            preorder,
            preLeft + 1,
            preLeft + leftSize,
            inorder,
            inLeft,
            inIndex - 1
        );


        // 构造右子树
        root.right = build(
            preorder,
            preLeft + leftSize + 1,
            preRight,
            inorder,
            inIndex + 1,
            inRight
        );


        return root;
    }
}
核心思路：前序找根，中序切左右

这题最关键的两条性质：

前序遍历
根 → 左 → 右

所以：

preorder[preLeft]

一定是当前子树的根。

中序遍历
左 → 根 → 右

找到根以后：

左边 = 左子树
右边 = 右子树

例如：

preorder = [3,9,20,15,7]
inorder  = [9,3,15,20,7]

前序第一个：

3

所以根是 3。

在 inorder 中：

[9] 3 [15,20,7]

因此：

左子树 = [9]
右子树 = [15,20,7]

然后递归处理左右子树。

过程追踪 / Walkthrough
preorder = [3,9,20,15,7]
inorder  = [9,3,15,20,7]


第一层：


preorder第一个 = 3
→ root = 3


inorder找到3：
[9] 3 [15,20,7]


左子树：
preorder = [9]
inorder  = [9]


右子树：
preorder = [20,15,7]
inorder  = [15,20,7]




处理右子树：


preorder第一个 = 20
→ root = 20


inorder：
[15] 20 [7]


所以：
左子树 = 15
右子树 = 7




最终：


        3
       / \
      9   20
         /  \
        15   7
为什么递归函数返回 TreeNode？

这题不是返回一个数字。

每次递归处理的是：

当前范围对应的整棵子树。

所以：

private TreeNode build(...)

表示：

给我这一段 preorder + inorder，我帮你构造这棵子树，并返回它的根节点。

然后父节点直接接收：

root.left = build(...);
root.right = build(...);

这就是递归构树的核心模式：

当前 root
   ↓
build 左子树 → 返回 TreeNode
   ↓
接到 root.left


build 右子树 → 返回 TreeNode
   ↓
接到 root.right


return root
为什么需要边界，而不是直接切数组？

可以直接创建新的子数组，但会产生大量额外空间和拷贝。

所以这里只传：

preLeft / preRight
inLeft / inRight

表示当前子树在两个遍历数组中的范围。

例如：

左子树 inorder：
inLeft → inIndex - 1


右子树 inorder：
inIndex + 1 → inRight
最关键：怎么确定 preorder 的左右范围？

设：

int leftSize = inIndex - inLeft;

表示：

左子树有多少个节点。

因为 preorder 是：

根 → 左 → 右

所以：

左子树 preorder

根后面的 leftSize 个元素：

preLeft + 1
~
preLeft + leftSize
右子树 preorder

剩下的：

preLeft + leftSize + 1
~
preRight

这个下标关系是本题最容易写错的地方。

为什么需要 HashMap？

如果每次都在 inorder 中：

for (int i = inLeft; i <= inRight; i++) {
    if (inorder[i] == root.val) ...
}

那么每层递归都可能重新扫描。

最坏情况下：

O(n²)

所以提前建立：

Map<Integer, Integer> map

保存：

值 → 在 inorder 中的位置

例如：

9 → 0
3 → 1
15 → 2
20 → 3
7 → 4

这样：

int inIndex = map.get(root.val);

就可以：

O(1)

直接找到根的位置。

易错点 / My Mistakes
一开始把 root 写成 int，实际上需要：
TreeNode root = new TreeNode(preorder[preLeft]);
Map 的方向写反了。应该：
map.put(inorder[i], i);

不是：

map.put(i, inorder[i]);
递归函数必须返回 TreeNode，因为它构造的是一棵子树。
递归结果不能直接丢掉：
build(...);
build(...);

必须：

root.left = build(...);
root.right = build(...);
必须有递归终止条件：
if (preLeft > preRight) return null;
不需要 dummy。树的构造就是“递归返回子树根节点，然后挂到当前节点上”。
preorder 第一个元素是当前根；inorder 中根左边是左子树，右边是右子树。
题目中的输出 [3,9,20,null,null,15,7] 是 LeetCode 对树的序列化展示，不代表你需要自己进行一次层序遍历。
复杂度

建立 HashMap：

O(n)

每个节点只构造一次：

O(n)

所以：

时间：O(n)
空间：O(n)

其中 O(n) 空间来自 HashMap 和递归栈。

关键词触发 / Triggers

"前序 + 中序构造二叉树" → 前序找根 + 中序切左右 + 递归 + HashMap

看到：

preorder + inorder

第一反应：

preorder 第一个
↓
当前根


去 inorder 找根
↓
左边 = 左子树
右边 = 右子树


递归构造
这题和前面几道树题串起来
98. 验证 BST
→ BST 的全局范围约束


230. BST 第 K 小
→ BST 中序 = 递增


199. 二叉树右视图
→ BFS + 每层最后一个


114. 二叉树展开
→ 先序顺序 + 原地指针重连


105. 前序 + 中序构造二叉树
→ 前序找根，中序拆左右

这题你真正需要记住的不是整份代码，而是这条链：

前序：根 → 左 → 右
中序：左 → 根 → 右


前序第一个 = 根
中序找到根 = 划分左右子树
↓
递归构造

# 图论 / Graph

1. 200. Number of Islands / 岛屿数量
**难度**: Medium / 中等 | **标签**: Array, DFS, BFS, Matrix / 数组, 深度优先, 广度优先, 矩阵

## 原题 / Original Problem
Given an m x n 2D binary grid `grid` which represents a map of `'1'`s (land) and `'0'`s (water), return the number of islands. An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically.

给你一个由 '1'（陆地）和 '0'（水）组成的二维网格，计算网格中岛屿的数量。

**示例**: 见原题

## 代码 / Code
```java
class Solution {
    int m, n;
    public int numIslands(char[][] grid) {
        int count = 0;
        m = grid.length; n = grid[0].length;
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                if (grid[i][j] == '1') { count++; dfs(grid, i, j); }
        return count;
    }
    void dfs(char[][] grid, int a, int b) {
        if (a < 0 || a >= m || b < 0 || b >= n) return;
        if (grid[a][b] == '0') return;
        grid[a][b] = '0';
        dfs(grid, a+1, b); dfs(grid, a-1, b);
        dfs(grid, a, b+1); dfs(grid, a, b-1);
    }
}
```

## 过程追踪 / Walkthrough
```
[1,1,0]
[1,0,0]
[0,0,1]

i=0,j=0: '1' → count=1, dfs淹 (0,0)(0,1)(1,0) → 全变0
i=0,j=1: '0' → 跳过
...
i=2,j=2: '1' → count=2, dfs淹

count=2 ✅
```

## 核心思路：DFS 洪水填充
找到一个 '1' → 计数+1 → 递归把相连的 '1' 全淹成 '0'（上下左右）
遍历完网格，数到几个 '1' 就有几个岛。

## 易错点 / Pitfalls
- `=` 赋值 vs `==` 比较：`grid[i][j] == '1'`
- `'0'` 检查在置零之前，否则无限递归
- `m n` 用类成员，别加 `int`（会隐藏成员）
- 边界检查放 dfs 第一行

## 关键词触发 / Triggers
"岛屿数量" / "连通区域" / "网格搜索" → DFS 洪水填充 或 BFS

2. 207. Course Schedule / 课程表
**难度**: Medium / 中等 | **标签**: DFS, BFS, Graph, Topological Sort / 深度优先, 广度优先, 图, 拓扑排序

## 原题 / Original Problem
There are a total of `numCourses` courses. Some courses have prerequisites, where `prerequisites[i] = [ai, bi]` means you must take `bi` before `ai`. Return `true` if you can finish all courses.

你这个学期必须选修 numCourses 门课程。先修课程按数组 prerequisites 给出，prerequisites[i] = [ai, bi] 表示学习 ai 前必须先学 bi。判断是否可能完成所有课程。

**示例**: 2, [[1,0]] → true；2, [[1,0],[0,1]] → false（循环依赖）

## 解法一：三色 DFS 判环
```java
class Solution {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) graph.add(new ArrayList<>());
        for (int[] p : prerequisites) {
            int a = p[0], b = p[1];
            graph.get(b).add(a);  // b → a
        }
        int[] visited = new int[numCourses]; // 0白 1灰 2黑
        for (int i = 0; i < numCourses; i++) {
            if (visited[i] == 0 && hasCycle(i, graph, visited)) return false;
        }
        return true;
    }
    boolean hasCycle(int course, List<List<Integer>> graph, int[] visited) {
        if (visited[course] == 1) return true;   // 灰=当前路径上有环
        if (visited[course] == 2) return false;  // 黑=以前查过了没环
        visited[course] = 1;
        for (int nei : graph.get(course))
            if (hasCycle(nei, graph, visited)) return true;
        visited[course] = 2;
        return false;
    }
}
```

## 解法二：BFS 拓扑排序
先上"不要等任何人"的课（入度=0），上完一门，等它的人减少一个依赖，谁的入度变成0了就入队继续上。
```java
class Solution {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) graph.add(new ArrayList<>());
        int[] indegree = new int[numCourses];
        for (int[] p : prerequisites) {
            int a = p[0], b = p[1];
            graph.get(b).add(a);
            indegree[a]++;
        }
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < numCourses; i++)
            if (indegree[i] == 0) q.offer(i);
        int count = 0;
        while (!q.isEmpty()) {
            int course = q.poll(); count++;
            for (int next : graph.get(course)) {
                indegree[next]--;
                if (indegree[next] == 0) q.offer(next);
            }
        }
        return count == numCourses;
    }
}
```

## 三色标记含义
| 颜色 | 数字 | 含义 |
|---|---|---|
| 白 | 0 | 还没碰过 |
| 灰 | 1 | 正在当前递归路径上 → 遇到灰=有环 |
| 黑 | 2 | 已处理完，确定没环 → 直接跳过 |

## 拓扑排序核心
入度 = 这门课要等几门。入度=0 → 直接能上。上完一门 → 等它的课入度-1。
最后上了 count 门，count == numCourses 就没环。

## 关键词触发 / Triggers
"先修课" / "课程表" / "循环依赖" → 三色DFS判环 或 拓扑排序


# 回溯 / Backtracking

1. 46. Permutations / 全排列
**难度**: Medium / 中等 | **标签**: Array, Backtracking / 数组, 回溯

## 原题 / Original Problem
Given an array `nums` of distinct integers, return all the possible permutations.

给定一个不含重复数字的数组 nums，返回其所有可能的全排列。

**示例**: [1,2,3] → [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]

## 代码 / Code
```java
class Solution {
    List<List<Integer>> result = new ArrayList<>();
    int[] nums;
    public List<List<Integer>> permute(int[] nums) {
        this.nums = nums;
        backtrack(new ArrayList<>(), new boolean[nums.length]);
        return result;
    }
    void backtrack(List<Integer> cur, boolean[] used) {
        if (cur.size() == nums.length) {
            result.add(new ArrayList<>(cur)); // 必须拷贝！
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            cur.add(nums[i]);     used[i] = true;
            backtrack(cur, used);
            cur.remove(cur.size() - 1); used[i] = false; // 回溯！
        }
    }
}
```

## 过程追踪 / Walkthrough
```
nums=[1,2,3]

[] → 选1 → [1] → 选2 → [1,2] → 选3 → [1,2,3] ✓
                ← 回溯删3 ←
         [1] → 选3 → [1,3] → 选2 → [1,3,2] ✓
                ← 回溯删2 ←
    ← 回溯删1 ←
[] → 选2 → [2] → ... 递归树展开所有分支
```

## 核心操作
- `used[i]` 标记是否已选 → 避免重复
- `cur.add(num)` 选了 → `cur.remove(last)` 回溯吐出来 → 换一个试
- `new ArrayList<>(cur)` 必须拷贝：cur 会被后续回溯修改

## 关键词触发 / Triggers
"全排列" / "所有组合" / "互不相同" → 回溯 + used 标记


# 双指针问题 / Two Pointers

1. 283. Move Zeroes / 移动零
**难度**: Easy / 简单 | **标签**: Array, Two Pointers / 数组, 双指针

## 原题 / Original Problem
Given an integer array `nums`, move all `0`'s to the end of it while maintaining the relative order of the non-zero elements. You must do this **in-place** without making a copy of the array.

给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。必须在不复制数组的情况下原地操作。

**示例**: nums = [0,1,0,3,12] → [1,3,12,0,0]

## 代码 / Code
```java
class Solution {
    public void moveZeroes(int[] nums) {
        int slow = 0;
        for (int fast = 0; fast < nums.length; fast++) {
            if (nums[fast] != 0) {
                nums[slow] = nums[fast];
                slow++;
            }
        }
        for (int i = slow; i < nums.length; i++) {
            nums[i] = 0;
        }
    }
}
```

## 核心思路：快慢指针
- fast：遍历每个元素，找非零
- slow：记录下一个非零元素应该放的位置
- fast 找到非零 → 搬到 slow 位置 → slow++
- 遍历完后 slow 后面全部填 0

## 易错点
- 不能排序！排序会打乱非零元素的相对顺序
- 最后要手动填 0，只搬不填会有残留

## 关键词触发
"原地" / "移动0到末尾" / "保持相对顺序" → 快慢指针

2. 11. Container With Most Water / 盛最多水的容器
**难度**: Medium / 中等 | **标签**: Array, Two Pointers, Greedy / 数组, 双指针, 贪心

## 原题 / Original Problem
Given an integer array `height` of length `n`, find two lines that together with the x-axis form a container, such that the container contains the most water. Return the maximum amount of water.

给定一个长度为 n 的整数数组 height，找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。

**示例**: height = [1,8,6,2,5,4,8,3,7] → 49

## 代码 / Code
```java
class Solution {
    public int maxArea(int[] height) {
        int left = 0, right = height.length - 1, maxArea = 0;
        while (left < right) {
            int w = right - left;
            int h = Math.min(height[left], height[right]);
            maxArea = Math.max(maxArea, w * h);
            if (height[left] < height[right]) left++;
            else right--;
        }
        return maxArea;
    }
}
```

## 面积公式
面积 = (right - left) × min(height[left], height[right])

## 核心思路：左右指针
- left=0, right=n-1，从最大宽度开始
- **移动较矮的那根**：宽度必缩小，要赌高度变大，矮的必须被抛弃

## 易错点
- 高度取 min，不是取哪根线
- 移动的是较矮的那根，和快慢指针不同（这题是 ←→ 两端指针）

## 关键词触发
"两条线" / "围成容器" / "最大面积" → 左右指针 + 移动较矮边

3. 42. Trapping Rain Water / 接雨水
**难度**: Hard / 困难 | **标签**: Array, Two Pointers, DP, Stack / 数组, 双指针, 动态规划, 栈

## 原题 / Original Problem
Given `n` non-negative integers representing an elevation map where the width of each bar is `1`, compute how much water it can trap after raining.

给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算下雨之后能接多少雨水。

**示例**: height = [0,1,0,2,1,0,1,3,2,1,2,1] → 6

## 代码 / Code
```java
class Solution {
    public int trap(int[] height) {
        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0, water = 0;
        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) leftMax = height[left];
                else water += leftMax - height[left];
                left++;
            } else {
                if (height[right] >= rightMax) rightMax = height[right];
                else water += rightMax - height[right];
                right--;
            }
        }
        return water;
    }
}
```

## 核心公式
每个位置积水量 = min(左边最高, 右边最高) - 当前高度

## 双指针逻辑
维护 leftMax 和 rightMax，哪边矮处理哪边。当前高度 < 那侧的 max → 积水；否则更新 max。

## 为什么只用一侧 max？
当 height[left] < height[right] 时，right 那边至少有一个 ≥ height[right]，所以 leftMax ≤ rightMax。left 的水量完全由 leftMax 决定。

## 和 Container 的区别
Container 求 max(宽×高)，这题求 sum(每列积水量)。

## 关键词触发
"接雨水" / "柱子存水" → 双指针 / DP 预处理 / 单调栈

4. 15. 3Sum / 三数之和
**难度**: Medium / 中等 | **标签**: Array, Two Pointers, Sorting / 数组, 双指针, 排序

## 原题 / Original Problem
Given an integer array `nums`, return all the triplets `[nums[i], nums[j], nums[k]]` such that `i != j != k` and `nums[i] + nums[j] + nums[k] == 0`. The solution set must not contain duplicate triplets.

给你一个整数数组 nums，返回所有和为 0 且不重复的三元组。

**示例**: [-1,0,1,2,-1,-4] → [[-1,-1,2],[-1,0,1]]

## 代码 / Code
```java
class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;  // 固定数去重
            int left = i + 1, right = nums.length - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    left++; right--;
                    while (left < right && nums[left] == nums[left - 1]) left++;   // 左去重
                    while (left < right && nums[right] == nums[right + 1]) right--; // 右去重
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return result;
    }
}
```

## 过程追踪 / Walkthrough
```
nums=[-4,-1,-1,0,1,2]（排序后）

i=0(-4): 找两数和=4 → -1+2=1, 0+2=2... 没有 → 结束
i=1(-1): 找两数和=1 → left=2(-1), right=5(2): -1+2=1 → [-1,-1,2] ✓
          left++, right-- → left=3(0), right=4(1): 0+1=1 → [-1,0,1] ✓
i=2(-1): 和 i=1 相同 → 跳过
i=3(0): left=4(1), right=5(2): 1+2=3 ≠ 0 → 结束

结果: [[-1,-1,2],[-1,0,1]] ✅
```

## 核心思路：固定一个 + 双指针
1. 排序（让双指针有意义）
2. 固定 i → 在 [i+1, n-1] 用双指针找两数和 = -nums[i]
3. 两处去重：固定数跳过相同值，找到一组后指针跳过重复值

## 易错点 / Pitfalls
- 双指针要 while 循环，不能只判断一次
- 加进结果的是数值 `Arrays.asList(nums[i], nums[left], nums[right])` 不是下标
- `result.add` 一次只能加一个三元组
- 忘了排序就双指针失效

## 关键词触发 / Triggers
"三数之和" / "和为0的三元组" / "不重复" → 排序 + 固定一个 + 双指针 + 去重

# 滑动窗口 / Sliding Window

1. 3. Longest Substring Without Repeating Characters / 无重复字符的最长子串
**难度**: Medium / 中等 | **标签**: Hash Table, String, Sliding Window / 哈希表, 字符串, 滑动窗口

## 原题 / Original Problem
Given a string s, find the length of the longest substring without repeating characters.

给定一个字符串 s，请你找出其中不含有重复字符的最长子串的长度。

**示例**: s = "abcabcbb" → 3（"abc"）；s = "bbbbb" → 1（"b"）；s = "pwwkew" → 3（"wke"）

## 代码 / Code
```java
class Solution {
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int left = 0, maxLen = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (map.containsKey(c)) {
                left = Math.max(left, map.get(c) + 1);
            }
            map.put(c, right);
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }
}
```

## 核心思路 / Core Idea
right 扩张窗口，每次走一步。遇到重复字符时，left 跳到重复位置的下一个。用 HashMap 记录每个字符最后出现的位置。Math.max 防止 left 回退（关键！）。窗口始终无重复，记录过程中的最大长度。

## 易错点 / Pitfalls
- "abba" 场景：left 只能往右走，不能直接用 map 的值赋值，必须 Math.max(left, map.get(c) + 1)
- 子串必须是连续的，子序列不行
- 窗口长度 = right - left + 1

## 关键词触发 / Triggers
"最长子串" / "无重复" / "连续" → 滑动窗口 + HashMap

2. 438. Find All Anagrams in a String / 找到字符串中所有字母异位词
**难度**: Medium / 中等 | **标签**: Hash Table, String, Sliding Window / 哈希表, 字符串, 滑动窗口

## 原题 / Original Problem
Given two strings s and p, return an array of all the start indices of p's anagrams in s.

给定两个字符串 s 和 p，找到 s 中所有 p 的异位词的子串，返回起始索引。

**示例**: s = "cbaebabacd", p = "abc" → [0,6]；s = "abab", p = "ab" → [0,1,2]

## 代码 / Code
```java
class Solution {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        if (s.length() < p.length()) return result;
        int[] pCount = new int[26], windowCount = new int[26];
        for (char c : p.toCharArray()) pCount[c - 'a']++;
        for (int i = 0; i < p.length(); i++) windowCount[s.charAt(i) - 'a']++;
        if (Arrays.equals(pCount, windowCount)) result.add(0);
        for (int i = p.length(); i < s.length(); i++) {
            windowCount[s.charAt(i) - 'a']++;
            windowCount[s.charAt(i - p.length()) - 'a']--;
            if (Arrays.equals(pCount, windowCount)) result.add(i - p.length() + 1);
        }
        return result;
    }
}
```

## 核心思路 / Core Idea
定长滑动窗口：窗口大小 = p.length()，锁定不变。维护两个 int[26] 计数数组 —— pCount（固定）和 windowCount（滑动更新）。每次滑动只更新进出一个字符，Arrays.equals 比较两个数组是否相同。时间复杂度 O(n)。

## 和 Group Anagrams 的联系
都用 int[26] 计数判断异位词。Group Anagrams 用排序或计数拼成字符串当 HashMap key，这题直接用 Arrays.equals 比较两个计数数组。

## 起始下标公式
i - p.length() + 1（i 是当前窗口右边界下标）

## 易错点 / Pitfalls
- s.length() < p.length() 直接返回空列表
- 初始化第一个窗口后必须检查一次（容易忘）
- 起始下标要算对：i - p.length() + 1

## 关键词触发 / Triggers
"字母异位词子串" / "找所有起始索引" / "定长窗口" → int[26] 计数 + 滑动窗口

# 每日刷题 / Daily Practice

1. 1979. Find Greatest Common Divisor of Array / 找出数组的最大公约数
**难度**: Easy / 简单 | **标签**: Array, Math / 数组, 数学

## 原题 / Original Problem
Given an integer array `nums`, return the greatest common divisor of the smallest number and largest number in `nums`.

给你一个整数数组 nums，返回数组中最大数和最小数的最大公约数。

**示例**: nums = [2,5,6,9,10] → 2；nums = [7,5,6,8,3] → 1

## 代码 / Code
```java
class Solution {
    public int findGCD(int[] nums) {
        int min = nums[0], max = nums[0];
        for (int num : nums) {
            if (num < min) min = num;
            if (num > max) max = num;
        }
        return gcd(max, min);
    }
    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}
```

## 两步走
1. 遍历找 min 和 max
2. 辗转相除法求 gcd(max, min)

## 辗转相除法（欧几里得算法）
递归版：
```java
int gcd(int a, int b) {
    return b == 0 ? a : gcd(b, a % b);
}
```
循环版（推荐，不怕栈溢出）：
```java
int gcd(int a, int b) {
    while (b != 0) {
        int temp = b;
        b = a % b;
        a = temp;
    }
    return a;
}
```

2. 316. Remove Duplicate Letters / 去除重复字母
**难度**: Medium / 中等 | **标签**: Stack, Greedy, String, Monotonic Stack / 栈, 贪心, 字符串, 单调栈

## 原题 / Original Problem
Given a string `s`, remove duplicate letters so that every letter appears once and only once. You must make sure your result is the smallest in lexicographical order among all possible results.

给你一个字符串 s，请你去除字符串中重复的字母，使得每个字母只出现一次。需保证返回结果的字典序最小。

**示例**: s = "bcabc" → "abc"；s = "cbacdcbc" → "acdb"

## 代码 / Code
```java
class Solution {
    public String removeDuplicateLetters(String s) {
        int[] lastIndex = new int[26];
        for (int i = 0; i < s.length(); i++) lastIndex[s.charAt(i) - 'a'] = i;
        boolean[] inStack = new boolean[26];
        Deque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (inStack[c - 'a']) continue;
            while (!stack.isEmpty() && stack.peek() > c && lastIndex[stack.peek() - 'a'] > i) {
                inStack[stack.pop() - 'a'] = false;
            }
            stack.push(c);
            inStack[c - 'a'] = true;
        }
        StringBuilder sb = new StringBuilder();
        Iterator<Character> it = stack.descendingIterator();
        while (it.hasNext()) sb.append(it.next());
        return sb.toString();
    }
}
```

## 核心思路：单调栈（贪心）
遍历每个字符，维护一个字典序递增的栈：
- 字符已在栈中 → 跳过
- 栈顶 > 当前字符，且栈顶在后面还会出现 → 弹掉栈顶
- 当前字符入栈

## 三个辅助数组
- `lastIndex[26]`：每个字符最后出现的位置（判断"后面还有吗"）
- `inStack[26]`：字符是否已在栈中（去重）
- `stack`：单调递增栈

## 弹栈条件（缺一不可）
1. 栈非空
2. `stack.peek() > 当前字符`（字典序可以更优）
3. `lastIndex[栈顶] > 当前位置`（栈顶字符后面还会出现）

如果栈顶字符在后面不会出现了，即使它比当前字符大也不能弹。

## ⚠️ 大坑：Deque 遍历方向
Java 的 `ArrayDeque` 用 `for-each` 遍历是从栈顶到栈底（头部到尾部）：
- push → 加在头部 → for-each 头部先出 → 结果反了！
```java
// ❌ 错误：结果逆序
for (char c : stack) { result.append(c); }

// ✅ 正确：从栈底到栈顶
Iterator<Character> it = stack.descendingIterator();
while (it.hasNext()) { result.append(it.next()); }
```

3. 1260. Shift 2D Grid / 二维网格迁移
**难度**: Easy / 简单 | **标签**: Array, Matrix, Simulation / 数组, 矩阵, 模拟

## 原题 / Original Problem
Given a 2D `grid` of size `m x n` and an integer `k`. You need to shift the grid `k` times. In one shift operation: Element at `grid[i][j]` moves to `grid[i][j+1]`. Element at `grid[i][n-1]` moves to `grid[i+1][0]`. Element at `grid[m-1][n-1]` moves to `grid[0][0]`. Return the 2D grid after `k` shifts.

给你一个 m 行 n 列的二维网格 grid 和一个整数 k。你需要将 grid 迁移 k 次，返回最终网格。

**示例**: grid = [[1,2,3],[4,5,6],[7,8,9]], k = 1 → [[9,1,2],[3,4,5],[6,7,8]]

## 代码 / Code
```java
class Solution {
    public List<List<Integer>> shiftGrid(int[][] grid, int k) {
        int m = grid.length, n = grid[0].length, total = m * n;
        k %= total;
        int[][] newGrid = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int idx = i * n + j;
                int newIdx = (idx + k) % total;
                newGrid[newIdx / n][newIdx % n] = grid[i][j];
            }
        }
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < n; j++) row.add(newGrid[i][j]);
            result.add(row);
        }
        return result;
    }
}
```

## 核心思路：一维化 + 取模
把二维网格展平成一维数组，每个元素右移 k 步，再折叠回二维。
真正移动 = k % total（剔除完整循环，只保留有效步数）。

## 三个公式
- 二维 → 一维：`idx = i * n + j`
- 右移 k 步：`newIdx = (idx + k) % total`
- 一维 → 二维：`newIdx / n` 得行，`newIdx % n` 得列

## 取模的直觉：钟表
钟表 12 点：11 点 + 2 小时 = 13 点 → 13 % 12 = 1 点。
一维数组：位置 8 + 1 步 = 9 → 9 % 9 = 0，绕回开头。
取模 = 走到头就从头继续走，形成循环。

## 为什么不是模拟 k 次？
k 可能非常大，模拟 k 次 O(k·m·n) 会超时。
`k % total` 只做有效移动，一次遍历 O(m·n)。

## 易错点
- k 要先对 total 取余
- 返回类型是 List<List<Integer>>，需要手动转换

4. 3499. Maximize Active Section with Trade I
**难度**: Medium / 中等 | **标签**: String, Greedy / 字符串, 贪心

## 原题 / Original Problem
You are given a binary string `s`. You can perform at most one operation to maximize the number of active sections. Choose a contiguous block of '1's surrounded by '0's → turn it to '0's → then choose a contiguous block of '0's surrounded by '1's → turn it to '1's. Return the maximum possible number of active sections.

给你一个二进制字符串 s，最多执行一次操作：选一个被 0 包围的连续 1 区块变全 0，然后将一个被 1 包围的连续 0 区块变全 1。求最大活跃区段数。

## 代码 / Code
```java
class Solution {
    public int maxActiveSectionsAfterTrade(String s) {
        int ones = 0, prevZeros = -1, curZeros = 0, maxGain = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                curZeros++;
            } else {
                ones++;
                if (curZeros > 0) {
                    if (prevZeros != -1) maxGain = Math.max(maxGain, prevZeros + curZeros);
                    prevZeros = curZeros;
                    curZeros = 0;
                }
            }
        }
        if (curZeros > 0 && prevZeros != -1) maxGain = Math.max(maxGain, prevZeros + curZeros);
        return ones + maxGain;
    }
}
```

## 操作本质（人话）
在 s 两端包上 '1' 得到 t = '1' + s + '1'：
- 选一个被 0 夹在中间的 1-区块（左右都有 0）
- 第一步：这块 1 变全 0 → 三块 0 连成一片
- 第二步：这片更大的 0 被两边 1 夹住 → 全部翻身变 1
- 净赚 = 左边 0 的个数 + 右边 0 的个数

## 解法一：游程法（直观）
把 t 切成连续相同字符的段：

t = "1101101" → runs = [2个1, 1个0, 2个1, 1个0, 1个1]

对每个内部的 1-段（下标 2, 4...，排除首尾）：
  收益 = 左边 0-段长度 + 右边 0-段长度

时间 O(n)，空间 O(n)

## 解法二：一次遍历 O(n)/O(1)（最优）
不关心 1 区块本身，只关心每段 0 的长度。三个变量接力：

previousZeros = -1 ← 上一段 0 的长度，-1 表示"还不存在" currentZeros = 0 ← 当前正在数的这段 0 的长度


遍历规则：
- 遇到 `0`：currentZeros++
- 遇到 `1`：
  - ones++
  - 如果 currentZeros > 0：
    - 如果 previousZeros != -1 → 中间的 1 区块左右都有 0！收益 = prev + cur
    - 交接：prev = cur，cur = 0
- 循环结束后额外处理末尾（cur>0 && prev!=-1 → 再算一次收益）

为什么不用管 1 区块是单个还是连续？因为 1 连续出现时 cur=0 不触发交接，
previousZeros 保持不变，本质上把连续的 1 视为一个整体区块。

## 易错点
- 必须是**被 0 包围**的 1，光一边有 0 不算（prevZeros = -1 时不算）
- 循环结束后要额外处理末尾的 0 段
- 只做一次操作，取最大收益，不是累加所有

5. 3500. Maximize Active Section with Trade II
**难度**: Hard / 困难 | **标签**: String, Binary Search, Sparse Table, Prefix Sum / 字符串, 二分, 稀疏表, 前缀和

## 原题 / Original Problem
Same as 3499, plus a 2D array `queries` where `queries[i] = [li, ri]` represents a substring. For each query, determine the max possible active sections after performing the operation on only that substring.

同上一题，外加 queries 数组表示多个查询，每个查询对 s[li..ri] 子串独立操作，返回整个 s 的活跃区段数。

## 代码 / Code
```java
class Solution {
    private int[][] sparseTable; private int[] log2;
    public List<Integer> maxActiveSectionsAfterTrade(String s, int[][] queries) {
        int n = s.length(), totalOnes = 0;
        int[] zeroRunId = new int[n]; Arrays.fill(zeroRunId, -1);
        int[] start = new int[n], end = new int[n], length = new int[n];
        int runCount = 0;
        for (int i = 0; i < n; ) {
            if (s.charAt(i) == '1') { totalOnes++; i++; continue; }
            int left = i;
            while (i < n && s.charAt(i) == '0') { zeroRunId[i] = runCount; i++; }
            start[runCount] = left; end[runCount] = i - 1; length[runCount] = i - left;
            runCount++;
        }
        int[] nextZeroRun = new int[n+1]; nextZeroRun[n] = -1;
        for (int i = n-1; i >= 0; i--) nextZeroRun[i] = zeroRunId[i] != -1 ? zeroRunId[i] : nextZeroRun[i+1];
        int[] prevZeroRun = new int[n]; int prev = -1;
        for (int i = 0; i < n; i++) { if (zeroRunId[i] != -1) prev = zeroRunId[i]; prevZeroRun[i] = prev; }
        int pairCount = Math.max(0, runCount-1);
        int[] pairGain = new int[pairCount];
        for (int i = 0; i < pairCount; i++) pairGain[i] = length[i] + length[i+1];
        buildSparseTable(pairGain);
        List<Integer> answer = new ArrayList<>();
        for (int[] q : queries) {
            int l = q[0], r = q[1];
            int firstRun = nextZeroRun[l], lastRun = prevZeroRun[r];
            if (firstRun == -1 || lastRun == -1 || firstRun >= lastRun) { answer.add(totalOnes); continue; }
            int maxGain = 0;
            maxGain = Math.max(maxGain, clippedLength(firstRun,l,r,start,end)+clippedLength(firstRun+1,l,r,start,end));
            maxGain = Math.max(maxGain, clippedLength(lastRun-1,l,r,start,end)+clippedLength(lastRun,l,r,start,end));
            int ml = firstRun+1, mr = lastRun-2;
            if (ml <= mr) maxGain = Math.max(maxGain, rangeMax(ml, mr));
            answer.add(totalOnes + maxGain);
        }
        return answer;
    }
    private int clippedLength(int run, int ql, int qr, int[] st, int[] ed) {
        return Math.max(0, Math.min(ed[run], qr) - Math.max(st[run], ql) + 1);
    }
    private void buildSparseTable(int[] v) { /* 同上，Sparse Table 预处理见完整版 */ }
    private int rangeMax(int l, int r) { /* 同上，Sparse Table 预处理见完整版 */ }
}
```

## 和上一题的区别
上一题：整个 s 做一次操作。
这题：给你很多 query [l, r]，每个 query 独立——只在子串 s[l..r] 内操作，但结果统计整个 s 的 1 的个数。

## 核心公式
答案 = 整个 s 的 1 的总数 + 子串内操作的最大收益
totalOnes 固定不变，收益在子串内算（逻辑同上一题）。

## 预处理：把 0-段信息全部提取
扫描一次 s，记录每个 0-段的 start、end、length，以及：
- zeroRunId[i]：位置 i 属于哪个 0-段（是 1 则 -1）
- nextZeroRun[i]：位置 i 及其右边第一个 0-段编号
- previousZeroRun[i]：位置 i 及其左边最后一个 0-段编号

## pairGain：相邻两段 0 之间的收益提前算
pairGain[i] = length[i] + length[i+1]
代表第 i 和第 i+1 个 0-段之间的 1-段，操作后能增加的活跃数。

## 单个 query 的三种候选（取最大）
设区间内第一个 0-段 = firstRun，最后一个 = lastRun：

1. **左边界截断**：firstRun 和 firstRun+1 之间的 1-段
   两个 0-段都可能被 query 边界截断 → clippedLength 手动算
2. **右边界截断**：lastRun-1 和 lastRun 之间的 1-段
   同理 clippedLength 手动算
3. **中间完整**：firstRun+1 到 lastRun-2 之间的 pairGain
   两个 0-段都完全在区间内，收益 = pairGain[i] 不变
   用 Sparse Table  O(1) 查最大值

## Sparse Table（稀疏表）— RMQ
提前打表存所有区间长度的最大值。查询任意区间时用两段拼：
取最大的 2^level ≤ 区间长度，用前后两段覆盖整个区间，O(1)。

## 复杂度
预处理 O(n log n)，每个 query O(1)，总 O(n log n + q)。

## 易错点
- 返回类型是 int[] 不是 List<Integer>
- 0-段 < 2 个时无法操作，收益 = 0
- clippedLength 要考虑 run 越界情况
- 左右边界可能是同一个 pair（firstRun+1 == lastRun），中间部分为空

6. 3513. XOR of Triplets / 异或三元组 ❌ 做错过
**难度**: Medium | **标签**: Math, Bit Manipulation / 数学, 位运算

## 原题 / Original Problem
Given an integer array `nums` of length `n`, where `nums` is a permutation of the integers in the range `[1, n]`.

An XOR triplet is defined as the XOR of three elements `nums[i] XOR nums[j] XOR nums[k]`, where `i <= j <= k`.

Return the number of **distinct** XOR triplet values from all possible triplets `(i, j, k)`.

给你一个长度为 n 的整数数组 nums，其中 nums 是范围 [1, n] 内所有数的排列。返回所有可能三元组 (i, j, k) 中不同的 XOR 值的数量，其中 i <= j <= k。

**示例**: nums = [3,1,2] → 输出 4（XOR 值 {0, 1, 2, 3}）

## 代码 / Code
```java
class Solution {
    public int xorTriplets(int[] nums) {
        int n = nums.length;
        if (n <= 2) return n;  // n=1 → 1, n=2 → 2（下标不够选三个不同的）

        // 找大于 n 的最小 2 的幂：从 1 开始不断翻倍直到超过 n
        int ans = 1;
        while (ans <= n) {
            ans <<= 1;
        }
        return ans;
    }
}
```

## 核心思路 / Core Idea
- n ≤ 2：下标不够选三个不同的 i<j<k，答案就是 n
- n ≥ 3：答案 = 大于 n 的最小 2 的幂（即 2^⌈log₂n⌉）
  - 上限：所有数 < 2^bits，XOR 结果 < 2^bits
  - 下限：n≥3 时有足够下标组合产生 [0, 2^bits-1] 的所有值

| n | ans | 原因 |
|---|:---:|---|
| 1 | 1 | 只有一个下标 |
| 2 | 2 | 只有两个下标 |
| 3~4 | 4 | 2²=4 |
| 5~8 | 8 | 2³=8 |
| 9~16 | 16 | 2⁴=16 |

## 易错点 / Pitfalls
- 不是遍历所有三元组！O(n³) 必超时，要找数学规律
- n=2 时答案也是 2（不能选三个不同下标，和 n=1 同理但结果不同）
- `ans <<= 1` 左移等价于 ×2，一直翻倍直到 > n

## 关键词触发 / Triggers
"排列" / "XOR 不同值" / "i≤j≤k 三元组" → 数学规律，答案为 2 的幂

7. 3513-II. XOR of Triplets II / 异或三元组 II ❌ 做错过
**难度**: Medium / 中等 | **标签**: Math, Bit Manipulation, Enumeration / 数学, 位运算, 枚举

## 原题 / Original Problem
Given an integer array `nums`, return the number of distinct XOR triplet values `nums[i] ^ nums[j] ^ nums[k]` where `i <= j <= k`.

给你一个整数数组 nums，返回所有 i≤j≤k 的三元组 XOR 值中不同值的数量。nums 是任意数组，n ≤ 1500。

**与 3513-I 的区别**: I 是 [1,n] 排列用数学规律；II 是任意数组用枚举。

**示例**: nums = [1,3] → 2；nums = [6,7,8,9] → 4

## 代码 / Code
```java
class Solution {
    public int xorTriplets(int[] nums) {
        int n = nums.length;
        boolean[] pair = new boolean[2048];    // 下标 = XOR 值
        // ① 枚举所有 i≤j，打出所有两两 XOR
        for (int i = 0; i < n; i++)
            for (int j = i; j < n; j++)
                pair[nums[i] ^ nums[j]] = true;
        // ② 每个两两 XOR 再 XOR 每个 nums[k]
        boolean[] triple = new boolean[2048];
        for (int k = 0; k < n; k++)
            for (int v = 0; v < 2048; v++)
                if (pair[v]) triple[v ^ nums[k]] = true;
        // ③ 数多少种
        int count = 0;
        for (boolean b : triple) if (b) count++;
        return count;
    }
}
```

## 过程追踪 / Walkthrough
nums = [1, 3]

① 两两 XOR（i ≤ j）：
  (0,0): 1^1 = 0  →  pair[0] = true
  (0,1): 1^3 = 2  →  pair[2] = true
  (1,1): 3^3 = 0  →  pair[0] = true
  pair = {0, 2} 两个 true

② 每个 pair 值 × 每个 nums[k]：
  k=0 (nums[0]=1) → 所有 pair 值 XOR 1
    pair[0]=true: 0^1 = 1 → triple[1] = true
    pair[2]=true: 2^1 = 3 → triple[3] = true
  k=1 (nums[1]=3) → 所有 pair 值 XOR 3
    pair[0]=true: 0^3 = 3 → triple[3] = true
    pair[2]=true: 2^3 = 1 → triple[1] = true
  triple = {1, 3} 两个 true

③ count = 2 ✅

## 为什么数组大小是 2048？
nums[i] ≤ 1500，1500 二进制占 11 位。
两个 11 位数 XOR 结果也 ≤ 2047。下一个 2 的幂 = 2048 = 2¹¹。
用布尔数组当下标，比 HashSet 快。

## 易错点 / Pitfalls
- 别和 3513-I 搞混：I 是排列用数学，II 是任意数组用枚举
- pair 和 triple 用 boolean[2048] 不用 HashSet（值域小，数组比 HashMap O(1) 常数更小）
- i≤j≤k 的限制不影响结果（XOR 有交换律，任意三个可以排成有序的）

8. 628. Maximum Product of Two Digits / 任意两位数字的最大乘积 ❌ 做错过
**难度**: Easy / 简单 | **标签**: Math, String / 数学, 字符串

## 原题 / Original Problem
Given a positive integer `n`, return the maximum product of any two digits in `n`. A digit may be used multiple times if it appears multiple times.

给定一个正整数 n，返回任意两位数字相乘所得的最大乘积。如果某个数字出现多次，可以多次使用。

**示例**: n=124 → 4×2=8；n=22 → 2×2=4；n=31 → 3×1=3

## 代码 / Code
```java
class Solution {
    public int maxProduct(int n) {
        String s = Integer.toString(n);
        int max1 = 0, max2 = 0;
        for (int i = 0; i < s.length(); i++) {
            int digit = s.charAt(i) - '0';  // '4' → 4
            if (digit > max1) {
                max2 = max1;   // 老大哥变老二
                max1 = digit;  // 新大哥上位
            } else if (digit > max2) {
                max2 = digit;
            }
        }
        return max1 * max2;
    }
}
```

## 过程追踪 / Walkthrough
```
n = 124 → s = "124"

i=0: digit=1, 1>max1(0) → max2=0, max1=1  →  max1=1, max2=0
i=1: digit=2, 2>max1(1) → max2=1, max1=2  →  max1=2, max2=1
i=2: digit=4, 4>max1(2) → max2=2, max1=4  →  max1=4, max2=2

return 4×2 = 8 ✅
```

## 易错点 / Pitfalls
- int 没有 `.length`！必须先 `Integer.toString(n)` 转字符串
- max1、max2 要在 for 外面声明，不然循环结束就没了
- `s.charAt(i) - '0'` 把字符转成数字（'4' 的 ASCII 减 '0' 的 ASCII = 4）

## 关键词触发 / Triggers
"最大乘积" / "找两个最大数字" → 一次遍历跟踪 max1 和 max2

9. 628. Maximum Product of Three Numbers / 三个数的最大乘积
**难度**: Easy / 简单 | **标签**: Array, Math, Sorting / 数组, 数学, 排序

## 原题 / Original Problem
Given an integer array `nums`, find three numbers whose product is maximum and return the maximum product.

给你一个整型数组 nums，在数组中找出由三个数组成的最大乘积，并输出这个乘积。

**示例**: [1,2,3] → 6；[1,2,3,4] → 24；[-1,-2,-3] → -6

## 代码 / Code
```java
class Solution {
    public int maximumProduct(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        int a = nums[n-1] * nums[n-2] * nums[n-3]; // 最大三个
        int b = nums[0] * nums[1] * nums[n-1];      // 最小两个×最大（负负得正）
        return Math.max(a, b);
    }
}
```

## 过程追踪 / Walkthrough
```
例1: nums = [-10, -5, 1, 2, 3]
排序: [-10, -5, 1, 2, 3]

候选1(最大三个): 3×2×1 = 6
候选2(最小两个×最大): -10×-5×3 = 150  ← 两个负数相乘变正数，炸了！

max(6, 150) = 150 ✅

例2: nums = [-5, -4, -3, -2]  全负数
候选1: -2×-3×-4 = -24
候选2: -5×-4×-2 = -40
max(-24, -40) = -24 ✅  越接近0越大
```

## 核心陷阱
两个负数相乘变正数 → 最小两个负数 × 最大正数，可能秒杀最大的三个正数。

## 关键词触发 / Triggers
"三个数最大乘积" / "最大乘积" → 排序 + 比较两种候选

10. 3014. Minimum Number of Pushes to Type Word I / 输入单词需要的最少按键次数 I
**难度**: Easy / 简单 | **标签**: Math, String, Greedy, Counting / 数学, 字符串, 贪心, 计数

## 原题 / Original Problem
You are given a string `word` containing lowercase English letters. Telephone keypads have keys mapped to distinct collections of letters. You are allowed to remap the keys numbered 2 to 9 to distinct letters. Return the **minimum** number of pushes needed to type `word` after remapping the keys.

给你一个由小写英文字母组成的字符串 word。可以将 2-9 号按键重新映射到字母集合。每个按键可映射任意数量字母，每个字母恰好映射到一个按键。返回输入 word 所需的最少按键次数。

**示例**: "abcde" → 5（5个字母各占一个按键第一位，各按1次）
"aabbccddeeffgghhiiiiii" → 24

## 代码 / Code
```java
class Solution {
    public int minimumPushes(String word) {
        int[] freq = new int[26];
        for (char c : word.toCharArray()) freq[c - 'a']++;
        Arrays.sort(freq);  // 升序，最大值在最后
        int total = 0;
        for (int i = 0; i < 26; i++) {
            int f = freq[25 - i];  // 从大到小取
            if (f == 0) break;
            total += f * (i / 8 + 1);  // 频次 × 按键层数
        }
        return total;
    }
}
```

## 过程追踪 / Walkthrough
```
word = "aabbccddeeffgghhiiiiii"

① 统计: i:6, a:2, b:2, c:2, d:2, e:2, f:2, g:2, h:2
② sort 升序: [0..0, 2,2,2,2,2,2,2,2, 6]  ← 3个0, 8个2, 1个6
③ 从后往前取(降序): [6, 2, 2, 2, 2, 2, 2, 2, 2]

   i=0: f=6,  i/8+1=1 → 6×1 = 6
   i=1~8: f=2, i/8+1=1 → 2×1×8 = 16
   i=9: f=2, i/8+1=2 → 2×2 = 4   ← 第9个字母在第2层，按2次

总: 6+16+4 = 24 ✅
```

## 核心思路
8 个按键，每个按键可以有第1位、第2位、第3位...
- 频次最高的 8 个字母 → 放第1位（按1次）
- 次高 8 个 → 放第2位（按2次）
- 以此类推
`i / 8 + 1` 就是当前字母在第几层（点几下）

## 易错点 / Pitfalls
- Arrays.sort 是升序，最大值在末尾 → `freq[25-i]` 从后往前取才算降序
- 用 `int[26]` 而不用 HashMap（26个字母固定，数组更快）

## 关键词触发 / Triggers
"最少按键" / "电话键盘" / "重新映射" → 统计频次 + 排序 + 层数分配

11. 486. Predict the Winner / 预测赢家
**难度**: Medium / 中等 | **标签**: Array, Math, DP, Game Theory / 数组, 数学, 动态规划, 博弈

## 原题 / Original Problem
You are given an integer array `nums`. Two players are playing a game with this array: player 1 and player 2. Players take turns, where each player takes a number from either end of the array. The game ends when there are no elements remaining. Return `true` if Player 1 can win. If the scores are equal, Player 1 is still the winner.

给你一个整数数组 nums。玩家 1 和玩家 2 轮流从数组任意一端取数字，加到自己的分数上。如果玩家 1 能成为赢家（平局也算），返回 true。

**示例**: [1,5,2] → false；[1,5,233,7] → true

## 核心视角
不关心"玩家1得分"和"玩家2得分"两个数。
只关心：**当前先手能比后手多拿多少分（净胜分）**。
净胜分 ≥ 0 → 玩家 1 赢。

## 解法一：递归 / Recursion
```java
class Solution {
    private int[] nums;
    public boolean predictTheWinner(int[] nums) {
        this.nums = nums;
        return dfs(0, nums.length - 1) >= 0;
    }
    int dfs(int left, int right) {
        if (left == right) return nums[left];
        int pickL = nums[left] - dfs(left + 1, right);
        int pickR = nums[right] - dfs(left, right - 1);
        return Math.max(pickL, pickR);
    }
}
```

过程: nums=[1,5,2]
```
dfs(0,2): 先手在 [1,5,2]
  选左: 1 - dfs(1,2)
    dfs(1,2): 对方在 [5,2]
      选5: 5 - dfs(2,2)=5-2=3
      选2: 2 - dfs(1,1)=2-5=-3
      → max=3, 对方净胜3
    pickL = 1-3 = -2
  选右: 2 - dfs(0,1) → dfs(0,1) 对方净胜4 → pickR = 2-4 = -2
  dfs(0,2) = -2 < 0 → false
```

## 解法二：DP / 动态规划 O(n²)
```java
class Solution {
    public boolean predictTheWinner(int[] nums) {
        int n = nums.length;
        int[][] dp = new int[n][n];
        // 按区间长度从小到大填表
        for (int len = 1; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                if (i == j) dp[i][j] = nums[i];
                else dp[i][j] = Math.max(
                    nums[i] - dp[i+1][j],
                    nums[j] - dp[i][j-1]
                );
            }
        }
        return dp[0][n-1] >= 0;
    }
}
```
dp[i][j] = 子数组 nums[i..j] 中先手的净胜分。必须按区间长度从小到大填，因为 dp[i][j] 依赖 dp[i+1][j] 和 dp[i][j-1]（都比 [i,j] 短）。

## 易错点 / Pitfalls
- 平局也算赢：`>= 0` 不是 `> 0`
- DP 必须按区间长度从小到大填，`i` 和 `j` 的双层循环顺序不能随便写
- 递归版：`private` 成员变量接收参数 → `java` 方法内不能嵌套方法

## 关键词触发 / Triggers
"轮流取两端" / "预测赢家" / "博弈" → dfs 净胜分 或 DP 区间填表

12. Find Missing Elements / 找缺失元素 ❌ 做错过
**难度**: Easy / 简单 | **标签**: Array, Hash Table / 数组, 哈希表

## 原题 / Original Problem
给你一个整数数组 nums，数组由若干互不相同的整数组成，原本包含某个范围内的所有整数，但可能缺失部分。该范围内最小和最大整数仍存在于 nums 中。返回有序列表包含所有缺失整数。

**示例**: [1,4,2,5] → [3]；[7,8,6,9] → []；[5,1] → [2,3,4]

## 解法一：布尔数组标记（最优，O(n)）
值域 ≤ 100，用 boolean 数组当下标标记：
```java
class Solution {
    public List<Integer> findMissingElements(int[] nums) {
        int min = nums[0], max = nums[0];
        for (int num : nums) {
            if (num < min) min = num;
            if (num > max) max = num;
        }
        boolean[] exist = new boolean[max - min + 1];
        for (int num : nums) exist[num - min] = true;
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < exist.length; i++)
            if (!exist[i]) result.add(i + min);
        return result;
    }
}
```

## 解法二：HashSet（直观）
```java
class Solution {
    public List<Integer> findMissingElements(int[] nums) {
        int min = nums[0], max = nums[0];
        for (int num : nums) {
            if (num < min) min = num;
            if (num > max) max = num;
        }
        Set<Integer> set = new HashSet<>();
        for (int num : nums) set.add(num);
        List<Integer> result = new ArrayList<>();
        for (int i = min; i <= max; i++)
            if (!set.contains(i)) result.add(i);
        return result;
    }
}
```

## `exist[num - min]` 映射原理
```
nums=[1,4,2,5], min=1, max=5
exist 大小 = 5-1+1 = 5

下标:  0    1    2    3    4
代表:  1    2    3    4    5
公式: 下标 = 数字 - min    还原: 数字 = 下标 + min

num=1: exist[1-1]=exist[0]=true
num=4: exist[4-1]=exist[3]=true
遍历 exist: exist[2]=false → 缺失 2+1=3 ✅
```

## 我犯的错 / My Mistakes
- `min = nums[0]` 放在循环里面 → 每次重置
- `exist(nums[j]-min)` 用圆括号 → 数组用中括号 `[]`
- `result.add(exist[z]+min)` → exist[z] 是 true/false，应该是 `z+min`
- `return null` → 没有缺失应返回空列表 `new ArrayList<>()`

## 关键词触发 / Triggers
"缺失元素" / "范围完整" → 布尔数组标记 或 HashSet + 范围遍历
