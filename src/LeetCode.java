import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LeetCode {

    /**
     * https://leetcode.cn/problems/two-sum/description/
     *
     * @param nums   自解
     * @param target
     * @return
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
     * https://leetcode.cn/problems/two-sum/description/
     *
     * @param nums   官解
     * @param target
     * @return
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
     * https://leetcode.cn/problems/add-two-numbers/description/
     *
     * @param l1
     * @param l2
     * @return
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
     * https://leetcode.cn/problems/longest-substring-without-repeating-characters/
     * 此解法超时
     * @param s
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

    public int lengthOfLongestSubstring2(String s) {
        int start = 0, result = 0;
        int[] last = new int[128];
        Arrays.fill(last, -1);
        for (int i = 0; i < s.length(); i++) {
            int index = s.charAt(i);
            start = Math.max(start, last[index]);
            result = Math.max(result, i - start + 1);
            last[index] = i;
        }
        return result;
    }


}
