package controller;

import facede.OrdemServicoFacade;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import model.Etapa;
import model.OrdemServico;
import model.OrdemServicoEtapa;
import model.OrdemServicoEvento;
import model.OrdemServicoFoto;
import model.PessoaFisica;
import model.PessoaJuridica;
import model.TipoEvento;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import util.HibernateFactory;
import util.JsfUtil;

@ManagedBean
@ViewScoped
public class AcompanharServicoController implements Serializable {

    public AcompanharServicoController() {
        limparCampos();
        placa = JsfUtil.getRequestParameter("placa");
        doc = JsfUtil.getRequestParameter("doc");
        if (placa != null && doc != null && !placa.isEmpty() && !doc.isEmpty()) {
            try {
                Session sessao = HibernateFactory.currentSession();
                ejbFacade = new OrdemServicoFacade();
                OrdemServico os = ejbFacade.ObterOrdemServicoPorPlaca(sessao, placa);
                if (os != null) {
                    if (os.getOrcamento().getCliente().getPessoa() instanceof PessoaFisica) {
                        if (!((PessoaFisica) os.getOrcamento().getCliente().getPessoa()).getCpf().equals(doc)) {
                            JsfUtil.addErrorMessage("Veículo não localizado.");
                            return;
                        }
                    } else {
                        if (!((PessoaJuridica) os.getOrcamento().getCliente().getPessoa()).getCnpj().equals(doc)) {
                            JsfUtil.addErrorMessage("Veículo não localizado.");
                            return;
                        }
                    }
                    atividades.clear();
                    for (OrdemServicoEtapa ativ : os.getEtapas()) {
                        if (ativ.getEtapa().getVisivelWebSite()) {
                            atividades.add(ativ);
                        }
                    }
                    if (atividades.size() > 0) {
                        etapa = atividades.get(0).getEtapa();
                        switch (atividades.get(0).getSituacao()) {
                            case 'A':
                                situacao = "Cancelado";
                                break;
                            case 'E':
                                situacao = "Em Execução";
                                break;
                            case 'F':
                                situacao = "Fila de Espera";
                                break;
                            case 'P':
                                situacao = "Parado";
                                break;
                            case 'C':
                                situacao = "Concluído";
                                break;
                            default:
                                situacao = "";
                        }
                    
                    }
                    setCliente(os.getOrcamento().getCliente().toString());
                    setVeiculo(os.getOrcamento().getVeiculo().toString());
                    setPlaca(os.getOrcamento().getVeiculo().getPlaca());
                    setOrcamento(os.getOrcamento().toString());
                    setDataAprovacao(new SimpleDateFormat("dd/MM/yyyy hh:mm").format(os.getDataAprovacao()));
                } else {
                    JsfUtil.addErrorMessage("Veículo não localizado.");
                }
            } catch (Exception ex) {
                JsfUtil.addErrorMessage(ex, "Erro ao carregar o andamento do serviço. Tente novamente.");
            } finally {
                HibernateFactory.closeSession();
            }
        } else {
            JsfUtil.addErrorMessage("Parâmetros inválidos.");
        }
    }

    public final void limparCampos() {
        atividades = new ArrayList<OrdemServicoEtapa>();
        setPlaca(null);
        setDoc(null);
        setCliente(null);
        setVeiculo(null);
        setOrcamento(null);
        setDataAprovacao(null);
        setSituacao(null);
        setEtapa(null);
        setAtividade(null);
        setInformacao(null);
    }

    public String logout() {
        return "/index?faces-redirect=true";
    }
    @EJB
    private OrdemServicoFacade ejbFacade;
    //propriedades
    private String placa;
    private String veiculo;
    private String doc;
    private String cliente;
    private Etapa etapa;
    private String situacao;
    private String orcamento;
    private String dataAprovacao;
    private String informacao;
    private List<OrdemServicoEtapa> atividades;
    private List<OrdemServicoEvento> eventos;
    private OrdemServicoEtapa atividade;
    private OrdemServicoEvento evento;
    private List<OrdemServicoFoto> fotos;

    // <editor-fold defaultstate="collapsed" desc="gets e sets">
    public OrdemServicoEvento getEvento() {
        return evento;
    }

    public void setEvento(OrdemServicoEvento evento) {
        this.evento = evento;
    }

    public OrdemServicoEtapa getAtividade() {
        return atividade;
    }

    public void setAtividade(OrdemServicoEtapa atividade) {
        this.atividade = atividade;
    }

    public List<OrdemServicoEvento> getEventos() {
        return eventos;
    }

    public List<OrdemServicoFoto> getFotos() {
        return fotos;
    }

    public void setFotos(List<OrdemServicoFoto> fotos) {
        this.fotos = fotos;
    }

    public List<OrdemServicoEtapa> getAtividades() {
        return atividades;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getInformacao() {
        return informacao;
    }

    public void setInformacao(String informacao) {
        this.informacao = informacao;
    }

    public String getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(String orcamento) {
        this.orcamento = orcamento;
    }

    public String getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(String dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public OrdemServicoFacade getFacade() {
        return ejbFacade;
    }

    public Etapa getEtapa() {
        return etapa;
    }

    public void setEtapa(Etapa etapa) {
        this.etapa = etapa;
    }
    // </editor-fold>

    public void prepararEvento() {
        try {
            Session sessao = HibernateFactory.currentSession();
            OrdemServicoEtapa etapaAux = ejbFacade.obterEtapa(sessao, atividade.getId());
            Hibernate.initialize(etapaAux.getEventos());
            eventos = etapaAux.getEventos();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao carregar os eventos da atividade.");
        } finally {
            HibernateFactory.closeSession();
        }
    }

    public void adicionarEvento() {
        try {
            Session sessao = HibernateFactory.currentSession();
            ejbFacade.incluirEvento(sessao, atividade, null, TipoEvento.ContatoCliente, informacao, null, null, false); 
            JsfUtil.addSuccessMessage("Mensagem enviada com sucesso. Obrigado pela sua colaboração.");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao enviar a mensagem. Tente mais tarde novamente.");
        } finally {
            HibernateFactory.closeSession();
        }
    }

    private void criaArquivo(byte[] bytes, String arquivo) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(arquivo);
            fos.write(bytes);
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OrdemServicoController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OrdemServicoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void changeFoto() {
        try {
            Session sessao = HibernateFactory.currentSession();
            evento = ejbFacade.obterEvento(sessao, evento.getId());
            Hibernate.initialize(evento.getFotos());
            setFotos(evento.getFotos());
            for (OrdemServicoFoto f : getFotos()) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
                String nomeArquivo = f.getId().toString() + ".jpg";
                String arquivo = scontext.getRealPath("/fotos/" + nomeArquivo);
                criaArquivo(f.getImagem(), arquivo);
            }
        } catch (Exception ex) {
            Logger.getLogger(OrdemServicoController.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ex, "Erro ao carregar as fotos. Tente novamente");
        } finally {
            HibernateFactory.closeSession();
        }
    }
}
