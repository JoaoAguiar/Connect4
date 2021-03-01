import java.util.*;

public class Node {
    public GameBoard board;
    public List<Node> childrens = new ArrayList<Node>();

    /***** Criação do Nó *****/
    public Node(GameBoard board) {
        this.board = board;
    }

    /***** Getters *****/
    // "Filhos" do nó
    public List<Node> getChildrens() {
        return childrens;
    }
    // Tabuleiro a que se refere o nó
    public GameBoard getGameBoard() {
        return board;
    }

    /***** Setters *****/
    // Tabuleiro a que se refere o nó
    public setGameBoard(GameBoard board) {
        this.board = board;
    }

    // Verifica se o nó é folha
    public Boolean isLeaf() {
        return childrens.isEmpty();
    }
}