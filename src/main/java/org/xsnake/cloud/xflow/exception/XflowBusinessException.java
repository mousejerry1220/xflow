package org.xsnake.cloud.xflow.exception;

public class XflowBusinessException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public XflowBusinessException() {}
	
	public XflowBusinessException(String message){
		super(message);
	}

}
