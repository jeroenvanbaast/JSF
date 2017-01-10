/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kochclient;

import calculate.Edge;
import calculate.KochFractal;
import calculate.KochManager;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javafx.application.Platform;
import java.util.logging.Logger;
import jsf31kochfractalfx.JSF31KochFractalFX;

/**
 *
 * @author Jeroen
 */
public class Client
{

    private int poort = 4444;
    private int level = 1;
    private List<Edge> edges = new ArrayList<Edge>();
    private List<Edge> edgesMetKleur = new ArrayList<Edge>();
    private boolean klaar = false;
    private JSF31KochFractalFX applicatie;
    private KochFractal kochFractal = new KochFractal();
    private int count;
    Socket clientSocket;
    DataOutputStream out;
    ObjectInputStream inObject;

    public Client(JSF31KochFractalFX applicatie)
    {        
        this.applicatie = applicatie;
        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
        try
        {
            applicatie.clearKochPanel();
            clientSocket = new Socket(InetAddress.getByName(null), poort);
            out = new DataOutputStream(clientSocket.getOutputStream());
            inObject = new ObjectInputStream(clientSocket.getInputStream());
            out.writeInt(level);
            kochFractal.setLevel(level);
            leesInEenKeer();
            //tekenPerEdge();
        } catch (IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
           }
        };
        Thread thread = new Thread(run);
        thread.start();
    }

    public void draw()
    {
        applicatie.clearKochPanel();
        for (Edge e : edgesMetKleur)
        {            
            e.GenerateColor();
            applicatie.drawEdge(e);
        }
    }

    public void leesInEenKeer()
    {
        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                while (!klaar)
                {
                    try
                    {
                        try
                        {
                            edges = (List<Edge>) inObject.readObject();
                            System.out.println(edges);
                            Platform.runLater(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    for (Edge e : edges)
                                    {
                                        Edge e2 = new Edge(e.X1, e.Y1, e.X2, e.Y2, e.Hue);
                                        applicatie.drawEdge(e2);
                                        edgesMetKleur.add(e2);
                                    }
                                }
                            });
                        } catch (IOException ex)
                        {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (ClassNotFoundException ex)
                    {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        Thread thread = new Thread(run);
        thread.start();
    }

    public void tekenPerEdge()
    {
        Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                while (!klaar)
                {
                    try
                    {
                        try
                        {
                            Edge e = (Edge) inObject.readObject();
                            System.out.println(e);
                            Platform.runLater(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Edge e2 = new Edge(e.X1, e.Y1, e.X2, e.Y2, e.Hue);
                                    applicatie.drawEdge(e2);
                                    edgesMetKleur.add(e2);
                                }
                            });
                            count++;
                        } catch (IOException ex)
                        {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (ClassNotFoundException ex)
                    {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        };
        Thread thread = new Thread(run);
        thread.start();
    }
}
