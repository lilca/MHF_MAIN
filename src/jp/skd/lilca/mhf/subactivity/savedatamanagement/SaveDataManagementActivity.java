package jp.skd.lilca.mhf.subactivity.savedatamanagement;

import java.util.ArrayList;
import java.util.HashMap;

import jp.skd.lilca.mhf.datastruct.BBSItem;
import jp.skd.lilca.mhf.lib.savedata.SavedData;
import jp.skd.lilca.mhf.lib.skill_value_list.StatusText;
import jp.skd.lilca.mhf.lib.tools.CsvStringToList;
import jp.skd.lilca.mhf.lib.datastruct.MessageData;
import jp.skd.lilca.mhf.main.Content;
import jp.skd.lilca.mhf.main.MainActivity;
import jp.skd.lilca.mhf.main.R;
import jp.skd.lilca.mhf.main.buki.BukiActivity;
import jp.skd.lilca.mhf.main.kafus.KafuActivity;
import jp.skd.lilca.mhf.main.listadapters.c_loud_bbs.BBSItemListAdapter;
import jp.skd.lilca.mhf.main.listadapters.c_loud_bbs.BBSItemListLayout;
import jp.skd.lilca.mhf.main.listadapters.savedatamanagement.SetItemListAdapter;
import jp.skd.lilca.mhf.main.listadapters.savedatamanagement.SetItemListLayout;
import jp.skd.lilca.mhf.main.preferences.cLoudPreferences;
import jp.skd.lilca.mhf.main.skill.SkillActivity;
import jp.skd.lilca.mhf.main.skill_value_list.SkillInfoIO;
import jp.skd.lilca.mhf.main.soubi.SoubiActivity;
import jp.skd.lilca.mhf.main.tab.TabButton;
import jp.skd.lilca.mhf.subactivity.c_loud_bbs.cLoudBBSActivity;
import jp.skd.lilca.mhf.web.WebConnection;
import jp.skd.lilca.mhf.web.WherePart;
import jp.skd.lilca.mhf.web.cLoudResult;
import mediba.ad.sdk.android.openx.MasAdView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.util.Linkify;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class SaveDataManagementActivity extends TabActivity implements Runnable{
	//
	public static final int MAXCOUNT	= 20;
	// �O���f�[�^
	int count		= 0;
	// ID
	public final static int REQUEST_EDITDATA	= 2001;
	public final static int REQUEST_cLOUDBBS	= 2002;
	// �Ăяo�����[�h�֘A
	public SaveDataManagementActivity me;
	//
	SetItemListAdapter adapter;			// �{��
	SetItemListAdapter adapter_cLoud;	// cLoud
	BBSItemListAdapter adapter_bbs;		// BBS
	public ArrayList<SetItemListLayout> list		= new ArrayList<SetItemListLayout>();	// �{��
	public ArrayList<SetItemListLayout> list_cLoud	= new ArrayList<SetItemListLayout>();	// cLoud
	public ArrayList<BBSItemListLayout> list_bbs	= new ArrayList<BBSItemListLayout>();	// BBS
	// �v���O���X�_�C�A���O�p
	Handler handler = new Handler();
	ProgressDialog progressDlg;
	ProgressDialog progressDlg2;
	// �X�L�����
	SkillInfoIO skillInfo;
	// �N���b�v�{�[�h
	String clipBoard = "";
	TextView clipView;
	// �^�u�֘A
	private static final String TAG[] = {  
		"tagCore", "tagCloud", "tagBBS"};
	TabHost tabHost;
	TabHost.TabSpec spec;
	View tab_v;
	// cLoud
	WebConnection skd;
	// �J�����g�^�u
	String currentTab;
	private static boolean initSigninFlag = true;
	// �C���e���g
	Intent intent_editdata;
	Intent intent_cLoudBBS;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savedatamanagement_main);
        // �X���[�v����
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        me = this;
        // �X�L����񐶐�
        this.skillInfo = new SkillInfoIO(this.getResources());
        // cLoud
        this.skd = WebConnection.getInstance();
        this.skd.initialize(this.skillInfo);
        // Web�ݒ�
        skd = WebConnection.getInstance();
        // �L��
		MasAdView mad = null;
		mad = (MasAdView) findViewById(R.id.adview);
		mad.setAuid("149624");
		mad.start();
        // �O���ďo������
        me = this;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            setParameters(extras);
        }
        // �{�^���ݒ�
        initBtn();
		//ListAdapter�ɏ�LList��o�^
        adapter			= new SetItemListAdapter(this, list);
        adapter_cLoud	= new SetItemListAdapter(this, list_cLoud);
        adapter_bbs		= new BBSItemListAdapter(this, list_bbs, this);
        ListView listview = (ListView)findViewById(R.id.listView1);
        ListView clistview = (ListView)findViewById(R.id.listView2);
        ListView blistview = (ListView)findViewById(R.id.listView3);
        listview.setAdapter(adapter);
        clistview.setAdapter(adapter_cLoud);
        blistview.setAdapter(adapter_bbs);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				editMessage("�f�[�^�Ǘ�", position, list.get(position).getSrc(), false);
				return;
			}
		});
        clistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				editMessage("�f�[�^�Ǘ�", position, list_cLoud.get(position).getSrc(), true);
				return;
			}
		});
        blistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				editMessageBBS("�f�[�^�Ǘ�", list_bbs.get(position).getBBSItem());
				return;
			}
		});
        // �J�����g�^�u
        currentTab = TAG[0];
        // TabHost�ݒ�
        tabHost = getTabHost();
        tabHost.setOnTabChangedListener(tabChangeListener);
        // �e�^�u����
        spec = tabHost.newTabSpec(TAG[0]);
        tab_v = new TabButton(this, "�{��", R.drawable.clear_sq);
        spec.setIndicator(tab_v);
        spec.setContent(R.id.content1sd);
        tabHost.addTab(spec);
        spec = tabHost.newTabSpec(TAG[1]);
        tab_v = new TabButton(this, "cLoud\n�X�g���[�W", R.drawable.clear_sq);
        spec.setIndicator(tab_v);
        spec.setContent(R.id.content2sd);
        tabHost.addTab(spec);
        spec = tabHost.newTabSpec(TAG[2]);
        tab_v = new TabButton(this, "cLoud\nBBS", R.drawable.clear_sq);
        spec.setIndicator(tab_v);
        spec.setContent(R.id.content3sd);
        tabHost.addTab(spec);
        /*spec = tabHost.newTabSpec(TAG[0]);
        spec.setIndicator("�{��");
        spec.setContent(R.id.content1);
        tabHost.addTab(spec);
        spec = tabHost.newTabSpec(TAG[1]);
        spec.setIndicator("cLoud�X�g���[�W");
        spec.setContent(R.id.content2);
        tabHost.addTab(spec);
        */
        // �N���b�v�{�[�h�̃r���[���擾
    	clipView = (TextView) me.findViewById(R.id.textView1);
    	dispClipBoard();
    	String[] msg = MessageData.getMessageAtRandom();
    	cautionMessage(msg[0], msg[1]);
   		// �ǂݍ��݁��\�[�gwith�v���O���X
   		Thread pdlg = new Thread(this);
   		pdlg.start();
		return;
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
       	MenuInflater inflater = getMenuInflater();
       	inflater.inflate(R.menu.savedatamgt_menu, menu);
    	return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
