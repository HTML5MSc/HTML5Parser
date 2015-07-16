package com.html5parser.classes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.html5dom.Document;
import com.html5dom.Element;
import com.html5parser.classes.token.TagToken;
import com.html5parser.classes.token.TagToken.Attribute;
import com.html5parser.constants.Namespace;
import com.html5parser.insertionModes.Initial;
import com.html5parser.interfaces.IInsertionMode;
import com.html5parser.parseError.ParseError;
import com.html5parser.parseError.ParseErrorType;
import com.html5parser.tracer.Tracer;

public class ParserContext {
	/*
	 * Tokenizer context
	 */
	private TokenizerContext tokenizerContext = new TokenizerContext();

	/*
	 * Insertion modes
	 */
	private IInsertionMode insertionMode = new Initial();
	private IInsertionMode originalInsertionMode;
	// private IInsertionMode currentTemplateInsertionMode;is the last ins. mode
	// pushed onto the stack

	/*
	 * Stacks
	 */
	private Stack<Element> openElements = new Stack<Element>();
	private Stack<IInsertionMode> templateInsertionModes = new Stack<IInsertionMode>();

	/*
	 * Flags
	 */
	private boolean flagScripting = false;
	private boolean flagForceQuirks = false;
	private boolean flagParserPause = false;
	private boolean flagFramesetOk = true;
	private boolean flagStopParsing = false;
	private boolean flagReconsumeToken = false;
	private boolean flagFosterParenting = false;
	private boolean flagHTMLFragmentParser = false;

	/*
	 * Others
	 */
	private ArrayList<Element> activeFormattingElements = new ArrayList<Element>();
	private ArrayList<ParseError> parseErrors = new ArrayList<ParseError>();

	private Element htmlFragmentContext;
	private Element headElementPointer;
	private Element formElementPointer;

	/*
	 * Document
	 */
	Document doc;

	private Tracer tracer;
	
	public TokenizerContext getTokenizerContext() {
		return tokenizerContext;
	}

	public void setTokenizerContext(TokenizerContext value) {
		this.tokenizerContext = value;
	}

	public IInsertionMode getInsertionMode() {
		return insertionMode;
	}

	public void setInsertionMode(IInsertionMode value) {
		this.insertionMode = value;
	}

	/*
	 * public IInsertionMode getInsertionMode() { return IInsertionMode; }
	 * 
	 * public void setInsertionMode(IInsertionMode IInsertionMode) {
	 * this.insertionMode = IInsertionMode; }
	 */
	public IInsertionMode getOriginalInsertionMode() {
		return originalInsertionMode;
	}

	public void setOriginalInsertionMode(IInsertionMode originalInsertionMode) {
		this.originalInsertionMode = originalInsertionMode;
	}

	public IInsertionMode getCurrentTemplateInsertionMode() {
		return templateInsertionModes.peek();
	}

	public Stack<Element> getOpenElements() {
		return openElements;
	}

	public void setOpenElements(Stack<Element> openElements) {
		this.openElements = openElements;
	}

	public ArrayList<ParseError> getParseErrors() {
		return parseErrors;
	}

	public void setParseErrors(ArrayList<ParseError> parseErrors) {
		this.parseErrors = parseErrors;
	}

	public void addParseErrors(ParseErrorType parseErrorType) {
		parseErrors.add(new ParseError(parseErrorType, this));
	}

	public void addParseErrors(ParseErrorType parseErrorType, String message) {
		parseErrors.add(new ParseError(parseErrorType, message, this));
	}

	public Stack<IInsertionMode> getTemplateInsertionModes() {
		return templateInsertionModes;
	}

	public void setTemplateInsertionModes(
			Stack<IInsertionMode> templateInsertionModes) {
		this.templateInsertionModes = templateInsertionModes;
	}

	public boolean isFlagScripting() {
		return flagScripting;
	}

	public void setFlagScripting(boolean flagScripting) {
		this.flagScripting = flagScripting;
	}

	public boolean isFlagForceQuirks() {
		return flagForceQuirks;
	}

	public void setFlagForceQuirks(boolean flagForceQuirks) {
		this.flagForceQuirks = flagForceQuirks;
	}

