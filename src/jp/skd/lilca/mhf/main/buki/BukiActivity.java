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
	// 外部呼び出しモード関連
	private boolean externalMode = false;
	public Activity me;
	private int tmpPos;
	//
	public static final int MAXCOUNT = 1000;
	public static final int REQUEST_CODE = 1;
	ListAdapter adapter;
	//
	String buki		= "片手剣";
	String itemName	= "";
	String skills	= "";
	int slots		= 3;	// 0~3
	int defence		= 0;	// 0~
	int count		= 0;
	int rare		= 0;
	int kaishin		= 0;
	int attack		= 0;
	int bukibai		= 0;
	String attr_type	= "なし";
	String spattr_type	= "なし";
	int reach		= 0;
	int gain		= 0;
	String btype	= "";
	//
	public ArrayList<String> stringList = new ArrayList<String>();
	ArrayList<ListLayout> list = new ArrayList<ListLayout>();
	// プログレスダイアログ用
	Handler handler = new Handler();
	ProgressDialog progressDlg;   /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_buki);
        // 外部呼出し処理
        me = this;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
        	externalMode = true;
        	this.setParameters(extras);
        }
        else{
			this.viewMessage("おしらせ","MHFのスキルシミュレーション（「装備無問題？」）が、メニューの「ごひいきに」からダウンロードできます。是非お試しください。");
        }
        // 広告
		MasAdView mad = null;
		mad = (MasAdView) this.findViewById(R.id.adview);
		mad.setAuid("149627");
		mad.start();
        // ボタン設定
        initBtn();
        // リスト追加
        updateCondition();
		//ListAdapterに上記Listを登録 
        adapter = new ListAdapter(this, list);
        ListView listview = (ListView)this.findViewById(R.id.listView1);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Buki tmp = new Buki(stringList.get(position));
				// 外部起動時
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
				// 外部起動時
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
        // 読み込み＆ソートwithプログレス
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
        	// テキストビューのクリア
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
    // オプションメニューアイテムが選択された時に呼び出されます
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    		case R.id.item1:
    			Uri uri1=Uri.parse("market://search?q=pub:\"lilca\"");
    			Intent familyIntent=new Intent(Intent.ACTION_VIEW,uri1);
    	    	startActivity(familyIntent);
    			break;
    		case R.id.item2:
    			this.viewMessage("おことわり", "本アプリはデータの内容を保証するものではありません。MHF Wikiの配布データを手作業で加工して活用しているので、MHF Wikiに追加または修正された内容は、バージョンアップのタイミングで反映できればいいなぁ、、、、、と思っています。開発者、お金に目がないにつき、広告はじめましたm(_ _)m");
    			break;
    		default:
    			break;
    	}
        return true;
    }
    //ファイル読み込み
    public void readDataBase(ArrayList<ListLayout> result){
		result.clear();
    	String line = null;
    	Buki b;
    	try{
    	// ストリームを開く
    	Resources res = this.getResources();
    	InputStream input = res.openRawResource(R.raw.bukidbs);
    	BufferedReader br = new BufferedReader(new InputStreamReader(input, "Shift_JIS"));
    	ListLayout temp;
    	// ループ前クリア
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
    			str 	= b.getType()+" 攻撃:"+b.getAttack()+
    						"("+b.getBukiBairitsu()+") "+
    						b.getAttrAttackString()+" "+b.getSpAttrAttackString()+
    						", 防: "+Integer.toString(b.getDef())+
    						", 会: "+b.getKaishin()+
    						", レア: "+b.getRare()+
    	    				" <font color=\"aqua\">"+b.getOyakata()+"</font>";
    			String btype = b.getBukiType();
    			if(!btype.equals(""))
    				str += "<font color=\"#ff69b4\">&lt;"+btype+"&gt;</font>";
    			cs = Html.fromHtml(str);
    			temp.setTextBottom(cs);
    			if(b.getType().equals("弓"))
    				temp.setTextRight( Html.fromHtml(b.getBin().printHtml()) );
    			else
    			if(b.getType().equals("ガンランス"))
    				temp.setTextRight( Html.fromHtml(b.getHougeki().print()) );
    			else
    			if(b.getType().equals("狩猟笛"))
    				temp.setTextRight( Html.fromHtml(b.getNeiro().printHtml()) );
    			else
    				temp.setTextRight(b.getReach());
    			result.add(temp);
    			stringList.add(line);
    			count++;
    		}
    	}
    	// ストリームを閉じる
    	br.close();
    	}catch(Exception e){
    		if( line != null )
    			Log.d("mise", line);
    		e.printStackTrace();
    	}

    	return;
    }
    // 一致検査
    public boolean isMatch(String str){
try{
   	Buki b = new Buki(str);
    	// タイプ
		if( !buki.equals("") && !b.getType().equals(buki) )
			return false;
		// SP
		if( btype.equals("ＳＰ") ){
			if( !b.isSP() )
				return false;
		}
		// HC
		else
		if( btype.equals("ＨＣ") ){
			if( !b.isBukiType("HC") )
				return false;
		}
    	// 種類判定
		else
    	if( !btype.equals("") && b.getBukiType().indexOf(btype)==-1 )
   			return false;
    	// 名前
    	if(b.getName().indexOf(itemName)==-1 && !itemName.equals(""))
    		return false;
    	// リーチ
    	if(Buki.getReachScalar(b.getReach()) < reach)
    		return false;
    	// 攻撃
    	if(b.getAttack() < attack)
    		return false;
    	// 武器倍率後
    	if(b.getBukiBairitsu() < bukibai)
    		return false;
    	// 属性攻撃
		if(!attr_type.equals("なし"))
			if(!b.getAttrType().equals(attr_type))
				return false;
    	// 異常属性攻撃
		if(!spattr_type.equals("なし"))
			if(!b.getSpAttrType().equals(spattr_type))
			return false;
    	// 会心率
    	if(b.getKaishin() < kaishin)
    		return false;
    	// スロット
    	if(b.getSlot() < slots)
    		return false;
    	// 防御
    	if(b.getDef() < defence)
    		return false;
    	// レア度
    	if(b.getRare() > rare)
    		return false;
    	// スキル
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
    	if(getPrePart().equals("すべて"))
    		buki = "";
    	else
    		buki = getPrePart();
    	itemName	= getPreName();
    	if(getPreBukiType().equals("すべて"))
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
    // 設定からの戻り
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // 条件表示設定
        updateCondition();
        // 読み込み＆ソートwithプログレス
        Thread pdlg = new Thread(this);
        pdlg.start();
    	return;
    }
    // 条件表示設定
    private void setCondition(){
    	TextView t = (TextView) this.findViewById(R.id.textView1);
       	t.setText(getConditionString());
    	return;
    }
    // 条件文字列取得
    private String getConditionString(){
    	String res = "条件：";
    	if(!getPreBukiType().equals("すべて"))
    		res += ", 種類="+getPreBukiType();
    	if(!getPreType().equals("すべて") && !getPreType().equals(""))
    		res += ", 武器="+getPreType();
    	if(!getPreName().equals(""))
    		res += ", 名称="+getPreName();
    	if(!getPreAttack().equals("0") && !getPreAttack().equals(""))
    		res += ", 攻撃≧"+getPreAttack();
    	if(!getPreBukibai().equals("0") && !getPreBukibai().equals(""))
    		res += ", 武器倍率後≧"+getPreBukibai();
    	if(!getPreAttrType().equals("なし") && !getPreAttrType().equals(""))
    		res += ", 属性="+getPreAttrType();
    	if(!getPreSpAttrType().equals("なし") && !getPreSpAttrType().equals(""))
    		res += ", 異常属性="+getPreSpAttrType();
    	if(!getPreReach().equals("極短") && !getPreRare().equals(""))
    		res += ", リーチ≧"+getPreReach();
    	if(!getPreSlots().equals("0") && !getPreSlots().equals(""))
    		res += ", スロット数≧"+getPreSlots();
    	if(!getPreKaishin().equals("-100") && !getPreRare().equals(""))
    		res += ", 会心率≧"+getPreKaishin();
    	if(!getPreDef().equals("0") && !getPreDef().equals(""))
    		res += ", 防御≧"+getPreDef();
    	if(!getPreRare().equals("20") && !getPreRare().equals(""))
    		res += ", レア度≦"+getPreRare();
    	if(count>=MAXCOUNT)
    		res += " (Hit="+count+" Over)";
    	else
    		res += " (Hit="+count+")";
    	return res;
    }
    // タイプ条件取得
    public String getPreType(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference_buki", "");
    }
    // 性別条件取得
    public String getPreSex(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference3_buki", "");
    }
    // 部位条件取得
    public String getPrePart(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference_buki", "");
    }
    // 名称条件取得
    public String getPreName(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("edittext_preference_buki", "");
    }
    // スロット数条件取得
    public String getPreSlots(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference4_buki", "");
    }
    // 防御条件取得
    public String getPreDef(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference5_buki", "");
    }
    // レア度条件取得
    public String getPreRare(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference6_buki", "");
    }
    // 会心率条件取得
    public String getPreKaishin(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference7_buki", "");
    }
    // 攻撃条件件取得
    public String getPreAttack(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference9_buki", "");
    }
    // 武器倍率後条件件取得
    public String getPreBukibai(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference11_buki", "");
    }
    // 属性攻撃条件件取得
    public String getPreAttrType(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference12_buki", "");
    }
    // 異常属性攻撃条件件取得
    public String getPreSpAttrType(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference13_buki", "");
    }
    // リーチ条件件取得
    public String getPreReach(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference8_buki", "");
    }
    // スキルゲイン条件取得
    public String getPreGain(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference10_buki", "");
    }
    // 武器種類取得
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
    	builder.setPositiveButton("選択", new android.content.DialogInterface.OnClickListener(){
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
    	// スレッド生成
    	new Thread(new Runnable(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					@Override
					public void run(){
				    	progressDlg = ProgressDialog.show(me, "読み込み＆ソート中","Please wait...", false);
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
				        // 条件表示
				        setCondition();
					}
				});
			}
    	}).start();
    	return;
    }
}