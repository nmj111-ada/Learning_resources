# 卡码网刷题笔记

> 卡码网（KamaCoder）刷题记录，主要练习 ACM 输入输出格式。

---

# 卡码网 A+B I～VI

这几题主要不是算法题，而是在练习 **ACM 输入输出**。

### 1. A+B I

知识点：

* `Scanner`
* `while (sc.hasNextInt())`
* 读到一对 `a、b` 就计算
* `System.out.println(a + b)`

### 2. A+B II

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

### 3. A+B III

知识点：

* 持续读取 `a、b`
* `0 0` 是结束标志
* `break` 结束循环
* `0 0` 本身不计算

触发：
`0 0 结束` → **`while + break`**

### 4. A+B IV

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

### 5. A+B VII

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

### 6. A+B VIII

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

### 总结知识点

`Scanner`
`hasNextInt()`
`nextInt()`
`while`
`for`
`break`
`多组测试数据`
`按 N/M 控制读取次数`
`ACM 输入输出格式`
