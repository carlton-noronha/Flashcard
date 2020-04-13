package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class FlashcardsApplication {

    private static final LinkedHashMap<String, LinkedHashMap<String, Integer>> flashcards = new LinkedHashMap<>();
    private static final LinkedHashMap<String, Integer> flashcardsValues = new LinkedHashMap<>();
    private static final List<String> logData = new ArrayList<>();
    private static final Date date = new Date();


    public static void main(String[] args) {

        if ((args.length == 2) && (args[0].equals("-import"))) {
            importFile(args[1]);
        } else if ((args.length == 4) && (args[0].equals("-import") || args[2].equals("-import"))) {
            if (args[0].equals("-import")) {
                importFile(args[1]);
            } else {
                importFile(args[3]);
            }
        }

        String choice;
        final Scanner scanner = new Scanner(System.in);

        // Menu for the user

        while (true) {
            System.out.println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            choice = scanner.nextLine();

            //logging the choice made by the user
            logData.add(date.getTime() + " " + choice);

            switch (choice) {
                case "add":

                    String card;
                    String cardDescription;

                    System.out.println("The card:");
                    card = scanner.nextLine();

                    if (!flashcards.containsKey(card)) {
                        System.out.println("The definition of the card:");
                        cardDescription = scanner.nextLine();
                        addACard(card, cardDescription);
                    } else {
                        System.out.println("The card \"" + card + "\" already exists.");
                        logData.add(date.getTime() + " The card \"" + card + "\" already exists.");
                    }
                    break;

                case "remove":
                    removeACard();
                    break;

                case "import":
                    importFile();
                    break;

                case "export":
                    saveFile();
                    break;

                case "ask":
                    askQuestions();
                    break;

                case "exit":
                    System.out.println("Bye bye!");
                    if ((args.length == 2) && (args[0].equals("-export"))) {
                        saveFile(args[1]);
                    } else if ((args.length == 4) && (args[0].equals("-export") || args[2].equals("-export"))) {
                        if (args[0].equals("-export")) {
                            saveFile(args[1]);
                        } else {
                            saveFile(args[3]);
                        }
                    }
                    System.exit(0);

                case "log":
                    log();
                    break;

                case "hardest card":
                    hardestCard();
                    break;

                case "reset stats":
                    resetStats();
                    break;

                default:
                    System.out.println("Invalid choice!!! Try again");

            }
        }
    }

    // Add flashcards to the map

    private static void addACard(String card, String cardDescription) {

        if (!flashcardsValues.containsKey(cardDescription)) {
            flashcardsValues.put(cardDescription, 0);
            LinkedHashMap<String, Integer> cardValue = new LinkedHashMap<>();
            cardValue.put(cardDescription, 0);
            flashcards.put(card, cardValue);
            System.out.println("The pair (\"" + card + "\":\"" + cardDescription +
                    "\") has been added.");
            logData.add(date.getTime() + " The pair (\"" + card + "\":\"" + cardDescription +
                    "\") has been added.");
        } else {
            System.out.println("The definition \"" + cardDescription + "\" already exists.");
            logData.add(date.getTime() + " The definition \"" + cardDescription + "\" already exists.");
        }
    }

    private static void removeACard() {

        String card;

        final Scanner scanner = new Scanner(System.in);

        System.out.println("The card:");
        card = scanner.nextLine();

        if (flashcards.containsKey(card)) {

            flashcards.remove(card);
            System.out.println("The card has been removed.");
            logData.add(date.getTime() + " The card has been removed.");

        } else {

            System.out.println("Can't remove \"" + card + "\": there is no such card.");
            logData.add(date.getTime() + " Can't remove \"" + card + "\": there is no such card.");
        }
    }

    private static void importFile() {

        final Scanner fileNameInput = new Scanner(System.in);

        System.out.println("File name:");
        String fileName = fileNameInput.nextLine();

        logData.add(date.getTime() + " " + fileName);

        File flashcardFile = new File("./" + fileName);

        try (final Scanner scanner = new Scanner(flashcardFile)) {

            String card;
            String cardDescription;
            String hardestCard;

            int noOfLoadedCards = 0;


            while (scanner.hasNext()) {
                scanner.useDelimiter("-");
                card = scanner.next().trim();
                scanner.reset();
                scanner.next();
                scanner.useDelimiter("=");
                cardDescription = scanner.next().replace("{", "").trim();
                scanner.useDelimiter("}");
                hardestCard = scanner.next().replace("=", "").trim();
                scanner.nextLine();
                LinkedHashMap<String, Integer> cardValue = new LinkedHashMap<>();
                cardValue.put(cardDescription, Integer.parseInt(hardestCard));
                flashcards.put(card, cardValue);
                noOfLoadedCards += 1;
            }

            System.out.println(noOfLoadedCards + " cards have been loaded.");
            logData.add(date.getTime() + " " + noOfLoadedCards + " cards have been loaded.");

        } catch (FileNotFoundException FNF) {
            System.out.println("File not found.");
            logData.add(date.getTime() + " File not found.");
        }

    }

    private static void importFile(String fileName) {

        logData.add(date.getTime() + " " + fileName);

        File flashcardFile = new File("./" + fileName);

        try (final Scanner scanner = new Scanner(flashcardFile)) {

            String card;
            String cardDescription;
            String hardestCard;

            int noOfLoadedCards = 0;


            while (scanner.hasNext()) {
                scanner.useDelimiter("-");
                card = scanner.next().trim();
                scanner.reset();
                scanner.next();
                scanner.useDelimiter("=");
                cardDescription = scanner.next().replace("{", "").trim();
                scanner.useDelimiter("}");
                hardestCard = scanner.next().replace("=", "").trim();
                scanner.nextLine();
                LinkedHashMap<String, Integer> cardValue = new LinkedHashMap<>();
                cardValue.put(cardDescription, Integer.parseInt(hardestCard));
                flashcards.put(card, cardValue);
                noOfLoadedCards += 1;
            }

            System.out.println(noOfLoadedCards + " cards have been loaded.");
            logData.add(date.getTime() + " " + noOfLoadedCards + " cards have been loaded.");

        } catch (FileNotFoundException FNF) {
            System.out.println("File not found.");
            logData.add(date.getTime() + " File not found.");
        }

    }

    private static void saveFile() {

        final Scanner fileNameInput = new Scanner(System.in);

        System.out.println("File name:");
        String fileName = fileNameInput.nextLine();

        logData.add(date.getTime() + " " + fileName);

        File flashcardFile = new File("./" + fileName);

        try (PrintWriter fileWriter = new PrintWriter(flashcardFile)) {
            for (var entry : flashcards.entrySet()) {
                fileWriter.println(entry.getKey() + " - " + entry.getValue());
            }
            System.out.println(flashcards.size() + " cards have been saved.");
            logData.add(date.getTime() + " " + flashcards.size() + " cards have been saved.");
        } catch (IOException IOE) {
            System.out.println("IOException: " + IOE.getMessage());
            logData.add(date.getTime() + " IOException: " + IOE.getMessage());
        }
    }

    private static void saveFile(String fileName) {


        logData.add(date.getTime() + " " + fileName);

        File flashcardFile = new File("./" + fileName);

        try (PrintWriter fileWriter = new PrintWriter(flashcardFile)) {
            for (var entry : flashcards.entrySet()) {
                fileWriter.println(entry.getKey() + " - " + entry.getValue());
            }
            System.out.println(flashcards.size() + " cards have been saved.");
            logData.add(date.getTime() + " " + flashcards.size() + " cards have been saved.");
        } catch (IOException IOE) {
            System.out.println("IOException: " + IOE.getMessage());
            logData.add(date.getTime() + " IOException: " + IOE.getMessage());
        }
    }

    private static void askQuestions() {

        String cardDescriptionAnswer;
        int noOfTimes = 0;

        final Scanner scanner = new Scanner(System.in);
        System.out.println("How many times to ask?");
        int noOfCards = scanner.nextInt();
        scanner.nextLine();

        logData.add(date.getTime() + " " + noOfCards);

        while (noOfTimes != noOfCards) {
            for (var entry : flashcards.entrySet()) {
                System.out.println("Print the definition of \"" + entry.getKey() + "\":");
                logData.add(date.getTime() + " Print the definition of \"" + entry.getKey() + "\":");

                cardDescriptionAnswer = scanner.nextLine();
                if (flashcards.get(entry.getKey()).containsKey(cardDescriptionAnswer)) {
                    System.out.println("Correct answer.");
                    logData.add(date.getTime() + " Correct answer.");
                } else if (cardDescriptionAnswer.equals("??")) {
                    for (var currentElement : flashcards.get(entry.getKey()).entrySet()) {
                        System.out.println("Wrong answer. The correct one is \"" + currentElement.getKey() + "\".");
                        flashcards.get(entry.getKey()).put(currentElement.getKey(), currentElement.getValue() + 1);
                        logData.add(date.getTime() + " Wrong answer. The correct one is \"" + currentElement.getKey() + "\".");
                    }
                } else {
                    boolean answerDetermined = false;
                    for (var currentElement : flashcards.entrySet()) {
                        for (var currentElementValue : flashcards.get(currentElement.getKey()).entrySet()) {
                            if (currentElementValue.getKey().equals(cardDescriptionAnswer)) {
                                for (var currentElementValueSet : flashcards.get(entry.getKey()).entrySet()) {
                                    System.out.println("Wrong answer. (The correct one is \"" + currentElementValueSet.getKey() +
                                            "\", you've just written the definition of \"" + currentElement.getKey() + "\".)");
                                    logData.add(date.getTime() + "Wrong answer. (The correct one is \"" + currentElementValueSet.getKey() +
                                            "\", you've just written the definition of \"" + currentElement.getKey() + "\".)");
                                    flashcards.get(entry.getKey()).put(currentElementValueSet.getKey(), currentElementValueSet.getValue() + 1);
                                }
                                answerDetermined = true;
                            }
                        }
                        if (answerDetermined)
                            break;
                    }
                    if (!answerDetermined) {
                        for (var currentElement : flashcards.get(entry.getKey()).entrySet()) {
                            System.out.println("Wrong answer. The correct one is \"" + currentElement.getKey() + "\".");
                            flashcards.get(entry.getKey()).put(currentElement.getKey(), currentElement.getValue() + 1);
                            logData.add(date.getTime() + " Wrong answer. The correct one is \"" + currentElement.getKey() + "\".");
                        }
                    }
                }
                noOfTimes += 1;

                if (noOfCards == noOfTimes)
                    break;
            }
        }
    }

    private static void log() {
        final Scanner fileNameInput = new Scanner(System.in);

        System.out.println("File name:");
        String fileName = fileNameInput.nextLine();
        logData.add(date.getTime() + " " + fileName);

        File logFile = new File("./" + fileName);

        try (PrintWriter printWriter = new PrintWriter(logFile)) {
            for (String currentLogMessage : logData)
                printWriter.println(currentLogMessage);

            System.out.println("The log has been saved.");
            printWriter.println(date.getTime() + " The log has been saved.");
        } catch (IOException IOE) {
            System.out.println("IOException: " + IOE.getMessage());
        }
    }

    private static void hardestCard() {
        List<String> hardestCards = new ArrayList<>();
        int highestDifficultyLevel = 0;
        for (var currentElement : flashcards.entrySet()) {
            for (var currentElementValue : flashcards.get(currentElement.getKey()).entrySet()) {
                if (currentElementValue.getValue() > highestDifficultyLevel) {
                    hardestCards.clear();
                    highestDifficultyLevel = currentElementValue.getValue();
                    hardestCards.add(currentElement.getKey());
                } else if ((currentElementValue.getValue() > 0) && (currentElementValue.getValue() == highestDifficultyLevel)) {
                    hardestCards.add(currentElement.getKey());
                }
            }
        }
        if (hardestCards.isEmpty()) {
            System.out.println("There are no cards with errors.");
        } else {
            if (hardestCards.size() == 1)
                System.out.print("The hardest card is ");
            else
                System.out.print("The hardest cards are ");
            for (int i = 0; i < hardestCards.size(); ++i) {
                if (i == hardestCards.size() - 1)
                    System.out.print("\"" + hardestCards.get(i) + "\"");
                else
                    System.out.print("\"" + hardestCards.get(i) + "\", ");
            }
            System.out.println(". You have " + highestDifficultyLevel + " errors answering them.");
        }
    }

    private static void resetStats() {

        for (var currentElement : flashcards.entrySet()) {
            for (var currentElementValue : flashcards.get(currentElement.getKey()).entrySet()) {
                flashcards.get(currentElement.getKey()).put(currentElementValue.getKey(), 0);
            }
        }
        System.out.println("Card statistics has been reset.");
    }

}

