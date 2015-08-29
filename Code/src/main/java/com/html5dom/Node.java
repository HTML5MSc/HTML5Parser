package com.html5dom;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Node implements Cloneable {
	public enum NodeType {
		ELEMENT_NODE, ATTRIBUTE_NODE, TEXT_NODE, CDATA_SECTION_NODE, COMMENT_NODE, DOCUMENT_NODE, DOCUMENT_TYPE_NODE, DOCUMENT_FRAGMENT_NODE
	}

	NodeType nodeType;
	Node parentNode;
	List<Node> childNodes;
	int siblingIndex;
	String namespaceURI;
	Map<String, Object> userData;

	protected Node(NodeType nodeType, String namespaceURI) {
		super();
		this.nodeType = nodeType;
		this.namespaceURI = namespaceURI;
		this.childNodes = new ArrayList<Node>();
		this.userData = new LinkedHashMap<String, Object>();
	}

	protected Node(NodeType nodeType) {
		this(nodeType, null);
	}

	public NodeType getNodeType() {
		return this.nodeType;
	}

	protected int getSiblingIndex() {
		return siblingIndex;
	}

	protected void setSiblingIndex(int siblingIndex) {
		this.siblingIndex = siblingIndex;
	}

	public abstract String getLocalName();
	
	public abstract String getNodeName();

	public abstract String getNodeValue();

	public abstract void setNodeValue(String nodeValue);

	public abstract String getOuterHtml();

	public Node appendChild(Node newChild) {
		return addChild(newChild, this.childNodes.size());
	}

	public void insertBefore(Node newChild, Node refChild) {
		if (refChild != null) {
			int position = refChild.getSiblingIndex();
			addChild(newChild, position);
		} else
			appendChild(newChild);
	}

	public Node removeChild(Node oldChild) {
		if (!childNodes.contains(oldChild))
			return null;

		childNodes.remove(oldChild);
		resetSiblingIndexes();

		oldChild.parentNode = null;
		return oldChild;
	}

	private Node addChild(Node newChild, int position) {
		if (childNodes.contains(newChild))
			childNodes.remove(newChild);

		childNodes.add(position, newChild);
		reparentChild(newChild);
		resetSiblingIndexes();

		return newChild;
	}

	private void resetSiblingIndexes() {
		for (int i = 0; i < this.childNodes.size(); i++) {
			childNodes.get(i).setSiblingIndex(i);
		}
	}

	public List<Node> getChildNodes() {
		return this.childNodes;
	}

	public Node getFirstChild() {
		if (!this.childNodes.isEmpty())
			return this.childNodes.get(0);
		else
			return null;
	}

	public Node getLastChild() {
		if (!this.childNodes.isEmpty())
			return this.childNodes.get(this.childNodes.size() - 1);
		else
			return null;
	}

	public Node getParentNode() {
		return this.parentNode;
	}
	
	 protected void setParentNode(Node parentNode) {
	        if (this.parentNode != null)
	            this.parentNode.removeChild(this);
	        this.parentNode = parentNode;
	    }
	
	protected void reparentChild(Node child) {
        if (child.parentNode != null)
            child.parentNode.removeChild(child);
        child.setParentNode(this);
    }

	public String getNamespaceURI() {
		return this.namespaceURI;
	}

	public Node getNextSibling() {
		if (parentNode == null)
			return null; // root

		final List<Node> siblings = parentNode.childNodes;
		final int index = siblingIndex + 1;
		if (siblings.size() > index)
			return siblings.get(index);
		else
			return null;
	}

	public Node getPreviousSibling() {
		if (parentNode == null)
			return null; // root

		if (siblingIndex > 0)
			return parentNode.childNodes.get(siblingIndex - 1);
		else
			return null;
	}

	public Object getUserData(String key) {
		return this.userData.get(key);
	}

	public void setUserData(String key, Object value) {
		this.userData.put(key, value);
	}

	public boolean hasChildNodes() {
		return !this.childNodes.isEmpty();
	}

	public Document getOwnerDocument() {
		if (this instanceof Document)
			return (Document) this;
		else if (parentNode == null)
			return null;
		else
			return parentNode.getOwnerDocument();
	}

	@Override
    public Node clone() {
        Node thisClone = doClone(null);

        // Queue up nodes that need their children cloned.
        LinkedList<Node> nodesToProcess = new LinkedList<Node>();
        nodesToProcess.add(thisClone);

        while (!nodesToProcess.isEmpty()) {
            Node currParent = nodesToProcess.remove();

            for (int i = 0; i < currParent.childNodes.size(); i++) {
                Node childClone = currParent.childNodes.get(i).doClone(currParent);
                currParent.childNodes.set(i, childClone);
                nodesToProcess.add(childClone);
            }
        }

        return thisClone;
    }

    /*
     * Return a clone of the node using the given parent (which can be null).
     * Not a deep copy of children.
     */
    protected Node doClone(Node parent) {
        Node clone;

        try {
            clone = (Node) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        clone.parentNode = parent; // can be null, to create an orphan split
        clone.siblingIndex = parent == null ? 0 : siblingIndex;
        clone.nodeType = nodeType;
        clone.namespaceURI = namespaceURI;
        clone.childNodes = new ArrayList<Node>(childNodes.size());

        for (Node child: childNodes)
            clone.childNodes.add(child);

        return clone;
    }
}