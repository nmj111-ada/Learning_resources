子串
1.Subarray Sum Equals K（和为 K 的子数组）
**难度**: Medium | **标签**: Array, Hash Table, Prefix Sum

## 题目一句话
数组里有多少个连续子数组，加起来等于 k。

## 先搞懂前缀和
前缀和 = 从数组开头一直加到当前位置的总和。

写成公式：**子数组[i到j]的和 = 前缀和[j] - 前缀和[i-1]**

## 核心推导（初中数学移项）

题目要求子数组和等于 k：

前缀和[j] - 前缀和[i-1] = k

前缀和[j] - k = 前缀和[i-1]


翻译成人话：**走到位置 j，前缀和是 X。去查表——之前有几个位置的前缀和等于 X-k？有几个就找到几个和是 k 的子数组。**

## 为什么 HashMap 的 value 是次数不是下标？

同一个前缀和可能在不同位置出现多次（因为数组有正有负），
每个位置都能和当前 j 凑出一个子数组，所以要数"出现了几次"。

## 为什么一开始要 put(0, 1)？

代表"还没开始时累计是 0"，算一条记录。
当某个前缀和刚好等于 k 时，X - k = 0，需要 0 在表里才能计数。

nums = [3], k = 3 前缀和 = 3，X - k = 3 - 3 = 0 → 表里有 0 → 找到一个子数组 [3]

## 和 Two Sum 的关系

| | Two Sum | 这题 |
|---|---|---|
| 表里存什么 | 数值 | 前缀和 |
| 找什么 | target - 当前数 | 当前前缀和 - k |
| value 是啥 | 下标 | 出现次数 |

套路一样：遍历时去表里找配对，O(1) 查找把 O(n²) 降到 O(n)。

## 易错点
- 先查表再往表里放当前前缀和（否则 k=0 时会把自己算进去，多算一次）
- 别忘了 map.put(0, 1)
哈希表
1.两数之和（简单）
难度: Easy |标签: Array, Hash Table |日期: 2026-07-18
题目一句话
在数组里找两个数，加起来等于 target，返回下标。
解法对比
暴力 O(n²) / O(1)
两层循环，每对都试一遍。
坑：记得写 if 判断，别直接 return ← 我犯过的错
哈希 O(n) / O(n)
complement = target - nums[i]，去 HashMap 里找。
走到一个数，不问"前面谁能和我配对"，而是问"我需要的人之前出现过吗"。
核心代码（Java）
// 哈希法：seen.containsKey(complement) 是关键


2. Group Anagrams（字母异位词分组）
**难度**: Medium | **标签**: Array, Hash Table, String, Sorting

## 题目一句话
把字母组成完全相同的单词分到同一组。

## 什么是字母异位词
字母种类和个数完全相同，仅是排列顺序不同。
"eat" 和 "tea" → 都是 a:1, e:1, t:1

## 核心思路
给每个单词生成一个"签名"，异位词的签名一定相同。
签名 → HashMap 的 key → 签名相同的单词自动归到同一组。

## 解法一：排序法 O(n·k log k)
每个单词的字符数组排序 → 排完后异位词完全相同 → 作为 key 放入 HashMap。

### 关键 API
- `s.toCharArray()` — String 不可变，拆成 char[] 才能排序
- `Arrays.sort(chars)` — 原地排序
- `new String(chars)` — char[] 拼回 String

## 解法二：计数法 O(n·k) ⭐ 加分
不排序，统计 26 个字母各出现几次，拼成签名 "#1#0#0#0#1#..."。

### 为什么不用排序？
排序是 k log k，统计计数只需遍历一遍单词 k。
k 很大时，计数法明显更快。

### 关键点
- `c - 'a'`：字符相减 = ASCII 差值，'e' - 'a' = 4 → count[4]++
- 用 `#` 分隔每个数字，防止拼接歧义：#1#12 ≠ #11#2

## 易错点
- HashMap 的 key 第一次出现时要先 new ArrayList<>()
- 最后返回 `new ArrayList<>(map.values())`，别直接返回 map.values()

## 关键词触发
看到 "分组" / "归类" / "字母异位词" → HashMap + 签名
和 Two Sum 一样：找同类项 → HashMap


2.双指针问题
1.Move Zeroes（移动零）
**难度**: Easy | **标签**: Array, Two Pointers | **日期**: 2026-07-18

## 题目一句话
把所有 0 挪到数组末尾，非零元素保持原有顺序，原地操作。

## 核心思路：快慢指针
- fast（快指针）：遍历每个元素，找非零
- slow（慢指针）：记录下一个非零元素应该放的位置
- fast 找到非零 → 搬到 slow 位置 → slow++
- 遍历完后 slow 后面全部填 0

## 易错点
- 不能排序！排序会打乱非零元素的相对顺序
- 最后要手动填 0，只搬不填的话数组尾部会有残留旧数据
- 原地操作 ≠ 不用额外变量，用几个 int 指针没问题

## 关键词触发
看到 "原地" / "移动某类元素到末尾" / "保持相对顺序" → 快慢指针

2.Container With Most Water（盛最多水的容器）
**难度**: Medium | **标签**: Array, Two Pointers, Greedy

## 题目一句话
找两条线，和 x 轴围成的容器能装最多的水。

