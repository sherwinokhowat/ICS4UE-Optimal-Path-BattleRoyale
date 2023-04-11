import java.util.Set;
import java.util.HashSet;

/**
 * [PathOptimizer.java] Class used to find the optimal path of a battle royale map. 
 *
 * @version 1.0
 * @author Sherwin Okhowat
 */
public class PathOptimizer {
  /**
   * The original map in a 2D char array.
   */
  private char[][] originalMap;

  /**
   * The optimal path in a 2D char array.
   */
  private char[][] optimalPath;

  /**
   * The row index of the starting position ('p') on the map.
   */
  private int startRow;

  /**
   * The column index of the starting position ('p') on the map.
   */
  private int startCol;

  /**
   * The length of the map.
   */
  private int mapLength;

  /**
   * The optimal loot.
   */
  private int optimalLoot;

  /**
   * The optimal number of items located.
   */
  private int optimalItemsLooted;

  /**
   * The optimal number of steps taken.
   */
  private int optimalSteps;

  /**
   * Set which stores the possible states and is used for memoization / dynamic
   * programming
   */
  private Set<String> memo;

  /**
   * Constructor for the PathOptimizer class. Takes in four parameters which are
   * necessary to search for the optimal path in a given map. Sets default values
   * to the rest of the class instance variables
   * 
   * @param map       The original map used to traverse for an optimal path.
   * @param startRow  The row index of the starting position ('p') on the map.
   * @param startCol  The column index of the starting position ('p') on the map.
   * @param mapLength The length of the map.
   */
  public PathOptimizer(int startRow, int startCol, int mapLength, char[][] map) {
    this.startRow = startRow;
    this.startCol = startCol;
    this.mapLength = mapLength;
    this.optimalLoot = -1;
    this.optimalItemsLooted = -1;
    this.optimalSteps = -1;
    this.memo = new HashSet<String>();
    this.optimalPath = new char[this.mapLength][this.mapLength];
    this.originalMap = map;
  }

  /**
   * Recursively searches the original map for the optimal path to collect all
   * loot
   * while minimizing the number of steps taken. Updates the optimal path if a
   * better path is
   * found.
   *
   * @param row            The current row
   * @param col            The current column
   * @param boundaryLength The length of the boundary
   * @param loot           The amount of loot collected so far.
   * @param itemsLooted    The number of items looted so far.
   * @param stepsTaken     The number of steps taken so far.
   */
  private void findOptimalPath(int row, int col, int boundaryLength, int loot, int itemsLooted,
      int stepsTaken) {
    char temp;
    String state;
    int lootCollected = loot;

    // Base 1: check if player is out of bounds
    if (isOutOfBounds(row, col, boundaryLength)) {
      return;
    }
    
    // If the current spot has loot, add it to the lootCollected counter
    if (isLootable(row, col)) {
      lootCollected += (this.originalMap[row][col] - '0');
      itemsLooted++;
    }
    
    state = row + "," + col + "," + boundaryLength + "," + lootCollected + "," + itemsLooted
        + "," + stepsTaken;
    // Base case 2: check the current state for memoization
    if (this.memo.contains(state)) {
      return;
    }
    
    // Base case 3: check if the player is finished the game
    if (isFinished(row, col, boundaryLength)) {
      updateOptimalPath(lootCollected, stepsTaken, row, col, itemsLooted);
      return;
    }

    // save the current character in this sport before replacing it with a v
    // (visited)
    temp = this.originalMap[row][col];
    if ((this.originalMap[row][col] != 'p') && (this.originalMap[row][col] != 'P')) {
      this.originalMap[row][col] = 'v';
    }

    // recursively search every direction
    findOptimalPath((row + 1), col, (boundaryLength + 1), lootCollected, itemsLooted,
        stepsTaken + 1);
    findOptimalPath((row - 1), col, (boundaryLength + 1), lootCollected, itemsLooted,
        stepsTaken + 1);
    findOptimalPath(row, (col + 1), (boundaryLength + 1), lootCollected, itemsLooted,
        stepsTaken + 1);
    findOptimalPath(row, (col - 1), (boundaryLength + 1), lootCollected, itemsLooted,
        stepsTaken + 1);
    findOptimalPath(row, col, (boundaryLength + 1), lootCollected, itemsLooted, stepsTaken);

    // Restore the maps original character so it does not affect the recursive calls
    this.originalMap[row][col] = temp;
    this.memo.add(state);
  }

