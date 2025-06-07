public class Main {

    public static void main(String[] args) {
        LeetCode leetCode = new LeetCode();
        LeetCode.ListNode head = leetCode.new ListNode(9);
//        head.next = leetCode.new ListNode(4);
//        head.next.next = leetCode.new ListNode(3);

        LeetCode.ListNode rear = leetCode.new ListNode(1);
        rear.next = leetCode.new ListNode(9);
        rear.next.next = leetCode.new ListNode(9);
        rear.next.next.next = leetCode.new ListNode(9);
        rear.next.next.next.next = leetCode.new ListNode(9);
        rear.next.next.next.next.next = leetCode.new ListNode(9);
        rear.next.next.next.next.next.next = leetCode.new ListNode(9);
        rear.next.next.next.next.next.next.next = leetCode.new ListNode(9);
        rear.next.next.next.next.next.next.next.next = leetCode.new ListNode(9);
        rear.next.next.next.next.next.next.next.next.next = leetCode.new ListNode(9);

        System.out.println(leetCode.addTwoNumbers(head, rear));
    }


}