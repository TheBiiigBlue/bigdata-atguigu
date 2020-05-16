package com.bigblue.hive.interceptor;

import com.bigblue.hive.utils.LogUtil;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/5/14
 */
public class LogETLInterceptor implements Interceptor {
    @Override
    public void initialize() {

    }

    //单event处理
    @Override
    public Event intercept(Event event) {
        String text = new String(event.getBody(), Charset.forName("UTF-8"));
        if(text != null && text.length() > 0){
            if(text.contains("start")){
                if(LogUtil.validateStart(text)){
                    return event;
                }
            }else {
                if(LogUtil.validateEvent(text)){
                    return event;
                }
            }
        }
        return null;
    }

    //多event处理
    @Override
    public List<Event> intercept(List<Event> list) {
        List<Event> eventList = new ArrayList<>();
        //取出合格的数据返回
        for (Event event : list) {
            eventList.add(intercept(event));
        }
        return eventList;
    }

    @Override
    public void close() {

    }

    public static class Builder implements Interceptor.Builder {

        @Override
        public Interceptor build() {
            return new LogETLInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
