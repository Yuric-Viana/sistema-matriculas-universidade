package br.com.sistemamatriculas.model;

import java.util.ArrayList;
import java.util.List;

public class SemestreLetivo {
    private int ano;
    private int periodo;
    private PeriodoMatricula periodoMatricula;
    private List<OfertaDisciplina> ofertas = new ArrayList<>();

    public SemestreLetivo(int ano, int periodo, PeriodoMatricula periodoMatricula) {
        this.ano = ano;
        this.periodo = periodo;
        this.periodoMatricula = periodoMatricula;
    }

    public String descricao() {
        return periodo + "/" + ano;
    }

    public List<OfertaDisciplina> getOfertas() {
        return ofertas;
    }

    public void adicionarOferta(OfertaDisciplina oferta) {
        if (oferta != null && !ofertas.contains(oferta)) {
            ofertas.add(oferta);
        }
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public PeriodoMatricula getPeriodoMatricula() {
        return periodoMatricula;
    }

    public void setPeriodoMatricula(PeriodoMatricula periodoMatricula) {
        this.periodoMatricula = periodoMatricula;
    }

    @Override
    public String toString() {
        return "SemestreLetivo [" + descricao() + "]";
    }
}
