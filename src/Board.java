package src;
import java.awt.*;
import java.awt.image.*;

public class Board {
    static Piece[][] position = new Piece[8][8];
    static Piece[] whitePieces = new Piece[16];
    static Piece[] blackPieces = new Piece[16];
    static King whiteKing;
    static King blackKing;
    
    static Piece selectedPiece = null;
    static int turn = 1;

    //for drawing move arrow
    static int[] previousPosition = {-1, -1};
    static int[] newPosition = {-1, -1};

    //for drawing selected piece being dragged and dropped
    static boolean dragged = false;
    static int dragX = -1;
    static int dragY = -1;

    static void initializeBoard() {//adds pieces to arrays
        for (int i = 0; i < 8; i++) {
            position[i][6] = new Pawn(1, i, 6);//white pawns
            position[i][1] = new Pawn(-1, i, 1);//black pawns
        }
        
        for (int i = -1; i <= 1; i+=2) {//-1 = black, 1 = white
            position[0][(i+1)/2*7] = new Rook(i, 0, (i+1)/2*7);
            position[1][(i+1)/2*7] = new Knight(i, 1, (i+1)/2*7);
            position[2][(i+1)/2*7] = new Bishop(i, 2, (i+1)/2*7);
            position[3][(i+1)/2*7] = new Queen(i, 3, (i+1)/2*7);
            position[4][(i+1)/2*7] = new King(i, 4, (i+1)/2*7);
            position[5][(i+1)/2*7] = new Bishop(i, 5, (i+1)/2*7);
            position[6][(i+1)/2*7] = new Knight(i, 6, (i+1)/2*7);
            position[7][(i+1)/2*7] = new Rook(i, 7, (i+1)/2*7);
        }

        for (int i = 0; i < 8; i++) {
            whitePieces[i] = position[i][7];
            whitePieces[i+8] = position[i][6];
            blackPieces[i] = position[i][0];
            blackPieces[i+8] = position[i][1];
        }

        whiteKing = (King)position[4][7];
        blackKing = (King)position[4][0];
    }

    static void show(Graphics g, ImageObserver imageObserver) {
        //draw board
        g.setColor(new Color(160, 100, 40));
        g.fillRect(0, 0, 400, 400);
        g.setColor(new Color(255, 230, 205));
        for (int i = 0; i < 32; i++) {
            g.fillRect(100*(i%4) + 50*((i/4)%2), 50*(i/4), 50, 50);
        }
        
        //show selected piece
        if (selectedPiece != null && !dragged) {
            g.setColor(Color.yellow);
            g.fillRect(selectedPiece.x*50, selectedPiece.y*50, 50, 50);
        }
        
        //highlights red if checked
        g.setColor(Color.red);
        if (inCheck(1)) {
            g.fillOval(whiteKing.x*50, whiteKing.y*50, 50, 50);
        }
        if (inCheck(0)) {
            g.fillOval(blackKing.x*50, blackKing.y*50, 50, 50);
        }

        //shows move arrow for the last move played
        ((Graphics2D)g).setStroke(new BasicStroke(5));
        g.drawLine(previousPosition[0]*50+25, previousPosition[1]*50+25, newPosition[0]*50+25, newPosition[1]*50+25);

        //display pieces
        for (Piece whitePiece : whitePieces) {
            if (!dragged || whitePiece != selectedPiece) whitePiece.show(g, imageObserver);
        }
        for (Piece blackPiece : blackPieces) {
            if (!dragged || blackPiece != selectedPiece) blackPiece.show(g, imageObserver);
        }
        if (selectedPiece != null && dragged) selectedPiece.show(dragX, dragY, g, imageObserver);
        
        //side labels
        g.setColor(Color.black);
        g.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
        for (int i = 1; i <= 8; i++) {
            g.drawString(Integer.toString(i), 410, 425-i*50);
            g.drawString(String.valueOf((char)(i + 64)), i*50-30, 420);
        }
    }

    static Piece pieceAt(int x, int y) {//returns piece found at coordinates
        return position[x][y];
    }
    static boolean emptyAt(int x, int y) {//determines if square at coordinates is empty or not
        return (pieceAt(x, y) == null);
    }
    static int playerAt(int x, int y) {//returns the player whose piece is at coordinates, or 0 if neither
        if (emptyAt(x, y)) return 0;
        else return pieceAt(x, y).player;
    }

    static boolean squareAttacked(int player, int x, int y) {//checks if (x,y) is attacked by player
        Piece[] pieces = (player == 1) ? whitePieces : blackPieces;
        for (Piece piece : pieces) {
            if (!piece.captured && piece.pseudoLegalMove(x, y)) return true;
        }
        return false;
    }
    static boolean inCheck(int player) {//checks if player is in check
        if (player == 1) return squareAttacked(0, whiteKing.x, whiteKing.y);
        else return squareAttacked(1, blackKing.x, blackKing.y);
    }
    static boolean safeMove(int newX, int newY) {//checks if a pseudo-legal move will leave player in check
        Piece targetSquare = pieceAt(newX, newY);
        int originalX = selectedPiece.x;
        int originalY = selectedPiece.y;

        if (targetSquare != null) targetSquare.captured = true;
        position[newX][newY] = selectedPiece;
        position[originalX][originalY] = null;
        selectedPiece.x = newX;
        selectedPiece.y = newY;
        
        boolean output = !inCheck(turn);

        if (targetSquare != null) targetSquare.captured = false;
        position[newX][newY] = targetSquare;
        position[originalX][originalY] = selectedPiece;
        selectedPiece.x = originalX;
        selectedPiece.y = originalY;

        return output;
    }
    static boolean safeMove(Piece piece, int newX, int newY) {
        Piece targetSquare = pieceAt(newX, newY);
        int originalX = piece.x;
        int originalY = piece.y;
        int player = piece.player;

        if (targetSquare != null) targetSquare.captured = true;
        position[newX][newY] = piece;
        position[originalX][originalY] = null;
        piece.x = newX;
        piece.y = newY;
        
        boolean output = !inCheck(player);

        if (targetSquare != null) targetSquare.captured = false;
        position[newX][newY] = targetSquare;
        position[originalX][originalY] = piece;
        piece.x = originalX;
        piece.y = originalY;

        return output;
    }

