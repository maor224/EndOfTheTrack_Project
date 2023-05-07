package com.example.endofthetrack_project.Model.AI;


/**
 * The Tree class represents a tree structure, consisting of a root node and child nodes.
 */
public class Tree {
    // the root node of the tree
    Node root;

    /**
     * Constructs an empty tree with a root node.
     */
    public Tree() {
        root = new Node();
    }

    /**
     * Constructs a tree with a given root node.
     * @param root The root node of the tree.
     */
    public Tree(Node root) {
        this.root = root;
    }

    /**
     * Returns the root node of the tree.
     * @return The root node of the tree.
     */
    public Node getRoot() {
        return root;
    }

    /**
     * Sets the root node of the tree.
     * @param root The new root node of the tree.
     */
    public void setRoot(Node root) {
        this.root = root;
    }

    /**
     * Adds a child node to a given parent node.
     * @param parent The parent node to which the child node will be added.
     * @param child The child node to be added.
     */
    public void addChild(Node parent, Node child) {
        parent.getChildArray().add(child);
    }

}
