/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kochserver;

import calculate.Edge;
import calculate.KochFractal;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeroen
 */
public class Server {

    private int poort = 4444;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private boolean klaar = false;
    private KochManager kochManager = new KochManager();
    private int level;
    private int currentlevel;
    private DataInputStream in;
    private ObjectOutputStream outObject;

    public Server() {
        //stuurPerEdge();
        stuurInEenKeer();


    }

    public void stuurPerEdge() {
        try {
            serverSocket = new ServerSocket(poort, 0, InetAddress.getByName(null));
            clientSocket = serverSocket.accept();
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            ObjectOutputStream outObject = new ObjectOutputStream(clientSocket.getOutputStream());
            while (!klaar) {
                level = in.readInt();
                if (level != currentlevel) {
                    currentlevel = level;
                    System.out.println(level);
                    kochManager.changeLevel(level);
                    for (Edge e : kochManager.getEdges()) {
                        outObject.writeObject(e);
                    }
                    System.out.println("Alles weggeschreven");
                    //klaar = true;
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stuurInEenKeer() 
    {
        try {
            serverSocket = new ServerSocket(poort, 0, InetAddress.getByName(null));
            clientSocket = serverSocket.accept();
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            ObjectOutputStream outObject = new ObjectOutputStream(clientSocket.getOutputStream());
            while (!klaar) {
            Runnable run = new Runnable()
            {
            @Override
            public void run()
            {
                try {
                    level = in.readInt();
                    if (level != currentlevel) {
                        currentlevel = level;
                        System.out.println(level);
                        kochManager.changeLevel(level);
                        outObject.writeObject(kochManager.getEdges());
                        System.out.println("Alles weggeschreven");
                        klaar = true;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
             };
            Thread thread = new Thread(run);
            thread.start();

            }
            } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    
}
