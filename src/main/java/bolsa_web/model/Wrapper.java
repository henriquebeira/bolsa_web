package bolsa_web.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
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
