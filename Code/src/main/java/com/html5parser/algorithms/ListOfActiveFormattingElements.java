package com.html5parser.algorithms;

import java.util.ArrayList;
import java.util.List;

import com.html5dom.Attribute;
import com.html5dom.Element;
import com.html5parser.classes.ParserContext;
import com.html5parser.classes.token.TagToken;

public class ListOfActiveFormattingElements {
	// private String[] markerElements = { "applet", "marquee", "object", "th",
	// "td", "template" };

	public static void push(ParserContext parserContext, Element element) {

		// Not a formatting element
		if (!element.isFormattingElement())
			return;

		ArrayList<Element> list = parserContext.getActiveFormattingElements();

		// 1 If there are already three elements in the list of active
		// formatting elements after the last list marker, if any, or anywhere
		// in the list if there are no list markers, that have the same tag
		// name, namespace, and attributes as element, then remove the earliest
		// such element from the list of active formatting elements. For these
		// purposes, the attributes must be compared as they were when the
		// elements were created by the parser; two elements have the same
		// attributes if all their parsed attributes can be paired such that the
		// two attributes in each pair have identical names, namespaces, and
		// values (the order of the attributes does not matter).

		int count = 0;
		int index = 0;
		for (int i = list.size() - 1; i >= 0; i--) {
			Element e = list.get(i);
			if (e == null)
				break;
			if (isSameNode(e, element)) {
				count++;
				index = i;
			}
		}

		if (count > 2)
			list.remove(index);

		// 2 Add element to the list of active formatting elements.
		list.add(element);
		parserContext.setActiveFormattingElements(list);
	}

	private static boolean isSameNode(Element e1, Element e2) {
		// compare name
		if (!e1.getNodeName().equals(e2.getNodeName()))
			return false;

		// compare ns
		String expectedNS = e1.getNamespaceURI();
		String actualNS = e2.getNamespaceURI();
		if ((expectedNS == null && actualNS != null)
				|| (expectedNS != null && !expectedNS.equals(actualNS))) {
			return false;
		}

		// compare attributes
		List<Attribute> expectedAttrs = e1.getAttributes();
		List<Attribute> actualAttrs = e2.getAttributes();
		if (expectedAttrs.size() != actualAttrs.size())
			return false;

		for (int i = 0; i < expectedAttrs.size(); i++) {
			Attribute expectedAttr = expectedAttrs.get(i);
			if (expectedAttr.getNodeName().startsWith("xmlns")) {
				continue;
			}
			Attribute actualAttr = null;
			if (expectedAttr.getNamespaceURI() == null) {
				actualAttr = e2.getAttributeNode(expectedAttr.getNodeName());
			} else {
				actualAttr = e2.getAttributeNodeNS(
						expectedAttr.getNamespaceURI(),
						expectedAttr.getNodeName());
			}
			if (actualAttr == null) {
				// Attribute not found
				return false;
			}
			if (!expectedAttr.getNodeValue().equals(actualAttr.getNodeValue())) {
				// Attribute values do not match value
				return false;
			}
		}

		return true;
	}

	public static void insertMarker(ParserContext parserContext) {
		parserContext.getActiveFormattingElements().add(null);
	}

	public static void clear(ParserContext parserContext) {
		// 1 Let entry be the last (most recently added) entry in the list of
		// active formatting elements.
		// 2 Remove entry from the list of active formatting elements.
		// 3 If entry was a marker, then stop the algorithm at this point. The
		// list has been cleared up to the last marker.
		// 4 Go to step 1.
		ArrayList<Element> list = parserContext.getActiveFormattingElements();
		// int indexLastMarker = list.lastIndexOf(null) + 1;
		int indexLastMarker = list.lastIndexOf(null);
		if (indexLastMarker == -1)
			indexLastMarker++;
		while (list.size() > indexLastMarker)
			list.remove(list.size() - 1);
	}

	public static void reconstruct(ParserContext parserContext) {
		ArrayList<Element> list = parserContext.getActiveFormattingElements();
		// 1 If there are no entries in the list of active formatting elements,
		// then there is nothing to reconstruct; stop this algorithm.
		if (list.isEmpty())
			return;

		// 2 If the last (most recently added) entry in the list of active
		// formatting elements is a marker, or if it is an element that is in
		// the stack of open elements, then there is nothing to reconstruct;
		// stop this algorithm.
		int lastEntry = list.size() - 1;
		int lastInList = 0;
		Element entry = list.get(lastEntry);
		if (entry == null)
			return;
		if (parserContext.getOpenElements().contains(entry))
			return;

		// 3 Let entry be the last (most recently added) element in the list of
		// active formatting elements.

		// 4 Rewind: If there are no entries before entry in the list of active
		// formatting elements, then jump to the step labeled create.

		// 5 Let entry be the entry one earlier than entry in the list of active
		// formatting elements.

		// 6 If entry is neither a marker nor an element that is also in the
		// stack of open elements, go to the step labeled rewind.
		for (lastInList = lastEntry; lastInList >= 0; lastInList--) {
			entry = list.get(lastInList);
			if (entry == null
					|| parserContext.getOpenElements().contains(entry)) {
				entry = list.get(lastInList + 1);
				break;
			}
		}

		// 7 Advance: Let entry be the element one later than entry in the list
		// of active formatting elements.

		// 8 Create: Insert an HTML element for the token for which the element
		// entry was created, to obtain new element.

		// 9 Replace the entry for entry in the list with an entry for new
		// element.

		// 10 If the entry for new element in the list of active formatting
		// elements is not the last entry in the list, return to the step
		// labeled advance.
		for (lastInList = list.indexOf(entry); lastInList <= lastEntry; lastInList++) {
			entry = list.get(lastInList);
			TagToken t = (TagToken) entry.getUserData("0");
			Element newElement = InsertAnHTMLElement.run(parserContext, t);
			list.remove(entry);
			list.add(lastInList, newElement);
		}
	}
}
