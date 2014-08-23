/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bolsa_web.interfaces;

import bolsa_web.model.Empresa;
import bolsa_web.model.Operacao;

/**
 * Interface para o cliente JRMI
 * @author Henriques
 */
public interface ClientInterface{

    //Recebe notificação de operação realizada.
    public boolean notifyCompletedOperation(Operacao operacao);
    
    //Recebe notificação de atualização dos valores da ação de uma empresa.
    public void notifyUpdate(Empresa empresaAtualizado);
}
