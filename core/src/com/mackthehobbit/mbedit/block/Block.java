package com.mackthehobbit.mbedit.block;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;

public class Block {
	
	public static final Block[] blocks = new Block[256];
	public static final BlockRenderer[] blockRenderers = new BlockRenderer[256];
	
	public final byte id;
	public final String name;
	public final boolean visible;
	public final boolean opaque; // bram, you do realise opaque means fully solid (ie. not transparent), right??
	private final HashMap<String, String> textures;
	
	public Block(int id, String name, boolean visible, boolean opaque) {
		if(blocks[id] != null) throw new RuntimeException("Duplicate block IDs @" + id);
		blocks[id] = this;
		blockRenderers[id] = BlockRenderer.normal;
		this.id = (byte) id;
		this.name = name;
		this.visible = visible;
		this.opaque = opaque;
		textures = new HashMap<String, String>();
	}
	
	public String getTexture(String direction) {
		if(textures.get(direction) == null) return textures.get("default");
		return textures.get(direction);
	}
	
	static {
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Gdx.files.internal("..\\android\\assets\\Data\\blocks.xml").file());
			doc.getDocumentElement().normalize();
			
			NodeList blocks = doc.getElementsByTagName("block");
			
			for(int i = 0; i < blocks.getLength(); i++) {
				Node block = blocks.item(i);
				if(block instanceof Element) {
					Element e = (Element) block;
					int id = Integer.parseInt(e.getAttribute("id"));
					String name = e.getElementsByTagName("name").item(0).getTextContent();
					boolean visible = true;
					if(e.getElementsByTagName("visible").item(0) != null)
						visible = Boolean.parseBoolean(e.getElementsByTagName("visible").item(0).getTextContent());
					boolean opaque = false;
					if(e.getElementsByTagName("opaque").item(0) != null)
						visible = Boolean.parseBoolean(e.getElementsByTagName("opaque").item(0).getTextContent());
					
					Block b = new Block(id, name, opaque, visible);
					
					if(e.getElementsByTagName("textures").item(0) != null) {
						NodeList textures = e.getElementsByTagName("textures").item(0).getChildNodes();
						for(int j = 0; j < textures.getLength(); j++) {
							Node texture = textures.item(j);
							if(texture.getNodeName().equals("#text")) continue;
							b.textures.put(texture.getNodeName(), texture.getTextContent());
						}
					}
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		blockRenderers[50] = BlockRenderer.torch;
		blockRenderers[107] = BlockRenderer.torch;
	}
	
}
