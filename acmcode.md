# ACM 刷题笔记

> 记录 ACM / 牛客面试算法题的刷题笔记，以及刷题常用的 Java 数据结构 / API。

---

## 一、Java 刷题常用数据结构 / API 速查表

### 总览表

| 数据结构 / 类 | 核心特点 | 常用声明 | 常用操作 | 适合场景 |
|---|---|---|---|---|
| 数组 int[] | 固定长度、连续存储 | `int[] nums = new int[n];` | `nums[i]` | 数组、二分、DP |
| 二维数组 int[][] | 矩阵 | `int[][] matrix = new int[m][n];` | `matrix[i][j]` | 矩阵、网格 DFS |
| ArrayList | 动态数组，随机访问快 | `List<Integer> list = new ArrayList<>();` | `add()`、`get()`、`remove()`、`size()` | 动态数组、结果集、序列模拟 |
| LinkedList | 双向链表 | `LinkedList<Integer> list = new LinkedList<>();` | `addLast()`、`removeLast()`、`addFirst()`、`removeFirst()` | 回溯 path、链表、队列 |
| Stack | 后进先出 LIFO | `Stack<Integer> stack = new Stack<>();` | `push()`、`pop()`、`peek()`、`isEmpty()` | 栈模拟、括号 |
| Queue | 先进先出 FIFO | `Queue<Integer> q = new LinkedList<>();` | `offer()`、`poll()`、`peek()`、`size()`、`isEmpty()` | BFS、队列模拟 |
| Deque | 双端队列，两头都能操作 | `Deque<Integer> dq = new ArrayDeque<>();` | `addFirst()`、`addLast()`、`pollFirst()`、`pollLast()`、`peekFirst()`、`peekLast()` | 单调队列、双端队列、栈 |
| PriorityQueue | 堆，默认小根堆 | `PriorityQueue<Integer> pq = new PriorityQueue<>();` | `offer()`、`poll()`、`peek()` | Top K、堆、MedianFinder |
| HashMap | key-value 映射 | `Map<Integer,Integer> map = new HashMap<>();` | `put()`、`get()`、`getOrDefault()`、`containsKey()`、`remove()` | 计数、映射、前缀和 |
| HashSet | 不重复元素集合 | `Set<Integer> set = new HashSet<>();` | `add()`、`remove()`、`contains()` | 去重、记录访问状态 |
| TreeMap | 有序 key-value | `Map<Integer,Integer> map = new TreeMap<>();` | `put()`、`get()`、`firstKey()`、`lastKey()` | 需要有序映射 |
| TreeSet | 有序且不重复 | `Set<Integer> set = new TreeSet<>();` | `add()`、`remove()`、`contains()` | 有序去重 |
| String | 不可变字符串 | `String s = "abc";` | `charAt()`、`substring()`、`length()`、`equals()` | 字符串题 |
| StringBuilder | 可变字符串 | `StringBuilder sb = new StringBuilder();` | `append()`、`deleteCharAt()`、`toString()` | 回溯、字符串拼接 |
| char[] | 字符数组，可修改 | `char[] chars = s.toCharArray();` | `chars[i]` | 原地修改字符串 |
| TreeNode | LeetCode 二叉树节点 | `TreeNode root` | `root.left`、`root.right`、`root.val` | 二叉树题 |
| Map<Character,Integer> | 字符 → 次数/位置 | `Map<Character,Integer> map` | `put()`、`get()` | 字符统计、字符串 |
| boolean[] | 状态标记 | `boolean[] used = new boolean[n];` | `used[i] = true/false` | N 皇后、访问标记 |
| int[] | 下标/计数/位置记录 | `int[] last = new int[26];` | `last[i]` | 字符最后位置、计数、DP |

### 最重要的几个容器区别

#### ArrayList

```java
List<Integer> list = new ArrayList<>();
```

核心：像**动态数组**。

常用：

```java
list.add(x);                // 末尾添加
list.add(i, x);             // 指定位置插入
list.get(i);                // 下标访问
list.remove(i);             // 删除指定下标
list.remove(list.size()-1); // 删除最后一个
list.size();                // 大小
```

适合：结果集、动态序列、按下标访问。

#### LinkedList

```java
LinkedList<Integer> list = new LinkedList<>();
```

常用：

```java
list.addLast(x);
list.removeLast();
list.addFirst(x);
list.removeFirst();
```

回溯里用：

```java
LinkedList<Integer> path
```

主要因为 `addLast` + `removeLast` 很符合 **选择 → 递归 → 回溯**。但不是必需，ArrayList 也可以。

### Stack 栈

```java
Stack<Integer> stack = new Stack<>();
```

特点：**LIFO**（Last In First Out，后进先出）。

