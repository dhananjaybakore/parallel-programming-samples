package com.dbakore.parprog.forkjoin;

import java.util.concurrent.RecursiveAction;

public class ParallelInverseSum extends RecursiveAction {
    private static int SEQ_THRESHOLD = 1000000;

    private int low;
    private int high;
    double [] input;
    double ans = 0;


    public ParallelInverseSum(double [] input, int low, int high){
        this.input = input;
        this.low = low;
        this.high = high;
    }
    @Override
    protected void compute() {
        // If seq threshold process else divide and conquer and split
        if(high-low <= SEQ_THRESHOLD){
            for(int i=low; i< high ; i++){
                ans += 1/input[i];
            }
        }else{
            ParallelInverseSum left = new ParallelInverseSum(input, low, (high+low)/2);
            ParallelInverseSum right = new ParallelInverseSum(input,(high+low)/2, high);
            left.fork();
            right.compute();
            left.join();
            ans = left.ans + right.ans;
        }
    }

    public double getAns() {
        return ans;
    }

}
