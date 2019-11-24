package com.example.exceptions;

/**
 * Custom wallet exception
 *
 */
public class WalletException extends Exception{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5031470104069765126L;
	private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public WalletException(String message, int errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public WalletException(){
        super();

    }

    public WalletException(String message){
        super(message);
    }

    public WalletException(Exception e){
        super(e);
    }
}
