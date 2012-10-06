//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.09.19 at 05:23:51 PM PDT 
//


package gov.nasa.arc.pds.xml.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 *  The Data Set PDS3 class is used to capture the data set information from the PDS3 Data Set Catalog. 
 * 
 * <p>Java class for Data_Set_PDS3 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Data_Set_PDS3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="data_set_id" type="{http://pds.nasa.gov/pds4/pds/v03}ASCII_Short_String_Collapsed"/>
 *         &lt;element name="data_set_name" type="{http://pds.nasa.gov/pds4/pds/v03}ASCII_Short_String_Collapsed"/>
 *         &lt;element name="data_set_release_date" type="{http://pds.nasa.gov/pds4/pds/v03}ASCII_Short_String_Collapsed"/>
 *         &lt;element name="start_date_time" type="{http://pds.nasa.gov/pds4/pds/v03}start_date_time"/>
 *         &lt;element name="stop_date_time" type="{http://pds.nasa.gov/pds4/pds/v03}stop_date_time"/>
 *         &lt;element name="producer_full_name" type="{http://pds.nasa.gov/pds4/pds/v03}ASCII_Short_String_Collapsed"/>
 *         &lt;element name="citation_text" type="{http://pds.nasa.gov/pds4/pds/v03}ASCII_Text_Preserved"/>
 *         &lt;element name="data_set_terse_desc" type="{http://pds.nasa.gov/pds4/pds/v03}ASCII_Text_Preserved"/>
 *         &lt;element name="abstract_desc" type="{http://pds.nasa.gov/pds4/pds/v03}ASCII_Text_Preserved"/>
 *         &lt;element name="data_set_desc" type="{http://pds.nasa.gov/pds4/pds/v03}ASCII_Text_Preserved"/>
 *         &lt;element name="confidence_level_note" type="{http://pds.nasa.gov/pds4/pds/v03}ASCII_Text_Preserved"/>
 *         &lt;element name="archive_status" type="{http://pds.nasa.gov/pds4/pds/v03}ASCII_Short_String_Collapsed"/>
 *         &lt;element name="NSSDC" type="{http://pds.nasa.gov/pds4/pds/v03}NSSDC" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Data_Set_PDS3", propOrder = {
    "dataSetId",
    "dataSetName",
    "dataSetReleaseDate",
    "startDateTime",
    "stopDateTime",
    "producerFullName",
    "citationText",
    "dataSetTerseDesc",
    "abstractDesc",
    "dataSetDesc",
    "confidenceLevelNote",
    "archiveStatus",
    "nssdcs"
})
public class DataSetPDS3 {

    @XmlElement(name = "data_set_id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String dataSetId;
    @XmlElement(name = "data_set_name", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String dataSetName;
    @XmlElement(name = "data_set_release_date", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String dataSetReleaseDate;
    @XmlElement(name = "start_date_time", required = true, nillable = true)
    protected StartDateTime startDateTime;
    @XmlElement(name = "stop_date_time", required = true, nillable = true)
    protected StopDateTime stopDateTime;
    @XmlElement(name = "producer_full_name", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String producerFullName;
    @XmlElement(name = "citation_text", required = true)
    protected String citationText;
    @XmlElement(name = "data_set_terse_desc", required = true)
    protected String dataSetTerseDesc;
    @XmlElement(name = "abstract_desc", required = true)
    protected String abstractDesc;
    @XmlElement(name = "data_set_desc", required = true)
    protected String dataSetDesc;
    @XmlElement(name = "confidence_level_note", required = true)
    protected String confidenceLevelNote;
    @XmlElement(name = "archive_status", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String archiveStatus;
    @XmlElement(name = "NSSDC")
    protected List<NSSDC> nssdcs;

    /**
     * Gets the value of the dataSetId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataSetId() {
        return dataSetId;
    }

    /**
     * Sets the value of the dataSetId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataSetId(String value) {
        this.dataSetId = value;
    }

    /**
     * Gets the value of the dataSetName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataSetName() {
        return dataSetName;
    }

    /**
     * Sets the value of the dataSetName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataSetName(String value) {
        this.dataSetName = value;
    }

    /**
     * Gets the value of the dataSetReleaseDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataSetReleaseDate() {
        return dataSetReleaseDate;
    }

    /**
     * Sets the value of the dataSetReleaseDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataSetReleaseDate(String value) {
        this.dataSetReleaseDate = value;
    }

    /**
     * Gets the value of the startDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link StartDateTime }
     *     
     */
    public StartDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Sets the value of the startDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link StartDateTime }
     *     
     */
    public void setStartDateTime(StartDateTime value) {
        this.startDateTime = value;
    }

    /**
     * Gets the value of the stopDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link StopDateTime }
     *     
     */
    public StopDateTime getStopDateTime() {
        return stopDateTime;
    }

    /**
     * Sets the value of the stopDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link StopDateTime }
     *     
     */
    public void setStopDateTime(StopDateTime value) {
        this.stopDateTime = value;
    }

    /**
     * Gets the value of the producerFullName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProducerFullName() {
        return producerFullName;
    }

    /**
     * Sets the value of the producerFullName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProducerFullName(String value) {
        this.producerFullName = value;
    }

    /**
     * Gets the value of the citationText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCitationText() {
        return citationText;
    }

    /**
     * Sets the value of the citationText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCitationText(String value) {
        this.citationText = value;
    }

    /**
     * Gets the value of the dataSetTerseDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataSetTerseDesc() {
        return dataSetTerseDesc;
    }

    /**
     * Sets the value of the dataSetTerseDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataSetTerseDesc(String value) {
        this.dataSetTerseDesc = value;
    }

    /**
     * Gets the value of the abstractDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbstractDesc() {
        return abstractDesc;
    }

    /**
     * Sets the value of the abstractDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbstractDesc(String value) {
        this.abstractDesc = value;
    }

    /**
     * Gets the value of the dataSetDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataSetDesc() {
        return dataSetDesc;
    }

    /**
     * Sets the value of the dataSetDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataSetDesc(String value) {
        this.dataSetDesc = value;
    }

    /**
     * Gets the value of the confidenceLevelNote property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfidenceLevelNote() {
        return confidenceLevelNote;
    }

    /**
     * Sets the value of the confidenceLevelNote property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfidenceLevelNote(String value) {
        this.confidenceLevelNote = value;
    }

    /**
     * Gets the value of the archiveStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArchiveStatus() {
        return archiveStatus;
    }

    /**
     * Sets the value of the archiveStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArchiveStatus(String value) {
        this.archiveStatus = value;
    }

    /**
     * Gets the value of the nssdcs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nssdcs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNSSDCS().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NSSDC }
     * 
     * 
     */
    public List<NSSDC> getNSSDCS() {
        if (nssdcs == null) {
            nssdcs = new ArrayList<NSSDC>();
        }
        return this.nssdcs;
    }

}