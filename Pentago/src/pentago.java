import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.geometry.Point3D;

public class pentago {

	public static void main(String[] args) {
		new pentago();

	}

	int linearBoards[][];
	int squareBoards[][][];
	boolean useLinearBoards = true;
	int counter = 0;
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	boolean isBlackPlayerTurn = true;

	pentago() {

		linearBoards = new int[4][9]; // value 0: empty
										// value -1: black
										// value +1: white
		squareBoards = new int[4][3][3]; // a stack of four 3x3 boards

		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 9; y++) {
				linearBoards[x][y] = 0;
			}
		}
		boolean gameOver = false;
		do {
			if (useLinearBoards) {
				convertBoards();
			}
			System.out.println("  0 1 2 3 4 5");
			System.out.println("  ___________");
			for (int y = 0; y < 6; y++) {
				System.out.print(y + " |");
				for (int x = 1; x < 6; x++) {// every row
					System.out.print(squareBoards[((x / 3) + (y / 3) * 2)][(x % 3)][(y % 3)] + " ");
				}
				System.out.println("|");
			}
			System.out.println("  ^^^^^^^^^^^^");
			try {
				gameOver = !playTurn();
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}

		} while (!gameOver);

	}

	public void rotateClockwise(int x) {
		if (!useLinearBoards) {
			convertBoards();
		}
		int held = linearBoards[x][1];
		for (int y = 2; y < 9; y++) {
			linearBoards[x][y - 1] = linearBoards[x][y];
		}
		linearBoards[x][9] = held;
	}

	public void rotateCounterClockwise(int x) {
		if (!useLinearBoards) {
			convertBoards();
		}
		int held = linearBoards[x][9];
		for (int y = 9; y > 2; y--) {
			linearBoards[x][y + 1] = linearBoards[x][y];
		}
		linearBoards[x][1] = held;
	}

	public boolean scoreColumns() {
		if (useLinearBoards) {
			convertBoards();
		}
		for (int x = 0; x < 6; x++) {// every column
			counter = 0;
			int type = squareBoards[x / 3][0][0];// getting type at the left-most space. This space must be skipped in next loop.
			for (int y = 1; y < 6; y++) {// every row
				if (type == squareBoards[((x / 3) + (y / 3) * 2)][(x % 3)][(y % 3)]) { // "boardNum:|"+((x/3)+(y/3)*2)+"| xval:|"+(x%3)+"| yval:|"+(y%3)+"| y:|"+y+"| x:|"+x
					counter++;
				} else {
					type = squareBoards[((x / 3) + (y / 3) * 2)][(x % 3)][(y % 3)];
					counter = 0;
				}
				if (counter >= 5) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean scoreRows() {
		if (useLinearBoards) {
			convertBoards();
		}
		for (int y = 0; y < 6; y++) {// every column
			counter = 0;
			int type = squareBoards[y / 3][0][0];// getting type at the top. This space must be skipped in next loop.
			for (int x = 1; x < 6; x++) {// every row
				if (type == squareBoards[((x / 3) + (y / 3) * 2)][(x % 3)][(y % 3)]) { // "boardNum:|" + ((x / 3) + (y / 3) * 2) + "| xval:|" + (x % 3) + "| yval:|" + (y % 3) + "| y:|" + y + "| x:|" + x
					counter++;
				} else {
					type = squareBoards[((x / 3) + (y / 3) * 2)][(x % 3)][(y % 3)];
					counter = 0;
				}
				if (counter >= 5) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean scoreDiagonals() {
		if (useLinearBoards) {
			convertBoards();
		}

		Point validDiagonals[][] = new Point[6][6];
		// ------------------- left to right
		validDiagonals[0][0].move(0, 0); // center
		validDiagonals[0][1].move(1, 1);
		validDiagonals[0][2].move(2, 2);
		validDiagonals[0][3].move(3, 3);
		validDiagonals[0][4].move(4, 4);
		validDiagonals[0][5].move(5, 5);

		validDiagonals[1][0].move(0, 1); // below
		validDiagonals[1][1].move(1, 2);
		validDiagonals[1][2].move(2, 3);
		validDiagonals[1][3].move(3, 4);
		validDiagonals[1][4].move(4, 5);

		validDiagonals[2][0].move(1, 0);// right
		validDiagonals[2][1].move(2, 1);
		validDiagonals[2][2].move(3, 2);
		validDiagonals[2][3].move(4, 3);
		validDiagonals[2][4].move(5, 4);
		// ------------------- left to right

		// ------------------- right to left
		validDiagonals[3][0].move(5, 0); // center
		validDiagonals[3][1].move(4, 1);
		validDiagonals[3][2].move(3, 2);
		validDiagonals[3][3].move(2, 3);
		validDiagonals[3][4].move(1, 4);
		validDiagonals[3][5].move(0, 5);

		validDiagonals[4][0].move(4, 0); // left
		validDiagonals[4][1].move(3, 1);
		validDiagonals[4][2].move(2, 2);
		validDiagonals[4][3].move(1, 3);
		validDiagonals[4][4].move(0, 4);

		validDiagonals[5][0].move(5, 1); // below
		validDiagonals[5][1].move(4, 2);
		validDiagonals[5][2].move(3, 3);
		validDiagonals[5][3].move(2, 4);
		validDiagonals[5][4].move(1, 5);
		// ------------------- right to left
		int type;// getting type at the top. This space must be skipped in next loop.
		Point3D space;
		Point3D spaces[];

		space = realPositionToSquarePosition(validDiagonals[1][0]);
		type = squareBoards[(int) space.getX()][(int) space.getY()][(int) space.getZ()];
		spaces = new Point3D[4];
		for (int i = 0; i < 4; i++) {
			spaces[i] = realPositionToSquarePosition(validDiagonals[1][i]);
		}
		if (type == squareBoards[(int) spaces[0].getX()][(int) spaces[0].getY()][(int) spaces[0].getZ()] && type == squareBoards[(int) spaces[1].getX()][(int) spaces[1].getY()][(int) spaces[1].getZ()] && type == squareBoards[(int) spaces[2].getX()][(int) spaces[2].getY()][(int) spaces[2].getZ()] && type == squareBoards[(int) spaces[3].getX()][(int) spaces[3].getY()][(int) spaces[3].getZ()]) {
			return true;
		}

		space = realPositionToSquarePosition(validDiagonals[2][0]);
		type = squareBoards[(int) space.getX()][(int) space.getY()][(int) space.getZ()];
		spaces = new Point3D[4];
		for (int i = 0; i < 4; i++) {
			spaces[i] = realPositionToSquarePosition(validDiagonals[2][i]);
		}
		if (type == squareBoards[(int) spaces[0].getX()][(int) spaces[0].getY()][(int) spaces[0].getZ()] && type == squareBoards[(int) spaces[1].getX()][(int) spaces[1].getY()][(int) spaces[1].getZ()] && type == squareBoards[(int) spaces[2].getX()][(int) spaces[2].getY()][(int) spaces[2].getZ()] && type == squareBoards[(int) spaces[3].getX()][(int) spaces[3].getY()][(int) spaces[3].getZ()]) {
			return true;
		}

		space = realPositionToSquarePosition(validDiagonals[4][0]);
		type = squareBoards[(int) space.getX()][(int) space.getY()][(int) space.getZ()];
		spaces = new Point3D[4];
		for (int i = 0; i < 4; i++) {
			spaces[i] = realPositionToSquarePosition(validDiagonals[4][i]);
		}
		if (type == squareBoards[(int) spaces[0].getX()][(int) spaces[0].getY()][(int) spaces[0].getZ()] && type == squareBoards[(int) spaces[1].getX()][(int) spaces[1].getY()][(int) spaces[1].getZ()] && type == squareBoards[(int) spaces[2].getX()][(int) spaces[2].getY()][(int) spaces[2].getZ()] && type == squareBoards[(int) spaces[3].getX()][(int) spaces[3].getY()][(int) spaces[3].getZ()]) {
			return true;
		}

		space = realPositionToSquarePosition(validDiagonals[5][0]);
		type = squareBoards[(int) space.getX()][(int) space.getY()][(int) space.getZ()];
		spaces = new Point3D[4];
		for (int i = 0; i < 4; i++) {
			spaces[i] = realPositionToSquarePosition(validDiagonals[5][i]);
		}
		if (type == squareBoards[(int) spaces[0].getX()][(int) spaces[0].getY()][(int) spaces[0].getZ()] && type == squareBoards[(int) spaces[1].getX()][(int) spaces[1].getY()][(int) spaces[1].getZ()] && type == squareBoards[(int) spaces[2].getX()][(int) spaces[2].getY()][(int) spaces[2].getZ()] && type == squareBoards[(int) spaces[3].getX()][(int) spaces[3].getY()][(int) spaces[3].getZ()]) {
			return true;
		}

		type = squareBoards[0][0][0];// getting type at the top. This space must be skipped in next loop.
		counter = 0;
		for (int i = 0; i < 6; i++) {
			space = realPositionToSquarePosition(validDiagonals[0][i]);
			if (squareBoards[(int) space.getX()][(int) space.getY()][(int) space.getZ()] == type) {
				counter++;
			} else {
				counter = 0;
			}
		}
		if (counter >= 5) {
			return true;
		}

		counter = 0;
		type = squareBoards[1][5][0];// getting type at the top. This space must be skipped in next loop.
		for (int i = 0; i < 6; i++) {
			space = realPositionToSquarePosition(validDiagonals[0][i]);
			if (squareBoards[(int) space.getX()][(int) space.getY()][(int) space.getZ()] == type) {

			} else {
				counter = 0;
			}
		}
		if (counter >= 5) {
			return true;
		}

		return false;
	}

	public void convertBoards() {
		if (useLinearBoards) {
			useLinearBoards = false;
			for (int x = 0; x < 4; x++) {// hard coding the conversion

				squareBoards[x][1][1] = linearBoards[x][0]; // middle of the square board

				for (int i = 1; i < 4; i++) {
					squareBoards[x][0][i - 1] = linearBoards[x][i]; // top row of the square board
				}

				squareBoards[x][1][2] = linearBoards[x][4]; // middle right of the square board

				for (int i = 2; i >= 0; i--) {
					squareBoards[x][2][i] = linearBoards[x][7 - i]; // bottom row of the square board
				}

				squareBoards[x][1][0] = linearBoards[x][8]; // middle left of the square board
			}
		} else {
			useLinearBoards = true;
			for (int x = 0; x < 4; x++) {// hard coding the conversion

				linearBoards[x][0] = squareBoards[x][1][1]; // middle of the square board

				for (int i = 1; i < 4; i++) {
					linearBoards[x][i] = squareBoards[x][0][i - 1]; // top row of the square board
				}

				linearBoards[x][4] = squareBoards[x][1][2]; // middle right of the square board

				for (int i = 2; i >= 0; i--) {
					linearBoards[x][7 - i] = squareBoards[x][2][i]; // bottom row of the square board
				}

				linearBoards[x][8] = squareBoards[x][1][0]; // middle left of the square board
			}

		}
	}

	/**
	 * Real Position To Square Position x is the board number y is the x position on that board z is the y position on that board
	 * 
	 * @param real
	 *            number Point
	 * @return Point3D
	 */
	public Point3D realPositionToSquarePosition(Point other) {
		return new Point3D(((other.getX() / 3) + (other.getY() / 3) * 2), (other.getX() % 3), (other.getY() % 3));
	}

	/**
	 * Square Position To Linear Position x is the board number y is the x position on that board
	 * 
	 * @param Point3D
	 *            Square Position
	 * @return Point linear position
	 */
	public Point squarePositionToLinearPosition(Point3D other) {

		int returnVal = 0;

		if (other.getY() == 0) {
			if (other.getZ() == 0) {
				returnVal = 1;
			} else if (other.getZ() == 1) {
				returnVal = 2;
			} else if (other.getZ() == 2) {
				returnVal = 3;
			}
		}

		else if (other.getY() == 1) {
			if (other.getZ() == 0) {
				returnVal = 8;
			} else if (other.getZ() == 1) {
				returnVal = 0;
			} else if (other.getZ() == 2) {
				returnVal = 4;
			}
		}

		else if (other.getY() == 2) {
			if (other.getZ() == 0) {
				returnVal = 7;
			} else if (other.getZ() == 1) {
				returnVal = 6;
			} else if (other.getZ() == 2) {
				returnVal = 5;
			}
		}

		return new Point((int) other.getX(), returnVal);
	}

	public boolean placePeice(int type, int linearPoint, int board) {
		if (!useLinearBoards) {
			convertBoards();
		}
		if (linearBoards[board][linearPoint] == 0) {
			linearBoards[board][linearPoint] = type;
			return true;
		}

		else
			return false;

	}

	public boolean playTurn() throws NumberFormatException, IOException {
		System.out.println(isBlackPlayerTurn ? "Black Player's Turn" : "White Player's Turn");

		System.out.println("x position");
		int xPos = Integer.parseInt(br.readLine());
		System.out.println("y position");
		int yPos = Integer.parseInt(br.readLine());

		Point placeAt = squarePositionToLinearPosition(realPositionToSquarePosition(new Point(xPos, yPos)));

		placePeice((isBlackPlayerTurn ? -1 : 1), (int) placeAt.getX(), (int) placeAt.getY());

		if (scoreColumns() || scoreRows() || scoreDiagonals()) {
			System.out.println(isBlackPlayerTurn ? "Black Player Wins!" : "White Player Wins!");
			return true;
		}
		System.out.println("block to rotate");
		int btr = Integer.parseInt(br.readLine());

		System.out.println("Rotate Clockwise[l], or Rotate Counter-Clockwise[o]");
		String rotate = br.readLine();
		if (rotate == "l") {
			rotateClockwise(btr);
		} else if (rotate == "o") {
			rotateCounterClockwise(btr);
		}

		if (scoreColumns() || scoreRows() || scoreDiagonals()) {
			System.out.println(isBlackPlayerTurn ? "Black Player Wins!" : "White Player Wins!");
			return true;
		}

		return false;

	}
}
