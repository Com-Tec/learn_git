package com.java.g.a.w01;

/**
 * 合并两个有序链表
 *
 * 将两个升序链表合并为一个新的 升序 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
 */
public class Solution21 {

    public static void main(String[] args) {
        ListNode l1Last=new ListNode(4,null);
        ListNode l12=new ListNode(2,l1Last);
        ListNode l1=new ListNode(1,l12);


        ListNode l2Last=new ListNode(4,null);
        ListNode l22=new ListNode(3,l2Last);
        ListNode l2=new ListNode(1,l22);

        ListNode head= mergeTwoLists(l1,l2);
        while(head !=null){
            System.out.println(head.val);
            head=head.next;
        }


    }

    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {

        ListNode head1=l1;
        ListNode head2=l2;

        int minVal=l1.val>l2.val?l2.val:l1.val;
        ListNode protectNode=new ListNode(minVal-10);

        ListNode head =null;

        while(head1 !=null && head2 !=null){

                if( head1.val >head2.val){
                    ListNode nextHead2=head2.next;

                    if(head==null){
                        head=head2;
                        protectNode.next=head;
                    }else {
                        head.next = head2;
                        head=head.next;
                    }

                    head2=nextHead2;

                }else if(head1.val<=head2.val){
                   ListNode nextHead1=head1.next;
                   if(head==null){
                       head=head1;
                       protectNode.next=head;
                   }else{
                       head.next=head1;
                       head=head.next;
                   }
                   head1=nextHead1;
                }
        }
        if(head1==null){
            head.next=head2;

        }
        if(head2==null){
            head.next=head1;
        }


        return protectNode.next;
    }




}
