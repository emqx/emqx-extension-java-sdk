package io.emqx.extension.handler.codec;

import erlport.terms.Atom;
import erlport.terms.Binary;

public class CodecUtil {
	
	public static String atom2String(Object object) {
		return ((Atom) object).value;
	}
	
	public static byte[] binary2ByteArray(Object object) {
		return ((Binary) object).raw;
	}
	
	public static String binary2String(Object object) {
		byte[] bytes = ((Binary) object).raw;
		if (bytes.length == 0) {
			return null;
		} else {
			return new String(bytes);
		}
	}

}
