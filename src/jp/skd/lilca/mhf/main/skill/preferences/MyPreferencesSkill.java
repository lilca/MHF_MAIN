package jp.skd.lilca.mhf.main.skill.preferences;

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

public class MyPreferencesSkill extends PreferenceActivity {

	// 外部呼び出しモード関連
//	private boolean externalMode = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        this.addPreferencesFromResource(R.xml.preferences_skill);
        setSummary(PreferenceManager.getDefaultSharedPreferences(this));
        // 外部呼出し
        Bundle extras = getIntent().getExtras();
        if(extras != null){
//        	externalMode = true;
        	if(extras.getString("mode").equals("easy"))
        		fixedPreference();
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
    	inflater.inflate(R.menu.submenu_skill, menu);
        return true;
    }
    // オプションメニューアイテムが選択された時に呼び出されます
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
			case R.id.item1:
				this.viewMessage("通知","スロット条件を初期化クリアしました。");
				this.ClearSlotCond();
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
		findPreference("edittext_preference_skill").setSummary("\""+pre.getString("edittext_preference_skill", "")+"\"");
		findPreference("checkbox_preference_skill").setSummary(Boolean.toString(pre.getBoolean("checkbox_preference_skill", false)));
		findPreference("list_preference4_skill").setSummary(pre.getString("list_preference4_skill", "0"));
		findPreference("list_preference7_skill").setSummary(pre.getString("list_preference7_skill", "0"));
		findPreference("ctg_skill_preference01_skill").setSummary(ComPreferences.getSkills01(pre, ComPreferences.CPF_MODE_SKILL));
		findPreference("ctg_skill_preference02_skill").setSummary(ComPreferences.getSkills02(pre, ComPreferences.CPF_MODE_SKILL));
		findPreference("ctg_skill_preference03_skill").setSummary(ComPreferences.getSkills03(pre, ComPreferences.CPF_MODE_SKILL));
		findPreference("ctg_skill_preference04_skill").setSummary(ComPreferences.getSkills04(pre, ComPreferences.CPF_MODE_SKILL));
		findPreference("ctg_skill_preference05_skill").setSummary(ComPreferences.getSkills05(pre, ComPreferences.CPF_MODE_SKILL));
		findPreference("ctg_skill_preference06_skill").setSummary(ComPreferences.getSkills06(pre, ComPreferences.CPF_MODE_SKILL));
		findPreference("ctg_skill_preference07_skill").setSummary(ComPreferences.getSkills07(pre, ComPreferences.CPF_MODE_SKILL));
		findPreference("ctg_skill_preference08_skill").setSummary(ComPreferences.getSkills08(pre, ComPreferences.CPF_MODE_SKILL));
		findPreference("ctg_skill_preference09_skill").setSummary(ComPreferences.getSkills09(pre, ComPreferences.CPF_MODE_SKILL));
		findPreference("ctg_skill_preference10_skill").setSummary(ComPreferences.getSkills10(pre, ComPreferences.CPF_MODE_SKILL));
		findPreference("ctg_skill_preference11_skill").setSummary(ComPreferences.getSkills11(pre, ComPreferences.CPF_MODE_SKILL));
		findPreference("ctg_skill_preference12_skill").setSummary(ComPreferences.getSkills12(pre, ComPreferences.CPF_MODE_SKILL));
		findPreference("ctg_skill_preference13_skill").setSummary(ComPreferences.getSkills13(pre, ComPreferences.CPF_MODE_SKILL));
		for(int idx=0; idx<SkillDefines.keyList.length; idx++)
			findPreference(SkillDefines.keyList[idx]+ComPreferences.CPF_MODE_SKILL).setSummary(pre.getString(SkillDefines.keyList[idx]+ComPreferences.CPF_MODE_SKILL, "0"));
		reloadPref();
		return;
	}
	private void fixedPreference(){
//		findPreference("edittext_preference_skill").setEnabled(false);
		findPreference("checkbox_preference_skill").setEnabled(false);
		findPreference("list_preference4_skill").setEnabled(false);
//		findPreference("list_preference7_skill").setEnabled(false);
//		reloadPref();
		return;
	}
	public void ClearSlotCond(){
		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
		Editor ed = pre.edit(); 
		ed.putString("edittext_preference_skill", "");
		if(findPreference("checkbox_preference_skill").isEnabled())
			ed.putBoolean("checkbox_preference_skill", false);
		if(findPreference("list_preference4_skill").isEnabled())
			ed.putString("list_preference4_skill", "3");
		ed.putString("list_preference7_skill", "0");
		ed.commit();
		
		setSummary(pre);
		return;
	}
	public void ClearSkillCond(){
		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
		Editor ed = pre.edit(); 
		for(int idx=0; idx<SkillDefines.keyList.length; idx++)
			ed.putString(SkillDefines.keyList[idx]+ComPreferences.CPF_MODE_SKILL, "0");
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
