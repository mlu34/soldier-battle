package fighters;

import framework.BattleField;
import framework.Random131;

//this class defines the characteristics and behavior of a "BasicSoldier"
public class BasicSoldier {

	//the relative fighting attributes of all soldiers of this class
	public final static int INITIAL_HEALTH = 10;
	public final static int ARMOR = 20;
	public final static int STRENGTH = 30;
	public final static int SKILL = 40;

	//used by other methods to specify a direction
	public final static int UP = 0;
	public final static int RIGHT = 1;
	public final static int DOWN = 2;
	public final static int LEFT = 3;
	public final static int UP_AND_RIGHT = 4;
	public final static int DOWN_AND_RIGHT = 5;
	public final static int DOWN_AND_LEFT = 6;
	public final static int UP_AND_LEFT = 7;
	public final static int NEUTRAL = -1;

	//each soldier has their own copy of these variables
	public final BattleField grid;
	public int row, col;
	public int health;
	public final int team;


	/* a constructor;
	 * takes in 4 parameters & assigns them to corresponding instance variables;
	 * sets the soldier's health to the value of INITIAL_HEALTH;
	 * the values passed in the parameters must match its variable type
	 */ 
	public BasicSoldier(BattleField gridIn, int teamIn, int rowIn, int colIn) {

		grid = gridIn;
		team = teamIn;
		row = rowIn;
		col = colIn;
		health = INITIAL_HEALTH;
	}


	/* returns true if the soldier can move, and returns false if it can't move;
	 * a soldier can move when there is an empty space adjacent to it (up, down,
	 * right, or left), this does not include diagonal movement;
	 * an empty space means there are no soldiers, no obstacles, and is not
	 * outside the battle field
	 */
	public boolean canMove() {

		return grid.get(row + 1, col) == BattleField.EMPTY ||
				grid.get(row - 1, col) == BattleField.EMPTY ||
				grid.get(row, col + 1) == BattleField.EMPTY ||
				grid.get(row, col - 1) == BattleField.EMPTY;
	}


	/* returns the number of enemies on the whole battlefield;
	 * it scans each grid one by one, and checks whether or not there is a
	 * soldier on the opposite team
	 */
	public int numberOfEnemiesRemaining() {

		//rowSize equals the number of rows on the battle field
		final int rowSize = grid.getRows();

		//colSize equals the number of columns on the battle field
		final int colSize = grid.getCols();

		int numOfEnemies = 0;

		//initializes enemyTeam as red team
		int enemyTeam = BattleField.RED_TEAM;

		//if the soldier's team is red team, then enemyTeam will be blue team
		if(team == BattleField.RED_TEAM) {

			enemyTeam = BattleField.BLUE_TEAM;
		}

		//checks each grid for enemyTeam soldiers, if there is an enemyTeam
		//soldier on the grid, numOfEnemies increases by 1
		for(int rowCounter = 0; rowCounter < rowSize; rowCounter++) {
			for(int colCounter = 0; colCounter < colSize; colCounter++) {
				if(grid.get(rowCounter, colCounter) == enemyTeam) {

					numOfEnemies++;
				}	
			}
		}

		return numOfEnemies;
	}


	/* returns the number of moves (number of up, down, right, and left 
	 * moves) it would take the soldier to reach its destination specified by 
	 * the parameter
	 */
	public int getDistance(int destinationRow, int destinationCol) {

		/*numOfMoves equals the sum of the soldier's distance from 
		 * destinationRow and the soldier's distance from destinationCol
		 */
		int numOfMoves = Math.abs(row - destinationRow) + 
				Math.abs(col - destinationCol);

		return numOfMoves;
	}


	//returns the direction of where the destination is relative to the soldier
	public int getDirection(int destinationRow, int destinationCol) {
		int direction = NEUTRAL;

		if(destinationCol == col && destinationRow > row) {
			direction = DOWN;

		} else if(destinationCol == col && destinationRow < row) {
			direction = UP;

		} else if(destinationRow == row && destinationCol < col) {
			direction = LEFT;

		} else if(destinationRow == row && destinationCol > col) {
			direction = RIGHT;

		} else if(destinationRow < row && destinationCol > col) {
			direction = UP_AND_RIGHT;

		} else if(destinationRow > row && destinationCol > col) {
			direction = DOWN_AND_RIGHT;

		} else if(destinationRow > row && destinationCol < col) {
			direction = DOWN_AND_LEFT;

		} else if(destinationRow < row && destinationCol < col) {
			direction = UP_AND_LEFT;
		}
		return direction;
	}


