
package net.controles.service;

import bolsa_web.model.Empresa;
import bolsa_web.model.Operacao;
import bolsa_web.model.Reference;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Classe que encapsula o Bean Empresa para disponibilizar mecanismos de
 * controle para o servidor.
 *
 * @author Henrique
 */
public class CompanyManager {

    //Bean Empresa encapsulado.
    private final Empresa empresa;

    // Lista de operações de compra disponíveis.
    private final ArrayList<Operacao> compras;
    //Lista de operações de venda disponíveis.
    private final ArrayList<Operacao> vendas;

    // Lista de Clientes ouvindo aos eventos desta empresa.
    private final ArrayList<Reference> ouvintes;

    /**
     * Construtora da classe. Recebe o Bean da empresa que irá encapsular.
     *
     * @param empresa Bean que será encapsulado
     */
    public CompanyManager(Empresa empresa) {
        this.empresa = empresa;

        compras = new ArrayList<>();
        vendas = new ArrayList<>();
        ouvintes = new ArrayList<>();
    }

    /**
     * Permite o acesso à empresa.
     *
     * @return Empresa encapsulada
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * Adiciona uma operação de compra à lista de espera, ou realiza a compra se
     * for possível no momento.
     *
     * @param compra Nova operação
     * @return verdadeiro se a operação tiver sucesso, ou falso do contrário.
     */
    public boolean addCompra(Operacao compra) {
        if (compra.getIsCompra().equalsIgnoreCase("true")) { // Se for uma operação de compra
//            if (!matchBuy(compra)) { // Se a operação de compra não possuir uma operação de venda compatível aguardando
//                compras.add(compra); // Adiciona à fila de espera
//            }

            matchBuy(compra);

            return true;
        }

        return false;
    }

    /**
     * Adiciona uma operação de venda à lista de espera, ou realiza a venda se
     * for possível no momento.
     *
     * @param venda Nova operação
     * @return verdadeiro se a operação tiver sucesso, ou false do contrário.
     */
    public boolean addVenda(Operacao venda) {
        if (!venda.getIsCompra().equalsIgnoreCase("true")) {
//            if (!matchSell(venda)) {
//                vendas.add(venda);
//            }

            matchSell(venda);

            return true;
        }

        return false;
    }

    /**
     * Busca uma operação de venda na lista de espera compatível com a operação
     * de compra fornecida como parâmetro. Se for encontrado um par, a operação
     * é realizada, removendo a venda que estava aguardando e disparando a
     * notificação para os clientes.
     *
     * @param compra Operação que será comparada
     * @return verdadeiro se for encontrado um par e notificado os clientes,
     * falso do contrário.
     */
    private boolean matchBuy(Operacao compra) {
//        if (hasExpired(compra)) {
//            System.out.println("Já expirou!");
//            return false;
//        }

        ArrayList<Operacao> rem = new ArrayList<>();
        
        System.out.println("Size of vendas: " + vendas.size());

        for (Operacao venda : vendas) {
//            if (!hasExpired(venda)) {
                if (compra.getPreco() >= venda.getPreco()) {
                    int quantidade = Math.min(compra.getQuantidade(), venda.getQuantidade());
                    int mediaPreco = (compra.getPreco() + venda.getPreco()) / 2;

                    //Notifica o comprador e o vendedor
                    Operacao compraN = new Operacao(compra).setPreco(mediaPreco).setQuantidade(quantidade);
                    Operacao vendaN = new Operacao(venda).setPreco(mediaPreco).setQuantidade(quantidade);

                    if (notifyCompletion(vendaN.getReferencia(), vendaN, false)) {
                        notifyCompletion(compraN.getReferencia(), compraN, true);
                    } else {
                        continue;
                    }
                    
                    if (venda.getQuantidade() > quantidade) {
                        venda.setQuantidade(venda.getQuantidade() - quantidade);
                        System.out.println("Sobrou na venda: " + venda.getQuantidade());
                        compra.setQuantidade(0);
                        break;
                    } else {
                        if (compra.getQuantidade() > quantidade) {
                            compra.setQuantidade(compra.getQuantidade() - quantidade);
                            System.out.println("Sobrou na compra: " + compra.getQuantidade());
                        } else {
                            System.out.println("Igualou");
                            compra.setQuantidade(0);
                            rem.add(venda);
                            break;
                        }
                    }
                }
//            } else {
//                rem.add(venda);
//            }
        }

        if (compra.getQuantidade() != 0) {
            System.out.println("Guardou a sobra: " + compra.getQuantidade());
            compras.add(compra);
        }

        if (!rem.isEmpty()) {
            for (Operacao remover : rem) {
                vendas.remove(remover);
            }

            return true;
        }

        return false;
    }

