/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bolsa_web.resources;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;

/**
 * Classe de inicialização da aplicação.
 * @author Henrique
 */
public class MyApplication extends Application{

    @Override
    public Set<Object> getSingletons() {
        Set ret = new HashSet<>();
        ret.add(BolsaResource.class);
        return ret;
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set ret = new HashSet<>();
        ret.add(BolsaResource.class);
        return ret;
    }
    
}
