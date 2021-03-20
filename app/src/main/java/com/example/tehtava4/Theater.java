package com.example.tehtava4;

import android.sax.StartElementListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Theater {

    private int id;
    private String name;

    public Theater(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Date makeDate(Date date, SimpleDateFormat format, String sDate) {
        try {
            String array[] = sDate.split("\\.");
            if (array.length == 3) {
                date.setDate(Integer.parseInt(array[0]));
                date.setMonth(Integer.parseInt(array[1]) - 1);
                date.setYear(Integer.parseInt(array[2]) - 1900);
            }
        } catch (NullPointerException e) {
            //System.out.println("The given date wasn't in the proper format");
        }
        date.setHours(date.getHours() + 2);
        date.setMinutes(0);
        date.setSeconds(0);
        return date;
    }



    public ArrayList<Show> getShows (String sDate, String starting, String ending) {

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        date = makeDate(date, format, sDate);

        String url = "https://www.finnkino.fi/xml/Schedule/?area=" + String.valueOf(id) + "&dt=" +
                String.valueOf(date.getDate()) + "." + String.valueOf(date.getMonth() + 1) + "." + String.valueOf(date.getYear() + 1900);
        System.out.println("URL: " + url);

        String showStart;
        String showEnd;
        String title;
        String startMinutes;
        String endMinutes;

        String headline;
        String arrayStart[];
        String arrayEnd[];

        ArrayList<Show> list = new ArrayList<>();

        try {
            arrayStart = starting.split("\\:");
            //System.out.println("arrayStart koko: " + arrayStart.length);
            if (arrayStart.length != 2) {
                starting = "00:00";
                arrayStart = starting.split("\\:");
                //System.out.println("Starting klo: " + arrayStart[0] + ":" + arrayStart[1] + " - if-haara");
            } else {
                //System.out.println("Starting klo: " + arrayStart[0] + ":" + arrayStart[1] + " - else-haara");
            }
        } catch (NullPointerException e) {
            starting = "00:00";
            arrayStart = starting.split(":");
            //System.out.println("Starting klo: " + arrayStart[0] + ":" + arrayStart[1] + " - catch-haara");
        }

        try {
            arrayEnd = ending.split("\\:");
            //System.out.println("arrayEnd koko: " + arrayEnd.length);
            if (arrayEnd.length != 2) {
                ending = "23:59";
                arrayEnd = ending.split("\\:");
                //System.out.println("Ending klo: " + arrayEnd[0] + ":" + arrayEnd[1] + " - if-haara");
            } else {
                //System.out.println("Ending klo: " + arrayEnd[0] + ":" + arrayEnd[1] + " - else-haara");
            }
        } catch (NullPointerException e) {
            ending = "23:59";
            arrayEnd = ending.split("\\:");
            //System.out.println("Ending klo: " + arrayEnd[0] + ":" + arrayEnd[1] + " - catch-haara");
        }

        try {
            SimpleDateFormat hourMin = new SimpleDateFormat("hh:mm");
            Date startingTime = getDate(starting, hourMin);
            startingTime.setDate(date.getDate());
            startingTime.setMonth(date.getMonth());
            startingTime.setYear(date.getYear());
            startingTime.setHours(Integer.parseInt(arrayStart[0]));
            startingTime.setMinutes(Integer.parseInt(arrayStart[1]));

            //System.out.println("1. getDate() (startingTime) läpi: " + startingTime);

            Date endingTime = getDate(ending, hourMin);
            endingTime.setDate(date.getDate());
            endingTime.setMonth(date.getMonth());
            endingTime.setYear(date.getYear());
            endingTime.setHours(Integer.parseInt(arrayEnd[0]));
            endingTime.setMinutes(Integer.parseInt(arrayEnd[1]));

            //System.out.println("2. getDate() (endingTime) läpi: " + endingTime);

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(url);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");
            list.clear();

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    showStart = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                    showEnd = element.getElementsByTagName("dttmShowEnd").item(0).getTextContent();
                    title = element.getElementsByTagName("Title").item(0).getTextContent();


                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date showStartDate = getDate(showStart, dt);
                    Date showEndDate = getDate(showEnd, dt);


                    if (showEndDate.getMinutes() < 10) {
                        endMinutes = "0" + String.valueOf(showEndDate.getMinutes());
                    } else {
                        endMinutes = String.valueOf(showEndDate.getMinutes());
                    }

                    if (showStartDate.getMinutes() < 10) {
                        startMinutes = "0" + String.valueOf(showStartDate.getMinutes());
                    } else {
                        startMinutes = String.valueOf(showStartDate.getMinutes());
                    }

                    headline = title + ": " + showStartDate.getHours() + ":" + startMinutes + " - "
                            + showEndDate.getHours() + ":" + endMinutes;

                    Show show = new Show(title, showStartDate, showEndDate, headline);

                    System.out.println("showStart: " + showStart);
                    System.out.println("showStartDate: " + showStartDate);
                    System.out.println("startingTime: " + startingTime);
                    System.out.println("endingTime: " + endingTime);

                    System.out.println(".");
                    System.out.println("-");
                    System.out.println("_");

                    if (showStartDate.after(startingTime) && showStartDate.before(endingTime)) {
                        show.setInTimeFrameTrue();
                    } else {
                        show.setInTimeFrameFalse();
                    }

                    if (show.isInTimeFrame()) {
                        list.add(show);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Date getDate(String body, SimpleDateFormat format) throws ParseException {
        Date date, today = new Date();

        try {
            String array[] = body.split("\\:");
            if (array.length == 3) {
                date = format.parse(body);
                //System.out.println("Pvm okei! - getDate-metodin if-haara");
            } else {
                date = today;
                date.setSeconds(0);
                //System.out.println("getDate-metodin else-haara");
            }
        } catch (NullPointerException e) {
            date = today;
            date.setSeconds(0);
            //System.out.println("getDate-metodin catch");
        }
        return date;
    }
}
