package jp.skd.lilca.mhf.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import mediba.ad.sdk.android.openx.MasAdView;

import jp.skd.lilca.mhf.lib.bougu.Bougu;
import jp.skd.lilca.mhf.lib.bougu.Kafus;
import jp.skd.lilca.mhf.lib.bougu.Sousyoku;
import jp.skd.lilca.mhf.lib.buki.Buki;
import jp.skd.lilca.mhf.lib.savedata.SavedData;
import jp.skd.lilca.mhf.lib.skill_gain.SkillDefines;
import jp.skd.lilca.mhf.lib.skill_gain.SkillVector;
import jp.skd.lilca.mhf.lib.skill_value_list.SkillValueList;
import jp.skd.lilca.mhf.lib.skill_value_list.StatusText;
import jp.skd.lilca.mhf.lib.tools.CsvStringToList;
import jp.skd.lilca.mhf.main.buki.BukiActivity;
import jp.skd.lilca.mhf.main.kafus.KafuActivity;
import jp.skd.lilca.mhf.main.listadapters.main.ListAdapter;
import jp.skd.lilca.mhf.main.listadapters.main.ListLayout;
import jp.skd.lilca.mhf.main.listadapters.skilllis.SkillsListAdapter;
import jp.skd.lilca.mhf.main.listadapters.skilllis.SkillsListLayout;
import jp.skd.lilca.mhf.main.preferences.MyPreferences;
import jp.skd.lilca.mhf.main.preferences.SkillsPreferences;
import jp.skd.lilca.mhf.main.skill.SkillActivity;
import jp.skd.lilca.mhf.main.skill_value_list.SkillInfoIO;
import jp.skd.lilca.mhf.main.soubi.SoubiActivity;
import jp.skd.lilca.mhf.main.tab.TabButton;
import jp.skd.lilca.mhf.main.preferences.ComPreferences;
import jp.skd.lilca.mhf.subactivity.setchoice.SetChoiceActivity;
import jp.skd.lilca.mhf.web.WebConnection;
import jp.skd.lilca.mhf.web.cLoudResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class MainActivity extends TabActivity implements OnTabChangeListener{
	// Preference�ďo���R�[�h
	public static final int REQUEST_PREFERENCES			= 9999;
	public static final int REQUEST_SKILLS_PREFERENCES	= 9998;
	// �O���C���X�^���X�h�c
	public final static int REQUEST_BUKI		= 11;
	public final static int REQUEST_BOUGU		= 0;
	public final static int REQUEST_SOUSYOKU	= 1;
	public final static int REQUEST_KAFUS		= 2;
	public final static int REQUEST_SETCHOICE	= 1001;
	// �A�C�e���̃f�[�^
	SavedData memory = new SavedData();
	// �o�b�t�@�[
	int currentItem;
	// �X�L�����
	SkillInfoIO skillInfo;
	// �J�����g�^�u
	String currentTab;
	// �C���e���g
	Intent intentBuki;
//	Intent ngIntentBuki;
	Intent intentBougu;
//	Intent ngIntentBougu;
	Intent intentSousyoku;
//	Intent ngIntentSousyoku;
	Intent intentKafus;
//	Intent ngIntentKafus;
	Intent intent_cLoud;
	Intent ngIntent_cLoud;
	Intent intent_setchoice;

	// �C���e���g�N���p�̃��X�i�[
	OnClickListener listenerBuki;
	OnClickListener listenerBougu;
	OnClickListener listenerBouguMod;
	OnClickListener listenerBouguDel;
	OnClickListener listenerSousyoku;
	OnClickListener[] listenerSousyokuArray = new OnClickListener[3];
	OnClickListener listenerKafus;
	OnClickListener[] listenerKafusArray = new OnClickListener[2];
	OnClickListener[] listenerDelete = new OnClickListener[3];
	OnClickListener listenerSetChoice;
	OnClickListener listenerNegative;
	OnClickListener listenerNegativeBuki;
	OnClickListener listenerNegativeBougu;
	// �Ăяo�����[�h�֘A
	Activity me;
	// �폜���̃|�W�V����
    int deletePos;
	// ��������
	String bui;
	String emptySlots;
	String spFlag;
	class DataPos{
		public boolean cLoudFlag;
		public int dataid;
		public DataPos(){
			this.cLoudFlag	= false;
			this.dataid		= 0;
			return;
		}
	}
	DataPos datapos = new DataPos();
	// �^�u�֘A
	private static final String TAG[] = {  
		"tagBuki", "tagAtama", "tagDou", "tagUde", "tagKoshi", "tagAshi", "tagKafu", "tagResult", };  
	TabHost tabHost;
	TabHost.TabSpec spec;
	View tab_v;
	// cLoud
	WebConnection skd;
	// �v���O���X�_�C�A���O�p
	Handler handler = new Handler();
	ProgressDialog progressDlg;
	ProgressDialog progressDlg2;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // �X���[�v����
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // �O���ďo������
        this.me = this;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            setParameters(extras);
        }
        // �X�L����񐶐�
        this.skillInfo = new SkillInfoIO(this.getResources());
        // Web�ݒ�
        skd = WebConnection.getInstance();
        // ��ʏ����ݒ�
        this.setTitle("�����ҏW�i"+memory.getTitle()+"�j");
        setExInfo2Preferences();
        setMemory2SkillsPreferences();
        // �J�����g�^�u
        currentTab = TAG[0];
        // TabHost�ݒ�
        tabHost = getTabHost();
        tabHost.setOnTabChangedListener(this);
        // �N���p�̃��X�i�[��`
        initClickListener();
        // �e�^�u����
        spec = tabHost.newTabSpec(TAG[0]);
        tab_v = new TabButton(this, "����", R.drawable.clear_sq);
        spec.setIndicator(tab_v);
        spec.setContent(R.id.content1);
        tabHost.addTab(spec);
        spec = tabHost.newTabSpec(TAG[1]);
        tab_v = new TabButton(this, "��", R.drawable.clear_sq);
        spec.setIndicator(tab_v);
        spec.setContent(R.id.content2);
        tabHost.addTab(spec);
        spec = tabHost.newTabSpec(TAG[2]);
        tab_v = new TabButton(this, "��", R.drawable.clear_sq);
        spec.setIndicator(tab_v);
        spec.setContent(R.id.content3);
        tabHost.addTab(spec);
        spec = tabHost.newTabSpec(TAG[3]);
        tab_v = new TabButton(this, "�r", R.drawable.clear_sq);
        spec.setIndicator(tab_v);
        spec.setContent(R.id.content4);
        tabHost.addTab(spec);
        spec = tabHost.newTabSpec(TAG[4]);
        tab_v = new TabButton(this, "��", R.drawable.clear_sq);
        spec.setIndicator(tab_v);
        spec.setContent(R.id.content5);
        tabHost.addTab(spec);
        spec = tabHost.newTabSpec(TAG[5]);
        tab_v = new TabButton(this, "�r", R.drawable.clear_sq);
        spec.setIndicator(tab_v);
        spec.setContent(R.id.content6);
        tabHost.addTab(spec);
        spec = tabHost.newTabSpec(TAG[6]);
        tab_v = new TabButton(this, "�J�t", R.drawable.clear_sq);
        spec.setIndicator(tab_v);
        spec.setContent(R.id.content7);
        tabHost.addTab(spec);
        spec = tabHost.newTabSpec(TAG[7]);
        tab_v = new TabButton(this, "���", R.drawable.clear_sq);
        spec.setIndicator(tab_v);
        spec.setContent(R.id.content8);
        tabHost.addTab(spec);
        // �{�^���ݒ�
        initSetListener();
        // �L��
		MasAdView mad = null;
		mad = (MasAdView) findViewById(R.id.adview);
		mad.setAuid("149624");
		mad.start();
        return;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	if( keyCode == KeyEvent.KEYCODE_BACK ){
   			AlertDialog.Builder dlg;
   			dlg = new AlertDialog.Builder(this);
 			dlg.setTitle("�m�F");
   			dlg.setMessage("�ҏW���I�����܂����H");
   			dlg.setPositiveButton("�͂�", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					me.finish();
				}
			});
   			dlg.setNegativeButton("������", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					;	// �������Ȃ�
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
    	// onOptionsItemSelected�֘A�g���邽��super.onKeyDown���Ă�
    	return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;
    }
    // �I�v�V�������j���[�A�C�e�����I�����ꂽ���ɌĂяo����܂�
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    		case R.id.menuitem1:
    			fileMessage("�t�@�C��");
    			break;
    		case R.id.menuitem2:
    			editMessage("�ҏW");
    			break;
    		case R.id.menuitem4:
    			shareMessage("���L");
    			break;
    		case R.id.menuitem5:
    			doSignin();
    			break;
    		case R.id.menuitem6:
    			Uri uri1=Uri.parse("market://search?q=pub:\"lilca\"");
    			Intent familyIntent=new Intent(Intent.ACTION_VIEW,uri1);
    	    	startActivity(familyIntent);
    			break;
    		case R.id.menuitem7:
    			infoMessage("���");
    			break;
    		default:
    			break;
    	}
        return true;
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == REQUEST_SKILLS_PREFERENCES){
	        // �����\���ݒ�
			setSkillsPreferences2Memory();
			setReqSkillsButton();
		}
		if(requestCode == REQUEST_PREFERENCES){
	        // �����\���ݒ�
			setPreferences2ExInfo();
	        memory.ifNotTakeoffSexually();
			this.setTabResult();
		}
		if(requestCode == REQUEST_BUKI){
			if(resultCode == RESULT_OK){
				String res = (String) data.getExtras().getString("text");
		        this.setBuki(res);
			}
			else{
			}
		}
		if(requestCode == REQUEST_BOUGU){
			if(resultCode == RESULT_OK){
				String res = (String) data.getExtras().getString("text");
		        this.setBougu(res);
			}
			else{
			}
		}
		if(requestCode == REQUEST_SOUSYOKU){
			if(resultCode == RESULT_OK){
				String res = (String) data.getExtras().getString("text");
		        this.setSousyoku(res);
			}
			else{
			}
		}
		if(requestCode == REQUEST_KAFUS){
			if(resultCode == RESULT_OK){
				String res = (String) data.getExtras().getString("text");
		        this.setKafus(res);
			}
			else{
			}
		}
		if(requestCode == REQUEST_SETCHOICE){
			if(resultCode == RESULT_OK){
				String res = (String) data.getExtras().getString("text");
				String res1 = (String) data.getExtras().getString("text_s1");
				String res2 = (String) data.getExtras().getString("text_s2");
				String res3 = (String) data.getExtras().getString("text_s3");
				this.setBouguSet(res, res1, res2, res3);
			}
		}
		return;
	}
	private void setSousyoku(String s){
		if(currentTab.equals(TAG[0])){
			if(currentItem!=-1)
				memory.getBuki().getSousyokuList().remove(currentItem);
			memory.getBuki().addSousyoku(new Sousyoku(s));				
	        this.setTabBuki();
		}
		if(currentTab.equals(TAG[1])){
			if(currentItem!=-1)
				memory.getAtama().getSousyokuList().remove(currentItem);
			memory.getAtama().addSousyoku(new Sousyoku(s));
	        this.setTabAtama();
		}
		else
		if(currentTab.equals(TAG[2])){
			if(currentItem!=-1)
				memory.getDou().getSousyokuList().remove(currentItem);
			memory.getDou().addSousyoku(new Sousyoku(s));
	        this.setTabDou();
		}
		else
		if(currentTab.equals(TAG[3])){
			if(currentItem!=-1)
				memory.getUde().getSousyokuList().remove(currentItem);
			memory.getUde().addSousyoku(new Sousyoku(s));
	        this.setTabUde();
		}
		else
		if(currentTab.equals(TAG[4])){
			if(currentItem!=-1)
				memory.getKoshi().getSousyokuList().remove(currentItem);
			memory.getKoshi().addSousyoku(new Sousyoku(s));
	        this.setTabKoshi();
		}
		else
		if(currentTab.equals(TAG[5])){
			if(currentItem!=-1)
				memory.getAshi().getSousyokuList().remove(currentItem);
			memory.getAshi().addSousyoku(new Sousyoku(s));
	        this.setTabAshi();
		}
        return;
	}
	private void setBuki(String b){
		Buki temp = new Buki(b);
        memory.replaceBuki(temp);
        memory.ifNotTakeoff();
        this.setTabBuki();
        return;
	}
	private void setKafus(String b){
		Kafus temp = new Kafus(b);
		if(currentItem!=-1)
			memory.getKafus().remove(currentItem);
		if(temp.getSlotCost()<=memory.getEmptyKafusSlots())
			memory.getKafus().add(temp);
        this.setTabKafu();
        return;
	}
	private void setBougu(String b){
		Bougu temp = new Bougu(b);
		if(temp.isAtama()){
			memory.replaceAtama(temp);
	        this.setTabAtama();
		}
		else
		if(temp.isDou()){
	        memory.replaceDou(temp);
	        this.setTabDou();
		}
		else
		if(temp.isUde()){
	        memory.replaceUde(temp);
	        this.setTabUde();
		}
		else
		if(temp.isKoshi()){
	        memory.replaceKoshi(temp);
	        this.setTabKoshi();
		}
		else
		if(temp.isAshi()){
	        memory.replaceAshi(temp);
	        this.setTabAshi();
		}
        return;
	}
	private void setBouguSet(String b, String s1, String s2, String s3){
		Bougu temp = new Bougu(b);
		if(temp.isAtama()){
			memory.setAtama(temp);
			memory.getAtama().addSousyoku(new Sousyoku(s1));
			memory.getAtama().addSousyoku(new Sousyoku(s2));
			memory.getAtama().addSousyoku(new Sousyoku(s3));
	        this.setTabAtama();
		}
		else
		if(temp.isDou()){
			memory.setDou(temp);
			memory.getDou().addSousyoku(new Sousyoku(s1));
			memory.getDou().addSousyoku(new Sousyoku(s2));
			memory.getDou().addSousyoku(new Sousyoku(s3));
	        this.setTabDou();
		}
		else
		if(temp.isUde()){
			memory.setUde(temp);
			memory.getUde().addSousyoku(new Sousyoku(s1));
			memory.getUde().addSousyoku(new Sousyoku(s2));
			memory.getUde().addSousyoku(new Sousyoku(s3));
	        this.setTabUde();
		}
		else
		if(temp.isKoshi()){
			memory.setKoshi(temp);
			memory.getKoshi().addSousyoku(new Sousyoku(s1));
			memory.getKoshi().addSousyoku(new Sousyoku(s2));
			memory.getKoshi().addSousyoku(new Sousyoku(s3));
	        this.setTabKoshi();
		}
		else
		if(temp.isAshi()){
			memory.setAshi(temp);
			memory.getAshi().addSousyoku(new Sousyoku(s1));
			memory.getAshi().addSousyoku(new Sousyoku(s2));
			memory.getAshi().addSousyoku(new Sousyoku(s3));
	        this.setTabAshi();
		}
        return;
	}
	@Override
	public void onTabChanged(String tabId) {
		currentTab = tabId;
		if (tabId.equals(TAG[0]))this.setTabBuki();
		else if (tabId.equals(TAG[1]))this.setTabAtama();
		else if (tabId.equals(TAG[2]))this.setTabDou();
		else if (tabId.equals(TAG[3]))this.setTabUde();
		else if (tabId.equals(TAG[4]))this.setTabKoshi();
		else if (tabId.equals(TAG[5]))this.setTabAshi();
		else if (tabId.equals(TAG[6]))this.setTabKafu();
		else if (tabId.equals(TAG[7]))this.setTabResult();
		return;
	}
    View typeChengeBtn;
	public void setTabBuki(){
        // �y����z�ݒ�
		ListAdapter adapter;
		Buki b = memory.getBuki();
		
		ArrayList<ListLayout> list = new ArrayList<ListLayout>();
        ListLayout temp = new ListLayout();
		temp.setTextTop(b.getName()+" "+b.getSlotString());
		String str = b.getType()+" �U��:"+b.getAttack()+
					"("+b.getBukiBairitsu()+") "+
					b.getAttrAttackString()+" "+b.getSpAttrAttackString()+
					", �h: "+Integer.toString(b.getDef())+
					", ��: "+b.getKaishin()+
					", ���A: "+b.getRare()+
    				" <font color=\"aqua\">"+b.getOyakata()+"</font>";
		String btype = b.getBukiType();
		if(!btype.equals(""))
			str += "<font color=\"#ff69b4\">&lt;"+btype+"&gt;</font>";
		CharSequence cs = Html.fromHtml(str);
		temp.setTextBottom(cs);
		temp.setTextRight1("");
		temp.setButtonImageId1(R.drawable.btnimage_mod);
		temp.setListener1(listenerBuki);
		temp.setTextRight2("");
		temp.setButtonImageId2(R.drawable.btnimage_del);
		temp.setListener2(listenerBouguDel);
		list.add(temp);
        // ���Ă��
        ArrayList<Sousyoku> SousyokuList = b.getSousyokuList(); 
        for(int idx=0; idx<SousyokuList.size(); idx++){
        	Sousyoku s1 = SousyokuList.get(idx);
            ListLayout temp1 = new ListLayout();
    		temp1.setTextTop(s1.getName()+" "+s1.getSlotCostString());
    		str = s1.getSkillsWithColorTag("#FF0000")+" "+s1.getCost()+", SP: "+s1.isSP();
    		cs = Html.fromHtml(str);
    		temp1.setTextBottom(cs);
    		temp1.setTextRight1("");
    		temp1.setButtonImageId1(R.drawable.btnimage_mod);
    		temp1.setListener1(listenerSousyokuArray[idx]);
    		temp1.setTextRight2("");
    		temp1.setButtonImageId2(R.drawable.btnimage_del);
    		temp1.setListener2(listenerDelete[idx]);
            list.add(temp1);        	
        }
        // �ǉ��ł��邩
        if(b.getEmptySlots() > 0){
        	ListLayout temp1 = new ListLayout();
        	temp1.setTextTop(b.getEmptySlots()+"�X���b�g�󂢂Ă��܂�");
			temp1.setTextRight2("");
    		temp1.setButtonImageId2(R.drawable.btnimage_add);
			temp1.setListener2(listenerSousyoku);
        	list.add(temp1);
        }
        this.emptySlots = Integer.toString(b.getEmptySlots());
        this.spFlag = Boolean.toString(b.isSP());
        // �o�^
        adapter = new ListAdapter(this, list);
        ListView listview = (ListView)findViewById(R.id.listView0);
        listview.setAdapter(adapter);
		return;
	}
	public void setTabAtama(){
        // �y���z�ݒ�
		ListAdapter adapter;
		Bougu b = memory.getAtama();

		ArrayList<ListLayout> list = new ArrayList<ListLayout>();
        ListLayout temp = new ListLayout();
		temp.setTextTop(b.getName()+" "+b.getSlotString());
		String str = b.getSkillsWithColorTag("#FF0000")+" Lv"+b.getLevel()+", �h: "+Integer.toString(b.getDef())+" �i���A"+b.getRare()+"�j";
		String btype = b.getBouguType();
		if(!btype.equals(""))
			str += "<font color=\"#ff69b4\">&lt;"+btype+"�h��&gt;</font>";
		CharSequence cs = Html.fromHtml(str);
		temp.setTextBottom(cs);
		if(b.isEmpty()){
			temp.setTextRight2("");
			temp.setButtonImageId2(R.drawable.btnimage_add);
			temp.setListener2(listenerBougu);
		}
		else{
			temp.setTextRight1("");
			temp.setButtonImageId1(R.drawable.btnimage_mod);
			temp.setListener1(listenerBouguMod);
			temp.setTextRight2("");
			temp.setButtonImageId2(R.drawable.btnimage_del);
			temp.setListener2(listenerBouguDel);
		}
		bui = "��";
        list.add(temp);
        // ���Ă��
        ArrayList<Sousyoku> SousyokuList = b.getSousyokuList(); 
        for(int idx=0; idx<SousyokuList.size(); idx++){
        	Sousyoku s1 = SousyokuList.get(idx);
            ListLayout temp1 = new ListLayout();
    		temp1.setTextTop(s1.getName()+" "+s1.getSlotCostString());
    		str = s1.getSkillsWithColorTag("#FF0000")+" "+s1.getCost()+", SP: "+s1.isSP();
    		cs = Html.fromHtml(str);
    		temp1.setTextBottom(cs);
    		temp1.setTextRight1("");
    		temp1.setButtonImageId1(R.drawable.btnimage_mod);
    		temp1.setListener1(listenerSousyokuArray[idx]);
    		temp1.setTextRight2("");
    		temp1.setButtonImageId2(R.drawable.btnimage_del);
    		temp1.setListener2(listenerDelete[idx]);
            list.add(temp1);        	
        }
        // �ǉ��ł��邩
        if(b.getEmptySlots() > 0){
        	ListLayout temp1 = new ListLayout();
        	temp1.setTextTop(b.getEmptySlots()+"�X���b�g�󂢂Ă��܂�");
			temp1.setTextRight2("");
    		temp1.setButtonImageId2(R.drawable.btnimage_add);
			temp1.setListener2(listenerSousyoku);
        	list.add(temp1);
        }
        this.emptySlots = Integer.toString(b.getEmptySlots());
        this.spFlag = Boolean.toString(b.isSP());
        // �o�^
        adapter = new ListAdapter(this, list);
        ListView listview = (ListView)findViewById(R.id.listView1);
        listview.setAdapter(adapter);
        return;
	}
	private void setTabDou(){
        // �y���z�ݒ�
		ListAdapter adapter;
		Bougu b = memory.getDou();
		
		ArrayList<ListLayout> list = new ArrayList<ListLayout>();
        ListLayout temp = new ListLayout();
		temp.setTextTop(b.getName()+" "+b.getSlotString());
		String str = b.getSkillsWithColorTag("#FF0000")+" Lv"+b.getLevel()+", �h: "+Integer.toString(b.getDef())+" �i���A"+b.getRare()+"�j";
		String btype = b.getBouguType();
		if(!btype.equals(""))
			str += "<font color=\"#ff69b4\">&lt;"+btype+"�h��&gt;</font>";
		CharSequence cs = Html.fromHtml(str);
		temp.setTextBottom(cs);
		if(b.isEmpty()){
			temp.setTextRight2("");
			temp.setButtonImageId2(R.drawable.btnimage_add);
			temp.setListener2(listenerBougu);
		}
		else{
			temp.setTextRight1("");
			temp.setButtonImageId1(R.drawable.btnimage_mod);
			temp.setListener1(listenerBouguMod);
			temp.setTextRight2("");
			temp.setButtonImageId2(R.drawable.btnimage_del);
			temp.setListener2(listenerBouguDel);
		}
		bui = "��";
        list.add(temp);
        // ���Ă��
        ArrayList<Sousyoku> SousyokuList = b.getSousyokuList(); 
        for(int idx=0; idx<SousyokuList.size(); idx++){
        	Sousyoku s1 = SousyokuList.get(idx);
            ListLayout temp1 = new ListLayout();
    		temp1.setTextTop(s1.getName()+" "+s1.getSlotCostString());
    		str = s1.getSkillsWithColorTag("#FF0000")+" "+s1.getCost()+", SP: "+s1.isSP();
    		cs = Html.fromHtml(str);
    		temp1.setTextBottom(cs);
    		temp1.setTextRight1("");
    		temp1.setButtonImageId1(R.drawable.btnimage_mod);
    		temp1.setListener1(listenerSousyokuArray[idx]);
    		temp1.setTextRight2("");
    		temp1.setButtonImageId2(R.drawable.btnimage_del);
    		temp1.setListener2(listenerDelete[idx]);
            list.add(temp1);        	
        }
        // �ǉ��ł��邩
        if(b.getEmptySlots() > 0){
        	ListLayout temp1 = new ListLayout();
        	temp1.setTextTop(b.getEmptySlots()+"�X���b�g�󂢂Ă��܂�");
			temp1.setTextRight2("");
    		temp1.setButtonImageId2(R.drawable.btnimage_add);
			temp1.setListener2(listenerSousyoku);
        	list.add(temp1);
        }
        this.emptySlots = Integer.toString(b.getEmptySlots());
        this.spFlag = Boolean.toString(b.isSP());
        // �o�^
        adapter = new ListAdapter(this, list);
        ListView listview = (ListView)findViewById(R.id.listView2);
        listview.setAdapter(adapter);
		return;
	}
	private void setTabUde(){
        // �y�r�z�ݒ�
		ListAdapter adapter;
		Bougu b = memory.getUde();
		
		ArrayList<ListLayout> list = new ArrayList<ListLayout>();
        ListLayout temp = new ListLayout();
		temp.setTextTop(b.getName()+" "+b.getSlotString());
		String str = b.getSkillsWithColorTag("#FF0000")+" Lv"+b.getLevel()+", �h: "+Integer.toString(b.getDef())+" �i���A"+b.getRare()+"�j";
		String btype = b.getBouguType();
		if(!btype.equals(""))
			str += "<font color=\"#ff69b4\">&lt;"+btype+"�h��&gt;</font>";
		CharSequence cs = Html.fromHtml(str);
		temp.setTextBottom(cs);
		if(b.isEmpty()){
			temp.setTextRight2("");
			temp.setButtonImageId2(R.drawable.btnimage_add);
			temp.setListener2(listenerBougu);
		}
		else{
			temp.setTextRight1("");
			temp.setButtonImageId1(R.drawable.btnimage_mod);
			temp.setListener1(listenerBouguMod);
			temp.setTextRight2("");
			temp.setButtonImageId2(R.drawable.btnimage_del);
			temp.setListener2(listenerBouguDel);
		}
		bui = "�r";
        list.add(temp);
        // ���Ă��
        ArrayList<Sousyoku> SousyokuList = b.getSousyokuList(); 
        for(int idx=0; idx<SousyokuList.size(); idx++){
        	Sousyoku s1 = SousyokuList.get(idx);
            ListLayout temp1 = new ListLayout();
    		temp1.setTextTop(s1.getName()+" "+s1.getSlotCostString());
    		str = s1.getSkillsWithColorTag("#FF0000")+" "+s1.getCost()+", SP: "+s1.isSP();
    		cs = Html.fromHtml(str);
    		temp1.setTextBottom(cs);
    		temp1.setTextRight1("");
    		temp1.setButtonImageId1(R.drawable.btnimage_mod);
    		temp1.setListener1(listenerSousyokuArray[idx]);
    		temp1.setTextRight2("");
    		temp1.setButtonImageId2(R.drawable.btnimage_del);
    		temp1.setListener2(listenerDelete[idx]);
            list.add(temp1);        	
        }
        // �ǉ��ł��邩
        if(b.getEmptySlots() > 0){
        	ListLayout temp1 = new ListLayout();
        	temp1.setTextTop(b.getEmptySlots()+"�X���b�g�󂢂Ă��܂�");
			temp1.setTextRight2("");
    		temp1.setButtonImageId2(R.drawable.btnimage_add);
			temp1.setListener2(listenerSousyoku);
        	list.add(temp1);
        }
        this.emptySlots = Integer.toString(b.getEmptySlots());
        this.spFlag = Boolean.toString(b.isSP());
        // �o�^
        adapter = new ListAdapter(this, list);
        ListView listview = (ListView)findViewById(R.id.listView3);
        listview.setAdapter(adapter);
		return;
	}
	private void setTabKoshi(){
        // �y���z�ݒ�
		ListAdapter adapter;
		Bougu b = memory.getKoshi();
		
		ArrayList<ListLayout> list = new ArrayList<ListLayout>();
        ListLayout temp = new ListLayout();
		temp.setTextTop(b.getName()+" "+b.getSlotString());
		String str = b.getSkillsWithColorTag("#FF0000")+" Lv"+b.getLevel()+", �h: "+Integer.toString(b.getDef())+" �i���A"+b.getRare()+"�j";
		String btype = b.getBouguType();
		if(!btype.equals(""))
			str += "<font color=\"#ff69b4\">&lt;"+btype+"�h��&gt;</font>";
		CharSequence cs = Html.fromHtml(str);
		temp.setTextBottom(cs);
		if(b.isEmpty()){
			temp.setTextRight2("");
			temp.setButtonImageId2(R.drawable.btnimage_add);
			temp.setListener2(listenerBougu);
		}
		else{
			temp.setTextRight1("");
			temp.setButtonImageId1(R.drawable.btnimage_mod);
			temp.setListener1(listenerBouguMod);
			temp.setTextRight2("");
			temp.setButtonImageId2(R.drawable.btnimage_del);
			temp.setListener2(listenerBouguDel);
		}
		bui = "��";
        list.add(temp);
        // ���Ă��
        ArrayList<Sousyoku> SousyokuList = b.getSousyokuList(); 
        for(int idx=0; idx<SousyokuList.size(); idx++){
        	Sousyoku s1 = SousyokuList.get(idx);
            ListLayout temp1 = new ListLayout();
    		temp1.setTextTop(s1.getName()+" "+s1.getSlotCostString());
    		str = s1.getSkillsWithColorTag("#FF0000")+" "+s1.getCost()+", SP: "+s1.isSP();
    		cs = Html.fromHtml(str);
    		temp1.setTextBottom(cs);
    		temp1.setTextRight1("");
    		temp1.setButtonImageId1(R.drawable.btnimage_mod);
    		temp1.setListener1(listenerSousyokuArray[idx]);
    		temp1.setTextRight2("");
    		temp1.setButtonImageId2(R.drawable.btnimage_del);
    		temp1.setListener2(listenerDelete[idx]);
            list.add(temp1);        	
        }
        // �ǉ��ł��邩
        if(b.getEmptySlots() > 0){
        	ListLayout temp1 = new ListLayout();
        	temp1.setTextTop(b.getEmptySlots()+"�X���b�g�󂢂Ă��܂�");
			temp1.setTextRight2("");
    		temp1.setButtonImageId2(R.drawable.btnimage_add);
			temp1.setListener2(listenerSousyoku);
        	list.add(temp1);
        }
        this.emptySlots = Integer.toString(b.getEmptySlots());
        this.spFlag = Boolean.toString(b.isSP());
        // �o�^
        adapter = new ListAdapter(this, list);
        ListView listview = (ListView)findViewById(R.id.listView4);
        listview.setAdapter(adapter);
		return;
	}
	private void setTabAshi(){
        // �y�r�z�ݒ�
		ListAdapter adapter;
		Bougu b = memory.getAshi();
		
		ArrayList<ListLayout> list = new ArrayList<ListLayout>();
        ListLayout temp = new ListLayout();
		temp.setTextTop(b.getName()+" "+b.getSlotString());
		String str = b.getSkillsWithColorTag("#FF0000")+" Lv"+b.getLevel()+", �h: "+Integer.toString(b.getDef())+" �i���A"+b.getRare()+"�j";
		String btype = b.getBouguType();
		if(!btype.equals(""))
			str += "<font color=\"#ff69b4\">&lt;"+btype+"�h��&gt;</font>";
		CharSequence cs = Html.fromHtml(str);
		temp.setTextBottom(cs);
		if(b.isEmpty()){
			temp.setTextRight2("");
			temp.setButtonImageId2(R.drawable.btnimage_add);
			temp.setListener2(listenerBougu);
		}
		else{
			temp.setTextRight1("");
			temp.setButtonImageId1(R.drawable.btnimage_mod);
			temp.setListener1(listenerBouguMod);
			temp.setTextRight2("");
			temp.setButtonImageId2(R.drawable.btnimage_del);
			temp.setListener2(listenerBouguDel);
		}
		bui = "�r";
		list.add(temp);
        // ���Ă��
        ArrayList<Sousyoku> SousyokuList = b.getSousyokuList(); 
        for(int idx=0; idx<SousyokuList.size(); idx++){
        	Sousyoku s1 = SousyokuList.get(idx);
            ListLayout temp1 = new ListLayout();
    		temp1.setTextTop(s1.getName()+" "+s1.getSlotCostString());
    		str = s1.getSkillsWithColorTag("#FF0000")+" "+s1.getCost()+", SP: "+s1.isSP();
    		cs = Html.fromHtml(str);
    		temp1.setTextBottom(cs);
    		temp1.setTextRight1("");
    		temp1.setButtonImageId1(R.drawable.btnimage_mod);
    		temp1.setListener1(listenerSousyokuArray[idx]);
    		temp1.setTextRight2("");
    		temp1.setButtonImageId2(R.drawable.btnimage_del);
    		temp1.setListener2(listenerDelete[idx]);
            list.add(temp1);        	
        }
        // �ǉ��ł��邩
        if(b.getEmptySlots() > 0){
        	ListLayout temp1 = new ListLayout();
        	temp1.setTextTop(b.getEmptySlots()+"�X���b�g�󂢂Ă��܂�");
			temp1.setTextRight2("");
    		temp1.setButtonImageId2(R.drawable.btnimage_add);
			temp1.setListener2(listenerSousyoku);
        	list.add(temp1);
        }
        this.emptySlots = Integer.toString(b.getEmptySlots());
        this.spFlag = Boolean.toString(b.isSP());
        // �o�^
        adapter = new ListAdapter(this, list);
        ListView listview = (ListView)findViewById(R.id.listView5);
        listview.setAdapter(adapter);
		return;
	}
	private void setTabKafu(){
        // �y�J�t�z�ݒ�
		ListAdapter adapter;
		ArrayList<Kafus> k = memory.getKafus();
		
        // ���Ă�J�t
		ArrayList<ListLayout> list = new ArrayList<ListLayout>();
        for(int idx=0; idx<k.size(); idx++){
        	if(k.get(idx).isEmpty())
        		continue;
            ListLayout temp1 = new ListLayout();
    		temp1.setTextTop(k.get(idx).getName()+" "+k.get(idx).getSlotCostString());
    		String str = k.get(idx).getSkillsWithColorTag("#FF0000");
    		CharSequence cs = Html.fromHtml(str);
    		temp1.setTextBottom(cs);
    		temp1.setTextRight1("");
    		temp1.setButtonImageId1(R.drawable.btnimage_mod);
    		temp1.setListener1(listenerKafusArray[idx]);
    		temp1.setTextRight2("");
    		temp1.setButtonImageId2(R.drawable.btnimage_del);
    		temp1.setListener2(listenerDelete[idx]);
            list.add(temp1);        	
        }
        // �ǉ��ł��邩
        int a = memory.getEmptyKafusSlots();
        if(a > 0){
        	ListLayout temp1 = new ListLayout();
        	temp1.setTextTop(a+"�X���b�g�󂢂Ă��܂�");
			temp1.setTextRight2("");
    		temp1.setButtonImageId2(R.drawable.btnimage_add);
			temp1.setListener2(listenerKafus);
        	list.add(temp1);
        }
        // �o�^
        adapter = new ListAdapter(this, list);
        ListView listview = (ListView)findViewById(R.id.listView6);
        listview.setAdapter(adapter);
		return;
	}
	private void setTabResult(){
        // �y���ʁz�ݒ�
        WebView temp = (WebView) this.findViewById(R.id.tvResult);
        String str ="<html><head><meta http-equiv='content-type' content='text/html;charset=UTF-8'></head>"+
        			"<body style=\"overflow:hidden;\" text=\"#FFFFFF\">"+
        			//"<img style=\"width:100%;height:100%;position:absolute;top:0;left:0;\" src=\"listitem01.png\" alt=\"\" />"+
        			"<div style=\"position:absolute;\"><font size=\"2\">"+
        			//"<ul style=\"padding:0px;margin:0px;\">"+
        			//"<li style=\"display:block;float:left;border:inset 1px #00ffff\">�e��</li>"+
        			//"<li style=\"display:block;float:left;\">����</li>"+
        			//"<li style=\"display:block;float:left;\">���r</li>"+
        			//"<li style=\"display:block;float:left;\">�g�b</li>"+
        			//"<li style=\"display:block;float:left;\">��</li>"+
        			//"</ul><br>"+
        			StatusText.getStatusText(skillInfo.getData(), memory, true)+
        			"</font></div></body></html>";
        temp.loadDataWithBaseURL("file:///android_res/drawable/", str, "text/html", "utf-8", null);
        //temp.loadData(str, "text/html", "UTF-8");
        if(Build.VERSION.SDK_INT < 11){
        	temp.setBackgroundColor(0);
        }
        // android3.0�ȏ�̂Ƃ�
        else{
        	//temp.setBackgroundColor(0xff000000);
        	temp.setBackgroundColor(0);
        	temp.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
//        temp.reload();
		return;
	}
	private EditText edtInput;
    public void saveMessage(){
    	edtInput = new EditText(this);
    	edtInput.setText(memory.getTitle());
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setView(edtInput);
    	builder.setTitle("�^�C�g������͂��ĉ�����");
    	builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
		    	memory.setTitle(edtInput.getText().toString());
		    	// Content content = new Content(me, MHF_DEFINES.SAVE_FILE_LIST[dataId]);
		        // content.setSavedData(memory);
		        // content.save();
		        setTitle("���������H�i"+memory.getTitle()+"�j");
		        setPreferences2ExInfo();
				setSkillsPreferences2Memory();
				return;
			}
		});
    	builder.show();
    	return;
    }
    public void clearMessage(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("���̑������N���A���܂����H");
    	builder.setPositiveButton("YES", new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
		    	memory.clearSoubi();
		        setTitle("���������H�i"+memory.getTitle()+"�j");
		        setExInfo2Preferences();
		        setMemory2SkillsPreferences();
		        onTabChanged(currentTab);
				return;
			}
		});
    	builder.show();
    	return;
    }
    public void fileMessage(String t){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	String[] list = {"�㏑�ۑ�", "Clipboard�֕ۑ�"};
    	builder.setTitle(t);
    	builder.setItems(list, new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if( which == 0 ){
					if( datapos.cLoudFlag ){
						memory.toCsv();
						HashMap<Integer, String> a = new HashMap<Integer, String>();
						a.put(datapos.dataid, memory.getAllElementsToString());
						putStorageList(a);
					}
					else{
						memory.toCsv();
						Content content = new Content(me, Content.SAVE_FILE_LIST[datapos.dataid]);
						content.setSavedData(memory);
						content.save();
						viewMessage("�㏑�ۑ�", "�ۑ����܂���");
					}
				}
				if( which == 1 ){
					memory.toCsv();
					Intent res = new Intent();
					res.putExtra("editresult", memory.getAllElementsToString());
					me.setResult(RESULT_OK, res);
					me.finish();
				}
				return;
			}
		});
    	builder.create();
    	builder.show();
    	return;
    }
    public void infoMessage(String t){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	String[] list = {"facebook", "WEB�T�C�g"};
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
	    			Uri uri2=Uri.parse("http://skd.backdrop.jp/mhf/index.html");
	    			Intent webIntent=new Intent(Intent.ACTION_VIEW,uri2);
	    	    	startActivity(webIntent);
				}
				return;
			}
		});
    	builder.create();
    	builder.show();
    	return;
    }
    private cLoudResult putstorageres;
    private HashMap<Integer, String> putdata;
    private void putStorageList(HashMap<Integer, String> a){
    	putdata = a;
    	// �X���b�h����
    	new Thread(new Runnable(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					@Override
					public void run(){
				    	progressDlg = ProgressDialog.show(me, "cLoud�֏������ݒ�","Please wait...", false);
				    	progressDlg.show();
					}
				});
				// �X�V
				try {
					putstorageres = skd.putStorageList(putdata);
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.post(new Runnable(){
					@Override
					public void run(){
						if(progressDlg != null & progressDlg.isShowing() )
							progressDlg.dismiss();
						if(putstorageres!=null)
							Toast.makeText(me, putstorageres.getHeadValue("message"), Toast.LENGTH_LONG).show();
					}
				});
			}
    	}).start();
    	return;
    }
    public void editMessage(String t){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	String[] list = {"�^�C�g���ύX", "�����N���A", };
    	builder.setTitle(t);
    	builder.setItems(list, new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if( which == 0 ){
					// �^�C�g���ύX
					saveMessage();
				}
				else
				if( which == 1 ){
					// �����N���A
	    			clearMessage();
				}
				return;
			}
		});
    	builder.create();
    	builder.show();
    	return;
    }
    public void shareMessage(String t){
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		String content = StatusText.getStatusText(skillInfo.getData(), memory, false);
		intent.putExtra(Intent.EXTRA_TEXT, content);
		startActivity(intent);
    	return;
    }
    private void setReqSkillsButton(){
		LayoutInflater inflater = LayoutInflater.from(me);
		final View reqSkillsView = inflater.inflate(R.layout.reqskilllist, null);
		// ���X�g�̎擾
		ArrayList<SkillsListLayout> list = new ArrayList<SkillsListLayout>();
		List<String> data;
		if(memory.getReqSkills()==null){
			data = new ArrayList<String>();
			data.clear();
		}
		else
			data = Arrays.asList(memory.getReqSkills());
    	SkillsListLayout first = new SkillsListLayout();
    	// �w�b�_
    	first.setTextLeft(Html.fromHtml("<font color=\"yellow\">�����X�L��</font>"));
    	first.setTextRight(Html.fromHtml("<font color=\"yellow\">�X�L��</font>"));
    	first.setTextRight2(Html.fromHtml("<font color=\"yellow\">�v</font>"));
    	first.setTextRight3(Html.fromHtml("<font color=\"yellow\">��</font>"));
    	first.setTextRight4(Html.fromHtml("<font color=\"yellow\">�c</font>"));
    	list.add(first);
    	// �����S�̂̃X�L���x�N�g��
    	SkillVector soubiTotal = memory.getTotalSkillVector();
    	// �f�[�^
		for(int idx=0; idx<data.size() && idx<10; idx++){
			if(data.get(idx).equals(""))
				continue;
        	SkillsListLayout temp1 = new SkillsListLayout();
        	String boot = data.get(idx);
        	String skill = skillInfo.getSkillName(boot);
        	temp1.setTextLeft(boot);
        	temp1.setTextRight(skill);
        	temp1.setTextRight2(Integer.toString(skillInfo.getSkillValue(boot)));
        	temp1.setTextRight3(Integer.toString(soubiTotal.getValue(skill)));
        	temp1.setTextRight4(Integer.toString(skillInfo.getSkillValue(boot)-soubiTotal.getValue(skill)));
        	list.add(temp1);
    	}
		// �A�_�v�^�ݒ�
		SkillsListAdapter adapter;
        adapter = new SkillsListAdapter(this, list);
		ListView skillList = (ListView)reqSkillsView.findViewById(R.id.skillListView1);
		skillList.setAdapter(adapter);
		AlertDialog.Builder builder = new AlertDialog.Builder(me);
		builder.setTitle("�ڕW�X�L���ꗗ")
		.setCancelable(true)
		.setView(reqSkillsView)
		.setPositiveButton("�ҏW", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				final Intent intentPre = new Intent(me, SkillsPreferences.class);
				intentPre.putExtra("mode", "easy");
				startActivityForResult(intentPre, REQUEST_SKILLS_PREFERENCES);
				dialog.cancel();
				return;
			}
		})
		.setNegativeButton("����", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				return;
			}
		})
		.show();
    	return;
    }
    public void setExInfo2Preferences(){
		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
		Editor ed = pre.edit();
		// ����
		ed.putString("list_preference", memory.getGender());
		if(memory.isPwrGofu())
			ed.putBoolean("chk_prefer_pwgo", true);
		else
			ed.putBoolean("chk_prefer_pwgo", false);
		if(memory.isPwrTsume())
			ed.putBoolean("chk_prefer_pwnl", true);
		else
			ed.putBoolean("chk_prefer_pwnl", false);
		if(memory.isDefGofu())
			ed.putBoolean("chk_prefer_dfgo", true);
		else
			ed.putBoolean("chk_prefer_dfgo", false);
		if(memory.isDefTsume())
			ed.putBoolean("chk_prefer_dfnl", true);
		else
			ed.putBoolean("chk_prefer_dfnl", false);
		ed.commit();
		return;
    }
    public void setMemory2SkillsPreferences(){
		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
		Editor ed = pre.edit();
		// �ڕW�X�L��
		for(int idx=0; idx<SkillDefines.keyList.length; idx++){
			ed.putString(SkillDefines.keyList[idx], "");
			for(int jdx=0; jdx<memory.getReqSkillSize(); jdx++){
				if( skillInfo.getSkillName(memory.getReqSkill(jdx)).equals(SkillDefines.nameList[idx]) ){
					String temp = memory.getReqSkill(jdx);
					ed.putString(SkillDefines.keyList[idx], temp);
				}
			}
		}
		ed.commit();
		return;
    }
    public void setPreferences2ExInfo(){
		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
		// ����
		memory.setGender(pre.getString("list_preference", ""));
		memory.setGofuTsume(
			pre.getBoolean("chk_prefer_pwgo", false),
			pre.getBoolean("chk_prefer_pwnl", false),
			pre.getBoolean("chk_prefer_dfgo", false),
			pre.getBoolean("chk_prefer_dfnl", false)
			);
		return;
    }
    public void setSkillsPreferences2Memory(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	String[] res = new String[13];
    	res[0] = ComPreferences.getSkills01(pre, ComPreferences.CPF_MODE_MAIN);
    	res[1] = ComPreferences.getSkills02(pre, ComPreferences.CPF_MODE_MAIN);
    	res[2] = ComPreferences.getSkills03(pre, ComPreferences.CPF_MODE_MAIN);
    	res[3] = ComPreferences.getSkills04(pre, ComPreferences.CPF_MODE_MAIN);
    	res[4] = ComPreferences.getSkills05(pre, ComPreferences.CPF_MODE_MAIN);
    	res[5] = ComPreferences.getSkills06(pre, ComPreferences.CPF_MODE_MAIN);
    	res[6] = ComPreferences.getSkills07(pre, ComPreferences.CPF_MODE_MAIN);
    	res[7] = ComPreferences.getSkills08(pre, ComPreferences.CPF_MODE_MAIN);
    	res[8] = ComPreferences.getSkills09(pre, ComPreferences.CPF_MODE_MAIN);
    	res[9] = ComPreferences.getSkills10(pre, ComPreferences.CPF_MODE_MAIN);
    	res[10] = ComPreferences.getSkills11(pre, ComPreferences.CPF_MODE_MAIN);
    	res[11] = ComPreferences.getSkills12(pre, ComPreferences.CPF_MODE_MAIN);
    	res[12] = ComPreferences.getSkills13(pre, ComPreferences.CPF_MODE_MAIN);
    	String temp = CsvStringToList.scatNullSkip(res, ",");
		memory.setReqSkills(CsvStringToList.split(temp, ","));
		return;
    }
    public void viewMessage(String t, String msg){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(t);
    	builder.setMessage(msg);
    	builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
    	builder.create();
    	builder.show();
    	return;
    }
    /**
     * 
     * @param t
     * @param msg
     * @return
     */
    public boolean typeChangeMessage(String t){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(t);
    	builder.setItems(R.array.buktype, new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String[] buktype = getResources().getStringArray(R.array.buktype);
				memory.getBuki().setString(buktype[which]);
				memory.ifNotTakeoff();
       			setTabBuki();
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
    private SkillVector reqSkill2SkillVector(){
    	SkillVector res = new SkillVector();
    	for(int idx=0; idx<memory.getReqSkillSize(); idx++){
    		String cur = memory.getReqSkills()[idx];
    		String key = skillInfo.getSkillName(cur);
    		Integer val = skillInfo.getSkillValue(cur);
    		res.put(key, val);
    	}
    	return res;
    }
    private void initClickListener(){
        // ����C���e���g�ݒ�
		intentBuki=new Intent(this, BukiActivity.class);
//		intentBuki=new Intent("android.intent.action.MAIN");
//		intentBuki.setClassName("jp.skd.lilca.mhf.buki","jp.skd.lilca.mhf.buki.BukiActivity");
//		Uri uri0=Uri.parse("market://search?q=pname:jp.skd.lilca.mhf.buki");
//		ngIntentBuki=new Intent(Intent.ACTION_VIEW,uri0);
        // �h��C���e���g�ݒ�
		intentBougu=new Intent(this, SoubiActivity.class);
//		intentBougu=new Intent("android.intent.action.MAIN");
//		intentBougu.setClassName("jp.skd.lilca.mhf.soubi","jp.skd.lilca.mhf.soubi.SoubiActivity");
//		Uri uri1=Uri.parse("market://search?q=pname:jp.skd.lilca.mhf.soubi");
//		ngIntentBougu=new Intent(Intent.ACTION_VIEW,uri1);
        // ��C���e���g�ݒ�
		intentSousyoku=new Intent(this, SkillActivity.class);
//		intentSousyoku=new Intent("android.intent.action.MAIN");
//		intentSousyoku.setClassName("jp.skd.lilca.mhf.skill","jp.skd.lilca.mhf.skill.SkillActivity");
//		Uri uri2=Uri.parse("market://search?q=pname:jp.skd.lilca.mhf.skill");
//		ngIntentSousyoku=new Intent(Intent.ACTION_VIEW,uri2);
        // �J�t�C���e���g�ݒ�
		intentKafus=new Intent(this, KafuActivity.class);
//		intentKafus=new Intent("android.intent.action.MAIN");
//		intentKafus.setClassName("jp.skd.lilca.mhf.kafus","jp.skd.lilca.mhf.kafus.KafuActivity");
//		Uri uri3=Uri.parse("market://search?q=pname:jp.skd.lilca.mhf.kafus");
//		ngIntentKafus=new Intent(Intent.ACTION_VIEW,uri3);
        // cLoud�C���e���g�ݒ�
		intent_cLoud=new Intent("android.intent.action.MAIN");
		intent_cLoud.setClassName("jp.skd.lilca.mhf.c_loud","jp.skd.lilca.mhf.c_loud.cLoudMainActivity");
		Uri uri4=Uri.parse("market://search?q=pname:jp.skd.lilca.mhf.c_loud");
		ngIntent_cLoud=new Intent(Intent.ACTION_VIEW,uri4);
        // �Z�b�g�I���C���e���g�ݒ�
		intent_setchoice=new Intent(this, SetChoiceActivity.class);

		// ����p�̃��X�i�[
		listenerBuki = new OnClickListener(){
        	@Override
        	public void onClick(View view) {
        		try{
        			Intent intent = intentBuki;
        			intent.putExtra("owner is lilca","");
        			intent.putExtra("slots",Integer.toString(getItemBuki().getSpendedSlots()));
        			me.startActivityForResult(intent, REQUEST_BUKI);
        		}catch(Exception e){
        			AlertDialog.Builder alertDlg = new AlertDialog.Builder(me);
        			alertDlg.setTitle("�A�g�G���[");
        			alertDlg.setMessage("����I���ƘA�g�ł��܂���ł����B");
        			alertDlg.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener(){
        				@Override
        				public void onClick(DialogInterface dialog, int which) {
//                			me.startActivity(ngIntentBuki);
        					return;
        				}
        			});
        			alertDlg.create();
        			alertDlg.show();
        		}
        	}
        };
		// �h��p�̃��X�i�[
		listenerBougu = new OnClickListener(){
        	@Override
        	public void onClick(View view) {
        		try{
        			Intent intent = intentBougu;
        			intent.putExtra("owner is lilca","");
        			intent.putExtra("operation","add");
        			intent.putExtra("parts",bui);
        			intent.putExtra("type",memory.getBuki().getTypeString());
        			intent.putExtra("sex",memory.getGender());
        			SkillVector req = reqSkill2SkillVector();
        			SkillVector sou = memory.getTotalSkillVector();
        			SkillVector bou = getItemBougu().getSkillVector();
        			SkillVector sa  = req.sub(sou.mul(req.unit())).add(bou.mul(req.unit()));
        			sa.roundingup(0);
        			intent.putExtra("skills", sa.exportString());
        			intent.putExtra("slots",Integer.toString(getItemBougu().getSpendedSlots()));
        			me.startActivityForResult(intent, REQUEST_BOUGU);
        		}catch(Exception e){
        			AlertDialog.Builder alertDlg = new AlertDialog.Builder(me);
        			alertDlg.setTitle("�A�g�G���[");
        			alertDlg.setMessage("�h��I���ƘA�g�ł��܂���ł����B");
        			alertDlg.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener(){
        				@Override
        				public void onClick(DialogInterface dialog, int which) {
//                			me.startActivity(ngIntentBougu);
        					return;
        				}
        			});
        			alertDlg.create();
        			alertDlg.show();
        		}
        	}
        };
		listenerBouguMod = new OnClickListener(){
        	@Override
        	public void onClick(View view) {
        		try{
        			Intent intent = intentBougu;
        			intent.putExtra("owner is lilca","");
        			intent.putExtra("operation","mod");
        			intent.putExtra("parts",bui);
        			intent.putExtra("type",memory.getBuki().getTypeString());
        			intent.putExtra("sex",memory.getGender());
        			intent.putExtra("sp",memory.getBougu(bui).isSP());
        			SkillVector req = reqSkill2SkillVector();
        			SkillVector sou = memory.getTotalSkillVector();
        			SkillVector bou = getItemBougu().getSkillVector();
        			SkillVector sa  = req.sub(sou.mul(req.unit())).add(bou.mul(req.unit()));
        			sa.roundingup(0);
        			intent.putExtra("skills", sa.exportString());
        			intent.putExtra("slots",Integer.toString(getItemBougu().getSpendedSlots()));
        			me.startActivityForResult(intent, REQUEST_BOUGU);
        		}catch(Exception e){
        			AlertDialog.Builder alertDlg = new AlertDialog.Builder(me);
        			alertDlg.setTitle("�A�g�G���[");
        			alertDlg.setMessage("�h��I���ƘA�g�ł��܂���ł����B");
        			alertDlg.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener(){
        				@Override
        				public void onClick(DialogInterface dialog, int which) {
//                			me.startActivity(ngIntentBougu);
        					return;
        				}
        			});
        			alertDlg.create();
        			alertDlg.show();
        		}
        	}
        };
		// �����p�̃��X�i�[
		listenerSousyoku = new OnClickListener(){
        	@Override
        	public void onClick(View view) {
        		try{
        			currentItem = -1;
           			Intent intent = intentSousyoku;
        			intent.putExtra("owner is lilca","");
        			intent.putExtra("slots",emptySlots);
        			intent.putExtra("sp",spFlag);
        			SkillVector req = reqSkill2SkillVector();
        			SkillVector sou = memory.getTotalSkillVector();
        			SkillVector sa  = req.sub(sou.mul(req.unit()));
        			sa.roundingup(0);
        			intent.putExtra("skills", sa.exportString());
        			me.startActivityForResult(intent, REQUEST_SOUSYOKU);
        		}catch(Exception e){
        			AlertDialog.Builder alertDlg = new AlertDialog.Builder(me);
        			alertDlg.setTitle("�A�g�G���[");
        			alertDlg.setMessage("��I���ƘA�g�ł��܂���ł����B");
        			alertDlg.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener(){
        				@Override
        				public void onClick(DialogInterface dialog, int which) {
//                			me.startActivity(ngIntentSousyoku);
        					return;
        				}
        			});
        			alertDlg.create();
        			alertDlg.show();
        		}
        	}
        };
		listenerSousyokuArray[0] = new OnClickListener(){
        	@Override
        	public void onClick(View view) {
        		modSousyokuListenerProc(0);
        	}
        };
		listenerSousyokuArray[1] = new OnClickListener(){
        	@Override
        	public void onClick(View view) {
        		modSousyokuListenerProc(1);
        	}
        };
		listenerSousyokuArray[2] = new OnClickListener(){
        	@Override
        	public void onClick(View view) {
        		modSousyokuListenerProc(2);
        	}
        };
		// �J�t�p�̃��X�i�[
		listenerKafus = new OnClickListener(){
        	@Override
        	public void onClick(View view) {
        		try{
        			currentItem = -1;
        			Intent intent = intentKafus;
        			intent.putExtra("owner is lilca","");
        			intent.putExtra("operation","add");
        			int temp = memory.getEmptyKafusSlots();
        			intent.putExtra("slots", Integer.toString(temp));
        			SkillVector req = reqSkill2SkillVector();
        			SkillVector sou = memory.getTotalSkillVector();
        			SkillVector sa  = req.sub(sou.mul(req.unit()));
        			sa.roundingup(0);
        			intent.putExtra("skills", sa.exportString());
        			me.startActivityForResult(intent, REQUEST_KAFUS);
        		}catch(Exception e){
        			AlertDialog.Builder alertDlg = new AlertDialog.Builder(me);
        			alertDlg.setTitle("�A�g�G���[");
        			alertDlg.setMessage("�J�t�I���ƘA�g�ł��܂���ł����B");
        			alertDlg.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener(){
        				@Override
        				public void onClick(DialogInterface dialog, int which) {
//                			me.startActivity(ngIntentKafus);
        					return;
        				}
        			});
        			alertDlg.create();
        			alertDlg.show();
        		}
        	}
        };
		listenerKafusArray[0] = new OnClickListener(){
        	@Override
        	public void onClick(View view) {
        		modKafuListenerProc(0);
        	}
        };
		listenerKafusArray[1] = new OnClickListener(){
        	@Override
        	public void onClick(View view) {
        		modKafuListenerProc(1);
        	}
        };
		// �폜�̃��X�i�[
       	listenerDelete[0] = new OnClickListener(){
       		@Override
       		public void onClick(View view) {
       			// �폜
       			delItem(0);
       			// �폜���b�Z�[�W
       			viewMessage("�폜���܂���", "���ɍ폜�ς݂ł�");
       		}
       	};
       	listenerDelete[1] = new OnClickListener(){
       		@Override
       		public void onClick(View view) {
       			// �폜
       			delItem(1);
       			// �폜���b�Z�[�W
       			viewMessage("�폜���܂���", "���ɍ폜�ς݂ł�");
       		}
       	};
       	listenerDelete[2] = new OnClickListener(){
       		@Override
       		public void onClick(View view) {
       			// �폜
       			delItem(2);
       			// �폜���b�Z�[�W
       			viewMessage("�폜���܂���", "���ɍ폜�ς݂ł�");
       		}
       	};
		// �h��폜�̃��X�i�[
       	listenerBouguDel = new OnClickListener(){
       		@Override
       		public void onClick(View view) {
       			// �폜
       			if(currentTab.equals(TAG[0])){memory.getBuki().remove();setTabBuki();}
       			else if(currentTab.equals(TAG[1])){memory.getAtama().remove();setTabAtama();}
       			else if(currentTab.equals(TAG[2])){memory.getDou().remove();setTabDou();}
       			else if(currentTab.equals(TAG[3])){memory.getUde().remove();setTabUde();}
       			else if(currentTab.equals(TAG[4])){memory.getKoshi().remove();setTabKoshi();}
       			else if(currentTab.equals(TAG[5])){memory.getAshi().remove();setTabAshi();}
       			else if(currentTab.equals(TAG[6])){memory.getKafus().remove(0);setTabKafu();}
       			// �폜���b�Z�[�W
       			viewMessage("�폜���܂���", "���ɍ폜�ς݂ł�");
       		}
       	};
		// �Z�b�g�I��
		listenerSetChoice = new OnClickListener(){
        	@Override
        	public void onClick(View view) {
        		// �m�F
            	AlertDialog.Builder builder = new AlertDialog.Builder(me);
            	builder.setTitle("�Z�b�g�I�����܂����H");
            	builder.setMessage("���\���Ԃ�����܂����A���܂����H");
            	builder.setNegativeButton("�L�����Z��", new android.content.DialogInterface.OnClickListener(){
        			@Override
        			public void onClick(DialogInterface dialog, int which) {
        				return;
        			}
            	});
            	builder.setPositiveButton("���I", new android.content.DialogInterface.OnClickListener(){
        			@Override
        			public void onClick(DialogInterface dialog, int which) {
                		//
               			Intent intent = intent_setchoice;
               			intent.putExtra("owner is lilca","");
            			intent.putExtra("parts",bui);
            			intent.putExtra("type",memory.getBuki().getTypeString());
            			intent.putExtra("sex",memory.getGender());
            			SkillVector req = reqSkill2SkillVector();
            			SkillVector sou = memory.getTotalSkillVector();
            			SkillVector bou = getItemBougu().getSkillVectorWithSousyoku();
            			SkillVector sa  = req.sub(sou.mul(req.unit())).add(bou.mul(req.unit()));
            			sa.roundingup(0);
            			intent.putExtra("skills", sa.exportString());
               			me.startActivityForResult(intent, REQUEST_SETCHOICE);
        				return;
        			}
        		});
            	builder.create();
            	builder.show();
       			return;
        	}
        };
		// �X�L�������}���i�J�t�j
		listenerNegative = new OnClickListener(){
        	@Override
        	public void onClick(View view) {
    			int restSlots = memory.getEmptyKafusSlots();
    			// �󂫃X���b�g�Ȃ�
    			if( restSlots == 0 ){
    				viewMessage("�󂫃X���b�g�Ȃ�", "�X���b�g�ɋ󂫂�����܂���");
    				return;
    			}
       			dispYokuseiList();
        		return;
        	}
        };
		// �X�L�������}���i�h��̎�j
		listenerNegativeBougu = new OnClickListener(){
        	@Override
        	public void onClick(View view) {
        		// �󂫃X���擾
        		int rest	= 0;
        		if( bui.equals("��") )
        			rest	= memory.getAtama().getEmptySlots();
        		else
        		if( bui.equals("��") )
        			rest	= memory.getDou().getEmptySlots();
        		else
        		if( bui.equals("�r") )
        			rest	= memory.getUde().getEmptySlots();
        		else
        		if( bui.equals("��") )
        			rest	= memory.getKoshi().getEmptySlots();
        		else
        		if( bui.equals("�r") )
        			rest	= memory.getAshi().getEmptySlots();
    			// �󂫃X���b�g�Ȃ�
    			if( rest == 0 ){
    				viewMessage("�󂫃X���b�g�Ȃ�", "�X���b�g�ɋ󂫂�����܂���");
    				return;
    			}
       			dispYokuseiList();
        		return;
        	}
        };
		// �X�L�������}���i����̎�j
		listenerNegativeBuki = new OnClickListener(){
        	@Override
        	public void onClick(View view) {
        		// �󂫃X���擾
        		int rest	= memory.getBuki().getEmptySlots();
    			// �󂫃X���b�g�Ȃ�
    			if( rest == 0 ){
    				viewMessage("�󂫃X���b�g�Ȃ�", "�X���b�g�ɋ󂫂�����܂���");
    				return;
    			}
       			dispYokuseiList();
        		return;
        	}
        };
        return;
    }
	private void initSetListener(){
        Button btn0 = (Button)findViewById(R.id.btnControl0);
        Button btn1 = (Button)findViewById(R.id.btnControl1);
        Button btn2 = (Button)findViewById(R.id.btnControl2);
        btn0.setOnClickListener( new OnClickListener(){
			@Override
			public void onClick(View v) {
				saveMessage();
				return;
			}
        });
        btn1.setOnClickListener( new OnClickListener(){
			@Override
			public void onClick(View v) {
				final Intent intentPre = new Intent(me, MyPreferences.class);
				intentPre.putExtra("mode", "easy");
				startActivityForResult(intentPre, REQUEST_PREFERENCES);
				return;
			}
        });
        btn2.setOnClickListener( new OnClickListener(){
			@Override
			public void onClick(View v) {
				setReqSkillsButton();
				return;
			}
        });
        Button btnAtamaSC	= (Button) this.findViewById(R.id.setchoicebtn01);
        Button btnDouSC		= (Button) this.findViewById(R.id.setchoicebtn02);
        Button btnUdeSC		= (Button) this.findViewById(R.id.setchoicebtn03);
        Button btnKoshiSC	= (Button) this.findViewById(R.id.setchoicebtn04);
        Button btnAshiSC	= (Button) this.findViewById(R.id.setchoicebtn05);
        Button btnNega00	= (Button) this.findViewById(R.id.negativeskill00);
        Button btnNega01	= (Button) this.findViewById(R.id.negativeskill01);
        Button btnNega02	= (Button) this.findViewById(R.id.negativeskill02);
        Button btnNega03	= (Button) this.findViewById(R.id.negativeskill03);
        Button btnNega04	= (Button) this.findViewById(R.id.negativeskill04);
        Button btnNega05	= (Button) this.findViewById(R.id.negativeskill05);
        Button btnNega06	= (Button) this.findViewById(R.id.negativeskill06);
        btnAtamaSC.setOnClickListener(listenerSetChoice);
        btnDouSC.setOnClickListener(listenerSetChoice);
        btnUdeSC.setOnClickListener(listenerSetChoice);
        btnKoshiSC.setOnClickListener(listenerSetChoice);
        btnAshiSC.setOnClickListener(listenerSetChoice);
        btnNega00.setOnClickListener(listenerNegativeBuki);
        btnNega01.setOnClickListener(listenerNegativeBougu);
        btnNega02.setOnClickListener(listenerNegativeBougu);
        btnNega03.setOnClickListener(listenerNegativeBougu);
        btnNega04.setOnClickListener(listenerNegativeBougu);
        btnNega05.setOnClickListener(listenerNegativeBougu);
        btnNega06.setOnClickListener(listenerNegative);
		return;
	}
    private void delItem(int a){
			// �폜
			if(currentTab.equals(TAG[0])){memory.getBuki().getSousyokuList().remove(a);setTabBuki();}
			else if(currentTab.equals(TAG[1])){memory.getAtama().getSousyokuList().remove(a);setTabAtama();}
			else if(currentTab.equals(TAG[2])){memory.getDou().getSousyokuList().remove(a);setTabDou();}
			else if(currentTab.equals(TAG[3])){memory.getUde().getSousyokuList().remove(a);setTabUde();}
			else if(currentTab.equals(TAG[4])){memory.getKoshi().getSousyokuList().remove(a);setTabKoshi();}
			else if(currentTab.equals(TAG[5])){memory.getAshi().getSousyokuList().remove(a);setTabAshi();}
			else if(currentTab.equals(TAG[6])){memory.getKafus().remove(a);setTabKafu();}
    	return;
    }
    private Buki getItemBuki(){
		// �I�����ꂽ������擾
    	Buki res = null;
		if(currentTab.equals(TAG[0])){res=memory.getBuki();}
		return res;
    }
    private Bougu getItemBougu(){
		// �I�����ꂽ�h����擾
    	Bougu res = null;
		if(currentTab.equals(TAG[1])){res=memory.getAtama();}
		else if(currentTab.equals(TAG[2])){res=memory.getDou();}
		else if(currentTab.equals(TAG[3])){res=memory.getUde();}
		else if(currentTab.equals(TAG[4])){res=memory.getKoshi();}
		else if(currentTab.equals(TAG[5])){res=memory.getAshi();}
		return res;
    }
    private Sousyoku getItemSousyoku(int a){
		// �I�����ꂽ�����i���擾
    	Sousyoku res = null;
		if(currentTab.equals(TAG[0])){res=memory.getBuki().getSousyokuList().get(a);}
		else if(currentTab.equals(TAG[1])){res=memory.getAtama().getSousyokuList().get(a);}
		else if(currentTab.equals(TAG[2])){res=memory.getDou().getSousyokuList().get(a);}
		else if(currentTab.equals(TAG[3])){res=memory.getUde().getSousyokuList().get(a);}
		else if(currentTab.equals(TAG[4])){res=memory.getKoshi().getSousyokuList().get(a);}
		else if(currentTab.equals(TAG[5])){res=memory.getAshi().getSousyokuList().get(a);}
		return res;
    }
    private Kafus getItemKafu(int a){
		// �I�����ꂽ�J�t���擾
    	Kafus res = null;
		if(currentTab.equals(TAG[6])){res=memory.getKafus().get(a);}
		return res;
    }
    private void modSousyokuListenerProc(int idx){
		try{
			currentItem = idx;
   			Intent intent = intentSousyoku;
			intent.putExtra("owner is lilca","");
			intent.putExtra("sp",spFlag);
			SkillVector req = reqSkill2SkillVector();
			SkillVector sou = memory.getTotalSkillVector();
			SkillVector ss = getItemSousyoku(currentItem).getSkillVector();
			SkillVector sa  = req.sub(sou.mul(req.unit())).add(ss.mul(req.unit()));
			sa.roundingup(0);
			intent.putExtra("skills", sa.exportString());
			intent.putExtra("slots",Integer.toString(getItemSousyoku(currentItem).getSlotCost()));
			me.startActivityForResult(intent, REQUEST_SOUSYOKU);
		}catch(Exception e){
			AlertDialog.Builder alertDlg = new AlertDialog.Builder(me);
			alertDlg.setTitle("�A�g�G���[");
			alertDlg.setMessage("��I���ƘA�g�ł��܂���ł����B");
			alertDlg.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
//        			me.startActivity(ngIntentSousyoku);
					return;
				}
			});
			alertDlg.create();
			alertDlg.show();
		}
		return;
    }
    private void modKafuListenerProc(int idx){
		try{
			currentItem = idx;
			Intent intent = intentKafus;
			intent.putExtra("owner is lilca","");
			intent.putExtra("operation","mod");
			SkillVector req = reqSkill2SkillVector();
			SkillVector sou = memory.getTotalSkillVector();
			SkillVector kf = getItemKafu(currentItem).getSkillVector();
			SkillVector sa  = req.sub(sou.mul(req.unit())).add(kf.mul(req.unit()));
			sa.roundingup(0);
			intent.putExtra("skills", sa.exportString());
			intent.putExtra("slots", Integer.toString(getItemKafu(currentItem).getSlotCost()));
			me.startActivityForResult(intent, REQUEST_KAFUS);
		}catch(Exception e){
			AlertDialog.Builder alertDlg = new AlertDialog.Builder(me);
			alertDlg.setTitle("�A�g�G���[");
			alertDlg.setMessage("�J�t�I���ƘA�g�ł��܂���ł����B");
			alertDlg.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
//        			me.startActivity(ngIntentKafus);
					return;
				}
			});
			alertDlg.create();
			alertDlg.show();
		}
    	return;
    }
    private void setParameters(Bundle extras){
		datapos.cLoudFlag	= extras.getBoolean("cloudflag"); 
		datapos.dataid		= (Integer)extras.getInt("choiceid"); 
		String choiced		= (String)extras.getString("choiceddata");
		this.memory.setAllElementsToString(choiced);
    	return;
    }
    cLoudResult signinres;
    private void doSignin(){
    	signinres = null;
    	// �X���b�h����
    	new Thread(new Runnable(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					@Override
					public void run(){
				    	progressDlg2 = ProgressDialog.show(me, "cLoud�փT�C���C��","Please wait...", false);
				    	progressDlg2.show();
					}
				});
				// �X�V
				try {
					signinres = signin();
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.post(new Runnable(){
					@Override
					public void run(){
						if(progressDlg2 != null & progressDlg2.isShowing() )
							progressDlg2.dismiss();
						if(signinres!=null)
							Toast.makeText(me, signinres.getHeadValue("message"), Toast.LENGTH_LONG).show();
						else
							Toast.makeText(me, "cLoud�ɃA�N�Z�X�ł��܂���ł���", Toast.LENGTH_LONG).show();
					}
				});
			}
    	}).start();
    	return;
    }
    private cLoudResult signin(){
    	cLoudResult res = null;
		// cLoud�A�N�Z�X
		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
		String email = pre.getString("email_preference", "");
		String secret = pre.getString("password_preference", "");
		if(email.equals("") || secret.equals("")){
			return res;
		}
		try {
			res = skd.connect(email, secret);
		}catch (Exception e) {
			e.printStackTrace();
		}
    	return res;
    }
    private void dispYokuseiList(){
    	// �������X�g�擾
		Bougu[] list = new Bougu[5];
		list[0] = memory.getAtama();
		list[1] = memory.getDou();
		list[2] = memory.getUde();
		list[3] = memory.getKoshi();
		list[4] = memory.getAshi();
		SkillValueList a		= new SkillValueList(memory.getBuki(), list, memory.getKafus(), skillInfo.getData(), false);
		ArrayList<String> ss	= a.getBootedList();
		String[] bootedList		= new String[ss.size()];
		for(int idx=0; idx<ss.size(); idx++)
			bootedList[idx]		= ss.get(idx);
		// �����X�L�����[��
		if( ss.size() == 0 ){
			viewMessage("�����X�L���Ȃ�", "�������Ă���X�L��������܂���");
			return;
		}
		// �_�C�A���O
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("�}���������X�L���̑I��");
    	builder.setItems(bootedList, new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// �ΏۃX�L���擾
				Bougu[] list = new Bougu[5];
				list[0] = memory.getAtama();
				list[1] = memory.getDou();
				list[2] = memory.getUde();
				list[3] = memory.getKoshi();
				list[4] = memory.getAshi();
    			SkillValueList a = new SkillValueList(memory.getBuki(), list, memory.getKafus(), skillInfo.getData(), false);
    			ArrayList<String> ss	= a.getBootedSkillList();
    			String target			= ss.get(which);
        		try{
        			int tabNo	= ((MainActivity) me).getCurrentTab();
        			if( tabNo == 0 )
        				((MainActivity) me).yokuseiSousyoku(target);
        			else
           			if( tabNo == 1 )
           				((MainActivity) me).yokuseiSousyoku(target);
          			else
           			if( tabNo == 2 )
           				((MainActivity) me).yokuseiSousyoku(target);
           			else
           			if( tabNo == 3 )
           				((MainActivity) me).yokuseiSousyoku(target);
           			else
           			if( tabNo == 4 )
           				((MainActivity) me).yokuseiSousyoku(target);
           			else
           			if( tabNo == 5 )
           				((MainActivity) me).yokuseiSousyoku(target);
           			else
        			if( tabNo == 6 )
        				((MainActivity) me).yokuseiKafus(target);
        			else
        				return;
        		}catch(Exception e){
        			e.printStackTrace();
        		}
				return;
			}
		});
    	builder.create();
    	builder.show();
    	return;
    }
    private void yokuseiSousyoku(String target) throws Exception{
		currentItem = -1;
		Intent intent = intentSousyoku;
		intent.putExtra("owner is lilca","");
		intent.putExtra("operation","negative");
		intent.putExtra("slots",emptySlots);
		intent.putExtra("sp",spFlag);
		SkillVector sv	= new SkillVector(target+"-1");
		intent.putExtra("skills", sv.exportString());
		me.startActivityForResult(intent, REQUEST_SOUSYOKU);
    	return;
    }
    private void yokuseiKafus(String target) throws Exception{
		currentItem		= -1;
		Intent intent	= intentKafus;
		intent.putExtra("owner is lilca","");
		intent.putExtra("operation","negative");
		int temp = memory.getEmptyKafusSlots();
		intent.putExtra("slots", Integer.toString(temp));
		SkillVector sv	= new SkillVector(target+"-1");
		intent.putExtra("skills", sv.exportString());
		me.startActivityForResult(intent, REQUEST_KAFUS);
    	return;
    }
    private int getCurrentTab(){
    	for(int idx=0; idx<TAG.length; idx++)
    		if( currentTab.equals(TAG[idx]) )
    			return idx;
    	return -1;
    }
}