package com.mhzdev.apptemplate.Utils.Crypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public enum AESUtil {
	;
	// 共通鍵
	private static final String ENCRYPTION_KEY = "3e67fc6724faf03dcc952f143d9dfe70";
	
	public static String encrypt(String src) {
		try {
			src = new String(Base64.decode(src)); //Decode base 64

			byte[] bkey = "3e67fc6724faf03dcc952f143d9dfe70".getBytes("UTF-8");
			SecretKeySpec sks = new SecretKeySpec(bkey, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, sks);
			return Base64.encodeBytes(cipher.doFinal(src.getBytes()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}