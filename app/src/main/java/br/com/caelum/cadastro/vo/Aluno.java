package br.com.caelum.cadastro.vo;

import java.io.Serializable;

/**
 * Created by android5497 on 02/09/15.
 */
public class Aluno implements Serializable {

    private Long id;
    private String nome;
    private String endereco;
    private String telefone;
    private String site;
    private Double nota;
    private String caminhoFoto;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    /*    @Override
        public String toString() {
            return id.toString().concat("\n").
                    concat(nome).concat("\n").
                    concat(endereco).concat("\n").
                    concat(telefone).concat("\n").
                    concat(site).concat("\n").
                    concat(nota.toString());
        }*/
    @Override
    public String toString() {
        return id.toString().concat(" ").
                concat(nome);
    }
}
