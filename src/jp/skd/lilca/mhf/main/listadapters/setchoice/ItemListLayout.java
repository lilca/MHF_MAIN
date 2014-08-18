package jp.skd.lilca.mhf.main.listadapters.setchoice;

public class ItemListLayout {
    private CharSequence textLeft   = null; 
    private CharSequence textTop    = null; 
    private CharSequence textBottom = null; 
    private CharSequence textRight  = null; 

    private String src		= "";
    private String srcS1	= "";
    private String srcS2	= "";
    private String srcS3	= "";

    public String getSrc(){
    	return this.src;
    }
    public void setSrc(String s){
    	this.src = s;
    	return;
    }
    public String getSrcS1(){
    	return this.srcS1;
    }
    public void setSrcS1(String s){
    	this.srcS1 = s;
    	return;
    }
    public String getSrcS2(){
    	return this.srcS2;
    }
    public void setSrcS2(String s){
    	this.srcS2 = s;
    	return;
    }
    public String getSrcS3(){
    	return this.srcS3;
    }
    public void setSrcS3(String s){
    	this.srcS3 = s;
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