## 面积公式
面积 = (right - left) × min(height[left], height[right])

## 核心思路：左右指针
- left=0, right=n-1，从最大宽度开始
- 每次计算当前面积，更新最大值
- **移动较矮的那根**：因为宽度必缩小，要赌高度变大，矮的那根必须被抛弃
- 直到 left 和 right 相遇

## 易错点
- 高度取 min，不是取哪根线
- 移动的是较矮的那根，不是随便移
- 不要和快慢指针搞混：这题是左右指针

## 关键词触发
看到 "两条线" / "围成容器" / "最大面积" → 左右指针 + 移动较矮边

3.Trapping Rain Water（接雨水）
**难度**: Hard | **标签**: Array, Two Pointers, DP, Stack

## 题目一句话
柱状图里下雨后能积多少水。

## 核心公式
每个位置积水量 = min(左边最高, 右边最高) - 当前高度


## 双指针核心逻辑
- left=0, right=n-1，维护 leftMax 和 rightMax
- height[left] < height[right] → 处理左边
  - height[left] >= leftMax → 更新 leftMax
  - 否则 water += leftMax - height[left]
- 否则处理右边，同理
- 移动较矮的那边（和 Container 一样的直觉）

## 为什么可以只用 leftMax？
当 height[left] < height[right] 时，right 那边至少有一个 ≥ height[right]，
所以 leftMax 一定 ≤ rightMax。left 的水量完全由 leftMax 决定。


## 关键词触发："接雨水" / "柱子存水" → 双指针 / DP 预处理 / 单调栈
3.滑动窗口
 1.Longest Substring Without Repeating Characters
**难度**: Medium | **标签**: Hash Table, String, Sliding Window

## 题目一句话
找最长的不含重复字符的连续子串。

## 核心思路：滑动窗口
- right 扩张窗口，每次走一步
- 遇到重复字符 → left 收缩窗口直到重复字符被踢出
- 窗口始终无重复，记录过程中的最大长度

## 两种实现
### Set 版（直观）
while (window.contains(c)) → 踢 s.charAt(left) → left++
每次 left 只走一步，重复字符很远时效率低

### HashMap 版（优化）
记录每个字符最后一次出现的位置
left = Math.max(left, map.get(c) + 1)  // 直接跳到重复位置的下一个
Math.max 防止 left 回退（关键！）

## 易错点
- "abba" 这种场景：left 只能往右走，不能用 map 的值直接赋值
- 子串必须是连续的，子序列不行
- 窗口长度 = right - left + 1

## 关键词触发
"最长子串" / "无重复" / "连续" → 滑动窗口 + HashSet/HashMap

2. Find All Anagrams in a String
**难度**: Medium | **标签**: Hash Table, String, Sliding Window

## 题目一句话
在 s 中找到所有 p 的字母异位词子串，返回起始下标。

## 核心思路：定长滑动窗口
- 窗口大小 = p.length()，锁定不变
- 维护两个 int[26]：pCount（固定）和 windowCount（滑动更新）
- 每次滑动：新字符进（count++），旧字符出（count--）
- Arrays.equals 比较 → 相同就记录起始下标

## 为什么不用每次重新统计？
每次滑动只涉及 2 个字符变动（一个进一个出），
用数组追踪，比较是 O(26)=O(1)，总复杂度 O(s)。

## 起始下标公式
i - p.length() + 1（i 是当前窗口右边界下标）

## 易错点
- s.length() < p.length() 直接返回空列表
- 初始化第一个窗口后别忘了检查
- 起始下标算对：i - p.length() + 1

## 和 Group Anagrams 的联系
都用 int[26] 计数判断异位词。
Group Anagrams 用排序或计数拼成字符串当 HashMap key，
这题直接用 Arrays.equals 比较两个计数数组。

每日刷题
1.Find Greatest Common Divisor of Array
**难度**: Easy | **标签**: Array, Math

## 题目一句话
找数组最大数和最小数的最大公约数（GCD）。

## 两步走
1. 遍历找 min 和 max
2. 辗转相除法求 gcd(max, min)

## 辗转相除法（欧几里得算法）
递归版：
```java
int gcd(int a, int b) {
    return b == 0 ? a : gcd(b, a % b);
}
循环版（推荐，不怕栈溢出）：

int gcd(int a, int b) {
    while (b != 0) {
        int temp = b;
        b = a % b;
        a = temp;
    }
    return a;
}
2.Remove Duplicate Letters / Smallest Subsequence
**难度**: Medium | **标签**: Stack, Greedy, String, Monotonic Stack

## 题目一句话
删掉重复字符，保留一个，使结果字典序最小，且保持原顺序。

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

3.Shift 2D Grid（二维网格迁移）
**难度**: Easy | **标签**: Array, Matrix, Simulation

## 题目一句话
二维网格所有元素整体右移 k 步，右边界的元素折行到下一行开头，最后一个元素回到 [0][0]。

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

4.Maximize Active Section with Trade I
**难度**: Medium | **标签**: String, Greedy

## 题目一句话
最多执行一次操作：选一个被 0 包围的连续 1 区块，先变全 0，然后它两边连成片的 0 全变 1。求最大 1 的个数。

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
