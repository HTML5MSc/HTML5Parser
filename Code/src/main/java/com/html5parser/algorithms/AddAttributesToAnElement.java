package com.html5parser.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

import com.html5parser.classes.token.TagToken;
import com.html5parser.classes.token.TagToken.Attribute;

public class AddAttributesToAnElement {
	public static void run(Element element, TagToken tagToken) {

		for (Attribute att : tagToken.getAttributes()) {
			if (!element.hasAttribute(att.getName())) {
				try {
					element.setAttribute(att.getName(), att.getValue());
				} catch (DOMException e) {
					@SuppressWarnings("unchecked")
					List<Attribute> invalidAtts = (List<Attribute>) element
							.getUserData("invalidAtts");
					if (invalidAtts == null)
						invalidAtts = new ArrayList<Attribute>();
					invalidAtts.add(att);
					element.setUserData("invalidAtts", invalidAtts, null);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}