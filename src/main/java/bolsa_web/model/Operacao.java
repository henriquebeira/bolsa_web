package bolsa_web.model;

import java.io.Serializable;
import java.util.Calendar;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean para uma Operação.
 * Beans são classes que apenas possuem variáveis, construtora e metodos get/set;
 * @author henrique
 */
@XmlRootElement //Mapeamento da classe para ser um elemento XML 
public class Operacao implements Serializable {

    private String isCompra;
    private String companyID;
    private Integer quantidade;
    private Integer preco;
    private Reference referencia;
//    private Calendar expireDate;

//    private ClientInterface clientSign;
    public Operacao() {

    }

    public Operacao(Operacao operacaoToClone) {
        this.isCompra = operacaoToClone.isCompra;
        this.referencia = operacaoToClone.referencia;
        this.companyID = operacaoToClone.companyID;
//        this.expireDate = operacaoToClone.expireDate;
    }

    public Operacao(boolean isCompra, String company) {
        this.isCompra = isCompra + "";
//        this.clientSign = client;
        this.companyID = company;
//        this.expireDate = expireDate;
    }

    public String getIsCompra() {
        return isCompra;
    }

    public Operacao setIsCompra(String isCompra) {
        this.isCompra = isCompra;
        return this;
    }

    public String getCompanyID() {
        return companyID;
    }

    public Operacao setCompanyID(String companyID) {
        this.companyID = companyID;
        return this;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Operacao setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        return this;
    }

    public Integer getPreco() {
        return preco;
    }

    public Operacao setPreco(Integer preco) {
        this.preco = preco;
        return this;
    }

    public Reference getReferencia() {
        return referencia;
    }

    public Operacao setReferencia(Reference referencia) {
        this.referencia = referencia;

        return this;
    }

//    public Calendar getExpireDate() {
//        return expireDate;
//    }
    @Override
    public String toString() {
        return companyID + ":" + isCompra + " - " + quantidade + " - $" + preco;
    }

}
