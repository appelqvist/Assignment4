package main;

import sun.awt.Mutex;

import javax.swing.*;

/**
 * Created by Andreas Appelqvist on 2016-01-08.
 * Buffer
 */
public class Buffer {

    private int max;
    private int writePos = 0;
    private int readPos = 0;
    private int findPos = 0;

    private int nbrOfReplace = 0;

    private String findString = "";
    private String replaceString = "";

    private String[] buffer;
    private BufferStatus[] status;

    private Controller controller;

    private boolean notify;

    /**
     * Konstruktor
     */
    public Buffer(Controller controller) {
        this.controller = controller;
        this.max = 10;
        buffer = new String[max];
        status = new BufferStatus[max];
        for (int i = 0; i < buffer.length; i++) {
            status[i] = BufferStatus.EMPTY;
        }
    }

    /**
     * Säger åt bufferten vilka strängar som de ska leta efter och
     * vad som ska byta ut.
     *
     * @param find
     * @param replace
     */
    public void setFindAndReplace(String find, String replace) {
        this.findString = find;
        this.replaceString = replace;
    }

    /**
     * Sätter om det ska vara notification på byte
     *
     * @param value
     */
    public void setNotify(boolean value) {
        notify = value;
    }


    /**
     * Skriver en sträng till bufferten
     *
     * @param str
     */
    public synchronized void write(String str) {
        while (status[writePos] != BufferStatus.EMPTY) {
            try {
                notifyAll();
                Thread.sleep(0);
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        buffer[writePos] = str;
        status[writePos] = BufferStatus.NEW;
        writePos = (writePos + 1) % max;
    }

    /**
     * Kollar om ett ord ska bytas ut
     */
    public synchronized void checking() {
        while (status[findPos] != BufferStatus.NEW) {
            try {
                notifyAll();
                Thread.sleep(0);
                wait();
            } catch (InterruptedException e) {
            }
        }

        String str = "";
        String newStr = "";

        if (!findString.equals("")) {

            str = buffer[findPos];

            while (str.contains(findString)) {
                newStr += str.substring(0, str.indexOf(findString));
                boolean doReplace = false;

                if (notify) {
                    int nbr = JOptionPane.showConfirmDialog(null, "Vill du byta *" + findString + "* mot *" + replaceString + "* ??");

                    if (nbr == 0) {
                        doReplace = true;
                    }
                } else {
                    doReplace = true;
                }

                if (doReplace) {
                    newStr += replaceString;
                    nbrOfReplace++;
                } else {
                    newStr += findString;
                }
                str = str.substring(str.indexOf(findString) + findString.length(), str.length());
            }
            newStr += str;
        }

        status[findPos] = BufferStatus.CHECKED;
        buffer[findPos] = newStr;

        controller.updateGUInbrReplace(nbrOfReplace);
        findPos = (findPos + 1) % max;
    }

    /**
     * Läser ord från bufferten, och skriver ut det i gui
     */
    public synchronized void read() {
        while (status[readPos] != BufferStatus.CHECKED) {
            try {
                notifyAll();
                Thread.sleep(0);
                wait();
            } catch (InterruptedException e) {
            }
        }
        String str = "";
        str = buffer[readPos];
        status[readPos] = BufferStatus.EMPTY;

        readPos = (readPos + 1) % max;
        controller.writeToGUIDest(str);
    }


    /**
     * Återställer antal byten
     */
    public void resetCount() {
        nbrOfReplace = 0;
    }

}
