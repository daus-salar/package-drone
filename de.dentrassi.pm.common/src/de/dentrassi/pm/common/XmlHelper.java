/*******************************************************************************
 * Copyright (c) 2014 Jens Reimann.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jens Reimann - initial API and implementation
 *******************************************************************************/
package de.dentrassi.pm.common;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlHelper
{
    public static final class NodeListIterator implements Iterator<Node>
    {
        private final NodeList list;

        private int index;

        public NodeListIterator ( final NodeList list )
        {
            this.list = list;
        }

        @Override
        public Node next ()
        {
            return this.list.item ( this.index++ );
        }

        @Override
        public boolean hasNext ()
        {
            return this.index < this.list.getLength ();
        }
    }

    /**
     * Iterate over the direct child elements of an element
     */
    public static final class ElementIterator implements Iterator<Element>
    {
        private final Element element;

        private int index;

        private final String name;

        public ElementIterator ( final Element element )
        {
            this ( element, null );
        }

        public ElementIterator ( final Element element, final String name )
        {
            this.element = element;
            this.name = name;
        }

        private Element peek ()
        {
            Node node;
            while ( ( node = this.element.getChildNodes ().item ( this.index ) ) != null )
            {
                if ( ! ( node instanceof Element ) )
                {
                    this.index++;
                    continue;
                }

                final Element ele = (Element)node;
                if ( this.name != null && !ele.getNodeName ().equals ( this.name ) )
                {
                    this.index++;
                    continue;
                }
            }
            // out of nodes
            return null;
        }

        @Override
        public Element next ()
        {
            final Element ele = peek ();
            if ( ele != null )
            {
                this.index++;
            }
            return ele;
        }

        @Override
        public boolean hasNext ()
        {
            // we could cache the result for quicker checking
            return peek () != null;
        }
    }

    private final DocumentBuilder db;

    private final TransformerFactory transformerFactory;

    private final XPathFactory xpathFactory;

    public XmlHelper ()
    {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance ();
        try
        {
            this.db = dbf.newDocumentBuilder ();
        }
        catch ( final ParserConfigurationException e )
        {
            throw new RuntimeException ( e );
        }

        this.transformerFactory = TransformerFactory.newInstance ();

        this.xpathFactory = XPathFactory.newInstance ();
    }

    public Document create ()
    {
        return this.db.newDocument ();
    }

    public Document parse ( final InputStream stream ) throws Exception
    {
        return this.db.parse ( stream );
    }

    public void write ( final Document doc, final OutputStream stream ) throws Exception
    {
        final Transformer transformer = this.transformerFactory.newTransformer ();
        final DOMSource source = new DOMSource ( doc );
        final StreamResult result = new StreamResult ( stream );
        transformer.setOutputProperty ( OutputKeys.INDENT, "yes" );
        transformer.setOutputProperty ( OutputKeys.ENCODING, "UTF-8" );
        transformer.setOutputProperty ( "{http://xml.apache.org/xslt}indent-amount", "2" );
        transformer.transform ( source, result );
    }

    public byte[] toData ( final Document doc ) throws Exception
    {
        final ByteArrayOutputStream out = new ByteArrayOutputStream ();
        write ( doc, out );
        out.close ();
        return out.toByteArray ();
    }

    public String getElementValue ( final Node element, final String path ) throws Exception
    {
        for ( final Node n : iter ( path ( element, path ) ) )
        {
            return text ( n );
        }
        return null;
    }

    private String text ( final Node node )
    {
        return node.getTextContent ();
    }

    public static Iterable<Element> iterElement ( final Element element, final String name )
    {
        return new Iterable<Element> () {

            @Override
            public Iterator<Element> iterator ()
            {
                return new ElementIterator ( element, name );
            }
        };
    }

    public static Iterable<Node> iter ( final NodeList list )
    {
        return new Iterable<Node> () {

            @Override
            public Iterator<Node> iterator ()
            {
                return new NodeListIterator ( list );
            }
        };
    }

    public NodeList path ( final Node node, final String path ) throws XPathExpressionException
    {
        final XPath xpath = this.xpathFactory.newXPath ();
        final XPathExpression expression = xpath.compile ( path );
        return (NodeList)expression.evaluate ( node, XPathConstants.NODESET );
    }

    public static Element addElement ( final Element parent, final String name )
    {
        final Element ele = parent.getOwnerDocument ().createElement ( name );
        parent.appendChild ( ele );
        return ele;
    }

    public static void fixSize ( final Element element )
    {
        final int len = element.getChildNodes ().getLength ();

        if ( len > 0 )
        {
            element.setAttribute ( "size", "" + len );
        }
        else
        {
            element.getParentNode ().removeChild ( element );
        }
    }

}