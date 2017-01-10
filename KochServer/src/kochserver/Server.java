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
        
        while(true)
        {
        //stuurPerEdge();
        stuurInEenKeer();
        }

    }

    public void stuurPerEdge() {
        try {
            serverSocket = new ServerSocket(poort, 0, InetAddress.getByName(null));
            clientSocket = serverSocket.accept();
            in = new DataInputStream(clientSocket.getInputStream());
            outObject = new ObjectOutputStream(clientSocket.getOutputStream());
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
        } catch (UnknownHostException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
            while (!klaar) {
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
                Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                try {                    
                    
                    in = new DataInputStream(clientSocket.getInputStream());
                    outObject = new ObjectOutputStream(clientSocket.getOutputStream());
                        while(true)
                        {
                            level = in.readInt();
                            currentlevel = level;
                            System.out.println(level);
                            kochManager.changeLevel(level);
                            outObject.writeObject(kochManager.getEdges());
                            System.out.println("Alles weggeschreven");
                            klaar = true;
                        }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }}
        };
        Thread thread = new Thread(run);
        thread.start();
                
            }
    }
    
    
}
