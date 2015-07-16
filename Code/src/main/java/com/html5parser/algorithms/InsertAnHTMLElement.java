package com.html5parser.algorithms;

import com.html5dom.Element;

import com.html5parser.classes.ParserContext;
import com.html5parser.classes.Token;
import com.html5parser.constants.Namespace;

public class InsertAnHTMLElement {

	public static Element run(ParserContext parserContext, Token token) {
		if (parserContext.isTracing())
			parserContext.getTracer().addParseEvent("8.2.5.1.4", token);
		
		return InsertForeignElement.run(parserContext, token, Namespace.HTML);
	}
}
