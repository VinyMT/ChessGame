package chess.pieces;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;
import tabuleiro.Board;
import tabuleiro.Position;

public class Pawn extends ChessPiece{

	private ChessMatch match;
	
	public Pawn(Board board, Color color, ChessMatch match) {
		super(board, color);
		this.match = match;
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] matrix = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0, 0);
		
		if(getColor() == Color.WHITE) {
			p.setValues(position.getRow() - 1, position.getColumn());
			if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				matrix[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow() - 2, position.getColumn());
			
			Position p2 = new Position(position.getRow() - 1, position.getColumn());
			if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
				matrix[p.getRow()][p.getColumn()] = true;
			}
			
			p.setValues(position.getRow() - 1, position.getColumn() - 1);
			if(getBoard().positionExists(p) && isThereOponentPiece(p)) {
				matrix[p.getRow()][p.getColumn()] = true;
			}
			
			p.setValues(position.getRow() - 1, position.getColumn() + 1);
			if(getBoard().positionExists(p) && isThereOponentPiece(p)) {
				matrix[p.getRow()][p.getColumn()] = true;
			}
			
			//en passant white
			if(position.getRow() == 3) {
				Position left = new Position(position.getRow(), position.getColumn() - 1);
				if(getBoard().positionExists(left) && getBoard().thereIsAPiece(left) && getBoard().piece(left) == match.getEnPassantVulnerable()) {
					matrix[left.getRow() - 1][left.getColumn()] = true;
				}
				Position right = new Position(position.getRow(), position.getColumn() + 1);
				if(getBoard().positionExists(right) && getBoard().thereIsAPiece(right) && getBoard().piece(right) == match.getEnPassantVulnerable()) {
					matrix[right.getRow() - 1][right.getColumn()] = true;
				}
			}
		} else {
			p.setValues(position.getRow() + 1, position.getColumn());
			if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				matrix[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow() + 2, position.getColumn());
			
			Position p2 = new Position(position.getRow() + 1, position.getColumn());
			if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
				matrix[p.getRow()][p.getColumn()] = true;
			}
			
			p.setValues(position.getRow() + 1, position.getColumn() - 1);
			if(getBoard().positionExists(p) && isThereOponentPiece(p)) {
				matrix[p.getRow()][p.getColumn()] = true;
			}
			
			p.setValues(position.getRow() + 1, position.getColumn() + 1);
			if(getBoard().positionExists(p) && isThereOponentPiece(p)) {
				matrix[p.getRow()][p.getColumn()] = true;
			}
			
			//en passant black
			if(position.getRow() == 4) {
				Position left = new Position(position.getRow(), position.getColumn() - 1);
				if(getBoard().positionExists(left) && getBoard().thereIsAPiece(left) && getBoard().piece(left) == match.getEnPassantVulnerable()) {
					matrix[left.getRow() + 1][left.getColumn()] = true;
				}
				Position right = new Position(position.getRow(), position.getColumn() + 1);
				if(getBoard().positionExists(right) && getBoard().thereIsAPiece(right) && getBoard().piece(right) == match.getEnPassantVulnerable()) {
					matrix[right.getRow() + 1][right.getColumn()] = true;
				}
			}
		}
		
		return matrix;
	}

	@Override
	public String toString() {
		return "P";
	}
	
	
}
