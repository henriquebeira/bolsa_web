
package net.controles.service;

import bolsa_web.interfaces.ServerInterface;
import bolsa_web.model.Empresa;
import bolsa_web.model.Operacao;
import bolsa_web.model.Reference;
import java.util.ArrayList;
import net.controles.simulador.SimuladorBolsa;

/**
 * Classe que implementa a interface do servidor.
 * 
 * @author Henriques
 */
public class ServerImplementation implements ServerInterface {
    private SimuladorBolsa simulador;
    private Controller controle;

    /**
     * Construtora da classe.
     * 
     */
    public ServerImplementation() {
        controle = new Controller();
        simulador = new SimuladorBolsa(controle);
        simulador.start();
        
        System.out.println("!!!!!!!!!!!!!");
        addEmpresa(new Empresa("PB568A").setName("PanBas").setValue(322));
        addEmpresa(new Empresa("EA851A").setName("EACon").setValue(422));
        addEmpresa(new Empresa("TB854A").setName("Tabuu").setValue(123));
        addEmpresa(new Empresa("YU852A").setName("YoUi").setValue(273));
        addEmpresa(new Empresa("RT652A").setName("RaTimbum").setValue(89));
        addEmpresa(new Empresa("XM965A").setName("XMLinha").setValue(186));
        
    }
    
    /**
     * Registra uma nova empresa à lista de empresas do servidor.
     * 
     * @param empresa nova empresa.
     */
    public void addEmpresa(Empresa empresa){
        controle.addCompany(empresa);
    }

    /**
     * Metodo que registra um novo ouvinte a uma determinada empresa.
     * 
     * @param empresa empresa que reberá o novo ouvinte
     * @param client cliente ouvinte
     * @return verdadeiro se a operação for bem sucedida, falso do contrário.
     */
    @Override
    public boolean listenToCompany(Empresa empresa, Reference client){
        System.out.println("! "+empresa.getName()+" !");
        return controle.getManagerFor(empresa.getID()).addOuvinte(client);
    }

    /**
     * Registra uma nova operação de compra ou venda.
     * 
     * @param operacao nova operação
     * @return verdadeiro se for bem sucedido, falso do contrário.
     */
    @Override
    public boolean registerOperation(Operacao operacao){
        CompanyManager manager = controle.getManagerFor(operacao.getCompanyID());
        if(manager == null){
            return false;
        }
        
        if(operacao.getIsCompra().equalsIgnoreCase("true")){
            return manager.addCompra(operacao);
        }else{
            return manager.addVenda(operacao);
        }
    }

    /**
     * Retorna uma lista de todas as empresas cadastradas no servidor.
     * 
     * @return Lista de empresas disponíveis no servidor
     */
    @Override
    public ArrayList<Empresa> getAllCompaniesStatus(){
        return controle.getListaEmpresas();
    }
    
//    public static void main(String ... args){
//        
//        ServerImplementation server = new ServerImplementation();
//            
//        server.addEmpresa(new Empresa("PB568A").setName("PanBas").setValue(322));
//        server.addEmpresa(new Empresa("EA851A").setName("EACon").setValue(422));
//        server.addEmpresa(new Empresa("TB854A").setName("Tabuu").setValue(123));
//        server.addEmpresa(new Empresa("YU852A").setName("YoUi").setValue(273));
//        server.addEmpresa(new Empresa("RT652A").setName("RaTimbum").setValue(89));
//        server.addEmpresa(new Empresa("XM965A").setName("XMLinha").setValue(186));
//            
//    }

    @Override
    public Empresa getCompanyForID(String ID){
        CompanyManager manager =  controle.getManagerFor(ID);
        
        if(manager != null){
            return manager.getEmpresa();
        }else{
            return null;
        }
    }

    public Controller getControle() {
        return controle;
    }
    
}