package com.nexentire.rental.util.hmac;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

@Service
public class HMacUtil {

	public static final String ALGORITHM = "HmacSHA256";

    public static String generateHMac(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance(ALGORITHM);

        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), ALGORITHM);
        sha256_HMAC.init(secret_key);

        return byteArrayToHex(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
