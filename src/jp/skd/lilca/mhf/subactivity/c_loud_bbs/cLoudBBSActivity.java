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
	// 外部データ
	String mode;			// 編集モード
	int count = 0;
	// 呼び出しモード関連
	public Activity me;
	//
	BBSItemListAdapter adapter;			// 自分
	BBSItemListAdapter adapter_all;		// 全体
	BBSItemListAdapter cur_adapter;		// 現在
	public ArrayList<BBSItemListLayout> list		= new ArrayList<BBSItemListLayout>();	// 自分
	public ArrayList<BBSItemListLayout> list_all	= new ArrayList<BBSItemListLayout>();	// 全体
	public ArrayList<BBSItemListLayout> cur_list;	// 現在
	String cur_mode = WebConnection.MODE_GET_BBS_ME;
	// プログレスダイアログ用
	Handler handler = new Handler();
	ProgressDialog progressDlg;
	ProgressDialog progressDlg2;
	// スキル情報
	SkillInfoIO skillInfo;
	// タブ関連
	private static final String TAG[] = {  
		"tagMe", "tagAll", };  
	TabHost tabHost;
	TabHost.TabSpec spec;
	// cLoud
	WebConnection skd;
	// カレントタブ
	String currentTab;
	private static boolean initSigninFlag = true;
	// インテント起動用のリスナー
	OnClickListener listenerCondBtn;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_loud_bbs_main);
        // スリープ無効
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        me = this;
        // スキル情報生成
        this.skillInfo = new SkillInfoIO(this.getResources());
        // Web設定
        skd = WebConnection.getInstance();
        // 広告
		MasAdView mad = null;
		mad = (MasAdView) findViewById(R.id.adview);
		mad.setAuid("149624");
		mad.start();
        // 外部呼出し処理
        me = this;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
        }
		//ListAdapterに上記Listを登録 
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
        // フォーカス設定
        cur_list	= list;
        cur_adapter	= adapter;
        // カレントタブ
        currentTab = TAG[0];
        // 起動用のリスナー定義
        initClickListener();
        // TabHost設定
        tabHost = getTabHost();
        tabHost.setOnTabChangedListener(tabChangeListener);
        // 各タブ生成
        spec = tabHost.newTabSpec(TAG[0]);
        spec.setIndicator("自分");
        spec.setContent(R.id.content1);
        tabHost.addTab(spec);
        spec = tabHost.newTabSpec(TAG[1]);
        spec.setIndicator("全体");
        spec.setContent(R.id.content2);
        tabHost.addTab(spec);
        // 絞り込み設定
        updateCond();
   		// 読み込み＆ソートwithプログレス
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
    	// リスト確認
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
    	// スレッド生成
    	new Thread(new Runnable(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					@Override
					public void run(){
				    	progressDlg = ProgressDialog.show(me, "読み込み中","Please wait...", false);
				    	progressDlg.show();
					}
				});
				// 読み出し
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
							Toast.makeText(me, "cLoudにアクセスできませんでした", Toast.LENGTH_LONG).show();
							noPreMessage("cLoudにアクセスできません", "emailまたはpasswordが未設定");
						}
						else{
							Toast.makeText(me, signinres.getHeadValue("message"), Toast.LENGTH_LONG).show();
							if(signinres.getHeadValue("result").equals("ok"))
								initSigninFlag = false;
							else
								noPreMessage("cLoudにアクセスできません", "emailまたはpasswordが未設定");
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
    //ファイル読み込み
    public void readDataBase(ArrayList<BBSItemListLayout> result, String mode){
    	result.clear();
		signinres = null;
		accessres = null;
    	// cLoudアクセス
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
    	// cLoudデータ読み込み
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
    	builder.setPositiveButton("アカウント作成", new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
    			Uri uri0=Uri.parse("https://backdrop-skd.ssl-lolipop.jp/cLoud/exlogin.php");
    			Intent cLoudIntent=new Intent(Intent.ACTION_VIEW,uri0);
    	    	startActivity(cLoudIntent);
				return;
			}
		});
    	builder.setNeutralButton("アカウント設定", new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
    			final Intent intentPre = new Intent(me, cLoudPreferences.class);
    	    	startActivity(intentPre);
				return;
			}
		});
    	builder.setNegativeButton("閉じる", new android.content.DialogInterface.OnClickListener(){
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
    	// スレッド生成
    	new Thread(new Runnable(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					@Override
					public void run(){
				    	progressDlg2 = ProgressDialog.show(me, "cLoud BBS取得中","Please wait...", false);
				    	progressDlg2.show();
					}
				});
				// 更新
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
				    		// １レコード
				    		getListItem(bi, idx, cur_list);
				    	}
				    	cur_adapter.notifyDataSetChanged();
						if(getbbsres==null)
							Toast.makeText(me, "getBBSから戻り値がありません", Toast.LENGTH_LONG).show();
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
   		lst[0]="詳細表示";	lst[1]="Clipboardへ保存";
   	    builder.setItems(lst, new android.content.DialogInterface.OnClickListener(){
   	    	@Override
   	    	public void onClick(DialogInterface dialog, int which) {
   	    		if( which == 0 ){
   	    			// 詳細データ
   	    			SavedData sd = new SavedData();
   	    			sd.setAllElementsToString(dataBuffer);
   	    			String str = StatusText.getStatusText(skillInfo.getData(), sd, true);
   	    			TextView tv = new TextView(me);
   	    			tv.setText(Html.fromHtml(str));
   	    			ScrollView sv = new ScrollView(me);
   	    	    	sv.addView(tv);
   	    	    	// ダイアログ
   	    			AlertDialog.Builder dlg;
   	    			dlg = new AlertDialog.Builder(me);
   	    			dlg.setTitle("詳細表示");
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
    		", モンスター="+pre.getString("bbs_level_preference", "")+
    		pre.getString("bbs_monster_preference", "")+
    		", 戦略="+pre.getString("bbs_storategy_preference", "")+
    		", 武器="+pre.getString("bbs_weapon_preference", "");
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
