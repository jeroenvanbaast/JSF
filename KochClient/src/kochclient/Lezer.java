/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kochclient;

import calculate.Edge;
import calculate.KochFractal;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.FileSystems;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import timeutil.TimeStamp;

/**
 *
 * @author jsf3
 */
public class Lezer
{

    private KochFractal koch;
    private TimeStamp T = new TimeStamp();
    ByteArrayOutputStream baos;
    ObjectOutputStream ops;
    private ArrayList<Edge> edges = new ArrayList<>();
    FileInputStream fs;
    private FileLock sharedLock = null;
    private FileLock sharedEdgesLock = null;
    private int AmountOfEdges = 0;
    private int ReadingPosistion = 5;

    public void readWithMapping()
    {
        try
        {
        
            edges.clear();
            int nrEdges = 0;
            RandomAccessFile ras = new RandomAccessFile("edges.out", "r");
            FileChannel fc = ras.getChannel();
            MappedByteBuffer MBB = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            while (true)
            {  
               
                MBB.position(0);
                sharedEdgesLock = fc.lock(0, 4, true);
                if (nrEdges != MBB.getInt(0))
                {    
                    edges.clear();
                    ReadingPosistion = 5;
                    nrEdges = MBB.getInt(0);
                    System.out.println("nrEdges :"+nrEdges);
                }
                sharedEdgesLock.release();
                sharedLock = fc.lock(ReadingPosistion, nrEdges * 4*8+5, true);
                MBB.position(ReadingPosistion);
                
                while (MBB.hasRemaining())
                {  
                    if (ReadingPosistion<nrEdges*4*8+5)
                    {
                    Edge e = new Edge(MBB.getDouble(), MBB.getDouble(), MBB.getDouble(), MBB.getDouble(),MBB.getDouble());
                    addEdge(e);
                    }
                    else{
                        break;
                    }
                }
                sharedLock.release();
            }
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(Lezer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(Lezer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void readBinairWithoutBuffer()
    {

        try
        {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path logDir = Paths.get("C:/Users/redxice/Desktop/kochschrijveBinar/Edges");
            logDir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
            while (true)
            {
                WatchKey key = watcher.take();
                for (WatchEvent event : key.pollEvents())
                {
                    if (event.kind() == ENTRY_CREATE || event.kind() == ENTRY_MODIFY)
                    {
                        //
                        // Get the name of created file.
                        //
                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path filename = ev.context();
                        Path child = logDir.resolve(filename);
                        WatchEvent.Kind kind = ev.kind();
                        T.setBegin("Begin Reading");
                        FileInputStream fis = new FileInputStream("Test.tmp");
                        ObjectInputStream ois = new ObjectInputStream(fis);
                        ArrayList<calculate.Edge> l = (ArrayList<calculate.Edge>) ois.readObject();
                        T.setEnd("Done reading");
                        System.out.println(T.toString());
                        System.out.println(l);
                        ois.close();
                    }

                }
            }
        } catch (InterruptedException ex)
        {
            Logger.getLogger(Lezer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(Lezer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(Lezer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addEdge(Edge edge)
    {
        this.edges.add(edge);
        ReadingPosistion = 4 * 8 + ReadingPosistion;
        AmountOfEdges++;
    }
}
