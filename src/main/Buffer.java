package main;

import sun.awt.Mutex;

import javax.swing.*;

/**
 * Created by Andreas Appelqvist on 2016-01-08.
 *   Buffer
 */
public class Buffer {

    private int max;
    private int writePos = 0;
    private int readPos = 0;
    private int findPos = 0;

    private int nbrOfReplace = 0;

    private String findString = "";
    private String replaceString = "";

    private Word[] buffer;

    private Controller controller;

    private boolean notify;

    /**
     * Konstruktor
     */
    public Buffer(Controller controller) {
        this.controller = controller;
        this.max = 10;
        buffer = new Word[max];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = new Word();
        }
    }

    /**
     * Säger åt bufferten vilka strängar som de ska leta efter och
     * vad som ska byta ut.
     * @param find
     * @param replace
     */
    public void setFindAndReplace(String find, String replace){
        this.findString = find;
        this.replaceString = replace;
    }

    /**
     * Sätter om det ska vara notification på byte
     * @param value
     */
    public void setNotify(boolean value) {
        notify = value;
    }


    /**
     * Skriver en sträng till bufferten
     * @param str
     */
    public synchronized void write(String str) {
        //System.out.println("början på write");
        //showBuffer();
        while (buffer[writePos].getStatus() != 0) {
            try {
                notifyAll();
                Thread.sleep(0);
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        buffer[writePos].setWord(str);
        buffer[writePos].setStatus(1);


        writePos = (writePos + 1) % max;
    }

    /**
     * Kollar om ett ord ska bytas ut
     */
    public synchronized void checking() {
        while (buffer[findPos].getStatus() != 1) {
            try {
                notify();
                Thread.sleep(0);
                wait();
            } catch (InterruptedException e) {
            }
        }

        String str = "";
        if (!findString.equals("")) {
            str = buffer[findPos].getWord();
            if (!replaceString.equals("")) {
                if (str.equals(findString)) {
                    if (!notify) {
                        buffer[findPos].setWord(replaceString);
                        nbrOfReplace++;
                    } else {
                        int nbr = JOptionPane.showConfirmDialog(null,"Vill du byta *"+buffer[findPos].getWord()+"* mot *"+replaceString+"* ??");
                        switch(nbr) {
                            case 0:
                                buffer[findPos].setWord(replaceString);
                                nbrOfReplace++;
                                break;
                            case 1:
                                System.out.println("NO REPLACE");
                                break;
                            case 2:
                                System.out.println("NO REPLACE");
                                break;
                        }
                    }
                }
            }
        }
        buffer[findPos].setStatus(2);
        controller.updateGUInbrReplace(nbrOfReplace);
        findPos = (findPos + 1) % max;
    }

    /**
     * Läser ord från bufferten, och skriver ut det i gui
     */
    public synchronized void read() {
        while (buffer[readPos].getStatus() != 2) {
            try {
                notifyAll();
                Thread.sleep(0);
                wait();
            } catch (InterruptedException e) {
            }
        }
        String str = "";
        str = buffer[readPos].getWord();
        buffer[readPos].setStatus(0);
        readPos = (readPos + 1) % max;
        controller.writeToGUIDest(str);

        //showBuffer();
    }


    /**
     * Återställer antal byten
     */
    public void resetCount(){
        nbrOfReplace = 0;
    }

}
