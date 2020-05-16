package com.bigblue.hive.interceptor;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/5/14
 */
public class LogTypeInterceptor implements Interceptor {
    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        String log = new String(event.getBody(), Charset.forName("UTF-8"));
        event.getHeaders().put("topic", log.contains("start") ? "topic_start" : "topic_event");
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> list) {
        return list.stream().map(event -> intercept(event)).collect(Collectors.toList());
    }

    @Override
    public void close() {

    }

    public static class Builder implements Interceptor.Builder {

        @Override
        public Interceptor build() {
            return new LogTypeInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
