package jp.skd.lilca.mhf.main.skill;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import mediba.ad.sdk.android.openx.MasAdView;

import jp.skd.lilca.mhf.lib.bougu.Sousyoku;
import jp.skd.lilca.mhf.lib.skill_gain.SkillDefines;
import jp.skd.lilca.mhf.lib.skill_gain.SkillGain;
import jp.skd.lilca.mhf.lib.skill_gain.SkillVector;
import jp.skd.lilca.mhf.lib.tools.CsvStringToList;
import jp.skd.lilca.mhf.main.R;
import jp.skd.lilca.mhf.main.preferences.ComPreferences;
import jp.skd.lilca.mhf.main.skill.ListAdapter;
import jp.skd.lilca.mhf.main.skill.ListLayout;
import jp.skd.lilca.mhf.main.skill.preferences.MyPreferencesSkill;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SkillActivity extends Activity implements Runnable{
	// �O���Ăяo�����[�h�֘A
	private boolean externalMode = false;
	public Activity me;
	private int tmpPos;
	//
	public static final int MAXCOUNT = 1000;
	public static final int REQUEST_CODE = 1;
	ListAdapter adapter;
	//
	boolean type	= false;	// 0:SP�ȊO�C1:SP
	String itemName	= "";
	String skills	= "";
	int slots		= 3;	// 0~3
	int count		= 0;
	String rank		= "";	// ���ʁA��ʁAHR100
	int gain		= 0;
	String operation;
	//
	public ArrayList<ListLayout> list = new ArrayList<ListLayout>();
	// �v���O���X�_�C�A���O�p
	Handler handler = new Handler();
	ProgressDialog progressDlg;
   /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_skill);
        // �O���ďo������
        me = this;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
        	externalMode = true;
        	this.operation = extras.getString("operation");
        	if(this.operation==null)
        		this.operation = "";
        	this.setParameters(extras);
        }
        else{
			this.viewMessage("�����点","MHF�̃X�L���V�~�����[�V�����i�u���������H�v�j���A���j���[�́u���Ђ����Ɂv����_�E�����[�h�ł��܂��B���񂨎������������B");
        }
        // �L��
		MasAdView mad = null;
		mad = (MasAdView) findViewById(R.id.adview);
		mad.setAuid("149623");
		mad.start();
        // �{�^���ݒ�
        initBtn();
        // ���X�g�ǉ�
        updateCondition();
		//ListAdapter�ɏ�LList��o�^ 
        adapter = new ListAdapter(this, list);
        ListView listview = (ListView)findViewById(R.id.listView1);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Sousyoku tmp = new Sousyoku(list.get(position).getSrc());
				// �O���N����
				if(externalMode){
					tmpPos = position;
					SkillActivity.this.responceMessage(tmp.getName(), tmp.getSozai());
				}
				else
					SkillActivity.this.viewMessage(tmp.getName(), tmp.getSozai());
				return;
			}
		});
        listview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Sousyoku tmp = new Sousyoku(list.get(position).getSrc());
				// �O���N����
				if(externalMode){
					Intent intent = new Intent();
					CharSequence text = list.get(position).getSrc();
					intent.putExtra("text", text);
					me.setResult(RESULT_OK, intent);
					me.finish();
				}
				else
					SkillActivity.this.viewMessage(tmp.getName(), tmp.getSozai());
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
    private void initBtn(){
    	Button searchBtn = (Button) this.findViewById(R.id.button1);
    	searchBtn.setOnClickListener(searchListener);
    	return;
    }
    private OnClickListener searchListener = new OnClickListener(){
    	@Override
    	public void onClick(View view) {
        	// �e�L�X�g�r���[�̃N���A
			final Intent intent = new Intent(me, MyPreferencesSkill.class);
			if(externalMode)
				intent.putExtra("mode", "easy");
	    	startActivityForResult(intent, REQUEST_CODE);
    		return;
    	}
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu_skill, menu);
        return true;
    }
    // �I�v�V�������j���[�A�C�e�����I�����ꂽ���ɌĂяo����܂�
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    		case R.id.item1:
    			Uri uri1=Uri.parse("market://search?q=pub:\"lilca\"");
    			Intent familyIntent=new Intent(Intent.ACTION_VIEW,uri1);
    	    	startActivity(familyIntent);
    			break;
    		case R.id.item2:
    			this.viewMessage("�����Ƃ��", "�{�A�v���̓f�[�^�̓��e��ۏ؂�����̂ł͂���܂���BMHF Wiki�̔z�z�f�[�^�����Ƃŉ��H���Ċ��p���Ă���̂ŁAMHF Wiki�ɒǉ��܂��͏C�����ꂽ���e�́A�o�[�W�����A�b�v�̃^�C�~���O�Ŕ��f�ł���΂����Ȃ��A�A�A�A�A�Ǝv���Ă��܂��B�J���ҁA�����ɖڂ��Ȃ��ɂ��A�L���͂��߂܂���m(_ _)m");
    			break;
    		default:
    			break;
    	}
        return true;
    }
    //�t�@�C���ǂݍ���
    public void readDataBase(ArrayList<ListLayout> result){
		result.clear();
    	String line = null;
    	Sousyoku b;
    	try{
    	// �X�g���[�����J��
    	Resources res = this.getResources();
    	InputStream input = res.openRawResource(R.raw.sousyoku);
    	BufferedReader br = new BufferedReader(new InputStreamReader(input, "Shift_JIS"));
    	// ���[�v�O�N���A
    	count = 0;
    	ListLayout temp;
    	while( ((line = br.readLine())!=null) && (count<MAXCOUNT) ){
    		if(isMatch(line)){
    			b = new Sousyoku(line);
    			if(b.isEmpty())
    				continue;
    			temp = new ListLayout();
    			temp.setTextTop(b.getName()+" �i"+b.getSlotCostString()+"�j");
    			String str = b.getSkillsWithColorTag("#CC0000")+" �i"+b.getRank()+"�j";
    			CharSequence cs = Html.fromHtml(str);
    			temp.setTextBottom(cs);
    			if( this.isOpeNegative() )
    				temp.setTextRight(Integer.toString(SkillGain.getSkillNegative(b.getSkills(), skills)));
    			else
    				temp.setTextRight(Integer.toString(SkillGain.getSkillGain(b.getSkills(), skills)));
    			temp.setSrc(line);
    			result.add(temp);
    			count++;
    		}
    	}
    	// �\�[�g
    	Collections.sort(result, new LayoutComparator());
    	// �����}����
    	if( this.isOpeNegative() )
    		Collections.reverse(result);
    	// �X�g���[�������
    	br.close();
    	}catch(Exception e){
    		if( line != null )
    			Log.d("mise", line);
    		e.printStackTrace();
    	}

    	return;
    }
    // ��v����
    public boolean isMatch(String str){
    	Sousyoku rec = new Sousyoku(str);
    	// �^�C�v
    	if( !( (type && rec.isSP()) || (!type && !rec.isSP()) ) )
    		return false;
    	// ���O
    	if(rec.getName().indexOf(itemName)==-1 && !itemName.equals(""))
    		return false;
    	// �X���b�g
    	if(rec.getSlotCost() > slots)
    		return false;
    	// �����N
//    	if(rec.equals("?"))
//    		;
//    	else
//    	if(rec.getRank().equals("HR100") && ( !rank.equals("HR100") ))
//    		return false;
//    	else
//       	if(!rec.getRank().equals("����") && ( rank.equals("����") ))
//       		return false;
    	// �X�L��
    	if( this.isOpeNegative() ){
   		int skillNega = SkillGain.getSkillNegative(rec.getSkills(), skills);
    		if(skillNega>=0 && !skills.equals(""))
    			return false;
    	}
    	else{

//    	int level = SkillGain.getLevel(skills, ",");
    		int skillGain = SkillGain.getSkillGain(rec.getSkills(), skills); 
    		if(skillGain<gain && !skills.equals(""))
    			return false;
    	}
    	return true;
    }
    public void updateCondition(){
   		type = getPreType();	// 0:SP�ȊO�C1:SP
    	itemName	= getPreName();
    	skills		= getPreSkillsSelection();
    	try{
    		slots = Integer.parseInt(getPreSlots());	// 0~3
    	}catch(Exception e){
    		slots = -1;
    	}
//    	rank = getPreRank();
    	try{
    		gain = Integer.parseInt(getPreGain());	// 0~
    	}catch(Exception e){
    		gain = -1;
    	}
    	return;
    }
    // �ݒ肩��̖߂�
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // �����\���ݒ�
        updateCondition();
        // �ǂݍ��݁��\�[�gwith�v���O���X
        Thread pdlg = new Thread(this);
        pdlg.start();
    	return;
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
    	res += "SP�V���[�Y="+getPreType();
    	if(!getPreName().equals(""))
    		res += ", ����="+getPreName();
    	if(!getPreSlots().equals("0") && !getPreSlots().equals(""))
    		res += ", �X���b�g����"+getPreSlots();
