package com.html5parser.algorithms;

import com.html5dom.Element;
import com.html5parser.classes.ParserContext;
import com.html5parser.classes.Token;

public class InsertForeignElement {

	public static Element run(ParserContext parserContext, Token token,
			String namespace) {

		if (parserContext.isTracing())
			parserContext.getTracer().addParseEvent("8.2.5.1.3", token);

		AdjustedInsertionLocation adjustedInsertionLocation = AppropiatePlaceForInsertingANode
				.run(parserContext);
		Element element = CreateAnElementForAToken.run(
				adjustedInsertionLocation.getParent(), namespace, token,
				parserContext);
		try {
			adjustedInsertionLocation.insertElement(element);
		} catch (Exception e) {
			// TODO drop the new element on the floor
		}

		parserContext.getOpenElements().push(element);
		return element;
	}
}
