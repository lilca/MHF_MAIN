package jp.skd.lilca.mhf.main.soubi;

import java.util.ArrayList;

import jp.skd.lilca.mhf.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
			//inflater.xmlから1行分のレイアウトを生成 
            vw = layoutInflater.inflate(R.layout.listitem_soubi, null);
            //各項目に値を設定 
            TextView inflaterTop    = (TextView) vw.findViewById(R.id.idTop);
            TextView inflaterBottom = (TextView) vw.findViewById(R.id.idBottom);
            TextView inflaterRight  = (TextView) vw.findViewById(R.id.idRight);
            inflaterTop.setText(listLayout.get(position).getTextTop());
            inflaterBottom.setText(listLayout.get(position).getTextBottom());
            inflaterRight.setText(listLayout.get(position).getTextRight());
        }
		else{ 
            //各項目に値を設定 
            TextView inflaterTop    = (TextView) vw.findViewById(R.id.idTop);
            TextView inflaterBottom = (TextView) vw.findViewById(R.id.idBottom);
            TextView inflaterRight  = (TextView) vw.findViewById(R.id.idRight);
            inflaterTop.setText(listLayout.get(position).getTextTop());
            inflaterBottom.setText(listLayout.get(position).getTextBottom());
            inflaterRight.setText(listLayout.get(position).getTextRight());
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
