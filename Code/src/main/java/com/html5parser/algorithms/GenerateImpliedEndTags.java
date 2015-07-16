package com.html5parser.algorithms;

import com.html5parser.classes.ParserContext;

public class GenerateImpliedEndTags {

	public static ParserContext run(ParserContext parserContext) {					
//		String nodeName = parserContext.getCurrentNode().getNodeName();		
//
//		// When the steps below require the UA to generate implied end tags,
//		// then, while the current node is a dd element, a dt element, an li
//		// element, an option element, an optgroup element, a p element, an rb
//		// element, an rp element, an rt element, or an rtc element, the UA must
//		// pop the current node off the stack of open elements.
//		while (isOneOf(nodeName, new String[] { "dd", "dt", "li", "option",
//				"optgroup", "p", "rb", "rp", "rt", "rtc" })) {
//			parserContext.getOpenElements().pop();
//			nodeName = parserContext.getCurrentNode().getNodeName();
//		}
//		return parserContext;
		return run(parserContext, null);
	}

	public static ParserContext run(ParserContext parserContext, String exception) {
		
		if (parserContext.isTracing())
			parserContext.getTracer().addParseEvent("8.2.5.3");
		
		String nodeName = parserContext.getCurrentNode().getNodeName();
		// If a step requires the UA to generate implied end tags but lists an
		// element to exclude from the process, then the UA must perform the
		// above steps as if that element was not in the above list.
		while ((exception == null || !nodeName.equals(exception))
				&& isOneOf(nodeName, new String[] { "dd", "dt", "li", "option",
						"optgroup", "p", "rb", "rp", "rt", "rtc" })) {
			parserContext.getOpenElements().pop();
			nodeName = parserContext.getCurrentNode().getNodeName();
		}
		return parserContext;
	}

	private static Boolean isOneOf(String value, String[] values) {
		for (String s : values)
			if (s.equals(value))
				return true;

		return false;
	}
}
