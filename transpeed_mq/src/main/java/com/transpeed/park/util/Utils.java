package com.transpeed.park.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

@Slf4j
public class Utils {
	 @Autowired
	 Environment env;
	 static Gson gson=new Gson();

//	String base64_led=new Base64().encodeToString(ledContent.getBytes("UTF-8"));

	public static String strConvertBase(String str) {
		if(null != str){
			Base64.Encoder encoder = Base64.getEncoder();
			return encoder.encodeToString(str.getBytes());
		}
		return null;
	}

	public static String baseConvertStr(String str) {
		if(null != str){
			Base64.Decoder decoder = Base64.getDecoder();
			try {
				return new String(decoder.decode(str.getBytes()), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		}
		return null;
	}


	 public final static boolean isJSONValid2(String jsonInString ) {
	        try {
	            final ObjectMapper mapper = new ObjectMapper();
	            mapper.readTree(jsonInString);
	            return true;
	        } catch (IOException e) {
	            return false;
	        }
	    }


	 public final static boolean isJSONValid3(String jsonInString) {
	        try {
	            gson.fromJson(jsonInString, Object.class);
	            return true;
	        } catch(JsonSyntaxException ex) {
	            return false;
	        }
	    }


public static boolean isjson(String str){
     try {
        JSONObject jsonStr= JSONObject.fromObject(str);
        return true;
      } catch (Exception e) {
        return false;
        }
}


	// 创建文件夹
	// 创建目录
	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {// 判断目录是否存在
			return false;
		}
		if (dir.mkdirs()) {// 创建目标目录
			return true;
		} else {
			return false;
		}
	}

	// 配置文件读取
	// 读取配置文件
	public static String readProperties(String str) {
		try {
			// 输入流加载配置文件
			String result = null;
//			InputStream in = Utils.class.getClassLoader().getResourceAsStream(
//					"whitelist.properties");
			log.debug("classpath:application.properties路径："+ResourceUtils.getFile("classpath:application.properties"));
			InputStream in = new FileInputStream(ResourceUtils.getFile("classpath:application.properties"));
			Properties properties = new Properties();
			properties.load(in);
			// 获取参数值
			result = properties.getProperty(str);
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	// 时间戳转换
	public static String TimeStamp2Date(String timestampString) {
		Long timestamp = Long.parseLong(timestampString) * 1000;
		String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date(timestamp));
		return date;
	}

	// 停车场号获取
	public static Integer ParkIDObtain(String ULID) {
		return Integer.parseInt(ULID.substring(0, ULID.length() - 3));
	}

	/**
	 * @description: 判断字符串是否为空 空 true 不空 false
	 */

	/**
	 * 检查字符是否为 null || "" 如果是 null 或者 "" 则返回true ,反则为 false
	 *
	 * @param methodName
	 *            调用方法名称 (哪个方法使用此验证)
	 * @param value
	 *            需要验证的多个字符，以英文逗号分隔
	 * @return
	 */
	public static boolean checkStringIsNull(String methodName, String... value) {
		int count = 0;
		for (int i = 0; i < value.length; i++) {
			// Log.debug("key的值:"+value+"  ,value的值是："+value[i]);
			if (StringIsEmpty(value[i])) {
				log.debug(methodName + "该方法传入参数存在空值！");
				return true;
			}
			count++;
		}
		if (count == value.length) {
			return false;
		}
		return true;
	}

	public static boolean isEmptyBatch(String... strs) {
		for (String str : strs) {
			if (str == null || "".equals(str))
				return true;
		}
		return false;
	}

	public static boolean StringIsEmpty(String str) {
		return null == str || str.length() <= 0;
	}

	public static boolean ObjectIsEmpty(Object obj) {
		return obj == null || obj.toString().length() == 0;
	}


	public static boolean isNotEmptyBatch(String... strs) {
		for (String str : strs) {
			if (str == null || "".equals(str))
				return false;
		}
		return true;
	}
    /**
     * 判断是否含有特殊字符
     *
     * @param str
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

	/**
	 * @description: 金额单位从元转为分
	 */

	public static String getMoneyToPoints(String money) {
		double a = Double.valueOf(money) * 100;
		int i = (new Double(a)).intValue();
		return String.valueOf(i);
	}

	/**
	 * @description: 金额单位从分转为元
	 * @version: V1.0
	 */

	public static String getMoneyToYuan(String TotalAmount) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(Double.parseDouble(TotalAmount) / 100);
	}

	// 无小数
	public static String getMoneyToYuanWithoutDecimal(String TotalAmount) {
		DecimalFormat df = new DecimalFormat("0");
		return df.format(Double.parseDouble(TotalAmount) / 100);
	}




	//生成指定长度的随机数
	 public static String randomNum(int num){
	        StringBuilder str=new StringBuilder();//定义变长字符串
	        Random random=new Random();
	        for (int i = 0; i < num; i++) {
	            str.append(random.nextInt(10));
	        }
	        return str.toString();
	    }

	// // application/x-www-form-urlencoded ==>> Map<String, String>
	// public static Map<String, String> urlencodedToMap(String msg) throws
	// UnsupportedEncodingException {
	// // 使用TreeMap保证字典排序
	// Map<String, String> map = new TreeMap<>();
	// String[] temp = urldecoded( msg ).split( "&" );
	// for (String anArray : temp) {
	// String[] list = anArray.split( "=", 2 );//限制有些value中包含=号会被分割
	// map.put( list[0], list[1] );
	// }
	// return map;
	// }


	// Map<String, String> ==> application/x-www-form-urlencoded
	public static StringBuilder mapToUrlencoded(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		for (String key : map.keySet()) {
			sb.append(key).append("=").append(map.get(key)).append("&");
		}
		// 删除最后一个&号
		sb.deleteCharAt(sb.toString().length() - 1);
		return sb;
	}

	// Map<String, String> ==> application/x-www-form-urlencoded 过滤相关字符
	public static StringBuilder mapToUrlencoded(Map<String, String> map,
			String filter) {
		map.remove(filter);
		return mapToUrlencoded(map);
	}


		public static StringBuilder mapToUrlencodedObject(Map<String, Object> map) {
			StringBuilder sb = new StringBuilder();
			for (String key : map.keySet()) {
				sb.append(key).append("=").append(map.get(key)).append("&");
			}
			sb.deleteCharAt(sb.toString().length() - 1);
			return sb;
		}
//		public static StringBuilder mapToUrlencodedObject(Map<String, Object> map,
//				String filter) {
//			map.remove(filter);
//			return mapToUrlencodedObject(map);
//		}

		public static StringBuilder mapToUrlencodedObject(Map<String, Object> map,
				String... filter) {
			log.debug("过滤字段长度："+filter.length);
			for (int i = 0; i < filter.length; i++) {
				log.debug("过滤字段长度："+filter[i]);
				map.remove(filter[i]);
			}
			return mapToUrlencodedObject(map);
	    }




	// map 进行有序排列
	// orderby 字典排序 true 字符原来顺序 false
	public static Map<String, String> mapOrderBy(Map<String, String> map,
			boolean orderby) {
		// 使用TreeMap保证字典排序
		Map<String, String> map_temp = orderby ? new TreeMap<String, String>()
				: new LinkedHashMap<String, String>();
		for (String key : map.keySet()) {
			map_temp.put(key, map.get(key));
		}
		return map_temp;
	}


	public static String getFileEncoding() {
		return System.getProperty("file.encoding");
	}

	// UTF-8 =>> GBK
	public static String utf_8ToGBK(String src)
			throws UnsupportedEncodingException {
		return URLEncoder.encode(src, "UTF-8").replace("+", "%20");
	}

	// 科学计数法转字符串
	public static String ScientificNotationToString(String ScientificNotationStr) {
		BigDecimal bigDecimal = new BigDecimal(ScientificNotationStr);
		return bigDecimal.toPlainString();
	}

	// 判断车牌号是否正确合法，支持新能源车牌
	// 正确 true 错误 false
	public static boolean isCarPlate(String PlateCN) {
		Pattern pattern = Pattern
				.compile("^(([\u4e00-\u9fa5]{1}[A-Z]{1})[-]?|([wW][Jj][\u4e00-\u9fa5]{1}[-]?)|([a-zA-Z]{2}))([A-Za-z0-9]{5}|[DdFf][A-HJ-NP-Za-hj-np-z0-9][0-9]{4}|[0-9]{5}[DdFf])$");
		Matcher matcher = pattern.matcher(PlateCN);
		return matcher.matches();
	}

	// 判断字符串是否是数字
	// 正确 true 错误 false
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}

