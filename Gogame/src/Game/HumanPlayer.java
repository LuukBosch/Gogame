package Game;

import java.util.Scanner;


public class HumanPlayer extends Player {

    public HumanPlayer(String name, int color) {
        super(name, color);
    }


    public int determineMove(Board board) {
        String prompt = "> " + getName() + ", what is your choice? ";
        int choice = readInt(prompt);
        return choice;
    }

    private int readInt(String prompt) {
        int value = 0;
        boolean intRead = false;
        @SuppressWarnings("resource")
        Scanner line = new Scanner(System.in);
        do {
            System.out.print(prompt);
            try (Scanner scannerLine = new Scanner(line.nextLine());) {
                if (scannerLine.hasNextInt()) {
                    intRead = true;
                    value = scannerLine.nextInt();
                }
            }
        } while (!intRead);
        return value;
    }

}
