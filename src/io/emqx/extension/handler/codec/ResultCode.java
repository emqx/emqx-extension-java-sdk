package io.emqx.extension.handler.codec;

public enum ResultCode {
	
	SUC, FAIL;

    public int getValue() {
		switch (this) {
		case SUC:
			return 0;
		case FAIL:
			return 1;
		default:
			return 1;
		}
	}
}
