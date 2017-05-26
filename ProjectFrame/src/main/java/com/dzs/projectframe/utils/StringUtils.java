package com.dzs.projectframe.utils;

import android.support.annotation.NonNull;

import com.dzs.projectframe.base.ProjectContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.1
 * @date 2015-12-23 上午9:53:00
 */
public class StringUtils {

    private final static Pattern EMAIL = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static Pattern PHONE = Pattern.compile("^[1][3,4,5,8][0-9]{9}$");
    private final static Pattern IMG_URL = Pattern.compile(".*?(gif|jpeg|png|jpg|bmp)");
    private final static Pattern URL = Pattern.compile("^(https|http)://.*?$(net|com|.com.cn|org|me|)");
    public static final String UTF_8 = "UTF-8";

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(CharSequence email) {
        if (isEmpty(email)) return false;
        return EMAIL.matcher(email).matches();
    }

    /**
     * 判断手机号
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(CharSequence phone) {
        if (isEmpty(phone)) {
            ToastUtils.showOneToast("手机号不能为空");
            return false;
        }
        if (PHONE.matcher(phone).matches()){
            return true;
        }else{
            ToastUtils.showOneToast("请输入正确的手机号");
            return false;
        }
    }

    /**
     * 判断字符串是否为空
     *
     * @param input
     * @return
     */
    public static boolean isEmpty(CharSequence input) {
        if (input == null || input.length() == 0) return true;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /*判断字符串数组是否为空*/
    public static boolean isEmpty(CharSequence... input) {
        if (input == null || input.length == 0) return true;
        for (CharSequence in : input) {
            if (isEmpty(in)) return true;
        }
        return true;
    }

    /**
     * 判断是否为url
     *
     * @param str
     * @return
     */
    public static boolean isUrl(CharSequence str) {
        if (isEmail(str)) return false;
        return URL.matcher(str).matches();
    }

    /**
     * 判断是否是图片url
     *
     * @param url
     * @return
     */
    public static boolean isImgUrl(CharSequence url) {
        if (isEmpty(url)) return false;
        return IMG_URL.matcher(url).matches();
    }

    /**
     * 字符串转换为Unicode编码
     *
     * @param s
     * @return
     */
    public static String stringToUnicode(String s) {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            if (ch > 255) {
                str.append("\\").append("u").append(Integer.toHexString(ch));
            } else {
                str.append("\\").append(Integer.toHexString(ch));
            }
        }
        return str.toString();
    }

    /**
     * 将网络请求Map转换为url
     *
     * @param url
     * @param parmas
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String mapToUrl(String url, Map<String, Object> parmas) throws UnsupportedEncodingException {
        return mapToCatchUrl(url, parmas);
    }

    /**
     * 将网络请求Map转换成为缓存KEY，去掉可变量
     *
     * @param url         URL
     * @param parmas      请求参数
     * @param variableKey 可变的键值(可以为空)
     * @throws UnsupportedEncodingException
     */
    public static String mapToCatchUrl(String url, Map<String, Object> parmas, String... variableKey) throws UnsupportedEncodingException {
        StringBuilder getUrl = new StringBuilder(url);
        if (parmas != null && !parmas.isEmpty()) {
            getUrl.append("?");
            for (Map.Entry<String, Object> parm : parmas.entrySet()) {
                if (variableKey != null) {
                    for (String key : variableKey) {
                        if (parm.getKey().equals(key)) break;
                    }
                }
                getUrl.append(parm.getKey()).append("=").append(URLEncoder.encode(parm.getValue() == null ? "" : parm.getValue().toString(), UTF_8));
                getUrl.append("&");
            }
            getUrl.deleteCharAt(getUrl.length() - 1);
        }
        return getUrl.toString();
    }

    /**
     * 将map请求参数转换为get参数
     *
     * @param parameters
     * @return
     * @throws UnsupportedEncodingException
     */
    public static StringBuilder mapToParmeters(Map<String, Object> parameters) throws UnsupportedEncodingException {
        if (parameters == null || parameters.isEmpty()) return new StringBuilder("");
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> parm : parameters.entrySet()) {
            sb.append(parm.getKey()).append("=").append(URLEncoder.encode(parm.getValue() == null ? "" : parm.getValue().toString(), UTF_8));
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb;
    }


    /**
     * 获取assets目录下的城市列表
     *
     * @return
     */
    public static String convertStreamToString() {
        StringBuilder sb = new StringBuilder();
        String line;
        InputStream is = null;
        try {
            is = ProjectContext.appContext.getAssets().open("province.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    /**
     * 判断密码是否包含中文
     *
     * @param password 密码字符串
     */
    public static boolean isContainChinese(@NonNull String password) {
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

}
