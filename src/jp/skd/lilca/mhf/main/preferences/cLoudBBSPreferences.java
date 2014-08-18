package jp.skd.lilca.mhf.main.preferences;

import jp.skd.lilca.mhf.main.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.widget.ListView;

public class cLoudBBSPreferences extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        // ÉXÉäÅ[Évñ≥å¯
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.addPreferencesFromResource(R.xml.cloudbbspreferences);
        setSummary(PreferenceManager.getDefaultSharedPreferences(this));
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
		findPreference("bbs_keyword_preference").setSummary(pre.getString("bbs_keyword_preference", ""));
		findPreference("bbs_level_preference").setSummary(pre.getString("bbs_level_preference", ""));
		findPreference("bbs_monster_preference").setSummary(pre.getString("bbs_monster_preference", ""));
		findPreference("bbs_storategy_preference").setSummary(pre.getString("bbs_storategy_preference", ""));
		findPreference("bbs_weapon_preference").setSummary(pre.getString("bbs_weapon_preference", ""));
		reloadPref();
		return;
	}
}
