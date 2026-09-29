package br.com.sistemamatriculas.model;

public class Disciplina {

    private int codigo;
    private String nome;
    private int creditos;
    private int cargaHoraria;

    public Disciplina(
        int codigo,
        String nome,
        int creditos,
        int cargaHoraria
    ) {
        this.codigo = codigo;
        this.nome = nome;
        this.creditos = creditos;
        this.cargaHoraria = cargaHoraria;
    }

    public boolean podeAbrirTurma() {
        // TODO: implementar
        return false;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    @Override
    public String toString() {
        return "Disciplina [codigo=" + codigo + ", nome=" + nome + ", creditos=" + creditos + ", cargaHoraria="
                + cargaHoraria + "]";
    }

    
}