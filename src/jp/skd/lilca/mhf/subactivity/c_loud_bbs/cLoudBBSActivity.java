package jp.skd.lilca.mhf.subactivity.c_loud_bbs;

import java.util.ArrayList;

import jp.skd.lilca.mhf.datastruct.BBSItem;
import jp.skd.lilca.mhf.lib.savedata.SavedData;
import jp.skd.lilca.mhf.lib.skill_value_list.StatusText;
import jp.skd.lilca.mhf.main.R;
import jp.skd.lilca.mhf.main.listadapters.c_loud_bbs.BBSItemListAdapter;
import jp.skd.lilca.mhf.main.listadapters.c_loud_bbs.BBSItemListLayout;
import jp.skd.lilca.mhf.main.preferences.cLoudBBSPreferences;
import jp.skd.lilca.mhf.main.preferences.cLoudPreferences;
import jp.skd.lilca.mhf.main.skill_value_list.SkillInfoIO;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class cLoudBBSActivity extends TabActivity implements Runnable{
	//
	public static final int REQ_PRE		= 3001;
	public static final int MAXCOUNT	= 20;
	// �O���f�[�^
	String mode;			// �ҏW���[�h
	int count = 0;
	// �Ăяo�����[�h�֘A
	public Activity me;
	//
	BBSItemListAdapter adapter;			// ����
	BBSItemListAdapter adapter_all;		// �S��
	BBSItemListAdapter cur_adapter;		// ����
	public ArrayList<BBSItemListLayout> list		= new ArrayList<BBSItemListLayout>();	// ����
	public ArrayList<BBSItemListLayout> list_all	= new ArrayList<BBSItemListLayout>();	// �S��
	public ArrayList<BBSItemListLayout> cur_list;	// ����
	String cur_mode = WebConnection.MODE_GET_BBS_ME;
	// �v���O���X�_�C�A���O�p
	Handler handler = new Handler();
	ProgressDialog progressDlg;
	ProgressDialog progressDlg2;
	// �X�L�����
	SkillInfoIO skillInfo;
	// �^�u�֘A
	private static final String TAG[] = {  
		"tagMe", "tagAll", };  
	TabHost tabHost;
	TabHost.TabSpec spec;
	// cLoud
	WebConnection skd;
	// �J�����g�^�u
	String currentTab;
	private static boolean initSigninFlag = true;
	// �C���e���g�N���p�̃��X�i�[
	OnClickListener listenerCondBtn;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_loud_bbs_main);
        // �X���[�v����
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        me = this;
        // �X�L����񐶐�
        this.skillInfo = new SkillInfoIO(this.getResources());
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
        }
		//ListAdapter�ɏ�LList��o�^ 
        adapter		= null;
        adapter_all	= null;
