public class Utils {

    /**
     * 获取数组最小值对应下标
     * @param arr
     * @return
     */
    public static int getMinIndex(int[] arr) {
        int minIndex = 0;
        int minValue = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < minValue) {
                minValue = arr[i];
                minIndex = i;
            }
        }
        return minIndex;
    }
}
