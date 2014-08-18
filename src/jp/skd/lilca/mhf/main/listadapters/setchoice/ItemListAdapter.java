package jp.skd.lilca.mhf.main.listadapters.setchoice;

import java.util.ArrayList;

import jp.skd.lilca.mhf.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater = null;
    private ArrayList<ItemListLayout> listLayout = null;
    
    public ItemListAdapter(Context context, ArrayList<ItemListLayout> lstly){
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
            vw = layoutInflater.inflate(R.layout.itemlistitem, null);
            //各項目に値を設定 
            TextView inflaterTop    = (TextView) vw.findViewById(R.id.idItemListTop);
            TextView inflaterBottom = (TextView) vw.findViewById(R.id.idItemListBottom);
            TextView inflaterRight  = (TextView) vw.findViewById(R.id.idItemListRight);
            inflaterTop.setText(listLayout.get(position).getTextTop());
            inflaterBottom.setText(listLayout.get(position).getTextBottom());
            inflaterRight.setText(listLayout.get(position).getTextRight());
        }
		else{ 
            //各項目に値を設定 
            TextView inflaterTop    = (TextView) vw.findViewById(R.id.idItemListTop);
            TextView inflaterBottom = (TextView) vw.findViewById(R.id.idItemListBottom);
            TextView inflaterRight  = (TextView) vw.findViewById(R.id.idItemListRight);
            inflaterTop.setText(listLayout.get(position).getTextTop());
            inflaterBottom.setText(listLayout.get(position).getTextBottom());
            inflaterRight.setText(listLayout.get(position).getTextRight());
        } 
         
        return vw; 
	}
	public ArrayList<ItemListLayout> getList(){
		return this.listLayout;
	}
	public void clear(){
		listLayout.clear();
		return;
	}
}
