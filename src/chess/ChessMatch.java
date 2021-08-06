package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;
import tabuleiro.Board;
import tabuleiro.Piece;
import tabuleiro.Position;

public class ChessMatch {
	private Board board;
	private int turn;
	private Color player;
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	private boolean check;
	private boolean checkMate;
	
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

	public boolean getCheckMate() {
		return checkMate;
	}
	
	public boolean getCheck() {
		return check;
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
	
	private Color opponent(Color c) {
		return (c == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private ChessPiece king(Color c) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == c).collect(Collectors.toList());
		for(Piece p : list) {
			if(p instanceof King) {
				return (ChessPiece)p;
			} 
		}
		throw new IllegalStateException("Não existe nenhum rei da cor " + c);
	}
	
	private boolean testCheck(Color c) {
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(c)).collect(Collectors.toList());
		for(Piece p : opponentPieces) {
			if(p.possibleMoves()[king(c).getChessPosition().toPosition().getRow()][king(c).getChessPosition().toPosition().getColumn()]) {
				return true;
			}
		}
		return false;
	}
	
	private boolean testCheckMate(Color c) {
		if(!testCheck(c)) {
			return false;
		}
		
		List<Piece> ls = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == c).collect(Collectors.toList());
		
		for(Piece p : ls) {
			boolean[][] matrix = p.possibleMoves();
			for(int i = 0; i < board.getRows(); i++) {
				for(int j = 0; j < board.getColumns(); j++) {
					if(matrix[i][j]) {
						Position src = ((ChessPiece) p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(src, target);
						boolean testCheck = testCheck(c);
						undoMove(src, target, capturedPiece);
						if(!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
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
		if(testCheck(player)) {
			undoMove(src, target, capturedPiece);
			throw new ChessException("Você não pode se colocar em cheque");
		}
		
		check = (testCheck(opponent(player))) ? true : false;
		if(testCheckMate(opponent(player))) {
			checkMate = true;
		} else {
			nextTurn();
		}
		
		return (ChessPiece) capturedPiece;
	}
	
	private Piece makeMove(Position src, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(src);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		board.placePiece(p, target);
		
		//castling king side rook
		if(p instanceof King && target.getColumn() == src.getColumn() + 2) {
			Position srcT = new Position(src.getRow(), src.getColumn() + 3);
			Position targetT = new Position(src.getRow(), src.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(srcT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		
		//castling queen side rook
		if(p instanceof King && target.getColumn() == src.getColumn() - 2) {
			Position srcT = new Position(src.getRow(), src.getColumn() - 4);
			Position targetT = new Position(src.getRow(), src.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(srcT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		
		return capturedPiece;
	}
	
	private void undoMove(Position src, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece) board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, src);
		
		if(capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
		
		//undo castling king side rook
		if(p instanceof King && target.getColumn() == src.getColumn() + 2) {
			Position srcT = new Position(src.getRow(), src.getColumn() + 3);
			Position targetT = new Position(src.getRow(), src.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			board.placePiece(rook, srcT);
			rook.decreaseMoveCount();
		}
		
		//undo castling queen side rook
		if(p instanceof King && target.getColumn() == src.getColumn() - 2) {
			Position srcT = new Position(src.getRow(), src.getColumn() - 4);
			Position targetT = new Position(src.getRow(), src.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			board.placePiece(rook, srcT);
			rook.decreaseMoveCount();
		}
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
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE)); 
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        
        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));     
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
	}
}
