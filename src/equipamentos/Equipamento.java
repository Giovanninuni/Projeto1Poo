package equipamentos;

import entidades.Heroi;

public class Equipamento {
    private String nome;
    private String descricao;
    private TipoEquipamento tipo;
    private int bonusAtaque;
    private int bonusDefesa;
    private Class<? extends Heroi> restricaoDeClasse;

    public Equipamento(String nome, String descricao, TipoEquipamento tipo, int bonusAtaque, int bonusDefesa) {
        this(nome, descricao, tipo, bonusAtaque, bonusDefesa, null);
    }

    // restricaoDeClasse == null => qualquer heroi pode usar (ex: acessorios genericos).
    // Passar Mago.class, Guerreiro.class etc. restringe o equipamento aquela subclasse.
    public Equipamento(String nome, String descricao, TipoEquipamento tipo, int bonusAtaque, int bonusDefesa, Class<? extends Heroi> restricaoDeClasse) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.bonusAtaque = bonusAtaque;
        this.bonusDefesa = bonusDefesa;
        this.restricaoDeClasse = restricaoDeClasse;
    }

    public boolean podeSerUsadoPor(Heroi heroi) {
        return restricaoDeClasse == null || restricaoDeClasse.isInstance(heroi);
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public TipoEquipamento getTipo() {
        return tipo;
    }

    public int getBonusAtaque() {
        return bonusAtaque;
    }

    public int getBonusDefesa() {
        return bonusDefesa;
    }
}
