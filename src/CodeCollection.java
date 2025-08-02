import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 大全
 * 从第一题开始刷
 * <a href="https://leetcode.cn/problemset/">...</a>
 */
public class CodeCollection {

    /**
     * 1 * 两数之和
     * <a href="https://leetcode.cn/problems/two-sum/description/">...</a>
     *
     * @param nums 自解
     */
    public int[] twoSumAnswer(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{0, 0};
    }

    /**
     * 1 * 两数之和
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

    /**
     * 2 * 两数相加
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


    /**
     * 3 * 求最长不重复字串
     * <a href="https://leetcode.cn/problems/longest-substring-without-repeating-characters/">...</a>
     * 此解法超时
     */
    public int lengthOfLongestSubstring(String s) {
        if (s.length() <= 0) {
            return 0;
        }
        int start = 0, end = 0, len = 1;
        HashMap<Character, Integer> map = new HashMap<>();
        map.put(s.charAt(0), 0);
        while (start < s.length() && end < s.length() - 1) {
            while ((end + 1) < s.length() && !map.containsKey(s.charAt(end + 1))) {
                map.put(s.charAt(end + 1), end + 1);
                end++;
            }
            len = Math.max(len, end - start + 1);
            map.remove(s.charAt(start));
            start++;
        }
        return len;
    }


    /**
     * 4 * 寻找两个正序数组中位数
     * 暴力，归并排序 + 取中位数 O(N)
     * <a href="https://leetcode.cn/problems/median-of-two-sorted-arrays/">...</a>
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int[] num3 = new int[nums1.length + nums2.length];
        int length = nums1.length + nums2.length;
        int cursor = 0;
        double result = 0;
        for (int i = 0, j = 0; i < nums1.length || j < nums2.length; cursor++) {
            if (i >= nums1.length) {
                num3[cursor] = nums2[j++];
            } else if (j >= nums2.length) {
                num3[cursor] = nums1[i++];
            } else if (nums1[i] < nums2[j]) {
                num3[cursor] = nums1[i++];
            } else {
                num3[cursor] = nums2[j++];
            }
            System.out.print(Arrays.toString(num3));
            if (length % 2 == 0 && cursor == length / 2) {
                result = (num3[cursor] + num3[cursor - 1]) / 2.0;
                break;
            } else if (length % 2 != 0 && cursor == length / 2) {
                result = num3[cursor];
                break;
            }
        }
        return result;
    }

    /*
     * 6 * 最长回文数
     * <a href="https://leetcode.cn/problems/longest-palindromic-substring/submissions/637679792/">...</a>
     * dp[i][j] i 字符串下标 j子串长度
     */
    public String longestPalindrome(String s) {
        if (s.length() <= 1) {
            return s;
        }
        int[][] dp = new int[s.length()][s.length() + 1];
        dp[0][0] = 0;
        for (int i = 0; i < s.length(); i++) {
            dp[i][1] = 1;
        }
        for (int i = 0; i < s.length(); i++) {
            if (i + 1 < s.length() && s.charAt(i) == s.charAt(i + 1)) {
                dp[i][2] = 1;
            } else {
                dp[i][2] = 0;
            }
        }
        for (int j = 3; j <= s.length(); j++) {
            for (int i = 0; i < s.length(); i++) {
                if (i + 1 >= s.length() || i + j - 1 >= s.length()) {
                    dp[i][j] = 0;
                } else {
                    dp[i][j] = dp[i + 1][j - 2] == 1 && s.charAt(i) == s.charAt(i + j - 1) ? 1 : 0;
                }

            }
        }
//        for (int i = 0; i < s.length(); i++) {
//            for (int j = 0; j <= s.length(); j++) {
//                System.out.print("(" + i + "," + j + ")=" + dp[i][j] + " ");
//                if (dp[i][j] == 1) {
//                    System.out.print(s.substring(i, Math.min(i + j, s.length())));
//                }
//            }
//            System.out.println();
//        }

        for (int j = s.length(); j > 0; j--) {
            for (int i = 0; i < s.length(); i++) {
                if (dp[i][j] == 1) {
                    return s.substring(i, i + j);
                }
            }
        }
        return s;
    }

    /*
     * 7 * Z字符串变换
     * <a href="https://leetcode.cn/problems/zigzag-conversion/description/">...</a>
     */
    public String zConvert(String s, int numRows) {
        if (numRows <= 1) {
            return s;
        }
        String[] res = new String[numRows];
        Arrays.fill(res, "");
        int flag = 0;
        int dir = 1;
        for (int i = 0; i < s.length(); i++) {
            res[flag] += s.charAt(i);
            flag += dir;
            if (flag == numRows - 1 || flag == 0) {
                dir *= -1;
            }
        }
        String resStr = "";
        for (int i = 0; i < res.length; i++) {
            resStr += res[i];
        }
        return resStr;
    }
}
