package bolsa_web.resources;

import bolsa_web.model.Empresa;
import bolsa_web.model.Operacao;
import bolsa_web.model.Reference;
import bolsa_web.model.Wrapper;
import com.sun.jersey.spi.resource.Singleton;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import net.controles.service.ServerImplementation;

/*
    Mapeamento dos recursos para a URL bolsa
*/
@Path("/bolsa")
@Singleton
public class BolsaResource {

    ServerImplementation server = new ServerImplementation();
    Integer inte = 0;

// Allows to insert contextual objects into the class, 
// e.g. ServletContext, Request, Response, UriInfo
//    @Context
//    UriInfo uriInfo;
//    @Context
//    Request request;

    // Retorna uma lista de todas as empresas cadastradas no servidor (Browser).
    @GET
    @Path("lista")
    @Produces(MediaType.TEXT_XML)
    public ArrayList<Empresa> getAllCompaniesStatusBrowser() {
        return server.getAllCompaniesStatus();
    }

    // Retorna uma lista de todas as empresas cadastradas no servidor (Aplicação).
    @GET
    @Path("lista")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ArrayList<Empresa> getAllCompaniesStatus() {
        return server.getAllCompaniesStatus();
    }

    // Método para testar a chamada URL
    @GET
    @Path("teste")
    @Produces(MediaType.APPLICATION_XML)
    public Operacao teste() {
        return new Operacao(true, "EA1234").setPreco(++inte).setQuantidade(10).setReferencia(new Reference().setIp("local").setPort(555));
    }

    // Metodo que registra um novo ouvinte a uma determinada empresa.
    @POST
    @Path("escutar")
    @Consumes({MediaType.APPLICATION_XML})
    public String listenToCompany(Wrapper wrap) {
        System.out.println("Called " + wrap.toString());
        if (server.listenToCompany(wrap.getEmpresa(), wrap.getReference())) {
            return "True";
        }
        
        return "False";
    }

    // Registra uma nova operação de compra ou venda.
    @POST
    @Path("registrar")
    @Consumes({MediaType.APPLICATION_XML})
    public String registerOperation(Operacao operacao) {
        System.out.println("Oper: " + operacao);

        if (server.registerOperation(operacao)){
            return "True";
        }
        
        return "False";
    }

    // Retorna uma empresas cadastradas no servidor, através do seu ID (Aplicação).
    @GET
    @Path("{id}")
    public Empresa getCompanyForID(@PathParam("id") String id) {
        return server.getCompanyForID(id);
    }

}
