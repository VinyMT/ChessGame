package app;

import chess.ChessMatch;
import tabuleiro.Position;

public class Main {

	public static void main(String[] args) {
		ChessMatch match = new ChessMatch();
		UI.printBoard(match.getPieces());
	}

}
