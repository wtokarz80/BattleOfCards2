import com.github.tomaslanger.chalk.Chalk;
import com.jakewharton.fliptables.FlipTable;

import java.util.*;

public class Table {
    Ui ui = new Ui();
    Display display;
    Scanner scan;
    private List<Card> tableCards;
    XMLParser xmlParser;
    Deck newDeck;
    Player player1;
    Player player2;

    public Table(boolean isHuman1, boolean isHuman2) {
        display = new Display();
        scan = new Scanner(System.in);
        player1 = createPlayer(isHuman1);
        player2 = createPlayer(isHuman2);
        xmlParser = new XMLParser();
        newDeck = xmlParser.getDeck();
        tableCards = new ArrayList<>();                  //krupier wyciąga karty na stół

        newDeck.shuffle();
        display.clearScreen();

        setPlayersHands(player1, player2, 3);


        initializeGame();
    }
    public Player createPlayer(boolean isHuman) {

        Hand hand = new Hand();
        if (isHuman) {
            System.out.println(Chalk.on("Enter Your name, please").magenta());
            String playerName = ui.getStringInput();

            return new HumanPlayer(playerName, hand);
        } else {
            return new ComputerPlayer("computer", hand);
        }
    }
    public void initializeGame() {
        boolean ifCanPlay = true;
        boolean switchPlayer = false;
        Player currentPlayer = player1;
        Player opponentPlayer = player2;
        display.displayStartScreen(currentPlayer, opponentPlayer);
        System.out.println("Press enter to continue...");
        ui.getStringInput();

        while (ifCanPlay) {
            display.clearScreen();
            if (!switchPlayer) {
                switchPlayer = true;
                currentPlayer = player1;
                opponentPlayer = player2;
            } else {
                switchPlayer = false;
                currentPlayer = player2;
                opponentPlayer = player1;
            }
            startGame(currentPlayer, opponentPlayer);
            ifCanPlay = canPlayersPlayer(currentPlayer, opponentPlayer);
        }
        endGameScreen(currentPlayer, opponentPlayer);
    }

    private void endGameScreen(Player currentPlayer, Player opponentPlayer) {
        String winner;
        String loser;
        if (currentPlayer.getHand().getHandList().size() != 0) {
            winner = currentPlayer.getPlayerName();
            loser = opponentPlayer.getPlayerName();
        } else {
            winner = opponentPlayer.getPlayerName();
            loser = currentPlayer.getPlayerName();
        }
        System.out.println(Chalk.on("The Winner is: " + winner).green());
        System.out.println(Chalk.on("The Looser is: " + loser).red());
    }

    private boolean canPlayersPlayer(Player currentPlayer, Player opponentPlayer) {
        int player1Cards = player1.getHand().getHandList().size();
        int player2Cards = player2.getHand().getHandList().size();
        return (player1Cards > 0 && player2Cards > 0);
    }

    private void startGame(Player currentPlayer, Player opponentPlayer) {

        Card currentPlayerCard = currentPlayer.showCurrentCard();
        Card opponentPlayerCard = opponentPlayer.showCurrentCard();
        System.out.println(currentPlayer.getPlayerName() + "'s Card: \n" + currentPlayerCard.toString());
        String chosenStatistic = currentPlayer.chooseStatistic();

        playerPutCard(currentPlayer, opponentPlayer, currentPlayerCard, opponentPlayerCard, chosenStatistic);
        printTable(currentPlayer, opponentPlayer, currentPlayerCard, opponentPlayerCard, chosenStatistic);

        switch (chosenStatistic) {
            case "strength":
                checkStrength(currentPlayer, opponentPlayer, currentPlayerCard, opponentPlayerCard);
                break;
            case "intelligence":
                checkIntelligence(currentPlayer, opponentPlayer, currentPlayerCard, opponentPlayerCard);
                break;
            case "agility":
                checkAgility(currentPlayer, opponentPlayer, currentPlayerCard, opponentPlayerCard);
                break;
            case "charisma":
                checkCharisma(currentPlayer, opponentPlayer, currentPlayerCard, opponentPlayerCard);
                break;
        }
        Common.delay(3000);
        printTable(currentPlayer, opponentPlayer, currentPlayerCard, opponentPlayerCard, "waits for enter");
        System.out.println("Press enter to continue...");
        scan.nextLine();
    }

    private void checkCharisma(Player currentPlayer, Player opponentPlayer, Card currentPlayerCard, Card opponentPlayerCard) {
        if (currentPlayerCard.getCharisma() > opponentPlayerCard.getCharisma()) {
            for (Card card : tableCards) {
                currentPlayer.getHand().getHandList().add(card);
            }
            tableCards.clear();
            System.out.println(currentPlayer.getPlayerName() + " wins round, and takes both cards");
        } else if (currentPlayerCard.getCharisma() < opponentPlayerCard.getCharisma()) {
            for (Card card : tableCards) {
                opponentPlayer.getHand().getHandList().add(card);
            }
            System.out.println(opponentPlayer.getPlayerName() + " wins round, and takes both cards");
            tableCards.clear();
        }
    }

