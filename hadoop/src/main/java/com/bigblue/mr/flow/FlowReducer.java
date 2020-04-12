package com.bigblue.mr.flow;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/4/12
 */
public class FlowReducer extends Reducer<Text, FlowBean, Text, FlowBean> {

    private FlowBean flowBean = new FlowBean();

    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        long upFlow = 0;
        long downFlow = 0;
        for (FlowBean bean : values) {
            upFlow += bean.getUpFlow();
            downFlow += bean.getDownFlow();
        }
        flowBean.set(upFlow, downFlow);
        context.write(key, flowBean);
    }
}
