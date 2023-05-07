package com.example.endofthetrack_project.Model.AI;

import java.util.Collections;
import java.util.Comparator;

/**
 * The UCT class is used to provide a method for the AI player
 * to evaluate and choose the best node in the game tree based on the UCT algorithm.
 */
public class UCT {

    /**
     * Calculates the UCT value for a given node based on its statistics.
     * @param totalVisit the total number of visits to the parent node
     * @param nodeWinScore the total win score of the node
     * @param nodeVisit the number of visits to the node
     * @return the calculated UCT value for the node
     */
    public static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return (nodeWinScore / (double) nodeVisit) +
                1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    /**
     * Finds the child node with the maximum UCT value of a given node.
     * @param node the parent node to evaluate
     * @return the child node with the maximum UCT value
     */
    static Node findBestNodeWithUCT(Node node) {
        int parentVisit = node.getState().getVisitCount();
        return Collections.max(
                node.getChildArray(),
                Comparator.comparing(c ->
                        uctValue(parentVisit, c.getState().getWinScore(),
                                c.getState().getVisitCount())));
    }

}
