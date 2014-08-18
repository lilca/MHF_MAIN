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
	// 外部データ
	int count		= 0;
	// ID
	public final static int REQUEST_EDITDATA	= 2001;
	public final static int REQUEST_cLOUDBBS	= 2002;
	// 呼び出しモード関連
	public SaveDataManagementActivity me;
	//
	SetItemListAdapter adapter;			// 本体
	SetItemListAdapter adapter_cLoud;	// cLoud
	BBSItemListAdapter adapter_bbs;		// BBS
	public ArrayList<SetItemListLayout> list		= new ArrayList<SetItemListLayout>();	// 本体
	public ArrayList<SetItemListLayout> list_cLoud	= new ArrayList<SetItemListLayout>();	// cLoud
	public ArrayList<BBSItemListLayout> list_bbs	= new ArrayList<BBSItemListLayout>();	// BBS
	// プログレスダイアログ用
	Handler handler = new Handler();
	ProgressDialog progressDlg;
	ProgressDialog progressDlg2;
	// スキル情報
	SkillInfoIO skillInfo;
	// クリップボード
	String clipBoard = "";
	TextView clipView;
	// タブ関連
	private static final String TAG[] = {  
		"tagCore", "tagCloud", "tagBBS"};
	TabHost tabHost;
	TabHost.TabSpec spec;
	View tab_v;
	// cLoud
	WebConnection skd;
	// カレントタブ
	String currentTab;
	private static boolean initSigninFlag = true;
	// インテント
	Intent intent_editdata;
	Intent intent_cLoudBBS;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savedatamanagement_main);
        // スリープ無効
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        me = this;
        // スキル情報生成
        this.skillInfo = new SkillInfoIO(this.getResources());
        // cLoud
        this.skd = WebConnection.getInstance();
        this.skd.initialize(this.skillInfo);
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
            setParameters(extras);
        }
        // ボタン設定
        initBtn();
		//ListAdapterに上記Listを登録
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
				editMessage("データ管理", position, list.get(position).getSrc(), false);
				return;
			}
		});
        clistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				editMessage("データ管理", position, list_cLoud.get(position).getSrc(), true);
				return;
			}
		});
        blistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				editMessageBBS("データ管理", list_bbs.get(position).getBBSItem());
				return;
			}
		});
        // カレントタブ
        currentTab = TAG[0];
        // TabHost設定
        tabHost = getTabHost();
        tabHost.setOnTabChangedListener(tabChangeListener);
        // 各タブ生成
        spec = tabHost.newTabSpec(TAG[0]);
        tab_v = new TabButton(this, "本体", R.drawable.clear_sq);
        spec.setIndicator(tab_v);
        spec.setContent(R.id.content1sd);
        tabHost.addTab(spec);
        spec = tabHost.newTabSpec(TAG[1]);
        tab_v = new TabButton(this, "cLoud\nストレージ", R.drawable.clear_sq);
        spec.setIndicator(tab_v);
        spec.setContent(R.id.content2sd);
        tabHost.addTab(spec);
        spec = tabHost.newTabSpec(TAG[2]);
        tab_v = new TabButton(this, "cLoud\nBBS", R.drawable.clear_sq);
        spec.setIndicator(tab_v);
        spec.setContent(R.id.content3sd);
        tabHost.addTab(spec);
        /*spec = tabHost.newTabSpec(TAG[0]);
        spec.setIndicator("本体");
        spec.setContent(R.id.content1);
        tabHost.addTab(spec);
        spec = tabHost.newTabSpec(TAG[1]);
        spec.setIndicator("cLoudストレージ");
        spec.setContent(R.id.content2);
        tabHost.addTab(spec);
        */
        // クリップボードのビューを取得
    	clipView = (TextView) me.findViewById(R.id.textView1);
    	dispClipBoard();
    	String[] msg = MessageData.getMessageAtRandom();
    	cautionMessage(msg[0], msg[1]);
   		// 読み込み＆ソートwithプログレス
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
// cLoudBBS統合により不要
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
// cLoudBBSの統合により不要
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
 			dlg.setTitle("確認");
   			dlg.setMessage("アプリを終了しますか？");
   			dlg.setPositiveButton("はい", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
   			dlg.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					;	// 何もしない
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
    	// onOptionsItemSelectedへ連携するためsuper.onKeyDownを呼ぶ
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
				readDataBase(currentTab, list, list_cLoud, list_bbs);
				handler.post(new Runnable(){
					@Override
					public void run(){
						progressDlg.dismiss();
						adapter.notifyDataSetChanged();
						adapter_cLoud.notifyDataSetChanged();
						dispClipBoard();
						// 最初でない、またはタブが「本体」である
						if( !initSigninFlag || currentTab.equals(TAG[0]))
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
    // クリップボード設定
    private void dispClipBoard(){
    	if( clipBoard.equals("") )
    		clipView.setText("Clipboard = なし");
    	else{
    		SavedData sd = new SavedData();
    		sd.setAllElementsToString(clipBoard);
    		String title = sd.getTitle();
    		String type = "ガンナー";
    		if(sd.isKenshi())
    			type = "剣士";
    		clipView.setText("Clipboard = "+title+"("+type+")"+"\n"+
        		"攻: "+StatusText.getAllAtk(skillInfo.getData(), sd)+"  "+
        		"防: "+StatusText.getAllDef(skillInfo.getData(), sd)+"  "+
        		StatusText.getAllTaiseiString(skillInfo.getData(), sd, ":", ",")
        	);
    	}
    	return;
    }
    cLoudResult signinres;
    cLoudResult accessres;
    //ファイル読み込み
    public void readDataBase(String tab,	ArrayList<SetItemListLayout> result,
    										ArrayList<SetItemListLayout> cresult,
    										ArrayList<BBSItemListLayout> bresult){
    	if(tab.equals(TAG[0])){
    		result.clear();
    		// 内部データ読み込み
    		for(int idx=0; idx<Content.SAVE_FILE_LIST.length; idx++){
    			Content content = new Content(this, Content.SAVE_FILE_LIST[idx]);
    			content.read();
    			SavedData data = content.getSavedData();
// 抜き取り用
String dummy = data.getAllElementsToString();
    			// １レコード
    			getListItem(data, idx, result);
    		}
    	}
    	else
    	if(tab.equals(TAG[1])){
    		// cLoudアクセス
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
    		// cLoudデータ読み込み
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
    			// １レコード
    			getListItem(data, idx, cresult);
    		}
    	}
    	else
    	if(tab.equals(TAG[2])){
        	bresult.clear();
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
        	getBBSList(WebConnection.MODE_GET_BBS_ALL);				// 今は固定
    	}
    	return;
    }
    private cLoudResult delbbsres;
    private String itemid;
    public void delBBSItem(String id){
    	itemid	= id;
    	// スレッド生成
    	new Thread(new Runnable(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					@Override
					public void run(){
				    	progressDlg2 = ProgressDialog.show(me, "cLoud 削除中","Please wait...", false);
				    	progressDlg2.show();
					}
				});
				// 削除
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
							Toast.makeText(me, "delBBSItemから戻り値がありません", Toast.LENGTH_LONG).show();
						else
							Toast.makeText(me, delbbsres.getHeadValue("message"), Toast.LENGTH_LONG).show();
				    	// リフレッシュ
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
				    		getListItemBBS(bi, idx, list_bbs);
				    	}
				    	adapter_bbs.notifyDataSetChanged();
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
    private void getListItem(SavedData d, int x, ArrayList<SetItemListLayout> res){
		String type = "ガンナー";
		if(d.isKenshi())
			type = "剣士";
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
    	String[] list = {"武器選択", "防具選択", "珠選択", "カフ選択",};
    	builder.setTitle("旧関連アプリ起動");
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
	EditText ev;
	String exData;
	Spinner spi_quest;
	Spinner spi_moster;
	Spinner spi_storategy;
	public void showRcvDialog(String data){
		// SavedDataに変換
		SavedData m = new SavedData();
		m.setAllElementsToString(data);
		// レイアウト取得
		LayoutInflater inflater = LayoutInflater.from(me);
		final View skillsDetailView = inflater.inflate(R.layout.skillsdetail, null);
		// コンボリスト
		ArrayAdapter<CharSequence> adpt_quest = ArrayAdapter.createFromResource(
			this, R.array.questlevel, android.R.layout.simple_spinner_item);
		adpt_quest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spi_quest = (Spinner) skillsDetailView.findViewById(R.id.sp_quest);
		spi_quest.setAdapter(adpt_quest);
		spi_quest.setPrompt("レベル");
		ArrayAdapter<CharSequence> adpt_monster = ArrayAdapter.createFromResource(
			this, R.array.monster, android.R.layout.simple_spinner_item);
		adpt_monster.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spi_moster = (Spinner) skillsDetailView.findViewById(R.id.sp_monster);
		spi_moster.setAdapter(adpt_monster);
		spi_moster.setPrompt("対象モンスター");
		ArrayAdapter<CharSequence> adpt_storategy = ArrayAdapter.createFromResource(
			this, R.array.storategy , android.R.layout.simple_spinner_item);
		adpt_storategy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spi_storategy = (Spinner) skillsDetailView.findViewById(R.id.sp_storategy);
		spi_storategy.setAdapter(adpt_storategy);
		spi_storategy.setPrompt("戦略");
		// アダプタ設定
		TextView detailView = (TextView)skillsDetailView.findViewById(R.id.sdTextView1);
		String str = StatusText.getStatusText(skillInfo.getData(), m, true);
		detailView.setText(Html.fromHtml(str));
		// データ&キーワード&コメント
		exData = data;
		ev = (EditText)skillsDetailView.findViewById(R.id.editText1);
		// ダイアログ表示
		AlertDialog.Builder builder = new AlertDialog.Builder(me);
		builder.setTitle("cLoud BBSに投稿しますか？")
		.setCancelable(true)
		.setView(skillsDetailView)
		.setPositiveButton("投稿", new DialogInterface.OnClickListener(){
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
		.setNegativeButton("閉じる", new DialogInterface.OnClickListener(){
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
   			list[0]="詳細表示"; list[1]="編集"; list[2]="コピー"; list[3]="cLoud BBSへ投稿"; list[4]="１件初期化";
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
   			list[0]="詳細表示"; list[1]="編集"; list[2]="コピー"; list[3]="貼付け"; list[4]="Clipboradと入替"; list[5]="cLoud BBSへ投稿"; list[6]="１件初期化";
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
   		lst[0]="詳細表示"; lst[1]="Clipboardへ保存"; lst[2]="ブログパーツ生成";
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
		// 貼付け
		SavedData sd = new SavedData();
		sd.setAllElementsToString(clipBoard);
		clipBoard = "";
		// 保存
		// クラウド
		if(cLoudFlag){
			HashMap<Integer, String> d = new HashMap<Integer, String>();
			d.put(pos, sd.getAllElementsToString());
			putStorageList(d);
		}
		// 本体
		else{
			Content content = new Content(me, Content.SAVE_FILE_LIST[pos]);
			content.setSavedData(sd);
			content.save();
		}
        // 読み込み＆ソートwithプログレス
   		Thread pdlg = new Thread((SaveDataManagementActivity)me);
   		pdlg.start();
    	return;
    }
    private void swapData(String data, int pos){
		// 入れ替え0
		// コピー
		String temp = clipBoard;
		clipBoard = data;
		dispClipBoard();
		// 貼付け
		SavedData sd = new SavedData();
		sd.setAllElementsToString(temp);
		// クラウド
		if(cLoudFlag){
			HashMap<Integer, String> d = new HashMap<Integer, String>();
			d.put(pos, sd.getAllElementsToString());
			putStorageList(d);
		}
		// 本体
		else{
			Content content = new Content(me, Content.SAVE_FILE_LIST[pos]);
			content.setSavedData(sd);
			content.save();
		}
        // 読み込み＆ソートwithプログレス
   		Thread pdlg = new Thread((SaveDataManagementActivity)me);
   		pdlg.start();
    	return;
    }
    private void clearData(int pos){
		// １件初期化
		SavedData sd = new SavedData();
		// クラウド
		if(cLoudFlag){
			HashMap<Integer, String> d = new HashMap<Integer, String>();
			d.put(pos, sd.getAllElementsToString());
			putStorageList(d);
		}
		// 本体
		else{
			Content content = new Content(me, Content.SAVE_FILE_LIST[pos]);
			content.setSavedData(sd);
			content.save();
		}
        // 読み込み＆ソートwithプログレス
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
    	// スレッド生成
    	new Thread(new Runnable(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					@Override
					public void run(){
				    	progressDlg2 = ProgressDialog.show(me, "cLoudへ書き込み中","Please wait...", false);
				    	progressDlg2.show();
					}
				});
				// 更新
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
    	// スレッド生成
    	new Thread(new Runnable(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					@Override
					public void run(){
				    	progressDlg2 = ProgressDialog.show(me, "cLoud BBSへ投稿中","Please wait...", false);
				    	progressDlg2.show();
					}
				});
				// 更新
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
							Toast.makeText(me, "putBBSから戻り値がありません", Toast.LENGTH_LONG).show();
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
				infoMessage("情報");
				return;
			}
    	});


    	// データ編集インテント設定
		intent_editdata=new Intent(this, MainActivity.class);
		intent_cLoudBBS=new Intent(this, cLoudBBSActivity.class);
    	return;
    }
	cLoudResult resStatus = null;
   	private void disp_cLoudStatus(){
   		// セッション確立
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
    	// スレッド生成
    	new Thread(new Runnable(){
			@Override
			public void run(){
				handler.post(new Runnable(){
					@Override
					public void run() {
						progressDlg2 = ProgressDialog.show(me, "ステータス取得中","Please wait...", false);
						progressDlg2.show();
						return;
					}
				});
				// 取得
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
				   		// 表示
				   		String msg	=	"ニックネーム："+nickname+"\n"+
									"会員番号　　："+memberid+"\n"+
									"有効会員数　："+members+"\n"+
									"入会日　　　："+created+"\n"+
									"投稿数　　　："+posts+"\n"+
									"与いいかも　："+given+"\n"+
									"授いいかも　："+taken+"\n"
									;
				   		cautionMessage("cLoudBBS関連ステータス", msg);
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
		// 詳細データ
		SavedData sd = new SavedData();
		sd.setAllElementsToString(data);
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
		return;
    }
    public void touchBBSItem(BBSItem item){
    	editMessageBBS("データ管理", item);
    	return;
    }
    private boolean g_checked;
    private BBSItem g_item;
    public void changeIikamo(boolean checked, BBSItem item){
    	// いいかもの変更
    	g_checked	= checked;
    	g_item		= item;
    	// スレッド生成
    	new Thread(new Runnable(){
			@Override
			public void run(){
				handler.post(new Runnable(){
					@Override
					public void run() {
						progressDlg2 = ProgressDialog.show(me, "いいかも変更中","Please wait...", false);
						progressDlg2.show();
						return;
					}
				});
				// 変更
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
    	String[] list = {"facebook", "WEBサイト", "ごひいきに"};
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
			"scrolling=\"yes\" width=\"300\" height=\"450\">未対応です</iframe>";
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
