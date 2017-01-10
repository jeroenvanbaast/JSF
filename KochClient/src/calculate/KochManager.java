/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;

/**
 *
 * @author redxice
 */
public class KochManager {
     private JSF31KochFractalFX application;
     private KochFractal koch ;
     private  List<Edge> edges;
     private CyclicBarrier barrier;
    private Future<ArrayList<Edge>> bottomEdges;
    private Future<ArrayList<Edge>> leftEdges;
    private Future<ArrayList<Edge>> rightEdges;
    private  ExecutorService ex;
    // private int counter = 0;
    
     private 
    TimeStamp ts2;
    TimeStamp ts;
public KochManager(JSF31KochFractalFX application)
{
this.application = application;
edges = new ArrayList<Edge>(); 
koch = new KochFractal();

this.barrier = new CyclicBarrier(3);
}

  public CyclicBarrier getBarrier()
    {
        return barrier;
    }


// is aangemaakt als synchronized omdat Meerdere threads hier gebruik van maken. 
//Dankzij synchronized kan dus maarr 1 thread per keer iets adden in de edges list.
public synchronized void addEdge(Edge edge)
{
}


 public  void drawEdges() {
     
}
   public void changeLevel(int nxt) {
}

   

    
}


   

    

