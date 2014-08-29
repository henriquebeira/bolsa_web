package net.controles.service;

import bolsa_web.model.Empresa;
import bolsa_web.model.Operacao;
import bolsa_web.model.Reference;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Classe que encapsula o Bean Empresa para disponibilizar mecanismos de
 * controle para o servidor.
 *
 * @author Henriques
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
        if (compra.isCompra()) { // Se for uma operação de compra
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
        if (!venda.isCompra()) {
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
        if (hasExpired(compra)) {
            System.out.println("Já expirou!");
            return false;
        }

        ArrayList<Operacao> rem = new ArrayList<>();

        for (Operacao venda : vendas) {
            if (!hasExpired(venda)) {
                if (compra.getPrecoUnitarioDesejado() >= venda.getPrecoUnitarioDesejado()) {
                    int quantidade = Math.min(compra.getQuantidade(), venda.getQuantidade());
                    int mediaPreco = (compra.getPrecoUnitarioDesejado() + venda.getPrecoUnitarioDesejado()) / 2;

                    //Notifica o comprador e o vendedor
                    Operacao compraN = new Operacao(compra).setPrecoUnitarioDesejado(mediaPreco).setQuantidade(quantidade);
                    Operacao vendaN = new Operacao(venda).setPrecoUnitarioDesejado(mediaPreco).setQuantidade(quantidade);

                    if (venda.getClientSign().notifyCompletedOperation(vendaN)) {
                        compra.getClientSign().notifyCompletedOperation(compraN);
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
            } else {
                rem.add(venda);
            }
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
        if (hasExpired(venda)) {
            System.out.println("Já expirou!");
            return false;
        }

        ArrayList<Operacao> rem = new ArrayList<>();

        for (Operacao compra : compras) {
            System.out.println("Fila: " + compras.size() + " !| " + compra.getCompanyID() + ", " + compra.getQuantidade() + ", " + compra.getPrecoUnitarioDesejado());
            if (!hasExpired(compra)) {
                if (venda.getPrecoUnitarioDesejado() <= compra.getPrecoUnitarioDesejado()) {
                    int quantidade = Math.min(venda.getQuantidade(), compra.getQuantidade());
                    int mediaPreco = (venda.getPrecoUnitarioDesejado() + compra.getPrecoUnitarioDesejado()) / 2;

                    //Notifica o comprador e o vendedor
                    Operacao compraN = new Operacao(compra).setPrecoUnitarioDesejado(mediaPreco).setQuantidade(quantidade);
                    Operacao vendaN = new Operacao(venda).setPrecoUnitarioDesejado(mediaPreco).setQuantidade(quantidade);

                    compra.getClientSign().notifyCompletedOperation(compraN);
                    venda.getClientSign().notifyCompletedOperation(vendaN);

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
            } else {
                rem.add(compra);
            }
        }

        if (venda.getQuantidade() != 0) {
            System.out.println("Guardou a sobra da venda: " + venda.getQuantidade());
            vendas.add(venda);
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
        return ouvintes.add(ouvinte);
    }

    /**
     * Método responsável por notificar os clientes ouvintes que uma alteração
     * na empresa encapsulada foi realizada.
     */
    private void notifyUpdate() {
        ArrayList<Reference> remove = new ArrayList<>();

        for (Reference ouvinte : ouvintes) {
            try {
                URL url = new URL("http://" + ouvinte.getIp() + ":" + ouvinte.getPort() + "/update/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if (conn.getResponseCode() != 200) {
                    continue;
                }

                // Buffer the result into a string
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();

                conn.disconnect();
            } catch (IOException io) {

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
        Calendar today = Calendar.getInstance();
        Calendar opDate = consult.getExpireDate();

        today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), 01, 0, 0);
        opDate.set(opDate.get(opDate.YEAR), opDate.get(opDate.MONTH), opDate.get(opDate.DAY_OF_MONTH), 23, 0, 0);

        return opDate.before(today);

    }
}