    /**
     * Busca uma operação de COMPRA na lista de espera compatível com a operação
     * de VENDA fornecida como parâmetro. Se for encontrado um par, a operação é
     * realizada, removendo a compra que estava aguardando e disparando a
     * notificação para os clientes.
     *
     * @param venda Operação que será comparada
     * @return verdadeiro se for encontrado um par e notificado os clientes,
     * falso do contrário.
     */
    private boolean matchSell(Operacao venda) {
//        if (hasExpired(venda)) {
//            System.out.println("Já expirou!");
//            return false;
//        }

        ArrayList<Operacao> rem = new ArrayList<>();
        
        System.out.println("Compras size: " + compras.size());

        for (Operacao compra : compras) {
            System.out.println("Fila: " + compras.size() + " !| " + compra.getCompanyID() + ", " + compra.getQuantidade() + ", " + compra.getPreco());
//            if (!hasExpired(compra)) {
                if (venda.getPreco() <= compra.getPreco()) {
                    int quantidade = Math.min(venda.getQuantidade(), compra.getQuantidade());
                    int mediaPreco = (venda.getPreco() + compra.getPreco()) / 2;

                    //Notifica o comprador e o vendedor
                    Operacao compraN = new Operacao(compra).setPreco(mediaPreco).setQuantidade(quantidade);
                    Operacao vendaN = new Operacao(venda).setPreco(mediaPreco).setQuantidade(quantidade);


                    if (notifyCompletion(compraN.getReferencia(), compraN, false)) {
                        notifyCompletion(vendaN.getReferencia(), vendaN, true);
                    } else {
                        continue;
                    }
                    
                    if (compra.getQuantidade() > quantidade) {
                        compra.setQuantidade(compra.getQuantidade() - quantidade);
                        System.out.println("Sobrou na compra: " + compra.getQuantidade());
                        venda.setQuantidade(0);
                        break;
                    } else {
                        if (venda.getQuantidade() > quantidade) {
                            venda.setQuantidade(venda.getQuantidade() - quantidade);
                            System.out.println("Sobrou na venda: " + venda.getQuantidade());
                        } else {

                            System.out.println("Igualou");
                            venda.setQuantidade(0);
                            rem.add(compra);
                            break;
                        }
                    }
                }
//            } else {
//                rem.add(compra);
//            }
        }

        if (venda.getQuantidade() != 0) {
            System.out.println("Guardou a sobra da venda: " + venda.getQuantidade());
            System.out.println(vendas.add(venda));
        }

        if (!rem.isEmpty()) {
            for (Operacao remover : rem) {
                compras.remove(remover);
            }

            return true;
        }

        return false;
    }

    /**
     * Adiciona um cliente como ouvinte dos eventos da empresa encapsulada.
     *
     * @param ouvinte cliente que deve receber as notificações
     * @return verdadeiro se a adição for bem sucedida, falso do contrário.
     */
    public boolean addOuvinte(Reference ouvinte) {
        System.out.println("Received Ouvinte: " + ouvinte);
        return ouvintes.add(ouvinte);
    }