  /**
   * 
   * Updates the optimal path if a better loot is found or if the loot collected
   * is the same but
   * fewer steps were taken. The optimal path is updated with the current
   * position, total loot
   * collected, and items looted.
   * 
   * @param lootCollected the total amount of loot collected in the current path
   * @param stepsTaken    the total number of steps taken in the current path
   * @param row           the row of the current position in the map
   * @param col           the column of the current position in the map
   * @param itemsLooted   the total number of items looted in the current path
   */
  private void updateOptimalPath(int lootCollected, int stepsTaken, int row, int col, int itemsLooted) {
    if (lootCollected > optimalLoot) {
      updateOptimalData(lootCollected, stepsTaken, row, col, itemsLooted);
    } else if ((lootCollected == optimalLoot) && (stepsTaken < optimalSteps)) {
      updateOptimalData(lootCollected, stepsTaken, row, col, itemsLooted);
    }
  }

  /**
   * 
   * Updates the optimal data with the current position, total loot collected, and
   * items looted.
   * The map is copied to the optimal path and the current position is marked as
   * the final position.
   * 
   * @param lootCollected the total amount of loot collected in the current path
   * @param stepsTaken    the total number of steps taken in the current path
   * @param row           the row of the current position in the map
   * @param col           the column of the current position in the map
   * @param itemsLooted   the total number of items looted in the current path
   */
  private void updateOptimalData(int lootCollected, int stepsTaken, int row, int col, int itemsLooted) {
    this.optimalLoot = lootCollected;
    this.optimalSteps = stepsTaken;
    copyMapToOptimalPath();
    this.optimalPath[row][col] = 'F';
    this.optimalItemsLooted = itemsLooted;
  }

  /**
   * Copies the map to the optimal path.
   */
  private void copyMapToOptimalPath() {
    for (int i = 0; i < this.mapLength; i++) {
      for (int j = 0; j < this.mapLength; j++) {
        this.optimalPath[i][j] = this.originalMap[i][j];
      }
    }
  }

  /**
   * Determines whether the specified position is out of bounds of the current
   * search area
   *
   * @param row            The row of the current position
   * @param col            The column of the current position
   * @param boundaryLength The length of the boundary
   * @return true if the position is out of bounds, false otherwise.
   */
  private boolean isOutOfBounds(int row, int col, int boundaryLength) {
    if ((row >= (this.mapLength - boundaryLength)) || (col >= (this.mapLength - boundaryLength))
        || (row <= (boundaryLength - 1)) || (col <= (boundaryLength - 1))) {
      return true;
    }

    return false;
  }

  /**
   * Determines whether the specified position is in the centre of the map with no
   * where to go, which indicates that we are finished.
   *
   * @param row            The row of the current position
   * @param col            The column of the current position
   * @param boundaryLength The length of the boundary
   * @return true if the position is in the centre with no where to go, false
   *         otherwise.
   */
  private boolean isFinished(int row, int col, int boundaryLength) {
    if (((row + 1) >= (this.mapLength - boundaryLength)) && ((col + 1) >= (this.mapLength - boundaryLength))
        && ((row - 1) <= (boundaryLength - 1)) && ((col - 1) <= (boundaryLength - 1))) {
      return true;
    }
    return false;
  }

  /**
   * Determines whether the specified position can be looted.
   *
   * @param row The row of the current position
   * @param col The column of the current position
   * @return true if the position can be looted, false otherwise
   */
  private boolean isLootable(int row, int col) {
    if (Character.isDigit(this.originalMap[row][col])) {
      return true;
    }

    return false;
  }

  /**
   * Finds the best valid starting position on the map and sets the optimal path
   * as such.
   */
  private void findBestStartingPosition() {
    for (int row = 0; row < this.mapLength; row++) {
      for (int col = 0; col < this.mapLength; col++) {
        // Only start on positions that do not contain loot
        if (!Character.isDigit(originalMap[row][col])) {
          char temp = this.originalMap[row][col];
          memo = new HashSet<String>();
          this.originalMap[row][col] = 'P';
          findOptimalPath(row, col, 0, 0, 0, 0);
          this.originalMap[row][col] = temp;
        }
      }
    }
  }

  /**
   * Finds the optimal path to reach the centre of the map, and prints the path to
   * the console along with the optimal amount of loot that can be collected.
   */
  public void printOptimalPath() {
    if ((this.startRow != -1) && (this.startCol != -1)) {
      findOptimalPath(this.startRow, this.startCol, 0, 0, 0, 0);
    } else {
      findBestStartingPosition();
    }

    // Impossible to reach the end
    if (this.optimalLoot == -1) {
      System.out.println("No path to solution.");
      return;
    }

    System.out.println("\nOptimal path: ");
    for (char[] chars : this.optimalPath) {
      for (int j = 0; j < this.optimalPath[0].length; j++) {
        System.out.print(chars[j] + " ");
      }
      System.out.println();
    }

    System.out.println(this.optimalItemsLooted + " item(s) looted");
    System.out.println(this.optimalLoot + " total loot collected");
    System.out.println(this.optimalSteps + " steps taken");
  }
}