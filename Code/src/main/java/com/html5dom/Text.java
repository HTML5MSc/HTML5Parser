package com.html5dom;

public class Text extends Node {

	private String data;

	protected Text() {
		super(NodeType.TEXT_NODE);
	}

	protected Text(String data) {
		super(NodeType.TEXT_NODE);
		this.data = data;
	}

	@Override
	public String getNodeName() {
		return "#text";
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
		return this.data == null ? "" : this.data;
	}

	@Override
	public String getLocalName() {
		return null;
	}
}