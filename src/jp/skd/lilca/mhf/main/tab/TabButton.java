package jp.skd.lilca.mhf.main.tab;

import jp.skd.lilca.mhf.main.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class TabButton extends FrameLayout {
	private LayoutInflater inflater;
	
	public TabButton(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
		return;
	}
	public TabButton(Context context, String title, int icon) {
		this(context);
		 View v = inflater.inflate(R.layout.tablayout, null);
		 ImageView iv = (ImageView) v.findViewById(R.id.tabicon);
		 iv.setImageResource(icon);
		 TextView tv = (TextView) v.findViewById(R.id.tabtext);
		 tv.setText(title);
		 addView(v);
		return;
	}

}
