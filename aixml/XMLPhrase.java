package com.ninigo2.aixml;

import com.ninigo2.ai.Evc;
import org.xml.sax.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yang on 2016/4/16.
 */
public class XMLPhrase {
    public static void phraseA(List<Evc> evCompares) throws Exception
    {
        SAXParserFactory factory =SAXParserFactory.newInstance();
        SAXParser parser=factory.newSAXParser();
        XMLReader reader=parser.getXMLReader();
        reader.setContentHandler(new MySaxContentHandler(evCompares));
        FileReader fileReader=new FileReader(new File("src\\com\\ninigo2\\aixml\\comparea.xml"));
        reader.parse(new InputSource(fileReader));
    }

    public static void phraseD(List<Evc> evCompares) throws Exception
    {
        SAXParserFactory factory =SAXParserFactory.newInstance();
        SAXParser parser=factory.newSAXParser();
        XMLReader reader=parser.getXMLReader();
        reader.setContentHandler(new MySaxContentHandler(evCompares));
        FileReader fileReader=new FileReader(new File("src\\com\\ninigo2\\aixml\\compared.xml"));
        reader.parse(new InputSource(fileReader));
    }

    private static class MySaxContentHandler implements ContentHandler
    {
        private List<Evc> evCompares=new ArrayList<Evc>();
        public MySaxContentHandler(List<Evc> evCompares)
        {
            this.evCompares=evCompares;
        }

        String elementName=null;
        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            elementName=qName;
        }
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            elementName=null;
        }
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if("p".equals(elementName))
            {
                int rank=Integer.valueOf(new String(ch,start,length).split(",")[0]);
                String compare=new String(ch,start,length).split(",")[1];

                evCompares.add(new Evc(rank,compare));
            }
        }
        @Override
        public void setDocumentLocator(Locator locator) {
        }
        @Override
        public void startDocument() throws SAXException {
        }
        @Override
        public void endDocument() throws SAXException {
        }
        @Override
        public void startPrefixMapping(String prefix, String uri) throws SAXException {
        }
        @Override
        public void endPrefixMapping(String prefix) throws SAXException {
        }
        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        }
        @Override
        public void processingInstruction(String target, String data) throws SAXException {
        }
        @Override
        public void skippedEntity(String name) throws SAXException {
        }
    }
}
