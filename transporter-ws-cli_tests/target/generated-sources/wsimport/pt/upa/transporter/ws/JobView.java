
package pt.upa.transporter.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for jobView complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="jobView">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="companyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="jobIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="jobOrigin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="jobDestination" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="jobPrice" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="jobState" type="{http://ws.transporter.upa.pt/}jobStateView" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "jobView", propOrder = {
    "companyName",
    "jobIdentifier",
    "jobOrigin",
    "jobDestination",
    "jobPrice",
    "jobState"
})
public class JobView {

    protected String companyName;
    protected String jobIdentifier;
    protected String jobOrigin;
    protected String jobDestination;
    protected int jobPrice;
    @XmlSchemaType(name = "string")
    protected JobStateView jobState;

    /**
     * Gets the value of the companyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the value of the companyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyName(String value) {
        this.companyName = value;
    }

    /**
     * Gets the value of the jobIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobIdentifier() {
        return jobIdentifier;
    }

    /**
     * Sets the value of the jobIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobIdentifier(String value) {
        this.jobIdentifier = value;
    }

    /**
     * Gets the value of the jobOrigin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobOrigin() {
        return jobOrigin;
    }

    /**
     * Sets the value of the jobOrigin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobOrigin(String value) {
        this.jobOrigin = value;
    }

    /**
     * Gets the value of the jobDestination property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobDestination() {
        return jobDestination;
    }

    /**
     * Sets the value of the jobDestination property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobDestination(String value) {
        this.jobDestination = value;
    }

    /**
     * Gets the value of the jobPrice property.
     * 
     */
    public int getJobPrice() {
        return jobPrice;
    }

    /**
     * Sets the value of the jobPrice property.
     * 
     */
    public void setJobPrice(int value) {
        this.jobPrice = value;
    }

    /**
     * Gets the value of the jobState property.
     * 
     * @return
     *     possible object is
     *     {@link JobStateView }
     *     
     */
    public JobStateView getJobState() {
        return jobState;
    }

    /**
     * Sets the value of the jobState property.
     * 
     * @param value
     *     allowed object is
     *     {@link JobStateView }
     *     
     */
    public void setJobState(JobStateView value) {
        this.jobState = value;
    }

}
