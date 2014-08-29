/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bolsa_web.interfaces;

import bolsa_web.model.Empresa;
import bolsa_web.model.Operacao;
import bolsa_web.model.Reference;
import java.util.ArrayList;

/**
 * Interface para o servidor JRMI.
 * @author Henriques
 */
public interface ServerInterface{

    // - Metodo que registra um novo cliente que tem interesse em ações de uma determinada empresa.
    public boolean listenToCompany(Empresa empresa, Reference client);

    // - Registra uma nova operação de compra ou venda.
    public boolean registerOperation(Operacao operacao);

    // - Retorna uma lista de todas as empresas cadastradas no servidor.
    public ArrayList<Empresa> getAllCompaniesStatus();
    
    // - Retorna os dados de uma empresa a partir de seu ID.
    public Empresa getCompanyForID(String ID);
    
}
