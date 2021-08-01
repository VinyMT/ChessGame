package app;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		ChessMatch match = new ChessMatch();
		List<ChessPiece> captured = new ArrayList<>();
		while(true) {
			try {
				UI.clearScreen();
				UI.printMatch(match, captured);
				System.out.println(" ");
				System.out.print("Source: ");
				ChessPosition src = UI.readChessPosition(scanner);
				
				boolean[][] matrix = match.possibleMoves(src);
				UI.clearScreen();
				UI.printBoard(match.getPieces(), matrix);
				
				System.out.println(" ");
				System.out.print("Target: ");
				ChessPosition target = UI.readChessPosition(scanner);
				
				ChessPiece capturedPiece = match.performChessMove(src, target);
				
				if(capturedPiece != null) {
					captured.add(capturedPiece);
				}
			} catch(ChessException e) {
				System.out.println("Erro: " + e.getMessage());
				scanner.nextLine();
			} catch(InputMismatchException e) {
				System.out.println("Erro de input: " + e.getMessage());
				scanner.nextLine();
			}
		}
	}

}
