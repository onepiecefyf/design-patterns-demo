package com.newcoder.algorithm.biz.random;

import java.util.Random;

public class RandomSection {


  public static void main (String[] args) {

    Random random = new Random();

//    for(int i = 0; i < 100; i ++) {
//      int r = random.nextInt(10) + 1;
//      System.out.println(r * 0.01);
//    }

    for(int i = 0; i < 100; i ++) {
      double r = random.nextDouble();
      System.out.println(r);
    }



  }



}
