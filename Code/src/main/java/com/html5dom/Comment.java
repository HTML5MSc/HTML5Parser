package com.html5dom;

public class Comment extends Node {

	private String data;

	protected Comment() {
		super(NodeType.COMMENT_NODE);
	}

	protected Comment(String data) {
		super(NodeType.COMMENT_NODE);
		this.data = data;
	}

	@Override
	public String getNodeName() {
		return "#comment";
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
		return "<!--" + (this.data == null ? "" : this.data) + "-->";
	}

	@Override
	public String getLocalName() {
		return null;
	}

}