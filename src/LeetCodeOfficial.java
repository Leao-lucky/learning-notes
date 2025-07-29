import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode官方解答
 */
public class LeetCodeOfficial {

    /*1*
     * <a href="https://leetcode.cn/problems/two-sum/description/">...</a>
     *
     * @param nums 官解
     */
    public int[] twoSumSolution(int[] nums, int target) {
        Map<Integer, Integer> hashtable = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; ++i) {
            if (hashtable.containsKey(target - nums[i])) {
                return new int[]{hashtable.get(target - nums[i]), i};
            }
            hashtable.put(nums[i], i);
        }
        return new int[0];
    }

    public class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    /*2*
     * 两数相加
     * <a href="https://leetcode.cn/problems/add-two-numbers/description/">...</a>
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode head = new ListNode();
        ListNode result = new ListNode();
        head.next = result;
        int carry = 0;
        while (l1 != null || l2 != null || carry != 0) {
            int a = 0, b = 0;
            if (l1 != null) {
                a = l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                b = l2.val;
                l2 = l2.next;
            }
            if (a + b + carry >= 10) {
                result.val = (a + b + carry) % 10;
                carry = 1;
            } else {
                result.val = a + b + carry;
                carry = 0;
            }
            if (l1 != null || l2 != null || carry != 0) {
                result.next = new ListNode();
                result = result.next;
            }
        }
        return head.next;
    }

    public boolean hasRepeatChar(String str) {
        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            if (map.containsKey(str.substring(i, i + 1))) {
                return true;
            } else {
                map.put(str.substring(i, i + 1), i);
            }
        }
        return false;
    }

    /*3*
     * 求最长不重复字串
     * <a href="https://leetcode.cn/problems/longest-substring-without-repeating-characters/">...</a>
     *滑动窗口
     */
    public int lengthOfLongestSubstring2(String s) {
        int result = 0;
        for (int start = 0, end = 0; end <= s.length(); end++) {
            if (start < end && hasRepeatChar(s.substring(start, end))) {
                start++;
            }
            result = Math.max(result, end - start);
        }
        return result;
    }

    /*4*
     * 寻找两个正序数组中位数
     * 二分查找
     * <a href="https://leetcode.cn/problems/median-of-two-sorted-arrays/">...</a>
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }
        int m = nums1.length, n = nums2.length;
        int left = 0, right = m;
        while (left <= right) {
            int i = (left + right) / 2;
            int j = (m + n + 1) / 2 - i;
            int left1 = (i == 0) ? Integer.MIN_VALUE : nums1[i - 1];
            int right1 = (i == m) ? Integer.MAX_VALUE : nums1[i];
            int left2 = (j == 0) ? Integer.MIN_VALUE : nums2[j - 1];
            int right2 = (j == n) ? Integer.MAX_VALUE : nums2[j];
            if (left1 <= right2 && left2 <= right1) {
                if ((m + n) % 2 == 0) {
                    return (Math.max(left1, left2) + Math.min(right1, right2)) / 2.0;
                }
                return Math.max(left1, left2);
            } else if (left1 > right2) {
                right = i - 1;
            } else {
                left = i + 1;
            }
        }
        return -1;
    }

    /*5*
     * 最长回文数
     * <a href="https://leetcode.cn/problems/longest-palindromic-substring/submissions/637679792/">...</a>
     * 动态规划
     */
    public String longestPalindrome(String s) {
        int len = s.length();
        if (len < 2) {
            return s;
        }

        int maxLen = 1;
        int begin = 0;
        // dp[i][j] 表示 s[i..j] 是否是回文串
        boolean[][] dp = new boolean[len][len];
        // 初始化：所有长度为 1 的子串都是回文串
        for (int i = 0; i < len; i++) {
            dp[i][i] = true;
        }

        char[] charArray = s.toCharArray();
        // 递推开始
        // 先枚举子串长度
        for (int L = 2; L <= len; L++) {
            // 枚举左边界，左边界的上限设置可以宽松一些
            for (int i = 0; i < len; i++) {
                // 由 L 和 i 可以确定右边界，即 j - i + 1 = L 得
                int j = L + i - 1;
                // 如果右边界越界，就可以退出当前循环
                if (j >= len) {
                    break;
                }

                if (charArray[i] != charArray[j]) {
                    dp[i][j] = false;
                } else {
                    if (j - i < 3) {
                        dp[i][j] = true;
                    } else {
                        dp[i][j] = dp[i + 1][j - 1];
                    }
                }

                // 只要 dp[i][L] == true 成立，就表示子串 s[i..L] 是回文，此时记录回文长度和起始位置
                if (dp[i][j] && j - i + 1 > maxLen) {
                    maxLen = j - i + 1;
                    begin = i;
                }
            }
        }
        return s.substring(begin, begin + maxLen);
    }
    //中心扩展法
    public String longestPalindrome2(String s) {
        if (s == null || s.length() < 1) {
            return "";
        }
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i);
            int len2 = expandAroundCenter(s, i, i + 1);
            int len = Math.max(len1, len2);
            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    public int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            --left;
            ++right;
        }
        return right - left - 1;
    }




}
