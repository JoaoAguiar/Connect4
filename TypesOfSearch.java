import java.util.*;

class TypesOfSearch {
  int total_nodes;

  /***** MiniMax *****/
  public int minimax(int depth, GameBoard initialGameBoard) {
    long inicialTime = System.currentTimeMillis();
    total_nodes = 0;

    GameBoard board = max_minimax(depth-1, initialGameBoard);

    long finalTime = System.currentTimeMillis() - inicialTime;

    System.out.println("Number of expanded nodes: " + total_nodes);
    System.out.println("Elapsed time: " + finalTime + "s");
    System.out.println();

    return board.getPreviousMove();
  }
  // Retorna o board (nó) com o maior valor
  public GameBoard max_minimax(int depth, GameBoard game_board) {
    if(game_board.checkGame() != 0 || depth == 0) {
      return game_board;
    }

    int minValue = -1000000;

    LinkedList<GameBoard> childrenList = new LinkedList<GameBoard>();
    childrenList = game_board.createChildren(1);
    
    GameBoard board = new GameBoard('_');

    for(GameBoard child_board : childrenList) {
      total_nodes += 1;
      GameBoard min_b = min_minimax(depth-1, child_board);
      int min = min_b.score(0);

      if(minValue < min) {
        minValue = min;
        board.copyBoard(board, child_board);
      }
    }

    return board;
  }
  // Retorna o board (nó) com o menor valor
  public GameBoard min_minimax(int depth, GameBoard game_board) {
    if(game_board.checkGame() != 0 || depth == 0) {
      return game_board;
    }

    int maxValue = 100000000;

    LinkedList<GameBoard> childrenList = new LinkedList<GameBoard>();
    childrenList = game_board.createChildren(2);

    GameBoard board = new GameBoard('_');

    for(GameBoard child_board : childrenList) {
      total_nodes += 1;
      GameBoard max_b = max_minimax(depth-1, child_board);
      int max = max_b.score(1);

      if(maxValue > max) {
        maxValue = max;
        board.copyBoard(board, child_board);
      }
    }

    return board;
  }

  /***** Alfa-Beta *****/
  public int alpha_beta(int depth, GameBoard initialGameBoard) {
    long inicialTime = System.currentTimeMillis();
    int alpha = -1000000;
    int beta = 1000000;
    total_nodes = 0;

    GameBoard board = max_alpha_beta(depth-1, alpha, beta, initialGameBoard);
    
    long finalTime = System.currentTimeMillis() - inicialTime;

    System.out.println("Number of expanded nodes: " + total_nodes);
    System.out.println("Elapsed time = " + finalTime + "s");
    System.out.println();
   
    return board.getPreviousMove();
  }
  // Retorna o board (nó) com o maior valor
  public GameBoard max_alpha_beta(int depth, int alpha, int beta, GameBoard game_board) {
    if(game_board.checkGame() != 0 || depth == 0) {
      return game_board;
    }

    int minValue = -100000000;

    LinkedList<GameBoard> childrenList = new LinkedList<GameBoard>();
    childrenList = game_board.createChildren(1);

    GameBoard board = new GameBoard('_');

    for(GameBoard child_board : childrenList) {
      total_nodes += 1;
      GameBoard min_b = min_alpha_beta(depth-1, alpha, beta, child_board);
      int min = min_b.score(1);

      if(minValue < min) {
        minValue = min;
        board.copyBoard(board, child_board);
      }
      if(minValue >= beta) {
        return board;
      }

      alpha = Math.max(alpha, minValue);
    }

    return board;
  }
  // Retorna o board (nó) com o menor valor
  public GameBoard min_alpha_beta(int depth, int alpha, int beta, GameBoard game_board) {
    if(game_board.checkGame() != 0 || depth == 0) {
      return game_board;
    }

    int maxValue = 100000000;

    LinkedList<GameBoard> childrenList = new LinkedList<GameBoard>();
    childrenList = game_board.createChildren(2);

    GameBoard board = new GameBoard('_');

    for(GameBoard child_board : childrenList) {
      total_nodes += 1;
      GameBoard max_b = max_alpha_beta(depth-1, alpha, beta, child_board);
      int max = max_b.score(1);

      if(maxValue > max) {
        maxValue = max;
        board.copyBoard(board, child_board);
      }
      if(maxValue <= alpha) {
        return board;
      }

      beta = Math.min(beta, maxValue);
    }

    return board;
  }

