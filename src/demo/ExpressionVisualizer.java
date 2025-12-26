package demo;
/*
Demo class for the math package. This demo shows a window with 3 sections:
- top section: text editable math expression
- center section: graph showing the expression tree
- bottom section: the result of the math expression or syntax error
*/
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import math.*;

class VisualNode {
    final Node node;
    final Rectangle2D bounds;
    VisualNode(Node node, Rectangle2D bounds) {
        this.node = node;
        this.bounds = bounds;
    }
}

public class ExpressionVisualizer extends JFrame {
    private static final int NODE_SIZE = 35;
    private static final int ROW_HEIGHT = 70;

    private static final Stroke HOVER_STROKE = new BasicStroke(5);
    private static final Stroke LEAF_STROKE = new BasicStroke(2);
    private static final Stroke NORMAL_STROKE = new BasicStroke(1);

    private Node root;
    private Node hoveredNode = null;
    private final List<VisualNode> visualNodes = new ArrayList<>();
    
    private final JTextField topBar;
    private final JLabel helpLabel;
    private final JPanel renderPanel;

    public ExpressionVisualizer(String initialExpression) {
        super("Expression Visualizer");
        
        topBar = new JTextField(initialExpression);
        topBar.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
        helpLabel = new JLabel("Value = ...");
        JPanel helpPanel = new JPanel();
        helpPanel.add(helpLabel);
        helpPanel.setBackground(new Color(220, 220, 220));

        renderPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
				if (root != null)
					renderTree((Graphics2D) g);
            }
        };
        renderPanel.setBackground(new Color(245, 245, 245));

        setLayout(new BorderLayout());
        add(topBar, BorderLayout.NORTH);
        add(renderPanel, BorderLayout.CENTER);
        add(helpPanel, BorderLayout.SOUTH);

        setupInteractions();
        setupParsingLogic(helpPanel);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void setupParsingLogic(JPanel helpPanel) {
        ExpressionParser ep = new ExpressionParser();
        
        DocumentListener dl = new DocumentListener() {
            public void update() {
                try {
                    root = ep.parseExpression(topBar.getText());
                    helpPanel.setBackground(new Color(200, 255, 200));
                    helpLabel.setText("Value = " + ep.evaluate(root));
                    renderPanel.repaint();
                } catch (Exception ex) {
                    helpPanel.setBackground(new Color(255, 200, 200));
                    helpLabel.setText("Error: " + ex.getMessage());
                }
            }
            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }
        };
        
        topBar.getDocument().addDocumentListener(dl);

		SwingUtilities.invokeLater(() -> dl.insertUpdate(null));
    }

    private void setupInteractions() {
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Node found = findNodeAt(e.getPoint());
                if (found != hoveredNode) {
                    hoveredNode = found;
                    renderPanel.repaint();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Node target = findNodeAt(e.getPoint());
                if (target != null && SwingUtilities.isLeftMouseButton(e)) {
                    editNode(target);
                }
            }
        };
        renderPanel.addMouseListener(mouseHandler);
        renderPanel.addMouseMotionListener(mouseHandler);
    }

    private void editNode(Node t) {
        if (t instanceof NodeDouble) {
            String v = JOptionPane.showInputDialog(this, "Edit value:", t.evaluate());
            if (v != null) {
                try {
                    ((NodeDouble) t).set(Double.parseDouble(v)); 
                    sync(); 
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (t instanceof UnaryNodeIdentifier) {
            String currentOp = t.getNodeAsString();
			String[] functions = UnaryNodeIdentifier.functionSet.toArray(new String[0]);
            String choice = (String) JOptionPane.showInputDialog(this, 
                    "Change function:", "Edit function",
                    JOptionPane.QUESTION_MESSAGE, null, functions, currentOp);
            
            if (choice != null && !choice.equals(currentOp)) {
                ((UnaryNodeIdentifier) t).set(choice);
                sync();
            }
        }
    }

    private void sync() {
        if (root != null) {
			StringBuilder sb = new StringBuilder();
			root.visit(sb);
			topBar.setText(sb.toString());
		}
    }

    private Node findNodeAt(Point p) {
        for (int i = visualNodes.size() - 1; i >= 0; i--) {
            VisualNode vn = visualNodes.get(i);
            if (vn.bounds.contains(p)) return vn.node;
        }
        return null;
    }

    private void renderTree(Graphics2D g2) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		visualNodes.clear();
		drawTree(g2, root, renderPanel.getWidth() / 2, 50, renderPanel.getWidth() / 4, 0);
    }

    private void drawTree(Graphics2D g2, Node node, int x, int y, int xOffset, int depth) {
        Node left = null, center = null, right = null;
		Color color;
        
        if (node instanceof UnaryNode)  {
			center = ((UnaryNode) node).getChild();
			color = Color.CYAN;
        } else if (node instanceof UnaryNodeIdentifier) {
			center = ((UnaryNodeIdentifier) node).getChild();
			color = Color.GREEN;
        } else if (node instanceof BinaryNode) {
            left = ((BinaryNode) node).getLeft();
            right = ((BinaryNode) node).getRight();
			color = Color.YELLOW;
        } else if (node instanceof TernaryNode) {
            TernaryNode tn = (TernaryNode) node;
            left = tn.getLeft(); center = tn.getCenter(); right = tn.getRight();
			color = Color.PINK;
        } else color = Color.ORANGE;

        int nextY = y + ROW_HEIGHT;
        int nextOffset = Math.max(xOffset / 2, 15);
        g2.setColor(Color.LIGHT_GRAY);

        if (left != null) { g2.drawLine(x, y, x - xOffset, nextY); drawTree(g2, left, x - xOffset, nextY, nextOffset, depth + 1); }
        if (center != null) { g2.drawLine(x, y, x, nextY); drawTree(g2, center, x, nextY, nextOffset, depth + 1); }
        if (right != null) { g2.drawLine(x, y, x + xOffset, nextY); drawTree(g2, right, x + xOffset, nextY, nextOffset, depth + 1); }

        Rectangle2D bounds = new Rectangle(x - NODE_SIZE / 2, y - NODE_SIZE / 2, NODE_SIZE, NODE_SIZE);
        visualNodes.add(new VisualNode(node, bounds));

        g2.setColor(color);
        g2.fill(bounds);

        boolean isHovered = (node == hoveredNode);
        g2.setColor(isHovered ? Color.YELLOW : Color.DARK_GRAY);
        g2.setStroke(isHovered ? HOVER_STROKE : (left == null && right == null && center == null ? LEAF_STROKE : NORMAL_STROKE));
        g2.draw(bounds);

        g2.setColor(Color.BLACK);
        String val = node.getNodeAsString();
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(val, x - fm.stringWidth(val) / 2, y + fm.getAscent() / 2 - 2);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ExpressionVisualizer("(sin(PI/4)+cos(PI/4))*sqrt(exp((log(2)*tan(PI/4))))").setVisible(true);
        });
    }
}