import java.util.*;
import java.lang.*;

class Player {
  public char symbol;
  public String name;

  /***** Criação de um Player *****/
  public Player(char symbol, String name) {
    this.symbol = symbol;
    this.name = name;
  }
  public Player(Player player) {
    symbol = player.getSymbol();
    name = player.getName();
  }

  /***** Getters *****/
  public char getSymbol() {
    return symbol;
  }
  public String getName() {
    return name;
  }
}
