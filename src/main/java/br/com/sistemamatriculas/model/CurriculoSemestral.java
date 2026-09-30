package br.com.sistemamatriculas.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class CurriculoSemestral {
    private Long id;
    private Date dataGeracao;
    private List<OfertaDisciplina> ofertas = new ArrayList<>();

    public CurriculoSemestral(Long id, Date dataGeracao) {
        this.id = id;
        this.dataGeracao = dataGeracao;
    }

    public void adicionarOferta(OfertaDisciplina ofertaDisciplina) {
        if (ofertaDisciplina == null) {
            throw new IllegalArgumentException("A oferta não pode ser nula.");
        }
        if (!ofertas.contains(ofertaDisciplina)) {
            ofertas.add(ofertaDisciplina);
        }
    }

    public void removerOferta(OfertaDisciplina ofertaDisciplina) {
        ofertas.remove(ofertaDisciplina);
    }

    public List<OfertaDisciplina> getOfertas() {
        return ofertas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(Date dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

}
