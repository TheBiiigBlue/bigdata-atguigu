package com.bigblue.mr.flow;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/4/12
 */
public class FlowMapper extends Mapper<LongWritable, Text, Text, FlowBean> {

    private Text phoneNum = new Text();
    private FlowBean bean = new FlowBean();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split("\t");
        phoneNum.set(split[1]);
        bean.set(Long.parseLong(split[split.length - 3]), Long.parseLong(split[split.length - 2]));
        context.write(phoneNum, bean);
    }
}
