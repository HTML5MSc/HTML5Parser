package com.html5parser.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.html5dom.DocumentType;
import com.html5dom.Element;
import com.html5dom.Node;
import com.html5parser.constants.Namespace;

public class Serializer {

	public static String toHtmlString(Node doc) {
		return doc.getOuterHtml();
	}

	public static String toHtml5libFormat(Node node) {
		String str = "";
		int ancestors = 0;
		if (node.getFirstChild() == null)
			return "| ";
		Node parent = node;
		Node current = node.getFirstChild();
		Node next = null;

		/*
		 * Check if there is an Invalid doctype
		 */
		if (parent.getUserData("invalidDoctype") != null)
			str += "\n| " + parent.getUserData("invalidDoctype").toString();

		for (;;) {
			// str += "\n| " + indent(ancestors);
			str += "\n| " + indent(ancestors + templateAncestors(current, 0));
			switch (current.getNodeType()) {
			case DOCUMENT_TYPE_NODE:
				DocumentType doctype = (DocumentType) current;
				String docname = doctype.getName();
				String publicid = doctype.getPublicId();
				String systemid = doctype.getSystemId();
				docname = docname == null ? "" : docname;
				if (publicid == null && systemid == null) {
					publicid = "";
					systemid = "";
				} else {
					publicid = " \"" + (publicid == null ? "" : publicid)
							+ "\"";
					systemid = " \"" + (systemid == null ? "" : systemid)
							+ "\"";
				}
				str += "<!DOCTYPE " + docname + publicid + systemid + ">";
				break;
			case COMMENT_NODE:
				str += "<!-- "
						+ (current.getNodeValue() == null ? "" : current
								.getNodeValue()) + " -->";
				if (parent != current.getParentNode()) {
					return str += " (misnested... aborting)";
				}
				break;
			case CDATA_SECTION_NODE:
				str += "<![CDATA[ "
						+ (current.getNodeValue() == null ? "" : current
								.getNodeValue()) + " ]]>";
				break;
			case TEXT_NODE:
				str += '"' + current.getOuterHtml() + '"';
				if (parent != current.getParentNode()) {
					return str += " (misnested... aborting)";
				}
				break;
			case ELEMENT_NODE:
				str += "<";
				if (current.getNamespaceURI() != null)
					switch (current.getNamespaceURI()) {
					case Namespace.SVG:
						str += "svg ";
						break;
					case Namespace.MathML:
						str += "math ";
						break;
					}
				str += current.getNodeName();
				str += '>';
				if (parent != current.getParentNode()) {
					return str += " (misnested... aborting)";
				} else {
					List<com.html5dom.Attribute> atts = ((Element) current)
							.getAttributes();
					Map<String, String> attrNames = new HashMap<String, String>();

					for (com.html5dom.Attribute att : atts) {
						String name = "";
						if (att.getNamespaceURI() != null) {
							switch (att.getNamespaceURI()) {
							case Namespace.XML:
								name += "xml ";
								break;
							case Namespace.XMLNS:
								name += "xmlns ";
								break;
							case Namespace.XLink:
								name += "xlink ";
								break;
							}
							name += att.getLocalName();
						} else {
							name += att.getNodeName();
						}
						attrNames.put(name, att.getNodeValue());
					}

					if (attrNames.size() > 0) {
						TreeMap<String, String> sorted_map = new TreeMap<String, String>(
								attrNames);
						for (Map.Entry<String, String> entry : sorted_map
								.entrySet()) {
							String key = entry.getKey();
							String value = entry.getValue();

							// str += "\n| " + indent(1 + ancestors) + key;
							str += "\n| "
									+ indent(1 + ancestors
											+ templateAncestors(current, 0))
									+ key;
							str += "=\"" + value + "\"";
						}
					}

					/*
					 * Template elements have 'content' child This is the
					 * required format of html5lib
					 */
					if (current.getNodeName().equals("template")
							&& ((Element) current).isHTMLElement()) {
						str += "\n| "
								+ indent(1 + ancestors
										+ templateAncestors(current, 0))
								+ "content";
					}

					next = current.getFirstChild();
					if (null != next) {
						parent = current;
						current = next;
						ancestors++;
						continue;
					}
				}
				break;
			default:
				break;
			}
			for (;;) {
				next = current.getNextSibling();
				if (next != null) {
					current = next;
					break;
				}
				current = current.getParentNode();
				parent = parent.getParentNode();
				ancestors--;
				if (current == node) {
					return str.substring(1);
				}
			}
		}
	}

	private static String indent(int ancestors) {
		String str = "";
		if (ancestors > 0) {
			while (0 <= --ancestors)
				str += "  ";
		}
		return str;
	}

	private static int templateAncestors(Node current, int ancestors) {
		if (current.getParentNode() != null) {
			current = current.getParentNode();
			if (current.getNodeName().equals("template")
					&& ((Element) current).isHTMLElement())
				ancestors++;
			return templateAncestors(current, ancestors);
		} else
			return ancestors;

	}
}