例如 push 1、2、3，栈为：

```text
3 ← 栈顶
2
1
```

操作：

```java
stack.push(4);      // 入栈
stack.pop();        // 删除并返回栈顶
stack.peek();       // 查看栈顶，不删除
stack.isEmpty();    // 是否为空
stack.size();       // 数量
```

注意：`stack.peek()` 不是判断空栈的方法，正确用 `stack.isEmpty()`。

### Queue 队列

```java
Queue<Integer> q = new LinkedList<>();
```

特点：**FIFO**（First In First Out，先进先出）。

例如 offer 10、20、30，队列为：

```text
队首 → 10 20 30 ← 队尾
```

操作：

```java
q.offer(40);   // 入队
q.poll();      // 删除并返回队首
q.peek();      // 查看队首
q.isEmpty();   // 是否为空
q.size();      // 大小
```

记忆：Stack 用 `push / pop / peek`，Queue 用 `offer / poll / peek`。

### Deque 双端队列

```java
Deque<Integer> dq = new ArrayDeque<>();
```

两边都能操作：

```text
First ← [ 1 2 3 ] → Last
```

常用：

```java
dq.addFirst(x);
dq.addLast(x);

dq.pollFirst();
dq.pollLast();

dq.peekFirst();
dq.peekLast();
```

对应：`peek` = 看不删，`poll` = 取出并删除，`First` = 队头，`Last` = 队尾。

滑动窗口最大值就是 Deque + 单调队列，存的是**下标**不是值。

### PriorityQueue 优先队列 / 堆

```java
PriorityQueue<Integer> pq = new PriorityQueue<>();
```

默认**小根堆**，即 `pq.peek()` 永远是最小值。

常用：

```java
pq.offer(x);
pq.poll();
pq.peek();
pq.size();
pq.isEmpty();
```

大根堆：

```java
PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> b - a);
```

MedianFinder 的「左大根堆 + 右小根堆」就是典型应用。

### HashMap

```java
Map<Character, Integer> map = new HashMap<>();
```

核心：key → value。

```java
map.put('a', 10);
map.put('b', 20);
int v = map.get('a');
```

常用：

```java
map.put(key, value);
map.get(key);
map.getOrDefault(key, 0);
map.containsKey(key);
map.remove(key);
```

最常见用途：计数、字符映射、值 → 下标、前缀和 → 出现次数。

例如最小覆盖子串的 `need`：A→1、B→1、C→1。

### HashSet

```java
Set<Character> set = new HashSet<>();
```

只保存**不重复元素**。

常用：

```java
set.add(x);
set.remove(x);
set.contains(x);
set.isEmpty();
set.size();
```

典型用途：去重、记录元素是否出现、访问状态。

### String vs StringBuilder

#### String

```java
String s = "abc";
```

**不可变**。

常用：

```java
s.length();
s.charAt(i);
s.substring(a, b);
s.equals(t);
s.toCharArray();
```

注意：`s.substring(a, b)` 是 `[a, b)`，**右边界不包含**。所以 `s.substring(index, end + 1)` 表示 `s[index ... end]`。

#### StringBuilder

```java
StringBuilder sb = new StringBuilder();
```

**可变字符串**。

常用：

```java
sb.append('a');
sb.deleteCharAt(sb.length() - 1);
sb.toString();
```

最适合回溯，模板：`append → 递归 → delete`。

17 电话号码组合、22 括号生成都用到了这个思想。

### TreeNode 二叉树

LeetCode 给的节点：

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
}
```

使用：`root.val`、`root.left`、`root.right`。

常见：DFS / BFS；前序 根→左→右，中序 左→根→右，后序 左→右→根。

已遇到的题：98 BST验证、230 BST第K小、199 右视图、114 展开为链表、105 前序+中序构造、437 路径总和III、124 最大路径和、236 最近公共祖先。

### 常见 Java 输入输出

#### Scanner

```java
Scanner sc = new Scanner(System.in);

int n = sc.nextInt();          // 整数
String s = sc.next();          // 一个字符串
String line = sc.nextLine();   // 一整行
boolean has = sc.hasNextInt(); // 还有没有数据
```

#### ACM 常见结构

- 不知道多少组，直到 EOF：

```java
while (sc.hasNextInt()) {
    // ...
}
```

- 已知 N 组：

```java
int n = sc.nextInt();
for (int i = 0; i < n; i++) {
    // ...
}
```

- 多组 N，每组里面还有 N 行：`while → for`
- 每行先 M，再读 M 个数字：`while → for → for`

A+B VIII 就是 `while → for → for` 结构。

---

## 二、题目笔记

### A+B I～VI

这几题主要不是算法题，而是在练习 **ACM 输入输出**。

#### 1. A+B I

知识点：

* `Scanner`
* `while (sc.hasNextInt())`
* 读到一对 `a、b` 就计算
* `System.out.println(a + b)`

#### 2. A+B II

知识点：

* 第一层输入 `N`
* `for` 循环处理 `N` 组数据
* 可能存在多组 `N`
* 所以整体是：**`while + for`**

核心结构：

```text
while 还有输入
    读 N
    for N 次
        读 a b
        输出 a+b
