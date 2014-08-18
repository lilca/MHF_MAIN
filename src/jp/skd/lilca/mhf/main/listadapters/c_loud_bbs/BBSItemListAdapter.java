package jp.skd.lilca.mhf.main.listadapters.c_loud_bbs;

import java.util.ArrayList;

import jp.skd.lilca.mhf.datastruct.BBSItem;
import jp.skd.lilca.mhf.main.R;
import jp.skd.lilca.mhf.subactivity.savedatamanagement.SaveDataManagementActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class BBSItemListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater = null;
    private ArrayList<BBSItemListLayout> listLayout = null;
//    private BBSItem bbsdata;
	private SaveDataManagementActivity owner;
    
    public BBSItemListAdapter(Context context, ArrayList<BBSItemListLayout> lstly, SaveDataManagementActivity o){
    	//LayoutInflater���擾 
        this.layoutInflater	= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listLayout		= lstly;
        this.owner			= o;
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
			//inflater.xml����1�s���̃��C�A�E�g�𐶐� 
            vw = layoutInflater.inflate(R.layout.bbslistitem, null);
		}
        // �w�i�ݒ�
        vw.setBackgroundResource(R.drawable.listitem01);
        //�e���ڂɒl��ݒ� 
        TextView inflaterTop    = (TextView) vw.findViewById(R.id.idTop);
        TextView inflaterMiddle0= (TextView) vw.findViewById(R.id.idMiddle0);
        TextView inflaterMiddle1= (TextView) vw.findViewById(R.id.idMiddle1);
        TextView inflaterMiddle2= (TextView) vw.findViewById(R.id.idMiddle2);
        TextView inflaterMiddle3= (TextView) vw.findViewById(R.id.idMiddle3);
        TextView inflaterMiddle4= (TextView) vw.findViewById(R.id.idMiddle4);
        TextView inflaterBottom = (TextView) vw.findViewById(R.id.idBottom);
        Button inflaterButton	= (Button) vw.findViewById(R.id.idBBSRight1);
        TextView tvIikamo		= (TextView) vw.findViewById(R.id.idIikamoText);
        ToggleButton tglIikamo	= (ToggleButton) vw.findViewById(R.id.idIIkamoButton);
        // �l�i�[
        inflaterTop.setText(listLayout.get(position).getTextTop());
        inflaterMiddle0.setText(listLayout.get(position).getTextMiddle0());
        inflaterMiddle1.setText(listLayout.get(position).getTextMiddle1());
        inflaterMiddle2.setText(listLayout.get(position).getTextMiddle2());
        inflaterMiddle3.setText(listLayout.get(position).getTextMiddle3());
        inflaterMiddle4.setText(listLayout.get(position).getTextMiddle4());
        inflaterBottom.setText(listLayout.get(position).getTextBottom());
        
        // ���X�i�[
        BBSItem src	= listLayout.get(position).getBBSItem();
        inflaterTop.setOnClickListener(mainListener);
        inflaterTop.setTag(src);
        inflaterMiddle0.setOnClickListener(mainListener);
        inflaterMiddle0.setTag(src);
        inflaterMiddle1.setOnClickListener(mainListener);
        inflaterMiddle1.setTag(src);
        inflaterMiddle2.setOnClickListener(mainListener);
        inflaterMiddle2.setTag(src);
        inflaterMiddle3.setOnClickListener(mainListener);
        inflaterMiddle3.setTag(src);
        inflaterMiddle4.setOnClickListener(mainListener);
        inflaterMiddle4.setTag(src);
        inflaterBottom.setOnClickListener(mainListener);
        inflaterBottom.setTag(src);
        // �폜�{�^��
        String delflag	= src.delflag;
        if( delflag.equals("1") ){
        	inflaterButton.setBackgroundResource(R.drawable.btnimage_del);
        	inflaterButton.setTag(src);
        	inflaterButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					BBSItem data	= (BBSItem)v.getTag();
					putMessage(owner, data);
					return;
				}
        	});
        }
        else
        	inflaterButton.setBackgroundColor(Color.argb(0, 0, 0, 0));
        // �����������
        String buf;
        if( src.iikamo_me.equals("1") ){
        	tglIikamo.setChecked(true);	// �����������Ă�
        	if( src.iikamo_cnt.equals("1") )
        		buf	= "���Ȃ����u���������v�Ǝv���Ă��܂�";
        	else
        		buf	= "���Ȃ��Ƒ�"+(Integer.parseInt(src.iikamo_cnt)-1)+"�l���u���������v�Ǝv���Ă��܂�";
        }
        else{
        	tglIikamo.setChecked(false);	// �����������ĂȂ�
            if( src.iikamo_cnt.equals("0") )
            	buf	= "";
            else
            	buf	= src.iikamo_cnt+"�l���u���������v�Ǝv���Ă��܂�";
        }
        tvIikamo.setText(Html.fromHtml(buf));
        // ���������̃��X�i�[��ݒ�
        tglIikamo.setOnClickListener(iikamoTglListener);
        tglIikamo.setTag(src);
        return vw; 
	}
	private OnClickListener mainListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			BBSItem item	= (BBSItem)v.getTag();
			owner.touchBBSItem(item);
			return;
		}
	};
	private OnClickListener iikamoTglListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			ToggleButton tgl = (ToggleButton)v;
			BBSItem item	= (BBSItem)tgl.getTag();
			// ���������ύX
			owner.changeIikamo(tgl.isChecked(), item);
			return;
		}
		
	};
	public ArrayList<BBSItemListLayout> getList(){
		return this.listLayout;
	}
	public void clear(){
		listLayout.clear();
		return;
	}
	String itemid;
	public void putMessage(Activity me, BBSItem data){
		itemid	= data.id;
		AlertDialog.Builder alert	= new AlertDialog.Builder(me);
		alert.setTitle("�m�F");
		alert.setMessage("�폜���܂����H");
		alert.setPositiveButton("�폜",
			new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				owner.delBBSItem(itemid);
				return;
			}
		});
		alert.setNegativeButton("�߂�",
				new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
		alert.create();
		alert.show();
		return;
	}
}
