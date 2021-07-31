package app;

import java.util.Scanner;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		ChessMatch match = new ChessMatch();
		while(true) {
			UI.printBoard(match.getPieces());
			System.out.println(" ");
			System.out.print("Source: ");
			ChessPosition src = UI.readChessPosition(scanner);
			System.out.println(" ");
			System.out.print("Target: ");
			ChessPosition target = UI.readChessPosition(scanner);
				
			ChessPiece capturedPiece = match.performChessMove(src, target);
		}
	}

}
