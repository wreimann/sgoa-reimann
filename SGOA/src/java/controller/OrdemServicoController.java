package controller;

import facede.EtapaFacade;
import facede.FuncionarioFacade;
import facede.OrdemServicoFacade;
import filter.LoginFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import model.Funcionario;
import model.OrdemServico;
import model.OrdemServicoEtapa;
import model.OrdemServicoEvento;
import model.OrdemServicoFoto;
import model.TipoEvento;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.primefaces.event.FileUploadEvent;
import util.HibernateFactory;
import util.JsfUtil;

@ManagedBean
@ViewScoped
public final class OrdemServicoController implements Serializable {

    @EJB
    private OrdemServicoFacade ejbFacade;
    private OrdemServicoEtapa current;
    private List<OrdemServicoEtapa> atividades;
    private TipoEvento tipoEvento;
    private List<TipoEvento> tiposEvento;
    private List<OrdemServicoEvento> eventos;
    //private StreamedContent imagem;
    private List<OrdemServicoFoto> fotosAux;
    private String descEvento;
    private Date dataInicioParada;
    private boolean exibirData;
    private OrdemServicoEtapa atividade;
    private OrdemServicoEvento evento;
    private List<OrdemServicoFoto> fotos;

    public Date getDataInicioParada() {
        return dataInicioParada;
    }

    public void setDataInicioParada(Date dataInicioParada) {
        this.dataInicioParada = dataInicioParada;
    }

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

    public boolean isExibirData() {
        return (tipoEvento == TipoEvento.InterrupcaoAtividade
                || tipoEvento == TipoEvento.ReinicioAtividade);
    }

    public String getDescEvento() {
        return descEvento;
    }

    public void setDescEvento(String descEvento) {
        this.descEvento = descEvento;
    }

    public List<TipoEvento> getTiposEvento() {
        return tiposEvento;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
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
    private List<Funcionario> funcionariosAtivos;

    public List<Funcionario> getFuncionariosAtivos() {
        return funcionariosAtivos;
    }
    private List<Etapa> etapasAtivas;

    public List<Etapa> getEtapasAtivas() {
        return etapasAtivas;
    }
    private String placa;

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
    private String veiculo;

    public String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }
    private String cliente;

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
    private Etapa proximaEtapa;

    public Etapa getProximaEtapa() {
        return proximaEtapa;
    }

    public void setProximaEtapa(Etapa proximaEtapa) {
        this.proximaEtapa = proximaEtapa;
    }
    private boolean inicioImediato;

    public boolean isInicioImediato() {
        return inicioImediato;
    }

    public void setInicioImediato(boolean inicioImediato) {
        this.inicioImediato = inicioImediato;
    }
    private Funcionario funcProximaEtapa;

    public Funcionario getFuncProximaEtapa() {
        return funcProximaEtapa;
    }

    public void setFuncProximaEtapa(Funcionario funcProximaEtapa) {
        this.funcProximaEtapa = funcProximaEtapa;
    }

    public OrdemServicoFacade getFacade() {
        return ejbFacade;
    }

    public OrdemServicoEtapa getCurrent() {
        return current;
    }

    public OrdemServicoController() {
        atividades = new ArrayList<OrdemServicoEtapa>();
        limparCampos();
        OrdemServicoEtapa etapaParam = (OrdemServicoEtapa) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("Etapa");
        if (etapaParam != null) {
            try {
                Session sessao = HibernateFactory.currentSession();
                ejbFacade = new OrdemServicoFacade();
                OrdemServico os = ejbFacade.obterPorId(sessao, etapaParam.getOrdemServico().getId());
                Hibernate.initialize(os.getEtapas());
                setCliente(os.getOrcamento().getCliente().toString());
                setVeiculo(os.getOrcamento().getVeiculo().toString());
                setPlaca(os.getOrcamento().getVeiculo().getPlaca());
                current = os.getEtapaAtual();
                atividades = os.getEtapas();
                etapasAtivas = montaListaEtapas();
                etapasAtivas.remove(current.getEtapa());
            } catch (Exception ex) {
                Logger.getLogger(OrdemServicoController.class.getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, "Erro ao carregar da atividade. Tente novamente.");
            } finally {
                HibernateFactory.closeSession();
            }

        }
    }