	public boolean isFlagParserPause() {
		return flagParserPause;
	}

	public void setFlagParserPause(boolean flagParserPause) {
		this.flagParserPause = flagParserPause;
	}

	public boolean isFlagFramesetOk() {
		return flagFramesetOk;
	}

	public void setFlagFramesetOk(boolean flagFramesetOk) {
		this.flagFramesetOk = flagFramesetOk;
	}

	public ArrayList<Element> getActiveFormattingElements() {
		return activeFormattingElements;
	}

	public void setActiveFormattingElements(
			ArrayList<Element> activeFormattingElements) {
		this.activeFormattingElements = activeFormattingElements;
	}

	public Element getCurrentNode() {
		return openElements.size() > 0 ? openElements.peek() : null;
	}

	public Element getAdjustedCurrentNode() {
		// The adjusted current node is the context element if the stack of open
		// elements has only one element in it and the parser was created by the
		// HTML fragment parsing algorithm; otherwise, the adjusted current node
		// is the current node.
		if (this.flagHTMLFragmentParser && this.openElements.size() == 1)
			// return openElements.peek();
			return htmlFragmentContext;
		else if (openElements.size() > 0)
			return openElements.peek();
		else
			return null;
	}

	public Element getHtmlFragmentContext() {
		return htmlFragmentContext;
	}

	public void setHtmlFragmentContext(Element htmlFragmentContext) {
		this.htmlFragmentContext = htmlFragmentContext;
	}

	public Element getHeadElementPointer() {
		return headElementPointer;
	}

	public void setHeadElementPointer(Element headElementPointer) {
		this.headElementPointer = headElementPointer;
	}

	public Element getFormElementPointer() {
		return formElementPointer;
	}

	public void setFormElementPointer(Element formElementPointer) {
		this.formElementPointer = formElementPointer;
	}

	public boolean isFlagStopParsing() {
		return flagStopParsing;
	}

	public void setFlagStopParsing(boolean flagStopParsing) {
		this.flagStopParsing = flagStopParsing;
	}

	public boolean isFlagReconsumeToken() {
		return flagReconsumeToken;
	}

	public void setFlagReconsumeToken(boolean flagReconsumeToken) {
		this.flagReconsumeToken = flagReconsumeToken;
	}

	public Document getDocument() {
		return doc;
	}

	public void setDocument(Document doc) {
		this.doc = doc;
	}

	// Remove duplicate attributes and generate parse errors
	public void validateAttributeNames() {
		List<Attribute> attributes = ((TagToken) this.tokenizerContext
				.getCurrentToken()).getAttributes();
		final List<Attribute> setToReturn = new ArrayList<>();
		final Set<String> set1 = new HashSet<String>();

		for (Attribute att : attributes) {
			if (set1.add(att.getName())) {
				setToReturn.add(att);
			} else {
				this.addParseErrors(ParseErrorType.DuplicatedAttributeName,
						att.getName());
			}
		}
		((TagToken) this.tokenizerContext.getCurrentToken())
				.setAttributes(setToReturn);
	}

	public boolean isFlagFosterParenting() {
		return flagFosterParenting;
	}

	public void setFlagFosterParenting(boolean flagFosterParenting) {
		this.flagFosterParenting = flagFosterParenting;
	}

	public boolean openElementsContain(String elementName) {
		return openElementsContain(elementName, Namespace.HTML);
	}

	public boolean openElementsContain(String elementName, String namespace) {
		List<Element> list = new ArrayList<Element>();
		list.addAll(openElements);
		for (int i = 0; i < list.size(); i++) {
			Element element = list.get(i);
			if (element.getNodeName().equals(elementName)
					&& element.getNamespaceURI().equals(namespace)) {
				return true;
			}
		}
		return false;
	}

	public boolean isFlagHTMLFragmentParser() {
		return flagHTMLFragmentParser;
	}

	public void setFlagHTMLFragmentParser(boolean flagHTMLFragmentParser) {
		this.flagHTMLFragmentParser = flagHTMLFragmentParser;
	}

	public Tracer getTracer() {
		return tracer;
	}

	public void setTracer(Tracer tracer) {
		this.tracer = tracer;
	}

	public boolean isTracing(){
		return this.tracer != null;
	}
}