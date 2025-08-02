import java.util.*;

/**
 * 面试经典150题
 * 刷题记录
 * <a href="https://leetcode.cn/studyplan/top-interview-150/">...</a>
 */
public class ClassicQuestions {

    /**
     * 7* 合并有序数组，我用的是归并排序
     * <a href="https://leetcode.cn/problems/merge-sorted-array/description/?envType=study-plan-v2&envId=top-interview-150">...</a>
     *
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = 0, j = 0;
        int[] result = new int[nums1.length];
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
            for (; j < n; j++) {
                result[i + j] = nums2[j];
            }
        }
        System.arraycopy(result, 0, nums1, 0, result.length);
    }

    /**
     * 8* 去除指定元素
     *
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
     *
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

    /**
     * MID 难度
     * <a href="https://leetcode.cn/problems/remove-duplicates-from-sorted-array-ii/description/?envType=study-plan-v2&envId=top-interview-150">...</a>
     * 10* 去除重复元素，保持相对顺序
     *
     */
    public int removeDuplicates2(int[] nums) {
        if (nums.length <= 2) {
            return nums.length;
        }
        int j = 2;
        for (int i = 2; i < nums.length; i++) {
            if (nums[i] != nums[j - 2]) {
                nums[j++] = nums[i];
            }
        }
        int[] result = new int[j];
//        System.arraycopy(nums, 0, result, 0, j);
//        System.out.println(Arrays.toString(result));
        return j;
    }

