package main;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.LinkedList;

/**
 * Created by Andreas Appelqvist on 2016-01-08.
 */
public class Controller {

    private Buffer buffer;
    private Writer writer;
    private Reader reader;
    private Modifier modifier;
    private GUIMonitor gui;

    /**
     * Konstruktor
     * @param gui
     */
    public Controller(GUIMonitor gui) {
        this.gui = gui;
        this.buffer = new Buffer(this);
    }

    /**
     * Skriver sträng till gui.
     * @param str
     */
    public void writeToGUIDest(String str) {
        gui.appendDestinationPane(str);
    }

    /**
     * Översätter string till linkedList.
     * @param str
     * @return
     */
    public LinkedList<String> stringToLinkedList(String str) {
        LinkedList<String> list = new LinkedList<String>();
        String[] array = str.split(" ");
        for (int i = 0; i < array.length; i++) {
            list.addLast(array[i]);
        }
        System.out.println(list);
        return list;
    }

    /**
     * Starta ny kopiering
     * @param notify
     * @param find
     * @param replace
     * @param txt
     */
    public void startCopy(boolean notify, String find, String replace, LinkedList<String> txt) {
        if (writer != null) {
            writer.stopThread();
            writer = null;
        }
        if (reader != null) {
            reader.stopThread();
            reader = null;
        }

        if (modifier != null) {
            modifier.stopThread();
            modifier = null;
        }

        writer = new Writer(buffer, txt);
        reader = new Reader(buffer);
        modifier = new Modifier(buffer);
        buffer.setNotify(notify);
        buffer.setFindAndReplace(find, replace);
        buffer.resetCount();
        runThreads();
        gui.setVisibleSaveFile(true);

    }

    /**
     * Spara fil på hårddisk
     * @param str
     */
    public void saveFile(String str){
        try (PrintStream out = new PrintStream(new FileOutputStream("saved.txt"))) {
            out.print(str);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hämtar en textfil som läses in.
     * @return
     */
    public String browserTxt() {

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "TXT");
        chooser.setFileFilter(filter);
        String txt = "";

        int returnValue = chooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            System.out.println(selectedFile);

            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))){
                String line;

                while ((line = br.readLine()) != null)
                {
                    txt += line;
                }

                br.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

        } else {
            System.err.println("fel format");
        }

        return txt;

    }

    /**
     * Uppdaterar gui med antal replaces
     * @param nbr
     */
    public void updateGUInbrReplace(int nbr){
        gui.setNumberOfReplace(nbr);
    }

    /**
     * Startar trådarna
     */
    public void runThreads() {
        writer.startThread();
        reader.startThread();
        modifier.startThread();
    }
}
