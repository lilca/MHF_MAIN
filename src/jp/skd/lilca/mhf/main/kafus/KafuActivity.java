package jp.skd.lilca.mhf.main.kafus;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import mediba.ad.sdk.android.openx.MasAdView;

import jp.skd.lilca.mhf.lib.bougu.Kafus;
import jp.skd.lilca.mhf.lib.skill_gain.SkillDefines;
import jp.skd.lilca.mhf.lib.skill_gain.SkillGain;
import jp.skd.lilca.mhf.lib.skill_gain.SkillVector;
import jp.skd.lilca.mhf.lib.tools.CsvStringToList;
import jp.skd.lilca.mhf.main.R;
import jp.skd.lilca.mhf.main.kafus.preferences.MyPreferencesKafus;
import jp.skd.lilca.mhf.main.preferences.ComPreferences;
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

public class KafuActivity extends Activity implements Runnable{
	// �O���Ăяo�����[�h�֘A
	private boolean externalMode = false;
	public Activity me;
	private int tmpPos;
	//
	public static final int MAXCOUNT = 1000;
	public static final int REQUEST_CODE = 1;
	ListAdapter adapter;
	//
	String itemName	= "";
	String kafuType	= "���ׂ�";
	String skills	= "";
	int slots		= 2;	// 1~2
	int count		= 0;
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
        setContentView(R.layout.main_kafus);
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
		mad.setAuid("149629");
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
				Kafus tmp = new Kafus(list.get(position).getSrc());
				// �O���N����
				if(externalMode){
					tmpPos = position;
					KafuActivity.this.responceMessage(tmp.getName(), tmp.getSozai());
				}
				else
					KafuActivity.this.viewMessage(tmp.getName(), tmp.getSozai());
				return;
			}
		});
        listview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Kafus tmp = new Kafus(list.get(position).getSrc());
				// �O���N����
				if(externalMode){
					Intent intent = new Intent();
					CharSequence text = list.get(position).getSrc();
					intent.putExtra("text", text);
					me.setResult(RESULT_OK, intent);
					me.finish();
				}
				else
					KafuActivity.this.viewMessage(tmp.getName(), tmp.getSozai());
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
			final Intent intent = new Intent(me, MyPreferencesKafus.class);
			if(externalMode){
				intent.putExtra("mode", "easy");
				intent.putExtra("operation", operation);
    		}
	    	startActivityForResult(intent, REQUEST_CODE);
    		return;
    	}
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu_kafus, menu);
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
    			this.viewMessage("�����Ƃ��","�{�A�v���̓f�[�^�̓��e��ۏ؂�����̂ł͂���܂���BMHF Wiki�̔z�z�f�[�^�����Ƃŉ��H���Ċ��p���Ă���̂ŁAMHF Wiki�ɒǉ��܂��͏C�����ꂽ���e�́A�o�[�W�����A�b�v�̃^�C�~���O�Ŕ��f�ł���΂����Ȃ��A�A�A�A�A�Ǝv���Ă��܂��B�J���ҁA�����ɖڂ��Ȃ��ɂ��A�L���͂��߂܂���m(_ _)m");
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
    	Kafus b;
    	try{
    	// �X�g���[�����J��
    	Resources res = this.getResources();
    	InputStream input = res.openRawResource(R.raw.kafus);
    	BufferedReader br = new BufferedReader(new InputStreamReader(input, "Shift_JIS"));
    	ListLayout temp;
    	// ���[�v�O�N���A
    	count = 0;
    	while( ((line = br.readLine())!=null) && (count<MAXCOUNT) ){
    		if(isMatch(line)){
    			b = new Kafus(line);
    			if(b.isEmpty())
    				continue;
    			temp = new ListLayout();
    			temp.setTextTop(b.getName()+" ("+b.getSlotCostString()+")");
    			String str = b.getSkillsWithColorTag("#CC0000");
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
    	String[] fields = CsvStringToList.split(str, ",");
try{
    	// ���O
    	if(fields[0].indexOf(itemName)==-1 && !itemName.equals(""))
    		return false;
    	// �^�C�v
    	if(!matchingType(fields[0], kafuType))
    		return false;
    	// �X���b�g
    	if(Integer.parseInt(fields[2]) > slots)
    		return false;
    	// �X�L��
    	if( this.isOpeNegative() ){
   		int skillNega = SkillGain.getSkillNegative(fields[1], skills);
    		if(skillNega>=0 && !skills.equals(""))
    			return false;
    	}
    	else{
    		//    	int level = SkillGain.getLevel(skills, ",");
    		int skillGain = SkillGain.getSkillGain(fields[1], skills); 
    		if(skillGain<gain && !skills.equals(""))
    			return false;
    	}
}catch(Exception e){
	e.printStackTrace();
}
		return true;
    }
    private boolean matchingType(String name, String typeString){
    	if(typeString.equals("���ׂ�"))
    		return true;
    	// ��v����
    	if(name.indexOf(typeString.substring(0, 2)) != -1){
    		// PA,PB,PC�̂Ƃ�
    		if(typeString.equals("PA") || typeString.equals("PB") || typeString.equals("PC")){
    			// �h�h���܂܂Ȃ�
    			if(name.indexOf("II") == -1)
    				return true;
   				return false;
    		}
    		// PAII,PBII,PCII
    		if(typeString.equals("PAII") || typeString.equals("PBII") || typeString.equals("PCII")){
    			// �h�h���܂܂Ȃ�
    			if(name.indexOf("II") == -1)
    				return false;
   				return true;
    		}
    		// SA,SB,SC�̂Ƃ�
    		if(typeString.equals("SA") || typeString.equals("SB") || typeString.equals("SC")){
    			// �h�h���܂܂Ȃ�
    			if(name.indexOf("II") == -1)
    				return true;
   				return false;
    		}
    		// SAII,SBII,SCII
    		if(typeString.equals("SAII") || typeString.equals("SBII") || typeString.equals("SCII")){
    			// �h�h���܂܂Ȃ�
    			if(name.indexOf("II") == -1)
    				return false;
   				return true;
    		}
    	}
    	// ��v���Ȃ�
    	return false;
    }
    public void updateCondition(){
    	itemName	= getPreName();
    	skills		= getPreSkillsSelection();
    	kafuType	= getPreType();
    	try{
    		slots = Integer.parseInt(getPreSlots());	// 1~2
    	}catch(Exception e){
    		slots = -1;
    	}
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
    	if(!getPreName().equals(""))
    		res += ", ����="+getPreName();
    	if(!getPreType().equals("���ׂ�") && !getPreType().equals(""))
    		res += ", �^�C�v="+getPreType();
    	if(!getPreSlots().equals("0") && !getPreSlots().equals(""))
    		res += ", �X���b�g����"+getPreSlots();
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
    // ���̏����擾
    public String getPreName(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("edittext_preference_kafus", "");
    }
    // ���̏����擾
    public String getPreType(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference8_kafus", "");
    }
    // �X���b�g�������擾
    public String getPreSlots(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference4_kafus", "2");
    }
    // �X�L���Q�C�������擾
    public String getPreGain(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference7_kafus", "");
    }
    // �X�L���I�������擾
    public String getPreSkillsSelection(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	String[] res = new String[13];
    	res[0] = ComPreferences.getSkills01(pre, ComPreferences.CPF_MODE_KAFUS);
    	res[1] = ComPreferences.getSkills02(pre, ComPreferences.CPF_MODE_KAFUS);
    	res[2] = ComPreferences.getSkills03(pre, ComPreferences.CPF_MODE_KAFUS);
    	res[3] = ComPreferences.getSkills04(pre, ComPreferences.CPF_MODE_KAFUS);
    	res[4] = ComPreferences.getSkills05(pre, ComPreferences.CPF_MODE_KAFUS);
    	res[5] = ComPreferences.getSkills06(pre, ComPreferences.CPF_MODE_KAFUS);
    	res[6] = ComPreferences.getSkills07(pre, ComPreferences.CPF_MODE_KAFUS);
    	res[7] = ComPreferences.getSkills08(pre, ComPreferences.CPF_MODE_KAFUS);
    	res[8] = ComPreferences.getSkills09(pre, ComPreferences.CPF_MODE_KAFUS);
    	res[9] = ComPreferences.getSkills10(pre, ComPreferences.CPF_MODE_KAFUS);
    	res[10] = ComPreferences.getSkills11(pre, ComPreferences.CPF_MODE_KAFUS);
    	res[11] = ComPreferences.getSkills12(pre, ComPreferences.CPF_MODE_KAFUS);
    	res[12] = ComPreferences.getSkills13(pre, ComPreferences.CPF_MODE_KAFUS);
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
		ed.putString("list_preference_kafus", extras.getString("parts"));
		ed.putString("list_preference2_kafus", extras.getString("type"));
		ed.putString("list_preference3_kafus", extras.getString("sex"));
//		ed.putBoolean("checkbox_preference_kafus", extras.getBoolean("sp"));
		ed.putString("edittext_preference_kafus", "");
		ed.putString("list_preference4_kafus", extras.getString("slots"));
		ed.putString("list_preference5_kafus", "0");
		ed.putString("list_preference6_kafus", "20");
		ed.putString("list_preference7_kafus", "1");
		ed.putString("list_preference8_kafus", "���ׂ�");
		// �X�L��
		SkillVector vector = new SkillVector();
		vector.importString(extras.getString("skills"));
		for(int idx=0; idx<SkillDefines.keyList.length; idx++){
			String viewid = SkillDefines.keyList[idx]+ComPreferences.CPF_MODE_KAFUS;
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