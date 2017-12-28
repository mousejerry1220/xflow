package org.xsnake.cloud.xflow.tools;

public class LockedException extends Exception{

	public LockedException(Exception e) {
		super(e);
	}

	public LockedException() {
	}

	private static final long serialVersionUID = 1L;

}
