package dependencytree;

import java.util.ArrayList;

/**
 *
 * @author Aashima
 */
public class Cell {
    private double content;
    private String contentFormula;//cell content. May be a constant or may contain formula
    private ArrayList<Cell> causedBy = new ArrayList<>();
    private ArrayList<Cell> effects = new ArrayList<>();
    
    public void setContent(double c){
        content = c;
    }
    
    public void setContentFormula(String s){
        contentFormula = s;
    }
    
    public void setCausedBy(Cell cb){
        this.causedBy.add(cb);
    }
    
    public void setEffects(Cell e){
        this.effects.add(e);
    }
    
    public double getContent(){
        return content;
    }
    
    public String getContentFormula(){
        return contentFormula;
    }
    
    public ArrayList<Cell> getCausedBy(){
        return causedBy;
    }
    
    public ArrayList<Cell> getEffects(){
        return effects;
    }
}