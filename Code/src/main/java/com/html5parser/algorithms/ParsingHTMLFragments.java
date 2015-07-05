package com.html5parser.algorithms;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import com.html5dom.Document;
import com.html5dom.Document.QuirksMode;
import com.html5dom.Element;
import com.html5dom.Node;
import com.html5dom.Node.NodeType;
import com.html5parser.classes.InsertionMode;
import com.html5parser.classes.ParserContext;
import com.html5parser.classes.Token.TokenType;
import com.html5parser.classes.TokenizerContext;
import com.html5parser.classes.TokenizerState;
import com.html5parser.classes.token.TagToken;
import com.html5parser.constants.Namespace;
import com.html5parser.factories.InsertionModeFactory;
import com.html5parser.factories.TokenizerStateFactory;
import com.html5parser.parser.Parser;
import com.html5parser.parser.Serializer;

public class ParsingHTMLFragments {

	public static List<Node> run(ParserContext parserContext, Element context,
			String input) throws ParserConfigurationException {

		// Create a new Document node, and mark it as being an HTML document.
		Document document = new Document();

		// TODO If the node document of the context element is in quirks mode,
		// then
		// let the Document be in quirks mode. Otherwise, the node document of
		// the context element is in limited-quirks mode, then let the Document
		// be in limited-quirks mode. Otherwise, leave the Document in no-quirks
		// mode.
		document.setQuirksMode(QuirksMode.noQuirks);

		// Create a new HTML parser, and associate it with the just created
		// Document node.
		Parser parser = new Parser();
		parser.setDoc(document);

		// This is for our implementation, to know if the parser was created in
		// this script
		parser.getParserContext().setFlagHTMLFragmentParser(true);

		// Set the state of the HTML parser's tokenization stage as follows:
		// NOTE the spec does not say anything about namspace of the context
		// element, so we assume the elements must be in the HTML namespace
		TokenizerContext tokenizer = parser.getParserContext()
				.getTokenizerContext();
		TokenizerStateFactory factory = TokenizerStateFactory.getInstance();
		String elementName = context.getTagName();

		if (context.isHTMLElement()) {
			switch (elementName) {

			// If it is a title or textarea element
			// Switch the tokenizer to the RCDATA state.
			case "title":
			case "textarea":
				tokenizer.setNextState(factory
						.getState(TokenizerState.RCDATA_state));
				break;

			// If it is a style, xmp, iframe, noembed, or noframes element
			// Switch the tokenizer to the RAWTEXT state.
			case "style":
			case "xmp":
			case "iframe":
			case "noembed":
			case "noframes":
				tokenizer.setNextState(factory
						.getState(TokenizerState.RAWTEXT_state));
				break;

			// If it is a script element
			// Switch the tokenizer to the script data state.
			case "script":
				tokenizer.setNextState(factory
						.getState(TokenizerState.Script_data_state));
				break;

			// If it is a noscript element
			// If the scripting flag is enabled, switch the tokenizer to the
			// RAWTEXT
			// state. Otherwise, leave the tokenizer in the data state.
			case "noscript":
				if (parserContext.isFlagScripting())
					tokenizer.setNextState(factory
							.getState(TokenizerState.RAWTEXT_state));
				else
					tokenizer.setNextState(factory
							.getState(TokenizerState.Data_state));
				break;

			// If it is a plaintext element
			// Switch the tokenizer to the PLAINTEXT state.
			case "plaintext":
				tokenizer.setNextState(factory
						.getState(TokenizerState.PLAINTEXT_state));
				break;

			// Otherwise
			// Leave the tokenizer in the data state.
			default:
				tokenizer.setNextState(factory
						.getState(TokenizerState.Data_state));
				break;

			}
		} else
			tokenizer.setNextState(factory.getState(TokenizerState.Data_state));

		// Let root be a new html element with no attributes.
		Element root = document.createElementNS(Namespace.HTML, "html");

		// Append the element root to the Document node created above.
		document.appendChild(root);

		// Set up the parser's stack of open elements so that it contains just
		// the single element root.
		ParserContext newParserContext = parser.getParserContext();
		newParserContext.getOpenElements().push(root);

		// If the context element is a template element, push "in template" onto
		// the stack of template insertion modes so that it is the new current
		// template insertion mode.
		InsertionModeFactory insertionModeFactory = InsertionModeFactory
				.getInstance();
		if (elementName.equals("template")) {
			newParserContext.getTemplateInsertionModes().push(
					insertionModeFactory
							.getInsertionMode(InsertionMode.in_template));
		}

		// Create a start tag token whose name is the local name of context and
		// whose attributes are the attributes of context.
		//
		// Let this start tag token be the start tag token of the context node,
		// e.g. for the purposes of determining if it is an HTML integration
		// point. TODO:?????
		TagToken startTagToken = new TagToken(TokenType.start_tag,
				context.getNodeName());
		for (int i = 0; i < context.getAttributes().size(); i++) {
			Node attribute = context.getAttributes().get(i);
			startTagToken.createAttribute(attribute.getNodeName());
			startTagToken.appendCharacterInValueInLastAttribute(attribute
					.getNodeValue());
		}

		// Add start tag token to the stack of emmited start tags
		tokenizer.appendEmittedStartTag(startTagToken.getValue());

		// Set namespace as attribute
		if (context.getNamespaceURI() != null) {
			startTagToken.createAttribute("xmlns");
			startTagToken.appendCharacterInValueInLastAttribute(context
					.getNamespaceURI());
		}

		context.setUserData("startTagToken", startTagToken);
		parser.getParserContext().setHtmlFragmentContext(context);

		// Reset the parser's insertion mode appropriately.
		ResetTheInsertionModeAppropriately.Run(newParserContext, context);

		// Set the parser's form element pointer to the nearest node to the
		// context element that is a form element (going straight up the
		// ancestor chain, and including the element itself, if it is a form
		// element), if any. (If there is no such form element, the form element
		// pointer keeps its initial value, null.)
		Element elementPointer = context;

		do {
			if (elementPointer.getTagName().equals("form")) {
				newParserContext.setFormElementPointer(elementPointer);
				break;
			}
			if (context.getParentNode() != null
					&& context.getParentNode().getNodeType() == NodeType.ELEMENT_NODE)
				elementPointer = (Element) context.getParentNode();
			else
				elementPointer = null;

		} while (elementPointer != null);

		// Place the input into the input stream for the HTML parser just
		// created. The encoding confidence is irrelevant.
		// Start the parser and let it run until it has consumed all the
		// characters just inserted into the input stream.
		parser.parse(input);

		// Return the child nodes of root, in tree order.
		return root.getChildNodes();

		// Question, what happen with the allocated memory for the newly created
		// parser
		// should we set this to null?

	}

	public static void main(String[] args) throws ParserConfigurationException {
		Document doc = new Document();
		ParserContext parserContext = new ParserContext();

		parserContext.setDocument(doc);

		Element context = doc.createElementNS(Namespace.SVG, "path");
		doc.appendChild(context);
		String input = "<nobr>X";
		List<Node> result = ParsingHTMLFragments.run(parserContext, context,
				input);
		System.out.println(result.size());
		int size = result.size();
		for (int i = 0; i < size; i++) {
			Node node = result.get(i);
			System.out.println(node);
			Node adopted = doc.importNode(node, true);
			context.appendChild(adopted);
		}
		System.out.println(Serializer.toHtmlString(doc));

		System.out.println(parserContext.getParseErrors());
	}
}
