/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kochserver;

import calculate.Edge;
import calculate.KochFractal;
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
import timeutil.TimeStamp;

/**
 *
 * @author redxice
 */
public class KochManager {

    private KochFractal koch;
    private List<Edge> edges;
    private CyclicBarrier barrier;
    private Future<ArrayList<Edge>> bottomEdges;
    private Future<ArrayList<Edge>> leftEdges;
    private Future<ArrayList<Edge>> rightEdges;
    private ExecutorService ex;

    private TimeStamp ts2;
    TimeStamp ts;

    public KochManager() {
        edges = new ArrayList<Edge>();
        koch = new KochFractal();

        this.barrier = new CyclicBarrier(3);
    }

    public CyclicBarrier getBarrier() {
        return barrier;
    }

// is aangemaakt als synchronized omdat Meerdere threads hier gebruik van maken. 
//Dankzij synchronized kan dus maarr 1 thread per keer iets adden in de edges list.
    public synchronized void addEdge(Edge edge) {
        this.edges.add(edge);
    }


    public void changeLevel(int nxt) {
        edges.clear();
        koch.setLevel(nxt);
   ex = Executors.newFixedThreadPool(3);
   MyCallable bottom = new MyCallable(this,"Bottom",nxt);
   MyCallable left = new MyCallable(this,"Left",nxt);
   MyCallable right = new MyCallable(this,"Right",nxt);
   ts = new TimeStamp();
   ts.setBegin("Begin");
   bottomEdges = ex.submit(bottom);
   leftEdges = ex.submit(left);
   rightEdges= ex.submit(right);
   try{
       this.edges.addAll(bottomEdges.get());
       this.edges.addAll(leftEdges.get());
       this.edges.addAll(rightEdges.get());
   }

  catch(InterruptedException | ExecutionException ex){
      System.out.println(ex.getMessage());
  }
  ex.shutdown();
    }

    public void LeesWithMapping() {
        try {
            TimeStamp T = new TimeStamp();
            T.setBegin("Begin reading mapped FILE :");
            RandomAccessFile ras = new RandomAccessFile("edges.out", "r");
            FileChannel fc = ras.getChannel();
            MappedByteBuffer MBB = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            while (MBB.hasRemaining()) {
                Edge e = new Edge(MBB.getDouble(), MBB.getDouble(), MBB.getDouble(), MBB.getDouble(),MBB.getInt());
                edges.add(e);
            }
            fc.close();
            T.setEnd("Done reading mapped File");
            System.out.println(T.toString());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<Edge> getEdges() {
        return edges;
    }

}
