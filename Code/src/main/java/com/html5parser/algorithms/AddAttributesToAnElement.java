package com.html5parser.algorithms;

import com.html5dom.Element;
import com.html5parser.classes.token.TagToken;
import com.html5parser.classes.token.TagToken.Attribute;

public class AddAttributesToAnElement {
	public static void run(Element element, TagToken tagToken) {
		for (Attribute att : tagToken.getAttributes())
			if (!element.hasAttribute(att.getName()))
				element.setAttributeNS(att.getNamespace(), att.getName(),
						att.getValue());
	}
}