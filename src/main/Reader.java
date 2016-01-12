package main;

/**
 * Created by Andreas Appelqvist on 2016-01-11.
 * Reader
 */
public class Reader extends Thread {

    private Buffer buffer;
    private boolean running = false;

    /**
     * Konstruktor
     * @param buffer
     */
    public Reader(Buffer buffer) {
        this.buffer = buffer;
    }

    /**
     * Startar tråden
     */
    public void startThread() {
        running = true;
        this.start();
    }

    /**
     * Stannar tråden
     */
    public void stopThread() {
        running = false;
        this.interrupt();
    }


    /**
     * Run-loop
     */
    @Override
    public void run() {
        super.run();
        while (running) {
            buffer.read();
        }
    }
}

