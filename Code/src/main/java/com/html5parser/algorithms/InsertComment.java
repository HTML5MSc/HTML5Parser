package com.html5parser.algorithms;

import com.html5dom.Document;
import com.html5dom.Node;
import com.html5parser.classes.ParserContext;
import com.html5parser.classes.Token;

public class InsertComment {

	/**
	 * 
	 * @param parserContext
	 * @param token
	 * @return the Node in which the characters where added
	 */
	public static Node run(ParserContext parserContext, Token token) {
		return run(parserContext, token, null);
	}

	/**
	 * 
	 * @param parserContext
	 * @param token
	 * @return the Node in which the characters where added
	 */
	public static Node run(ParserContext parserContext, Token token,
			Node position) {

		// Let data be the data given in the comment token being processed.
		String data = token.getValue();

		parserContext.addParseEvent("8.2.5.1_9", "Comment \"" + data + "\"");

		// If position was specified, then let the adjusted insertion location
		// be position. Otherwise, let adjusted insertion location be the
		// appropriate place for inserting a node.
		AdjustedInsertionLocation adjustedInsertionLocation;
		if (position != null) {
			adjustedInsertionLocation = new AdjustedInsertionLocation(position,
					null);
		} else {
			adjustedInsertionLocation = AppropiatePlaceForInsertingANode
					.run(parserContext);
		}

		// Create a Comment node whose data attribute is set to data and whose
		// node document is the same as that of the node in which the adjusted
		// insertion location finds itself.
		Document document = adjustedInsertionLocation.getParent()
				.getOwnerDocument();

		// If doc is null it means it is the document
		if (document == null)
			document = ((Document) adjustedInsertionLocation.getParent());

		Node textNode = document.createComment(data);
		adjustedInsertionLocation.insertElement(textNode);
		return textNode;
	}
}
