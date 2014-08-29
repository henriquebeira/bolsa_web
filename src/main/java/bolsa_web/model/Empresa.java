
package bolsa_web.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean para uma Empresa.
 * Beans são classes que apenas possuem variáveis, construtora e metodos get/set;
 * @author henrique
 */
@XmlRootElement
public class Empresa implements Serializable {
    
    private String name;
    private String ID; //Chave para referencia
    private Integer value; //Valor em centavos (100 -> 1 R$)

    public Empresa() {
        
    }
    
    public Empresa(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public Empresa setName(String name) {
        this.name = name;
        
        return this;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public Integer getValue() {
        return value;
    }

    public Empresa setValue(Integer value) {
        this.value = Math.abs(value);
        
        return this;
    }
    
    // Client side
    @Override
    public String toString() {
        return "R$ " + ((value != null ? value : 0) / 100.0);
    }
    
}
