package com.html5parser.tracer;

public class ParseError {

	public enum ParseErrorType {
		InvalidInputCharacter, UnexpectedInputCharacter, UnexpectedToken, DuplicatedAttributeName, EndTagWithAttributes, EndTagWithSelfClosingFlag, StartTagWithSelfClosingFlag, InvalidNamespace
	}

	private String message = "";
	private ParseErrorType type;

	public ParseError(ParseErrorType type, String message) {
		this.message = message;
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public ParseErrorType getType() {
		return this.type;
	}
}