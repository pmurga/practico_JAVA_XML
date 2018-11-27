package library;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLProcessor {
    private XPath xpath;
    
    public Node root(String fileXML, String root) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(fileXML);
            XPathFactory xpathFactory = XPathFactory.newInstance();
            xpath = xpathFactory.newXPath();
            return (Node) xpath.compile(root).evaluate(doc, XPathConstants.NODE);
        } catch (Exception e) {
            return null;
        }
    }

    public Node getNode(Node node, String path) {
        try {
            return (Node) xpath.compile(path).evaluate(node, XPathConstants.NODE);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean existsNode(Node node, String path) {
        Node n;
        try {
            n = (Node) xpath.compile(path).evaluate(node, XPathConstants.NODE);
        } catch (Exception e) {
            n = null;
        }
        return (n != null);
    }

    public NodeList getNodeList(Node node, String path) {
        try {
            return (NodeList) xpath.compile(path).evaluate(node, XPathConstants.NODESET);
        } catch (Exception e) {
            return null;
        }
    }

    public String getNodeData(Node node, String path) {
        try {
            return (String) xpath.compile(path).evaluate(node, XPathConstants.STRING);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean existsNodeData(Node node, String path) {
        String s;
        try {
            s = (String) xpath.compile(path).evaluate(node, XPathConstants.STRING);
        } catch (Exception e) {
            s = null;
        }
        return (s != null && !s.equals(""));
    }

    public StringList getNodeListData(Node node, String path) {
        try {
            StringList items = new StringList();
            NodeList list = (NodeList) xpath.compile(path).evaluate(node, XPathConstants.NODESET);
            for (int i = 0; i < list.getLength(); i++) {
                Node nodeString = list.item(i);
                items.add(nodeString.getNodeValue());
            }
            return items;
        } catch (Exception e) {
            return null;
        }
    }
}
