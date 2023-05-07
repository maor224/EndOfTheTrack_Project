package com.example.endofthetrack_project.Model.AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A class representing a node in a game tree used for AI algorithm.
 * Each node contains a state, a reference to its parent node,
 * and a list of its child nodes.
 */
public class Node {
    // the state of the game at this node
    State state;
    // the parent node of this node
    Node parent;
    // the child nodes of this node
    List<Node> childArray;

    /**
     * Constructor for creating a new node with an empty state and no parent or children.
     */
    public Node() {
        this.state = new State();
        childArray = new ArrayList<>();
    }

    /**
     * Constructor for creating a new node with a given state and no parent or children.
     * @param state the state of the game at this node
     */
    public Node(State state) {
        this.state = new State(state);
        childArray = new ArrayList<>();
    }

    /**
     * Constructor for creating a new node with a given state, parent, and children.
     * @param state the state of the game at this node
     * @param parent the parent node of this node
     * @param childArray the child nodes of this node
     */
    public Node(State state, Node parent, List<Node> childArray) {
        this.state = state;
        this.parent = parent;
        this.childArray = childArray;
    }

    /**
     * Copy constructor for creating a new node that is a copy of an existing node.
     * @param node the existing node to be copied
     */
    public Node(Node node) {
        this.childArray = new ArrayList<>();
        this.state = new State(node.getState());
        if (node.getParent() != null)
            this.parent = node.getParent();
        List<Node> childArray = node.getChildArray();
        for (Node child : childArray) {
            this.childArray.add(new Node(child));
        }
    }

    /**
     * Getter method for the state of the game at this node.
     * @return the state of the game at this node
     */
    public State getState() {
        return state;
    }

    /**
     * Setter method for the state of the game at this node.
     * @param state the new state of the game at this node
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Getter method for the parent node of this node.
     * @return the parent node of this node
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Setter method for the parent node of this node.
     * @param parent the new parent node of this node
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * Getter method for the child nodes of this node.
     * @return the child nodes of this node
     */
    public List<Node> getChildArray() {
        return childArray;
    }

    /**
     * Setter method for the child nodes of this node.
     * @param childArray the new child nodes of this node
     */
    public void setChildArray(List<Node> childArray) {
        this.childArray = childArray;
    }

    /**
     * This method returns a random child node from the list of child nodes of the current node.
     * <p>
     *     The efficiency of this method is O(1) because it only requires generating
     *     a random number and retrieving an element from an array list,
     *     which can be done in constant time.
     * <p>
     *     The memory efficiency is also O(1) because it does not create any new data structures.
     * </p>
     * @return A random child node from the list of child nodes.
     */
    public Node getRandomChildNode() {
        int numOfPossibleMoves = this.childArray.size();
        int selectRandom = (int) (Math.random() * numOfPossibleMoves);
        return this.childArray.get(selectRandom);
    }

    /**
     * Returns the child node with the highest visit count.
     * <p>
     * The function first sorts the child nodes of this node using
     * the visit count of each child node as the sorting key.
     * <p>
     * It then returns the child node with the highest visit count (the first element of the sorted list).
     * <p>
     * Time complexity: O(n log n), where n is the number of child nodes.
     * <p>
     * Space complexity: O(1), as the function does not use any additional memory besides the input list of child nodes.
     *
     * @return Node The child node with the highest visit count.
     */
    public Node getChildWithMaxScore() {
        return Collections.max(this.childArray, Comparator.comparing(c ->
                c.getState().getVisitCount()));
    }
}
