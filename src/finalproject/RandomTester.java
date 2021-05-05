package finalproject;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class RandomTester {
	private static final double SEED_DENSITY = 0.05;
	private static final double MIN_CLUE_DENSITY = 0.2;
	private static final double MAX_CLUE_DENSITY = 0.5;
	private static final boolean SHOW_PROGRESS = false;
	private static final long TIMEOUT_MILLIS = 60000;
	private static final boolean SHOW_SUMMARY = true;

	private static Random rand = new Random();
	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {

		// Greeting/message
		System.out.println(
				"NOTE: Your solving algorithm is used in the process of generating random puzzles.."
						+ "\nIf it does not work, the generated puzzles may not be solvable.\n");

		// Get number of puzzles
		System.out.print("How many puzzles would you like to generate?\n>> ");
		String userInput = sc.nextLine();
		int numPuzzles;
		while (true) {
			try {
				numPuzzles = Integer.parseInt(userInput);
				if (numPuzzles <= 0) {
					System.out.print(
							"The number of puzzles must be greater than zero\n>> ");
					continue;
				} else
					break;
			} catch (NumberFormatException e) {
				System.out.print(userInput + " is not a valid integer\n>> ");
				userInput = sc.nextLine();
			}
		}

		// Get size of puzzles
		System.out.print(
				"What size puzzle would you like? (Note that a standard sudoku has SIZE = 3)\n>> ");
		userInput = sc.nextLine();
		int size;
		while (true) {
			try {
				size = Integer.parseInt(userInput);
				if (size < 1 || size > 100) {
					System.out.print(
							"size must be between 1 and 100 (both inclusive)\n>> ");
					continue;
				}
				break;
			} catch (NumberFormatException e) {
				System.out.print(userInput + " is not a valid integer\n>> ");
				userInput = sc.nextLine();
			}
		}
		System.out.println();

		// Generate and solve sudokus
		long duration;
		long minTime = Long.MAX_VALUE;
		long maxTime = 0;
		long total = 0;
		for (int i = 0; i < numPuzzles; i++) {
			if (SHOW_PROGRESS)
				System.out.println("Generating puzzle... ");
			ChessSudoku puzzle = makeRandomPuzzle(size, false, false, false);
			ChessSudoku puzzleCopy = deepCopyPuzzle(puzzle);
			if (SHOW_PROGRESS)
				System.out.print("Solving... ");

			try {
				duration = Tester.runSolve(puzzle, false, TIMEOUT_MILLIS);
				if (Tester.isSolved(puzzle, false, false, false)) {
					System.out.println((double) duration / 1000000 + " ms");
				} else {
					System.out.println("Puzzle not solved");
					System.out.println("Original puzzle:");
					puzzleCopy.print();
					System.out.println();
					System.out.println("Received puzzle:");
					puzzle.print();
					System.out.println();
					System.out.println();
				}
			} catch (TimeoutException e) {
				System.out.println("[Timeout after " + TIMEOUT_MILLIS + " ms]");
				System.out.println("Original puzzle:");
				puzzleCopy.print();
				System.out.println();
				duration = TIMEOUT_MILLIS * 1000000;
			} catch (InterruptedException | ExecutionException e) {
				System.out.println("An unexpected error occurred: ");
				e.printStackTrace();
				duration = TIMEOUT_MILLIS * 1000000;
			}

			// Collect stats
			if (SHOW_SUMMARY) {
				if (duration < minTime)
					minTime = duration;
				if (duration > maxTime)
					maxTime = duration;
				total += duration;
			}
		}

		// Show stats
		if (SHOW_SUMMARY) {
			System.out.println();
			System.out.println("Stats:");
			System.out.println("------");
			System.out.printf("Fastest solve: %.3f ms\n",
					(double) minTime / 1000000);
			System.out.printf("Slowest solve: %.3f ms\n",
					(double) maxTime / 1000000);
			System.out.printf("Average: %.3f ms\n",
					(double) total / (1000000 * numPuzzles));
		}

		// Force the program to exit to make sure all solves stop
		System.exit(0);
	}

	public static ChessSudoku seedPuzzle(int size, boolean knightRule,
			boolean kingRule, boolean queenRule) {
		ChessSudoku puzzle = new ChessSudoku(size);
		int n = size * size;

		int numCluesToAdd = (int) (SEED_DENSITY * n * n);
		int currentNumClues = 0;
		while (currentNumClues < numCluesToAdd) {
			// Decide where to put next clue and what value to give it
			int row = rand.nextInt(n);
			int col = rand.nextInt(n);
			if (puzzle.grid[row][col] != 0)
				continue;
			int val = rand.nextInt(n);

			// Assign value and check that puzzle is still valid
			puzzle.grid[row][col] = val;
			boolean valid = Tester.isValidValue(puzzle, row, col, knightRule,
					kingRule, queenRule);
			if (!valid)
				puzzle.grid[row][col] = 0;
			else
				currentNumClues++;
		}

		return puzzle;
	}

	private static ChessSudoku makeRandomPuzzle(int size, boolean knightRule,
			boolean kingRule, boolean queenRule) {
		int n = size * size;

		ChessSudoku puzzle = seedPuzzle(size, knightRule, kingRule, queenRule);
		puzzle.solve(false);

		// Decide how many clues to give
		int minInt = (int) (MIN_CLUE_DENSITY * n * n);
		int maxInt = (int) (MAX_CLUE_DENSITY * n * n);
		int numCluesToGive = minInt + rand.nextInt(maxInt - minInt);
		int currentNumClues = n * n;
		while (currentNumClues > numCluesToGive) {
			// Decide which clue to remove next
			int row = rand.nextInt(n);
			int col = rand.nextInt(n);
			if (puzzle.grid[row][col] == 0)
				continue;

			puzzle.grid[row][col] = 0;
			currentNumClues--;
		}

		return puzzle;
	}

	private static ChessSudoku deepCopyPuzzle(ChessSudoku puzzle) {
		ChessSudoku newPuzzle = new ChessSudoku(puzzle.SIZE);
		newPuzzle.grid = deepCopyGrid(puzzle.grid);
		newPuzzle.knightRule = puzzle.knightRule;
		newPuzzle.kingRule = puzzle.kingRule;
		newPuzzle.queenRule = puzzle.queenRule;

		return newPuzzle;
	}

	private static int[][] deepCopyGrid(int[][] grid) {
		int numRows = grid.length;
		int numCols = grid[0].length;
		int[][] newGrid = new int[numRows][numCols];

		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				newGrid[r][c] = grid[r][c];
			}
		}

		return newGrid;
	}
}
