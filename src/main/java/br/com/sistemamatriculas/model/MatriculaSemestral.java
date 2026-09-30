package br.com.sistemamatriculas.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import br.com.sistemamatriculas.enums.TipoOpcao;

public class MatriculaSemestral {
    private Long id;
    private Date dataMatricula;
    private Aluno aluno;
    private SemestreLetivo semestreLetivo;
    private List<ItemMatricula> itens = new ArrayList<>();
    private SistemaCobranca sistemaCobranca;

    public MatriculaSemestral(Long id, Date dataMatricula) {
        this.id = id;
        this.dataMatricula = dataMatricula;
    }

    public void adicionarItem(OfertaDisciplina oferta, TipoOpcao tipo) {

        if (oferta == null || tipo == null) {
            throw new IllegalArgumentException(
                    "Oferta e tipo de opção são obrigatórios.");
        }

        if (!oferta.verificarDisponibilidadeVagas()) {
            throw new IllegalStateException(
                    "Não há vagas disponíveis nessa oferta.");
        }

        ItemMatricula item = new ItemMatricula(
                null,
                tipo,
                LocalDate.now(),
                this,
                oferta
        );

        itens.add(item);
        oferta.adicionarItemMatricula(item);
    }

    public void cancelarItem(ItemMatricula item) {

        if (item == null) {
            throw new IllegalArgumentException(
                    "O item não pode ser nulo.");
        }

        if (!itens.contains(item)) {
            throw new IllegalArgumentException(
                    "Este item não pertence a esta matrícula.");
        }

        itens.remove(item);
        item.getOfertaDisciplina().removerItemMatricula(item);
    }

    public void notificarCobranca() {
        if (sistemaCobranca != null) {
            sistemaCobranca.receberNotificacao(this);
        }
    }

    public Long getId() {
        return id;
    }

    public Date getDataMatricula() {
        return dataMatricula;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public SemestreLetivo getSemestreLetivo() {
        return semestreLetivo;
    }

    public List<ItemMatricula> getItens() {
        return itens;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDataMatricula(Date dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public void setSemestreLetivo(SemestreLetivo semestreLetivo) {
        this.semestreLetivo = semestreLetivo;
    }

    public void setItens(List<ItemMatricula> itens) {
        this.itens = itens;
    }

    public void setSistemaCobranca(SistemaCobranca sistemaCobranca) {
        this.sistemaCobranca = sistemaCobranca;
    }

    @Override
    public String toString() {
        return "MatriculaSemestral [id=" + id
                + ", aluno=" + (aluno != null ? aluno.getNome() : null)
                + ", semestreLetivo=" + semestreLetivo
                + ", itens=" + itens.size()
                + "]";
    }
}