package bolsa_web.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean para uma Wrapper. Classe utilizada para a construção de dois objetos.
 * Beans são classes que apenas possuem variáveis, construtora e metodos get/set;
 * @author henrique
 */

@XmlRootElement //Mapeamento da classe para ser um elemento XML
public class Wrapper {

    private Empresa empresa;
    private Reference reference;

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return empresa + " - " + reference;
    }
}