	// 获得GUID
	public static String getGUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	// 计时函数
	public static long CalcTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return cal.getTimeInMillis();
	}


	/**
	 * 获取属性名数组
	 * */
	public static String[] getFiledName(Object o) {
		Field[] fields = o.getClass().getDeclaredFields();
		String[] fieldNames = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			// System.out.println(fields[i].getType());
			fieldNames[i] = fields[i].getName();
		}
		return fieldNames;
	}

	/*
	 * 根据属性名获取属性值
	 */
	public static Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			return value;
		} catch (Exception e) {

			return null;
		}
	}


	//多个参数判断是否是SUCCESS
	public static boolean isAllSUCCESS(String... strs) {
		for (String str : strs) {
			if (!"SUCCESS".equals(str))
				return false;
		}
		return true;
	}

	public static String createWhiteDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {// 判断目录是否存在
			log.debug("创建目录失败，目标目录已存在！" + destDirName);
			return destDirName;
		}
		if (dir.mkdirs()) {// 创建目标目录
			log.debug("创建目录成功！" + destDirName);
			return destDirName;
		} else {
			log.debug("创建目录失败！");
			return "";
		}
	}

//	public static String writePro(String key, String value) throws IOException {
//		Properties prop = new Properties();
//		FileOutputStream oFile = null;
////		String dbFilePath = IndexController.class.getClassLoader().getResource("").getPath()+ "application.properties";
////		String dbFilePath = ResourceUtils.getFile("classpath:application.properties").getPath();
//		ClassPathResource resource = new ClassPathResource("application.properties");
//		log.debug("ClassPathResource中的classpath下面的application.properties:"+resource);
//		File file=resource.getFile();
//		log.debug("file:"+file);
//		String url=resource.getURI().toString();
//		log.debug("url="+url);
////		Log.debug("getDescription:"+resource.getDescription());//class path resource [application.properties]
////		Log.debug("getFile:"+resource.getFile());//F:\javaworkspace\transpeed_ty\target\classes\application.properties
////		Log.debug("getFilename:"+resource.getFilename());//application.properties
////		Log.debug("getInputStream:"+resource.getInputStream());//java.io.BufferedInputStream@ca65872
////		Log.debug("getPath:"+resource.getPath());//application.properties
////		Log.debug("AbstractResource:"+resource.getURI());///F:/javaworkspace/transpeed_ty/target/classes/application.properties
////		Log.debug("ClassPathResource:"+resource.getURL());///F:/javaworkspace/transpeed_ty/target/classes/application.properties
//		try {
////			dbFilePath = URLDecoder.decode(dbFilePath, "utf-8");
////			oFile = new FileOutputStream(dbFilePath, true);
////			prop.setProperty(key, value);
////			prop.store(oFile, dbFilePath);
////			Log.debug("成功写入配置文件：" + dbFilePath);
//
//			oFile = new FileOutputStream(file, true);
//			prop.setProperty(key, value);
//			prop.store(oFile, resource.getURI().toString());
//			log.debug("成功写入配置文件：" + resource.getURI());
//			oFile.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			log.error("writePro error：" + e.getMessage());
//		}
//		log.debug("write success");
//		return "write success";
//
//	}

	public static void downloadFile(String fileName, String prefix,HttpServletResponse response) throws UnsupportedEncodingException {
		response.setContentType("application/force-download");// 设置强制下载不打开
        response.addHeader("Content-Disposition", "attachment;fileName=" +new String(fileName.getBytes("gbk"), "iso8859-1"));
        response.setHeader("Context-Type", "application/xmsdownload");
	        File file = new File(prefix+fileName);
	        if (file.exists()){
	            byte[] buffer = new byte[1024];
	            FileInputStream fis = null;
	            BufferedInputStream bis = null;
	            try {
	                fis = new FileInputStream(file);
	                bis = new BufferedInputStream(fis);
					OutputStream os = response.getOutputStream();
	                int i = bis.read(buffer);
	                while (i != -1){
	                    os.write(buffer ,0,i);
	                    i = bis.read(buffer);
	                }
	                fis.close();
	                bis.close();
	            } catch (FileNotFoundException e) {
	                e.printStackTrace();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	}




	public static void mkdirFull(File file) {
		if (file.getParentFile().exists()) {
			file.mkdir();
		} else {
			mkdirFull(file.getParentFile());
			file.mkdir();
		}
	}

	public static String getFilePath(String imageName,String url) {
		String split = "A";
		if (imageName.contains("A"))
			split = "A";
		else if (imageName.contains("B"))
			split = "B";
		else if (imageName.contains("C"))
			split = "C";
		else if (imageName.contains("D"))
			split = "D";
		else
			return "none";
		int index = imageName.indexOf(split) + 1;//8
		if (imageName.length() < (index + 8) || index == 0){
			log.error("Status=failed&Explain=imageName length error");
			return "none";
		}
		String TimePrefix = imageName.substring(index, index + 8);
		String ulid = imageName.substring(0, index-1);
		log.debug("path：  " + url + "/" + TimePrefix + "/"+ ulid);
		return url + "/" + TimePrefix + "/" + ulid + "/";
	}
}
