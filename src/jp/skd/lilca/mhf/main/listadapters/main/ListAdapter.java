package jp.skd.lilca.mhf.main.listadapters.main;

import java.util.ArrayList;

import jp.skd.lilca.mhf.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
	
	private LayoutInflater layoutInflater = null;
    private ArrayList<ListLayout> listLayout = null;
    
    public ListAdapter(Context context, ArrayList<ListLayout> lstly){
    	//LayoutInflaterを取得 
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listLayout = lstly;
        return;
    }
	@Override
	public int getCount(){
		return listLayout.size();
	}

	@Override
	public Object getItem(int position) {
		return listLayout.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View vw, ViewGroup parent){
		if(vw == null){
			//xmlから1行分のレイアウトを生成 
            vw = layoutInflater.inflate(R.layout.listitem, null);
            // 背景設定
            vw.setBackgroundResource(R.drawable.listitem01);
            //各項目に値を設定 
            TextView inflaterTop    = (TextView) vw.findViewById(R.id.idTop);
            TextView inflaterBottom = (TextView) vw.findViewById(R.id.idBottom);
            Button inflaterRight1  = (Button) vw.findViewById(R.id.idRight1);
            Button inflaterRight2  = (Button) vw.findViewById(R.id.idRight2);
            ListLayout tList = listLayout.get(position);
            inflaterTop.setText(tList.getTextTop());
            inflaterBottom.setText(tList.getTextBottom());
            inflaterRight1.setText(tList.getTextRight1());
            inflaterRight1.setBackgroundResource(tList.getButtonImageId1());
            if(tList.getListner1()==null)
            	inflaterRight1.setVisibility(View.INVISIBLE);
            else
            	inflaterRight1.setOnClickListener(tList.getListner1());
            inflaterRight2.setText(tList.getTextRight2());
            inflaterRight2.setBackgroundResource(tList.getButtonImageId2());
            if(tList.getListner2()==null)
            	inflaterRight2.setVisibility(View.INVISIBLE);
            else
            	inflaterRight2.setOnClickListener(tList.getListner2());
        }
         
        return vw; 
	}
	public ArrayList<ListLayout> getList(){
		return this.listLayout;
	}
	public void clear(){
		listLayout.clear();
		return;
	}

}
