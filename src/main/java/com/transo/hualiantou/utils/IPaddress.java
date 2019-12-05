package com.transo.hualiantou.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public class IPaddress {

	public static String getIP() {
		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();// 获取本地所有网络接口
			while (en.hasMoreElements()) {// 遍历枚举中的每一个元素
				NetworkInterface ni = (NetworkInterface) en.nextElement();
				Enumeration<InetAddress> enumInetAddr = ni.getInetAddresses();
				while (enumInetAddr.hasMoreElements()) {
					InetAddress inetAddress = (InetAddress) enumInetAddr
							.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& !inetAddress.isLinkLocalAddress()
							&& inetAddress.isSiteLocalAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return "本地IP异常";
	}
	
	
	//获取使用者IP
	public static final String getIpAddress(final HttpServletRequest request)
            throws Exception {
        if (request == null) {
            throw (new Exception("getIpAddr method HttpServletRequest Object is null"));
        }
        String ipString = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getRemoteAddr();
        }
     
        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ipString.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ipString = str;
                break;
            }
        }
     
        return ipString;
    }
	
}