  /***** Monte Carlo Tree Search *****/
  public int monte_carlo_tree_search(int depth, GameBoard initialGameBoard, int currentPlayer) {
    long inicialTime = System.currentTimeMillis();
    total_nodes = 1;
    double upperConfidenceTrees = Double.MIN_VALUE;

    Node root = new Node(initialGameBoard);
    Node chosen = new Node(initialGameBoard);
    Node node;

    for(int i=0; i<depth; i++) {
      node = root;

      LinkedList<Node> visited = new LinkedList<Node>();
      visited.add(node);

      while(!node.isLeaf()) {
        for(Node children : node.getChildrens()) {
          total_nodes += 1;

          if(upperConfidenceTrees < selectUCB(children.getGameBoard().getNumberVictory(), children.getGameBoard().getNumberSimulations(), i)) {
            chosen = children;
            upperConfidenceTrees = selectUCB(children.getGameBoard().getNumberVictory(), children.getGameBoard().getNumberSimulations(), i);
          }
        }

        upperConfidenceTrees = Double.MIN_VALUE;
        node = chosen;
        visited.add(node);
      }

      upperConfidenceTrees = Double.MIN_VALUE;

      node.addChildrens(node.getGameBoard().possibleChildrens(node.getGameBoard(), currentPlayer));

      for(Node children : node.getChildrens()) {
        if(upperConfidenceTrees < selectUCB(children.getGameBoard().getNumberVictory(), children.getGameBoard().getNumberSimulations(), i)) {
          chosen = children;
          upperConfidenceTrees = selectUCB(children.getGameBoard().getNumberVictory(), children.getGameBoard().getNumberSimulations(), i);
        }
      }

      upperConfidenceTrees = Double.MIN_VALUE;
      int value = rollout(chosen.getGameBoard(), currentPlayer);

      GameBoard chosen_board = chosen.getGameBoard();
      chosen_board.setNumberVictory(value);
      chosen_board.setNumberSimulations();
      chosen.setGameBoard(chosen_board);

      for(Node fatherNode : visited) {
        chosen_board = fatherNode.getGameBoard();
        chosen_board.setNumberVictory(value);
        chosen_board.setNumberSimulations();
        fatherNode.setGameBoard(chosen_board);
      }
    }

    int choise = 0;

    Node new_node = new Node();

    for(Node auxNode : root.getChildrens()) {
      if(auxNode.getGameBoard().getNumberVictory() > choise) {
        choise = auxNode.getGameBoard().getNumberVictory();
        new_node = auxNode;
      }
    }

    long finalTime = System.currentTimeMillis() - inicialTime;

    System.out.println("Number of expanded nodes: " + total_nodes);
    System.out.println("Elapsed time: " + finalTime + "s");
    System.out.println();

    return new_node.getGameBoard().getPreviousMove();
  }
  // Valor do Upper Confidence Trees
  public double selectUCB(int numberVictory, int numberSimulations, int i) {
    if(numberSimulations == 0) {
      return Integer.MAX_VALUE;
    }

    return (double)((numberVictory/numberSimulations) + (Math.sqrt(2) * Math.sqrt(Math.log(i)/numberSimulations)));
  }
  // Simulação com jogadas random 
  public int rollout(GameBoard board, int currentPlayer) {
    while(board.checkGame() == 0) {
      int random = (int)(Math.round(Math.random()*6))/ 1;

      LinkedList<Integer> possibleActions = new LinkedList<Integer>();

        for(int i=1; i<8; i++) {
            if(board.validMove(i)) {
              possibleActions.add(i);
            }
        }

        if(possibleActions.contains(random)) {
          board.makeMove(random, currentPlayer);
        }
    }

    if(board.checkGame() == 1) {
      return 1;
    }
    else {
      return 0;
    }
  }
}