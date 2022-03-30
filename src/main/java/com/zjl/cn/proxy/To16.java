package com.zjl.cn.proxy;

import cn.hutool.core.util.StrUtil;

/**
 * @Description:
 * @Author: zjl
 * @Date:Created in 2022/3/27 15:56
 * @Modified By:
 */
public class To16 {

    /**
     * 初始化 62 进制数据，索引位置代表转换字符的数值 0-61，比如 A代表10，z代表61
     */
    private static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * 进制转换比率
     */
    private static final int SCALE = 62;

    /**
     * 匹配字符串只包含数字和大小写字母
     */
    private static final String REGEX = "^[0-9a-zA-Z]+$";

    /**
     * 十进制数字转为62进制字符串
     *
     * @param val    十进制数字
     * @param length 输出字符串长度
     * @return 62进制字符串
     */
    public static String encode10To62(long val, int length)
    {
        String to62 = encode10To62(val);
        System.out.println(to62);
//        return StrUtil.leftPad(encode10To62(val), length, '0');
        return StrUtil.fillBefore(to62, '0', length);
    }

    /**
     * 十进制数字转为62进制字符串
     *
     * @param val 十进制数字
     * @return 62进制字符串
     */
    public static String encode10To62(long val)
    {
        if (val < 0)
        {
            throw new IllegalArgumentException("this is an Invalid parameter:" + val);
        }
        StringBuilder sb = new StringBuilder();
        int remainder;
        while (Math.abs(val) > SCALE - 1)
        {
            //从最后一位开始进制转换，取转换后的值，最后反转字符串
            remainder = Long.valueOf(val % SCALE).intValue();
            sb.append(CHARS.charAt(remainder));
            val = val / SCALE;
        }
        //获取最高位
        sb.append(CHARS.charAt(Long.valueOf(val).intValue()));
        return sb.reverse().toString();
    }

    public static void main(String[] args) {
        String to62 = encode10To62(100, 6);
        System.out.println(to62);


        long to10 = decode62To10(to62);
        System.out.println(to10);

    }

    /**
     * 十进制数字转为62进制字符串
     *
     * @param val 62进制字符串
     * @return 十进制数字
     */
    public static long decode62To10(String val)
    {
        if (val == null)
        {
            throw new NumberFormatException("null");
        }
        if (!val.matches(REGEX))
        {
            throw new IllegalArgumentException("this is an Invalid parameter:" + val);
        }
        String tmp = val.replace("^0*", "");

        long result = 0;
        int index = 0;
        int length = tmp.length();
        for (int i = 0; i < length; i++)
        {
            index = CHARS.indexOf(tmp.charAt(i));
            result += (long)(index * Math.pow(SCALE, length - i - 1));
        }
        return result;
    }

}
