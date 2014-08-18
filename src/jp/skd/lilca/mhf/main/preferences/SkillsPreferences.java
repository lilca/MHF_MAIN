package jp.skd.lilca.mhf.main.preferences;

import java.util.ArrayList;

import jp.skd.lilca.mhf.lib.skill_gain.SkillDefines;
import jp.skd.lilca.mhf.lib.tools.CsvStringToList;
import jp.skd.lilca.mhf.main.R;
import jp.skd.lilca.mhf.main.skill_value_list.SkillInfoIO;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;

public class SkillsPreferences extends PreferenceActivity {
	// 外部呼び出しモード関連
//	private boolean externalMode = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        // スリープ無効
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.addPreferencesFromResource(R.xml.skillpreferences);
        setSummary(PreferenceManager.getDefaultSharedPreferences(this));
        // 外部呼出し
        Bundle extras = getIntent().getExtras();
        if(extras != null){
//        	externalMode = true;
        	if(extras.getString("mode").equals("easy"))
        		fixedPreference();
        }
        // リストの値設定
        setListValue();
        return;
    }
    @Override
    protected void onResume(){
    	super.onResume();
    	getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
    	return;
    }
    @Override
    protected void onPause(){
    	super.onPause();
    	getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
    	return;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.submenu, menu);
        return true;
    }
    // オプションメニューアイテムが選択された時に呼び出されます
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
			case R.id.item1:
	   			this.viewMessage("通知","スキル条件を初期化クリアしました。");
	  			this.ClearSkillCond();
				break;
    		default:
    			break;
    	}
        return true;
    }
    private SharedPreferences.OnSharedPreferenceChangeListener listener =
    	new SharedPreferences.OnSharedPreferenceChangeListener() {
			
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
					String key) {
				setSummary(sharedPreferences);
    			return;
			}
		};
	private void reloadPref(){
		ListView v = this.getListView();  
		v.invalidateViews();
		return;
	}
	private void setSummary(SharedPreferences pre){
		findPreference("ctg_skill_preference01").setSummary(ComPreferences.getSkills01(pre, ComPreferences.CPF_MODE_MAIN));
		findPreference("ctg_skill_preference02").setSummary(ComPreferences.getSkills02(pre, ComPreferences.CPF_MODE_MAIN));
		findPreference("ctg_skill_preference03").setSummary(ComPreferences.getSkills03(pre, ComPreferences.CPF_MODE_MAIN));
		findPreference("ctg_skill_preference04").setSummary(ComPreferences.getSkills04(pre, ComPreferences.CPF_MODE_MAIN));
		findPreference("ctg_skill_preference05").setSummary(ComPreferences.getSkills05(pre, ComPreferences.CPF_MODE_MAIN));
		findPreference("ctg_skill_preference06").setSummary(ComPreferences.getSkills06(pre, ComPreferences.CPF_MODE_MAIN));
		findPreference("ctg_skill_preference07").setSummary(ComPreferences.getSkills07(pre, ComPreferences.CPF_MODE_MAIN));
		findPreference("ctg_skill_preference08").setSummary(ComPreferences.getSkills08(pre, ComPreferences.CPF_MODE_MAIN));
		findPreference("ctg_skill_preference09").setSummary(ComPreferences.getSkills09(pre, ComPreferences.CPF_MODE_MAIN));
		findPreference("ctg_skill_preference10").setSummary(ComPreferences.getSkills10(pre, ComPreferences.CPF_MODE_MAIN));
		findPreference("ctg_skill_preference11").setSummary(ComPreferences.getSkills11(pre, ComPreferences.CPF_MODE_MAIN));
		findPreference("ctg_skill_preference12").setSummary(ComPreferences.getSkills12(pre, ComPreferences.CPF_MODE_MAIN));
		findPreference("ctg_skill_preference13").setSummary(ComPreferences.getSkills13(pre, ComPreferences.CPF_MODE_MAIN));
		for(int idx=0; idx<SkillDefines.keyList.length; idx++)
			findPreference(SkillDefines.keyList[idx]).setSummary(pre.getString(SkillDefines.keyList[idx]+ComPreferences.CPF_MODE_MAIN, "0"));
		reloadPref();
		return;
	}
	private void fixedPreference(){
		reloadPref();
		return;
	}
	public void ClearSkillCond(){
		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
		Editor ed = pre.edit(); 
		for(int idx=0; idx<SkillDefines.keyList.length; idx++)
			ed.putString(SkillDefines.keyList[idx]+ComPreferences.CPF_MODE_MAIN, "");
		ed.commit();
		
		setSummary(pre);
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
    private void setListValue(){
    	// 初期化
		ArrayList<String> resLabel = new ArrayList<String>();
		ArrayList<String> resValue = new ArrayList<String>();
		ArrayList<String> doubleList = new ArrayList<String>();
		// スキル情報読込み
		ArrayList<String> si = new SkillInfoIO(this.getResources()).getSkillList();
    	// リスト値の設定
		for(int idx=0; idx<SkillDefines.keyList.length; idx++){
			// リストのクリア
			resLabel.clear();
			resValue.clear();
			// 項目の取得
			ListPreference lv = (ListPreference)findPreference(SkillDefines.keyList[idx]+ComPreferences.CPF_MODE_MAIN);
			//　項目名（タイトル）取得
			String title = (String) lv.getTitle();
			// 
			for(int jdx=0; jdx<si.size(); jdx++){
				String[] info = CsvStringToList.split(si.get(jdx), ",");
				// 2重のとき
        		if( info[1].equals("2重") ){
        			doubleList.add(si.get(jdx));
        		}
        		else
				//
				if( title.equals(info[0]) ){
					resValue.add(info[2]);
					resLabel.add(info[2]);
				}
			}
			resValue.add("");
			resLabel.add("なし");
			lv.setEntries((String[])resLabel.toArray(new String[0]));
			lv.setEntryValues((String[])resValue.toArray(new String[0]));
		}
    	return;
    }
}
