package jp.skd.lilca.mhf.main.preferences;

import jp.skd.lilca.mhf.main.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;

public class MyPreferences extends PreferenceActivity {

    @Override
    public void onCreate(Bundle InstanceState) {
    	super.onCreate(InstanceState);
        // スリープ無効
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.addPreferencesFromResource(R.xml.preferences);
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
//				this.viewMessage("通知","武具条件を初期化クリアしました。");
//				this.ClearBukiCond();
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
		findPreference("list_preference").setSummary(pre.getString("list_preference", "男"));
		reloadPref();
		return;
	}
	/*
	private void fixedPreference(){
		findPreference("list_preference").setSelectable(false);
		findPreference("list_preference2").setSelectable(false);
		findPreference("list_preference3").setSelectable(false);
		findPreference("list_preference").setEnabled(false);
		findPreference("list_preference2").setEnabled(false);
		findPreference("list_preference3").setEnabled(false);
		reloadPref();
		return;
	}
	/*
	public void ClearBukiCond(){
		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
		Editor ed = pre.edit();
		if((findPreference("list_preference").isEnabled()))
			ed.putString("list_preference", "すべて");
		ed.putString("edittext_preference", "");
		ed.putString("list_preference4", "0");
		ed.putString("list_preference5", "0");
		ed.putString("list_preference6", "20");
		ed.putString("list_preference7", "0");
		ed.commit();
		
		setSummary(pre);
		return;
	}
	public void ClearSkillCond(){
		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
		Editor ed = pre.edit(); 
		for(int idx=0; idx<keyList.length; idx++)
			ed.putString(keyList[idx], "0");
		ed.commit();
		
		setSummary(pre);
		return;
	}
	*/
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
