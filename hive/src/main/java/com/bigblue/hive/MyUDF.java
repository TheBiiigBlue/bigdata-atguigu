package com.bigblue.hive;

import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/5/2
 */
public class MyUDF extends UDF {

    public String evaluate(String str) {
        if(str == null || str.trim() == ""){
            return null;
        }
        return str.toLowerCase();
    }

    public int evaluate(int a, int b) {
       return a + b + 5;
    }

}
