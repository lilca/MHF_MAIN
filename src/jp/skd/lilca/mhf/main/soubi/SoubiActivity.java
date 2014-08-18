package jp.skd.lilca.mhf.main.soubi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import mediba.ad.sdk.android.openx.MasAdView;

import jp.skd.lilca.mhf.lib.bougu.Bougu;
import jp.skd.lilca.mhf.lib.skill_gain.SkillDefines;
import jp.skd.lilca.mhf.lib.skill_gain.SkillGain;
import jp.skd.lilca.mhf.lib.skill_gain.SkillVector;
import jp.skd.lilca.mhf.lib.tools.CsvStringToList;
import jp.skd.lilca.mhf.main.R;
import jp.skd.lilca.mhf.main.preferences.ComPreferences;
import jp.skd.lilca.mhf.main.soubi.preferences.MyPreferencesSoubi;

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
import android.widget.Toast;

public class SoubiActivity extends Activity implements Runnable{
	// �O���Ăяo�����[�h�֘A
	private boolean externalMode = false;
	public Activity me;
	private int tmpPos;
	//
	public static final int MAXCOUNT = 10000;
	public static final int REQUEST_CODE = 1;
	ListAdapter adapter;
	//
	String bui		= "��";
	int type		= 0;	// 0:���m�C1:�K���i�[
	boolean male	= true;	// true:�j�L�����@false:���L����
	String itemName	= "";
	String skills	= "";
	int slots		= 3;	// 0~3
	int defence		= 0;	// 0~
	int count		= 0;
	int rare		= 0;
	int gain		= 0;
	SkillVector vector = new SkillVector();
	String bouguType= "";
	String operation;
	boolean sp;
	//
	public ArrayList<ListLayout> list = new ArrayList<ListLayout>();
	// �v���O���X�_�C�A���O�p
	Handler handler = new Handler();
	ProgressDialog progressDlg;
   /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_soubi);
        // �O���ďo������
        me = this;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
        	externalMode = true;
        	this.operation = extras.getString("operation");
        	if(this.operation==null)
        		this.operation = "";
        	this.setParameters(extras);
        	/*-- ���V�t�F���Ή� --*/
        	if(extras.getString("owner is lucifer")!=null){
        		getMostItem(extras);
        		return;
        	}
        	/*-- ���V�t�F���Ή�end --*/
        }
        else{
			this.viewMessage("�����点","MHF�̃X�L���V�~�����[�V�����i�u���������H�v�j���A���j���[�́u���Ђ����Ɂv����_�E�����[�h�ł��܂��B���񂨎������������B");
        }
        // �L��
		MasAdView mad = null;
		mad = (MasAdView) findViewById(R.id.adview);
		mad.setAuid("149622");
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
				Bougu tmp = null;
				try{
					tmp = new Bougu(list.get(position).getSrc());
				}catch(Exception e){
					e.printStackTrace();
				}
				// �O���N����
				if(externalMode){
					tmpPos = position;
					SoubiActivity.this.responceMessage(tmp.getName(), tmp.getSozai());
				}
				else
					SoubiActivity.this.viewMessage(tmp.getName(), tmp.getSozai());
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
				// �O���N����
				if(externalMode){
					Intent intent = new Intent();
					CharSequence text = list.get(position).getSrc();
					intent.putExtra("text", text);
					me.setResult(RESULT_OK, intent);
					me.finish();
				}
				else
					SoubiActivity.this.viewMessage(tmp.getName(), tmp.getSozai());
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
			final Intent intent = new Intent(me, MyPreferencesSoubi.class);
			if(externalMode){
				intent.putExtra("mode", "easy");
				intent.putExtra("operation", operation);
				intent.putExtra("sp", sp);
			}
	    	startActivityForResult(intent, REQUEST_CODE);
    		return;
    	}
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu_soubi, menu);
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
    	Bougu b;
    	try{
    	// �X�g���[�����J��
    	Resources res = this.getResources();
    	InputStream input = res.openRawResource(R.raw.bougudbs);
    	BufferedReader br = new BufferedReader(new InputStreamReader(input, "Shift_JIS"));
    	ListLayout temp;
    	// ���[�v�O�N���A
    	count = 0;
    	while( ((line = br.readLine())!=null) && (count<MAXCOUNT) ){
    		boolean aas = isMatch(line);
    		if(aas){
    			b = new Bougu(line);
    			if(b.isEmpty())
    				continue;
    			String btype = b.getBouguType();
    			temp = new ListLayout();
    			temp.setTextTop(b.getName()+" "+b.getSlotString());
    			String str = b.getSkillsWithColorTag("#CC0000")+" Lv"+b.getLevel()+", �h: "+Integer.toString(b.getDef())+" �i���A"+b.getRare()+"�j";
    			if(!btype.equals(""))
    				str += "<font color=\"#ff69b4\">&lt;"+btype+"�h��&gt;</font>";
    			CharSequence cs = Html.fromHtml(str);
    			temp.setTextBottom(cs);
    			temp.setTextRight(Integer.toString(SkillGain.getSkillGain(b.getSkills(), skills)));
    			temp.setSrc(line);
    			result.add(temp);
    			count++;
    		}
    	}
    	// �\�[�g
		Collections.sort(result, new LayoutComparator());
    	// �X�g���[�������
    	br.close();
    	}catch(Exception e){
    		if(line != null)
    			Log.d("mise", line);
    		e.printStackTrace();
    	}

    	return;
    }
    // ��v����
    public boolean isMatch(String str){
try{
    	Bougu b = new Bougu(str);
    	// Bougu���g���ĂȂ������W�b�N�p
    	//CsvStringToList cstl = new CsvStringToList();
    	//String[] fields = cstl.split(str, ",");
    	// �^�C�v
    	//if( !((type==0 && fields[2].equals("1")) || (type==1 && fields[3].equals("1"))) )
    	if( !((type==0 && b.isKenshi()) || (type==1 && b.isGunner())) )
    		return false;
    	// ����
    	if( male != b.canMale() && !male != b.canFemale() )
    		return false;
    	// ���ʊm�F
    	//if(!fields[0].equals(bui) && !bui.equals(""))
    	if(!b.isBui(bui) && !bui.equals(""))
    		return false;
    	// SP����
    	if(bouguType.equals(""));
    	else
    	if(bouguType.equals("�r�o") && !b.isSP()) return false;
    	else
    	if(bouguType.equals("�g�b") && !b.isBouguType("�g�b")) return false;
    	else
       	if(bouguType.equals("���") && !b.isBouguType("���")) return false;
    	else
    	if(bouguType.equals("����") && !b.isBouguType("����")) return false;
    	else
       	if(bouguType.equals("�V��") && !b.isBouguType("�V��")) return false;
    	else
       	if(bouguType.equals("�e��") && !b.isBouguType("�e��")) return false;
    	else
       	if(bouguType.equals("��`") && !b.isBouguType("��`")) return false;
    	else
       	if(bouguType.equals("�f��") && !b.isBouguType("�f��")) return false;
       	else
       	if(bouguType.equals("�f��") && !b.isBouguType("�f��")) return false;
    	
    	// ���O
    	if(b.getName().indexOf(itemName)==-1 && !itemName.equals(""))
    		return false;
    	// �X���b�g
    	if(b.getSlot() < slots)
    		return false;
    	// �h��
    	if(b.getDef() < defence)
    		return false;
    	// ���A�x
    	if(b.getRare() > rare)
    		return false;
    	// �X�L��
//    	int level = SkillGain.getLevel(skills, ",");
    	int skillGain = SkillGain.getSkillGain(b.getSkills(), skills); 
    	if(skillGain<gain && !skills.equals(""))
    		return false;
}catch(Exception e){
	e.printStackTrace();
	Toast.makeText(this, "Error data:"+str, Toast.LENGTH_LONG).show();
	return false;
}
		return true;
    }
    public void updateCondition(){
    	// ����
    	if(getPrePart().equals("���ׂ�"))
    		bui = "";
    	else
    		bui = getPrePart();
    	// �w�i�ύX
		View v = findViewById(R.id.linearLayout0);
    	if( bui.equals("") )
    		v.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.soubi_all_bg));
    	else
   		if( bui.equals("��") )
    		v.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.soubi_atama_bg));
    	else
   		if( bui.equals("��") )
    		v.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.soubi_dou_bg));
    	else
   		if( bui.equals("�r") )
    		v.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.soubi_ude_bg));
    	else
   		if( bui.equals("��") )
    		v.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.soubi_koshi_bg));
    	else
   		if( bui.equals("�r") )
    		v.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.soubi_ashi_bg));
    	// �^�C�v
    	if(getPreType().equals("���m"))
    		type = 0;	// 0:���m�C1:�K���i�[
    	else
    		type = 1;
    	// �L��������
    	if(getPreGender().equals("�j"))
    		male = true;
    	else
    		male = false;
    	// ����
    	itemName	= getPreName();
    	// �X�L��
    	skills		= getPreSkillsSelection();
    	// �h��^�C�v
    	if(getPreBouguType().equals("���ׂ�"))
    		bouguType = "";
    	else
    		bouguType = getPreBouguType();
   		// �X���b�g
    	try{
    		slots = Integer.parseInt(getPreSlots());	// 0~3
    	}catch(Exception e){
    		slots = -1;
    	}
    	// �h���
    	try{
    		defence = Integer.parseInt(getPreDef());	// 0~
    	}catch(Exception e){
    		defence = -1;
    	}
    	// ���A�x
    	try{
    		rare = Integer.parseInt(getPreRare());	// 0~
    	}catch(Exception e){
    		rare = -1;
    	}
    	// �X�L���Q�C��
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
    	res += "�^�C�v="+getPreType()+", ����="+getPreGender()+", ����="+getPrePart();
    	if(!getPreBouguType().equals("���ׂ�"))
    		res += ", ���="+getPreBouguType();
    	if(!getPreName().equals(""))
    		res += ", ����="+getPreName();
    	if(!getPreSlots().equals("0") && !getPreSlots().equals(""))
    		res += ", �X���b�g����"+getPreSlots();
    	if(!getPreDef().equals("0") && !getPreDef().equals(""))
    		res += ", �h�䁆"+getPreDef();
    	if(!getPreRare().equals("20") && !getPreRare().equals(""))
    		res += ", ���A�x��"+getPreRare();
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
    public String getPreType(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference2_soubi", "");
    }
    // ���ʏ����擾
    public String getPreGender(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference3_soubi", "");
    }
    // ���ʏ����擾
    public String getPrePart(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference_soubi", "");
    }
    // ���̏����擾
    public String getPreName(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("edittext_preference_soubi", "");
    }
    // �X���b�g�������擾
    public String getPreSlots(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference4_soubi", "");
    }
    // �h������擾
    public String getPreDef(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference5_soubi", "");
    }
    // ���A�x�����擾
    public String getPreRare(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference6_soubi", "");
    }
    // �X�L���Q�C�������擾
    public String getPreGain(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference7_soubi", "");
    }
    // SP�����擾
    public String getPreBouguType(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference_bougutype_soubi", "");
    }
    // �X�L���I�������擾
    public String getPreSkillsSelection(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	String[] res = new String[13];
    	res[0] = ComPreferences.getSkills01(pre, ComPreferences.CPF_MODE_SOUBI);
    	res[1] = ComPreferences.getSkills02(pre, ComPreferences.CPF_MODE_SOUBI);
    	res[2] = ComPreferences.getSkills03(pre, ComPreferences.CPF_MODE_SOUBI);
    	res[3] = ComPreferences.getSkills04(pre, ComPreferences.CPF_MODE_SOUBI);
    	res[4] = ComPreferences.getSkills05(pre, ComPreferences.CPF_MODE_SOUBI);
    	res[5] = ComPreferences.getSkills06(pre, ComPreferences.CPF_MODE_SOUBI);
    	res[6] = ComPreferences.getSkills07(pre, ComPreferences.CPF_MODE_SOUBI);
    	res[7] = ComPreferences.getSkills08(pre, ComPreferences.CPF_MODE_SOUBI);
    	res[8] = ComPreferences.getSkills09(pre, ComPreferences.CPF_MODE_SOUBI);
    	res[9] = ComPreferences.getSkills10(pre, ComPreferences.CPF_MODE_SOUBI);
    	res[10] = ComPreferences.getSkills11(pre, ComPreferences.CPF_MODE_SOUBI);
    	res[11] = ComPreferences.getSkills12(pre, ComPreferences.CPF_MODE_SOUBI);
    	res[12] = ComPreferences.getSkills13(pre, ComPreferences.CPF_MODE_SOUBI);
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
		ed.putString("list_preference_soubi", extras.getString("parts"));
		ed.putString("list_preference2_soubi", extras.getString("type"));
		ed.putString("list_preference3_soubi", extras.getString("sex"));
		if(extras.getBoolean("sp")){
			ed.putString("list_preference_bougutype_soubi", "�r�o");
			sp = true;
		}
		else{
			ed.putString("list_preference_bougutype_soubi", "���ׂ�");
			sp = false;
		}
		ed.putString("edittext_preference_soubi", "");
		ed.putString("list_preference4_soubi", extras.getString("slots"));
		ed.putString("list_preference5_soubi", "0");
		ed.putString("list_preference6_soubi", "20");
		ed.putString("list_preference7_soubi", "1");
		// �X�L��
		SkillVector vector = new SkillVector();
		vector.importString(extras.getString("skills"));
		for(int idx=0; idx<SkillDefines.keyList.length; idx++){
			String viewid = SkillDefines.keyList[idx]+ComPreferences.CPF_MODE_SOUBI;
			ed.putString(viewid, "");
			String key = SkillDefines.nameList[idx];
			if(vector.getValue(key)==null)
				continue;
			Integer val = vector.getValue(key);
			if(val == null)
				continue;
			if(val > 0)
				ed.putString(viewid, "+"+Integer.toString(val));
			else
				ed.putString(viewid, Integer.toString(val));
		}
		ed.commit();
    	return;
    }
    private void getMostItem(Bundle bdl){
    	// ����
   		bui = bdl.getString("parts");
    	// �^�C�v
    	if(bdl.getString("type").equals("���m"))
    		type = 0;	// 0:���m�C1:�K���i�[
    	else
    		type = 1;
    	// �L��������
    	if(bdl.getString("sex").equals("�j"))
    		male = true;
    	else
    		male = false;
    	// ����
    	itemName	= "";
    	// �X�L��
    	skills		= bdl.getString("skills");
    	// SP
    	if(bdl.getString("sp").equals("true"))
    		bouguType	= "�r�o";
    	else
    		bouguType	= "";
   		// �X���b�g
   		slots = Integer.parseInt(bdl.getString("slots"));
    	// �h���
   		defence = 0;
    	// ���A�x
   		rare = 20;
    	// �X�L���Q�C��
   		gain = 1;

   		// ��Ԃ��������
        readDataBase(list);
		Intent intent = new Intent();
		CharSequence text = list.get(0).getSrc();
		intent.putExtra("text", text);
		me.setResult(RESULT_OK, intent);
		me.finish();
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
}