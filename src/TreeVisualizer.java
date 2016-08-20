import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Weston Ford on 2/12/2016.
 */
public class TreeVisualizer extends JPanel {


    private ArrayList<Node> nodeList = new ArrayList<>();
    private List<Node> queue = new ArrayList<>();
    private int x, y, width, height;
    private int xinit;
    private int minX, maxX;


    public TreeVisualizer(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        xinit = findMiddleX();
        minX = xinit;
        maxX = xinit;

    }
    public void setWidth(int width){
        this.width = width;
    }
    public void setHeight(int height){
        this.height = height;
    }
    public int findMiddleX(){
        return x/2-width;
    }




    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Color greyfill = new Color(28, 28, 28);
        Color belight = new Color(0, 136, 119);
        Color belighter = new Color(81,237,188);
        Color relight = new Color(119,17,221);
        Color relighter = new Color(132,50,255);
        Color chosenr = new Color(253,151,31);
        //Color chosenr = new Color(150, 0, 80);
        //Color chosenr = new Color(253, 151, 31).brighter();
        Color chosenb = new Color(116, 226, 46);
        //Color chosenr = new Color(249,38, 114);
        //Color chosenb = new Color(0, 136, 119);
        //Color chosenr = new Color(119,17,221);



        //Blob of settings copied from a "List of rendering hints frequently used" forum post.
        //http://www.java-gaming.org/index.php?topic=27079.0
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);

        int fontsize = 12;
        Font originalfont = new Font("Courier New", Font.BOLD, fontsize);
        g2.setFont(originalfont);


        //setBackground(new Color(0, 0, 0));

        int xtemp;
        int ytemp;

        g2.setStroke(new BasicStroke(3));



        for (int i = 0; i < nodeList.size(); i++) {
            Node node = nodeList.get(i);
            xtemp = node.getXloc();
            ytemp = node.getYloc();
            g2.setColor(greyfill);


            //System.out.println(node.getContent() + ": x1=" + xtemp + ", y1=" + ytemp + ", x1=" + (xtemp+width) + ", y2=" + (ytemp+height));


            g2.fillRoundRect(xtemp, ytemp, width, height, 10, 10);

            //g2.setColor(new Color(Color.BLACK.getRGB()));
            //g2.setStroke(new BasicStroke(10));

            if(node.getType() == NodeType.BEHAVIOR) {
                g2.setColor(belight);
                g2.drawRoundRect(xtemp, ytemp, width, height, 10, 10);
                g2.setColor(belighter);
            }
            else if(node.getType() == NodeType.ROOT) {
                g2.setColor(belight);
                g2.drawRoundRect(xtemp, ytemp, width, height, 10, 10);
                g2.setColor(belighter);
            }
            else if(node.getType() == NodeType.RESPONSE) {
                g2.setColor(relight);
                g2.drawRoundRect(xtemp, ytemp, width, height, 10, 10);
                g2.setColor(relighter);
            }
            else if(node.getType() == NodeType.CHOSENB){
                //chosenb = chosenb.brighter();
                g2.setColor(chosenb);
                g2.drawRoundRect(xtemp, ytemp, width, height, 10, 10);
            }
            else if(node.getType() == NodeType.CHOSENR){
                //chosenb = chosenb.brighter();
                g2.setColor(chosenr);
                g2.drawRoundRect(xtemp, ytemp, width, height, 10, 10);
            }


            while(g2.getFontMetrics().stringWidth(node.getContent()) > width)
                    g2.setFont(new Font("Courier New", Font.PLAIN, fontsize--));
            g2.drawString(node.getContent(), xtemp+(int)(.5*(width - g2.getFontMetrics().stringWidth(node.getContent()))), ytemp+(int)(.5*height + .5*g2.getFontMetrics().getMaxAscent()));
            g2.setFont(originalfont);


            for (int j = 0; j < node.numChildren(); j++){
                if (node.getChild(j).getType() == NodeType.BEHAVIOR) {
                    g2.setColor(belight);
                }
                else if (node.getChild(j).getType() == NodeType.RESPONSE) {
                    g2.setColor(relight);
                }
                else if (node.getChild(j).getType() == NodeType.CHOSENB){
                    if(node.getType() == NodeType.CHOSENB)
                        g2.setColor(chosenb);
                    else
                        g2.setColor(belight);
                }
                else if (node.getChild(j).getType() == NodeType.CHOSENR){
                    g2.setColor(chosenr);
                }
                g2.drawLine((int)(xtemp + .5*width),(int)(ytemp + 1.1*height),(int)(node.getChild(j).getXloc() + .5*width),(int)(node.getChild(j).getYloc() - .1*height));
            }

        }
    }

    public void generateOffsets(){
        double offset = 0;
        int depth = nodeList.get(0).getDepth();

        for (int i = 0; i < nodeList.size()-1; i++){
            if(nodeList.get(i).getDepth() != depth){
                depth--;
                offset = 0;
            }
            offset = processNode(nodeList.get(i), offset);

        }
        processNode(nodeList.get(nodeList.size()-1), 0);
        Node last = nodeList.get(nodeList.size()-1);
        double oldshift = last.getShift();
        double newshift = oldshift - last.numChildren()-1;
        last.setShift(newshift);

        //if (xinit/width < last.getShift())
         //   width = (int)(xinit/last.getShift());



        setXYVals(last, xinit, newshift);

        /*double ratio;
        if(minX < 0 || maxX > x) {
            ratio = (double) (x) / (double) (Math.abs(minX) + maxX);
            width = (int)((double)width * ratio);
        }*/
        /*if (xinit/width < last.getShift())
            width = (int)(xinit/last.getShift());*/
        int panelAdjustX = (x-maxX - minX);
        int panelAdjustY = ((nodeList.get(0).getDepth()+3)*2*height - y);
        x += panelAdjustX;
        y += panelAdjustY;
        centerAllNodes();
    }

    //Initial values passed should be root, xcurr should be xinit, and poffset should be root.getShift()
    public void setXYVals(Node parent, int xcur, double poffset){

        //System.out.println("Node: " + parent.getContent() + ": poffset="+poffset+": offset="+(parent.getOffset()+parent.getChildshift()+parent.getShift()) + ": childshift=" +parent.getChildshift());
        double difference = (parent.getOffset()+parent.getChildshift()+parent.getShift()) - poffset;

        int newX = (int)(xcur + difference*width);
        int newY = parent.getDepth()*2*height;
        if(newX < minX)
            minX = newX;
        if(newX + width > maxX)
            maxX = newX + width;

        parent.setXloc(newX);
        parent.setYloc(newY);
        int num = parent.numChildren();
        for (int i = 0; i < num; i++){
            setXYVals(parent.getChild(i), parent.getXloc(), parent.getShift());
        }
    }

    public void buildNodeList(Node parent){
        nodeList.add(0, parent);
        int counter = parent.numChildren();
        for(int i = 0; i < counter; i++){
            parent.getChild(i).setDepth(parent.getDepth()+1);
            queue.add(parent.getChild(i));
        }
        if(!queue.isEmpty()) {
            buildNodeList(queue.remove(0));
        }
    }


    public void printList(){
        for(int i = 0; i < nodeList.size(); i++) {
            System.out.println(nodeList.get(i).getContent() + " : " + nodeList.get(i).getDepth() + " : " +nodeList.get(i).getXloc());
        }
    }
    public double getChildSum(Node parent){
        double sum = 0;
        int num = parent.numChildren();

        for (int i = 0; i < num; i++){
            sum += parent.getChild(i).getOffset()+parent.getChild(i).getShift();
        }

        if(num == 1)
            return sum;
        else
            return sum/2;
    }
    public double shiftParent(Node parent){
        int num = parent.numChildren();
        if (num > 1)
            return num-1;
        else
            return num;
    }
    public double processNode(Node parent, double offset){
        double compensator = 0;
        if(parent.getType() == NodeType.RESPONSE)
            return offset;
        if (parent.numChildren() > 1)
            compensator = 1.5;

        parent.setOffset(offset);
        //System.out.println("Offset of " + parent.getContent() + " = " + offset);
        parent.setShift(shiftParent(parent)+getChildSum(parent));
        shiftChild(parent);
        return offset + parent.getShift() + compensator;
    }

    public void shiftChild(Node parent){
        int num = parent.numChildren();
        if(num == 1)
            parent.getChild(0).setChildshift(1);
        else
            for (int i = 0; i < num; i++)
                parent.getChild(i).setChildshift(2*(num-1-i));
    }

    public void centerAllNodes(){
        int adjustvalue = ((x - maxX) - minX)/2;
        for (int i = 0; i < nodeList.size(); i++){
            nodeList.get(i).setXloc(nodeList.get(i).getXloc() + adjustvalue);
        }
        maxX+=adjustvalue;
        minX+=adjustvalue;
    }
    public int getPanelWidth(){
        return x;
    }
    public int getPanelHeight(){
        return y;
    }
}
