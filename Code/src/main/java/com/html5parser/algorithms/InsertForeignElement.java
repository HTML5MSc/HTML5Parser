package com.html5parser.algorithms;

import com.html5dom.Element;
import com.html5parser.classes.ParserContext;
import com.html5parser.classes.Token;

public class InsertForeignElement {

	public static Element run(ParserContext context, Token token,
			String namespace) {
		AdjustedInsertionLocation adjustedInsertionLocation = AppropiatePlaceForInsertingANode
				.run(context);
		Element element = CreateAnElementForAToken.run(
				adjustedInsertionLocation.getParent(), namespace, token,
				context);
		try {
			adjustedInsertionLocation.insertElement(element);
		} catch (Exception e) {
			// TODO drop the new element on the floor
		}

		context.getOpenElements().push(element);
		return element;
	}
}
