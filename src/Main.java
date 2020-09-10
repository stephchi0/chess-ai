package src;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Board.initializeBoard();

        final JFrame frame = new JFrame("Chess");
        final JPanel panel = new JPanel();
        final Game chess = new Game();

        frame.setSize(500, 500);
        frame.setResizable(false);
        frame.setContentPane(panel);

        panel.setLayout(null);
        panel.add(chess);
        panel.addMouseListener(chess);
        panel.addMouseMotionListener(chess);
        chess.setBounds(0, 0, 500, 500);
        
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
/*
TO DO LIST
check for checkmate/stalemate
en passant can still leave u in check
currently the move arrow only works for moves that arent castle or en passant
have ai understand promotion and en passant
have ai prioritize certain squares over others
have ai understand checkmate/stalemate
add en passant to list of legal moves
allow board to be flipped
implement draw by repetition, draw by insufficient material, draw by not capturing for 50 moves
alpha/beta pruning
allow drag and drop piece control
minimax currently may think it can take a piece thats defended at depth 0
increase depth with less pieces
add restart button and end game screen
*/