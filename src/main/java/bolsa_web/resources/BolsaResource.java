package bolsa_web.resources;

import bolsa_web.model.Empresa;
import bolsa_web.model.Operacao;
import bolsa_web.model.Wrapper;
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

// Will map the resource to the URL todos
@Path("/bolsa")
public class BolsaResource {

    ServerImplementation server = new ServerImplementation();

    // Allows to insert contextual objects into the class, 
    // e.g. ServletContext, Request, Response, UriInfo
    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    //Retorna uma lista de todas as empresas cadastradas no servidor.
    @GET
    @Path("lista")
    @Produces(MediaType.TEXT_XML)
    public ArrayList<Empresa> getAllCompaniesStatusBrowser() {
        return server.getAllCompaniesStatus();
    }

    // Return the list of todos for applications
    @GET
    @Path("lista")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ArrayList<Empresa> getAllCompaniesStatus() {
        return server.getAllCompaniesStatus();
    }

    // retuns the number of todos
    // use http://localhost:8084/de.vogella.jersey.todo/rest/todos/count
    // to get the total number of records
    @GET
    @Path("teste")
    @Produces(MediaType.TEXT_PLAIN)
    public String teste() {
        return "Teste!";
    }

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

    @POST
    @Path("registrar")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public boolean registerOperation(Operacao operacao) {

        return server.registerOperation(operacao);
    }

    // Defines that the next path parameter after todos is
    // treated as a parameter and passed to the TodoResources
    // Allows to type http://localhost:8084/bolsa_web/rest/bolsa/PB568A
    // 1 will be treaded as parameter todo and passed to TodoResource
    @GET
    @Path("{id}")
    public Empresa getCompanyForID(@PathParam("id") String id) {
        return server.getCompanyForID(id);
    }

}