```

#### 3. A+B III

知识点：

* 持续读取 `a、b`
* `0 0` 是结束标志
* `break` 结束循环
* `0 0` 本身不计算

触发：`0 0 结束` → **`while + break`**

#### 4. A+B IV

知识点：

* 每行先读 `N`
* 后面有 `N` 个整数
* `for` 循环读取并累加
* `N == 0` 时结束

核心：

```text
读 N
如果 N==0 → break
重复 N 次读取数字并求和
```

#### 5. A+B VII

知识点：

* 输入方式没变化
* 重点是**输出格式**
* `println()` 本身已经换行
* 再输出一次空行即可

例如：

```java
System.out.println(result);
System.out.println();
```

#### 6. A+B VIII

知识点：

* 多组测试数据
* 第一行 `N`：这一组有多少行
* 每行先读 `M`
* 后面有 `M` 个整数
* 求这一行的和
* 每行结果后输出一个空行

核心结构：

```text
while 还有测试组
    读 N
    for N 行
        读 M
        for M 个数
            累加
        输出结果
        输出空行
```

也就是今天最后这题掌握了：

> **`while → for → for` 三层输入结构**

代码：

```java
import java.util.*;

public class Main{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        while(sc.hasNextInt()){
            int n = sc.nextInt();
            
            for(int i = 0;i<n;i++){
                int m = sc.nextInt();
                int result = 0;
                for(int j = 0;j<m;j++){
                    result += sc.nextInt();

                }
                System.out.println(result);
                if (i != n - 1){System.out.println();
                }
            }
        }
    }
}
```

总结知识点：`Scanner`、`hasNextInt()`、`nextInt()`、`while`、`for`、`break`、`多组测试数据`、`按 N/M 控制读取次数`、`ACM 输入输出格式`。

---

### 1. 序列操作

#### 题目 / Problem

维护一个初始为空的整数序列，根据输入的操作指令完成添加、删除、查询、插入、排序以及输出等操作。

#### 核心思路 / Core Idea

使用：

```java
List<Integer> list = new ArrayList<>();
```

根据操作类型 `op` 分支处理：

```text
1 → 末尾添加
2 → 删除末尾
3 → 查询下标
4 → 指定位置插入
5 → 升序排序
6 → 降序排序
7 → 输出长度
8 → 输出整个序列
```

ACM 输入结构：

```text
读取 q
↓
循环 q 次
↓
每次先读取 op
↓
根据 op 决定后面读取几个参数
```

常用 API：

```java
list.add(x);
list.remove(list.size() - 1);
list.get(i);
list.add(i + 1, x);
Collections.sort(list);
list.sort(Collections.reverseOrder());
list.size();
```

输出整个序列：

```java
for (int i = 0; i < list.size(); i++) {
    System.out.print(list.get(i));
    if (i < list.size() - 1) {
        System.out.print(" ");
    }
}
System.out.println();
```

#### 易错点 / Pitfalls

* `List`、`ArrayList`、`Collections` 需要导入 `java.util.*`
* `list.add(i + 1, x)` 表示在下标 `i` 的元素后插入
* 输出元素之间需要空格，最后一个元素后不加空格
* `""` 是空字符串，`" "` 才是一个空格
* `Collections.reverseOrder()` 是方法，需要 `()`
* ACM 模式需要自己处理输入输出

#### 关键词触发 / Triggers

"q 次操作" / "维护一个序列" → **ArrayList + 操作模拟 + switch/if**

#### 标签

`ACM` `模拟` `ArrayList` `输入输出`

---

### 2. 栈操作

#### 题目 / Problem

实现一个初始为空的栈，支持：`push x`、`pop`、`query`、`size`。

栈为空时，`pop` 和 `query` 输出 `Empty`。

#### 核心思路 / Core Idea

使用栈模拟操作：

```java
Stack<Integer> stack = new Stack<>();
```

输入先读取操作字符串：

```java
String op = sc.next();
```

如果是 `push`，再读取一个整数 `x`。其它操作根据当前栈是否为空执行。

常用 API：

```java
stack.push(x);
stack.pop();
stack.peek();
stack.size();
stack.isEmpty();
```

ACM 输入结构：

```text
读取 n
↓
循环 n 次
↓
读取字符串 op
↓
根据 op 执行操作
```

例如：

```text
push 1
push 2
size
query
pop
```

#### 易错点 / Pitfalls

* 字符串命令使用 `sc.next()`
* `push x` 需要继续读取 `x`
* 判断栈空使用 `stack.isEmpty()`
* `!stack.isEmpty()` 表示"栈不为空"
* 不能用 `stack.peek() != null` 判断空栈（空栈执行 `peek()` 会抛异常）
* `Scanner` 在大数据 ACM 题中可能较慢，必要时使用 `BufferedReader`

#### 关键词触发 / Triggers

"push / pop / query / size" → **栈 + isEmpty() + 字符串命令**

#### 标签

`ACM` `栈` `模拟` `Scanner` `输入输出`

---

### 3. 队列操作

#### 题目 / Problem

维护一个空队列，支持：

```text
1 x → 入队
2   → 队头出队
3   → 查询队首
4   → 输出队列大小
```

队列为空时：`2 → ERR_CANNOT_POP`、`3 → ERR_CANNOT_QUERY`。

#### 代码 / Code

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        Queue<Integer> queue = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            int op = sc.nextInt();

            if (op == 1) {
                int x = sc.nextInt();
                queue.offer(x);

            } else if (op == 2) {
                if (!queue.isEmpty()) {
                    queue.poll();
                } else {
                    System.out.println("ERR_CANNOT_POP");
                }

            } else if (op == 3) {
                if (!queue.isEmpty()) {
                    System.out.println(queue.peek());
                } else {
                    System.out.println("ERR_CANNOT_QUERY");
                }

            } else if (op == 4) {
                System.out.println(queue.size());
            }
        }
    }
}
```