    static void selectPiece(int x, int y) {
        if (!emptyAt(x, y) && pieceAt(x, y).player == turn)
            selectedPiece = pieceAt(x, y);
    }
    static void movePiece(int newX, int newY) {
        clearEnPassantable(turn);//en passant opportunity only lasts one turn, resets after
        if (selectedPiece instanceof King && ((King)selectedPiece).castle(newX, newY) != 0) {//castling
            castle(selectedPiece.player, ((King)selectedPiece).castle(newX, newY));
            turn = -turn;
        }
        else if (selectedPiece instanceof Pawn && ((Pawn)selectedPiece).canCaptureEnPassant(newX, newY)) {//capturing en passant
            captureEnPassant((Pawn)selectedPiece, newX, newY);
            turn = -turn;
        }
        else if (selectedPiece.legalMove(newX, newY)) {//every other move besides castling or capturing en passant
            //for move arrow
            previousPosition[0] = selectedPiece.x; previousPosition[1] = selectedPiece.y;
            newPosition[0] = newX; newPosition[1] = newY;

            if (!emptyAt(newX, newY)) pieceAt(newX, newY).captured = true;//captures piece

            if (selectedPiece instanceof King) ((King)selectedPiece).castleLegal = false;//castling becomes illegal if king or rook moves
            if (selectedPiece instanceof Rook) ((Rook)selectedPiece).castleLegal = false;
            if (selectedPiece instanceof Pawn && Math.abs(selectedPiece.y-newY) == 2)//pawn can be captured en passant by opponent the turn after it moves up 2 squares
                ((Pawn)selectedPiece).enPassantable = true;

            //actually changing the location of the piece
            position[newX][newY] = selectedPiece;
            position[selectedPiece.x][selectedPiece.y] = null;
            selectedPiece.x = newX;
            selectedPiece.y = newY;
            
            if (selectedPiece instanceof Pawn && (selectedPiece.y == 0 || selectedPiece.y == 7)) promote((Pawn)selectedPiece);//pawn promotes when it reaches end of board

            selectedPiece.numberOfMoves++;//keeps track of number of moves played on a piece

            turn = -turn;
        }
        selectedPiece = null;
    }
    static void castle(int player, int kingside) {
        int row = (player == 1) ? 7 : 0;
        King king = (player == 1) ? whiteKing : blackKing;
        if (kingside == 1) {//kingside = 1 => kingside castle
            position[5][row] = position[7][row];
            position[7][row] = null;
            position[6][row] = king;
            position[4][row] = null;

            pieceAt(5, row).x = 5;
            king.x = 6;
            ((Rook)pieceAt(5, row)).castleLegal = false;
            king.castleLegal = false;
        }
        else if (kingside == -1) {//kingside = -1 => queenside castle
            position[3][row] = position[0][row];
            position[0][row] = null;
            position[2][row] = king;
            position[4][row] = null;

            pieceAt(3, row).x = 3;
            king.x = 2;
            ((Rook)pieceAt(3, row)).castleLegal = false;
            king.castleLegal = false;
        }
        king.castled = true;
    }
    static void promote(Pawn pawn) {//promotes pawn to queen
        Queen promotedPawn = new Queen(pawn.player, pawn.x, pawn.y);
        Piece[] pieces = (pawn.player == 1) ? whitePieces : blackPieces;
        for (int i = 8; i < 16; i++) {
            if (pieces[i] == pawn) pieces[i] = promotedPawn;
        }
        position[pawn.x][pawn.y] = promotedPawn;
    }
    static void captureEnPassant(Pawn pawn, int newX, int newY) {//capturing en passant
        int originalX = pawn.x;
        int originalY = pawn.y;

        Pawn capturedPawn = (Pawn)pieceAt(newX, newY + pawn.player);

        capturedPawn.captured = true;
        pawn.x = capturedPawn.x;
        pawn.y -= pawn.player;
        
        position[capturedPawn.x][capturedPawn.y] = null;
        position[pawn.x][pawn.y] = pawn;
        position[originalX][originalY] = null;
    }
    static void clearEnPassantable(int player) {//marks all of player's pawns as not en passant-
        Piece[] pieces = (player == 1) ? whitePieces : blackPieces;
        for (int i = 8; i < 16; i++) {
            if (pieces[i] instanceof Pawn) ((Pawn)pieces[i]).enPassantable = false;
        }
    }
}