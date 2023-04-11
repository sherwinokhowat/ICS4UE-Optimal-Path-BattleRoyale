import java.util.Scanner;
import java.io.File;
import java.io.IOException;

/**
 * [GameMap.java] Represents a map which is a matrix from a specified file that
 * contains information about the game map, including the original map, the
 * optimal path to reach the centre of the map, and the optimal amount of loot
 * that can be collected.
 *
 * @version 1.0
 * @author Sherwin Okhowat
 */
public class GameMap {
  /**
   * Current map represented as a 2D char array.
   */
  private char[][] map;

  /**
   * The row index of the starting position ('p') on the map.
   */
  private int startRow;

  /**
   * The column index of the starting position ('p') on the map.
   */
  private int startCol;

  /**
   * The length of the map
   */
  private int mapLength;

  /**
   * Loads a map: loads the original map, and
   * loads the optimal path.
   *
   * @param fileName The name of the file containing the map data.
   * @throws InvalidMapException Checks for either an empty map or a map that does
   *                             not exist
   */
  public void load(String fileName) throws InvalidMapException {
    mapLength = 0;
    File file = new File(fileName);
    if (!file.exists()) {
      throw new InvalidMapException();
    }

    initializeMap(fileName);

    if (mapLength == 0) {
      throw new InvalidMapException();
    }

    this.printOriginalMap();
    PathOptimizer pathOptimizer = new PathOptimizer(startRow, startCol, mapLength, map);
    long start = System.nanoTime();
    pathOptimizer.printOptimalPath();
    long end = System.nanoTime();
    System.out.println((end - start) / 1000000 + "ms");

  }

  /**
   * Initializes the map by reading in data from the specified file.
   *
   * @param fileName The name of the file containing the map data.
   */
  private void initializeMap(String fileName) {
    startRow = -1;
    startCol = -1;

    try {
      calculateMapLength(fileName);
      this.map = new char[this.mapLength][this.mapLength];
      populateMap(fileName);
    } catch (IOException e) {
      System.err.println("\nError reading file: " + fileName + "\n");
      e.printStackTrace();
    }
  }

  /**
   * Counts the length of the map by reading the first line of the file.
   *
   * @param fileName The name of the file containing the map data.
   * @throws IOException If an error occurs while reading the file.
   */
  private void calculateMapLength(String fileName) throws IOException {
    Scanner scanner = new Scanner(new File(fileName));
    if (!scanner.hasNext()) {
      return;
    }
    String str = scanner.nextLine();
    str = str.replaceAll("\\s", "");

    this.mapLength = str.length();
    scanner.close();
  }

  /**
   * Fills the map 2D array with the characters from the given file.
   *
   * @param fileName The name of the file containing the map data.
   * @throws IOException If an error occurs while reading the file.
   */
  private void populateMap(String fileName) throws IOException {
    Scanner scanner = new Scanner(new File(fileName));
    String line;

    for (int r = 0; r < this.mapLength; r++) {
      line = scanner.nextLine();
      line = line.replaceAll("\\s", "");
      for (int c = 0; c < this.mapLength; c++) {
        this.map[r][c] = line.charAt(c);
        if ((this.map[r][c] == 'p') || (this.map[r][c] == 'P')) {
          this.startRow = r;
          this.startCol = c;
        }
      }
    }
    scanner.close();
  }

  /**
   * Prints the original map to the console.
   */
  private void printOriginalMap() {
    System.out.println("\nOriginal map: ");

    // Print the map to the console
    for (int r = 0; r < this.mapLength; r++) {
      for (int c = 0; c < this.mapLength; c++) {
        System.out.print(this.map[r][c] + " ");
      }
      System.out.println();
    }
  }

}