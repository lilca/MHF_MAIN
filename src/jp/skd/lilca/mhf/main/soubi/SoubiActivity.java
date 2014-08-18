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
	// 外部呼び出しモード関連
	private boolean externalMode = false;
	public Activity me;
	private int tmpPos;
	//
	public static final int MAXCOUNT = 10000;
	public static final int REQUEST_CODE = 1;
	ListAdapter adapter;
	//
	String bui		= "頭";
	int type		= 0;	// 0:剣士，1:ガンナー
	boolean male	= true;	// true:男キャラ　false:女キャラ
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
	// プログレスダイアログ用
	Handler handler = new Handler();
	ProgressDialog progressDlg;
   /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_soubi);
        // 外部呼出し処理
        me = this;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
        	externalMode = true;
        	this.operation = extras.getString("operation");
        	if(this.operation==null)
        		this.operation = "";
        	this.setParameters(extras);
        	/*-- ルシフェル対応 --*/
        	if(extras.getString("owner is lucifer")!=null){
        		getMostItem(extras);
        		return;
        	}
        	/*-- ルシフェル対応end --*/
        }
        else{
			this.viewMessage("おしらせ","MHFのスキルシミュレーション（「装備無問題？」）が、メニューの「ごひいきに」からダウンロードできます。是非お試しください。");
        }
        // 広告
		MasAdView mad = null;
		mad = (MasAdView) findViewById(R.id.adview);
		mad.setAuid("149622");
		mad.start();
        // ボタン設定
        initBtn();
        // リスト追加
        updateCondition();
		//ListAdapterに上記Listを登録 
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
				// 外部起動時
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
				// 外部起動時
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
    	Bougu b;
    	try{
    	// ストリームを開く
    	Resources res = this.getResources();
    	InputStream input = res.openRawResource(R.raw.bougudbs);
    	BufferedReader br = new BufferedReader(new InputStreamReader(input, "Shift_JIS"));
    	ListLayout temp;
    	// ループ前クリア
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
    			String str = b.getSkillsWithColorTag("#CC0000")+" Lv"+b.getLevel()+", 防: "+Integer.toString(b.getDef())+" （レア"+b.getRare()+"）";
    			if(!btype.equals(""))
    				str += "<font color=\"#ff69b4\">&lt;"+btype+"防具&gt;</font>";
    			CharSequence cs = Html.fromHtml(str);
    			temp.setTextBottom(cs);
    			temp.setTextRight(Integer.toString(SkillGain.getSkillGain(b.getSkills(), skills)));
    			temp.setSrc(line);
    			result.add(temp);
    			count++;
    		}
    	}
    	// ソート
		Collections.sort(result, new LayoutComparator());
    	// ストリームを閉じる
    	br.close();
    	}catch(Exception e){
    		if(line != null)
    			Log.d("mise", line);
    		e.printStackTrace();
    	}

    	return;
    }
    // 一致検査
    public boolean isMatch(String str){
try{
    	Bougu b = new Bougu(str);
    	// Bouguを使ってない旧ロジック用
    	//CsvStringToList cstl = new CsvStringToList();
    	//String[] fields = cstl.split(str, ",");
    	// タイプ
    	//if( !((type==0 && fields[2].equals("1")) || (type==1 && fields[3].equals("1"))) )
    	if( !((type==0 && b.isKenshi()) || (type==1 && b.isGunner())) )
    		return false;
    	// 性別
    	if( male != b.canMale() && !male != b.canFemale() )
    		return false;
    	// 部位確認
    	//if(!fields[0].equals(bui) && !bui.equals(""))
    	if(!b.isBui(bui) && !bui.equals(""))
    		return false;
    	// SP判定
    	if(bouguType.equals(""));
    	else
    	if(bouguType.equals("ＳＰ") && !b.isSP()) return false;
    	else
    	if(bouguType.equals("ＨＣ") && !b.isBouguType("ＨＣ")) return false;
    	else
       	if(bouguType.equals("狩護") && !b.isBouguType("狩護")) return false;
    	else
    	if(bouguType.equals("剛種") && !b.isBouguType("剛種")) return false;
    	else
       	if(bouguType.equals("天嵐") && !b.isBouguType("天嵐")) return false;
    	else
       	if(bouguType.equals("覇種") && !b.isBouguType("覇種")) return false;
    	else
       	if(bouguType.equals("秘伝") && !b.isBouguType("秘伝")) return false;
    	else
       	if(bouguType.equals("Ｇ級") && !b.isBouguType("Ｇ級")) return false;
       	else
       	if(bouguType.equals("Ｇ狩") && !b.isBouguType("Ｇ狩")) return false;
    	
    	// 名前
    	if(b.getName().indexOf(itemName)==-1 && !itemName.equals(""))
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
    	// 部位
    	if(getPrePart().equals("すべて"))
    		bui = "";
    	else
    		bui = getPrePart();
    	// 背景変更
		View v = findViewById(R.id.linearLayout0);
    	if( bui.equals("") )
    		v.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.soubi_all_bg));
    	else
   		if( bui.equals("頭") )
    		v.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.soubi_atama_bg));
    	else
   		if( bui.equals("胴") )
    		v.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.soubi_dou_bg));
    	else
   		if( bui.equals("腕") )
    		v.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.soubi_ude_bg));
    	else
   		if( bui.equals("腰") )
    		v.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.soubi_koshi_bg));
    	else
   		if( bui.equals("脚") )
    		v.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.soubi_ashi_bg));
    	// タイプ
    	if(getPreType().equals("剣士"))
    		type = 0;	// 0:剣士，1:ガンナー
    	else
    		type = 1;
    	// キャラ性別
    	if(getPreGender().equals("男"))
    		male = true;
    	else
    		male = false;
    	// 名称
    	itemName	= getPreName();
    	// スキル
    	skills		= getPreSkillsSelection();
    	// 防具タイプ
    	if(getPreBouguType().equals("すべて"))
    		bouguType = "";
    	else
    		bouguType = getPreBouguType();
   		// スロット
    	try{
    		slots = Integer.parseInt(getPreSlots());	// 0~3
    	}catch(Exception e){
    		slots = -1;
    	}
    	// 防御力
    	try{
    		defence = Integer.parseInt(getPreDef());	// 0~
    	}catch(Exception e){
    		defence = -1;
    	}
    	// レア度
    	try{
    		rare = Integer.parseInt(getPreRare());	// 0~
    	}catch(Exception e){
    		rare = -1;
    	}
    	// スキルゲイン
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
    	TextView t = (TextView) findViewById(R.id.textView1);
       	t.setText(getConditionString());
    	return;
    }
    // 条件文字列取得
    private String getConditionString(){
    	String res = "条件：";
    	res += "タイプ="+getPreType()+", 性別="+getPreGender()+", 部位="+getPrePart();
    	if(!getPreBouguType().equals("すべて"))
    		res += ", 種類="+getPreBouguType();
    	if(!getPreName().equals(""))
    		res += ", 名称="+getPreName();
    	if(!getPreSlots().equals("0") && !getPreSlots().equals(""))
    		res += ", スロット数≧"+getPreSlots();
    	if(!getPreDef().equals("0") && !getPreDef().equals(""))
    		res += ", 防御≧"+getPreDef();
    	if(!getPreRare().equals("20") && !getPreRare().equals(""))
    		res += ", レア度≦"+getPreRare();
    	if(!getPreGain().equals("0") && !getPreGain().equals(""))
    		res += ", スキルゲイン≧"+getPreGain();
    	if(!getPreSkillsSelection().equals(""))
    		res += ", スキル≧"+getPreSkillsSelection();
    	if(count>=MAXCOUNT)
    		res += " (Hit="+count+" Over)";
    	else
    		res += " (Hit="+count+")";
    	return res;
    }
    // タイプ条件取得
    public String getPreType(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference2_soubi", "");
    }
    // 性別条件取得
    public String getPreGender(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference3_soubi", "");
    }
    // 部位条件取得
    public String getPrePart(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference_soubi", "");
    }
    // 名称条件取得
    public String getPreName(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("edittext_preference_soubi", "");
    }
    // スロット数条件取得
    public String getPreSlots(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference4_soubi", "");
    }
    // 防御条件取得
    public String getPreDef(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference5_soubi", "");
    }
    // レア度条件取得
    public String getPreRare(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference6_soubi", "");
    }
    // スキルゲイン条件取得
    public String getPreGain(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference7_soubi", "");
    }
    // SP条件取得
    public String getPreBouguType(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	return pre.getString("list_preference_bougutype_soubi", "");
    }
    // スキル選択条件取得
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
    	builder.setPositiveButton("選択", new android.content.DialogInterface.OnClickListener(){
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
			ed.putString("list_preference_bougutype_soubi", "ＳＰ");
			sp = true;
		}
		else{
			ed.putString("list_preference_bougutype_soubi", "すべて");
			sp = false;
		}
		ed.putString("edittext_preference_soubi", "");
		ed.putString("list_preference4_soubi", extras.getString("slots"));
		ed.putString("list_preference5_soubi", "0");
		ed.putString("list_preference6_soubi", "20");
		ed.putString("list_preference7_soubi", "1");
		// スキル
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
    	// 部位
   		bui = bdl.getString("parts");
    	// タイプ
    	if(bdl.getString("type").equals("剣士"))
    		type = 0;	// 0:剣士，1:ガンナー
    	else
    		type = 1;
    	// キャラ性別
    	if(bdl.getString("sex").equals("男"))
    		male = true;
    	else
    		male = false;
    	// 名称
    	itemName	= "";
    	// スキル
    	skills		= bdl.getString("skills");
    	// SP
    	if(bdl.getString("sp").equals("true"))
    		bouguType	= "ＳＰ";
    	else
    		bouguType	= "";
   		// スロット
   		slots = Integer.parseInt(bdl.getString("slots"));
    	// 防御力
   		defence = 0;
    	// レア度
   		rare = 20;
    	// スキルゲイン
   		gain = 1;

   		// 一番いいやつ検索
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
				        // 条件表示設定
				        setCondition();
					}
				});
			}
    	}).start();
    	return;
    }
}