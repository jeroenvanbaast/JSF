/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kochserver;

import calculate.Edge;
import calculate.KochFractal;
import kochserver.KochManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.concurrent.Callable;



/**
 *
 * @author redxice
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class MyCallable implements Observer,Callable{
    KochFractal koch;
    String name;
    KochManager manager;
    private List<Edge> edges ;
    public MyCallable(KochManager manager,String name,int nxt){
        this.koch = new KochFractal();
        this.koch.setLevel(nxt);
        this.name = name;
        this.manager = manager;
        this.koch.addObserver(this);   
        this.edges = new ArrayList<>();
        
    }
  
   

    @Override
    public synchronized void update(java.util.Observable o, Object o1) {
        Edge e= (Edge)o1;
        Edge edge = new Edge(e.X1,e.Y1,e.X2,e.Y2,e.Hue);
        manager.addEdge(edge);
    }

    @Override
    public Object call() throws Exception
    {
        
          switch (this.name) {
            case "Bottom":
                this.koch.generateBottomEdge();
              //  this.manager.AddToCounter();
                break;
            case "Left":
                this.koch.generateLeftEdge();
                // this.manager.AddToCounter();
                break;
            case "Right":
                this.koch.generateRightEdge();
                // this.manager.AddToCounter();
                break;
            default:
                break;
        }
          this.manager.getBarrier().await();
          return this.edges;
    }



  
    
}
