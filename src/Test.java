import java.util.Scanner;

public class Test {
    /**
     * 字母分块反转
     */
    public void test1() {
        Scanner scanner = new Scanner(System.in);
        String inputStr = scanner.nextLine();
        int pack = scanner.nextInt();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < inputStr.length(); i = i + pack) {
            str.append(reverseString(inputStr.substring(i, Math.min(i + pack, inputStr.length()))));
        }
        System.out.println(str);
    }

    /*
     * //
     * /*
     * @param str
     * @return
     */
    public String reverseString(String str) {
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
     * 检验地址是否相等
     */
    public void test2() {
        String a = new String("MIPhone");
        String b = "MIPhone";
        String c = "MI" + "Phone";
        String d = c;
        System.out.print(a == b);
        System.out.print(a == c);
        System.out.print(b == c);
        System.out.print(b == d);
    }

    /**
     * 静态方法与普通方法继承与多态
     */
    public void test3() {
        Animal animal = new Animal();
        Animal dog = new Dog();
        animal.run();
        dog.run();
        Animal.eat();
        animal.eat();
        dog.eat();
    }

    public class Animal {

        public static void eat() {
            System.out.println("Animal eat");
        }

        public void run() {
            System.out.println("Animal Running");
        }

    }

    public class Dog extends Animal {

        public static void eat() {
            System.out.println("Dog eat");
        }

        public void run() {
            System.out.println("Dog Running");
        }

    }

    /**
     * 引用与对象
     */
    public void test4() {
        StringBuffer a = new StringBuffer("A");
        StringBuffer b = new StringBuffer("B");
        operate(a, b);
        System.out.println(a + "." + b);
    }

    private void operate(StringBuffer x, StringBuffer y) {
        x.append(y);
        y = x;
    }

    public void test5() {
        Object o = new Object() {
            public boolean equals(Object obj) {
                return true;
            }
        };
        System.out.println(o.equals("Fred"));
    }
}
