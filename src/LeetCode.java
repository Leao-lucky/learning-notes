import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LeetCode {

    /**
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

    public int longestLength = 0;

    /**
     * 求最长不重复字串
     * <a href="https://leetcode.cn/problems/longest-substring-without-repeating-characters/">...</a>
     * 此解法超时
     */
    public void lengthOfLongestSubstring(String s) {
        if (s.length() <= 1 || !hasRepeatChar(s)) {
            if (s.length() > longestLength) {
                longestLength = s.length();
            }
            return;
        }
        for (int i = 0; i < 2; i++) {
            lengthOfLongestSubstring(s.substring(i, s.length() - 1 + i));
        }
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

    /**
     * 寻找两个正序数组中位数
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

    /*5*
     * 最长回文数
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

    /*6* Z字符串变换
     * <a href="https://leetcode.cn/problems/zigzag-conversion/description/">...</a>
     */
//    public String zConvert(String s, int numRows) {
//        int [][]result = new int[1000][1000];
//        for (int i = 0; i < s.length(); i++) {
//            int val = i % (numRows * 2 - 2);
//            int column = val - numRows + 1;
//            int row =
//            result[][i % numRows] = s.charAt(i);
//        }
//    }

    /**
     *7* 合并有序数组，我用的是归并排序
     * <a href="https://leetcode.cn/problems/merge-sorted-array/description/?envType=study-plan-v2&envId=top-interview-150">...</a>
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = 0, j = 0;
        int [] result = new int[nums1.length];
        while (i < m && j < n) {
            if (nums1[i] < nums2[j]) {
                result[i + j] = nums1[i];
                i++;
            } else {
                result[i + j] = nums2[j];
                j++;
            }
        }
        if (i < m) {
            for (; i < m; i++) {
                result[i + j] = nums1[i];
            }
        } else if (j < n) {
            for (;j < n; j++) {
                result[i + j] = nums2[j];
            }
        }
        System.arraycopy(result, 0, nums1, 0, result.length);
    }

    /**
     * 8* 去除指定元素
     * @param nums
     * @param val
     * @return
     */
    public int removeElement(int[] nums, int val) {
        int j = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != val) {
                nums[j++] = nums[i];
            }
        }
        return j;
    }

    /**
     * 9* 去除重复元素，保持相对顺序
     * @param nums
     * @return
     */
    public int removeDuplicates(int[] nums) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int j = 0;
        for (int i = 0; i < nums.length; i++) {
            if (!map.containsKey(nums[i])) {
                map.put(nums[i], 1);
                nums[j++] = nums[i];
            }
        }
        return j;
    }

    /**MID 难度
     * <a href="https://leetcode.cn/problems/remove-duplicates-from-sorted-array-ii/description/?envType=study-plan-v2&envId=top-interview-150">...</a>
     * 10* 去除重复元素，保持相对顺序
     * @param nums
     * @return
     */
    public int removeDuplicates2(int[] nums) {
        if (nums.length <=2) {
            return nums.length;
        }
        int j = 2;
        for (int i = 2; i < nums.length; i++) {
            if (nums[i] != nums[j - 2]) {
                nums[j++] = nums[i];
            }
        }
        int []result = new int[j];
//        System.arraycopy(nums, 0, result, 0, j);
//        System.out.println(Arrays.toString(result));
        return j;
    }

    /**
     * 11* 多数元素
     * <a href="https://leetcode.cn/problems/majority-element/?envType=study-plan-v2&envId=top-interview-150">...</a>
     * @param nums
     * @return
     */
    public int majorityElement(int[] nums) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int major = nums.length / 2;
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])) {
                map.put(nums[i], map.get(nums[i]) + 1);
            } else {
                map.put(nums[i], 1);
            }
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() > major) {
                return entry.getKey();
            }
        }
        return -1;
    }

    /**
     * 12* 轮转数组
     */
    public void rotate(int[] nums, int k) {

    }



}
