package com.html5parser.parseError;

import com.html5parser.classes.ParserContext;
import com.html5parser.classes.Token;

public class ParseError {

	private String message = "";

	public ParseError(ParseErrorType type, ParserContext parserContext) {

		int currentChar;
		Token currentToken = parserContext.getTokenizerContext()
				.getCurrentToken();

		switch (type) {
		case InvalidInputCharacter:
			currentChar = parserContext.getTokenizerContext()
					.getCurrentInputCharacter();
			this.message = "Invalid input character \""
					+ String.valueOf(Character.toChars(currentChar)) + "\" ("
					+ currentChar + ") while preprocessing the stream";
			break;
		case UnexpectedInputCharacter:
			currentChar = parserContext.getTokenizerContext()
					.getCurrentInputCharacter();
			this.message = "Unexpected character \""
					+ (currentChar != -1 ? String.valueOf(Character
							.toChars(currentChar)) : "EOF") + "\" ("
					+ currentChar + ") in Tokenizer "
					+ getTokenizerStateName(parserContext);
			break;
		case UnexpectedToken:
			this.message = "Unexpected "
					+ currentToken.getType()
					+ " token"
					+ (currentToken.getValue() == null ? "" : (" \""
							+ currentToken.getValue() + "\"")) + " in "
					+ getInsertionModeName(parserContext) + " Insertion Mode";
			break;
		case EndTagWithAttributes:
			this.message = "End tag token \"" + currentToken.getValue()
					+ "\" emitted with attributes.";
			break;
		case EndTagWithSelfClosingFlag:
			this.message = "End tag token \"" + currentToken.getValue()
					+ "\" emitted with its self-closing flag set";
			break;
		case StartTagWithSelfClosingFlag:
			this.message = "Start tag token \""
					+ currentToken.getValue()
					+ "\" emitted with its self-closing flag set and not acknowledged";
			break;
		default:
			this.message = "Parse error";
			break;

		}
	}

	public ParseError(ParseErrorType type, String error,
			ParserContext parserContext) {
		Token currentToken = parserContext.getTokenizerContext()
				.getCurrentToken();

		switch (type) {
		case DuplicatedAttributeName:
			this.message = "Duplicated attribute name \"" + error
					+ "\" in element \"" + currentToken.getValue() + "\"";
			break;
		case InvalidNamespace:
			this.message = "Invalid attribute namespace \"" + error
					+ "\" in element \"" + currentToken.getValue() + "\"";
		default:
			this.message = error;
			break;
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private String getTokenizerStateName(ParserContext parserContext) {
		String name = parserContext.getTokenizerContext().getNextState()
				.toString();
		name = name.replace("com.html5parser.tokenizerStates.", "");
		name = name.substring(0, name.indexOf("@"));
		return name;
	}

	private String getInsertionModeName(ParserContext parserContext) {
		String name = parserContext.getInsertionMode().toString();
		name = name.replace("com.html5parser.insertionModes.", "");
		name = name.substring(0, name.indexOf("@"));
		return name;
	}
}