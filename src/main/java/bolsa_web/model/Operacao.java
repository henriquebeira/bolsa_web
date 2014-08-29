

package bolsa_web.model;

import bolsa_web.interfaces.ClientInterface;
import java.io.Serializable;
import java.util.Calendar;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean para uma Operação.
 * Beans são classes que apenas possuem variáveis, construtora e metodos get/set;
 * @author henrique
 */
@XmlRootElement
public class Operacao implements Serializable {
    
    private boolean isCompra;
    private String companyID;
    private Integer quantidade;
    private Integer precoUnitarioDesejado;
    private Calendar expireDate;
    
    private ClientInterface clientSign;

    public Operacao() {
        
    }
    
    public Operacao(Operacao operacaoToClone){
        this.isCompra = operacaoToClone.isCompra;
        this.clientSign = operacaoToClone.clientSign;
        this.companyID = operacaoToClone.companyID;
        this.expireDate = operacaoToClone.expireDate;
    }
    
    public Operacao(boolean isCompra, String company, Calendar expireDate, ClientInterface client) {
        this.isCompra = isCompra;
        this.clientSign = client;
        this.companyID = company;
        this.expireDate = expireDate;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Operacao setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        
        return this;
    }

    public Integer getPrecoUnitarioDesejado() {
        return precoUnitarioDesejado;
    }

    public Operacao setPrecoUnitarioDesejado(Integer preçoUnitárioDesejado) {
        this.precoUnitarioDesejado = preçoUnitárioDesejado;
        
        return this;
    }

    public boolean isCompra() {
        return isCompra;
    }

    public ClientInterface getClientSign() {
        return clientSign;
    }

    public String getCompanyID() {
        return companyID;
    }

    public Calendar getExpireDate() {
        return expireDate;
    }
    
    
}
