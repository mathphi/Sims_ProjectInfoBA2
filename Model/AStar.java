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
import Tools.Rect;
import Tools.Size;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class AStar {
	private Rect mapRect;
	private Size mapSize;
	private boolean closed[][];
	private Point posStart;
	private Point posEnd;
	private Cell[][] grid;
	private PriorityQueue<Cell> open;
	private int V_H_COST = 1;
	// private int DIAGONAL_COST = 100000;
	private GameObject movedObj;

	public AStar(Size map_size, Point pos_start, Point pos_end, GameObject moved_obj, ArrayList<GameObject> objects) {
		posStart = pos_start;
		posEnd = pos_end;
		mapSize = map_size;
		mapRect = new Rect(new Point(0, 0), map_size);
		movedObj = moved_obj;

		grid = new Cell[mapSize.getWidth()][mapSize.getHeight()];

		closed = new boolean[mapSize.getWidth()][mapSize.getHeight()];
		open = new PriorityQueue<>((Object o1, Object o2) -> {
			Cell c1 = (Cell) o1;
			Cell c2 = (Cell) o2;

			return c1.finalCost < c2.finalCost ? -1 : c1.finalCost > c2.finalCost ? 1 : 0;
		});

		for (int i = 0; i < mapSize.getWidth(); i++) {
			for (int j = 0; j < mapSize.getHeight(); j++) {
				grid[i][j] = new Cell(i, j);
				grid[i][j].heuristicCost = Math.abs(i - posEnd.getX()) + Math.abs(j - posEnd.getY());
//                  System.out.print(grid[i][j].heuristicCost+" ");
			}
//              System.out.println();
		}
		grid[posEnd.getX()][posEnd.getY()].finalCost = 0;

		open.add(grid[posStart.getX()][posStart.getY()]);
		for (GameObject o : new ArrayList<GameObject>(objects)) {
			if (o.isObstacle() && o != moved_obj) {
				setBlocked(o);
			}
		}

		// Use another end position if the selected is blocked
		if (grid[posEnd.getX()][posEnd.getY()] == null) {
			posEnd = getNextFreePos(posEnd);
		}

		Cell current;

		while (true) {
			current = open.poll();
			if (current == null)
				break;
			closed[current.i][current.j] = true;

			if (current.equals(grid[posEnd.getX()][posEnd.getY()])) {
				return;
			}

			Cell t;
			if (current.i - 1 >= 0) {
				t = grid[current.i - 1][current.j];
				checkAndUpdateCost(current, t, current.finalCost + V_H_COST);

				if (current.j - 1 >= 0) {
					t = grid[current.i - 1][current.j - 1];
					// DIAGONAL are commented because the player cannot move on diagonal !
					// checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
				}

				if (current.j + 1 < grid[0].length) {
					t = grid[current.i - 1][current.j + 1];
					// checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
				}
			}

			if (current.j - 1 >= 0) {
				t = grid[current.i][current.j - 1];
				checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
			}

			if (current.j + 1 < grid[0].length) {
				t = grid[current.i][current.j + 1];
				checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
			}

			if (current.i + 1 < grid.length) {
				t = grid[current.i + 1][current.j];
				checkAndUpdateCost(current, t, current.finalCost + V_H_COST);

				if (current.j - 1 >= 0) {
					t = grid[current.i + 1][current.j - 1];
					// checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
				}

				if (current.j + 1 < grid[0].length) {
					t = grid[current.i + 1][current.j + 1];
					// checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
				}
			}
		}
	}
	
	/**
	 * Search for next accessible position by scanning Cells in a square of the size of movedObj
	 * 
	 * @param pos
	 * The initial position (eventually blocked)
	 * 
	 * @return
	 */
	private Point getNextFreePos(Point pos) {
		int w = movedObj.getSize().getWidth();
		int h = movedObj.getSize().getHeight();

		int dx = 0;
		int dy = 0;
		
		Point nextFreePos = pos;

		int level = 1;
		int count = 1;
		for (int i = 1 ; i < w * h + 1 ; i++) {
			Point testPos = new Point(pos.getX() - dx, pos.getY() - dy);
			
			if (mapRect.contains(testPos)) {
				if (grid[testPos.getX()][testPos.getY()] != null) {
					nextFreePos = testPos;
					break;
				}
			}

			// Permutation
			int dxp = dx;
			int dyp = dy;
			
			dx = dyp;
			dy = dxp;

			// Next on the contour
			if (count % 2 != 0) {
				if (dx > dy)
					dy++;
				else
					dx++;
			}

			// Next rectangle size contour
			if (i % (level*level) == 0) {
				level++;
				
				dx = level-1;
				dy = 0;
				
				count = 1;
			}
			
			count++;
		}
		
		return nextFreePos;
	}

	static class Cell {
		int heuristicCost = 0; // Heuristic cost
		int finalCost = 0; // G+H
		int i, j;
		Cell parent;

		Cell(int i, int j) {
			this.i = i;
			this.j = j;
		}

		@Override
		public String toString() {
			return "[" + this.i + ", " + this.j + "]";
		}
	}

	private void setBlocked(GameObject o) {
		for (int i = 0; i < o.getSize().getWidth(); i++) {
			for (int j = 0; j < o.getSize().getHeight(); j++) {
				int x = o.getPos().getX() + i;
				int y = o.getPos().getY() + j;

				grid[x][y] = null;

				// Add contour to obstacles to allow to move object with size larger than 1x1
				for (int w = 0 ; w < movedObj.getSize().getWidth() ; w++) {
					for (int h = 0 ; h < movedObj.getSize().getHeight() ; h++) {
						if (mapRect.contains(new Point(x - w, y - h))) {
							grid[x - w][y - h] = null;
						}
					}
				}
			}
		}
	}

	private void checkAndUpdateCost(Cell current, Cell t, int cost) {
		if (t == null || closed[t.i][t.j])
			return;
		int t_final_cost = t.heuristicCost + cost;

		boolean inOpen = open.contains(t);
		if (!inOpen || t_final_cost < t.finalCost) {
			t.finalCost = t_final_cost;
			t.parent = current;
			if (!inOpen)
				open.add(t);
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
