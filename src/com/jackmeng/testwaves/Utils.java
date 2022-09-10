package com.jackmeng.testwaves;

import java.util.concurrent.Callable;

public final class Utils {
  public static int rng(int min, int max) {
    return (int) (Math.random() * (max - min)) + min;
  }

  public static int limg(int i, int lim) {
    return i > lim ? lim : i;
  }

  public static int liml(int i, int lim) {
    return i < lim ? lim : i;
  }

  public static int[] fillArr(int[] arr, Callable<Integer> e) {
    for (int i = 0; i < arr.length; i++) {
      try {
        arr[i] = e.call();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
    return arr;
  }
}
