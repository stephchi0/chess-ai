package src;
import java.util.ArrayList;
public class AI { 
    static int player = -1;//1 is white, -1 is black

    static double positionEvaluation(Piece[] whitePieces, Piece[] blackPieces) {
        double output = 0;

        Piece[][] allPieces = {whitePieces, blackPieces};
        for (Piece[] pieces : allPieces) {
            for (Piece piece : pieces) {
                if (!piece.captured) {
                    int p = piece.player;
                    int x = piece.x;
                    int y = piece.y;

                    output += piece.value*p;
                    if ((piece instanceof Knight || piece instanceof Bishop) && piece.numberOfMoves == 0) output -= 0.25*p;//want to develop pieces
                    if (piece instanceof King && ((King)piece).castled) output += 0.7*p;//castled king is safer
                    if (piece instanceof King && ((King)piece).castleLegal) output += 0.2*p;//want to reach castle
                    if (piece instanceof Knight && (x == 0 || x == 7)) output -= 0.15*p;//knights at edge of board are ineffective
                    if (piece instanceof Knight && (y == 0 || y == 7)) output -= 0.25*p;
                    if ((piece instanceof Pawn || piece instanceof Knight || piece instanceof Bishop) && (x == 2 || x == 5) && (y == 2 || y == 5)) output += 0.15*p;//controlling center is good
                    if ((piece instanceof Pawn || piece instanceof Knight || piece instanceof Bishop) && (x == 3 || x == 4) && (y == 3 || y == 4)) output += 0.25*p;
                }
            }
        }
        return output;
    }

    static Piece pieceToMove;
    static int[] coordinateToMoveTo;
    static int depth = 5;
    static double minimax(Piece[][] position, Piece[] whitePieces, Piece[] blackPieces, int currentDepth, int player, double alpha, double beta) {
        if (currentDepth == 0) return positionEvaluation(whitePieces, blackPieces);//ends recursion once max depth is reached

        Piece[] playerPieces = (player == 1) ? whitePieces : blackPieces;
        double bestEval = -9999*player;//initialized as worst possible evaluation for the turn player, aka checkmated
        boolean moveFound = false;

        for (int i = 15; i >= 0; i--) {
            Piece selectedPiece = playerPieces[i];
            if (!selectedPiece.captured) {
                int originalX = selectedPiece.x;
                int originalY = selectedPiece.y;

                ArrayList<int[]> legalSquares = selectedPiece.getLegalMoves();
                
                for (int[] coordinates : legalSquares) {
                    int targetX = coordinates[0];
                    int targetY = coordinates[1];
                    Piece targetSquare = position[targetX][targetY];

                    //guarantees one of the legal moves will be played by picking the first legal move
                    if (!moveFound && currentDepth == depth) {
                        moveFound = true;
                        pieceToMove = selectedPiece;
                        coordinateToMoveTo = coordinates;
                    }

                    //change position based on move that is being looked at
                    boolean promoted = false;
                    boolean castled = false;
                    if (selectedPiece instanceof King && Math.abs(targetX - originalX) > 1) {//if castling
                        boolean kingside = (targetX > originalX);
                        Rook rook = (kingside) ? (Rook)Board.pieceAt(7, originalY) : (Rook)Board.pieceAt(0, originalY);
                        int rookOriginalX = rook.x;
                        int rookNewX = (kingside) ? 5 : 3;
                        int kingNewX = (kingside) ? 6 : 2;

                        position[rookNewX][originalY] = rook;
                        position[rookOriginalX][originalY] = null;
                        position[kingNewX][originalY] = selectedPiece;
                        position[4][originalY] = null;

                        rook.x = rookNewX;
                        selectedPiece.x = kingNewX;
                        rook.castleLegal = false;
                        ((King)selectedPiece).castleLegal = false;

                        castled = true;
                        ((King)selectedPiece).castled = true;
                    }
                    else {
                        if (targetSquare != null) targetSquare.captured = true;
                        position[targetX][targetY] = selectedPiece;
                        position[originalX][originalY] = null;
                        selectedPiece.x = targetX;
                        selectedPiece.y = targetY;
                        selectedPiece.numberOfMoves++;
                        //handles pawn promotion
                        if (selectedPiece instanceof Pawn && (targetY == 0 || targetY == 7)) {
                            Queen promotedPawn = new Queen(player, targetX, targetY, false);
                            playerPieces[i] = promotedPawn;
                            position[targetX][targetY] = promotedPawn;
                            promoted = true;
                        }
                    }
                    
                    //evaluate the position recursively
                    double eval = minimax(position, whitePieces, blackPieces, currentDepth - 1, -player, alpha, beta);
                    if (player == 1 && eval > bestEval || player == -1 && eval < bestEval) {
                        moveFound = true;
                        bestEval = eval;
                        if (currentDepth == depth) {
                            pieceToMove = selectedPiece;
                            coordinateToMoveTo = coordinates;
                        }
                    }

                    //reverses change in position after evaluation
                    if (castled) {
                        boolean kingside = (targetX > originalX);
                        Rook rook = (kingside) ? (Rook)Board.pieceAt(5, originalY) : (Rook)Board.pieceAt(3, originalY);
                        int rookOriginalX = rook.x;
                        int rookNewX = (kingside) ? 7 : 0;
                        int kingOriginalX = selectedPiece.x;

                        position[rookNewX][originalY] = rook;
                        position[rookOriginalX][originalY] = null;
                        position[4][originalY] = selectedPiece;
                        position[kingOriginalX][originalY] = null;

                        rook.x = rookNewX;
                        selectedPiece.x = 4;
                        rook.castleLegal = true;
                        ((King)selectedPiece).castleLegal = true;

                        ((King)selectedPiece).castled = false;
                    }
                    else {
                        if (targetSquare != null) targetSquare.captured = false;
                        position[targetX][targetY] = targetSquare;
                        position[originalX][originalY] = selectedPiece;
                        selectedPiece.x = originalX;
                        selectedPiece.y = originalY;
                        selectedPiece.numberOfMoves--;
                        if (promoted) playerPieces[i] = selectedPiece;//undo promotion
                    }

                    //alpha beta pruning
                    if (player == 1) alpha = Math.max(alpha, bestEval);
                    else beta = Math.min(beta, bestEval); 
                    if (beta <= alpha) {
                        i = -1;
                        break;
                    }
                }
            }
        }
        if (!moveFound && !Board.inCheck(player)) return 0;//stalemate
        return bestEval;
    }
}