package jp.skd.lilca.mhf.main.listadapters.main;

import android.view.View.OnClickListener;

public class ListLayout {
    private CharSequence textLeft		= null; 
    private CharSequence textTop		= null; 
    private CharSequence textBottom	= null;
    private CharSequence textRight1	= null;
    private CharSequence textRight2	= null;
    private OnClickListener lsn1	= null;
    private OnClickListener lsn2	= null;
    private int btnid1;
    private int btnid2;
    
    public CharSequence getTextLeft(){
        return textLeft;
    }
    public void setTextLeft(CharSequence textLeft){
        this.textLeft = textLeft;
    }
    public CharSequence getTextTop(){
        return textTop;
    }
    public void setTextTop(CharSequence textTop){
        this.textTop = textTop;
    }
    public CharSequence getTextBottom(){
        return textBottom; 
    }
    public void setTextBottom(CharSequence textBottom){
        this.textBottom = textBottom; 
    }
    public CharSequence getTextRight1(){
        return textRight1;
    }
    public CharSequence getTextRight2(){
        return textRight2;
    }
    public void setTextRight1(CharSequence textRight){ 
        this.textRight1 = textRight; 
    }
    public void setTextRight2(CharSequence textRight){ 
        this.textRight2 = textRight; 
    }
    public OnClickListener getListner1(){
    	return this.lsn1;
    }
    public OnClickListener getListner2(){
    	return this.lsn2;
    }
    public void setListener1(OnClickListener src){ 
        this.lsn1 = src; 
    }
    public void setListener2(OnClickListener src){ 
        this.lsn2 = src; 
    }
    public void setButtonImageId1(int id){
    	this.btnid1 = id;
    }
    public void setButtonImageId2(int id){
    	this.btnid2 = id;
    }
    public int getButtonImageId1(){
    	return this.btnid1;
    }
    public int getButtonImageId2(){
    	return this.btnid2;
    }
}
