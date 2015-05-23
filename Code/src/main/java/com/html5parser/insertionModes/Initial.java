package com.html5parser.insertionModes;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

import com.html5parser.algorithms.InsertComment;
import com.html5parser.classes.InsertionMode;
import com.html5parser.classes.ParserContext;
import com.html5parser.classes.Token;
import com.html5parser.classes.token.DocTypeToken;
import com.html5parser.factories.InsertionModeFactory;
import com.html5parser.interfaces.IInsertionMode;
import com.html5parser.parseError.ParseErrorType;

public class Initial implements IInsertionMode {

	public ParserContext process(ParserContext parserContext) {

		Token token = parserContext.getTokenizerContext().getCurrentToken();

		switch (token.getType()) {
		// A character token that is one of U+0009 CHARACTER TABULATION, "LF"
		// (U+000A), "FF" (U+000C), "CR" (U+000D), or U+0020 SPACE
		// Ignore the token.
		case character:
			if (!token.isSpaceCharacter())
				anythingElse(parserContext);
			break;
		// A comment token
		// Insert a comment as the last child of the Document object.
		case comment:
			InsertComment
					.run(parserContext, token, parserContext.getDocument());
			break;

		// TODO A DOCTYPE token
		case DOCTYPE:
			DocTypeToken thisToken = (DocTypeToken) token;
			String tagName = thisToken.getValue();
			String publicIdentifier = thisToken.getPublicIdentifier();
			String systemIdentifier = thisToken.getSystemIdentifier();
			if (tagName != null
					&& ((tagName.equals("html") || publicIdentifier != null || (systemIdentifier != null && !tagName
							.equals("about:legacy-compat"))))
					&& !(((tagName.equals("html"))
							&& publicIdentifier != null
							&& publicIdentifier
									.equals("-//W3C//DTD HTML 4.0//EN") && (systemIdentifier == null || systemIdentifier
							.equals("http://www.w3.org/TR/REC-html40/strict.dtd")))
							|| ((tagName.equals("html"))
									&& publicIdentifier != null
									&& publicIdentifier
											.equals("-//W3C//DTD HTML 4.01//EN") && (systemIdentifier == null || systemIdentifier
									.equals("http://www.w3.org/TR/html4/strict.dtd")))
							|| ((tagName.equals("html"))
									&& publicIdentifier != null
									&& systemIdentifier != null
									&& publicIdentifier
											.equals("-//W3C//DTD XHTML 1.0 Strict//EN") && systemIdentifier
										.equals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd")) || ((tagName
							.equals("html"))
							&& publicIdentifier != null
							&& systemIdentifier != null
							&& publicIdentifier
									.equals("-//W3C//DTD XHTML 1.1//EN") && systemIdentifier
								.equals("http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"))

					)) {
				parserContext.addParseErrors(ParseErrorType.UnexpectedToken);
			}

			// // set the value of to one of these: "quirks mode",
			// "limited-quirks mode", "no-quirks mode"
			// doc.setUserData("quirksmode", "no-quirks mode", null);
			Document doc = parserContext.getDocument();
			if (tagName != null && !tagName.isEmpty()) {
				DOMImplementation domImpl = doc.getImplementation();
				try {
					DocumentType doctype = domImpl.createDocumentType(tagName,
							publicIdentifier, systemIdentifier);
					doc.appendChild(doctype);
				} catch (DOMException e) {
					e.printStackTrace();
					String invalidDoctype = "<!DOCTYPE "
							+ (tagName == null ? "" : tagName)
							+ (publicIdentifier == null ? ""
									: (" " + publicIdentifier))
							+ (systemIdentifier == null ? ""
									: (" " + systemIdentifier)) + ">";
					doc.setUserData("invalidDoctype", invalidDoctype, null);
				}
			} else {
				String invalidDoctype = "<!DOCTYPE >";
				doc.setUserData("invalidDoctype", invalidDoctype, null);
			}
			InsertionModeFactory factory = InsertionModeFactory.getInstance();
			parserContext.setInsertionMode(factory
					.getInsertionMode(InsertionMode.before_html));
			break;
		default:
			anythingElse(parserContext);
			break;
		}
		return parserContext;
	}

	public void anythingElse(ParserContext parserContext) {
		// Anything else
		// TODO If the document is not an iframe srcdoc document, then this is a
		// parse error; set the Document to quirks mode.
		// In any case, switch the insertion mode to "before html", then
		// reprocess the token.
		parserContext.setInsertionMode(InsertionModeFactory.getInstance()
				.getInsertionMode(InsertionMode.before_html));
		parserContext.setFlagReconsumeToken(true);
		Document doc = parserContext.getDocument();
		doc.setUserData("quirksmode", "quirks mode", null);
	}
}
