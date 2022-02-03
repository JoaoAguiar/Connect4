import java.util.*;

public class Node {
    public GameBoard board;
    public List<Node> childrens = new ArrayList<Node>();

    /***** Criação do Nó *****/
    public Node(GameBoard board) {
        this.board = board;
    }
    public Node() {
        this.board = null;
    }

    /***** Getters *****/
    public List<Node> getChildrens() {
        return childrens;
    }
    public GameBoard getGameBoard() {
        return board;
    }

    /***** Setters *****/
    public void setGameBoard(GameBoard board) {
        this.board = board;
    }

    public Boolean isLeaf() {
        return childrens.isEmpty();
    }
}