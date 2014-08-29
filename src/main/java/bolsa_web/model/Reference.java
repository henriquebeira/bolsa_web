
package bolsa_web.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean para uma Referência. Beans são classes que apenas possuem variáveis, construtora e metodos get/set;
 *
 * @author henrique
 */

@XmlRootElement //Mapeamento da classe para ser um elemento XML
public class Reference implements Serializable{
    private String ip;
    private Integer port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return ip+":"+port;
    }
    
    
}
