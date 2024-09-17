package blackjack.Client.Gui.Util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * PreloadedAtlas loads an Atlas from the resource folder.
 * Finds sprites based on xml parsed.
 * Stores a map of the name and the location on the atlas.
 */
public class PreloadedAtlas extends Atlas {

    private HashMap<String, int[]> locationMap = new HashMap<>();

    public PreloadedAtlas(String atlasPath, String xmlPath) throws IOException {
        super(atlasPath);
        loadXML(xmlPath);
    }

    private void loadXML(String path) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document indexes = builder.parse(path);
            indexes.getDocumentElement().normalize();

            NodeList nodeList = indexes.getElementsByTagName("SubTexture");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String name = element.getAttribute("name");
                    int x = Integer.parseInt(element.getAttribute("x"));
                    int y = Integer.parseInt(element.getAttribute("y"));
                    int width = Integer.parseInt(element.getAttribute("width"));
                    int height = Integer.parseInt(element.getAttribute("height"));
                    locationMap.put(name, new int[]{x, y, width, height});
                }
                
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getSprite(String key) {
        if (!this.locationMap.keySet().contains(key)) {
            throw new IllegalArgumentException(key + " not in " + locationMap.keySet().toString());
        }
        int[] pos = locationMap.get(key);
        return getSprite(pos[0], pos[1], pos[2], pos[3]);
    }

}
