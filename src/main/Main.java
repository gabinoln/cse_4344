package main;

/**
 * Gabino Luna
 * CSE-4344
 * Lab 1
 */
public class Main {

    static ConnectionThread connectionThread;
    static Thread listenerThread;

    public static void main(String [] args) {
        listenerThread = new Thread(new PortListener());
//        connectionThread = new ConnectionThread();
//        connectionThread.run();
        listenerThread.start();
    }


}
