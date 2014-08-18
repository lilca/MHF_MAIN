package jp.skd.lilca.mhf.subactivity.setchoice;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import jp.skd.lilca.mhf.lib.bougu.Bougu;
import jp.skd.lilca.mhf.lib.bougu.Sousyoku;
import jp.skd.lilca.mhf.lib.skill_gain.SkillGain;
import jp.skd.lilca.mhf.lib.skill_gain.SkillVector;
import jp.skd.lilca.mhf.lib.tools.CsvStringToList;
import jp.skd.lilca.mhf.main.R;
import jp.skd.lilca.mhf.main.listadapters.setchoice.ItemLayoutComparator;
import jp.skd.lilca.mhf.main.listadapters.setchoice.ItemListAdapter;
import jp.skd.lilca.mhf.main.listadapters.setchoice.ItemListLayout;
import mediba.ad.sdk.android.openx.MasAdView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SetChoiceActivity extends Activity implements Runnable{
	//
	public static final int MAXCOUNT	= 1000;
	public static final int CUTOFF		= 5;
	public static final double CUTRATE	= 0.5; 
	// �O���f�[�^
	String bui;
	int type;
	boolean male;
	boolean isSP;
	int gain;
	SkillVector inVector;
	int count		= 0;
	String skills;
	String strType;
	String strGender;
	String strSlots;
	String strSkills;
	// �Ăяo�����[�h�֘A
	public Activity me;
	private int tmpPos;
	//
	ItemListAdapter adapter;
	public ArrayList<ItemListLayout> list = new ArrayList<ItemListLayout>();
	// �v���O���X�_�C�A���O�p
	Handler handler = new Handler();
	ProgressDialog progressDlg;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setchoice_main);
        // �L��
		MasAdView mad = null;
		mad = (MasAdView) findViewById(R.id.adview);
		mad.setAuid("149624");
		mad.start();
        // �O���ďo������
        me = this;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
        	// �p�����[�^�i�[
        	this.setParameters(extras);
        }
        // ���͂��Ȃ�������
        else{
			Intent intent = new Intent();
			intent.putExtra("text", "non parameters.");
			me.setResult(RESULT_CANCELED, intent);
			me.finish();
        }
        // �{�^���ݒ�
        initBtn();
        // ���X�g�ǉ�
        updateCondition();
		//ListAdapter�ɏ�LList��o�^ 
        adapter = new ItemListAdapter(this, list);
        ListView listview = (ListView)findViewById(R.id.listView1);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Bougu tmp = null;
				try{
					tmp = new Bougu(list.get(position).getSrc());
				}catch(Exception e){
					e.printStackTrace();
				}
				// �m�F�_�C�A���O
				tmpPos = position;
				((SetChoiceActivity) me).responceMessage(tmp.getName(), tmp.getSozai());
				return;
			}
		});
        listview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Bougu tmp = null;
				try{
					tmp = new Bougu(list.get(position).getSrc());
				}catch(Exception e){
					e.printStackTrace();
				}
				// �m�F�_�C�A���O
				tmpPos = position;
				((SetChoiceActivity) me).responceMessage(tmp.getName(), tmp.getSozai());
				return;
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				return;
			}
		});
        // �ǂݍ��݁��\�[�gwith�v���O���X
        Thread pdlg = new Thread(this);
        pdlg.start();
		return;
	}
	@Override
	public void run() {
		try {
			Thread.sleep(200);
		} catch (Exception e) {
			e.printStackTrace();
		}
		readAndSort();
		return;
	}
    private void readAndSort(){
    	// �X���b�h����
    	new Thread(new Runnable(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					@Override
					public void run(){
				    	progressDlg = ProgressDialog.show(me, "�ǂݍ��݁��\�[�g���i���߁j","Please wait...", false);
				    	progressDlg.show();
					}
				});
				readDataBase(list);
				handler.post(new Runnable(){
					@Override
					public void run(){
						progressDlg.dismiss();
						adapter.notifyDataSetChanged();
				        // �����\���ݒ�
				        setCondition();
					}
				});
			}
    	}).start();
    	return;
    }
    private ItemListLayout setItemLayout(Bougu b){
    	ItemListLayout temp = new ItemListLayout();
		temp.setTextTop(
			b.getName()+" "+b.getSlotString()+"\n"+
			b.getTamaNames(",")
		);
		String str = b.getSkillsWithSousyokuWithColorTag(":", "#CC0000")+" Lv"+b.getLevel()+", �h: "+Integer.toString(b.getDef())+" �i���A"+b.getRare()+"�j";
		CharSequence cs = Html.fromHtml(str);
		temp.setTextBottom(cs);
		temp.setTextRight(Integer.toString(SkillGain.getSkillGain(b.getSkillsWithSousyoku(":"), skills)));
		temp.setSrc(b.getCsvString());
		if( b.getSousyokuList().size() > 0)
			temp.setSrcS1(b.getSousyokuList().get(0).getCsvString());
		if( b.getSousyokuList().size() > 1)
			temp.setSrcS2(b.getSousyokuList().get(1).getCsvString());
		if( b.getSousyokuList().size() > 2)
			temp.setSrcS3(b.getSousyokuList().get(2).getCsvString());
    	return temp;
    }
    private ArrayList<Bougu> cutOverItemBougu(ArrayList<Bougu> list, int max){
    	ArrayList<Bougu> tempList = list;
    	if( tempList.size() > CUTOFF ){
    		int th = (int) (max * CUTRATE);
    		list = new ArrayList<Bougu>();
    		for(int idx=0; idx<tempList.size(); idx++)
    			if( th < SkillGain.getSkillGain(tempList.get(idx).getSkills(), skills) )
    				list.add(tempList.get(idx));
    	}
    	return list;
    }
    private ArrayList<Sousyoku> cutOverItemSousyoku(ArrayList<Sousyoku> list, int max){
    	ArrayList<Sousyoku> tempList = list;
    	if( tempList.size() > CUTOFF ){
    		int th = (int) (max * CUTRATE);
    		list = new ArrayList<Sousyoku>();
    		for(int idx=0; idx<tempList.size(); idx++)
    			if( th < SkillGain.getSkillGain(tempList.get(idx).getSkills(), skills) )
    				list.add(tempList.get(idx));
    	}
    	return list;
    }
    //�t�@�C���ǂݍ���
    public void readDataBase(ArrayList<ItemListLayout> result){
		result.clear();
    	String line = null;
    	// �o�b�t�@
    	ArrayList<Bougu> bougu1Candidates			= new ArrayList<Bougu>();
    	ArrayList<Bougu> bougu2Candidates			= new ArrayList<Bougu>();
    	ArrayList<Bougu> bougu3Candidates			= new ArrayList<Bougu>();
    	ArrayList<Bougu> bouguSpCandidates			= new ArrayList<Bougu>();
    	ArrayList<Sousyoku> sousyoku1Candidates		= new ArrayList<Sousyoku>();
    	ArrayList<Sousyoku> sousyoku2Candidates		= new ArrayList<Sousyoku>();
    	ArrayList<Sousyoku> sousyoku3Candidates		= new ArrayList<Sousyoku>();
    	ArrayList<Sousyoku> sousyokuSpCandidates	= new ArrayList<Sousyoku>();
    	Bougu b;
    	Sousyoku s;
    	int maxSkillGainBougu1		= 0;
    	int maxSkillGainBougu2		= 0;
    	int maxSkillGainBougu3		= 0;
    	int maxSkillGainSousyoku1	= 0;
    	int maxSkillGainSousyoku2	= 0;
    	int maxSkillGainSousyoku3	= 0;
try{
    	// �h��̃X�g���[�����J��
    	Resources res = this.getResources();
    	InputStream input = res.openRawResource(R.raw.bougudbs);
    	BufferedReader br = new BufferedReader(new InputStreamReader(input, "Shift_JIS"));
    	// ���[�v�O�N���A
    	while( (line = br.readLine())!=null){
			b = new Bougu(line);
    		if(isMatch(line)){
    			if(b.isEmpty())
    				continue;
    			if(b.getSlot() == 1){
    				bougu1Candidates.add(b);
    				maxSkillGainBougu1 = Math.max(SkillGain.getSkillGain(b.getSkills(), skills), maxSkillGainBougu1);
    			}
    			else
    			if(b.getSlot() == 2){
    				bougu2Candidates.add(b);
    				maxSkillGainBougu2 = Math.max(SkillGain.getSkillGain(b.getSkills(), skills), maxSkillGainBougu2);
    			}
    			else
    			if(b.getSlot() == 3){
    				bougu3Candidates.add(b);
    				maxSkillGainBougu3 = Math.max(SkillGain.getSkillGain(b.getSkills(), skills), maxSkillGainBougu3);
    			}
    		}
    		if(b.isSP() && b.isBui(bui)){
    			bouguSpCandidates.add(b);
    		}
    	}
    	// �X�g���[�������
    	br.close();
    	}catch(Exception e){
    		Log.d("mise", line);
    		e.printStackTrace();
    	}
    	try{
    	// �����i�̃X�g���[�����J��
    	Resources res = this.getResources();
    	InputStream input = res.openRawResource(R.raw.sousyoku);
    	BufferedReader br = new BufferedReader(new InputStreamReader(input, "Shift_JIS"));
    	// ���[�v�O�N���A
    	while( (line = br.readLine())!=null){
			s = new Sousyoku(line);
    		if(isMatchSousyoku(line)){
    			if(s.isEmpty())
    				continue;
        		if(s.isSP()){
        			sousyokuSpCandidates.add(s);
        		}
        		else{
        			if(s.getSlotCost() == 1){
        				sousyoku1Candidates.add(s);
        				maxSkillGainSousyoku1 = Math.max(SkillGain.getSkillGain(s.getSkills(), skills), maxSkillGainSousyoku1);
        			}
        			else
        			if(s.getSlotCost() == 2){
        				sousyoku2Candidates.add(s);
        				maxSkillGainSousyoku2 = Math.max(SkillGain.getSkillGain(s.getSkills(), skills), maxSkillGainSousyoku2);
        			}
        			else
        			if(s.getSlotCost() == 3){
        				sousyoku3Candidates.add(s);
        				maxSkillGainSousyoku3 = Math.max(SkillGain.getSkillGain(s.getSkills(), skills), maxSkillGainSousyoku3);
        			}
        		}
    		}
    	}
    	// �X�g���[�������
    	br.close();
}catch(Exception e){
	Log.d("mise", line);
	e.printStackTrace();
}
    	// �J�b�g
    	bougu1Candidates = cutOverItemBougu(bougu1Candidates, maxSkillGainBougu1);
    	bougu2Candidates = cutOverItemBougu(bougu2Candidates, maxSkillGainBougu2);
    	bougu3Candidates = cutOverItemBougu(bougu3Candidates, maxSkillGainBougu3);
    	sousyoku1Candidates = cutOverItemSousyoku(sousyoku1Candidates, maxSkillGainSousyoku1);
    	sousyoku2Candidates = cutOverItemSousyoku(sousyoku2Candidates, maxSkillGainSousyoku2);
    	sousyoku3Candidates = cutOverItemSousyoku(sousyoku3Candidates, maxSkillGainSousyoku3);
    	int to;
    	int border;
    	// �X���b�g�P����
    	border = 0;
    	ArrayList<ItemListLayout> resSlot1 = new ArrayList<ItemListLayout>();
    	for(int idx=0; idx<bougu1Candidates.size(); idx++){
    		Bougu tempB0 = new Bougu(bougu1Candidates.get(idx).getCsvString());
    		border = SkillGain.getSkillGain(tempB0.getSkills(), skills);
			resSlot1.add(setItemLayout(tempB0));
    		for(int jdx=0; jdx<sousyoku1Candidates.size(); jdx++){
    			Bougu tempB = new Bougu(bougu1Candidates.get(idx).getCsvString(),
    								sousyoku1Candidates.get(jdx).getCsvString(),
    								"","");
    			if( border < SkillGain.getSkillGain(tempB.getSkillsWithSousyoku(":"), skills) )
    				resSlot1.add(setItemLayout(tempB));
    		}
    	}
    	// ���ʂɔ��f
    	Collections.sort(resSlot1, new ItemLayoutComparator());
    	to = Math.min(resSlot1.size(), MAXCOUNT);
    	for(int idx=0; idx<to; idx++)
    		result.add(resSlot1.get(idx));
    	// �X���b�g2����
    	border = 0;
    	ArrayList<ItemListLayout> resSlot2 = new ArrayList<ItemListLayout>();
    	for(int idx=0; idx<bougu2Candidates.size(); idx++){
    		Bougu tempB0 = new Bougu(bougu2Candidates.get(idx).getCsvString());
    		border = SkillGain.getSkillGain(tempB0.getSkills(), skills);
			resSlot2.add(setItemLayout(tempB0));
    		for(int jdx=0; jdx<sousyoku2Candidates.size(); jdx++){
    			Bougu tempB = new Bougu(bougu2Candidates.get(idx).getCsvString(),
    								sousyoku2Candidates.get(jdx).getCsvString(),
    								"","");
    			if( border < SkillGain.getSkillGain(tempB.getSkillsWithSousyoku(":"), skills) )
    				resSlot2.add(setItemLayout(tempB));
    		}
    		for(int jdx1=0; jdx1<sousyoku1Candidates.size(); jdx1++){
    			Bougu tempA = new Bougu(bougu2Candidates.get(idx).getCsvString(),
						sousyoku1Candidates.get(jdx1).getCsvString(),
						"","");
        		int slot1 = SkillGain.getSkillGain(tempA.getSkillsWithSousyoku(":"), skills);
    			if( border <  slot1)
    				resSlot2.add(setItemLayout(tempA));
        		for(int jdx2=0; jdx2<sousyoku1Candidates.size(); jdx2++){
        			if(jdx1 > jdx2)
        				continue;
        			Bougu tempB = new Bougu(bougu2Candidates.get(idx).getCsvString(),
							sousyoku1Candidates.get(jdx1).getCsvString(),
							sousyoku1Candidates.get(jdx2).getCsvString(),"");
        			int slot2 = SkillGain.getSkillGain(tempB.getSkillsWithSousyoku(":"), skills);
        			if( border < slot2 && slot1 < slot2)
        				resSlot2.add(setItemLayout(tempB));
        		}
    		}
    	}
    	// ���ʂɔ��f
    	Collections.sort(resSlot2, new ItemLayoutComparator());
    	to = Math.min(resSlot2.size(), MAXCOUNT);
    	for(int idx=0; idx<to; idx++)
    		result.add(resSlot2.get(idx));
    	// �X���b�g3����
    	border = 0;
    	ArrayList<ItemListLayout> resSlot3 = new ArrayList<ItemListLayout>();
    	for(int idx=0; idx<bougu3Candidates.size(); idx++){
    		Bougu tempB0 = new Bougu(bougu3Candidates.get(idx).getCsvString());
    		border = SkillGain.getSkillGain(tempB0.getSkills(), skills);
			resSlot3.add(setItemLayout(tempB0));
    		for(int jdx=0; jdx<sousyoku3Candidates.size(); jdx++){
    			Bougu tempB = new Bougu(bougu3Candidates.get(idx).getCsvString(),
    								sousyoku3Candidates.get(jdx).getCsvString(),
    								"","");
    			if( border < SkillGain.getSkillGain(tempB.getSkillsWithSousyoku(":"), skills) )
    				resSlot3.add(setItemLayout(tempB));
    		}
    		for(int jdx1=0; jdx1<sousyoku1Candidates.size(); jdx1++){
    			Bougu tempA1 = new Bougu(bougu3Candidates.get(idx).getCsvString(),
    					sousyoku1Candidates.get(jdx1).getCsvString(), "", "");
    			int slot1 = SkillGain.getSkillGain(tempA1.getSkillsWithSousyoku(":"), skills);
    			if( border < slot1 )
    				resSlot3.add(setItemLayout(tempA1));
        		for(int jdx2=0; jdx2<sousyoku2Candidates.size(); jdx2++){
        			if(jdx1 > jdx2)
        				continue;
        			Bougu tempB = new Bougu(bougu3Candidates.get(idx).getCsvString(),
							sousyoku2Candidates.get(jdx2).getCsvString(),
							sousyoku1Candidates.get(jdx1).getCsvString(),"");
        			int slot2 = SkillGain.getSkillGain(tempB.getSkillsWithSousyoku(":"), skills);
        			if( border < slot2 && slot1 < slot2 )
        				resSlot3.add(setItemLayout(tempB));
        		}
        		for(int jdx2=0; jdx2<sousyoku1Candidates.size(); jdx2++){
        			if(jdx1 > jdx2)
        				continue;
        			Bougu tempA2 = new Bougu(bougu3Candidates.get(idx).getCsvString(),
        					sousyoku1Candidates.get(jdx1).getCsvString(),
        					sousyoku1Candidates.get(jdx2).getCsvString(), "");
        			int slot11 = SkillGain.getSkillGain(tempA2.getSkillsWithSousyoku(":"), skills);
            		for(int jdx3=0; jdx3<sousyoku1Candidates.size(); jdx3++){
            			if(jdx2 > jdx3)
            				continue;
            			Bougu tempB = new Bougu(bougu3Candidates.get(idx).getCsvString(),
            					sousyoku1Candidates.get(jdx1).getCsvString(),
            					sousyoku1Candidates.get(jdx2).getCsvString(),
            					sousyoku1Candidates.get(jdx3).getCsvString());
            			int slot3 = SkillGain.getSkillGain(tempB.getSkillsWithSousyoku(":"), skills);
            			if( border < slot3 && slot1 < slot3 && slot11 < slot3 )
            				resSlot3.add(setItemLayout(tempB));
            		}
        		}
    		}
    	}
    	// ���ʂɔ��f
    	Collections.sort(resSlot3, new ItemLayoutComparator());
    	to = Math.min(resSlot3.size(), MAXCOUNT);
    	for(int idx=0; idx<to; idx++)
    		result.add(resSlot3.get(idx));
    	// SP����
    	ArrayList<ItemListLayout> resSP = new ArrayList<ItemListLayout>();
    	for(int idx=0; idx<bouguSpCandidates.size(); idx++)
    		for(int jdx=0; jdx<sousyokuSpCandidates.size(); jdx++){
    			Bougu tempB = new Bougu(bouguSpCandidates.get(idx).getCsvString(),
    								sousyokuSpCandidates.get(jdx).getCsvString(),
    								"","");
    			resSP.add(setItemLayout(tempB));
    		}
    	// ���ʂɔ��f
    	Collections.sort(resSP, new ItemLayoutComparator());
    	to = Math.min(resSP.size(), MAXCOUNT);
    	for(int idx=0; idx<to; idx++)
    		result.add(resSP.get(idx));
    	// �S�̃\�[�g
    	Collections.sort(result, new ItemLayoutComparator());
    	to = Math.min(result.size(), MAXCOUNT);
    	ArrayList<ItemListLayout> preResult = result;
    	result = new ArrayList<ItemListLayout>();
    	for(int idx=0; idx<to; idx++)
    		result.add(preResult.get(idx));
    	count = result.size();
    	return;
    }
    // ��v����
    public boolean isMatch(String str){
try{
    	Bougu b = new Bougu(str);
    	// Bougu���g���ĂȂ������W�b�N�p
    	String[] fields = CsvStringToList.split(str, ",");
    	// �^�C�v
    	if( !((type==0 && fields[2].equals("1")) || (type==1 && fields[3].equals("1"))) )
    		return false;
    	// ����
    	if( male != b.canMale() && !male != b.canFemale() )
    		return false;
    	// ���ʊm�F
    	if(!fields[0].equals(bui) && !bui.equals(""))
    		return false;
    	// SP����
    	if(isSP)
    		if(fields[1].startsWith("SP"))
    			return true;
    		else
    			return false;
    	// ���O
    	// �X���b�g
    	// �h��
    	// ���A�x
    	// �X�L��
//    	int level = SkillGain.getLevel(skills, ",");
    	int skillGain = SkillGain.getSkillGain(fields[12], skills); 
    	if(skillGain<gain && !skills.equals(""))
    		return false;
}catch(Exception e){
	e.printStackTrace();
	Toast.makeText(this, "Error data:"+str, Toast.LENGTH_LONG).show();
}
		return true;
    }
    public boolean isMatchSousyoku(String str){
try{
		Sousyoku rec = new Sousyoku(str);
		// �^�C�v
		// ���O
		// �X���b�g
		// �����N
		// �X�L��
		int skillGain = SkillGain.getSkillGain(rec.getSkills(), skills); 
		if(skillGain<gain && !skills.equals(""))
			return false;
}catch(Exception e){
	e.printStackTrace();
	Toast.makeText(this, "Error data:"+str, Toast.LENGTH_LONG).show();
}
		return true;
    }
    public void viewMessage(String t, String msg){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(t);
    	builder.setMessage(msg);
    	builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setResult(RESULT_OK);
				return;
			}
		});
    	builder.create();
    	builder.show();
    	return;
    }
    private void setParameters(Bundle extras){
		this.bui		= extras.getString("parts");
		this.strType	= extras.getString("type");
		this.strGender	= extras.getString("sex");
		// �X�L��
    	this.strSkills	= extras.getString("skills");
		inVector		= new SkillVector();
		inVector.importString(this.strSkills);
		this.skills		= inVector.exportSkillsWithoutZeroElements();
    	return;
    }
    public void updateCondition(){
    	// ����
    	// �^�C�v
    	if(this.strType.equals("���m"))
    		type = 0;	// 0:���m�C1:�K���i�[
    	else
    		type = 1;
    	// �L��������
    	if(strGender.equals("�j"))
    		male = true;
    	else
    		male = false;
    	// ����
    	// �X�L��
    	// SP
   		// �X���b�g
    	// �h���
    	// ���A�x
    	// �X�L���Q�C��
    	this.gain = 1;	// �Œ�
    	return;
    }
    private void initBtn(){
//    	Button searchBtn = (Button) this.findViewById(R.id.button1);
//   	searchBtn.setOnClickListener(searchListener);
    	return;
    }
    public boolean responceMessage(String t, String msg){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(t);
    	builder.setMessage(msg);
    	builder.setPositiveButton("�I��", new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//
				Intent intent = new Intent();
				CharSequence text = list.get(tmpPos).getSrc();
				CharSequence text_s1 = list.get(tmpPos).getSrcS1();
				CharSequence text_s2 = list.get(tmpPos).getSrcS2();
				CharSequence text_s3 = list.get(tmpPos).getSrcS3();
				intent.putExtra("text", text);
				intent.putExtra("text_s1", text_s1);
				intent.putExtra("text_s2", text_s2);
				intent.putExtra("text_s3", text_s3);
				me.setResult(RESULT_OK, intent);
				me.finish();
				return;
			}
		});
    	builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				return;
			}
		});
    	builder.create();
    	builder.show();
    	return true;
    }
    // �����\���ݒ�
    private void setCondition(){
    	TextView t = (TextView) findViewById(R.id.textView1);
       	t.setText(getConditionString());
    	return;
    }
    // ����������擾
    private String getConditionString(){
    	String res = "�����F";
    	res += "�^�C�v="+this.strType+", ����="+this.strGender+", ����="+this.bui;
    	if(!this.skills.equals(""))
    		res += ", �X�L����"+this.skills;
    	if(count>=MAXCOUNT)
    		res += " (Hit="+count+" Over)";
    	else
    		res += " (Hit="+count+")";
    	return res;
    }
}
