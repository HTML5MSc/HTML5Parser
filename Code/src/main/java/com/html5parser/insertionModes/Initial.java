package com.html5parser.insertionModes;

import com.html5dom.Document;
import com.html5dom.Document.QuirksMode;
import com.html5dom.DocumentType;
import com.html5parser.algorithms.InsertComment;
import com.html5parser.classes.InsertionMode;
import com.html5parser.classes.ParserContext;
import com.html5parser.classes.Token;
import com.html5parser.classes.token.DocTypeToken;
import com.html5parser.factories.InsertionModeFactory;
import com.html5parser.interfaces.IInsertionMode;
import com.html5parser.tracer.ParseError.ParseErrorType;

public class Initial implements IInsertionMode {

	public ParserContext process(ParserContext parserContext) {

		Token token = parserContext.getTokenizerContext().getCurrentToken();

		parserContext.addParseEvent("8.2.5.4.1", token);

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

		// If the DOCTYPE token's name is not a case-sensitive match for the
		// string "html", or the token's public identifier is not missing, or
		// the token's system identifier is neither missing nor a case-sensitive
		// match for the string "about:legacy-compat", and none of the sets of
		// conditions in the following list are matched, then there is a parse
		// error.
		// A system identifier whose value is the empty string is not
		// considered missing for the purposes of the conditions above.
		case DOCTYPE:

			DocTypeToken docTypeToken = (DocTypeToken) token;
			String name = docTypeToken.getValue();
			String publicId = docTypeToken.getPublicIdentifier();
			String systemId = docTypeToken.getSystemIdentifier();

			if (name != null
					&& !name.equals("html")
					|| publicId != null
					|| systemId != null
					|| (systemId != null && !systemId
							.equals("about:legacy-compat"))
					&& !validateDoctype(name, publicId, systemId)) {
				parserContext.addParseErrors(ParseErrorType.UnexpectedToken);
			}

			Document doc = parserContext.getDocument();
			DocumentType doctype = doc.createDocumentType(name, publicId,
					systemId);
			doc.appendChild(doctype);

			// TODO Then, if the document is not an iframe srcdoc document, and
			// the DOCTYPE token matches one of the conditions in the following
			// list, then set the Document to quirks mode:
			checkQuirksDocument(parserContext, name, publicId, systemId);

			// TODO Otherwise, if the document is not an iframe srcdoc document,
			// and the DOCTYPE token matches one of the conditions in the
			// following list, then set the Document to limited-quirks mode:
			checkLimitedQuirksDocument(parserContext, publicId, systemId);

			// Then, switch the insertion mode to "before html".
			parserContext.setInsertionMode(InsertionModeFactory.getInstance()
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
		parserContext.addParseErrors(ParseErrorType.UnexpectedToken);
		parserContext.getDocument().setQuirksMode(QuirksMode.quirks);

		// In any case, switch the insertion mode to "before html", then
		// reprocess the token.
		parserContext.setInsertionMode(InsertionModeFactory.getInstance()
				.getInsertionMode(InsertionMode.before_html));
		parserContext.setFlagReconsumeToken(true);
	}

	private boolean validateDoctype(String name, String publicId,
			String systemId) {
		if (name != null && publicId != null && name.equals("html")) {
			if (publicId.equals("-//W3C//DTD HTML 4.0//EN")
					&& (systemId == null || systemId
							.equals("http://www.w3.org/TR/REC-html40/strict.dtd")))
				return true;
			if (publicId.equals("-//W3C//DTD HTML 4.01//EN")
					&& (systemId == null || systemId
							.equals("http://www.w3.org/TR/html4/strict.dtd")))
				return true;
			if (publicId.equals("-//W3C//DTD XHTML 1.0 Strict//EN")
					&& systemId
							.equals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"))
				return true;
			if (publicId.equals("-//W3C//DTD XHTML 1.1//EN")
					&& systemId
							.equals("http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"))
				return true;
		}
		return false;
	}

	private void checkQuirksDocument(ParserContext parserContext, String name,
			String publicId, String systemId) {

		String[] publicIdEqualsToVals = new String[] {
				"-//W3O//DTD W3 HTML Strict 3.0//EN//",
				"-/W3C/DTD HTML 4.0 Transitional/EN", "HTML" };

		String[] publicIdStartWithVals = new String[] {
				"+//Silmaril//dtd html Pro v0r11 19970101//",
				"-//AdvaSoft Ltd//DTD HTML 3.0 asWedit + extensions//",
				"-//AS//DTD HTML 3.0 asWedit + extensions//",
				"-//IETF//DTD HTML 2.0 Level 1//",
				"-//IETF//DTD HTML 2.0 Level 2//",
				"-//IETF//DTD HTML 2.0 Strict Level 1//",
				"-//IETF//DTD HTML 2.0 Strict Level 2//",
				"-//IETF//DTD HTML 2.0 Strict//",
				"-//IETF//DTD HTML 2.0//",
				"-//IETF//DTD HTML 2.1E//",
				"-//IETF//DTD HTML 3.0//",
				"-//IETF//DTD HTML 3.2 Final//",
				"-//IETF//DTD HTML 3.2//",
				"-//IETF//DTD HTML 3//",
				"-//IETF//DTD HTML Level 0//",
				"-//IETF//DTD HTML Level 1//",
				"-//IETF//DTD HTML Level 2//",
				"-//IETF//DTD HTML Level 3//",
				"-//IETF//DTD HTML Strict Level 0//",
				"-//IETF//DTD HTML Strict Level 1//",
				"-//IETF//DTD HTML Strict Level 2//",
				"-//IETF//DTD HTML Strict Level 3//",
				"-//IETF//DTD HTML Strict//",
				"-//IETF//DTD HTML//",
				"-//Metrius//DTD Metrius Presentational//",
				"-//Microsoft//DTD Internet Explorer 2.0 HTML Strict//",
				"-//Microsoft//DTD Internet Explorer 2.0 HTML//",
				"-//Microsoft//DTD Internet Explorer 2.0 Tables//",
				"-//Microsoft//DTD Internet Explorer 3.0 HTML Strict//",
				"-//Microsoft//DTD Internet Explorer 3.0 HTML//",
				"-//Microsoft//DTD Internet Explorer 3.0 Tables//",
				"-//Netscape Comm. Corp.//DTD HTML//",
				"-//Netscape Comm. Corp.//DTD Strict HTML//",
				"-//O'Reilly and Associates//DTD HTML 2.0//",
				"-//O'Reilly and Associates//DTD HTML Extended 1.0//",
				"-//O'Reilly and Associates//DTD HTML Extended Relaxed 1.0//",
				"-//SoftQuad Software//DTD HoTMetaL PRO 6.0::19990601::extensions to HTML 4.0//",
				"-//SoftQuad//DTD HoTMetaL PRO 4.0::19971010::extensions to HTML 4.0//",
				"-//Spyglass//DTD HTML 2.0 Extended//",
				"-//SQ//DTD HTML 2.0 HoTMetaL + extensions//",
				"-//Sun Microsystems Corp.//DTD HotJava HTML//",
				"-//Sun Microsystems Corp.//DTD HotJava Strict HTML//",
				"-//W3C//DTD HTML 3 1995-03-24//",
				"-//W3C//DTD HTML 3.2 Draft//", "-//W3C//DTD HTML 3.2 Final//",
				"-//W3C//DTD HTML 3.2//", "-//W3C//DTD HTML 3.2S Draft//",
				"-//W3C//DTD HTML 4.0 Frameset//",
				"-//W3C//DTD HTML 4.0 Transitional//",
				"-//W3C//DTD HTML Experimental 19960712//",
				"-//W3C//DTD HTML Experimental 970421//",
				"-//W3C//DTD W3 HTML//", "-//W3O//DTD W3 HTML 3.0//",
				"-//WebTechs//DTD Mozilla HTML 2.0//",
				"-//WebTechs//DTD Mozilla HTML//" };

		String[] systemIdEqualsToVals = new String[] { "http://www.ibm.com/data/dtd/v11/ibmxhtml1-transitional.dtd" };
		String[] publicIdSystemIdVals = new String[] {
				"-//W3C//DTD HTML 4.01 Frameset//",
				"-//W3C//DTD HTML 4.01 Transitional//" };

		if ((name != null && !name.equals("html"))
				|| startsWithCaseInsensitive(publicId, publicIdStartWithVals)
				|| equalsToCaseInsensitive(publicId, publicIdEqualsToVals)
				|| (systemId == null && startsWithCaseInsensitive(publicId,
						publicIdSystemIdVals))
				|| equalsToCaseInsensitive(systemId, systemIdEqualsToVals))
			parserContext.getDocument().setQuirksMode(QuirksMode.quirks);
	}

	private void checkLimitedQuirksDocument(ParserContext parserContext,
			String publicId, String systemId) {
		String[] publicIdStartWithVals = new String[] {
				"-//W3C//DTD XHTML 1.0 Frameset//",
				"-//W3C//DTD XHTML 1.0 Transitional//" };
		String[] publicIdSystemIdVals = new String[] {
				"-//W3C//DTD HTML 4.01 Frameset//",
				"-//W3C//DTD HTML 4.01 Transitional//" };
		if (startsWithCaseInsensitive(publicId, publicIdStartWithVals)
				|| (systemId != null && startsWithCaseInsensitive(publicId,
						publicIdSystemIdVals)))
			parserContext.getDocument().setQuirksMode(QuirksMode.limitedQuirks);
	}

	private Boolean startsWithCaseInsensitive(String value, String[] values) {
		value = value == null ? "" : value.toLowerCase();
		for (String s : values)
			if (value.startsWith(s.toLowerCase()))
				return true;

		return false;
	}

	private Boolean equalsToCaseInsensitive(String value, String[] values) {
		value = value == null ? "" : value.toLowerCase();
		for (String s : values)
			if (value.equals(s.toLowerCase()))
				return true;

		return false;
	}
}
