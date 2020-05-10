package com.bigblue.hive.etl;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/5/4
 */
public class ETLDriver implements Tool {

    private Configuration configuration;

    public int run(String[] args) throws Exception {
        //获取job对象
        Job job = Job.getInstance(configuration);
        //设置jar包路径
        job.setJarByClass(ETLDriver.class);

        //设置mapper类和输出kv类型
        job.setMapperClass(ETLMapper.class);

        //设置最终输出的kv类型
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Text.class);

        //设置输入输出的路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        //提交任务
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public void setConf(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConf() {
        return this.configuration;
    }

    public static void main(String[] args) {
        try {
            int stat = ToolRunner.run(new Configuration(), new ETLDriver(), args);
            System.out.println(stat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
