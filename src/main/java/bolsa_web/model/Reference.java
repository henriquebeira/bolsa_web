/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bolsa_web.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author henrique
 */
@XmlRootElement
public class Reference implements Serializable{
    private String ip;
    private Integer port;

    public String getIp() {
        return ip;
    }

    public Reference setIp(String ip) {
        this.ip = ip;
        
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public Reference setPort(Integer port) {
        this.port = port;
        
        return this;
    }

    @Override
    public String toString() {
        return ip+":"+port;
    }
    
    
}
