package Model;

import View.Window;

import Tools.Point;
import Tools.Size;

import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;



//ATTENTION LIGNE SUPPRIME
//import org.omg.CosNaming.IstringHelper;

public class Game implements DeletableObserver {
    private ArrayList<GameObject> objects = new ArrayList<GameObject>();
    private ArrayList<Person> population = new ArrayList<Person>();
    private Person active_player = null;

    private Window window;
    private Size size;

    public Game(Window window) {
        this.window = window;
        size = window.getMapSize();
        // Creating one Player at position (1,1)
        Person p = new Kid(new Point(10, 10), "Test", "Person", "m");
        objects.add(p);
        population.add(p);
        window.setPlayer(p);
        active_player = p;

        // Map building
        /*
        for (int i = 0; i < size; i++) {
            objects.add(new BlockUnbreakable(i, 0));
            objects.add(new BlockUnbreakable(0, i));
            objects.add(new BlockUnbreakable(i, size - 1));
            objects.add(new BlockUnbreakable(size - 1, i));
        }
        Random rand = new Random();
        for (int i = 0; i < numberOfBreakableBlocks; i++) {
            int x = rand.nextInt(size-4) + 2;
            int y = rand.nextInt(size-4) + 2;
            int lifepoints = rand.nextInt(5) + 1;
            BlockBreakable block = new BlockBreakable(x, y, lifepoints);
            block.attachDeletable(this);
            objects.add(block);
        }*/

        window.setGameObjects(this.getGameObjects());
        notifyView();
    }


    public void movePlayer(int x, int y) {
    	movePlayer(new Point(x, y));
    }

    public void movePlayer(Point p) {
        Point nextPos = active_player.getPos().add(p);

        //TODO: this is obselete
        boolean obstacle = false;
        for (GameObject object : objects) {
            if (object.isAtPosition(nextPos)) {
                obstacle = object.isObstacle();
            }
            if (obstacle == true) {
                break;
            }
        }/*
        active_player.rotate(x, y);
        if (obstacle == false) {
            active_player.move(x, y);
        }*/
        notifyView();
    }

    public void action() {
    	/*
        Activable aimedObject = null;
		for(GameObject object : objects){
			if(object.isAtPosition(active_player.getFrontX(),active_player.getFrontY())){
			    if(object instanceof Activable){
			        aimedObject = (Activable) object;
			    }
			}
		}
		if (aimedObject != null) {
		    aimedObject.activate();
            notifyView();
		}*/
        
    }

    private void notifyView() {
        window.update();
    }

    public ArrayList<GameObject> getGameObjects() {
        return this.objects;
    }

    @Override
    synchronized public void delete(Deletable ps, ArrayList<GameObject> loot) {
        objects.remove(ps);
        if (loot != null) {
            objects.addAll(loot);
        }
        notifyView();
    }


    public void playerPos() {
        System.out.println(active_player.getPos().getX() + ":" + active_player.getPos().getY());
        
    }

	public void stop() {
		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
	}


	public void sendPlayer(Point p) {
		Thread t = new Thread(new AStarThread(this, active_player, p));
		t.start();
	}


}