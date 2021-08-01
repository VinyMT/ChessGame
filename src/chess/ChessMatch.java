package chess;

import java.util.ArrayList;
import java.util.List;

import chess.pieces.King;
import chess.pieces.Rook;
import tabuleiro.Board;
import tabuleiro.Piece;
import tabuleiro.Position;

public class ChessMatch {
	private Board board;
	private int turn;
	private Color player;
	private List<ChessPiece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	
	public ChessMatch() {
		turn = 1;
		player = Color.WHITE;
		board = new Board(8, 8);
		initialSetup();
	}
	
	public int getTurn() {
		return turn;
	}

	public Color getPlayer() {
		return player;
	}

	public ChessPiece[][] getPieces(){
		ChessPiece[][] chessPieces = new ChessPiece[board.getRows()][board.getColumns()];
		for(int i = 0; i < board.getRows(); i ++) {
			for(int j = 0; j < board.getColumns(); j++) {
				chessPieces[i][j] = (ChessPiece) board.piece(i, j);
			} 
		}
		return chessPieces;
	}
	
	private void nextTurn() {
		turn++;
		player = (player == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	
	public boolean[][] possibleMoves(ChessPosition pos){
		Position position = pos.toPosition();
		validateSrc(position);
		
		return board.piece(position).possibleMoves();
	}
	
	public ChessPiece performChessMove(ChessPosition srcPos, ChessPosition targetPos) {
		Position src = srcPos.toPosition();
		Position target = targetPos.toPosition();
		validateSrc(src);
		validateTarget(src, target);
		Piece capturedPiece = makeMove(src, target);
		nextTurn();
		return (ChessPiece) capturedPiece;
	}
	
	private Piece makeMove(Position src, Position target) {
		Piece p = board.removePiece(src);
		Piece capturedPiece = board.removePiece(target);
		
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		board.placePiece(p, target);
		
		return capturedPiece;
	}
	
	private void validateSrc(Position src) {
		if(!board.thereIsAPiece(src)) {
			throw new ChessException("Não existe uma peça nessa posição");
		}
		if(player != ((ChessPiece)board.piece(src)).getColor()) {
			throw new ChessException("Não é a vez dessa cor ainda");
		}
		if(!board.piece(src).isThereAnyPossibleMove()) {
			throw new ChessException("Não existe nennhum movimento possivel para esta peça");
		}
	}
	
	private void validateTarget(Position src, Position target) {
		if(!board.piece(src).possibleMove(target)) {
			throw new ChessException("Essa peça não pode se mover para essa posição");
		}
	}
	
	private void initialSetup() {
		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));
	}
}
