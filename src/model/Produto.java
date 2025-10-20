package model;

public class Produto {
    private int id;
    private String nome;
    private String descricao;
    private String codigoBarras;
    private String fabricante;
    private String categoria;
    private double precoCompra;
    private double precoVenda;
    private int estoque;       
    private boolean controlado;

    public Produto() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public double getPrecoCompra() { return precoCompra; }
    public void setPrecoCompra(double precoCompra) { this.precoCompra = precoCompra; }

    public double getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(double precoVenda) { this.precoVenda = precoVenda; }

    public int getEstoque() { return estoque; }
    public void setEstoque(int estoque) { this.estoque = estoque; }

    public boolean isControlado() { return controlado; }
    public void setControlado(boolean controlado) { this.controlado = controlado; }

    public int getQuantidade() {
        return estoque;
    }

    public void setQuantidade(int quantidade) {
        this.estoque = quantidade;
    }

    public void setPreco(double preco) {
        this.precoVenda = preco;
    }

    public double getPreco() {
        return this.precoVenda;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", codigoBarras='" + codigoBarras + '\'' +
                ", fabricante='" + fabricante + '\'' +
                ", categoria='" + categoria + '\'' +
                ", precoCompra=" + precoCompra +
                ", precoVenda=" + precoVenda +
                ", estoque=" + estoque +
                ", controlado=" + controlado +
                '}';
    }
}
