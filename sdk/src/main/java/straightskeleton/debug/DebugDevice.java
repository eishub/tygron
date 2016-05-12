/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package straightskeleton.debug;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.vecmath.Point3d;
import straightskeleton.Corner;
import straightskeleton.Edge;
import straightskeleton.Output;
import straightskeleton.Skeleton;
import utils.Cache;
import utils.Loop;
import utils.LoopL;
import utils.Loopable;

/**
 * Todo:
 *
 * Remove coupling to UI thread!
 *
 * Display several LoopL<Corner>
 * Hover over corners for location.
 * Hover over edges to see: current corners, end points, current gradients.
 * Need a clone ceveral LoopL<Corner>
 * Hover over corners for location.
 * Hover over edges to see: current corners, end points, current gradients.
 * Need a corners that clones currentCorners, removes machiens, stores current gradients.
 *
 * Display data in skeleton.out (points on faces, parent faces)
 * Hover over a leading corner -> view Face
 *
 * hard wired demos:
 * offset of 1cm on selected faces
 * horizontal extrude of 1cm @ height 2cm-pi/4 grad
 * 
 * pretty iutput
 * nested anchors?
 * absolute, relative anchors, repeating anchors
 *
 * @author twak
 */
public class DebugDevice
{
    // global debug switch
    public static boolean debug = false;
    static DebugDevice instance = new DebugDevice();

    public static void reset() {
        debug = false;
        instance.toDisplay.clear();
        instance.push();
    }

    static class Status
    {
        LoopL<Corner> corners;
        Output output;
        String name;
        public Status (String name, LoopL<Corner> corners, Output output)
        {
            this.name = name;
            this.corners = corners;
            this.output = output;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    List<Status> toDisplay = new ArrayList();
    JFrame window = null;
    private JDebugDevice jDD;

    public static void dump ( String name, Skeleton skel )
    {
        if (!debug)
            return;

        instance.toDisplay.add(new Status ( skel.name+":"+name, Corner.dupeNewAll(skel.findLoopLive()), skel.output.dupeEdgesOnly() ));
        push();
    }

    public static void dump ( String name, LoopL<Corner> corners )
    {
        if (!debug)
            return;

        instance.toDisplay.add(new Status ( name, Corner.dupeNewAll(corners), null ));
        push();
    }

    public static void dumpPoints ( String name, LoopL<Point3d> points )
    {
        if (!debug)
            return;

        LoopL<Corner> loopl = new LoopL();

        Cache<Point3d, Corner> cCache = new Cache<Point3d, Corner>()
        {
            @Override
            public Corner create( Point3d i )
            {
                return new Corner( i.x, i.y, i.y );
            }
        };

        for ( Loop<Point3d> lc : points )
        {
            Loop<Corner> loop = new Loop();
            loopl.add( loop );
            for ( Loopable<Point3d> loopable : lc.loopableIterator() )
            {
                Corner me = cCache.get( loopable.get());
                Corner yu = cCache.get( loopable.get());
                loop.append( me );
                me.nextC = yu;
                yu.prevC = me;
                me.nextL = new Edge (me, yu);
                yu.prevL = me.nextL;
            }
        }

        instance.toDisplay.add(new Status ( name, loopl, null ));
        push();
    }


    public static void push()
    {
        if (!debug)
            return;
        instance.push_();
    }
    
    private void push_()
    {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (window == null) {
                    window = new JFrame(this.getClass().getSimpleName());
                    window.setSize(800, 800);
                    window.setVisible(true);
                    window.setContentPane(jDD = new JDebugDevice(DebugDevice.this));

                    window.addWindowListener(new WindowAdapter() {

                        @Override
                        public void windowClosing(WindowEvent e) {
                            DebugDevice.instance.window = null;
                        }
                    });

                }
                jDD.pingChanged();
            }
        });
    }
}
