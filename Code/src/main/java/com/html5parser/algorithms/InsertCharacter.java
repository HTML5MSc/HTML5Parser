package com.html5parser.algorithms;

import com.html5dom.Document;
import com.html5dom.Node;
import com.html5dom.Node.NodeType;
import com.html5parser.classes.ParserContext;
import com.html5parser.classes.Token;

public class InsertCharacter {

	/**
	 * 
	 * @param parserContext
	 * @param token
	 * @return the Node in which the characters where added
	 */
	public static Node run(ParserContext parserContext, Token token) {
		return run(parserContext, token.getValue());
	}

	public static Node run(ParserContext parserContext, int character) {
		return run(parserContext, String.valueOf(Character.toChars(character)));
	}

	/**
	 * 
	 * @param parserContext
	 * @param token
	 * @return the Node in which the characters where added
	 */
	public static Node run(ParserContext parserContext, String data) {

		if (parserContext.isTracing())
			parserContext.getTracer().addParseEvent("8.2.5.1.8",
					"Character \"" + data + "\"");

		// Let the adjusted insertion location be the appropriate place for
		// inserting a node.
		AdjustedInsertionLocation adjustedInsertionLocation = AppropiatePlaceForInsertingANode
				.run(parserContext);

		// If the adjusted insertion location is in a Document node, then abort
		// these steps.
		if (adjustedInsertionLocation.getParent().getNodeType() == NodeType.DOCUMENT_NODE) {
			return null;
		}

		// If there is a Text node immediately before the adjusted insertion
		// location, then append data to that Text node's data.
		Node referenceLocation = adjustedInsertionLocation.getReferenceNode();
		Node beforeLocation = null;
		if (referenceLocation != null)
			beforeLocation = referenceLocation.getPreviousSibling();
		// if it will be inserted before location
		if (beforeLocation != null
				&& beforeLocation.getNodeType() == NodeType.TEXT_NODE) {
			beforeLocation.setNodeValue((beforeLocation.getNodeValue())
					.concat(data));
			return beforeLocation;
		}// if it will be inserted as a last child
		else if (adjustedInsertionLocation.getParent().getLastChild() != null
				&& adjustedInsertionLocation.getParent().getLastChild()
						.getNodeType() == NodeType.TEXT_NODE) {
			Node location = adjustedInsertionLocation.getParent()
					.getLastChild();
			location.setNodeValue((location.getNodeValue()).concat(data));
			return referenceLocation;
		}

		else {
			// Otherwise, create a new Text node whose data is data and whose
			// node document is the same as that of the element in which the
			// adjusted insertion location finds itself, and insert the newly
			// created node at the adjusted insertion location.
			Document document = adjustedInsertionLocation.getParent()
					.getOwnerDocument();

			// If doc is null it means it is the document
			if (document == null)
				document = ((Document) adjustedInsertionLocation.getParent());

			Node textNode = document.createTextNode(data);
			adjustedInsertionLocation.insertElement(textNode);
			return textNode;
		}
	}
}
