
package pt.upa.transporter.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for decideJobResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="decideJobResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://ws.transporter.upa.pt/}jobView" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "decideJobResponse", propOrder = {
    "_return"
})
public class DecideJobResponse {

    @XmlElement(name = "return")
    protected JobView _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link JobView }
     *     
     */
    public JobView getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link JobView }
     *     
     */
    public void setReturn(JobView value) {
        this._return = value;
    }

}
