import java.util.*;

class GameBoard {
  public char[][] board;
  public int previousMove;
  public int gameScore;
  public int numberSimulations;
  public int numberVictory;
  public Player player1;
  public Player player2;
  public LinkedList<GameBoard> children;
  public GameBoard board_father;

  /***** Criação do tabuleiro *****/
  // Tabuleiro só com o caracter 'character'
  public GameBoard(char character) {
    board = new char[6][7];
    gameScore = 0;

    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 7; j++) {
        board[i][j] = character;
      }
    }
  }

  // Tabuleiro só com as mesmas caracteristicas que a matriz 'board'
  public GameBoard(char board[][]) {
    this.board = new char[6][7];

    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 7; j++) {
        this.board[i][j] = board[i][j];
      }
    }
  }

  /***** Setters *****/
  // Players em jogo
  public void setPlayers(Player player1, Player player2) {
    this.player1 = new Player(player1.getSymbol(), player1.getName());
    this.player2 = new Player(player2.getSymbol(), player2.getName());
  }

  // Move anterior
  public void setPreviousMove(int move) {
    previousMove = move;
  }

  // "Avaliação" do tabuleiro
  public void setGameScore(int gameScore) {
    this.gameScore = gameScore;
  }

  // Numero de vitorias nas simulações (Monte Carlo Tree Search)
  public void setNumberVictory(int value) {
    this.numberVictory += value;
  }

  // Numero de simulações feitas (Monte Carlo Tree Search)
  public void setNumberSimulations() {
    this.numberSimulations++;
  }

  /***** Getters *****/
  // Tabuleiro de jogo
  public char[][] getBoard() {
    return board;
  }

  // Players em jogo
  public Player getPlayer(int currentPlayer) {
    if (currentPlayer == 1) {
      return player1;
    }

    return player2;
  }

  // Move anterior
  public int getPreviousMove() {
    return previousMove;
  }

  // "Avaliação" do tabuleiro
  public int getGameScore() {
    return gameScore;
  }

  // Numero de vitorias nas simulações (Monte Carlo Tree Search)
  public int getNumberVictory() {
    return numberVictory;
  }

  // Numero de simulações feitas (Monte Carlo Tree Search)
  public int getNumberSimulations() {
    return numberSimulations;
  }

  /***** Funções Auxiliares *****/
  // Verifica se o move é possivelisn't
  public boolean validMove(int move) {
    if (move > 7 || move < 0) {
      return false;
    }

    for (int i = 0; i < 6; i++) {
      if (board[i][move - 1] != '_') {
        return false;
      } else {
        break;
      }
    }

    return true;
  }

  // Faz o move de um certo player
  public void makeMove(int move, int currentPlayer) {
    for(int i = 0; i < 6; i++) {
      if(board[i][move - 1] != '_') {
        board[i][move - 1] = getPlayer(currentPlayer).getSymbol();
        setPreviousMove(move);
      }
    }

    numberSimulations = 0;
    numberVictory = 0;
  }

  // Calcula a "avaliação" do tabuleiro
  public int score(int currentPlayer) {
    int score = checkHorizontal() + checkVertical() + checkDiagonal1() + checkDiagonal2();
  
    if(currentPlayer == 1) {
      score = score + 16;
    }
    else if(currentPlayer == 2) {
      score = score - 16;
    }
  
    return score;
  }

  public int checkScore(String line) {
    char symbol1 = getPlayer(1).getSymbol();
    char symbol2 = getPlayer(2).getSymbol();
    int numberSymbol1 = 0;
    int numberSymbol2 = 0;

    for(int i=0; i<line.length(); i++) {
      if(line.charAt(i) == symbol2) {
        numberSymbol2++;
      }
      else if(line.charAt(i) == symbol1) {
        numberSymbol1++;
      }
    }

      if(numberSymbol2 == 1 && numberSymbol1 == 0) { 
        return -1; 
      }
      else if(numberSymbol2 == 2 && numberSymbol1 == 0) { 
        return -10; 
      }
      else if(numberSymbol2 == 3 && numberSymbol1 == 0) { 
        return -50; 
      }
      else if(numberSymbol2 == 4 && numberSymbol1 == 0) { 
        return -528; 
      }
      else if(numberSymbol2 == 0 && numberSymbol1 == 1) { 
        return 1; 
      }
      else if(numberSymbol2 == 0 && numberSymbol1 == 2) { 
        return 10; 
      }
      else if(numberSymbol2 == 0 && numberSymbol1 == 3) { 
        return 50; 
      }
      else if(numberSymbol2 == 0 && numberSymbol1 == 4) { 
        return 528; 
      }
      else { 
        return 0; 
      }
  }

  // Cria "filhos" para um nó da arvore
  public LinkedList<GameBoard> createChildren(int currentPlayer) {
    children = new LinkedList<GameBoard>();

    char[][] board = getBoard();
    GameBoard previous_board = new GameBoard(board);
    int move = 1;

    while(move < 8) {
      if(validMove(move) == true) {
        GameBoard new_board = new GameBoard(board);

        new_board.setPlayers(getPlayer(1),getPlayer(2));
        new_board.makeMove(move, currentPlayer);
        board_father = previous_board;

        children.add(new_board);
      }

      move++;
    }

    return children;
  }
  
  // Possiveis "filhos" de um tabuleiro
  public List<Node> possibleChildrens(GameBoard board, int currentPlayer) {
    LinkedList<Integer> possibleActions = new LinkedList<Integer>();
    LinkedList<Node> newChildrens = new LinkedList<Node>();

    for(int i=1; i<8; i++) {
      if(board.validMove(i)) {
        possibleActions.add(i);
      }
    }
    for(int action : possibleActions) {
      board.makeMove(action, currentPlayer);
      Node children = new Node(board);
      newChildrens.add(children);
    }

    return newChildrens;
  }

  // Faz uma copia exata do tabuleiro
  public void copyBoard(GameBoard target, GameBoard board) {
    GameBoard new_board = new GameBoard(board.board);
    target = new_board;

    target.setPlayers(getPlayer(1), getPlayer(2));
    target.setPreviousMove(getPreviousMove());
    target.setGameScore(getGameScore());
  }

  // Vai verificar se há algum acontecimento no jogo
  // Vitoria do player1 (1), vitoria do player 2 (-1), empate (2) ou nada (0)
  public int checkGame() {
    if(checkDraw()) {
      return 2;
    }
    else if(checkHorizontal() >= 512 || checkVertical() >= 512 || checkDiagonal1() >= 512 || checkDiagonal2() >= 512) {
      return 1;
    }
    else if (checkHorizontal() <= -512 || checkVertical() <= -512 || checkDiagonal1() <= -512 || checkDiagonal2() <= -512) {
      return -1;
    }
    else {
      return 0;
    }
  }

  // Verifica se há um empate
  public Boolean checkDraw() {
    if(gameScore != 16) {
      return false;
    }

    for(int i=0; i<7; i++) {
      if(board[i][5] == ' ') {
        return false;
      }
    }
  
    return true;
  }
  // Verifica os pontos nas linas
  public int checkHorizontal() {
    int lineScore = 0;
    String horizontalLine = "";
  
    for (int i=0; i<6; i++){
      for(int j=0; j<4; j++){
        horizontalLine += board[j][i];
        horizontalLine += board[j+1][i];
        horizontalLine += board[j+2][i];
        horizontalLine += board[j+3][i];
  
        lineScore += checkScore(horizontalLine);
  
        horizontalLine = "";
      }
    }
  
    return lineScore;
  }  
  // Verifica os pontos nas colunas
  public int checkVertical() {
    int lineScore = 0;
    String verticalLine = "";
  
    for (int i=0; i<7; i++) {
      for(int j=0; j<3; j++) {
        verticalLine += board[i][j];
        verticalLine += board[i][j+1];
        verticalLine += board[i][j+2];
        verticalLine += board[i][j+3];

        lineScore += checkScore(verticalLine);

        verticalLine = "";
      }
    }
  
    return lineScore;
  }
  // Verifica os pontos nas diagonais \
  public int checkDiagonal1() {
    int lineScore = 0;
    int possibleDiagonals[] = new int[] {1,2,3,3,2,1};
    int auxX[] = new int[] {0,-1,-2,-2,-2,-2};
    int auxY[] = new int[] {3,4,5,5,5,5};
    String diagonalLine = "";

    for(int i=0; i<6; i++) {
      int aux = 0;
  
      for(int j=0 ; j<possibleDiagonals[i]; j++) {
        diagonalLine += this.board[i+auxX[i]+j][j+auxY[i]-aux];
        diagonalLine += this.board[i+auxX[i]+j+1][j+auxY[i]-aux-1];
        diagonalLine += this.board[i+auxX[i]+j+2][j+auxY[i]-aux-2];
        diagonalLine += this.board[i+auxX[i]+j+3][j+auxY[i]-aux-3];
  
        lineScore += checkScore(diagonalLine);
  
        diagonalLine = "";
        aux += 2;
      }
    }
  
    return lineScore;
  }
  // Verifica os pontos nas diagonais /
  public int checkDiagonal2() {
    int lineScore = 0;
    int possibleDiagonals[] = new int[] {1, 2, 3, 3, 2, 1};
    int auxX[] = new int[] {0, -1, -2, -2, -2, -2};
    int auxY[] = new int[] {2, 1, 0, 0, 0, 0};
    String diagonalLine = ""; 

    for(int i=0; i<6; i++) {
      for(int j=0 ; j<possibleDiagonals[i]; j++) {
        diagonalLine += board[i+auxX[i]+j][j+auxY[i]];
        diagonalLine += board[i+auxX[i]+j+1][j+auxY[i]+1];
        diagonalLine += board[i+auxX[i]+j+2][j+auxY[i]+2];
        diagonalLine += board[i+auxX[i]+j+3][j+auxY[i]+3];
  
        lineScore += checkScore(diagonalLine);
  
        diagonalLine = "";
      }
    }
  
    return lineScore;
  }  
}
