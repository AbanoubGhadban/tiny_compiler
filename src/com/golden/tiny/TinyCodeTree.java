package com.golden.tiny;

import com.golden.tiny.exceptions.CompilerException;
import com.golden.tiny.statement_parts.IComposedStatementPart;
import com.golden.tiny.statement_parts.IStatementPart;
import com.golden.tiny.statement_parts.Statement;
import com.golden.tiny.tokens.Comment;
import com.golden.tiny.tokens.Token;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

public class TinyCodeTree extends JFrame
{
    private JTree tree;
    public TinyCodeTree() throws Exception {
        //create the root node
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Tiny Code");

        String filePath = "sample.txt";
        String s = Files.readString(Paths.get(filePath));

        TinyScanner scanner = new TinyScanner(s);
        List<Token> tokens = scanner.scan();

        tokens.removeIf(new Predicate<Token>() {
            @Override
            public boolean test(Token token) {
                return (token instanceof Comment);
            }
        });
        TinyParser parser = new TinyParser(tokens);
        List<Statement> statements = parser.parseTokens();

        for (Statement statement : statements) {
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(statement.getStatementType().toString());
            renderTree(child, statement);
            root.add(child);
        }

        //create the tree by passing in the root node
        tree = new JTree(root);
        JScrollPane scroll = new JScrollPane(tree);
        add(scroll);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Tiny Code Tree");
        this.pack();
        this.setVisible(true);
    }

    void renderTree(DefaultMutableTreeNode parent, IComposedStatementPart statementPart) {
        for (IStatementPart part : statementPart.getParts()) {
            DefaultMutableTreeNode child;
            if (part instanceof IComposedStatementPart) {
                if (part instanceof Statement) {
                    child = new DefaultMutableTreeNode(((Statement) part).getStatementType().toString());
                } else {
                    child = new DefaultMutableTreeNode(part.getClass().getSimpleName());
                }
                renderTree(child, (IComposedStatementPart) part);
            } else if (part instanceof Token) {
                child = new DefaultMutableTreeNode(((Token)part).getToken() + " (" + part.getClass().getSimpleName() + ")");
            } else {
                child = new DefaultMutableTreeNode(part.getClass().getName());
            }
            parent.add(child);
        }
    }

    String sh(String fullName) {
        var arr = fullName.split("\\.");
        return arr[arr.length - 1];
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new TinyCodeTree();
                } catch (CompilerException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
