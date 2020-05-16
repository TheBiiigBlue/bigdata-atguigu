package com.bigblue.hive.utils;

import org.apache.commons.lang.math.NumberUtils;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/5/14
 */
public class LogUtil {

    // {"action":"1","ar":"MX","ba":"HTC","detail":"542","en":"start",
    // "entry":"2","extend1":"","g":"S3HQ7LKM@gmail.com","hw":"640*960",
    // "l":"en","la":"-43.4","ln":"-98.3","loading_time":"10","md":"HTC-5",
    // "mid":"993","nw":"WIFI","open_ad_type":"1","os":"8.2.1","sr":"D",
    // "sv":"V2.9.0","t":"1559551922019","uid":"993","vc":"0","vn":"1.1.5"}
    public static boolean validateStart(String text) {
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = JSONObject.parseObject(text);
//        }catch (Exception e) {
//            return false;
//        }
//        return jsonObject == null ? false : true;

        // 校验json
        if (!text.trim().startsWith("{") || !text.trim().endsWith("}")){
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String text = "{\"action\":\"1\",\"ar\":\"MX\",\"ba\":\"HTC\",\"detail\":\"\",\"en\":\"start\",\"entry\":\"3\",\"extend1\":\"\",\"g\":\"04FP1ZFK@gmail.com\",\"hw\":\"640*960\",\"l\":\"pt\",\"la\":\"-13.2\",\"ln\":\"-94.6\",\"loading_time\":\"11\",\"md\":\"HTC-0\",\"mid\":\"4\",\" nw\":\"4G\",\"open_ad_type\":\"2\",\"os\":\"8.1.6\",\"sr\":\"O\",\"sv\":\"V2.3.9\",\"t\":\"1589308880446\",\"uid\":\"4\",\"vc\":\"19\",\"vn\":\"1.2.1\"}";
        System.out.println(validateStart(text));

        String text2 = "1583769623716|{\"cm\":{\"ln\":\"-47.1\",\"sv\":\"V2.8.5\",\"os\":\"8.1.8\",\"g\":\"0V4RX69Q@gmail.com\",\"mid\":\"999\",\"nw\":\"WIFI\",\"l\":\"es\",\"vc\":\"18\",\"hw\":\"640*1136\",\"ar\":\"MX\",\"uid\":\"999\",\"t\":\"1583753599996\",\"la\":\"4.9\",\"md\":\"HTC-6\",\"vn\":\"1.0.7\",\"ba\":\"HTC\",\"sr\":\"T\"},\"ap\":\"app\",\"et\":[{\"ett\":\"1583709506038\",\"en\":\"newsdetail\",\"kv\":{\"entry\":\"3\",\"goodsid\":\"245\",\"news_staytime\":\"5\",\"loading_time\":\"6\",\"action\":\"1\",\"showtype\":\"1\",\"category\":\"25\",\"type1\":\"201\"}},{\"ett\":\"1583682839723\",\"en\":\"ad\",\"kv\":{\"activityId\":\"1\",\"displayMills\":\"65627\",\"entry\":\"3\",\"action\":\"5\",\"contentType\":\"0\"}},{\"ett\":\"1583733307843\",\"en\":\"active_background\",\"kv\":{\"active_source\":\"2\"}},{\"ett\":\"1583675223346\",\"en\":\"comment\",\"kv\":{\"p_comment_id\":4,\"addtime\":\"1583743421850\",\"praise_count\":980,\"other_id\":4,\"comment_id\":7,\"reply_count\":146,\"userid\":7,\"content\":\"尺导忌赃蠕慕豹然\"}}]}";
        System.out.println(validateEvent(text2));
    }

    // 服务器时间 | json
    // 1549696569054 | {"cm":{"ln":"-89.2","sv":"V2.0.4","os":"8.2.0","g":"M67B4QYU@gmail.com",
    // "nw":"4G","l":"en","vc":"18","hw":"1080*1920","ar":"MX","uid":"u8678","t":"1549679122062",
    // "la":"-27.4","md":"sumsung-12","vn":"1.1.3","ba":"Sumsung","sr":"Y"},"ap":"weather","et":[]}
    public static boolean validateEvent(String text) {
        String[] logContents = text.split("\\|");
        if(logContents == null || logContents.length != 2){
            return false;
        }
        //校验服务器时间
        if(logContents[0].length() != 13 || !NumberUtils.isDigits(logContents[0])){
            return false;
        }
        //校验json
//        return validateStart(text);
        return validateStart(logContents[1]);
    }
}
