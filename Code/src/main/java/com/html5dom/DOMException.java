package com.html5dom;

public class DOMException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Parameterless Constructor
    public DOMException() {}

    //Constructor that accepts a message
    public DOMException(String message)
    {
       super(message);
    }
}