package telran.net;

import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
import telran.view.StandardInputOutput;

public class Main {
    static EchoClient echoClient;

    public static void main(String[] args) {
        Item[] items = {
                Item.of("start session", Main::startSession),
                Item.of("exit", Main::Exit, true)
        };
        Menu menu = new Menu("Reverse-Length Client Application", items);
        menu.perform(new StandardInputOutput());
    }

    static void startSession(InputOutput io) {
        String host = io.readString("Enter hostname");
        int port = io.readNumberRange("Enter port", "Wrong port", 3000, 50000).intValue();
        if (echoClient != null) {
            echoClient.close();
        }
        echoClient = new EchoClient(host, port);
        Menu menu = new Menu("Run Session",
                Item.of("Send request", IO -> {
                    Menu subMenu = new Menu("Enter request type",
                            Item.of("Normal", iO -> stringProcessing(iO, "normal")),
                            Item.of("Reverse", iO -> stringProcessing(iO, "reverse")),
                            Item.of("Length", iO -> stringProcessing(iO, "length")),
                            Item.ofExit());
                    subMenu.perform(io);
                }),
                Item.ofExit());
        menu.perform(io);
    }

    static void Exit(InputOutput io) {
        if (echoClient != null) {
            echoClient.close();
        }
    }

    static void stringProcessing(InputOutput io, String type) {
        String input = io.readString("Enter string for processing");
        String request = type + ":" + input;
        String response = echoClient.sendAndReceive(request);
        io.writeLine("Response: " + response);
    }
}