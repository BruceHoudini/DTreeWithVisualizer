import jdk.nashorn.internal.runtime.ParserException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Weston Ford on 2/12/2016.
 */
public class Parser {
    private LexicalAnalyzer lex;
    private Token tok;
    private Node root;
    private List<Node> queue = new ArrayList<>();
    private Stack<Node> stack = new Stack<>();

    public Parser(File source){
        lex = new LexicalAnalyzer(source);
    }
    public void parse() throws ParserException {
        tok = lex.pop();
        if(tok.getTokType() == TokenType.OPENB){
            if((tok = lex.pop()).getTokType() == TokenType.ROOT){
                if((tok = lex.pop()).getTokType() == TokenType.CLOSEB){
                    root = new Node("Root", NodeType.ROOT);
                    root.setDepth(1);
                    buildTree(root);
                }
                else
                    throw new ParserException("Expected > after root");
            }
            else
                throw new ParserException("Expected keyword: root");
        }
        else
            throw new ParserException("Expected <");
    }

    //Hold on to your butts
    public void buildTree(Node parent) throws ParserException{
        tok = lex.pop();
        String content;
        NodeType type;
        Node child;
        if (tok.getTokType() == TokenType.OPENB) {
            if ((tok = lex.pop()).getTokType() == TokenType.NODE) {
                if ((tok = lex.pop()).getTokType() == TokenType.SPACE) {
                    if ((tok = lex.pop()).getTokType() == TokenType.BEHAVIOR) {
                        if ((tok = lex.pop()).getTokType() == TokenType.EQUALS) {
                            if ((tok = lex.pop()).getTokType() == TokenType.QUOTE) {
                                if ((lex.peek()).getTokType() == TokenType.QUOTE) {
                                    lex.pop();
                                    if ((tok = lex.pop()).getTokType() == TokenType.RESPONSE) {
                                        if ((tok = lex.pop()).getTokType() == TokenType.EQUALS) {
                                            if ((tok = lex.pop()).getTokType() == TokenType.QUOTE) {
                                                if (lex.peek().getTokType() == TokenType.TEXT) {
                                                    content = buildString();
                                                    type = NodeType.RESPONSE;
                                                    if (lex.pop().getTokType() == TokenType.SLASH) {
                                                        child = new Node(content, type);
                                                        parent.addChild(child);
                                                        lex.pop();
                                                        buildTree(parent);
                                                        return;
                                                    } else
                                                        throw new ParserException("Expected / after response before >");
                                                } else
                                                    throw new ParserException("No behavior or response specified");
                                            } else
                                                throw new ParserException("Expected \" after =");
                                        } else
                                            throw new ParserException("Expected = after Response");
                                    } else
                                        throw new ParserException("Expected keyword: Response");
                                } else if ((lex.peek()).getTokType() == TokenType.TEXT) {
                                    content = buildString();
                                    type = NodeType.BEHAVIOR;
                                    if ((tok = lex.pop()).getTokType() == TokenType.CLOSEB) {
                                        child = new Node(content, type);
                                        parent.addChild(child);
                                        buildTree(child);
                                    } else {
                                        while (tok.getTokType() != TokenType.CLOSEB)
                                            tok = lex.pop();
                                        child = new Node(content, type);
                                        parent.addChild(child);
                                        buildTree(child);
                                    }
                                } else
                                    throw new ParserException("Expected \" or text");
                            } else
                                throw new ParserException("Expected \" after =");
                        } else
                            throw new ParserException("Expected = after behavior");
                    } else if (tok.getTokType() == TokenType.RESPONSE) {
                        if ((tok = lex.pop()).getTokType() == TokenType.EQUALS) {
                            if ((tok = lex.pop()).getTokType() == TokenType.QUOTE) {
                                if ((lex.peek()).getTokType() == TokenType.QUOTE) {
                                    lex.pop();
                                    if ((tok = lex.pop()).getTokType() == TokenType.BEHAVIOR) {
                                        if ((tok = lex.pop()).getTokType() == TokenType.EQUALS) {
                                            if ((tok = lex.pop()).getTokType() == TokenType.QUOTE) {
                                                if (lex.peek().getTokType() == TokenType.TEXT) {
                                                    content = buildString();
                                                    type = NodeType.BEHAVIOR;
                                                    if (lex.pop().getTokType() == TokenType.CLOSEB) {
                                                        child = new Node(content, type);
                                                        parent.addChild(child);
                                                        lex.pop();
                                                        buildTree(child);
                                                    } else
                                                        throw new ParserException("Expected / after response before >");
                                                } else
                                                    throw new ParserException("No behavior or response specified");
                                            } else
                                                throw new ParserException("Expected \" after =");
                                        } else
                                            throw new ParserException("Expected = after Response");
                                    } else
                                        throw new ParserException("Expected keyword: Response");
                                } else if ((lex.peek()).getTokType() == TokenType.TEXT) {
                                    content = buildString();
                                    type = NodeType.RESPONSE;
                                    if ((tok = lex.pop()).getTokType() == TokenType.SLASH) {
                                        child = new Node(content, type);
                                        parent.addChild(child);
                                        lex.pop();
                                        buildTree(parent);
                                        return;
                                    } else {
                                        while (tok.getTokType() != TokenType.SLASH)
                                            tok = lex.pop();
                                        child = new Node(content, type);
                                        parent.addChild(child);
                                        lex.pop();
                                        buildTree(parent);
                                        return;
                                    }
                                } else
                                    throw new ParserException("Expected \" or text");
                            } else
                                throw new ParserException("Expected \" after =");
                        } else
                            throw new ParserException("Expected = after response");
                    } else
                        throw new ParserException("Expected keyword: Behavior or Response");
                } else
                    throw new ParserException("Expected space after node");
            } else if (tok.getTokType() == TokenType.SLASH) {
                if ((tok = lex.pop()).getTokType() == TokenType.NODE) {
                    if ((tok = lex.pop()).getTokType() == TokenType.CLOSEB) {
                        //buildTree(parent);
                        return;
                    } else
                        throw new ParserException("Expected > after /node");
                } else if ((tok.getTokType() == TokenType.ROOT)) {
                    if ((tok = lex.pop()).getTokType() == TokenType.CLOSEB) {
                        return;
                    } else
                        throw new ParserException("Expect > after /root");
                } else
                    throw new ParserException("Expected keyword: node or root after /");
            } else
                throw new ParserException("Expected node or /node");
        } else
            throw new ParserException("Expected <");
        buildTree(parent);


    }
    public String buildString(){
        String result;
        result = lex.pop().getLexeme();

        while(lex.peek().getTokType() != TokenType.QUOTE) {
            result += (lex.pop().getLexeme());
        }
        lex.pop();

        return result;
    }
    public Node getRoot(){
        return root;
    }

