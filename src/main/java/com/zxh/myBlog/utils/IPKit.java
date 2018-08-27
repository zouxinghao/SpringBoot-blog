package com.zxh.myBlog.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class IPKit {

	
	/**
	 * 
	 * @param request
	 * @return ip address
	 */
	public static String getIpAddrByRequest(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "Unknow".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
		return ip;
	}
	
	public static String getRealIp() throws Exception{
		String localIp = null; // return if netip does not exist, return it
		String netIp = null;
		
		Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		boolean findNet = false;
		while(netInterfaces.hasMoreElements() && !findNet) {
			NetworkInterface next = netInterfaces.nextElement();
			Enumeration<InetAddress> address = next.getInetAddresses();
			while(address.hasMoreElements()) {
				ip = address.nextElement();
				if(!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
					netIp = ip.getHostName();
					findNet = true;
					break;
				} else if(ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
					localIp = ip.getHostAddress();
				}
			}
		}
		if(netIp != null && !"".equals(netIp)) {
			return netIp;
		} else {
			return localIp;
		}
	}
}
