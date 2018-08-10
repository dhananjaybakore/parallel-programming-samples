package com.dbakore.parprog.forkjoin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class MainRunner implements CommandLineRunner{
    private static ForkJoinPool myPool = new ForkJoinPool(6);
    private static final Logger log = LoggerFactory.getLogger(MainRunner.class);
    private static final int COUNT = 80000000;



    @Override
    public void run(String... args) throws Exception {
        // generateSampleInputData();

        double [] inputarr = getInputArray();
        long startTimeAsync = System.nanoTime();
        double ansAsync = parallelSumAsync(inputarr);
        long durationAsync = System.nanoTime() - startTimeAsync;
        logmessage("Parallel Version : ",ansAsync, durationAsync);


        long startTime = System.nanoTime();
        double ans = parallelSum(inputarr);
        long duration = System.nanoTime() - startTime;
        logmessage("Sync Version : ",ans, duration);

    }

    private double [] getInputArray() {
        String line="";
        double[] arr = new double[COUNT];
        int i=0;
        try(BufferedReader br = new BufferedReader(new FileReader("numbers.txt"))){
            while ((line = br.readLine()) != null) {
                arr[i]=Double.parseDouble(line);
                i++;
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

        return arr;

    }

    private static void generateSampleInputData() {
        try (FileWriter writer = new FileWriter(new File("numbers.txt"))){
            for(int i=0; i< COUNT; i++){
                Integer number = ThreadLocalRandom.current().nextInt(1,100);
                writer.write(number.toString());
                writer.write(System.getProperty("line.separator"));
            }

        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    private static void logmessage(String s, double ans, long l) {
        double duration = l / 1000000;
        log.info(s+" Answer: "+ans+" Time(ms): "+duration);
    }


    private static double parallelSumAsync(double[] arr){
        ParallelInverseSum parallelSumTask = new ParallelInverseSum(arr,0,arr.length);
        myPool.invoke(parallelSumTask);
        //ForkJoinPool.commonPool().invoke(parallelSumTask);
        return parallelSumTask.getAns();
    }

    private static double parallelSum(double [] arr){
        double ans=0;
        for(int i =0; i< arr.length; i++){
            ans+= 1/arr[i];
        }
        return ans;
    }


}

