package com.bigblue.mr.wordcount;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/4/12
 */
public class WCDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //获取job实例
        Job job = Job.getInstance();
        //设置类路径classpath
        job.setJarByClass(WCDriver.class);
        //设置mapper和reducer
        job.setMapperClass(WCMapper.class);
        job.setReducerClass(WCReducer.class);
        //设置mapper和reducer的输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        //设置输入输出数据
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        //提交job
        boolean result = job.waitForCompletion(true);
        System.out.println(result ? 0 : 1);
    }
}
