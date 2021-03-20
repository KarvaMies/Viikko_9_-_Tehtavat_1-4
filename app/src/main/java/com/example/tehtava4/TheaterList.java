package com.example.tehtava4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class TheaterList {

    private static TheaterList TL = null;

    ArrayList<Theater> theaterList = new ArrayList<>();
    ArrayList<String> nameList = new ArrayList<>();

    public static TheaterList getInstance() {
        if (TL == null) {
            TL = new TheaterList();
        }
        return TL;
    }

    public void readXML (String url) {
        int id;
        String name;

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(url);
            doc.getDocumentElement().normalize();

            theaterList.clear();
            nameList.clear();
            NodeList nList = doc.getDocumentElement().getElementsByTagName("TheatreArea");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    name = element.getElementsByTagName("Name").item(0).getTextContent();
                    id = Integer.parseInt(element.getElementsByTagName("ID").item(0).getTextContent());

                    Theater theater = new Theater(id, name);
                    theaterList.add(theater);
                    nameList.add(name);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public ArrayList <String> getNameList() {
        return nameList;
    }

    public ArrayList <Theater> getTheaterList() {
        return theaterList;
    }
}