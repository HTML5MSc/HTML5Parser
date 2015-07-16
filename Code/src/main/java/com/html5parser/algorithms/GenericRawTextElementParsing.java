package com.html5parser.algorithms;

import com.html5parser.classes.InsertionMode;
import com.html5parser.classes.ParserContext;
import com.html5parser.classes.TokenizerState;
import com.html5parser.classes.token.TagToken;
import com.html5parser.factories.InsertionModeFactory;
import com.html5parser.factories.TokenizerStateFactory;

public class GenericRawTextElementParsing {

	public static ParserContext run(ParserContext parserContext, TagToken token) {
		
		if (parserContext.isTracing())
			parserContext.getTracer().addParseEvent("8.2.5.2.1", token);
		
		TokenizerStateFactory tokenizerFactory = TokenizerStateFactory
				.getInstance();
		InsertionModeFactory insertionModeFactory = InsertionModeFactory
				.getInstance();
		InsertAnHTMLElement.run(parserContext, token);
		parserContext.getTokenizerContext().setNextState(
				tokenizerFactory.getState(TokenizerState.RAWTEXT_state));
		parserContext.setOriginalInsertionMode(parserContext.getInsertionMode());
		parserContext.setInsertionMode(insertionModeFactory
				.getInsertionMode(InsertionMode.text));
		return parserContext;
	}
}
