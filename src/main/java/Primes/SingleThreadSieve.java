package Primes;

import java.util.Arrays;
import java.util.function.IntConsumer;

public class SingleThreadSieve {
    private int maxNum;
    private int[] numbers;

    //Store vals/just print?
    public SingleThreadSieve(int maxNum){
        this.maxNum = maxNum;
        numbers = new int[maxNum-1];
        for(int i=0;i<numbers.length;i++)
            numbers[i]=i+2;
    }

    public void printNums(){
        int pi=-1, p;
        while(pi<numbers.length-1){
            pi++;
            p = numbers[pi];

            if(numbers[pi]==-1)
                continue;
            for(int i=pi+1;i<numbers.length;i++){
                if(numbers[i]!=-1&&numbers[i]%p==0)
                    numbers[i]=-1;
            }
        }
        Arrays.stream(numbers).forEach(i -> {
            if(i!=-1)
                System.out.print(i+" ");
        });
        System.out.println();
    }

    public static void run(int num){
        SingleThreadSieve s = new SingleThreadSieve(100);
        s.printNums();
    }
}