    public void atualizar(ActionEvent event) {
        if (current.getDataEntrada() != null && current.getDataSaida() != null) {
            if (current.getDataEntrada().after(current.getDataSaida())) {
                JsfUtil.addErrorMessage(null, "A data de saída deve ser maior que a data de entrada na atividade.");
                return;
            }
        }
        if (current.getDataSaida() != null && proximaEtapa == null) {
            JsfUtil.addErrorMessage(null, "Informe a próxima atividade.");
            return;
        }
        if (current.getDataSaida() != null && current.getFuncionario() == null) {
            JsfUtil.addErrorMessage(null, "Informe o funcionário executor da atividade.");
            return;
        }
        if (current.getDataSaida() != null && current.getHorasTrabalhadas() == 0) {
            JsfUtil.addErrorMessage(null, "Informe as horas de trabalho.");
            return;
        }
        if (atividades.size() > 1) {
            if (current.getFuncionario() == null) {
                JsfUtil.addErrorMessage(null, "O campo 'Funcionário Executor' é obrigatório.");
                return;
            }
            //atividades ordenadas por maior data de cadastro
            if (atividades.size() > 2 && atividades.get(1).getDataSaida().after(current.getDataEntrada())) {
                JsfUtil.addErrorMessage(null, "A data de entrada deve ser maior que a data de saída da atividade anterior.");
                return;
            }
        }
        if (inicioImediato && getFuncProximaEtapa() == null) {
            JsfUtil.addErrorMessage(null, "Informe o funcionário da próxima etapa.");
            return;
        }
        try {
            Session sessao = HibernateFactory.currentSession();
            LoginController loginController = new LoginController();
            ejbFacade.atualizarServico(sessao, loginController.getUsuarioSession(), current, proximaEtapa, inicioImediato, funcProximaEtapa);
            JsfUtil.addSuccessMessage("Atividade atualizada com sucesso!");
            setProximaEtapa(null);
            setInicioImediato(false);
            setFuncProximaEtapa(null);
            OrdemServico resultado = ejbFacade.ObterOrdemServicoPorPlaca(sessao, placa);
            if (resultado != null) {
                setCliente(resultado.getOrcamento().getCliente().toString());
                setVeiculo(resultado.getOrcamento().getVeiculo().toString());
                current = resultado.getEtapaAtual();
                atividades = resultado.getEtapas();
                etapasAtivas = montaListaEtapas();
                etapasAtivas.remove(current.getEtapa());
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao salvar o registro.");
        } finally {
            HibernateFactory.closeSession();
        }
    }

    public void limparCampos() {
        current = new OrdemServicoEtapa();
        setPlaca(null);
        setCliente(null);
        setVeiculo(null);
        setTipoEvento(null);
        setDescEvento(null);
        setDataInicioParada(null);
        setAtividade(null);
        funcionariosAtivos = montaListaFuncionarios();
        montaListaTiposEvento();
        fotosAux = new ArrayList<OrdemServicoFoto>();

    }

    public void prepararEvento() {
        try {
            Session sessao = HibernateFactory.currentSession();
            OrdemServicoEtapa etapa = ejbFacade.obterEtapa(sessao, atividade.getId());
            Hibernate.initialize(etapa.getEventos());
            eventos = etapa.getEventos();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao carregar os eventos da atividade.");
        } finally {
            HibernateFactory.closeSession();
        }
    }

    public void changeTipoEvento() {
        montaListaTiposEvento();
        if (current.getSituacao() == 'P') {
            tiposEvento.add(TipoEvento.ReinicioAtividade);
        } else if (current.getSituacao() == 'E') {
            tiposEvento.add(TipoEvento.InterrupcaoAtividade);
        }
        setDataInicioParada(null);
        setDescEvento(null);
        setTipoEvento(TipoEvento.Informacao);
    }

    public void adicionarEvento(ActionEvent event) {
        if (getTipoEvento() == TipoEvento.InterrupcaoAtividade
                && current.getDataEntrada().after(getDataInicioParada())) {
            JsfUtil.addErrorMessage(null, "A data da interrupção da atividade deve ser maior que a data de início da atividade.");
            return;
        }
        if (getTipoEvento() == TipoEvento.ReinicioAtividade) {
            Date dataInterrupcao = null;
            for (OrdemServicoEvento item : current.getEventos()) {
                if (item.getTipoEvento() == TipoEvento.InterrupcaoAtividade) {
                    dataInterrupcao = item.getDataInicioParada();
                    break;
                }
            }
            if (dataInterrupcao != null && dataInterrupcao.after(getDataInicioParada())) {
                JsfUtil.addErrorMessage(null, "A data de reinício da atividade deve ser maior que a data de interrupção.");
                return;
            }
        }
        try {
            Session sessao = HibernateFactory.currentSession();
            LoginController loginController = new LoginController();
            ejbFacade.incluirEvento(sessao, current, loginController.getUsuarioSession(), getTipoEvento(), getDescEvento(), getDataInicioParada(), fotosAux);
            JsfUtil.addSuccessMessage("Evento incluído com sucesso!");
            fotosAux = new ArrayList<OrdemServicoFoto>();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao salvar o evento.");
        } finally {
            HibernateFactory.closeSession();
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            // imagem = new DefaultStreamedContent(event.getFile().getInputstream());
            OrdemServicoFoto foto = new OrdemServicoFoto();
            foto.setNomeArquivo(event.getFile().getFileName());
            foto.setImagem(event.getFile().getContents());
            fotosAux.add(foto);
        } catch (Exception ex) {
            Logger.getLogger(OrdemServicoController.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ex, "Erro ao fazer upload da foto. Tente novamente");
        }
    }

    public List<Etapa> montaListaEtapas() {
        List<Etapa> resultado = new ArrayList<Etapa>();
        try {
            Session sessao = HibernateFactory.currentSession();
            EtapaFacade etapaFacade = new EtapaFacade();
            resultado = etapaFacade.selecionarTodosAtivos(sessao);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de etapas. ");
        } finally {
            HibernateFactory.closeSession();
        }
        return resultado;
    }

    public void changePlaca() {
        if (placa == null || placa.isEmpty()) {
            return;
        }
        try {
            OrdemServico resultado;
            Session sessao = HibernateFactory.currentSession();
            resultado = ejbFacade.ObterOrdemServicoPorPlaca(sessao, placa);
            if (resultado != null) {
                setCliente(resultado.getOrcamento().getCliente().toString());
                setVeiculo(resultado.getOrcamento().getVeiculo().toString());
                current = resultado.getEtapaAtual();
                atividades = resultado.getEtapas();
                etapasAtivas = montaListaEtapas();
                etapasAtivas.remove(current.getEtapa());
            } else {
                JsfUtil.addErrorMessage(null, "Nenhuma ordem de serviço localiza para a placa informada.");
            }
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Nenhuma ordem de serviço localiza para a placa informada.");
        } finally {
            HibernateFactory.closeSession();
        }
    }

    public List<Funcionario> montaListaFuncionarios() {
        List<Funcionario> resultado = new ArrayList<Funcionario>();
        try {
            Session sessao = HibernateFactory.currentSession();
            FuncionarioFacade funcionarioFacade = new FuncionarioFacade();
            resultado = funcionarioFacade.selecionarTodosAtivos(sessao);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Erro ao carregar a lista de funcionarios. ");
        } finally {
            HibernateFactory.closeSession();
        }
        return resultado;
    }

    public void montaListaTiposEvento() {
        tiposEvento = new ArrayList<TipoEvento>();
        tiposEvento.add(TipoEvento.Informacao);
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
