package jp.skd.lilca.mhf.main.listadapters.c_loud_bbs;

import jp.skd.lilca.mhf.datastruct.BBSItem;

public class BBSItemListLayout {
    private CharSequence textLeft   = null;
    private CharSequence textTop    = null;
    private CharSequence textMiddle0= null;
    private CharSequence textMiddle1= null;
    private CharSequence textMiddle2= null;
    private CharSequence textMiddle3= null;
    private CharSequence textMiddle4= null;
    private CharSequence textBottom = null;
    private CharSequence textRight  = null;
    private BBSItem bbsitem			= null;

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
    public CharSequence getTextMiddle0() { 
        return textMiddle0; 
    }
    public void setTextMiddle0(CharSequence textMiddle) { 
        this.textMiddle0 = textMiddle; 
    } 
    public CharSequence getTextMiddle1() { 
        return textMiddle1; 
    }
    public void setTextMiddle1(CharSequence textMiddle) { 
        this.textMiddle1 = textMiddle; 
    } 
    public CharSequence getTextMiddle2() { 
        return textMiddle2; 
    }
    public void setTextMiddle2(CharSequence textMiddle) { 
        this.textMiddle2 = textMiddle; 
    } 
    public CharSequence getTextMiddle3() { 
        return textMiddle3; 
    }
    public void setTextMiddle3(CharSequence textMiddle) { 
        this.textMiddle3 = textMiddle; 
    } 
    public CharSequence getTextMiddle4() { 
        return textMiddle4; 
    }
    public void setTextMiddle4(CharSequence textMiddle) { 
        this.textMiddle4 = textMiddle; 
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
    public BBSItem getBBSItem(){
    	return this.bbsitem;
    }
    public void setBBSItem(BBSItem item){
    	this.bbsitem	= item;
    	return;
    }
}
