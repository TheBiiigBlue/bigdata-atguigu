package com.bigblue.hive.etl;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/5/4
 */
public class ETLUtil {

    /**
     * 过滤长度小于9个字段的
     * 去掉类别字段中的空格
     * 修改相关视频id字段的分隔符，由`\t`替换为`&`
     * @param oriStr
     * @return
     */
    public static String filterStr(String oriStr) {
        String[] strArr = oriStr.split("\t");
        StringBuffer sb = new StringBuffer();
        if(strArr.length < 9){
            return null;
        }
        strArr[3] = strArr[3].replace(" ", "");
        for (int i = 0; i < strArr.length; i++) {
            sb.append(strArr[i]);
            if(i < 9){
                //如果只有9个元素，则最后一个元素不加\t
                if(i != strArr.length - 1){
                    sb.append("\t");
                }
            }else {
                if(i != strArr.length - 1){
                    sb.append("&");
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String etlStr = filterStr("SDNkMu8ZT68\tw00dy911\t630\tPeople & Blogs\t186\t10181\t3.49\t494\t257\trjnbgpPJUks");
        System.out.println(etlStr);
    }
}
