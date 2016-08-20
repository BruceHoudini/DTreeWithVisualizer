import java.util.ArrayList;

/**
 * Created by Weston Ford on 2/12/2016.
 */
public class Node {
    private ArrayList<Node> children = new ArrayList<>();
    private NodeType type;
    private String content;
    private int depth;
    private double offset = 0;
    private double shift = 0;
    private double childshift = 0;
    private int xloc, yloc;

    public Node(String content, NodeType type){
        this.content = content;
        this.type = type;
    }
    public void addChild(Node child){
        children.add(child);
    }
    public Node getChild(int index){
        return children.get(index);
    }
    public int numChildren(){
        return children.size();
    }
    public String getContent() {
        return content;
    }
    public NodeType getType(){
        return type;
    }
    public void setType(NodeType type){this.type = type;}
    public void setDepth(int d){ depth = d;}
    public int getDepth(){ return depth;}

    public int getYloc() {
        return yloc;
    }

    public void setYloc(int yloc) {
        this.yloc = yloc;
    }

    public int getXloc() {
        return xloc;
    }

    public void setXloc(int xloc) {
        this.xloc = xloc;
    }

    public double getChildshift() {
        return childshift;
    }

    public void setChildshift(double childshift) {
        this.childshift = childshift;
    }

    public double getShift() {
        return shift;
    }

    public void setShift(double shift) {
        this.shift = shift;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }
}