    /**
     * 11* 多数元素
     * <a href="https://leetcode.cn/problems/majority-element/?envType=study-plan-v2&envId=top-interview-150">...</a>
     *
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
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = nums[(i + n - k % n) % n];
        }
        System.arraycopy(result, 0, nums, 0, result.length);
        System.out.println(Arrays.toString(nums));
    }

    /**
     * 翻转数组做法
     *
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
     *
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
     *
     */
    public int jump(int[] nums) {
        int[] dp = new int[nums.length];
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
     *
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
    static class RandomizedSet {
        List<Integer> list;

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
     *
     */
    public int[] productExceptSelf(int[] nums) {
        int[] answer = new int[nums.length];
        int[] dp = new int[nums.length];
        int[] np = new int[nums.length];
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
     * 21 * 分发糖果 两次贪心
     * <a href="https://leetcode.cn/problems/candy/?envType=study-plan-v2&envId=top-interview-150">...</a>
     *
     */
    public int candy(int[] ratings) {
        int[] candy = new int[ratings.length];
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

    /**
     * 22* 接雨水
     *<a href="https://leetcode.cn/problems/trapping-rain-water/description/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public int trap(int[] height) {
        int[] dp = new int[height.length];
        int[] res = new int[height.length];
        int right = dpTrap(dp, height);
        if (right == height.length - 1) {
            return dp[height.length - 1];
        } else {
            Utils.reverseArray(height);
            dpTrap(res, height);
            System.out.println(Arrays.toString(dp));
            System.out.println(Arrays.toString(res));
            System.out.println(right);
            return dp[right] + res[height.length - right - 1];
        }
    }

    public int dpTrap(int[] dp, int[] height) {
        Stack<Integer> stack = new Stack<>();
        stack.push(0);
        dp[0] = 0;
        int left = 0, right = 0;
        for (int i = 1; i < height.length; i++) {
            if (stack.isEmpty() || height[i] < height[left]) {
                stack.push(i);
                dp[i] = dp[i - 1];
            } else {
                right = i;
                int square = Math.min(height[left], height[right]) * (right - left - 1);
                dp[i] = dp[left] + square;
                while (!stack.isEmpty()) {
                    Integer pop = stack.pop();
                    if (height[pop] < height[right] && !stack.isEmpty()) {
                        dp[i] -= height[pop];
                    }
                }
                stack.push(right);
                left = right;
            }
        }
//        System.out.println(Arrays.toString(dp));
        return right;
    }


    /**
     * 23* 罗马数字转整数
     * <a href="https://leetcode.cn/problems/roman-to-integer/description/?envType=study-plan-v2&envId=top-interview-150">...</a>
     *
     */
    public int romanToInt(String s) {
        Map<String, Integer> map = new HashMap<>() {{
            put("I", 1);
            put("V", 5);
            put("X", 10);
            put("L", 50);
            put("C", 100);
            put("D", 500);
            put("M", 1000);
            put("IV", 4);
            put("IX", 9);
            put("XL", 40);
            put("XC", 90);
            put("CD", 400);
            put("CM", 900);
        }};
        int res = 0, i = 0;
        while (i < s.length()) {
            if (i + 1 < s.length() && map.containsKey(s.substring(i, i + 2))) {
                res += map.get(s.substring(i, i + 2));
                i = i + 2;
            } else {
                res += map.get(s.substring(i, i + 1));
                i++;
            }
        }
//        System.out.println(res);
        return res;
    }

    /**
     * 24* 整数转罗马
     * <a href="https://leetcode.cn/problems/integer-to-roman/?envType=study-plan-v2&envId=top-interview-150">...</a>
     *
     */
    public String intToRoman(int num) {
        String[] THOUSANDS = new String[]{"", "M", "MM", "MMM"};
        String[] HUNDREDS = new String[]{"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] TENS = new String[]{"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] ONES = new String[]{"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
        return THOUSANDS[num / 1000] + HUNDREDS[num % 1000 / 100] + TENS[num % 100 / 10] + ONES[num % 10];
    }

    /**
     * 25* 最后一个单词长度
     * <a href="https://leetcode.cn/problems/length-of-last-word/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public int lengthOfLastWord(String s) {
        String[] split = s.split(" ");
        return split[split.length - 1].length();
    }

    /**
     * 26* 最长公共前缀 还可以采用分治和二分查找
     * <a href="https://leetcode.cn/problems/longest-common-prefix/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public String longestCommonPrefix(String[] strs) {
        String res = "";
        for (int i = 0; i < strs[0].length(); i++) {
            int j = 1;
            while (j < strs.length) {
                if (i > strs[j].length() - 1 || !strs[j].substring(0, i + 1).equals(strs[0].substring(0, i + 1))) {
                    break;
                }
                j++;
            }
            if (j >= strs.length) {
                res = strs[0].substring(0, i + 1);
            }
        }
        System.out.println(res);
        return res;
    }

    /**
     * 27 *反转字符串中的单词
     * <a href="https://leetcode.cn/problems/reverse-words-in-a-string/?envType=study-plan-v2&envId=top-interview-150">...</a>
     *
     */
    public String reverseWords(String s) {
        String[] split = s.trim().split("\s+");
//        Utils.reverseArray(split);
        Collections.reverse(Arrays.asList(split));
        String res = "";
        for (int i = 0; i < split.length; i++) {
            res += split[i] + " ";
        }
        return res.substring(0, res.length() - 1);
    }


    /**
     * 28* Z字变换
     * <a href="https://leetcode.cn/problems/zigzag-conversion/?envType=study-plan-v2&envId=top-interview-150">...</a>
     *
     */
    public String convert(String s, int numRows) {
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

    /**
     * 29* 找字符串
     * <a href="https://leetcode.cn/problems/find-the-index-of-the-first-occurrence-in-a-string/description/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public int strStr(String haystack, String needle) {
        return haystack.indexOf(needle);
    }

    /**
     * 30* 文本左右对齐
     * <a href="https://leetcode.cn/problems/text-justification/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public List<String> fullJustify(String[] words, int maxWidth) {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < words.length; ) {
            int wordlen = words[i].length();
            int j = i;
            while (j + 1 < words.length && wordlen + 1 + words[j + 1].length() <= maxWidth) {
                wordlen = wordlen + 1 + words[j + 1].length();
                j++;
            }
            if (j == i) {
                res.add(words[i] + " ".repeat(maxWidth - words[i].length()));
            } else if (j == words.length - 1) {
                String str = "";
                int start = i;
                while (start <= j) {
                    str += words[start] + " ";
                    start++;
                }
                str = str.trim();
                res.add(str + " ".repeat(maxWidth - str.length()));
            } else {
                int blankNum = maxWidth;
                int start = i;
                while (start <= j) {
                    blankNum -= words[start].length();
                    start++;
                }
                String str = "";
                int x = blankNum / (j - i);
                int y = blankNum % (j - i);
                start = i;
                while (start <= j) {
                    if (start - i < y) {
                        str += words[start] + " ".repeat(x + 1);
                    } else {
                        str += words[start] + " ".repeat(x);
                    }
                    start++;
                }
                res.add(str.trim());

            }
            i = j + 1;
        }
        System.out.println(Arrays.toString(res.toArray()));
        return res;
    }

    /**
     * 31* 验证回文串
     * <a href="https://leetcode.cn/problems/valid-palindrome/?envType=study-plan-v2&envId=top-interview-150">...</a>
     *
     */
    public boolean isPalindrome(String s) {
        if (s == null || s.isEmpty()) {
            return true;
        }
        int left = 0, right = s.length() - 1;
        while (left < right) {
            char start = s.charAt(left), end = s.charAt(right);
            if (!Character.isLetterOrDigit(start)) {
                left++;
                continue;
            }
            if (!Character.isLetterOrDigit(end)) {
                right--;
                continue;
            }
            if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
                return false;
            } else {
                left++;
                right--;
            }
        }
        return true;
    }

    /**
     * 32* 子序列
     * <a href="https://leetcode.cn/problems/is-subsequence/?envType=study-plan-v2&envId=top-interview-150">...</a>
     *
     */
    public boolean isSubsequence(String s, String t) {
        int s1 = 0, s2 = 0;
        int m = s.length();
        int n = t.length();
        while (s1 < m && s2 < n) {
            if (s.charAt(s1) == t.charAt(s2)) {
                s1++;
                s2++;
            } else {
                s2++;
            }
        }
        if (s1 == m) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 33* 两数之和 二分查找做法
     */
    public int[] twoSum(int[] numbers, int target) {
        for (int i = 0; i < numbers.length - 1; i++) {
            int num1 = numbers[i];
            int num2 = target - num1;
            int index2 = Arrays.binarySearch(numbers, i + 1, numbers.length - 1, num2);
            if (index2 > 0) {
                return new int[]{i + 1, index2 + 1};
            }
        }
        return new int[]{-1, -1};
    }

    /**
     * 34*最大容器
     * <a href="https://leetcode.cn/problems/container-with-most-water/description/?envType=study-plan-v2&envId=top-interview-150">...</a>
     *
     */
    public int maxArea(int[] height) {
        int start = 0, end = height.length - 1;
        int maxWater = 0;
        while (start < end) {
            int water = Math.min(height[start], height[end]) * (end - start);
            maxWater = Math.max(maxWater, water);
            if (height[start] < height[end]) {
                start++;
            } else {
                end--;
            }

        }
        return maxWater;
    }

    /**
     * 35 * 三数之和
     * <a href="https://leetcode.cn/problems/3sum/?envType=study-plan-v2&envId=top-interview-150">...</a>
     *
     */
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(nums);
        for (int first = 0; first < nums.length - 1; first++) {
            if (first > 0 && nums[first] == nums[first - 1]) {
                continue;
            }
            int third = nums.length - 1;
            for (int second = first + 1; second < nums.length - 1; second++) {
                if (second > first + 1 && nums[second] == nums[second - 1]) {
                    continue;
                }
                while (third > second && nums[third] + nums[second] + nums[first] > 0) {
                    third--;
                }
                if (third == second) {
                    break;
                }
                if (third > second && nums[third] + nums[second] + nums[first] == 0) {
                    res.add(Arrays.asList(nums[first], nums[second], nums[third]));
                }
            }
        }
        return res;
    }

    /**
     * 36* 长度最小子数组
     * <a href="https://leetcode.cn/problems/minimum-size-subarray-sum/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public int minSubArrayLen(int[] nums, int target) {
        if (nums.length <= 0) {
            return 0;
        }
        int left = 0, right = left;
        int sum = nums[left];
        int result = Integer.MAX_VALUE;
        while (right < nums.length) {
            if (left < right) {
                sum += nums[right];
            }
            if (sum >= target) {
                while (sum - nums[left] >= target) {
                    sum -= nums[left];
                    left++;
                }
                result = Math.min(result, right - left + 1);
            }
            right++;
        }
        return result == Integer.MAX_VALUE ? 0 : result;

    }

    /**
     * 37 * 串联子串,固定窗口大小，按字母平移每个窗口都暴力判断需要循环判断s.lenghth - subLen次
     *<a href="https://leetcode.cn/problems/substring-with-concatenation-of-all-words/description/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public List<Integer> findSubstring_exceed_time(String s, String[] words) {
        if (words == null || words.length == 0) {
            return Collections.emptyList();
        }
        int m = words.length;
        int n = words[0].length();
        int subLen = m * n;
        List<Integer> res = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i + subLen <= s.length(); i++) {
            map.clear();
            for (int j = 0; j < m; j++) {
                String s1 = s.substring(i + j * n, i + (j + 1) * n);
                map.put(s1, map.getOrDefault(s1, 0) + 1);
            }
            for (String s2 : words) {
                map.put(s2, map.getOrDefault(s2, 0) - 1);
            }
            boolean found = true;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (0 != entry.getValue()) {
                    found = false;
                    break;
                }
            }
            if (found) {
                res.add(i);
            }
        }
        return res;
    }

    /**
     * 37 * 串联子串,固定窗口大小，按字母平移每个窗口都暴力判断需要循环判断s.lenghth - subLen次
     *<a href="https://leetcode.cn/problems/substring-with-concatenation-of-all-words/description/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public List<Integer> findSubstring(String s, String[] words) {
        if (words == null || words.length == 0) {
            return Collections.emptyList();
        }
        int m = words.length;
        int n = words[0].length();
        int subLen = m * n;
        List<Integer> res = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; /*patch start*/ i < n/*patch end*/ && i + subLen <= s.length(); i++) {
            map.clear();
            for (int j = 0; j < m; j++) {
                String s1 = s.substring(i + j * n, i + (j + 1) * n);
                map.put(s1, map.getOrDefault(s1, 0) + 1);
            }
            for (String s2 : words) {
                map.put(s2, map.getOrDefault(s2, 0) - 1);
                if (map.get(s2) == 0) {
                    map.remove(s2);
                }
            }
            if (map.isEmpty()) {
                res.add(i);
            }
            int start = i, end = i + subLen;
            //patch start
            while (end + n <= s.length()) {
                String endStr = s.substring(end, end + n);
                map.put(endStr, map.getOrDefault(endStr, 0) + 1);
                if (map.get(endStr) == 0) {
                    map.remove(endStr);
                }
                end = end + n;
                String startStr = s.substring(start, start + n);
                map.put(startStr, map.getOrDefault(startStr, 0) - 1);
                if (map.get(startStr) == 0) {
                    map.remove(startStr);
                }
                start = start + n;
                if (map.isEmpty()) {
                    res.add(start);
                }
            }
            //patch end
        }
        return res;
    }

    /**
     * 38* 最小覆盖子串
     * <a href="https://leetcode.cn/problems/minimum-window-substring/?envType=study-plan-v2&envId=top-interview-150">...</a>
     *
     */
    public String minWindow(String s, String t) {
        int n = t.length();
        Blanket blanket = new Blanket();
        for (int i = 0; i < n; i++) {
            blanket.put(t.charAt(i), blanket.get(t.charAt(i)) - 1);
        }
        int start = 0, end = start + t.length() - 1;
        for (int i = start; i <= end && i < s.length(); i++) {
            if (t.contains((s.substring(i, i + 1)))) {
                blanket.put(s.charAt(i), blanket.get(s.charAt(i)) + 1);
            }
        }
        if (blanket.getNegetive() <= 0) {
            return s.substring(start, end + 1);
        }
        int length = Integer.MAX_VALUE;
        String res = "";
        while (end < s.length()) {
            if (end + 1 < s.length() && t.contains(s.substring(end + 1, end + 2))) {
                blanket.put(s.charAt(end + 1), blanket.get(s.charAt(end + 1)) + 1);
            }
            end++;
            while (blanket.getNegetive() <= 0) {
                if (t.contains(s.substring(start, start + 1))) {
                    if (blanket.get(s.charAt(start)) > 0) {
                        blanket.put(s.charAt(start), blanket.get(s.charAt(start)) - 1);
                        start++;
                    } else {
                        break;
                    }
                } else {
                    start++;
                }
            }
            if (end - start + 1 < length && blanket.getNegetive() <= 0) {
                length = end - start + 1;
                res = s.substring(start, end + 1);
            }
        }
        return res;
    }

    static class Blanket {
        HashMap<Character, Integer> map = new HashMap<>();
        int negetive = 0;
        public Blanket() {}

        public void put(char c, int val) {
            Integer oldValue = map.getOrDefault(c, 0);
            map.put(c, val);
            Integer newValue = map.getOrDefault(c, 0);
            if (oldValue < 0 && newValue >=0) {
                negetive--;
            } else if (oldValue >= 0 && newValue < 0) {
                negetive++;
            }
        }

        public int get(char c) {
            return map.getOrDefault(c, 0);
        }

        public int getNegetive() {
            return negetive;
        }

    }

    /**
     * 39* 有效的数独
     * <a href="https://leetcode.cn/problems/valid-sudoku/description/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public boolean isValidSudoku(char[][] board) {
        int [][]colume = new int[board.length][board.length];
        int [][]row = new int[board.length][board.length];
        int [][][]subBox = new int[board.length / 3][board.length / 3][9];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != '.') {
                    int content = board[i][j] - '0' - 1;
                    row[i][content]++;
                    colume[content][j]++;
                    subBox[i / 3][j / 3][content]++;
                    if (row[i][content] > 1 || colume[content][j] > 1 || subBox[i / 3][j / 3][content] > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 40* 螺旋矩阵
     *<a href="https://leetcode.cn/problems/spiral-matrix/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        List<Integer> res = new ArrayList<>();
        HashMap <Integer,Integer> map = new HashMap<>();
        int i = 0, j = 0;
        int flag = 0;
        while (i >= 0 && i < m && j >= 0 && j < n) {
            res.add(matrix[i][j]);
            map.put(i * n + j, 1);
            if (flag % 4 == 0) {//当前变换是j++
                if ((j + 1) >= n || map.containsKey(i * n + j + 1)) {
                    flag++;
                    i++;
                    if (map.containsKey(i * n + j)) {
                        break;
                    }
                } else {
                    j++;
                }
            } else if (flag % 4 == 1) {//当前变换是i++
                if ((i + 1) >= m || map.containsKey((i + 1) * n + j)) {
                    flag++;
                    j--;
                    if (map.containsKey(i * n + j)) {
                        break;
                    }
                } else {
                    i++;
                }
            } else if (flag % 4 == 2) {//当前变换是j--
                if ((j - 1) < 0 || map.containsKey(i * n + j - 1)) {
                    flag++;
                    i--;
                    if (map.containsKey(i * n + j)) {
                        break;
                    }
                } else {
                    j--;
                }
            } else {
                if ((i - 1) < 0 || map.containsKey((i - 1) * n + j)) {
                    flag++;
                    j++;
                    if (map.containsKey(i * n + j)) {
                        break;
                    }
                } else {
                    i--;
                }
            }
        }
        return res;
    }

    /**
     * 41 * 旋转图像 矩阵知识 先矩阵转置 再行反转
     * <a href="https://leetcode.cn/problems/rotate-image/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public void rotate(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i; j < matrix.length; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[i][matrix.length - j -1];
                matrix[i][matrix.length - j - 1] = temp;
            }
        }
    }

    /**
     * 42 * 矩阵置零
     *<a href="https://leetcode.cn/problems/set-matrix-zeroes/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public void setZeroes(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int temp = matrix[0][0];
        for (int i = 0; i < m; i++) {
            if (matrix[i][0] == 0) {
                matrix[0][0] = 0;
                break;
            }
        }
        for (int j = 0; j < n; j++) {
            if (matrix[m - 1][j] == 0) {
                matrix[m - 1][0] = 0;
                break;
            }
        }
        for (int i = m - 1; i >= 0; i--) {
            if (matrix[i][n - 1] == 0) {
                matrix[m - 1][n - 1] = 0;
                break;
            }
        }
        for (int j = n - 1; j > 0; j--) {
            if (matrix[0][j] == 0 || temp == 0) {
                matrix[0][n - 1] = 0;
                break;
            }
        }
        for (int i = 1; i < m - 1; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    break;
                }
            }
        }
        for (int j = 1; j < n - 1; j++) {
            for (int i = 0; i < m; i++) {
                if (matrix[i][j] == 0) {
                    matrix[0][j] = 0;
                    break;
                }
            }
        }
        for (int i = 1; i < m - 1; i++) {
            if (matrix[i][0] == 0) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = 0;
                }
            }
        }
        for (int j = 1; j < n - 1; j++) {
            if (matrix[0][j] == 0) {
                for (int i = 0; i < m; i++) {
                    matrix[i][j] = 0;
                }
            }
        }
        if (matrix[0][n - 1] == 0) {
            for (int j = 1; j < n; j++) {
                matrix[0][j] = 0;
            }
            temp = 0;
        }
        if (matrix[m -1][n - 1] == 0) {
            for (int i = m - 1; i >= 0; i--) {
                matrix[i][n - 1] = 0;
            }
        }
        if (matrix[m - 1][0] == 0) {
            for (int j = 0; j < n; j++) {
                matrix[m - 1][j] = 0;
            }
        }
        if (matrix[0][0] == 0) {
            for (int i = 0; i < m; i++) {
                matrix[i][0] = 0;
            }
        }
        if (temp == 0) {
            matrix[0][0] = 0;
        }
    }

    /**
     * 43 * 矩阵 生命游戏
     * <a href="https://leetcode.cn/problems/game-of-life/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public void gameOfLife(int[][] board) {
        int [][] newBoard = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                int state = getLiftCellState(board, i , j);
                if (board[i][j] == 1) {
                    if (state < 2 || state > 3) {
                        newBoard[i][j] = 0;
                    } else {
                        newBoard[i][j] = 1;
                    }
                } else {
                    if (state == 3) {
                        newBoard[i][j] = 1;
                    } else {
                        newBoard[i][j] = 0;
                    }

                }
            }
        }
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(newBoard[i], 0, board[i], 0, board[0].length);
        }
    }

    private int getLiftCellState(int[][] board, int i , int j) {
        int m = board.length;
        int n = board[0].length;
        int state = 0;
        if (i - 1 >= 0 && j - 1 >= 0) {
            state += board[i - 1][j - 1];
        }
        if (i - 1 >= 0) {
            state += board[i - 1][j];
        }
        if (j - 1 >= 0) {
            state += board[i][j - 1];
        }
        if (i + 1 < m) {
            state += board[i + 1][j];
        }
        if (j + 1 < n) {
            state += board[i][j + 1];
        }
        if (i + 1 < m && j + 1 < n) {
            state += board[i + 1][j + 1];
        }
        if (i - 1 >= 0 && j + 1 < n) {
            state += board[i - 1][j + 1];
        }
        if (i + 1 < m && j - 1 >= 0) {
            state += board[i + 1][j - 1];
        }

        return state;
    }

    /**
     * 44 * 赎金信 哈希表
     * <a href="https://leetcode.cn/problems/ransom-note/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public boolean canConstruct(String ransomNote, String magazine) {
        HashMap<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < magazine.length(); i++) {
            char ch = magazine.charAt(i);
            map.put(ch, map.getOrDefault(ch, 0) + 1);
        }
        for (int i = 0; i < ransomNote.length(); i++) {
            char ch = ransomNote.charAt(i);
            map.put(ch, map.getOrDefault(ch, 0) - 1);
            if (map.get(ch) < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 45 *同构字符串 哈希表
     * <a href="https://leetcode.cn/problems/isomorphic-strings/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public boolean isIsomorphic(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        HashMap<Character, Character> mapA = new HashMap<>();
        HashMap<Character, Character> mapB = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            if (mapA.containsKey(s.charAt(i)) && mapA.get(s.charAt(i)) != t.charAt(i)) {
                return false;
            }
            mapA.put(s.charAt(i), t.charAt(i));
            if (mapB.containsKey(t.charAt(i)) && mapB.get(t.charAt(i)) != s.charAt(i)) {
                return false;
            }
            mapB.put(t.charAt(i), s.charAt(i));
        }
        return true;
    }

    /**
     * 46 * 单词规律 双映射 哈希表
     * <a href="https://leetcode.cn/problems/word-pattern/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public boolean wordPattern(String pattern, String s) {
        String[] words = s.split("\\s+");
        if (pattern.length() != words.length) {
            return false;
        }
        HashMap<Character, String> mapA = new HashMap<>();
        HashMap<String, Character> mapB = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            if (mapA.containsKey(pattern.charAt(i))) {
                if (!Objects.equals(mapA.get(pattern.charAt(i)), words[i])) {
                    return false;
                }
            }
            mapA.put(pattern.charAt(i), words[i]);

            if (mapB.containsKey(words[i])) {
                if (!Objects.equals(mapB.get(words[i]), pattern.charAt(i))) {
                    return false;
                }
            }
            mapB.put(words[i], pattern.charAt(i));
        }
        return true;
    }

    /**
     * 47 * 有效的字母异位词
     * <a href="https://leetcode.cn/problems/valid-anagram/description/?envType=study-plan-v2&envId=top-interview-150">...</a>
     */
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        HashMap<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            map.put(s.charAt(i), map.getOrDefault(s.charAt(i), 0) + 1);
        }
        for (int i = 0; i < t.length(); i++) {
            map.put(t.charAt(i), map.getOrDefault(t.charAt(i), 0) - 1);
            if (map.get(t.charAt(i)) == 0) {
                map.remove(t.charAt(i));
            }
        }
        return map.isEmpty();
    }
}