//    	if(!getPreRank().equals("����") && !getPreRank().equals(""))
//   		res += ", �����N��"+getPreRank();
    	if(!getPreGain().equals("0") && !getPreGain().equals(""))
    		res += ", �X�L���Q�C����"+getPreGain();
    	if(!getPreSkillsSelection().equals(""))
    		res += ", �X�L����"+getPreSkillsSelection();
    	if(count>=MAXCOUNT)
    		res += " (Hit="+count+" Over)";
    	else
    		res += " (Hit="+count+")";
    	return res;
    }
    // �^�C�v�����擾
    public boolean getPreType(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getBoolean("checkbox_preference_skill", false);
    }
    // ���̏����擾
    public String getPreName(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("edittext_preference_skill", "");
    }
    // �X���b�g�������擾
    public String getPreSlots(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference4_skill", "");
    }
    // �����N�x�����擾
    public String getPreRank(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference6_skill", "");
    }
    // �X�L���Q�C�������擾
    public String getPreGain(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference7_skill", "");
    }
    // �X�L���I�������擾
    public String getPreSkillsSelection(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	String[] res = new String[13];
    	res[0] = ComPreferences.getSkills01(pre, ComPreferences.CPF_MODE_SKILL);
    	res[1] = ComPreferences.getSkills02(pre, ComPreferences.CPF_MODE_SKILL);
    	res[2] = ComPreferences.getSkills03(pre, ComPreferences.CPF_MODE_SKILL);
    	res[3] = ComPreferences.getSkills04(pre, ComPreferences.CPF_MODE_SKILL);
    	res[4] = ComPreferences.getSkills05(pre, ComPreferences.CPF_MODE_SKILL);
    	res[5] = ComPreferences.getSkills06(pre, ComPreferences.CPF_MODE_SKILL);
    	res[6] = ComPreferences.getSkills07(pre, ComPreferences.CPF_MODE_SKILL);
    	res[7] = ComPreferences.getSkills08(pre, ComPreferences.CPF_MODE_SKILL);
    	res[8] = ComPreferences.getSkills09(pre, ComPreferences.CPF_MODE_SKILL);
    	res[9] = ComPreferences.getSkills10(pre, ComPreferences.CPF_MODE_SKILL);
    	res[10] = ComPreferences.getSkills11(pre, ComPreferences.CPF_MODE_SKILL);
    	res[11] = ComPreferences.getSkills12(pre, ComPreferences.CPF_MODE_SKILL);
    	res[12] = ComPreferences.getSkills13(pre, ComPreferences.CPF_MODE_SKILL);
    	return CsvStringToList.scatNullSkip(res, ",");
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
				intent.putExtra("text", text);
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
    private void setParameters(Bundle extras){
		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
		Editor ed = pre.edit();
//		ed.putString("list_preference_skill", extras.getString("parts"));
//		ed.putString("list_preference2_skill", extras.getString("type"));
//		ed.putString("list_preference3_skill", extras.getString("sex"));
//		ed.putString("edittext_preference_skill", "");	// ���O
		String temp = extras.getString("sp");
		if(temp.equals("true"))
			ed.putBoolean("checkbox_preference_skill", true);		// �r�o
		else
			ed.putBoolean("checkbox_preference_skill", false);		// �r�o
		ed.putString("list_preference4_skill", extras.getString("slots"));		// ����X���b�g
//		ed.putString("list_preference5_skill", "0");
		ed.putString("list_preference7_skill", "1");		// �X�L���Q�C��
		// �X�L��
		SkillVector vector = new SkillVector();
		vector.importString(extras.getString("skills"));
		for(int idx=0; idx<SkillDefines.keyList.length; idx++){
			String viewid = SkillDefines.keyList[idx]+ComPreferences.CPF_MODE_SKILL;
			ed.putString(viewid, "");
			String key = SkillDefines.nameList[idx];
			if(vector.getValue(key)==null)
				continue;
			int val = vector.getValue(key);
			if(val == 0)
				continue;
			if(val > 0)
				ed.putString(viewid, "+"+Integer.toString(val));
			else
				ed.putString(viewid, Integer.toString(val));
		}
		ed.commit();
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
				    	progressDlg = ProgressDialog.show(me, "�ǂݍ��݁��\�[�g��","Please wait...", false);
				    	progressDlg.show();
					}
				});
				readDataBase(list);
				handler.post(new Runnable(){
					@Override
					public void run(){
						if(progressDlg != null & progressDlg.isShowing() )
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
    private boolean isOpeNegative(){
    	if( this.operation.equals("negative") )
    		return true;
    	return false;
    }
}