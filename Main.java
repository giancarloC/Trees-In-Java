/*
Code to test
*/

import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

public class Main{

  //function that returns a n-array of random numbers
  public static int[] getRandomArray(int n){
    //creates array to be returned
    int[] arr = new int[n];

    //creates random generator
    Random rand = new Random();

    //creates hash set and adds random numbers to it until set is size n
    Set<Integer> unique = new HashSet<Integer>(n);
    while(unique.size() < n){
      unique.add(rand.nextInt());
    }

    //adds numbers in set to array
    int i = 0;
    for(int num: unique){
      arr[i] = num;
      i++;
    }

    return arr;
  }

  //function that returns a n-array of numbers: n, n-1, n-2,..., 1
  public static int[] getSortedArray(int n){
    //creates array to be returned
    int[] arr = new int[n];

    //adds numbers in set to array
    int i;
    for(i = 0; i < n; i++){
      arr[i] = n-i;
    }

    return arr;
  }

  //main method to test
  public static void main(String arg[]){
    //creates BST
    BST b = new BST();
    AVL a = new AVL();

    //creates random array of size n
    int n = 10000;
    int[] arr = getRandomArray(n);

    //inserts into BST
    int i;
    for(i = 0; i < n; i++){
      b.insertRec(arr[i]);
    }

    b.print();

  }

}
