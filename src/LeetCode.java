import java.util.*;

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
     * 普通做法
     * <a href="https://leetcode.cn/problems/rotate-array/description/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public void rotate(int[] nums, int k) {
        int n = nums.length;
        int []result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = nums[(i + n - k % n) % n];
        }
        System.arraycopy(result, 0, nums, 0, result.length);
        System.out.println(Arrays.toString(nums));
    }

    /**
     * 翻转数组做法
     * @param nums
     * @param k
     */
    public void rotate2(int[] nums, int k) {
        k %= nums.length;
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

    public void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }

    /**
     * 13* 买卖股票 超时
     * <a href="https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public int maxProfit1_wrong(int[] prices) {
        int maxProfit = 0;
        for (int i = 1; i < prices.length; i++) {
            for (int j = i - 1; j >= 0; j--) {
                maxProfit = Math.max(maxProfit, prices[i] - prices[j]);
            }
        }
        System.out.println(maxProfit);
        return maxProfit;
    }

    public int maxProfit1(int[] prices) {
        int minPrice = prices[0];
        int maxProfit = 0;
        for (int i = 1; i < prices.length; i++) {
            maxProfit = Math.max(maxProfit, prices[i] - minPrice);
            minPrice = Math.min(minPrice, prices[i]);
        }
        System.out.println(maxProfit);
        return maxProfit;
    }

    /**
     * 14* 买卖股票进阶题 动态规划 / 贪心算法
     * <a href="https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/?envType=study-plan-v2&envId=top-interview-150">...</a>
     * @param prices
     * @return
     */
    public int maxProfit(int[] prices) {
        /*int [][]dp = new int[prices.length][2];
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        for (int i = 1; i < prices.length; i++) {
            dp[i][1] = Math.max(dp[i - 1][0] - prices[i], dp[i - 1][1]);
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
        }
        System.out.println(dp[prices.length - 1][0]);
        return dp[prices.length - 1][0];*/
        int result = 0;
        for (int i = 1; i < prices.length; i++) {
            result += Math.max(0, prices[i] - prices[i - 1]);
        }
        System.out.println(result);
        return result;
    }

    /**
     * 15 * 动态规划/贪心
     * 跳跃游戏
     * <a href="https://leetcode.cn/problems/jump-game/description/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public boolean canJump(int[] nums) {
        /*动态规划
        int []dp = new int[nums.length];
        Arrays.fill(dp, 0);
        dp[0] = 1;
        for (int i = 1; i < nums.length; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (!(dp[j] == 0 || (dp[j] == 1 && nums[j] < i - j))) {
                    dp[i] = 1;
                    break;
                }
            }
        }
        System.out.println(Arrays.toString(dp));
        return dp[nums.length - 1] == 1;*/

        int distance = 0;
        for (int i = 0; i < nums.length && i <= distance; i++) {
            distance = Math.max(distance, i + nums[i]);
        }
        return distance >= nums.length - 1;
    }

    /**
     * 16* 跳跃游戏2 dp 复杂度略高， 贪心算法 反向查找O(n)
     * <a href="https://leetcode.cn/problems/jump-game-ii/?envType=study-plan-v2&envId=top-interview-150">...</a>
     * @param nums
     * @return
     */
    public int jump(int[] nums) {
        int []dp = new int[nums.length];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int i = 1; i < nums.length; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if ((dp[j] < Integer.MAX_VALUE && nums[j] >= i - j)) {
                    dp[i] = Math.min(dp[i], dp[j] + 1);
                }
            }
        }
        System.out.println(Arrays.toString(dp));
        return dp[nums.length - 1];
    }

    /**
     * 17* H指数
     * <a href="https://leetcode.cn/problems/h-index/description/?envType=study-plan-v2&envId=top-interview-150">...</a>
     * @param citations
     * @return
     */
    public int hIndex(int[] citations) {
        /*int []H = new int[citations.length];
        for (int i = 0; i < citations.length; i++) {
            for (int j = 0; j < citations.length; j++) {
                if (citations[i] > j) {
                    H[j]++;
                }
            }
        }
        for (int i = H.length - 1; i >= 0; i--) {
            if (H[i] > i) {
                System.out.println(i + 1);
                return i + 1;
            }
        }
        return 0;*/
        int h = 0;
        int i = citations.length - 1;
        Arrays.sort(citations);
        while (h <= citations.length && i >= 0) {
            if (citations[i] > h) {
                h++;
            }
            i--;
        }
        System.out.println(h);
        return h;
    }

    /**
     * Your RandomizedSet object will be instantiated and called as such:
     * RandomizedSet obj = new RandomizedSet();
     * boolean param_1 = obj.insert(val);
     * boolean param_2 = obj.remove(val);
     * int param_3 = obj.getRandom();
     * 18 * O(1)时间插入删除获取元素
     * <a href="https://leetcode.cn/problems/insert-delete-getrandom-o1/description/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    class RandomizedSet {
        List<Integer> list ;

        public RandomizedSet() {
            list = new ArrayList<>();
        }

        public boolean insert(int val) {
            if (list.contains(val)) {
                return false;
            }
            return list.add(val);
        }

        public boolean remove(int val) {
            int index = list.indexOf(val);
            if (index < 0) {
                return false;
            }
            list.remove(index);
            return true;
        }

        public int getRandom() {
            return list.get(new Random().nextInt(list.size()));
        }
    }

    /**
     * 19 数组乘积
     * <a href="https://leetcode.cn/problems/product-of-array-except-self/?envType=study-plan-v2&envId=top-interview-150">...</a>
     * @param nums
     * @return
     */
    public int[] productExceptSelf(int[] nums) {
        int []answer = new int[nums.length];
        int []dp = new int[nums.length];
        int []np = new int[nums.length];
        dp[0] = nums[0];
        np[nums.length - 1] = nums[nums.length - 1];
        for (int i = 1; i < nums.length; i++) {
            dp[i] = dp[i - 1] * nums[i];
        }
        for (int i = nums.length - 2; i >= 0; i--) {
            np[i] = np[i + 1] * nums[i];
        }
        answer[0] = np[1];
        answer[nums.length - 1] = dp[nums.length - 2];
        for (int i = 1; i < nums.length - 1; i++) {
            answer[i] = dp[i - 1] * np[i + 1];
        }
        System.out.println(Arrays.toString(answer));
        System.out.println(Arrays.toString(dp));
        System.out.println(Arrays.toString(np));
        return answer;
    }

    /**
     * 20* 加油站 贪心，实际就是二重循环跳过一些一定不可以的情况
     * <a href="https://leetcode.cn/problems/gas-station/description/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int n = gas.length;
        int i = 0;
        while (i < n) {
            int leftGas = 0;
            int j = 0;
            while (j < n) {
                leftGas += gas[(i + j) % n] - cost[(i + j) % n];
                if (leftGas < 0) {
                    break;
                }
                j++;
            }
            if (j == n) {
                System.out.println(i);
               return i;
            } else {
                i = i + j + 1;
            }
        }
        return -1;
    }

    /**
     * 21 * 分发糖果
     * <a href="https://leetcode.cn/problems/candy/?envType=study-plan-v2&envId=top-interview-150">...</a>
     * @param ratings
     * @return
     */
    public int candy(int[] ratings) {
        int []candy = new int[ratings.length];
        Arrays.fill(candy, 1);
        for (int i = 1; i < ratings.length; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candy[i] = candy[i - 1] + 1;
            }
        }
        for (int i = candy.length - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                candy[i] = Math.max(candy[i], candy[i + 1] + 1);
            }
        }
//        System.out.println(Arrays.toString(ratings));
//        System.out.println(Arrays.toString(candy));
//        System.out.println(Arrays.stream(candy).sum());
        return Arrays.stream(candy).sum();
    }



}
