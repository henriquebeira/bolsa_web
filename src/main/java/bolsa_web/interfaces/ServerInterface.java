/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bolsa_web.interfaces;

import bolsa_web.model.Empresa;
import bolsa_web.model.Operacao;
import java.util.ArrayList;

/**
 * Interface para o servidor JRMI.
 * @author Henriques
 */
public interface ServerInterface{
    public final static String SERVER_NAME = "Bolsa de Valores";

    //Metodo que registra um novo cliente que tem interesse em ações de uma determinada empresa.
    public boolean listenToCompany(Empresa empresa, ClientInterface client);

    //Registra uma nova operação de compra ou venda.
    public boolean registerOperation(Operacao operacao);

    //Retorna uma lista de todas as empresas cadastradas no servidor.
    public ArrayList<Empresa> getAllCompaniesStatus();
    
    //Retorna os dados de uma empresa a partir de seu ID.
    public Empresa getCompanyForID(String ID);
    
}
