package jp.skd.lilca.mhf.main.kafus;

public class ListLayout {
    private CharSequence textLeft   = null;
    private CharSequence textTop    = null;
    private CharSequence textBottom = null;
    private CharSequence textRight  = null;
    
    private String src = "";

    public String getSrc(){
    	return this.src;
    }
    public void setSrc(String s){
    	this.src = s;
    	return;
    }
    public CharSequence getTextLeft() { 
        return textLeft; 
    } 
    public void setTextLeft(CharSequence textLeft) { 
        this.textLeft = textLeft; 
    } 
    public CharSequence getTextTop() { 
        return textTop; 
    } 
    public void setTextTop(CharSequence textTop) { 
        this.textTop = textTop; 
    } 
    public CharSequence getTextBottom() { 
        return textBottom; 
    } 
    public void setTextBottom(CharSequence textBottom) { 
        this.textBottom = textBottom; 
    } 
    public CharSequence getTextRight() { 
        return textRight; 
    } 
    public void setTextRight(CharSequence textRight) { 
        this.textRight = textRight; 
    }
}
