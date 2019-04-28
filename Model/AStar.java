/**  AStar: algorithme de plus court chemin
 * 
 * Vous ne devez pas commprendre cet algorithm en detail. Vous pouvez simplement l'utiliser
 * de la facon suivante: 
 * 
 * -> on instancie un objet AStart avec la position de depart, la destination et la liste de obstacles.
 * -> on invoque la methode getNextStep pour avoir la direction à suivre lors du premier changement de
 *    case. 0,1,2,3 correspondent restpectivement a Est, Nord, Ouest, Sud.
 * 
 * */
package Model;

import Tools.Point;
import Tools.Size;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class AStar {
	private Size mapSize;
	private boolean closed[][];
	private Point posStart;
	private Point posEnd;
	private Cell [][] grid; 
	private PriorityQueue<Cell> open;
	private int V_H_COST = 1;
	private int DIAGONAL_COST = 100000;

	public AStar(Size map_size, Point pos_start, Point pos_end, ArrayList<GameObject> objects) {
		posStart = pos_start;
		posEnd = pos_end;
		mapSize = map_size;
		
		grid = new Cell[mapSize.getWidth()][mapSize.getHeight()];


        closed = new boolean[mapSize.getWidth()][mapSize.getHeight()];
        open = new PriorityQueue<>((Object o1, Object o2) -> {
                Cell c1 = (Cell)o1;
                Cell c2 = (Cell)o2;

                return c1.finalCost<c2.finalCost?-1:
                        c1.finalCost>c2.finalCost?1:0;

        	});

        for(int i = 0 ; i<mapSize.getWidth() ; i++) {
              for(int j = 0 ; j < mapSize.getHeight() ; j++) {
                  grid[i][j] = new Cell(i, j);
                  grid[i][j].heuristicCost = Math.abs(i-posEnd.getX())+Math.abs(j-posEnd.getY());
//                  System.out.print(grid[i][j].heuristicCost+" ");
              }
//              System.out.println();
           }
        grid[posEnd.getX()][posEnd.getY()].finalCost = 0;

		open.add(grid[posStart.getX()][posStart.getY()]);
		for(GameObject o: objects) {
			if (o.isObstacle()) {
				for (int i = 0 ; i < o.getSize().getWidth() ; i++) {
					for (int j = 0 ; j < o.getSize().getHeight() ; j++) {
						setBlocked(o.getPos().add(i, j));
					}
				}
			}
		}

		Cell current;

		while(true){ 
			current = open.poll();
			if(current==null)break;
			closed[current.i][current.j]=true; 

			if(current.equals(grid[posEnd.getX()][posEnd.getY()])){
				return; 
			} 

			Cell t;  
			if(current.i-1>=0){
				t = grid[current.i-1][current.j];
				checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 

				if(current.j-1>=0){                      
					t = grid[current.i-1][current.j-1];
					checkAndUpdateCost(current, t, current.finalCost+DIAGONAL_COST); 
				}

				if(current.j+1<grid[0].length){
					t = grid[current.i-1][current.j+1];
					checkAndUpdateCost(current, t, current.finalCost+DIAGONAL_COST); 
				}
			} 

			if(current.j-1>=0){
				t = grid[current.i][current.j-1];
				checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 
			}

			if(current.j+1<grid[0].length){
				t = grid[current.i][current.j+1];
				checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 
			}

			if(current.i+1<grid.length){
				t = grid[current.i+1][current.j];
				checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 

				if(current.j-1>=0){
					t = grid[current.i+1][current.j-1];
					checkAndUpdateCost(current, t, current.finalCost+DIAGONAL_COST); 
				}

				if(current.j+1<grid[0].length){
					t = grid[current.i+1][current.j+1];
					checkAndUpdateCost(current, t, current.finalCost+DIAGONAL_COST); 
				}  
			}
		} 
	}

	static class Cell{  
		int heuristicCost = 0; //Heuristic cost
		int finalCost = 0; //G+H
		int i, j;
		Cell parent; 

		Cell(int i, int j){
			this.i = i;
			this.j = j; 
		}

		@Override
		public String toString(){
			return "["+this.i+", "+this.j+"]";
		}
	}


	private void setBlocked(Point p){
		grid[p.getX()][p.getY()] = null;
	}

	private void checkAndUpdateCost(Cell current, Cell t, int cost){
		if(t == null || closed[t.i][t.j])return;
		int t_final_cost = t.heuristicCost+cost;

		boolean inOpen = open.contains(t);
		if(!inOpen || t_final_cost<t.finalCost){
			t.finalCost = t_final_cost;
			t.parent = current;
			if(!inOpen)open.add(t);
		}
	}

	public int getNextStep() {
		int direction = -1;
		if (closed[posEnd.getX()][posEnd.getY()]) {
			int deltai = 0;
			int deltaj = 0;
			// Trace back the path
			Cell current = grid[posEnd.getX()][posEnd.getY()];
			while (current != null && current.parent != null) {
				if (current.parent.i == posStart.getX() && current.parent.j == posStart.getY()) {
					deltai = current.i - posStart.getX();
					deltaj = current.j - posStart.getY();
					direction = 1 - deltai + deltaj * (deltaj + 1);
				}
				current = current.parent;
			}
		} else
			System.out.println("No possible path");

		return direction;
	}
}
