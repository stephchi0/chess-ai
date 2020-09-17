package src;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Board.initializeBoard();

        final JFrame frame = new JFrame("Chess");
        final JPanel panel = new JPanel();
        final Game chess = new Game();

        frame.setSize(525, 475);
        frame.setResizable(false);
        frame.setContentPane(panel);

        panel.setLayout(null);
        panel.add(chess);
        panel.addMouseListener(chess);
        panel.addMouseMotionListener(chess);
        chess.setBounds(0, 0, 525, 475);
        
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
/*
TO DO LIST
check for checkmate/stalemate
en passant can still leave you in check**
have ai understand en passant
have ai understand checkmate/stalemate
add en passant to list of legal moves
allow board to be flipped
implement draw by repetition, draw by insufficient material, draw by not capturing for 50 moves**
minimax currently may think it can take a piece thats defended at depth 0
add restart button and end game screen**
maybe add number of squares a piece controls to its evaluation
*/