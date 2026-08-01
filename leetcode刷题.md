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


链表 / Linked List

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


二叉树 / Binary Tree

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
