package com.shawn.study.java.configuration.source;

import static com.shawn.study.java.configuration.constant.ConfigSourceOrdinal.CLASSPATH_XML_ORDINAL;

import com.shawn.study.java.configuration.exception.LoadConfigException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ClasspathXmlConfigSource extends ClasspathConfigSource {

  private static final String RESOURCE_NAME = "application.xml";
  private static final String CONFIG_SOURCE_NAME = "Classpath Xml Config Source";
  private static final Map<String, String> CONFIG_MAP = new HashMap<>();

  public ClasspathXmlConfigSource() {
    super(RESOURCE_NAME, CONFIG_SOURCE_NAME, CLASSPATH_XML_ORDINAL.getOrdinal());
  }

  @Override
  protected void load(InputStream classpathFile) {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder;
    try {
      documentBuilder = documentBuilderFactory.newDocumentBuilder();
      Document document = documentBuilder.parse(classpathFile);
      NodeList nodeList = document.getChildNodes();
      for (int i = 0; i < nodeList.getLength(); i++) {
        Node item = nodeList.item(i);
        if (item instanceof Element) {
          Element element = (Element) item;
          final String tagName = element.getTagName();
          String nodeValue = element.getTextContent();
          CONFIG_MAP.put(tagName, nodeValue);
        }
      }
    } catch (ParserConfigurationException | SAXException | IOException e) {
      throw new LoadConfigException("failed to load xml config", e);
    }
  }

  @Override
  public Set<String> getPropertyNames() {
    return CONFIG_MAP.keySet();
  }

  @Override
  public String getValue(String propertyName) {
    return CONFIG_MAP.get(propertyName);
  }
}
