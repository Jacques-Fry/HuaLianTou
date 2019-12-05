package com.transo.hualiantou.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CuccSMS {

	/*502975*/

	// HTTP 普通短信 POST请求示例
	public static void main(String[] args) {
		sendSMS("505002","13025029601","wzcy002","【华联投】亲爱的用户您好，您的验证码666666，该验证码5分钟内有效，请勿泄漏于他人！",0);
	}

	/**
	 * 联通短信发送
	 * @param usr		用户ID(账号)
	 * @param mobile	接受手机号,多个号码以逗号分隔如"13515714339,18823178320" ,最多20个号码
	 * @param password	密码
	 * @param sms		发送内容包含签名(签名需要跟账号的签名一致)
	 */
	public static void sendSMS(String usr,String mobile,String password,String sms,int type){
		StringBuffer stringBuffer = new StringBuffer();
		try {
			String text = usr + "|" + password + "|" + mobile;
			String sign = md5LowerCase(text);
			stringBuffer.append("usr=" + usr);
			stringBuffer.append("&mobile=" + mobile);
			sms = URLEncoder.encode(sms, "GBK");
			stringBuffer.append("&sms=" + sms);
			stringBuffer.append("&type=" + type);		//0: 普通短信 1:模板短信
			stringBuffer.append("&extdsrcid=");
			stringBuffer.append("&sign=" + sign);
			System.out.println(sendPost("http://xapi.hzsysms.com:6088", stringBuffer.toString()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}


	public static final String md5LowerCase(String src) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(src.getBytes("UTF-8"));
		byte b[] = md.digest();
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}

	public static String sendPost(String url, String param) {
		if (url == null) {
			return null;
		}
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		System.out.println("Send to " + url + "?" + param);
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setConnectTimeout(4000);
			conn.setReadTimeout(8000);
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());
			out.print(param);
			out.flush();
			out.close();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("http send error: " + e.getMessage());
			result = null;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}