	/* returns the direction of the soldier's nearest team mate;
	 * if there are no nearby team mates, it will return NEUTRAL;
	 * uses the getDirection() method to get the direction of the team mate
	 */
	public int getDirectionOfNearestFriend() {

		//rowSize equals the number of rows on the battle field
		final int rowSize = grid.getRows();

		//colSize equals the number of columns on the battle field
		final int colSize = grid.getCols();

		//initial direction equals NEUTRAL
		int direction = NEUTRAL;

		//initial distance equals the sum of rows & columns on the battle field
		int distance = rowSize + colSize;

		/* checks each grid one by one for any team mates;
		 * if the team mate's distance is less than the distance variable & is
		 * not 0, then distance variable will equal the team mate's distance &
		 * the direction will equal the direction of that team mate
		 */
		for(int rowCounter = 0; rowCounter < rowSize; rowCounter++) {
			for(int colCounter = 0; colCounter < colSize; colCounter++) {
				if(grid.get(rowCounter, colCounter) == team && 
						getDistance(rowCounter, colCounter) != 0 && 
						getDistance(rowCounter, colCounter) < distance) {

					distance = getDistance(rowCounter, colCounter);
					direction = getDirection(rowCounter, colCounter);
				}
			}
		}

		return direction;
	}


	/* returns the number of nearby team mates inside the radius (measured in 
	 * moves) specified by the parameter
	 */
	public int countNearbyFriends(int radius) {
		
		//rowSize equals the number of rows on the battle field
		final int rowSize = grid.getRows();
		
		//colSize equals the number of columns on the battle field
		final int colSize = grid.getCols();
		int numOfFriends = 0;
		
		/* checks each grid one by one for a soldier's team mate that is inside
		 * the radius & adds one to numOfFriends if the conditions are true
		 */
		for(int rowCounter = 0; rowCounter < rowSize; rowCounter++) {
			for(int colCounter = 0; colCounter < colSize; colCounter++) {
				if(getDistance(rowCounter, colCounter) <= radius &&
						grid.get(rowCounter, colCounter) == team && 
						getDistance(rowCounter, colCounter) != 0) {

					numOfFriends++;
				}
			}
		}
		return numOfFriends;
	}


	//gets direction of nearest enemy inside the radius (measured in moves)
	public int getDirectionOfNearestEnemy(int radius) {
		
		//rowSize equals the number of rows on the battle field
		final int rowSize = grid.getRows();
		
		//colSize equals the number of columns on the battle field
		final int colSize = grid.getCols();
		int direction = NEUTRAL;
		int distance = grid.getRows() * grid.getCols();
		
		//initializes enemyTeam as red team
		int enemyTeam = BattleField.RED_TEAM;
		
		//if the soldier's team is red team, then enemyTeam will be blue team
		if(team == BattleField.RED_TEAM) {

			enemyTeam = BattleField.BLUE_TEAM;
		}

		/* checks each grid one by one for any enemies;
		 * if the enemy's distance is less than the distance variable & is
		 * inside the radius, then distance variable will equal the enemy's 
		 * distance & the direction will equal the direction of that enemy
		 */
		for(int rowCounter = 0; rowCounter < rowSize; rowCounter++) {
			for(int colCounter = 0; colCounter < colSize; colCounter++) {
				if(grid.get(rowCounter, colCounter) == enemyTeam && 
						getDistance(rowCounter, colCounter) <= radius &&
						getDistance(rowCounter, colCounter) < distance) {

					distance = getDistance(rowCounter, colCounter);
					direction = getDirection(rowCounter, colCounter);
				}
			}
		}
		return direction;
	}


	/* this method is called by the framework each time it is the soldier's 
	 * turn to perform an action (move, attack, or do nothing);
	 * the soldier will only attack if there is an enemy next to it, and will 
	 * only move if there is an empty space next to it; 
	 * the soldier will do nothing if it can't attack or move
	 */
	public void performMyTurn() {
		
		//gets the direction of the nearest enemy that is one move away from 
		//the soldier
		int directionOfEnemy = getDirectionOfNearestEnemy(1);

		
		/* if there is an enemy 1 move away, the soldier will attack the enemy;
		 * if there is an empty space beside the soldier, it will move there;
		 * otherwise the soldier will do nothing
		 */
		if(directionOfEnemy != NEUTRAL) {

			if(directionOfEnemy == LEFT) {
				grid.attack(row, col - 1);

			} else if(directionOfEnemy == RIGHT) {
				grid.attack(row, col + 1);

			} else if(directionOfEnemy == UP) {
				grid.attack(row - 1, col);

			} else if(directionOfEnemy == DOWN) {
				grid.attack(row + 1, col);
			} 
			
		} else if(canMove() == true) {

			if(grid.get(row, col - 1) == BattleField.EMPTY) {
				col--;

			} else if(grid.get(row, col + 1) == BattleField.EMPTY) {
				col++;

			} else if(grid.get(row - 1, col) == BattleField.EMPTY) {
				row--;

			} else if(grid.get(row + 1, col) == BattleField.EMPTY) {
				row++;
			}	
		} 
	}
}
