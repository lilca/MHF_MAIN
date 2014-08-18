package jp.skd.lilca.mhf.main.buki;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import mediba.ad.sdk.android.openx.MasAdView;

import jp.skd.lilca.mhf.lib.buki.Buki;
import jp.skd.lilca.mhf.main.R;
import jp.skd.lilca.mhf.main.buki.preferences.MyPreferencesBuki;

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

public class BukiActivity extends Activity implements Runnable{
	// �O���Ăяo�����[�h�֘A
	private boolean externalMode = false;
	public Activity me;
	private int tmpPos;
	//
	public static final int MAXCOUNT = 1000;
	public static final int REQUEST_CODE = 1;
	ListAdapter adapter;
	//
	String buki		= "�Ў茕";
	String itemName	= "";
	String skills	= "";
	int slots		= 3;	// 0~3
	int defence		= 0;	// 0~
	int count		= 0;
	int rare		= 0;
	int kaishin		= 0;
	int attack		= 0;
	int bukibai		= 0;
	String attr_type	= "�Ȃ�";
	String spattr_type	= "�Ȃ�";
	int reach		= 0;
	int gain		= 0;
	String btype	= "";
	//
	public ArrayList<String> stringList = new ArrayList<String>();
	ArrayList<ListLayout> list = new ArrayList<ListLayout>();
	// �v���O���X�_�C�A���O�p
	Handler handler = new Handler();
	ProgressDialog progressDlg;   /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_buki);
        // �O���ďo������
        me = this;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
        	externalMode = true;
        	this.setParameters(extras);
        }
        else{
			this.viewMessage("�����点","MHF�̃X�L���V�~�����[�V�����i�u���������H�v�j���A���j���[�́u���Ђ����Ɂv����_�E�����[�h�ł��܂��B���񂨎������������B");
        }
        // �L��
		MasAdView mad = null;
		mad = (MasAdView) this.findViewById(R.id.adview);
		mad.setAuid("149627");
		mad.start();
        // �{�^���ݒ�
        initBtn();
        // ���X�g�ǉ�
        updateCondition();
		//ListAdapter�ɏ�LList��o�^ 
        adapter = new ListAdapter(this, list);
        ListView listview = (ListView)this.findViewById(R.id.listView1);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Buki tmp = new Buki(stringList.get(position));
				// �O���N����
				if(externalMode){
					tmpPos = position;
					BukiActivity.this.responceMessage(tmp.getName(), tmp.getSozai());
				}
				else
					BukiActivity.this.viewMessage(tmp.getName(), tmp.getSozai());
				return;
			}
		});
        listview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Buki tmp = new Buki(stringList.get(position));
				// �O���N����
				if(externalMode){
					Intent intent = new Intent();
					CharSequence text = stringList.get(position);
					intent.putExtra("text", text);
					me.setResult(RESULT_OK, intent);
					me.finish();
				}
				else
					BukiActivity.this.viewMessage(tmp.getName(), tmp.getSozai());
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
			final Intent intent = new Intent(me, MyPreferencesBuki.class);
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
    	inflater.inflate(R.menu.menu_buki, menu);
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
    	Buki b;
    	try{
    	// �X�g���[�����J��
    	Resources res = this.getResources();
    	InputStream input = res.openRawResource(R.raw.bukidbs);
    	BufferedReader br = new BufferedReader(new InputStreamReader(input, "Shift_JIS"));
    	ListLayout temp;
    	// ���[�v�O�N���A
    	count = 0;
    	stringList.clear();
    	while( ((line = br.readLine())!=null) && (count<MAXCOUNT) ){
    		if(isMatch(line)){
    			b = new Buki(line);
    			if(b.isEmpty())
    				continue;
    			temp = new ListLayout();
    			String str = (count+1)+":"+b.getName()+" "+b.getSlotString();
    			CharSequence cs = Html.fromHtml(str);
    			temp.setTextTop(cs);
    			str 	= b.getType()+" �U��:"+b.getAttack()+
    						"("+b.getBukiBairitsu()+") "+
    						b.getAttrAttackString()+" "+b.getSpAttrAttackString()+
    						", �h: "+Integer.toString(b.getDef())+
    						", ��: "+b.getKaishin()+
    						", ���A: "+b.getRare()+
    	    				" <font color=\"aqua\">"+b.getOyakata()+"</font>";
    			String btype = b.getBukiType();
    			if(!btype.equals(""))
    				str += "<font color=\"#ff69b4\">&lt;"+btype+"&gt;</font>";
    			cs = Html.fromHtml(str);
    			temp.setTextBottom(cs);
    			if(b.getType().equals("�|"))
    				temp.setTextRight( Html.fromHtml(b.getBin().printHtml()) );
    			else
    			if(b.getType().equals("�K�������X"))
    				temp.setTextRight( Html.fromHtml(b.getHougeki().print()) );
    			else
    			if(b.getType().equals("��J"))
    				temp.setTextRight( Html.fromHtml(b.getNeiro().printHtml()) );
    			else
    				temp.setTextRight(b.getReach());
    			result.add(temp);
    			stringList.add(line);
    			count++;
    		}
    	}
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
try{
   	Buki b = new Buki(str);
    	// �^�C�v
		if( !buki.equals("") && !b.getType().equals(buki) )
			return false;
		// SP
		if( btype.equals("�r�o") ){
			if( !b.isSP() )
				return false;
		}
		// HC
		else
		if( btype.equals("�g�b") ){
			if( !b.isBukiType("HC") )
				return false;
		}
    	// ��ޔ���
		else
    	if( !btype.equals("") && b.getBukiType().indexOf(btype)==-1 )
   			return false;
    	// ���O
    	if(b.getName().indexOf(itemName)==-1 && !itemName.equals(""))
    		return false;
    	// ���[�`
    	if(Buki.getReachScalar(b.getReach()) < reach)
    		return false;
    	// �U��
    	if(b.getAttack() < attack)
    		return false;
    	// ����{����
    	if(b.getBukiBairitsu() < bukibai)
    		return false;
    	// �����U��
		if(!attr_type.equals("�Ȃ�"))
			if(!b.getAttrType().equals(attr_type))
				return false;
    	// �ُ푮���U��
		if(!spattr_type.equals("�Ȃ�"))
			if(!b.getSpAttrType().equals(spattr_type))
			return false;
    	// ��S��
    	if(b.getKaishin() < kaishin)
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
//    	int skillGain = SkillGain.getSkillGain(fields[12], skills); 
//    	if(skillGain<gain && !skills.equals(""))
//    		return false;
}catch(Exception e){
	e.printStackTrace();
}
		return true;
    }
    public void updateCondition(){
    	if(getPrePart().equals("���ׂ�"))
    		buki = "";
    	else
    		buki = getPrePart();
    	itemName	= getPreName();
    	if(getPreBukiType().equals("���ׂ�"))
    		btype = "";
    	else
    		btype = getPreBukiType();
   		attr_type	= getPreAttrType();
   		spattr_type	= getPreSpAttrType();
    	try{
    		slots = Integer.parseInt(getPreSlots());	// 0~3
    	}catch(Exception e){
    		slots = -1;
    	}
    	try{
    		defence = Integer.parseInt(getPreDef());	// 0~
    	}catch(Exception e){
    		defence = -1;
    	}
    	try{
    		rare = Integer.parseInt(getPreRare());	// 0~
    	}catch(Exception e){
    		rare = -1;
    	}
    	try{
    		kaishin = Integer.parseInt(getPreKaishin());	// 0~
    	}catch(Exception e){
    		kaishin = -1;
    	}
    	try{
    		attack = Integer.parseInt(getPreAttack());	// 0~
    	}catch(Exception e){
    		attack = -1;
    	}
    	try{
    		bukibai = Integer.parseInt(getPreBukibai());	// 0~
    	}catch(Exception e){
    		bukibai = -1;
    	}
    	try{
    		reach = Buki.getReachScalar(getPreReach());
    	}catch(Exception e){
    		reach = -1;
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
    	TextView t = (TextView) this.findViewById(R.id.textView1);
       	t.setText(getConditionString());
    	return;
    }
    // ����������擾
    private String getConditionString(){
    	String res = "�����F";
    	if(!getPreBukiType().equals("���ׂ�"))
    		res += ", ���="+getPreBukiType();
    	if(!getPreType().equals("���ׂ�") && !getPreType().equals(""))
    		res += ", ����="+getPreType();
    	if(!getPreName().equals(""))
    		res += ", ����="+getPreName();
    	if(!getPreAttack().equals("0") && !getPreAttack().equals(""))
    		res += ", �U����"+getPreAttack();
    	if(!getPreBukibai().equals("0") && !getPreBukibai().equals(""))
    		res += ", ����{���う"+getPreBukibai();
    	if(!getPreAttrType().equals("�Ȃ�") && !getPreAttrType().equals(""))
    		res += ", ����="+getPreAttrType();
    	if(!getPreSpAttrType().equals("�Ȃ�") && !getPreSpAttrType().equals(""))
    		res += ", �ُ푮��="+getPreSpAttrType();
    	if(!getPreReach().equals("�ɒZ") && !getPreRare().equals(""))
    		res += ", ���[�`��"+getPreReach();
    	if(!getPreSlots().equals("0") && !getPreSlots().equals(""))
    		res += ", �X���b�g����"+getPreSlots();
    	if(!getPreKaishin().equals("-100") && !getPreRare().equals(""))
    		res += ", ��S����"+getPreKaishin();
    	if(!getPreDef().equals("0") && !getPreDef().equals(""))
    		res += ", �h�䁆"+getPreDef();
    	if(!getPreRare().equals("20") && !getPreRare().equals(""))
    		res += ", ���A�x��"+getPreRare();
    	if(count>=MAXCOUNT)
    		res += " (Hit="+count+" Over)";
    	else
    		res += " (Hit="+count+")";
    	return res;
    }
    // �^�C�v�����擾
    public String getPreType(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference_buki", "");
    }
    // ���ʏ����擾
    public String getPreSex(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference3_buki", "");
    }
    // ���ʏ����擾
    public String getPrePart(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference_buki", "");
    }
    // ���̏����擾
    public String getPreName(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("edittext_preference_buki", "");
    }
    // �X���b�g�������擾
    public String getPreSlots(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference4_buki", "");
    }
    // �h������擾
    public String getPreDef(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference5_buki", "");
    }
    // ���A�x�����擾
    public String getPreRare(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference6_buki", "");
    }
    // ��S�������擾
    public String getPreKaishin(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference7_buki", "");
    }
    // �U���������擾
    public String getPreAttack(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference9_buki", "");
    }
    // ����{����������擾
    public String getPreBukibai(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference11_buki", "");
    }
    // �����U���������擾
    public String getPreAttrType(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference12_buki", "");
    }
    // �ُ푮���U���������擾
    public String getPreSpAttrType(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference13_buki", "");
    }
    // ���[�`�������擾
    public String getPreReach(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference8_buki", "");
    }
    // �X�L���Q�C�������擾
    public String getPreGain(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference10_buki", "");
    }
    // �����ގ擾
    public String getPreBukiType(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference_bougutype_buki", "");
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
				CharSequence text = stringList.get(tmpPos);
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
//		ed.putString("list_preference_buki", extras.getString("parts"));
//		ed.putString("list_preference2_buki", extras.getString("type"));
//		ed.putString("list_preference3_buki", extras.getString("sex"));
//		ed.putBoolean("checkbox_preference_buki", extras.getBoolean("sp"));
//		ed.putString("edittext_preference_buki", "");
		ed.putString("list_preference4_buki", extras.getString("slots"));
//		ed.putString("list_preference5_buki", "0");
//		ed.putString("list_preference6_buki", "20");
//		ed.putString("list_preference7_buki", "0");
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
				        // �����\��
				        setCondition();
					}
				});
			}
    	}).start();
    	return;
    }
}