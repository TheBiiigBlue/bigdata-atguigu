package com.bigblue.hive.etl;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/5/4
 */
public class ETLMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    private Text v = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //获取数据
        String oriStr = value.toString();

        //过滤数据
        String etlStr = ETLUtil.filterStr(oriStr);

        //写出数据
        if(etlStr != null){
            v.set(etlStr);
            context.write(NullWritable.get(), v);
        }
    }
}
