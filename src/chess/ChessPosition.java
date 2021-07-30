package chess;

import tabuleiro.Position;

public class ChessPosition {
	private char column;
	private int row;
	
	public ChessPosition(char column, int row) {
		if(column < 'a' || column > 'h' || row < 1 || row > 8) {
			throw new ChessException("Valores invalidos");
		}
		this.column = column;
		this.row = row;
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}
	
	protected Position toPosition() {
		int posColumn = column - 'a';
		int posRow = 8 - row;
		
		return new Position(posRow, posColumn);
	}
	
	protected ChessPosition fromPosition(Position pos) {
		return new ChessPosition((char)('a'- pos.getColumn()), 8 - pos.getRow());
	}

	@Override
	public String toString() {
		return "" + column + row;
	}
	
}
