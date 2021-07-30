package app;

import chess.ChessMatch;
import tabuleiro.BoardException;

public class Main {

	public static void main(String[] args) {
		try {
			ChessMatch match = new ChessMatch();
			UI.printBoard(match.getPieces());
		} catch (BoardException e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

}
