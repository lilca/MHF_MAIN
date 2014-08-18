package jp.skd.lilca.mhf.main.buki.preferences;

import jp.skd.lilca.mhf.main.R;
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

public class MyPreferencesBuki extends PreferenceActivity {
	// �O���Ăяo�����[�h�֘A
//	private boolean externalMode = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        this.addPreferencesFromResource(R.xml.preferences_buki);
        setSummary(PreferenceManager.getDefaultSharedPreferences(this));
        // �O���ďo��
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
    	inflater.inflate(R.menu.submenu_buki, menu);
        return true;
    }
    // �I�v�V�������j���[�A�C�e�����I�����ꂽ���ɌĂяo����܂�
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
			case R.id.item1:
				this.viewMessage("�ʒm","����������������N���A���܂����B");
				this.ClearBukiCond();
				break;
			case R.id.item2:
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
		findPreference("list_preference_bougutype_buki").setSummary(pre.getString("list_preference_bougutype_buki", "���ׂ�"));
		findPreference("edittext_preference_buki").setSummary("\""+pre.getString("edittext_preference_buki", "")+"\"");
		findPreference("list_preference_buki").setSummary(pre.getString("list_preference_buki", "���ׂ�"));
		findPreference("list_preference4_buki").setSummary(pre.getString("list_preference4_buki", "0"));
		findPreference("list_preference5_buki").setSummary(pre.getString("list_preference5_buki", "0"));
		findPreference("list_preference6_buki").setSummary(pre.getString("list_preference6_buki", "20"));
		findPreference("list_preference7_buki").setSummary(pre.getString("list_preference7_buki", "-100"));
		findPreference("list_preference8_buki").setSummary(pre.getString("list_preference8_buki", "�ɒZ"));
		findPreference("list_preference9_buki").setSummary(pre.getString("list_preference9_buki", "0"));
		findPreference("list_preference11_buki").setSummary(pre.getString("list_preference11_buki", "0"));
		findPreference("list_preference12_buki").setSummary(pre.getString("list_preference12_buki", "�Ȃ�"));
		findPreference("list_preference13_buki").setSummary(pre.getString("list_preference13_buki", "�Ȃ�"));
		reloadPref();
		return;
	}
	private void fixedPreference(){
//		findPreference("list_preference").setSelectable(false);
		reloadPref();
		return;
	}
	public void ClearBukiCond(){
		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
		Editor ed = pre.edit();
		ed.putString("list_preference_bougutype_buki", "���ׂ�");
		if((findPreference("list_preference_buki").isEnabled()))
			ed.putString("list_preference_buki", "���ׂ�");
		ed.putString("edittext_preference_buki", "");
		ed.putString("list_preference4_buki", "0");
		ed.putString("list_preference5_buki", "0");
		ed.putString("list_preference6_buki", "20");
		ed.putString("list_preference7_buki", "-100");
		ed.putString("list_preference8_buki", "�ɒZ");
		ed.putString("list_preference9_buki", "0");
		ed.putString("list_preference11_buki", "0");
		ed.putString("list_preference12_buki", "�Ȃ�");
		ed.putString("list_preference13_buki", "�Ȃ�");
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
