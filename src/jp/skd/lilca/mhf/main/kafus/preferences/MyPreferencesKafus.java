package jp.skd.lilca.mhf.main.kafus.preferences;

import jp.skd.lilca.mhf.lib.skill_gain.SkillDefines;
import jp.skd.lilca.mhf.main.R;
import jp.skd.lilca.mhf.main.preferences.ComPreferences;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

public class MyPreferencesKafus extends PreferenceActivity {

	// 外部呼び出しモード関連
//	private boolean externalMode = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        this.addPreferencesFromResource(R.xml.preferences_kafus);
        setSummary(PreferenceManager.getDefaultSharedPreferences(this));
        // 外部呼出し
        Bundle extras = getIntent().getExtras();
        if(extras != null){
//        	externalMode = true;
        	if(extras.getString("mode").equals("easy")){
        		if(extras.getString("operation").equals("mod"))
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
    	inflater.inflate(R.menu.submenu_kafus, menu);
        return true;
    }
    // オプションメニューアイテムが選択された時に呼び出されます
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
			case R.id.item1:
				this.viewMessage("通知","カフ条件を初期化クリアしました。");
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
		findPreference("edittext_preference_kafus").setSummary("\""+pre.getString("edittext_preference_kafus", "")+"\"");
		findPreference("list_preference4_kafus").setSummary(pre.getString("list_preference4_kafus", "2"));
		findPreference("list_preference7_kafus").setSummary(pre.getString("list_preference7_kafus", "0"));
		findPreference("list_preference8_kafus").setSummary(pre.getString("list_preference8_kafus", "すべて"));
		findPreference("ctg_skill_preference01_kafus").setSummary(ComPreferences.getSkills01(pre, ComPreferences.CPF_MODE_KAFUS));
		findPreference("ctg_skill_preference02_kafus").setSummary(ComPreferences.getSkills02(pre, ComPreferences.CPF_MODE_KAFUS));
		findPreference("ctg_skill_preference03_kafus").setSummary(ComPreferences.getSkills03(pre, ComPreferences.CPF_MODE_KAFUS));
		findPreference("ctg_skill_preference04_kafus").setSummary(ComPreferences.getSkills04(pre, ComPreferences.CPF_MODE_KAFUS));
		findPreference("ctg_skill_preference05_kafus").setSummary(ComPreferences.getSkills05(pre, ComPreferences.CPF_MODE_KAFUS));
		findPreference("ctg_skill_preference06_kafus").setSummary(ComPreferences.getSkills06(pre, ComPreferences.CPF_MODE_KAFUS));
		findPreference("ctg_skill_preference07_kafus").setSummary(ComPreferences.getSkills07(pre, ComPreferences.CPF_MODE_KAFUS));
		findPreference("ctg_skill_preference08_kafus").setSummary(ComPreferences.getSkills08(pre, ComPreferences.CPF_MODE_KAFUS));
		findPreference("ctg_skill_preference09_kafus").setSummary(ComPreferences.getSkills09(pre, ComPreferences.CPF_MODE_KAFUS));
		findPreference("ctg_skill_preference10_kafus").setSummary(ComPreferences.getSkills10(pre, ComPreferences.CPF_MODE_KAFUS));
		findPreference("ctg_skill_preference11_kafus").setSummary(ComPreferences.getSkills11(pre, ComPreferences.CPF_MODE_KAFUS));
		findPreference("ctg_skill_preference12_kafus").setSummary(ComPreferences.getSkills12(pre, ComPreferences.CPF_MODE_KAFUS));
		findPreference("ctg_skill_preference13_kafus").setSummary(ComPreferences.getSkills13(pre, ComPreferences.CPF_MODE_KAFUS));
		for(int idx=0; idx<SkillDefines.keyList.length; idx++)
			findPreference(SkillDefines.keyList[idx]+ComPreferences.CPF_MODE_KAFUS).setSummary(pre.getString(SkillDefines.keyList[idx]+ComPreferences.CPF_MODE_KAFUS, "0"));
		reloadPref();
		return;
	}
	private void fixedPreference(){
//		findPreference("list_preference_kafus").setSelectable(false);
//		findPreference("list_preference2_kafus").setSelectable(false);
//		findPreference("list_preference3_kafus").setSelectable(false);
//		findPreference("list_preference_kafus").setEnabled(false);
		findPreference("list_preference4_kafus").setEnabled(false);
//		findPreference("list_preference3_kafus").setEnabled(false);
		reloadPref();
		return;
	}
	public void ClearBukiCond(){
		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
		Editor ed = pre.edit();
		ed.putString("edittext_preference_kafus", "");
		if((findPreference("list_preference4_kafus").isEnabled()))
			ed.putString("list_preference4_kafus","2");
		ed.putString("list_preference7_kafus", "0");
		ed.putString("list_preference8_kafus", "すべて");
		ed.commit();
		
		setSummary(pre);
		return;
	}
	public void ClearSkillCond(){
		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
		Editor ed = pre.edit(); 
		for(int idx=0; idx<SkillDefines.keyList.length; idx++)
			ed.putString(SkillDefines.keyList[idx]+ComPreferences.CPF_MODE_KAFUS, "0");
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
