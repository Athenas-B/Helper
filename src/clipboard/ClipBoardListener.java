/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import clipboard.Record;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oldřich
 */
public class ClipBoardListener extends Thread implements ClipboardOwner {

    Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
    Collection<Record> data;
    private volatile boolean pleseDie;

    public ClipBoardListener() {
        this.pleseDie = false;
        this.setDaemon(true);
    }
   
    
    @Override
    public void run() {
        while (!pleseDie) {
            try {
                Transferable trans = sysClip.getContents(this);
                TakeOwnership(trans);
                ClipBoardListener.sleep(100);

                System.out.println("running");
            } catch (InterruptedException ex) {
                Logger.getLogger(ClipBoardListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void lostOwnership(Clipboard c, Transferable t) {

        try {
            ClipBoardListener.sleep(250);  //waiting e.g for loading huge elements like word's etc.
        } catch (InterruptedException e) {
            System.out.println("Exception: " + e);
        }
        Transferable contents = sysClip.getContents(this);
        try {
            process_clipboard(contents, c);
        } catch (Exception ex) {
            //Logger.getLogger(BoardListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        TakeOwnership(contents);

    }

    void TakeOwnership(Transferable t) {
        sysClip.setContents(t, this);
    }

    public void process_clipboard(Transferable t, Clipboard c) { //your implementation
        String tempText;
        Transferable trans = t;
        System.out.println("processing");
        try {
            if (trans != null ? trans.isDataFlavorSupported(DataFlavor.stringFlavor) : false) {
                tempText = (String) trans.getTransferData(DataFlavor.stringFlavor);
                System.out.println(tempText);
                data.add(new Record(tempText));
            }

        } catch (UnsupportedFlavorException | IOException e) {
        }
    }

    public Collection<Record> getData() {
        return data;
    }

    public void setData(Collection<Record> data) {
        this.data = data;
    }

    public boolean isPleseDie() {
        return pleseDie;
    }

    public void setPleseDie(boolean pleseDie) {
        this.pleseDie = pleseDie;
    }

}
