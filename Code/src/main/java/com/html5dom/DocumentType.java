package com.html5dom;

public class DocumentType extends Node {

	private String name;
	private String publicId;
	private String systemId;

	public DocumentType(String name, String publicId, String systemId) {
		super(NodeType.DOCUMENT_TYPE_NODE);
		this.name = name;
		this.publicId = publicId;
		this.systemId = systemId;
	}

	@Override
	public String getNodeName() {
		return this.name;
	}

	@Override
	public String getNodeValue() {
		return null;
	}

	@Override
	public void setNodeValue(String nodeValue) {
	}

	public String getName() {
		return this.name;
	}

	public String getPublicId() {
		return this.publicId;
	}

	public String getSystemId() {
		return this.systemId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	@Override
	public String getOuterHtml() {
		String docname;
		String publicid;
		String systemid;
		docname = this.name == null ? "" : (" " + this.name);
		if (publicId == null && systemId == null) {
			publicid = "";
			systemid = "";
		} else {
			publicid = " \"" + (publicId == null ? "" : publicId) + "\"";
			systemid = " \"" + (systemId == null ? "" : systemId) + "\"";
		}

		return "<!DOCTYPE" + docname + publicid + systemid + ">";
	}

	@Override
	public String getLocalName() {
		return null;
	}
	
	public String getDefaultDocType(){
		return "<!DOCTYPE HTML>";
	}
}