/*    		case R.id.menuitem1:
    			break;
*/    		case R.id.menuitem2:
    			HashMap<Integer, String> d = new HashMap<Integer, String>();
    			for(int idx=0; idx<list.size(); idx++)
    				d.put(idx, list.get(idx).getSrc());
    			putStorageList(d);
		   		Thread pdlg2 = new Thread((SaveDataManagementActivity)me);
		   		pdlg2.start();
    			break;
// cLoudBBS�����ɂ��s�v
/*    		case R.id.menuitem3:
    			me.startActivityForResult(intent_cLoudBBS, REQUEST_cLOUDBBS);
    			break;
*/
/*    		case R.id.menuitem4:
    			break;
*/    		case R.id.menuitem5:
    			luncherMessage();
    			break;
    		default:
    			break;
    	}
        return true;
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == REQUEST_EDITDATA){
			if(resultCode == RESULT_OK){
				//
				String res = data.getExtras().getString("editresult");
				if( res != null ){
					clipBoard = res;
				}
				//
			}
			Thread pdlg = new Thread((SaveDataManagementActivity)me);
	   		pdlg.start();
		}
// cLoudBBS�̓����ɂ��s�v
/*		else
		if(requestCode == REQUEST_cLOUDBBS){
			if(resultCode == RESULT_OK){
				String choiced = data.getExtras().getString("choiceddata");
				copyData(choiced);
			}
		}
*/
		return;
	}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	if( keyCode == KeyEvent.KEYCODE_BACK ){
   			AlertDialog.Builder dlg;
   			dlg = new AlertDialog.Builder(this);
 			dlg.setTitle("�m�F");
   			dlg.setMessage("�A�v�����I�����܂����H");
   			dlg.setPositiveButton("�͂�", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
   			dlg.setNegativeButton("������", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					;	// �������Ȃ�
				}
   			});
   			dlg.show();
    	}
    	else
    	if( keyCode == KeyEvent.KEYCODE_HOME ){
    		;
    	}
    	else
       	if( keyCode == KeyEvent.KEYCODE_MENU ){
       		;
       	}
    	// onOptionsItemSelected�֘A�g���邽��super.onKeyDown���Ă�
    	return super.onKeyDown(keyCode, event);
    }
	@Override
	public void run() {
		try {
			Thread.sleep(300);
		} catch (Exception e) {
			e.printStackTrace();
		}
   		read();
		return;
	}
    private void read(){
    	// �X���b�h����
    	new Thread(new Runnable(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					@Override
					public void run(){
				    	progressDlg = ProgressDialog.show(me, "�ǂݍ��ݒ�","Please wait...", false);
				    	progressDlg.show();
					}
				});
				// �ǂݏo��
				readDataBase(currentTab, list, list_cLoud, list_bbs);
				handler.post(new Runnable(){
					@Override
					public void run(){
						progressDlg.dismiss();
						adapter.notifyDataSetChanged();
						adapter_cLoud.notifyDataSetChanged();
						dispClipBoard();
						// �ŏ��łȂ��A�܂��̓^�u���u�{�́v�ł���
						if( !initSigninFlag || currentTab.equals(TAG[0]))
							;
						else
						if( signinres==null ){
							Toast.makeText(me, "cLoud�ɃA�N�Z�X�ł��܂���ł���", Toast.LENGTH_LONG).show();
							noPreMessage("cLoud�ɃA�N�Z�X�ł��܂���", "email�܂���password�����ݒ�");
						}
						else{
							Toast.makeText(me, signinres.getHeadValue("message"), Toast.LENGTH_LONG).show();
							if(signinres.getHeadValue("result").equals("ok"))
								initSigninFlag = false;
							else
								noPreMessage("cLoud�ɃA�N�Z�X�ł��܂���", "email�܂���password�����ݒ�");
						}
						if(accessres!=null)
							Toast.makeText(me, accessres.getHeadValue("message"), Toast.LENGTH_LONG).show();
					}
				});
			}
    	}).start();
    	return;
    }
    // �N���b�v�{�[�h�ݒ�
    private void dispClipBoard(){
    	if( clipBoard.equals("") )
    		clipView.setText("Clipboard = �Ȃ�");
    	else{
    		SavedData sd = new SavedData();
    		sd.setAllElementsToString(clipBoard);
    		String title = sd.getTitle();
    		String type = "�K���i�[";
    		if(sd.isKenshi())
    			type = "���m";
    		clipView.setText("Clipboard = "+title+"("+type+")"+"\n"+
        		"�U: "+StatusText.getAllAtk(skillInfo.getData(), sd)+"  "+
        		"�h: "+StatusText.getAllDef(skillInfo.getData(), sd)+"  "+
        		StatusText.getAllTaiseiString(skillInfo.getData(), sd, ":", ",")
        	);
    	}
    	return;
    }
    cLoudResult signinres;
    cLoudResult accessres;
    //�t�@�C���ǂݍ���
    public void readDataBase(String tab,	ArrayList<SetItemListLayout> result,
    										ArrayList<SetItemListLayout> cresult,
    										ArrayList<BBSItemListLayout> bresult){
    	if(tab.equals(TAG[0])){
    		result.clear();
    		// �����f�[�^�ǂݍ���
    		for(int idx=0; idx<Content.SAVE_FILE_LIST.length; idx++){
    			Content content = new Content(this, Content.SAVE_FILE_LIST[idx]);
    			content.read();
    			SavedData data = content.getSavedData();
// �������p
String dummy = data.getAllElementsToString();
    			// �P���R�[�h
    			getListItem(data, idx, result);
    		}
    	}
    	else
    	if(tab.equals(TAG[1])){
    		// cLoud�A�N�Z�X
    		signinres = null;
    		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    		String email = pre.getString("email_preference", "");
    		String secret = pre.getString("password_preference", "");
    		if(email.equals("") || secret.equals("")){
    			return;
    		}
    		if( initSigninFlag ){
    			try {
    				signinres = skd.connect(email, secret);
    			}catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    		// cLoud�f�[�^�ǂݍ���
    		accessres = null;
    		cresult.clear();
    		cLoudResult res = getStorageList();
    		ArrayList<String> lst = new ArrayList<String>();
    		if( res != null )
    			lst = res.getBodyList();
    		for(int idx=0; idx<lst.size(); idx++){
    			SavedData data = new SavedData();
    			String[] temp = CsvStringToList.split(lst.get(idx), ";sep;");
    			data.setAllElementsToString(temp[1]);
    			// �P���R�[�h
    			getListItem(data, idx, cresult);
    		}
    	}
    	else
    	if(tab.equals(TAG[2])){
        	bresult.clear();
    		signinres = null;
    		accessres = null;
        	// cLoud�A�N�Z�X
        	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
        	String email = pre.getString("email_preference", "");
        	String secret = pre.getString("password_preference", "");
        	if(email.equals("") || secret.equals("")){
        		return;
        	}
        	if( initSigninFlag ){
        		try {
        			signinres = skd.connect(email, secret);
        		}catch (Exception e) {
        			e.printStackTrace();
        		}
        	}
        	// cLoud�f�[�^�ǂݍ���
        	getBBSList(WebConnection.MODE_GET_BBS_ALL);				// ���͌Œ�
    	}
    	return;
    }
    private cLoudResult delbbsres;
    private String itemid;
    public void delBBSItem(String id){
    	itemid	= id;
    	// �X���b�h����
    	new Thread(new Runnable(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					@Override
					public void run(){
				    	progressDlg2 = ProgressDialog.show(me, "cLoud �폜��","Please wait...", false);
				    	progressDlg2.show();
					}
				});
				// �폜
				try {
					delbbsres = skd.deleteBBSItem(itemid);
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.post(new Runnable(){
					@Override
					public void run(){
						progressDlg2.dismiss();
				    	adapter_bbs.notifyDataSetChanged();
						if(delbbsres==null)
							Toast.makeText(me, "delBBSItem����߂�l������܂���", Toast.LENGTH_LONG).show();
						else
							Toast.makeText(me, delbbsres.getHeadValue("message"), Toast.LENGTH_LONG).show();
				    	// ���t���b�V��
				   		Thread pdlg = new Thread((SaveDataManagementActivity)me);
				   		pdlg.start();
					}
				});
			}
    	}).start();
    	return;
    }
    private cLoudResult getbbsres;
    private String getbbsmode;
    private void getBBSList(String m){
    	getbbsmode	= m;
    	getbbsres	= null;
    	// �X���b�h����
    	new Thread(new Runnable(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					@Override
					public void run(){
				    	progressDlg2 = ProgressDialog.show(me, "cLoud BBS�擾��","Please wait...", false);
				    	progressDlg2.show();
					}
				});
				// �X�V
				try {
					SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(me);
					WherePart wp = new WherePart(
						getbbsmode,
						pre.getString("bbs_keyword_preference", ""),
						pre.getString("bbs_level_preference", ""),
						pre.getString("bbs_monster_preference", ""),
						pre.getString("bbs_storategy_preference", ""),
						pre.getString("bbs_weapon_preference", "")
						);
					getbbsres = skd.getBBS(wp);
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.post(new Runnable(){
					@Override
					public void run(){
						progressDlg2.dismiss();
				    	ArrayList<String> lst = new ArrayList<String>();
				    	if( getbbsres != null )
				    		lst = getbbsres.getBodyList();
				    	for(int idx=0; idx<lst.size(); idx++){
				    		BBSItem bi = new BBSItem(lst.get(idx));
				    		// �P���R�[�h
				    		getListItemBBS(bi, idx, list_bbs);
				    	}
				    	adapter_bbs.notifyDataSetChanged();
						if(getbbsres==null)
							Toast.makeText(me, "getBBS����߂�l������܂���", Toast.LENGTH_LONG).show();
						else
							Toast.makeText(me, getbbsres.getHeadValue("message"), Toast.LENGTH_LONG).show();
					}
				});
			}
    	}).start();
    	return;
    }
    private void getListItem(SavedData d, int x, ArrayList<SetItemListLayout> res){
		String type = "�K���i�[";
		if(d.isKenshi())
			type = "���m";
		SetItemListLayout temp = new SetItemListLayout();
		temp.setTextTop((x+1)+": "+d.getTitle()+"("+type+")");
		temp.setTextBottom(StatusText.getSoubiSummaryLine(skillInfo.getData(),d));
		temp.setSrc(d.getAllElementsToString());
		res.add(temp);
    	return;
    }
    private void getListItemBBS(BBSItem data, int x, ArrayList<BBSItemListLayout> res){
    	SavedData d = new SavedData();
    	d.setAllElementsToString(data.soubi);
		BBSItemListLayout temp = new BBSItemListLayout();
		String buf;
		temp.setTextTop(d.getTitle());
		temp.setTextMiddle0(data.keywords);
		temp.setTextMiddle1(data.quest+data.monster+" "+data.storategy);
		temp.setTextMiddle2(data.buki+"("+data.weapon+")");
		temp.setTextMiddle3(data.totaldef+" "+StatusText.getAllTaiseiString(skillInfo.getData(), d, ":", ","));
		temp.setTextMiddle4(data.getSkillsNameList());
		buf = "<font color=\"#ffaa00\">"+data.nickname+"</font> "+data.created+" (id="+data.id+")";
		temp.setTextBottom(Html.fromHtml(buf));
		temp.setSrc(d.getAllElementsToString());
		temp.setBBSItem(data);
		res.add(temp);
    	return;
    }
    public void luncherMessage(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	String[] list = {"����I��", "�h��I��", "��I��", "�J�t�I��",};
    	builder.setTitle("���֘A�A�v���N��");
    	builder.setItems(list, new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if( which == 0 ){
	    			final Intent intent = new Intent(me, BukiActivity.class);
	    	    	startActivity(intent);
				}
				if( which == 1 ){
	    			final Intent intent = new Intent(me, SoubiActivity.class);
	    	    	startActivity(intent);
				}
				if( which == 2 ){
	    			final Intent intent = new Intent(me, SkillActivity.class);
	    	    	startActivity(intent);
				}
				if( which == 3 ){
	    			final Intent intent = new Intent(me, KafuActivity.class);
	    	    	startActivity(intent);
				}
				return;
			}
		});
    	builder.create();
    	builder.show();
    	return;
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
    public void cautionMessage(String t, String msg){
    	TextView tv = new TextView(this);
    	tv.setAutoLinkMask(Linkify.WEB_URLS);
    	tv.setText(msg);
    	ScrollView sv = new ScrollView(this);
    	sv.addView(tv);
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(t);
    	builder.setView(sv);
//    	builder.setMessage(msg);
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
    public void savedMessage(String t, String msg){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(t);
    	builder.setMessage(msg);
    	builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				intent.putExtra("editmode", "save");
				me.setResult(RESULT_OK, intent);
				me.finish();
				return;
			}
		});
    	builder.create();
    	builder.show();
    	return;
    }
    public void noPreMessage(String t, String msg){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(t);
    	builder.setMessage(msg);
    	builder.setPositiveButton("�A�J�E���g�쐬", new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
    			Uri uri0=Uri.parse("https://backdrop-skd.ssl-lolipop.jp/cLoud/exlogin.php");
    			Intent cLoudIntent=new Intent(Intent.ACTION_VIEW,uri0);
    	    	startActivity(cLoudIntent);
				return;
			}
		});
    	builder.setNeutralButton("�A�J�E���g�ݒ�", new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
    			final Intent intentPre = new Intent(me, cLoudPreferences.class);
    	    	startActivity(intentPre);
				return;
			}
		});
    	builder.setNegativeButton("����", new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
    	builder.create();
    	builder.show();
    	return;
    }
	EditText ev;
	String exData;
	Spinner spi_quest;
	Spinner spi_moster;
	Spinner spi_storategy;
	public void showRcvDialog(String data){
		// SavedData�ɕϊ�
		SavedData m = new SavedData();
		m.setAllElementsToString(data);
		// ���C�A�E�g�擾
		LayoutInflater inflater = LayoutInflater.from(me);
		final View skillsDetailView = inflater.inflate(R.layout.skillsdetail, null);
		// �R���{���X�g
		ArrayAdapter<CharSequence> adpt_quest = ArrayAdapter.createFromResource(
			this, R.array.questlevel, android.R.layout.simple_spinner_item);
		adpt_quest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spi_quest = (Spinner) skillsDetailView.findViewById(R.id.sp_quest);
		spi_quest.setAdapter(adpt_quest);
		spi_quest.setPrompt("���x��");
		ArrayAdapter<CharSequence> adpt_monster = ArrayAdapter.createFromResource(
			this, R.array.monster, android.R.layout.simple_spinner_item);
		adpt_monster.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spi_moster = (Spinner) skillsDetailView.findViewById(R.id.sp_monster);
		spi_moster.setAdapter(adpt_monster);
		spi_moster.setPrompt("�Ώۃ����X�^�[");
		ArrayAdapter<CharSequence> adpt_storategy = ArrayAdapter.createFromResource(
			this, R.array.storategy , android.R.layout.simple_spinner_item);
		adpt_storategy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spi_storategy = (Spinner) skillsDetailView.findViewById(R.id.sp_storategy);
		spi_storategy.setAdapter(adpt_storategy);
		spi_storategy.setPrompt("�헪");
		// �A�_�v�^�ݒ�
		TextView detailView = (TextView)skillsDetailView.findViewById(R.id.sdTextView1);
		String str = StatusText.getStatusText(skillInfo.getData(), m, true);
		detailView.setText(Html.fromHtml(str));
		// �f�[�^&�L�[���[�h&�R�����g
		exData = data;
		ev = (EditText)skillsDetailView.findViewById(R.id.editText1);
		// �_�C�A���O�\��
		AlertDialog.Builder builder = new AlertDialog.Builder(me);
		builder.setTitle("cLoud BBS�ɓ��e���܂����H")
		.setCancelable(true)
		.setView(skillsDetailView)
		.setPositiveButton("���e", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
    			putBBS(
    				ev.getText().toString(),
    				(String)spi_quest.getSelectedItem(),
    				(String)spi_moster.getSelectedItem(),
    				(String)spi_storategy.getSelectedItem(),
    				exData);
   				dialog.cancel();
				return;
			}
		})
		.setNegativeButton("����", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
   				dialog.cancel();
				return;
			}
		});
    	builder.show();
    	return;
    }
    private String dataBuffer;
    private int selectedPos;
    private boolean cLoudFlag;
    public void editMessage(String t, int p, String data, boolean flag){
    	selectedPos	= p;
    	dataBuffer	= data;
    	cLoudFlag	= flag;

    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
   		String[] list;
    	builder.setTitle(t);
   		if(clipBoard.equals("")){
   			list = new String[5];
   			list[0]="�ڍו\��"; list[1]="�ҏW"; list[2]="�R�s�["; list[3]="cLoud BBS�֓��e"; list[4]="�P��������";
   	    	builder.setItems(list, new android.content.DialogInterface.OnClickListener(){
   				@Override
   				public void onClick(DialogInterface dialog, int which) {
   					setResult(RESULT_OK);
   					if( which == 0 ){ viewDetail(dataBuffer); }
   					else
   					if( which == 1 ){ editData(cLoudFlag, selectedPos, dataBuffer); }
   					else
   					if( which == 2 ){ copyData(dataBuffer); }
   					else
   					if( which == 3 ){ showRcvDialog(dataBuffer); }
   					else
   					if( which == 4 ){ clearData(selectedPos); }
   					return;
   				}
   			});
   		}
   		else{
   			list = new String[7];
   			list[0]="�ڍו\��"; list[1]="�ҏW"; list[2]="�R�s�["; list[3]="�\�t��"; list[4]="Clipborad�Ɠ���"; list[5]="cLoud BBS�֓��e"; list[6]="�P��������";
   	    	builder.setItems(list, new android.content.DialogInterface.OnClickListener(){
   				@Override
   				public void onClick(DialogInterface dialog, int which) {
   					setResult(RESULT_OK);
   					if( which == 0 ){ viewDetail(dataBuffer); }
   					else
   					if( which == 1 ){ editData(cLoudFlag, selectedPos, dataBuffer); }
   					else
   					if( which == 2 ){ copyData(dataBuffer); }
   					else
   					if( which == 3 ){ pasteData(selectedPos); }
   					else
   					if( which == 4 ){ swapData(dataBuffer, selectedPos); }
   					else
   					if( which == 5 ){ showRcvDialog(dataBuffer); }
   					else
   					if( which == 6 ){ clearData(selectedPos); }
   					return;
   				}
   			});
   		}
    	builder.create();
    	builder.show();
    	return;
    }
    private BBSItem dataBufferBBS;
    public void editMessageBBS(String t, BBSItem item){
    	dataBufferBBS	= item;

    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
   		String[] lst;
    	builder.setTitle(t);
    	lst = new String[3];
   		lst[0]="�ڍו\��"; lst[1]="Clipboard�֕ۑ�"; lst[2]="�u���O�p�[�c����";
   	    builder.setItems(lst, new android.content.DialogInterface.OnClickListener(){
   	    	@Override
   	    	public void onClick(DialogInterface dialog, int which) {
   	    		if( which == 0 ){ viewDetail(dataBufferBBS.soubi); }
   	    		else
   	    		if( which == 1 ){ copyData(dataBufferBBS.soubi); }
   	    		else
   	    		if( which == 2 ){ exportBlogParts(dataBufferBBS); }
   	    		return;
   	    	}
   		});
    	builder.create();
    	builder.show();
    	return;
    }
    private void editData(boolean flag, int pos, String data){
		intent_editdata.putExtra("cloudflag", flag);
		intent_editdata.putExtra("choiceid", pos);
		intent_editdata.putExtra("choiceddata", data);
		me.startActivityForResult(intent_editdata, REQUEST_EDITDATA);
    	return;
    }
    private void copyData(String data){
		clipBoard = data;
		dispClipBoard();
    	return;
    }
    private void pasteData(int pos){
		// �\�t��
		SavedData sd = new SavedData();
		sd.setAllElementsToString(clipBoard);
		clipBoard = "";
		// �ۑ�
		// �N���E�h
		if(cLoudFlag){
			HashMap<Integer, String> d = new HashMap<Integer, String>();
			d.put(pos, sd.getAllElementsToString());
			putStorageList(d);
		}
		// �{��
		else{
			Content content = new Content(me, Content.SAVE_FILE_LIST[pos]);
			content.setSavedData(sd);
			content.save();
		}
        // �ǂݍ��݁��\�[�gwith�v���O���X
   		Thread pdlg = new Thread((SaveDataManagementActivity)me);
   		pdlg.start();
    	return;
    }
    private void swapData(String data, int pos){
		// ����ւ�0
		// �R�s�[
		String temp = clipBoard;
		clipBoard = data;
		dispClipBoard();
		// �\�t��
		SavedData sd = new SavedData();
		sd.setAllElementsToString(temp);
		// �N���E�h
		if(cLoudFlag){
			HashMap<Integer, String> d = new HashMap<Integer, String>();
			d.put(pos, sd.getAllElementsToString());
			putStorageList(d);
		}
		// �{��
		else{
			Content content = new Content(me, Content.SAVE_FILE_LIST[pos]);
			content.setSavedData(sd);
			content.save();
		}
        // �ǂݍ��݁��\�[�gwith�v���O���X
   		Thread pdlg = new Thread((SaveDataManagementActivity)me);
   		pdlg.start();
    	return;
    }
    private void clearData(int pos){
		// �P��������
		SavedData sd = new SavedData();
		// �N���E�h
		if(cLoudFlag){
			HashMap<Integer, String> d = new HashMap<Integer, String>();
			d.put(pos, sd.getAllElementsToString());
			putStorageList(d);
		}
		// �{��
		else{
			Content content = new Content(me, Content.SAVE_FILE_LIST[pos]);
			content.setSavedData(sd);
			content.save();
		}
        // �ǂݍ��݁��\�[�gwith�v���O���X
   		Thread pdlg = new Thread((SaveDataManagementActivity)me);
   		pdlg.start();
    	return;
    }
    private cLoudResult getStorageList(){
    	cLoudResult res = null;
		try {
			res = skd.getStorageList();
			accessres = res;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return res;
    }
    private cLoudResult putstorageres;
    private HashMap<Integer, String> putdata;
    private void putStorageList(HashMap<Integer, String> a){
    	putdata = a;
    	// �X���b�h����
    	new Thread(new Runnable(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					@Override
					public void run(){
				    	progressDlg2 = ProgressDialog.show(me, "cLoud�֏������ݒ�","Please wait...", false);
				    	progressDlg2.show();
					}
				});
				// �X�V
				try {
					putstorageres = skd.putStorageList(putdata);
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.post(new Runnable(){
					@Override
					public void run(){
						progressDlg2.dismiss();
						adapter.notifyDataSetChanged();
						adapter_cLoud.notifyDataSetChanged();
						dispClipBoard();
						if(putstorageres!=null)
							Toast.makeText(me, putstorageres.getHeadValue("message"), Toast.LENGTH_LONG).show();
					}
				});
			}
    	}).start();
    	return;
    }
    private cLoudResult putbbsres;
    private String bbskeyword;
    private String bbsquest;
    private String bbsmonster;
    private String bbsstorategy;
    private String bbsdata;
    private void putBBS(String keyword, String q, String m, String st, String data){
    	bbskeyword		= keyword;
    	bbsquest		= q;
    	bbsmonster		= m;
    	bbsstorategy	= st;
    	bbsdata			= data;
    	putbbsres		= null;
    	// �X���b�h����
    	new Thread(new Runnable(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					@Override
					public void run(){
				    	progressDlg2 = ProgressDialog.show(me, "cLoud BBS�֓��e��","Please wait...", false);
				    	progressDlg2.show();
					}
				});
				// �X�V
				try {
					putbbsres = skd.putBBS(bbskeyword, bbsquest, bbsmonster, bbsstorategy, bbsdata);
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.post(new Runnable(){
					@Override
					public void run(){
						progressDlg2.dismiss();
						if(putbbsres==null)
							Toast.makeText(me, "putBBS����߂�l������܂���", Toast.LENGTH_LONG).show();
						else
							Toast.makeText(me, putbbsres.getHeadValue("message"), Toast.LENGTH_LONG).show();
					}
				});
			}
    	}).start();
    	return;
    }
   	private void initBtn(){
    	Button reloadBtn = (Button) this.findViewById(R.id.btnSD_RELOAD);
    	reloadBtn.setOnClickListener( new android.view.View.OnClickListener(){
			@Override
			public void onClick(View v) {
    			initSigninFlag = true;
		   		Thread pdlg = new Thread((SaveDataManagementActivity)me);
		   		pdlg.start();
				return;
			}
    	});
    	Button newsBtn = (Button) this.findViewById(R.id.btnSD_NEWS);
    	newsBtn.setOnClickListener( new android.view.View.OnClickListener(){
			@Override
			public void onClick(View v) {
    			Uri uri = Uri.parse("http://soubi.bbs.coocan.jp");
    			Intent webIntent=new Intent(Intent.ACTION_VIEW,uri);
    	    	startActivity(webIntent);
				return;
			}
    	});
    	Button statusBtn = (Button) this.findViewById(R.id.btnSD_STATUS);
    	statusBtn.setOnClickListener( new android.view.View.OnClickListener(){
			@Override
			public void onClick(View v) {
				disp_cLoudStatus();
				return;
			}
    	});
    	Button configBtn = (Button) this.findViewById(R.id.btnSD_CONFIG);
    	configBtn.setOnClickListener( new android.view.View.OnClickListener(){
			@Override
			public void onClick(View v) {
    			final Intent intentPre = new Intent(me, cLoudPreferences.class);
    	    	startActivity(intentPre);
				return;
			}
    	});
    	Button infoBtn = (Button) this.findViewById(R.id.btnSD_INFO);
    	infoBtn.setOnClickListener( new android.view.View.OnClickListener(){
			@Override
			public void onClick(View v) {
				infoMessage("���");
				return;
			}
    	});


    	// �f�[�^�ҏW�C���e���g�ݒ�
		intent_editdata=new Intent(this, MainActivity.class);
		intent_cLoudBBS=new Intent(this, cLoudBBSActivity.class);
    	return;
    }
	cLoudResult resStatus = null;
   	private void disp_cLoudStatus(){
   		// �Z�b�V�����m��
		signinres = null;
		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
		String email = pre.getString("email_preference", "");
		String secret = pre.getString("password_preference", "");
		if(email.equals("") || secret.equals("")){
			return;
		}
		try {
			signinres = skd.connect(email, secret);
		}catch (Exception e) {
			e.printStackTrace();
		}
   		//
    	// �X���b�h����
    	new Thread(new Runnable(){
			@Override
			public void run(){
				handler.post(new Runnable(){
					@Override
					public void run() {
						progressDlg2 = ProgressDialog.show(me, "�X�e�[�^�X�擾��","Please wait...", false);
						progressDlg2.show();
						return;
					}
				});
				// �擾
		   		try{
		   			resStatus = skd.getStatus();
				}catch (Exception e) {
					e.printStackTrace();
				}
		   		handler.post(new Runnable(){
					@Override
					public void run() {
						progressDlg2.dismiss();
						//
				   		String nickname	= resStatus.getBodyValue("nickname");
				   		String memberid	= resStatus.getBodyValue("memberid");
				   		String members	= resStatus.getBodyValue("members");
				   		String created	= resStatus.getBodyValue("created");
				   		String posts	= resStatus.getBodyValue("posts");
				   		String given	= resStatus.getBodyValue("given");
				   		String taken	= resStatus.getBodyValue("taken");
				   		// �\��
				   		String msg	=	"�j�b�N�l�[���F"+nickname+"\n"+
									"����ԍ��@�@�F"+memberid+"\n"+
									"�L��������@�F"+members+"\n"+
									"������@�@�@�F"+created+"\n"+
									"���e���@�@�@�F"+posts+"\n"+
									"�^���������@�F"+given+"\n"+
									"�����������@�F"+taken+"\n"
									;
				   		cautionMessage("cLoudBBS�֘A�X�e�[�^�X", msg);
						return;
					}
		   		});
				return;
			}
    	}).start();
   		return;
   	}
    private void setParameters(Bundle extras){
    	return;
    }
    private OnTabChangeListener tabChangeListener = new OnTabChangeListener(){
		@Override
		public void onTabChanged(String tabId) {
			if( currentTab == tabId )
				return;
			currentTab = tabId;
			if (tabId.equals(TAG[0])){
		   		Thread pdlg = new Thread((SaveDataManagementActivity)me);
		   		pdlg.start();
			}
			else if (tabId.equals(TAG[1])){
		   		Thread pdlg = new Thread((SaveDataManagementActivity)me);
		   		pdlg.start();
			}
			else if (tabId.equals(TAG[2])){
		   		Thread pdlg = new Thread((SaveDataManagementActivity)me);
		   		pdlg.start();
			}
			return;
		}
    };
    private void viewDetail(String data){
		// �ڍ׃f�[�^
		SavedData sd = new SavedData();
		sd.setAllElementsToString(data);
		String str = StatusText.getStatusText(skillInfo.getData(), sd, true);
		TextView tv = new TextView(me);
		tv.setText(Html.fromHtml(str));
		ScrollView sv = new ScrollView(me);
	    sv.addView(tv);
	    // �_�C�A���O
		AlertDialog.Builder dlg;
		dlg = new AlertDialog.Builder(me);
		dlg.setTitle("�ڍו\��");
		dlg.setView(sv);
		dlg.create();
		dlg.show();
		return;
    }
    public void touchBBSItem(BBSItem item){
    	editMessageBBS("�f�[�^�Ǘ�", item);
    	return;
    }
    private boolean g_checked;
    private BBSItem g_item;
    public void changeIikamo(boolean checked, BBSItem item){
    	// ���������̕ύX
    	g_checked	= checked;
    	g_item		= item;
    	// �X���b�h����
    	new Thread(new Runnable(){
			@Override
			public void run(){
				handler.post(new Runnable(){
					@Override
					public void run() {
						progressDlg2 = ProgressDialog.show(me, "���������ύX��","Please wait...", false);
						progressDlg2.show();
						return;
					}
				});
				// �ύX
		   		try{
					cLoudResult res = skd.changeIikamo(g_checked, g_item);
				}catch (Exception e) {
					e.printStackTrace();
				}
		   		handler.post(new Runnable(){
					@Override
					public void run() {
						progressDlg2.dismiss();
				   		list_bbs.clear();
				   		me.getBBSList(WebConnection.MODE_GET_BBS_ALL);						
						return;
					}
		   		});
				return;
			}
    	}).start();
    	return;
    }
    public void infoMessage(String t){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	String[] list = {"facebook", "WEB�T�C�g", "���Ђ�����"};
    	builder.setTitle(t);
    	builder.setItems(list, new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if( which == 0 ){
	    			Uri uri0=Uri.parse("http://www.facebook.com/pages/%E8%A3%85%E5%82%99%E7%84%A1%E5%95%8F%E9%A1%8C/235519143170426#!/pages/%E8%A3%85%E5%82%99%E7%84%A1%E5%95%8F%E9%A1%8C/235519143170426");
	    			Intent fbIntent=new Intent(Intent.ACTION_VIEW,uri0);
	    	    	startActivity(fbIntent);
				}
				else
				if( which == 1 ){
	    			Uri uri2=Uri.parse("http://skd.backdrop.jp/noproblem/");
	    			Intent webIntent=new Intent(Intent.ACTION_VIEW,uri2);
	    	    	startActivity(webIntent);
				}
				else
				if( which == 2 ){
	    			Uri uri1=Uri.parse("market://search?q=pub:\"lilca\"");
	    			Intent familyIntent=new Intent(Intent.ACTION_VIEW,uri1);
	    	    	startActivity(familyIntent);
				}
				return;
			}
		});
    	builder.create();
    	builder.show();
    	return;
    }
    public void exportBlogParts(BBSItem item){
		String output =
			"<iframe src=\"http://skd.backdrop.jp"+
			"/mhf/webservice/mhf_external_controller.php?"+
			"itmid="+item.id+"\""+
			"scrolling=\"yes\" width=\"300\" height=\"450\">���Ή��ł�</iframe>";
		this.shareMessage(output);
    	return;
    }
    public void shareMessage(String text){
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, text);
		startActivity(intent);
    	return;
    }
}
