import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    private static final String FILE_NAME = "document.txt";

    static LinkedList<String> document = new LinkedList<>();
    static Stack<EditAction> undoStack = new Stack<>();
    static Stack<EditAction> redoStack = new Stack<>();

    static class EditAction {
        String type;
        String line;

        EditAction(String type, String line) {
            this.type = type;
            this.line = line;
        }
    }

    static void addLine(String line) {
        document.addLast(line);
        undoStack.push(new EditAction("ADD", line));
        redoStack.clear();
        System.out.println("Line added.");
    }

    static void deleteLastLine() {
        if (document.isEmpty()) {
            System.out.println("Document is empty.");
            return;
        }

        String line = document.removeLast();
        undoStack.push(new EditAction("DELETE", line));
        redoStack.clear();
        System.out.println("Line deleted.");
    }

    static void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }

        EditAction action = undoStack.pop();

        if (action.type.equals("ADD")) {
            if (!document.isEmpty()) {
                document.removeLast();
            }
        } else if (action.type.equals("DELETE")) {
            document.addLast(action.line);
        }

        redoStack.push(action);
        System.out.println("Undo done.");
    }

    static void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("Nothing to redo.");
            return;
        }

        EditAction action = redoStack.pop();

        if (action.type.equals("ADD")) {
            document.addLast(action.line);
        } else if (action.type.equals("DELETE")) {
            if (!document.isEmpty()) {
                document.removeLast();
            }
        }

        undoStack.push(action);
        System.out.println("Redo done.");
    }

    static void displayDocument() {
        if (document.isEmpty()) {
            System.out.println("Document is empty.");
            return;
        }

        System.out.println("Document:");
        for (int i = 0; i < document.size(); i++) {
            System.out.println((i + 1) + ". " + document.get(i));
        }
    }

    static void loadDocument() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                document.addLast(line);
            }
            System.out.println("Document loaded.");
        } catch (IOException e) {
            System.out.println("Could not load the document.");
        }
    }

    static void saveDocument() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String line : document) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Document saved.");
        } catch (IOException e) {
            System.out.println("Could not save the document.");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        loadDocument();

        while (true) {
            System.out.println("\n===== UNDO/REDO TEXT EDITOR =====");
            System.out.println("1. Add line");
            System.out.println("2. Delete last line");
            System.out.println("3. Undo");
            System.out.println("4. Redo");
            System.out.println("5. Display");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter text: ");
                    String line = sc.nextLine();
                    addLine(line);
                    break;

                case "2":
                    deleteLastLine();
                    break;

                case "3":
                    undo();
                    break;

                case "4":
                    redo();
                    break;

                case "5":
                    displayDocument();
                    break;

                case "6":
                    saveDocument();
                    System.out.println("Exiting editor.");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid option. Choose 1 to 6.");
            }
        }
    }
}
