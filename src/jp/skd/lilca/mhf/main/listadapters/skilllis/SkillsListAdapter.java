package jp.skd.lilca.mhf.main.listadapters.skilllis;

import java.util.ArrayList;

import jp.skd.lilca.mhf.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SkillsListAdapter extends BaseAdapter {
	
	private LayoutInflater layoutInflater = null;
    private ArrayList<SkillsListLayout> listLayout = null;
    
    public SkillsListAdapter(Context context, ArrayList<SkillsListLayout> lstly){
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
            vw = layoutInflater.inflate(R.layout.skillslistitem, null);
            //各項目に値を設定 
            TextView inflaterLL		= (TextView) vw.findViewById(R.id.idLineLeft);
            TextView inflaterLR1	= (TextView) vw.findViewById(R.id.idLineRight1);
            TextView inflaterLR2	= (TextView) vw.findViewById(R.id.idLineRight2);
            TextView inflaterLR3	= (TextView) vw.findViewById(R.id.idLineRight3);
            TextView inflaterLR4	= (TextView) vw.findViewById(R.id.idLineRight4);
            SkillsListLayout tList = listLayout.get(position);
            inflaterLL.setText(tList.getTextLeft());
            inflaterLR1.setText(tList.getTextRight());
            inflaterLR2.setText(tList.getTextRight2());
            inflaterLR3.setText(tList.getTextRight3());
            inflaterLR4.setText(tList.getTextRight4());
        }
         
        return vw; 
	}
	public ArrayList<SkillsListLayout> getList(){
		return this.listLayout;
	}
	public void clear(){
		listLayout.clear();
		return;
	}

}
