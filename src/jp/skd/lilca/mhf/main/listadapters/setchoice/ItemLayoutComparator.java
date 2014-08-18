package jp.skd.lilca.mhf.main.listadapters.setchoice;

import java.util.Comparator;

public class ItemLayoutComparator implements Comparator<Object> {

	@Override
	public int compare(Object obj1, Object obj2) {
		int num1;
		int num2;

		ItemListLayout lay1 = (ItemListLayout) obj1;
		ItemListLayout lay2 = (ItemListLayout) obj2;

		num1 = Integer.parseInt((String) lay1.getTextRight());
		num2 = Integer.parseInt((String) lay2.getTextRight());
		return num2 - num1;
	}

}