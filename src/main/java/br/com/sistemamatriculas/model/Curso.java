package br.com.sistemamatriculas.model;

import java.util.ArrayList;
import java.util.List;

public class Curso {

    private int codigo;
    private String nome;
    private int numeroCreditos;
    private List<Disciplina> disciplinas = new ArrayList<>();

    public Curso(int codigo, String nome, int numeroCreditos) {
        this.codigo = codigo;
        this.nome = nome;
        this.numeroCreditos = numeroCreditos;
    }

    public void adicionarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina não pode ser nula");
        }

        if (!disciplinas.contains(disciplina)) {
            disciplinas.add(disciplina);
        }
    }

    public void removerDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina não pode ser nula");
        }

        if (disciplinas.contains(disciplina)) {
            disciplinas.remove(disciplina);
        }
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

    public int getNumeroCreditos() {
        return numeroCreditos;
    }

    public void setNumeroCreditos(int numeroCreditos) {
        this.numeroCreditos = numeroCreditos;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    @Override
    public String toString() {
        return "Curso [codigo=" + codigo + ", nome=" + nome + ", numeroCreditos=" + numeroCreditos + ", disciplinas="
                + disciplinas + "]";
    }

    
}