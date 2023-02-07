package ai;

import java.util.ArrayList;

import main.GamePanel;

public class PathFinder {

	GamePanel gamePanel;
	Node[][] node;
	ArrayList<Node> openList = new ArrayList<>();
	public ArrayList<Node> pathList = new ArrayList<>(); //NPC AND MONSTERS CAN TRACK THIS PATH
	Node startNode, goalNode, currentNode;
	boolean goalReached = false;
	int step = 0;
	
	public PathFinder(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		instantiateNodes();
	}
	
	public void instantiateNodes() { //CREATE A NODE FOR EVERY TILE ON THE MAP
		
		node = new Node[gamePanel.maxWorldCol][gamePanel.maxWorldRow];
		
		int col = 0;
		int row = 0;
		
		while (col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {
			
			node[col][row] = new Node(col, row);
			
			col++;
			if (col == gamePanel.maxWorldCol) {
				col = 0;
				row++;
			}
		}
	}
	
	public void resetNodes() {
		
		int col = 0;
		int row = 0;
		
		while (col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {
			
			//RESET NODE DATA
			node[col][row].open = false;
			node[col][row].checked = false;
			node[col][row].solid = false;
			
			col++;
			if (col == gamePanel.maxWorldCol) {
				col = 0;
				row++;
			}
			
		}
		
		//RESET OTHER SETTINGS
		openList.clear();
		pathList.clear();
		goalReached = false;
		step = 0;
	}
	
	public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
		
		resetNodes();
		
		//SET START AND GOAL NODES
		startNode = node[startCol][startRow];
		currentNode = startNode;
		goalNode = node[goalCol][goalRow];
		openList.add(currentNode);
		
		int col = 0;
		int row = 0;
		
		while (col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {
			
			//SET SOLID NODE
			//CHECK TILES
			int tileNumber = gamePanel.tileManager.mapTileNumber[gamePanel.currentMap][col][row];
			if (gamePanel.tileManager.tile[tileNumber].collision == true) {
				node[col][row].solid = true;
			}
			
			//CHECK INTERACTIVE TILES
			for (int i = 0; i < gamePanel.interactTile[1].length; i++) {
				if (gamePanel.interactTile[gamePanel.currentMap][i] != null && gamePanel.interactTile[gamePanel.currentMap][i].destructible == true) {
					int itCol = gamePanel.interactTile[gamePanel.currentMap][i].worldX/gamePanel.tileSize;
					int itRow = gamePanel.interactTile[gamePanel.currentMap][i].worldY/gamePanel.tileSize;
					node[itCol][itRow].solid = true;
				}
			}
			
			//SET COST
			getCost(node[col][row]);
			
			col++;
			if (col == gamePanel.maxWorldCol) {
				col = 0;
				row++;
			}
		}
	}
	
	public void getCost(Node node) {
		
		//G COST
		int xDistance = Math.abs(node.col - startNode.col);
		int yDistance = Math.abs(node.row - startNode.row);
		node.gCost = xDistance + yDistance;
		
		//H COST
		xDistance = Math.abs(node.col - goalNode.col);
		yDistance = Math.abs(node.row - goalNode.row);
		node.hCost = xDistance + yDistance;
		
		//F COST
		node.fCost = node.gCost + node.hCost;
	}
	
	public boolean search() {
		
		while (goalReached == false && step < 500) {
			int col = currentNode.col;
			int row = currentNode.row;
			
			//CHECK THE CURRENT NODE
			currentNode.checked = true;
			openList.remove(currentNode);
			
			//OPEN THE UP NODE
			if (row - 1 >= 0) {
				openNode(node[col][row-1]);
			}
			
			//OPEN THE LEFT NODE
			if (col - 1 >= 0) {
				openNode(node[col-1][row]);
			}
			
			//OPEN THE DOWN NODE
			if (row + 1 < gamePanel.maxWorldRow) {
				openNode(node[col][row+1]);
			}
			
			//OPEN THE RIGHT NODE
			if (col + 1 < gamePanel.maxWorldCol) {
				openNode(node[col+1][row]);
			}
			
			//FIND THE BEST NODE
			int bestNodeIndex = 0;
			int bestNodefCost = 999;
			
			for (int i = 0; i < openList.size(); i++) {
				
				//CHECK IF THE NODES fCOST IS BETTER
				if (openList.get(i).fCost < bestNodefCost) {
					bestNodeIndex = i;
					bestNodefCost = openList.get(i).fCost;
				}
				
				//IF fCOST IS EQUAL THEN CHECK THE gCOST
				else if (openList.get(i).fCost == bestNodefCost) {
					if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
						bestNodeIndex = i;
					}
				}
			}
			
			//IF THERE IS NO NODE IN THE OPENLIST, END LOOP
			if (openList.size() == 0) {
				break;
			}
			
			//AFTER THE LOOP, OPENLIST[BESTNODEINDEX] IS THE NEXT STEP (= currentNode)
			currentNode = openList.get(bestNodeIndex);
			
			if (currentNode == goalNode) {
				goalReached = true;
				trackThePath();
			}
			step++;
		}
		return goalReached;
	}
	
	public void openNode(Node node) {
		
		if (node.open == false && node.checked == false && node.solid == false) {
			
			node.open = true;
			node.parent = currentNode;
			openList.add(node);
		}
	}
	
	public void trackThePath() {
		
		Node current = goalNode;
		
		while(current != startNode) {
			
			pathList.add(0, current); //ADDING TO THE FIRST SLOT SO THE LAST NODE ADDED IS IN THE [0]
			current = current.parent;
		}
	}
}
