package com.zxh.myBlog.utils;

import java.util.Random;

public class UUID {
	static Random r = new Random();
	
	
	/**
	 * Generate a random number in a range
	 * @param min
	 * @param max
	 * @return
	 */
	public static int random(int min, int max) {
		return r.nextInt(max - min + 1) + min;
	}
	
	private static final char[] _UU64 = "-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] _UU32 = "0123456789abcdefghijklmnopqrstuv".toCharArray();

    public static String UU64() {
    	return UU64(java.util.UUID.randomUUID());
    }

    
    /**
     * return a 64-bit form UUID, with compression， content : [\\ -0-9 a-z A-Z]
     * 
     * --compress 32-bit to 22-bit
     * @param uu
     * @return
     */
	private static String UU64(java.util.UUID uu) {
		// TODO Auto-generated method stub
		int index = 0;
		char[] cs = new char[22];
		long L = uu.getMostSignificantBits();
		long R = uu.getLeastSignificantBits();
		long mask = 63;
		// 
		for (int off = 58; off >= 4; off -= 6) {
            long hex = (L & (mask << off)) >>> off;
            cs[index++] = _UU64[(int) hex];
        }
        // 从L64位取最后的4位 ＋ R64位头2位拼上
        int l = (int) (((L & 0xF) << 2) | ((R & (3 << 62)) >>> 62));
        cs[index++] = _UU64[l];
        // 从R64位取10次，每次取6位
        for (int off = 56; off >= 2; off -= 6) {
            long hex = (R & (mask << off)) >>> off;
            cs[index++] = _UU64[(int) hex];
        }
        // 剩下的两位最后取
        cs[index++] = _UU64[(int) (R & 3)];
        // 返回字符串
        return new String(cs);
	}
}