//        adapter		= new BBSItemListAdapter(this, list);
//        adapter_all	= new BBSItemListAdapter(this, list_all);
        ListView listview = (ListView)findViewById(R.id.listView1);
        ListView clistview = (ListView)findViewById(R.id.listView2);
        listview.setAdapter(adapter);
        clistview.setAdapter(adapter_all);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				editMessage("cLoud BBS", list.get(position).getSrc());
				return;
			}
		});
        clistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				editMessage("cLoud BBS", list_all.get(position).getSrc());
				return;
			}
		});
        // �t�H�[�J�X�ݒ�
        cur_list	= list;
        cur_adapter	= adapter;
        // �J�����g�^�u
        currentTab = TAG[0];
        // �N���p�̃��X�i�[��`
        initClickListener();
        // TabHost�ݒ�
        tabHost = getTabHost();
        tabHost.setOnTabChangedListener(tabChangeListener);
        // �e�^�u����
        spec = tabHost.newTabSpec(TAG[0]);
        spec.setIndicator("����");
        spec.setContent(R.id.content1);
        tabHost.addTab(spec);
        spec = tabHost.newTabSpec(TAG[1]);
        spec.setIndicator("�S��");
        spec.setContent(R.id.content2);
        tabHost.addTab(spec);
        // �i�荞�ݐݒ�
        updateCond();
   		// �ǂݍ��݁��\�[�gwith�v���O���X
   		Thread pdlg = new Thread(this);
   		pdlg.start();
		return;
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
       	MenuInflater inflater = getMenuInflater();
       	inflater.inflate(R.menu.c_loud_bbs_menu, menu);
    	return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    		case R.id.menuitem1:
    			initSigninFlag = true;
		   		Thread pdlg = new Thread((cLoudBBSActivity)me);
		   		pdlg.start();
    			break;
    		default:
    			break;
    	}
        return true;
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == REQ_PRE){
			updateCond();
	   		Thread pdlg = new Thread((cLoudBBSActivity)me);
	   		pdlg.start();
		}
		return;
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
    	// ���X�g�m�F
    	if(currentTab.equals(TAG[0])){
    		cur_list	= list;
    		cur_adapter	= adapter;
    		cur_mode	= WebConnection.MODE_GET_BBS_ME;
    	}
    	else
    	if(currentTab.equals(TAG[1])){
    		cur_list	= list_all;
    		cur_adapter	= adapter_all;
    		cur_mode	= WebConnection.MODE_GET_BBS_ALL;
    	}
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
				readDataBase(cur_list, cur_mode);
				handler.post(new Runnable(){
					@Override
					public void run(){
						progressDlg.dismiss();
//						cur_adapter.notifyDataSetChanged();
						if( !initSigninFlag )
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
    cLoudResult signinres;
    cLoudResult accessres;
    //�t�@�C���ǂݍ���
    public void readDataBase(ArrayList<BBSItemListLayout> result, String mode){
    	result.clear();
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
    	getBBSList(mode);
    	return;
    }
    private void getListItem(BBSItem data, int x, ArrayList<BBSItemListLayout> res){
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
		buf = "<font color=\"#ffaa00\">"+data.nickname+"</font> "+data.created;
		temp.setTextBottom(Html.fromHtml(buf));
		temp.setSrc(d.getAllElementsToString());
		res.add(temp);
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
				    		getListItem(bi, idx, cur_list);
				    	}
				    	cur_adapter.notifyDataSetChanged();
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
    private String dataBuffer;
    public void editMessage(String t, String data){
    	dataBuffer	= data;

    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
   		String[] lst;
    	builder.setTitle(t);
    	lst = new String[2];
   		lst[0]="�ڍו\��";	lst[1]="Clipboard�֕ۑ�";
   	    builder.setItems(lst, new android.content.DialogInterface.OnClickListener(){
   	    	@Override
   	    	public void onClick(DialogInterface dialog, int which) {
   	    		if( which == 0 ){
   	    			// �ڍ׃f�[�^
   	    			SavedData sd = new SavedData();
   	    			sd.setAllElementsToString(dataBuffer);
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
   	    		}
   	    		else
   	    		if( which == 1 ){ toClip(dataBuffer); }
   	    		return;
   	    	}
   		});
    	builder.create();
    	builder.show();
    	return;
    }
    private void toClip(String data){
		Intent intent = new Intent();
		intent.putExtra("choiceddata", data);
		me.setResult(RESULT_OK, intent);
		me.finish();
    	return;
    }
    private void initClickListener(){
    	listenerCondBtn = new OnClickListener(){
			@Override
			public void onClick(View v) {
				final Intent intentPre = new Intent(me, cLoudBBSPreferences.class);
				startActivityForResult(intentPre, REQ_PRE);
				return;
			}
    	};
    	Button btn = (Button)findViewById(R.id.btnBBSCond);
    	btn.setOnClickListener(listenerCondBtn);
    	return;
    }
    private void updateCond(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	String buf =
    		"keyword="+pre.getString("bbs_keyword_preference", "")+
    		", �����X�^�[="+pre.getString("bbs_level_preference", "")+
    		pre.getString("bbs_monster_preference", "")+
    		", �헪="+pre.getString("bbs_storategy_preference", "")+
    		", ����="+pre.getString("bbs_weapon_preference", "");
    	TextView tv = (TextView)findViewById(R.id.idbbscond);
    	tv.setText(Html.fromHtml(buf));
    	return;
    }
    private OnTabChangeListener tabChangeListener = new OnTabChangeListener(){
		@Override
		public void onTabChanged(String tabId) {
			if( currentTab == tabId )
				return;
			currentTab = tabId;
			if (tabId.equals(TAG[0])){
		   		Thread pdlg = new Thread((cLoudBBSActivity)me);
		   		pdlg.start();
			}
			else if (tabId.equals(TAG[1])){
		   		Thread pdlg = new Thread((cLoudBBSActivity)me);
		   		pdlg.start();
			}
			return;
		}
    };
}
