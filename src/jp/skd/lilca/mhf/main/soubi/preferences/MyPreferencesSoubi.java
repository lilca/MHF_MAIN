package jp.skd.lilca.mhf.main.soubi.preferences;

import jp.skd.lilca.mhf.lib.skill_gain.SkillDefines;
import jp.skd.lilca.mhf.main.R;
import jp.skd.lilca.mhf.main.preferences.ComPreferences;
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
import android.widget.ListView;

public class MyPreferencesSoubi extends PreferenceActivity {

	// 外部呼び出しモード関連
//	private boolean externalMode = false;

	// SP
	private boolean sp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        this.addPreferencesFromResource(R.xml.preferences_soubi);
        setSummary(PreferenceManager.getDefaultSharedPreferences(this));
        // 外部呼出し
        Bundle extras = getIntent().getExtras();
        if(extras != null){
//        	externalMode = true;
        	if(extras.getString("mode").equals("easy")){
        		if(extras.getBoolean("sp"))	sp = true;
        		else						sp = false;
        		if(extras.getString("operation").equals("mod"))
        			fixedPreferenceEx();
        		else
        			fixedPreference();
        	}
        }

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
    	inflater.inflate(R.menu.submenu_soubi, menu);
        return true;
    }
    // オプションメニューアイテムが選択された時に呼び出されます
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
			case R.id.item1:
				this.viewMessage("通知","武具条件を初期化クリアしました。");
				this.ClearBukiCond();
				break;
			case R.id.item2:
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
		findPreference("edittext_preference_soubi").setSummary("\""+pre.getString("edittext_preference_soubi", "")+"\"");
		findPreference("list_preference_bougutype_soubi").setSummary(pre.getString("list_preference_bougutype_soubi", "すべて"));
		findPreference("list_preference_soubi").setSummary(pre.getString("list_preference_soubi", "すべて"));
		findPreference("list_preference2_soubi").setSummary(pre.getString("list_preference2_soubi", "剣士"));
		findPreference("list_preference3_soubi").setSummary(pre.getString("list_preference3_soubi", "男"));
		findPreference("list_preference4_soubi").setSummary(pre.getString("list_preference4_soubi", "0"));
		findPreference("list_preference5_soubi").setSummary(pre.getString("list_preference5_soubi", "0"));
		findPreference("list_preference6_soubi").setSummary(pre.getString("list_preference6_soubi", "20"));
		findPreference("list_preference7_soubi").setSummary(pre.getString("list_preference7_soubi", "0"));
		findPreference("ctg_skill_preference01_soubi").setSummary(ComPreferences.getSkills01(pre, ComPreferences.CPF_MODE_SOUBI));
		findPreference("ctg_skill_preference02_soubi").setSummary(ComPreferences.getSkills02(pre, ComPreferences.CPF_MODE_SOUBI));
		findPreference("ctg_skill_preference03_soubi").setSummary(ComPreferences.getSkills03(pre, ComPreferences.CPF_MODE_SOUBI));
		findPreference("ctg_skill_preference04_soubi").setSummary(ComPreferences.getSkills04(pre, ComPreferences.CPF_MODE_SOUBI));
		findPreference("ctg_skill_preference05_soubi").setSummary(ComPreferences.getSkills05(pre, ComPreferences.CPF_MODE_SOUBI));
		findPreference("ctg_skill_preference06_soubi").setSummary(ComPreferences.getSkills06(pre, ComPreferences.CPF_MODE_SOUBI));
		findPreference("ctg_skill_preference07_soubi").setSummary(ComPreferences.getSkills07(pre, ComPreferences.CPF_MODE_SOUBI));
		findPreference("ctg_skill_preference08_soubi").setSummary(ComPreferences.getSkills08(pre, ComPreferences.CPF_MODE_SOUBI));
		findPreference("ctg_skill_preference09_soubi").setSummary(ComPreferences.getSkills09(pre, ComPreferences.CPF_MODE_SOUBI));
		findPreference("ctg_skill_preference10_soubi").setSummary(ComPreferences.getSkills10(pre, ComPreferences.CPF_MODE_SOUBI));
		findPreference("ctg_skill_preference11_soubi").setSummary(ComPreferences.getSkills11(pre, ComPreferences.CPF_MODE_SOUBI));
		findPreference("ctg_skill_preference12_soubi").setSummary(ComPreferences.getSkills12(pre, ComPreferences.CPF_MODE_SOUBI));
		findPreference("ctg_skill_preference13_soubi").setSummary(ComPreferences.getSkills13(pre, ComPreferences.CPF_MODE_SOUBI));
		for(int idx=0; idx<SkillDefines.keyList.length; idx++)
			findPreference(SkillDefines.keyList[idx]+ComPreferences.CPF_MODE_SOUBI).setSummary(pre.getString(SkillDefines.keyList[idx]+ComPreferences.CPF_MODE_SOUBI, "0"));
		reloadPref();
		return;
	}
	private void fixedPreference(){
//		findPreference("list_preference_soubi").setSelectable(false);
//		findPreference("list_preference2_soubi").setSelectable(false);
//		findPreference("list_preference3_soubi").setSelectable(false);
		findPreference("list_preference_soubi").setEnabled(false);
		findPreference("list_preference2_soubi").setEnabled(false);
		findPreference("list_preference3_soubi").setEnabled(false);
		((ListPreference)findPreference("list_preference_bougutype_soubi")).setEntries(R.array.entriesnewtype_list_preference);
		((ListPreference)findPreference("list_preference_bougutype_soubi")).setEntryValues(R.array.entriesnewtype_list_preference);
		reloadPref();
		return;
	}
	private void fixedPreferenceEx(){
		findPreference("list_preference_soubi").setEnabled(false);
		findPreference("list_preference2_soubi").setEnabled(false);
		findPreference("list_preference3_soubi").setEnabled(false);
		findPreference("list_preference4_soubi").setEnabled(false);
		if(sp){
			findPreference("list_preference_bougutype_soubi").setEnabled(false);
			// リスト変更
			((ListPreference)findPreference("list_preference_bougutype_soubi")).setEntries(R.array.entriesnewtype_list_preference);
			((ListPreference)findPreference("list_preference_bougutype_soubi")).setEntryValues(R.array.entriesnewtype_list_preference);
		}
		else{
			((ListPreference)findPreference("list_preference_bougutype_soubi")).setEntries(R.array.entriesnewtypewithoutsp_list_preference);
			((ListPreference)findPreference("list_preference_bougutype_soubi")).setEntryValues(R.array.entriesnewtypewithoutsp_list_preference);
		}
		reloadPref();
		return;
	}
	public void ClearBukiCond(){
		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
		Editor ed = pre.edit();
		if((findPreference("list_preference_bougutype_soubi").isEnabled()))
			ed.putString("list_preference_bougutype_soubi", "すべて");
		if((findPreference("list_preference_soubi").isEnabled()))
			ed.putString("list_preference_soubi", "すべて");
		if((findPreference("edittext_preference_soubi").isEnabled()))
			ed.putString("edittext_preference_soubi", "");
		if((findPreference("list_preference4_soubi").isEnabled()))
			ed.putString("list_preference4_soubi", "0");
		if((findPreference("list_preference5_soubi").isEnabled()))
			ed.putString("list_preference5_soubi", "0");
		if((findPreference("list_preference6_soubi").isEnabled()))
			ed.putString("list_preference6_soubi", "20");
		if((findPreference("list_preference7_soubi").isEnabled()))
			ed.putString("list_preference7_soubi", "0");
		ed.commit();
		
		setSummary(pre);
		return;
	}
	public void ClearSkillCond(){
		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
		Editor ed = pre.edit(); 
		for(int idx=0; idx<SkillDefines.keyList.length; idx++)
			ed.putString(SkillDefines.keyList[idx]+ComPreferences.CPF_MODE_SOUBI, "0");
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
}
