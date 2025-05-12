import java.util.Scanner;

public class LeetCode {


    /**
     * 字母分块反转
     */
    public static void test1() {
        Scanner scanner = new Scanner(System.in);
        String inputStr = scanner.nextLine();
        int pack = scanner.nextInt();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < inputStr.length(); i = i + pack) {
            str.append(reverseString(inputStr.substring(i, Math.min(i + pack, inputStr.length()))));
        }
        System.out.println(str);
    }

    public static String reverseString(String str) {
        char[] arr = str.toCharArray();
        int left = 0;
        int right = arr.length - 1;
        while (left < right) {
            char temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
        return new String(arr);
    }

    /**
     *  检验地址是否相等
     */
    public static void test2() {
        String a = new String("MIPhone");
        String b = "MIPhone";
        String c = "MI" + "Phone";
        String d = c;
        System.out.print(a == b);
        System.out.print(a == c);
        System.out.print(b == c);
        System.out.print(b == d);
    }
}