#### 核心思路

直接使用 Queue 模拟先进先出：

```text
1 x → offer(x)
2   → poll()
3   → peek()
4   → size()
```

空队列判断统一使用 `queue.isEmpty()`。

#### 易错点 / Pitfalls

* 队列是 FIFO，不是栈的 LIFO
* `offer()` 入队、`poll()` 删除队首、`peek()` 查看队首
* 不要使用 `peek() != null` 判断是否为空，直接 `queue.isEmpty()`
* `ERR_CANNOT_POP` 和 `ERR_CANNOT_QUERY` 是字符串，要使用双引号
* `Queue` 是接口，常见实现 `Queue<Integer> queue = new LinkedList<>();`

#### 关键词触发 / Triggers

"队列" / "入队" / "出队" / "队首" → Queue + offer/poll/peek

#### 标签

`ACM` `队列` `FIFO` `模拟` `Scanner` `输入输出`

### 4. 赛车排名 / 拓扑排序

#### 题目 / Problem

给出 N 个队伍以及 M 场比赛结果，P1 赢了 P2 表示 P1 的排名必须在 P2 前面。若有多个合法排名，要求编号小的队伍优先。

#### 代码 / Code

```java
import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        List<Integer>[] graph = new ArrayList[n + 1];

        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }

        int[] indegree = new int[n + 1];

        // P1 赢 P2：P1 -> P2
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());

            int p1 = Integer.parseInt(st.nextToken());
            int p2 = Integer.parseInt(st.nextToken());

            graph[p1].add(p2);
            indegree[p2]++;
        }

        // 小根堆：多个入度为 0 的队伍时，编号小的优先
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        for (int i = 1; i <= n; i++) {
            if (indegree[i] == 0) {
                pq.offer(i);
            }
        }

        StringBuilder result = new StringBuilder();

        while (!pq.isEmpty()) {
            int cur = pq.poll();
            result.append(cur);

            for (int next : graph[cur]) {
                indegree[next]--;

                if (indegree[next] == 0) {
                    pq.offer(next);
                }
            }
        }

        System.out.println(result);
    }
}
```

#### 核心思路 / Core Idea

把 P1 赢 P2 转换成有向边 P1 → P2，用 `indegree` 记录每个队伍有多少个前置队伍。

每次选择：**入度 = 0 且编号最小**的队伍，加入答案，然后删除它指向的边，更新入度。

#### 复杂度 / Complexity

时间：O((N + M) log N)
空间：O(N + M)

#### 易错点 / Pitfalls

* `indegree[1] ~ indegree[n]` 对应队伍 1 ~ n，所以数组开 n + 1
* P1 赢 P2 是 `graph[p1].add(p2)`
* 多个入度为 0 时，要用 PriorityQueue 保证小编号优先
* 本题是 ACM 输入输出，不能把样例数据写死

#### 关键词触发 / Triggers

必须在前面 / 依赖关系 / 合法顺序 → **拓扑排序**
编号小的优先 → **小根堆**

#### 标签

`ACM` `图` `拓扑排序` `入度` `PriorityQueue` `贪心`