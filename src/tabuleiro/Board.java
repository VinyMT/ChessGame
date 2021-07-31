package tabuleiro;

public class Board {
	
	private int rows;
	private int columns;
	private Piece[][] pieces;
	
	
	public Board(int rows, int columns) {
		if(rows < 1 || columns < 1) {
			throw new BoardException("Número de linhas e colunas invalido");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}


	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
	
	public Piece piece(int row, int column) {
		if(!positionExists(row, column)) {
			throw new BoardException("Essa posição não existe");
		}
		return pieces[row][column];
	}
	
	public Piece piece(Position pos) {
		if(!positionExists(pos)) {
			throw new BoardException("Essa posição não existe");
		}
		return pieces[pos.getRow()][pos.getColumn()];
	}
	
	public void placePiece(Piece piece, Position pos) {
		if(thereIsAPiece(pos)) {
			throw new BoardException("Já existe uma peça nesse local");
		}
		pieces[pos.getRow()][pos.getColumn()] = piece;
		piece.position = pos;
	}
	
	public Piece removePiece(Position pos) {
		if(!positionExists(pos)) {
			throw new BoardException("Essa posição não existe");
		}
		
		if(piece(pos) == null) {
			return null;
		}
		
		Piece aux = piece(pos);
		aux.position = null;
		pieces[pos.getRow()][pos.getColumn()] = null;
		
		return aux;
	}
	
	private boolean positionExists(int row, int column) {
		return row < rows && row >= 0 && column < columns && column >= 0;
	}
	
	public boolean positionExists(Position pos) {
		return positionExists(pos.getRow(), pos.getColumn());
	}
	
	public boolean thereIsAPiece(Position pos) {
		if(!positionExists(pos)) {
			throw new BoardException("Essa posição não existe");
		}
		return piece(pos) != null;
	}
}
