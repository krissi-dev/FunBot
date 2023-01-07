package de.krissi;

//Importieren der libraries
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main extends ListenerAdapter {
    public static void main(String[] args) throws Exception {
        String token = ""; //Token aus Sicherheitsgründen entfernt
        JDABuilder jda = JDABuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT); //Konfiguration der JDA Instanz
        jda.addEventListeners(new Main()); //Listener erstellen
        jda.build(); //Erstellung der JDA Instanz und start des Login-Prozesses
        System.out.println("Event Listener erstellt."); //Dokumentation der Aktion in der Konsole
        System.out.println("Erfolgreich eingeloggt."); //Dokumentation der Aktion in der Konsole
    }

    //Funktion zum Ermitteln des Schere, Stein, Papier Gewinners (siehe rps, weiter unten)
    private static String determineWinner(String playersChoice, String botsChoice) {
        if (playersChoice.equals(botsChoice)) { //Überprüft, ob playersChoice und botsChoice identisch sind
            return "Unentschieden!"; //Der Bot schreibt "Unentschieden" zurück
        } else if (playersChoice.equals("Schere") && botsChoice.equals("Papier") ||
                playersChoice.equals("Stein") && botsChoice.equals("Schere") ||
                playersChoice.equals("Papier") && botsChoice.equals("Stein")) { //Überprüft, ob ein Fall vorliegt, in dem der Spieler gewonnen hat
            return "Du hast gewonnen!"; //Der Bot schreibt "DU hast gewonnen!" zurück
        } else { //Wird ausgeführt, wenn es weder unentschieden steht, noch der Spieler gewinnt
            return "Ich habe gewonnen!"; //Der Bot schreibt "Ich habe gewonnen!" zurück
        }
    }

    //Wird abgerufen, sobald eine Nachricht im Chat gesendet wird
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        System.out.println("Nachricht erkannt."); //Dokumentiert in der Konsole, dass eine Nachricht im Chat erkannt wurde
        String message = event.getMessage().getContentRaw(); //Speichert den Inhalt der erkannten Nachricht in die Variable message
        if (message.startsWith("!")) { //Überprüft, ob die Nachricht mit einem ! beginnt = die Nachricht ein Bot Command ist
            System.out.println("Command erkannt."); //Dokumentiert in der Konsole, dass die erkannte Nachricht sich an den Bot richtet
            List<String> args = Arrays.asList(message.substring(1).split("\\s+")); //Teilt den Inhalt des Commands bei jedem Leerzeichen und speichert sie dann in eine Liste, ohne das ! am Anfang
            String command = args.get(0).toLowerCase(); //Speichert den Command in eine Variable und macht ihn case insensitive, indem er alle Buchstaben in Kleinbuchstaben umwandelt
            System.out.println("Command " + command + " und " + (args.size()-1) + " Argument(e) erkannt.");  //Dokumentation der Aktion in der Konsole (Command und Anzahl der Argumente)
            switch (command) {
                case "ping": //Überprüft die Erreichbarkeit des Bots
                    event.getChannel().sendMessage("Pong!").queue(); //sendet die Antwort in den Chat
                    System.out.println("Antwort auf !ping Command gesendet."); //Dokumentiert die bereits ausgeführten Aktionen in der Konsole
                    break; //Verhindert, dass der Code weiterläuft
                case "repeat": //Wiederholt den eingegebenen Text
                    event.getChannel().sendMessage(message.substring(8)).queue(); //sendet die eingegebene Nachricht in den Chat abzüglich "!repeat"
                    System.out.println("Antwort auf !repeat Command gesendet."); //Dokumentiert die bereits ausgeführten Aktionen in der Konsole
                    break; //Verhindert, dass der Code weiterläuft
                case "hello": //Sagt Hallo :)
                    event.getChannel().sendMessage("Moin!").queue(); //sendet "Moin!" in den Chat
                    System.out.println("Antwort auf !hello Command gesendet."); //Dokumentiert die bereits ausgeführten Aktionen in der Konsole
                    break; //Verhindert, dass der Code weiterläuft
                case "choose": //Wählt zufällig eines der eingegebenen Parameter aus
                    if (args.size() > 2) { //Überprüft, ob mindestens 2 Parameter zur Auswahl stehen
                        Random random = new Random();
                        int pick = random.nextInt(args.size()-1); //Wählt zufällig den Index von einem der Parameter. -1, da "choose" nicht mitgezählt werden soll und speichert den Index dann in die Variable pick
                        event.getChannel().sendMessage(args.get(pick+1)).queue(); //sendet den ausgewählten Parameter in den Chat
                        System.out.println("Antwort auf !choose Command gesendet."); //Dokumentiert die bereits ausgeführten Aktionen in der Konsole
                    }else { //Wird ausgeführt, falls abgesehen vom Command weniger als 2 Parameter angegeben wurden
                        event.getChannel().sendMessage("Gebe mindestens 2 Auswahlmöglichkeiten an.").queue(); //Sendet eine Aufforderung zur Eingabe von mehr Parametern, falls weniger als 2 eingegeben wurden, in den Chat
                        System.out.println("Antwort auf !choose Command gesendet. Zu wenig Parameter."); //Dokumentiert die bereits ausgeführten Aktionen in der Konsole
                    }
                    break; //Verhindert, dass der Code weiterläuft
                case "coinflip": //Münze werfen
                    if (Math.random() < 0.5){ //Wird ausgeführt, wenn eine zufällige Zahl kleiner als 0,5 ist
                        event.getChannel().sendMessage("Kopf").queue(); //sendet das Ergebnis "Kopf" als Nachricht in den Chat
                    }else { //wird ausgeführt, wenn die zufällige Zahl aus der if-Abfrage >= 0,5 ist
                        event.getChannel().sendMessage("Zahl").queue(); //sendet das Ergebnis "Zahl" als Nachricht in den Chat
                    }
                    System.out.println("Antwort auf !coinflip Command gesendet."); //Dokumentiert die bereits ausgeführten Aktionen in der Konsole
                    break; //Verhindert, dass der Code weiterläuft
                case "rps": //Rock, Paper, Scissors Mini game
                    if (args.size() == 2) { //Überprüft, ob genau ein Parameter abgesehen vom Command eingegeben wurde
                        if (args.get(1).equals("Schere") || args.get(1).equals("Stein") || args.get(1).equals("Papier")) { //Überprüft, ob der eingegebene Parameter eine gültige rps Geste ist
                            String playersChoice = args.get(1); //Speichert die vom Spieler gespielte Geste in die Variable playersChoice
                            List<String> rps = Arrays.asList("Schere", "Stein", "Papier"); //Erstellt eine Liste mit den möglichen rps Gesten
                            Random random = new Random();
                            String botsChoice = rps.get(random.nextInt(3)); //Wählt aus der Liste der möglichen rps Gesten zufällig eine aus und speichert sie in die Variable botsChoice
                            event.getChannel().sendMessage("Du hast " + playersChoice + " gespielt und ich " + botsChoice + ".\n" + determineWinner(playersChoice, botsChoice)).queue(); //Sendet die gespielten Gesten und das Ergebnis in den Chat, indem die Funktion determineWinner aufgerufen wird und playersChoice und botsChoice übergeben werden
                            System.out.println("Antwort auf !rps Command gesendet."); //Dokumentiert die bereits ausgeführten Aktionen in der Konsole
                        }else { //Wird ausgeführt, wenn das zum Command zusätzlich eingegebene Parameter keine gültige rps Geste ist
                            event.getChannel().sendMessage(args.get(1) + " ist keine gültige Geste. Zur Auswahl stehen: \"Schere\", \"Stein\" oder \"Papier\".").queue(); //Sendet eine Aufforderung zur Eingabe einer gültigen rps Geste in den Chat
                            System.out.println("Antwort auf !rps Command gesendet. Ungültige Parameter."); //Dokumentiert die bereits ausgeführten Aktionen in der Konsole
                        }
                    }else { //Wird ausgeführt, falls außer dem Command nicht genau ein weiterer Parameter eingegeben wurde
                        event.getChannel().sendMessage("Gebe genau eine Geste an, die du spielen möchtest.").queue(); //Sendet eine Aufforderung zur Eingabe von genau einem Parameter zusätzlich zum Command in den Chat
                        System.out.println("Antwort auf !rps Command gesendet. Zu wenig Parameter.");  //Dokumentiert die bereits ausgeführten Aktionen in der Konsole
                    }
                    break; //Verhindert, dass der Code weiterläuft
                default: //Wird ausgeführt, wenn keiner der anderen Fälle eintritt (keiner der vorher aufgeführten Commands eingegeben wurde)
                    event.getChannel().sendMessage("Unbekannter Befehl.").queue(); //Sendet die Information, dass der eingegebene Command nicht bekannt ist, in den Chat
                    System.out.println("Antwort auf !" + command + " Command gesendet. Ungültiger Command.");  //Dokumentiert, dass ein unbekannter Befehl eingegeben wurde, in der Konsole
                    break; //Verhindert, dass der Code weiterläuft
            }
        }
    }
}