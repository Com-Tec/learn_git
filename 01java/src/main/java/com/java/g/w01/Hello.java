package com.java.g.w01;

public class Hello {
    public static void main(String[] args) {
        //  System.out.println("hello class main() executed.");
        String s1="5";
        String s2=new String("5");
        int i1=5;
        int i2=0;
        while(i2!=5){
            i1=i1+Integer.valueOf(s1+s2);
            i2++;
            System.out.println("---"+i2);
        }
    }
}
