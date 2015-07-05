package com.html5dom;

public class CDATASection extends Node {

	private String data;

	protected CDATASection() {
		super(NodeType.CDATA_SECTION_NODE);
	}

	protected CDATASection(String data) {
		super(NodeType.CDATA_SECTION_NODE);
		this.data = data;
	}

	@Override
	public String getNodeName() {
		return "#cdata-section";
	}

	@Override
	public String getNodeValue() {
		return this.data;
	}

	@Override
	public void setNodeValue(String nodeValue) {
		this.data = nodeValue;
	}

	@Override
	public String getOuterHtml() {
		return "<![CDATA[" + (this.data == null ? "" : this.data) + "]]>";
	}

	@Override
	public String getLocalName() {
		return null;
	}
}