package com.html5parser.tokenizerStates;

import java.util.LinkedList;
import java.util.Queue;

import com.html5parser.classes.ParserContext;
import com.html5parser.classes.Token;
import com.html5parser.classes.TokenizerContext;
import com.html5parser.classes.token.TagToken;

public class Character_reference_in_attribute_value_state {
	protected Queue<Token> reference = new LinkedList<Token>();
	protected boolean parsingCharacterReference = false;

	protected void attemptToConsumeReference(ParserContext context,
			TokenizerContext tokenizerContext) {
		Queue<Token> result = Tokenizing_character_references
				.getTokenCharactersFromReference(reference, context);

		if (result != null) {
			for (Token tokenResult : result) {
				((TagToken) tokenizerContext.getCurrentToken())
						.appendCharacterInValueInLastAttribute(tokenResult
								.getValue());
			}
		}
		parsingCharacterReference = false;
		reference = new LinkedList<Token>();
	}

	// If the possible reference is numerical then try to match,
	// If not numerical, try to match the whole input as a reference and add a
	// parse error because it doesn't end with ;
	// If no match reference add the input to the value of the attribute
	protected void attemptToConsumeReferenceInAttribute(
			TokenizerContext tokenizerContext, ParserContext context) {
		
		context.addParseEvent("8.2.4.41");
		
		if (parsingCharacterReference) {
			if (!reference.isEmpty() && reference.peek().getValue().equals("#"))
				attemptToConsumeReference(context, tokenizerContext);
			else {
				String original = "";
				for (Token token : reference)
					original = original.concat(token.getValue());

				Queue<Token> result = Tokenizing_character_references
						.getTokenCharactersFromReference(reference, context);

				if (result == null) {
					((TagToken) tokenizerContext.getCurrentToken())
							.appendCharacterInValueInLastAttribute(0x0026);
					((TagToken) tokenizerContext.getCurrentToken())
							.appendCharacterInValueInLastAttribute(original);
				} else {
					for (Token tokenResult : result) {
						((TagToken) tokenizerContext.getCurrentToken())
								.appendCharacterInValueInLastAttribute(tokenResult
										.getValue());
					}
				}

			}
		}
		parsingCharacterReference = false;
		reference = new LinkedList<Token>();
	}
}