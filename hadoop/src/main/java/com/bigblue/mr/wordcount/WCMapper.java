package com.bigblue.mr.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/4/12
 */
public class WCMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private Text text = new Text();
    private IntWritable one = new IntWritable(1);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] words = value.toString().split(" ");
        for (String word : words) {
            //为了防止map创建大量对象导致经常GC，应该提出来这两个new
//            context.write(new Text(word), new IntWritable(1));
            text.set(word);
            context.write(text, one);
        }
    }
}
