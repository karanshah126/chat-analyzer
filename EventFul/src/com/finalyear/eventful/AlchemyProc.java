package com.finalyear.eventful;

import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import java.io.*;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.alchemyapi.api.*;

public class AlchemyProc {
    static String sc="";
    static String ty="";
     Vector<String> keywords;
     Vector<String> type;
     Vector<Double> scor;
    public  void keywordDetect(String tex)throws IOException, SAXException,
            ParserConfigurationException, XPathExpressionException
    {
        // Create an AlchemyAPI object.
        AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromString("ac9996055d43910d27c4af9bf88ba7ef5f245633");
        
        Document doc;
        AlchemyAPI_KeywordParams kp=new AlchemyAPI_KeywordParams();
        kp.setSentiment(true);
        // Extract topic keywords for a text string.
        doc = alchemyObj.TextGetRankedKeywords(tex, kp);
        System.out.println(getStringFromDocument(doc));
        Document document=convertStringToDocument(getStringFromDocument(doc));
        document.normalize();
        Element rootElement = document.getDocumentElement();
        NodeList nodes = rootElement.getChildNodes();

        for(int i=0; i<nodes.getLength(); i++){
        Node node = nodes.item(i);
    
        if(node.getNodeName().equals("keywords"))
        {
            Element newElement =(Element) node;
            NodeList nodes2=newElement.getChildNodes();
                for(int j=0; j<nodes2.getLength();j++)  {
                    Node node2= nodes2.item(j);
                   
                        if(node2.getNodeName().equals("keyword"))
                        {
                            Element newerElement =(Element) node2;
                            NodeList nodes3=newerElement.getChildNodes();                            
                            for(int k=0; k<nodes3.getLength();k++){
                                Node node3=nodes3.item(k);
                                if(node3.getNodeName().equals("sentiment"))
                                {
                                    Element newestElement = (Element) node3;
                                    NodeList nodes4=newestElement.getChildNodes();
                                    for(int l=0; l<nodes4.getLength();l++){
                                        Node node4=nodes4.item(l);
                                        if(node4.getNodeName().equals("score"))
                                        {
                                            Element score =(Element) node4;
                                            NodeList scores=score.getChildNodes();
                                            //System.out.println(scores.item(0).getTextContent());
                                            scor.add(Double.parseDouble(scores.item(0).getTextContent()));
                                        }
                                        if(node4.getNodeName().equals("type"))
                                        {
                                            Element score =(Element) node4;
                                            NodeList scores=score.getChildNodes();
                                           // System.out.println(scores.item(0).getTextContent());
                                            type.add(scores.item(0).getTextContent());
                                        }
                                    }
                                }
                                if(node3.getNodeName().equals("text"))
                                {
                                    Element score =(Element) node3;
                                    NodeList scores=score.getChildNodes();
                                    //System.out.println(scores.item(0).getTextContent());
                                    keywords.add(scores.item(0).getTextContent()+"");
                                }
                            }
                        }
                        
                }
         }
        
        }
        
        
    }
    public void printVec()
    {
        for(int i=0;i<type.size()&&i<scor.size()&&i<keywords.size();i++)
        {
            System.out.println(keywords.elementAt(i)+" "+type.elementAt(i)+" "+scor.elementAt(i));
        }
    }
        
    
    // utility method
    private static String getStringFromDocument(Document doc) {
        try {
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);

            return writer.toString();
        } catch (TransformerException ex) {
            ex.printStackTrace();
            return null;
        }
        
    
    }
     private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try 
        {  
            builder = factory.newDocumentBuilder();  
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return null;
    }
}