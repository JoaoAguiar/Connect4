import java.util.*;

class Connect4 {
  static Player player1;
  static Player player2;

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    System.out.println();
    System.out.println("************************");
    System.out.println("***** CONNECT FOUR *****");
    System.out.println("************************");
    System.out.println();
    System.out.println("Who do you want to play with?");
    System.out.println("1) Computer");
    System.out.println("2) Human");
    System.out.println();
    System.out.print(">>> ");
    int opponent = choice(input, 2);

    if (opponent == 1) {
      HumanAgainstComputer(input);
    } else {
      HumanAgainstHuman(input);
    }
  }

  // Faz print ao tabuleiro de jogo
  public static void drawBoard(char board[][]) {
    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 7; j++) {
        if (j == 0) {
          System.out.println("     ");
        }

        System.out.print(" " + board[i][j] + " ");

        if (j == 6) {
          System.out.println();
        }
      }
    }

    System.out.println(" 1  2  3  4  5  6  7 ");
    System.out.println();
  }

  // Verifica se as escolhas foram corretas
  public static int choice(Scanner input, int nOptions) {
    int choice = input.nextInt();

    if (nOptions == 2) {
      if (choice != 1 && choice != 2) {
        System.out.println();
        System.out.println("You chose an invalid number, try again!!");
        System.out.print(">>> ");
        choice = choice(input, 2);
      }
    } else {
      if (choice != 1 && choice != 2 && choice != 3) {
        System.out.println();
        System.out.println("You chose an invalid number, try again!!");
        System.out.print(">>> ");
        choice = choice(input, 3);
      }
    }

    return choice;
  }

  // Criação dos players
  public static void createPlayers(int symbol, String player1_name, String player2_name, int n) {
    if (n == 1) {
      if (symbol == 1) {
        player2 = new Player('X', player2_name);
        player1 = new Player('O', player1_name);
      } else {
        player2 = new Player('O', player2_name);
        player1 = new Player('X', player1_name);
      }
    } else if (n == 2) {
      if (symbol == 1) {
        player1 = new Player('X', player1_name);
        player2 = new Player('O', player2_name);
      } else {
        player1 = new Player('O', player1_name);
        player2 = new Player('X', player2_name);
      }
    }
  }

  /***** Jogo *****/
  // Jogar Humano vs Humano
  public static void HumanAgainstHuman(Scanner input) {
    System.out.println();
    System.out.println("Player 1 name: ");
    String player1_name = input.next();
    System.out.println("Player 2 name: ");
    String player2_name = input.next();

    // Escolha do simbolo
    System.out.println();
    System.out.println("Wich symbol do you want to play " + player1_name + ":");
    System.out.println("1) X");
    System.out.println("2) O");
    System.out.println();
    System.out.print(">>> ");
    int symbol = choice(input, 2);

    // Escolha de quem joga primeiro
    System.out.println();
    System.out.println("Do you want to play first " + player1_name + ":");
    System.out.println("1) Yes");
    System.out.println("2) No");
    System.out.println();
    System.out.print(">>> ");
    int currentPlayer = choice(input, 2);

    // Cria os Players
    // O '2' significa que são 2 humanos
    createPlayers(symbol, player1_name, player2_name, 2);

    System.out.println();
    System.out.println();
    System.out.println("*** LET'S PLAY! ***");

    // Criação do tabuleiro
    GameBoard game_board = new GameBoard('_');

    game_board.setPlayers(player1, player2);
    char[][] board = game_board.getBoard();
    int round = 1;

    // Só pára quando alguem ganhar ou se empatarem
    while (game_board.checkGame() == 0) {
      System.out.println();
      System.out.println("*** ROUND " + round + " ***");
      System.out.println();

      drawBoard(board);

      System.out.println("It's " + game_board.getPlayer(currentPlayer).getName() + "'s turn");
      System.out.print("Were to play: ");
      int move = input.nextInt();

      while(!game_board.validMove(move)) {
        System.out.println();
        System.out.println("You chose an invalid move, try again!!");
        System.out.print(">>> ");
        move = input.nextInt();
      }

      game_board.makeMove(move, currentPlayer);
      game_board.setGameScore(game_board.score(currentPlayer));
      board = game_board.getBoard();
      round += 1;

      if(currentPlayer == 1) {
        currentPlayer = 2;
      }
      else {
        currentPlayer = 1;
      }
    }

    System.out.println("*** GAME OVER ***");
    System.out.println();

    drawBoard(board);

    // Verificar que Player ganhou, ou se acabou em empate 
    if(game_board.checkGame() == 1) {
      System.out.println("The winner is " + game_board.getPlayer(1).getName());
    }
    else if(game_board.checkGame() == -1) {
      System.out.println("The winner is " + game_board.getPlayer(2).getName());
    }
    else if(game_board.checkGame() == 2) {
      System.out.println("It's a draw");
    }
  }
  // Jogar Humano vs Computador
  public static void HumanAgainstComputer(Scanner input) {
    String player2_name = "Human";
    String player1_name = "Computer";
    TypesOfSearch search = new TypesOfSearch();

    // Escolha do simbolo
    System.out.println();
    System.out.println("Symbol you want to play with:");
    System.out.println("1) X");
    System.out.println("2) O");
    System.out.println();
    System.out.print(">>> ");
    int symbol = choice(input, 2);

    // Escolher que joga primeiro
    System.out.println();
    System.out.println("Who plays first:");
    System.out.println("1) Computer");
    System.out.println("2) You");
    System.out.println();
    System.out.print(">>> ");
    int currentPlayer = choice(input, 2);

    // Escolher o algoritmo com que o Computador vai se basear
    System.out.println();
    System.out.println("Algorithm to be used by the computer in the game:");
    System.out.println("1) MinMax");
    System.out.println("2) AlfaBeta");
    System.out.println("3) Monte-Carlo");
    System.out.println();
    System.out.print(">>> ");
    int algorithm = choice(input, 3);

    // Profundidade maxima da árvore que vai ser criada pelo Computador
    System.out.println();
    System.out.print("Maximum depth to be used in the algorithm: ");
    int depth = input.nextInt();

    // Cria os Players
    // O '1' significa que é 1 humanos
    createPlayers(symbol, player1_name, player2_name, 1);

    System.out.println();
    System.out.println();
    System.out.println("*** LET'S PLAY! ***");

    // Cria o tabuleiro de jogo
    GameBoard game_board = new GameBoard('_');

    game_board.setPlayers(player1, player2);
    game_board.setPreviousMove(0);
    char[][] board = game_board.getBoard();
    int move = 0;
    int round = 1;

    // Só pára quando alguem ganhar ou se empatarem 
    while(game_board.checkGame() == 0) {
      System.out.println("*** ROUND " + round + " ***");
      System.out.println();

      drawBoard(board);

      if(currentPlayer == 1) {
        System.out.println("It's Computer turn");

        // MiniMax
        if(algorithm == 1) {
          move = search.minimax(depth, game_board);
        }
        // Alpha-Beta
        else if(algorithm == 2) {
          move = search.alpha_beta(depth, game_board);
        }
        // Monte Carlo Tree Search
        else {
          move = search.monte_carlo_tree_search(depth, game_board, currentPlayer);
        }

        game_board.makeMove(move, currentPlayer);
        game_board.setGameScore(game_board.score(currentPlayer));
        board = game_board.getBoard();
        currentPlayer = 2;
        round += 1;
      }
      else {
        System.out.println("It's your turn");
        System.out.print("Choose your coordinate to play: ");
        move = input.nextInt();

        while(!game_board.validMove(move)) {
          System.out.println();
          System.out.println("You chose an invalid number, try again!!");
          System.out.print(">>> ");
          move = input.nextInt();
        }

        game_board.makeMove(move, currentPlayer);
        game_board.setGameScore(game_board.score(currentPlayer));
        board = game_board.getBoard();
        currentPlayer = 1;
        round += 1;
      }
    }

    System.out.println("*** GAME OVER ***");
    System.out.println();

    drawBoard(board);

    // Verificar que Player ganhou, ou se acabou em empate 
    if(game_board.checkGame() == 1) {
      System.out.println("The winner is the Computer");
    }
    else if(game_board.checkGame() == -1) {
      System.out.println("The winner is you!");
    }
    else if(game_board.checkGame() == 2) {
      System.out.println("It's a draw");
    }
  }
}