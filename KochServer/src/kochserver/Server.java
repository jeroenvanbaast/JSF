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
    
    public Server() 
    {
        //stuurPerEdge();
        stuurInEenKeer();
    }
    
    public void stuurPerEdge()
    {
        try {
            serverSocket = new ServerSocket(poort, 0, InetAddress.getByName(null));
            System.out.println("Server wacht tot client is gestart");
            clientSocket = serverSocket.accept();
            System.out.println("Client is gestart");
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
          //  DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            ObjectOutputStream outObject = new ObjectOutputStream(clientSocket.getOutputStream());
            while(!klaar)
            {
                level = in.readInt();
                if(level != currentlevel)
                {     
                currentlevel = level;   
                System.out.println(level);
                kochManager.changeLevel(level);
                for(Edge e :kochManager.getEdges())
                {
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
            System.out.println("Server wacht tot client is gestart");
            clientSocket = serverSocket.accept();
            System.out.println("Client is gestart");
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
          //  DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            ObjectOutputStream outObject = new ObjectOutputStream(clientSocket.getOutputStream());
            while(!klaar)
            {
                level = in.readInt();
                if(level != currentlevel)
                {     
                currentlevel = level;   
                System.out.println(level);
                kochManager.changeLevel(level);               
                outObject.writeObject(kochManager.getEdges());
                System.out.println("Alles weggeschreven");
                //klaar = true;
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
}
