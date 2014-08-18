package jp.skd.lilca.mhf.web;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class cLoudResult {
	public static final String LIST_SEPARATOR = ";sep;";
	private ArrayList<String> headList;
	private ArrayList<String> bodyList;
	private HashMap<String, String> head;
	private HashMap<String, String> body;
	
	public cLoudResult(String xml){
		this.headList = new ArrayList<String>();
		this.bodyList = new ArrayList<String>();
		this.head = new HashMap<String, String>();
		this.body = new HashMap<String, String>();
		try{
			// ドキュメント生成
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbFactory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
			// ルート取得
			Element root = doc.getDocumentElement();
			// head部分取得
			NodeList headRoot = root.getElementsByTagName("head").item(0).getChildNodes();
			for(int idx=0; idx<headRoot.getLength(); idx++){
				Node item = headRoot.item(idx);
				if(item.getNodeType() == Node.ELEMENT_NODE){
					if(item.getNodeName().equals("list"))
						this.headList.add(item.getTextContent());
					else
						this.head.put(item.getNodeName(), item.getTextContent());
				}
			}
			// body部分取得
			NodeList bodyRoot = root.getElementsByTagName("body").item(0).getChildNodes();
			for(int idx=0; idx<bodyRoot.getLength(); idx++){
				Node item = bodyRoot.item(idx);
				if(item.getNodeType() == Node.ELEMENT_NODE){
					if(item.getNodeName().equals("list"))
						this.bodyList.add(item.getTextContent());
					else
						this.body.put(item.getNodeName(), item.getTextContent());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return;
	}
	public String getHeadValue(String key){
		if(this.head.get(key)==null)
			return "";
		return this.head.get(key);
	}
	public ArrayList<String> getHeadList(){
		return this.headList;
	}
	public String getBodyValue(String key){
		if(this.body.get(key)==null)
			return "";
		return this.body.get(key);
	}
	public ArrayList<String> getBodyList(){
		return this.bodyList;
	}
}
