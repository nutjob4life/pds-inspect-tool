// Copyright 2006-2011, by the California Institute of Technology.
// ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
// Any commercial use must be negotiated with the Office of Technology Transfer
// at the California Institute of Technology.
//
// This software is subject to U. S. export control laws and regulations
// (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
// is subject to U.S. export control laws and regulations, the recipient has
// the responsibility to obtain export licenses or other export authority as
// may be required before exporting such information to foreign countries or
// providing access to foreign nationals.
//
// $Id$

// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2011.05.13 at 11:22:31 AM PDT
//


package gov.nasa.pds.harvest.policy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{}bundles" minOccurs="0"/>
 *         &lt;element ref="{}collections" minOccurs="0"/>
 *         &lt;element ref="{}directories" minOccurs="0"/>
 *         &lt;element ref="{}pds3Directory" minOccurs="0"/>
 *         &lt;element ref="{}validation" minOccurs="0"/>
 *         &lt;element ref="{}candidates"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "policy")
public class Policy {

    protected Bundle bundles;
    protected Collection collections;
    protected Directory directories;
    protected Pds3Directory pds3Directory;
    protected Validation validation;
    @XmlElement(required = true)
    protected Candidate candidates;

    public Policy() {
      directories = new Directory();
      pds3Directory = new Pds3Directory();
      bundles = new Bundle();
      collections = new Collection();
      validation = new Validation();
  }

    /**
     * Gets the value of the bundles property.
     *
     * @return
     *     possible object is
     *     {@link Bundle }
     *
     */
    public Bundle getBundles() {
        return bundles;
    }

    /**
     * Sets the value of the bundles property.
     *
     * @param value
     *     allowed object is
     *     {@link Bundle }
     *
     */
    public void setBundles(Bundle value) {
        this.bundles = value;
    }

    /**
     * Gets the value of the collections property.
     *
     * @return
     *     possible object is
     *     {@link Collection }
     *
     */
    public Collection getCollections() {
        return collections;
    }

    /**
     * Sets the value of the collections property.
     *
     * @param value
     *     allowed object is
     *     {@link Collection }
     *
     */
    public void setCollections(Collection value) {
        this.collections = value;
    }

    /**
     * Gets the value of the directories property.
     *
     * @return
     *     possible object is
     *     {@link Directory }
     *
     */
    public Directory getDirectories() {
        return directories;
    }

    /**
     * Sets the value of the directories property.
     *
     * @param value
     *     allowed object is
     *     {@link Directory }
     *
     */
    public void setDirectories(Directory value) {
        this.directories = value;
    }

    /**
     * Gets the value of the pds3Directory property.
     *
     * @return
     *     possible object is
     *     {@link Pds3Directory }
     *
     */
    public Pds3Directory getPds3Directory() {
        return pds3Directory;
    }

    /**
     * Sets the value of the pds3Directory property.
     *
     * @param value
     *     allowed object is
     *     {@link Pds3Directory }
     *
     */
    public void setPds3Directory(Pds3Directory value) {
        this.pds3Directory = value;
    }

    /**
     * Gets the value of the validation property.
     *
     * @return
     *     possible object is
     *     {@link Validation }
     *
     */
    public Validation getValidation() {
        return validation;
    }

    /**
     * Sets the value of the validation property.
     *
     * @param value
     *     allowed object is
     *     {@link Validation }
     *
     */
    public void setValidation(Validation value) {
        this.validation = value;
    }

    /**
     * Gets the value of the candidates property.
     *
     * @return
     *     possible object is
     *     {@link Candidate }
     *
     */
    public Candidate getCandidates() {
        return candidates;
    }

    /**
     * Sets the value of the candidates property.
     *
     * @param value
     *     allowed object is
     *     {@link Candidate }
     *
     */
    public void setCandidates(Candidate value) {
        this.candidates = value;
    }

}