    private boolean notifyCompletion(Reference ref, Operacao oper, boolean compra) {
        String params = ";id$" + oper.getCompanyID() + ";compra$" + compra + ";price$" + oper.getPreco() + ";quant$" + oper.getQuantidade();
        String url_ = "http://" + ref.getIp() + ":" + ref.getPort() + "/complete/" + params;
        System.out.println("Has some to: " + url_);
        try {
            URL url = new URL(url_); // 
            System.out.println("Try open");
            URLConnection uc = url.openConnection();
            System.out.println("Connection Open");
            HttpURLConnection conn = (HttpURLConnection) uc;
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            System.out.println("Method Accepted");

            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            System.out.println("Send info to: " + "http://" + ref.getIp() + ":" + ref.getPort() + ""); // /update/

            int resp = conn.getResponseCode();
            System.out.println("Resp" + resp);

            conn.disconnect();
            
            return resp < 300;
            
        } catch (IOException io) {
        }

        return false;
    }

    /**
     * Método responsável por notificar os clientes ouvintes que uma alteração
     * na empresa encapsulada foi realizada.
     */
    private void notifyUpdate() {
        ArrayList<Reference> remove = new ArrayList<>();

//        System.out.println("Notifing");
        String params = ";id$" + empresa.getID() + ";value$" + empresa.getValue() + "";
        
        for (Reference ouvinte : ouvintes) {
            String url_ = "http://" + ouvinte.getIp() + ":" + ouvinte.getPort() + "/update/" + params;
            System.out.println("Has some to: " + url_);
            try {
                URL url = new URL(url_); // 
//                System.out.println("Try open");
                URLConnection uc = url.openConnection();
//                System.out.println("Connection Open");
                HttpURLConnection conn = (HttpURLConnection) uc;
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
//                System.out.println("Method Accepted");

                conn.setUseCaches(false);
                conn.setAllowUserInteraction(false);
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");

//                System.out.println("Send info to: " + "http://" + ouvinte.getIp() + ":" + ouvinte.getPort() + ""); // /update/

                // Create the form content
//                OutputStream out = conn.getOutputStream();
//                System.out.println("Got outputstream");
//                Writer writer = new OutputStreamWriter(out, "UTF-8");
//                System.out.println("Whriter opened");
//                
//                writer.write(params);
//                writer.flush();
                int resp = conn.getResponseCode();
//                System.out.println("Resp" + resp);
//                String[] paramName = new String[]{"id", "value"};
//                String[] paramVal = new String[]{empresa.getID(), empresa.getValue().toString()};
//                for (int i = 0; i < paramName.length; i++) {
//                    writer.write(paramName[i]);
//                    writer.write("=");
//                    writer.write(URLEncoder.encode(paramVal[i], "UTF-8"));
//                    writer.write("&");
//                }
//                writer.close();
//                out.close();

                conn.disconnect();
            } catch (IOException io) {
//                System.out.println("Notify ERROR");
//                io.printStackTrace();
            }
            //try {
//            ouvinte.notifyUpdate(empresa);
            //} catch (RemoteException ex) {
            //    remove.add(ouvinte);
            //}
        }

        for (Reference clientInterface : remove) {
            ouvintes.remove(clientInterface);
        }
    }

    private boolean notifying = false;

    /**
     * Método auxiliar para disparar a notificação aos clientes ouvintes. Não
     * permite notificações concorrentes.
     */
    public void fireNotify() {
        if (!notifying) {
            notifying = true;

            notifyUpdate();

            notifying = false;
        }
    }

    /**
     * Método altera o preço das ações da empresa, e dispara a notificação aos
     * clientes.
     *
     * @param value Novo valor das ações.
     */
    public void setValue(Integer value) {
        empresa.setValue(value);
        fireNotify();
    }

    boolean hasExpired(Operacao consult) {
//        Calendar today = Calendar.getInstance();
//        Calendar opDate = consult.getExpireDate();
//
//        today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), 01, 0, 0);
//        opDate.set(opDate.get(opDate.YEAR), opDate.get(opDate.MONTH), opDate.get(opDate.DAY_OF_MONTH), 23, 0, 0);
//
//        return opDate.before(today);
        return false;
    }
}
