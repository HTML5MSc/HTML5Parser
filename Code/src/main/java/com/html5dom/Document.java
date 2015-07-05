package com.html5dom;

public class Document extends Node {

	public enum QuirksMode {
		noQuirks, quirks, limitedQuirks
	}

	private QuirksMode quirksMode = QuirksMode.noQuirks;

	public Document() {
		super(NodeType.DOCUMENT_NODE);
	}

	public QuirksMode getQuirksMode() {
		return this.quirksMode;
	}

	public void setQuirksMode(QuirksMode quirksMode) {
		this.quirksMode = quirksMode;
	}

	public boolean isQuirksMode() {
		return this.quirksMode == QuirksMode.quirks;
	}

	public boolean isNoQuirksMode() {
		return this.quirksMode == QuirksMode.noQuirks;
	}

	public boolean isLimitedQuirksMode() {
		return this.quirksMode == QuirksMode.limitedQuirks;
	}

	public CDATASection createCDATASection(String data) {
		return new CDATASection(data);
	}

	public Comment createComment(String data) {
		return new Comment(data);
	}

	public DocumentType createDocumentType(String name, String publicId,
			String systemId) {
		return new DocumentType(name, publicId, systemId);
	}

	// DocumentFragment createDocumentFragment()
	public Element createElement(String tagName) {
		return new Element(tagName);
	}

	public Element createElementNS(String namespaceURI, String qualifiedName) {
		return new Element(qualifiedName, namespaceURI);
	}

	public Text createTextNode(String data) {
		return new Text(data);
	}

	@Override
	public String getNodeName() {
		return "#document";
	}

	@Override
	public String getNodeValue() {
		return null;
	}

	@Override
	public void setNodeValue(String nodeValue) {
	}

	@Override
	public String getLocalName() {
		return null;
	}

	public Node importNode(Node node, boolean deep) {
		Node newNode = node.clone();
		newNode.parentNode = null;
		if (!deep)
			newNode.childNodes = null;

		return newNode;
	}

	@Override
	public String getOuterHtml() {
		return this.serialize(false);
	}

	public String getOuterHtml(Boolean prettyPrint) {
		return this.serialize(prettyPrint);
	}

	private String serialize(boolean prettyPrint) {
		String str = "";
		int ancestors = 0;
		if (this.getFirstChild() == null)
			return "";
		Node parent = this;
		Node current = this.getFirstChild();
		Node next = null;
		for (;;) {
			if (prettyPrint)
				str += indent(ancestors) + current.getOuterHtml() + "\n";
			else
				str += current.getOuterHtml();

			if (current.getNodeType() == NodeType.ELEMENT_NODE) {
				next = current.getFirstChild();
				if (null != next) {
					parent = current;
					current = next;
					ancestors++;
					continue;
				}
			}
			for (;;) {
				if (current.getNodeType() == NodeType.ELEMENT_NODE) {
					String endTag = ((Element) current).getEndTagHtml();
					if (!endTag.equals("")) {
						if (prettyPrint)
							str += indent(ancestors) + endTag + "\n";
						else
							str += endTag;
					}
				}

				next = current.getNextSibling();
				if (next != null) {
					current = next;
					break;
				}
				current = current.getParentNode();
				parent = parent.getParentNode();
				ancestors--;
				if (current == this) {
					return str;
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
}