    private void checkAgility(Player currentPlayer, Player opponentPlayer, Card currentPlayerCard, Card opponentPlayerCard) {
        if (currentPlayerCard.getAgility() > opponentPlayerCard.getAgility()) {
            for (Card card : tableCards) {
                currentPlayer.getHand().getHandList().add(card);
            }
            tableCards.clear();
            System.out.println(currentPlayer.getPlayerName() + " wins, and takes both cards");
        } else {
            for (Card card : tableCards) {
                opponentPlayer.getHand().getHandList().add(card);
            }
            tableCards.clear();
            System.out.println(opponentPlayer.getPlayerName() + " wins, and takes both cards");
        }
    }

    private void checkIntelligence(Player currentPlayer, Player opponentPlayer, Card currentPlayerCard, Card opponentPlayerCard) {
        if (currentPlayerCard.getIntelligence() > opponentPlayerCard.getIntelligence()) {
            for (Card card : tableCards) {
                currentPlayer.getHand().getHandList().add(card);
            }
            tableCards.clear();
            System.out.println(currentPlayer.getPlayerName() + " wins, and takes both cards");
        } else {
            for (Card card : tableCards) {
                opponentPlayer.getHand().getHandList().add(card);
            }
            tableCards.clear();
            System.out.println(opponentPlayer.getPlayerName() + " wins, and takes both cards");
        }
    }

    private void checkStrength(Player currentPlayer, Player opponentPlayer, Card currentPlayerCard, Card opponentPlayerCard) {
        if (currentPlayerCard.getStrength() > opponentPlayerCard.getStrength()) {
            for (Card card : tableCards) {
                currentPlayer.getHand().getHandList().add(card);
            }
            tableCards.clear();
            System.out.println(currentPlayer.getPlayerName() + " wins, and takes both cards");
        } else {
            for (Card card : tableCards) {
                opponentPlayer.getHand().getHandList().add(card);
            }
            tableCards.clear();
            System.out.println(opponentPlayer.getPlayerName() + " wins, and takes both cards");
        }
    }

    private void playerPutCard(Player currentPlayer, Player opponentPlayer, Card currentPlayerCard, Card opponentPlayerCard, String chosenStatistic) {

        tableCards.add(currentPlayerCard);
        currentPlayer.removeCard(currentPlayerCard);

        tableCards.add(opponentPlayerCard);
        opponentPlayer.removeCard(opponentPlayerCard);


    }

    private void printTable(Player currentPlayer, Player opponentPlayer, Card currentPlayerCard, Card opponentPlayerCard, String chosenStatistic) {
        System.out.print("\033[H\033[2J");
        String[] innerHeaders = {"Battle card: "};
        String[][] innerData = {{String.valueOf(currentPlayerCard)}};
        String inner = FlipTable.of(innerHeaders, innerData);
        String[] innerHeaders2 = {"Battle card: "};
        String[][] innerData2 = {{String.valueOf(opponentPlayerCard)}};
        String inner2 = FlipTable.of(innerHeaders2, innerData2);
        String[] headers = {" ", "BATTLE OF CARDS", " "};
        String[][] data = {
                {"Current player: " + currentPlayer.getPlayerName(), " ", "Opponent player: " + opponentPlayer.getPlayerName()},
                {"Cards in hand: " + String.valueOf(currentPlayer.getHand().getHandList().size()), "Cards on table: " + tableCards.size(), "Cards in hand: " + String.valueOf(opponentPlayer.getHand().getHandList().size())},
                {inner, "Comparing by: \n" + chosenStatistic, inner2}
        };
        System.out.println(Chalk.on(FlipTable.of(headers, data)).cyan());
    }


    public void setPlayersHands(Player player1, Player player2, int numberOfCards) {
        if (numberOfCards > newDeck.getCardList().size() || numberOfCards <= 0) {
            numberOfCards = newDeck.getCardList().size();
        }
        Hand player1Hand = new Hand();                                              //gracze wyciagają ręce
        Hand player2Hand = new Hand();

        for (int s = 0; s < numberOfCards; s++) {                                    //krupier rozdaje karty raz jednemu raz drugiemu
            dealOneCard(newDeck, player1Hand);
            dealOneCard(newDeck, player2Hand);
        }
        player1.setHand(player1Hand);
        player2.setHand(player2Hand);
    }

    public Hand dealOneCard(Deck deck, Hand hand) {
        hand.addCard(deck.getCardByIndex(0));
        deck.removeCard(0);
        return hand;
    }

}