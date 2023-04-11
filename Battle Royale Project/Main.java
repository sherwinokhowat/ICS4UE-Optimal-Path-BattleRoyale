import java.util.Scanner;

/**
 * [Main.java] Class used to run the program. Asks the user for a file name and
 * outputs the original map and the most optimal path.
 *
 * @version 1.0
 * @author Sherwin Okhowat
 */
class Main {
  public static void main(String[] args) {
    GameMap map = new GameMap();
    Scanner input = new Scanner(System.in);
    String file = "";
    boolean quit = false;

    while (!quit) {
      System.out.print("\nEnter the text file name (Type \"quit\" to quit): ");
      file = input.nextLine();
      if (!file.toLowerCase().equals("quit")) {
        try {
          map.load(file);
        } catch (InvalidMapException e) {
          System.out.println("\n***************************************************************");
          System.out.print("*\tThe map you chose is either empty or does not exist!      *");
          System.out.println("\n***************************************************************");

        }

      } else {
        quit = true;
      }
    }

    input.close();
  }
}