    public void printTree(Node parent){
        int counter = parent.numChildren();
        for (int i = 0; i < counter; i++){
            System.out.println("Parent: " + parent.getContent());
            System.out.println("Child: " + parent.getChild(i).getContent());
            printTree(parent.getChild(i));
        }
    }
    public void breadthFirstPrintTree(Node parent){
        int counter = parent.numChildren();
        for(int i = 0; i < counter; i++){
            System.out.println(parent.getContent() + "-->" + parent.getChild(i).getContent());
            queue.add(parent.getChild(i));
        }
        if(!queue.isEmpty())
            breadthFirstPrintTree(queue.remove(0));
    }


    public void BFS(Node parent, String search){
        int counter = parent.numChildren();
        Node tempNode;
        for(int i = 0; i < counter; i++){
            if(search.equals(parent.getContent())) {
                parent.setType(NodeType.CHOSENB);
                tempNode=parent.getChild((int)(System.currentTimeMillis()%counter));
                while(tempNode.getType() != NodeType.RESPONSE) {
                    tempNode.setType(NodeType.CHOSENB);
                    counter = tempNode.numChildren();
                    tempNode = tempNode.getChild((int)(System.currentTimeMillis() % counter));
                }
                tempNode.setType(NodeType.CHOSENR);
                System.out.println("BFS Response = " + tempNode.getContent());
                return;
            }

            queue.add(parent.getChild(i));
        }
        if(!queue.isEmpty())
            BFS(queue.remove(0), search);

    }
    public void DFS(Node parent, String search){
        int counter = parent.numChildren();
        Node tempNode;
        for (int i = 0; i < counter; i++){
            if(search.equals(parent.getContent())) {
                parent.setType(NodeType.CHOSENB);
                tempNode=parent.getChild((int)(System.currentTimeMillis()%counter));
                while(tempNode.getType() != NodeType.RESPONSE) {
                    tempNode.setType(NodeType.CHOSENB);
                    counter = tempNode.numChildren();
                    tempNode = tempNode.getChild((int)(System.currentTimeMillis()%counter));
                }

                tempNode.setType(NodeType.CHOSENR);

                System.out.println("DFS Response = " + tempNode.getContent());
                return;
            }
            DFS(parent.getChild(i), search);
        }
    }
}
