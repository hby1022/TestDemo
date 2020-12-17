/* =============================================================================================
 * 字符串处理类
 * ==============================================================================================
 *
 * --------------------------------------
 * StringUtil.java
 * --------------------------------------
 *
 * Original Author: ecif-chenpeng
 * Create Date: 2013/03/08
 */

package com.transpeed.park.util;



public class StringUtil  {

    /**
     * 判断传入的参数是否为空
     * @param str 可变长度参数
     * @return
     */
    public static boolean testIsNull(String... strs){
        for (String str : strs) {
            if (("").equals(str) || null == str) {
                return true;
            }
        }
        return false;
    }

}
