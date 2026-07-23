数组
# 数组 / Array

# 1. 53. Maximum Subarray / 最大子数组和
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

子串 / Subarray

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
哈希表 / Hash Table
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


双指针问题 / Two Pointers

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
滑动窗口 / Sliding Window

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

每日刷题 / Daily Practice

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
