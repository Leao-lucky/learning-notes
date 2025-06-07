[markdown 基础语法规则](https://blog.csdn.net/qq_40818172/article/details/126260661)
# 正则表达式（部分）
## 基本字符匹配
| 规则       | 含义                         | 示例                           |
|----------|----------------------------|------------------------------|
| `.`      | 匹配任意一个字符（除换行符）             | `a.c` 可匹配 `abc`, `a1c`       |
| `[abc]`  | 匹配 `a`、`b`、`c` 中的任意一个字符    | `gr[ae]y` 匹配 `gray` 或 `grey` |
| `[^abc]` | 匹配**不在** `a`、`b`、`c` 中的字符  |                              |
| `[a-z]`  | 匹配 `a` 到 `z` 的任意字符         | `[A-Za-z]` 匹配所有英文字母          |
| `\d`     | 匹配一个数字，等价于 `[0-9]`         |                              |
| `\D`     | 匹配一个非数字字符                  |                              |
| `\w`     | 匹配字母、数字或下划线 `[a-zA-Z0-9_]` |                              |
| `\W`     | 匹配非单词字符                    |                              |
| `\s`     | 匹配空白字符（空格、换行、Tab 等）        |                              |
| `\S`     | 匹配非空白字符                    |                              |
## 数量匹配
| 规则      | 含义                | 示例                       |
| ------- | ----------------- | ------------------------ |
| `*`     | 匹配前一个元素 0 次或多次    | `a*` 匹配 "", "a", "aaa"   |
| `+`     | 匹配前一个元素 1 次或多次    | `a+` 匹配 "a", "aa", "aaa" |
| `?`     | 匹配前一个元素 0 次或 1 次  | `a?` 匹配 "", "a"          |
| `{n}`   | 匹配前一个元素恰好 n 次     | `a{3}` 匹配 "aaa"          |
| `{n,}`  | 匹配前一个元素至少 n 次     | `a{2,}` 匹配 "aa", "aaa"   |
| `{n,m}` | 匹配前一个元素 n 到 m 次之间 | `a{2,4}` 匹配 "aa", "aaa"  |

# java 运行时内存
## 线程共享
方法区 java堆
## 线程私有
程序计数器 java虚拟堆栈 

# 运行时异常与编译异常
## 编译异常
| 异常类                      | 说明               |
| ------------------------ | ---------------- |
| `IOException`            | 处理文件、网络 IO 失败时抛出 |
| `SQLException`           | 数据库操作失败          |
| `ClassNotFoundException` | 类找不到             |
| `InterruptedException`   | 线程被中断时抛出         |
| `FileNotFoundException`  | 文件未找到            |
| `ParseException`         | 解析文本失败，如日期解析     |
| `InstantiationException` | 尝试实例化抽象类或接口      |
## 运行时异常
| 异常类                               | 说明         |
| --------------------------------- | ---------- |
| `NullPointerException`            | 空指针        |
| `ArrayIndexOutOfBoundsException`  | 数组越界       |
| `ArithmeticException`             | 算术错误（如除以零） |
| `ClassCastException`              | 类型转换错误     |
| `IllegalArgumentException`        | 传递非法参数     |
| `IllegalStateException`           | 状态错误       |
| `NumberFormatException`           | 字符串转数字失败   |
| `ConcurrentModificationException` | 集合并发修改异常   |

