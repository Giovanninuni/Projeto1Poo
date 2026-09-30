package equipamentos;

import contratos.Negociavel;
import entidades.Heroi;

public class Equipamento implements Negociavel {
    private String nome;
    private String descricao;
    private TipoEquipamento tipo;
    private int bonusPoder;
    private int bonusDefesa;
    private int valor;
    private Class<? extends Heroi> restricaoDeClasse;
    
    /* Construtor package-private/sem modificador, apenas o pacote acessa, pois unico lugar que é acessado é
    no CatalogoDeEquipamentos */
    Equipamento(String nome, String descricao, TipoEquipamento tipo, int bonusPoder, int bonusDefesa, int valor) {
        this(nome, descricao, tipo, bonusPoder, bonusDefesa, valor, null);
    }

    // restricaoDeClasse == null => qualquer heroi pode usar (ex: acessorios genericos).
    // Passar Mago.class, Guerreiro.class etc. restringe o equipamento aquela subclasse.
    Equipamento(String nome, String descricao, TipoEquipamento tipo, int bonusPoder, int bonusDefesa, int valor, Class<? extends Heroi> restricaoDeClasse) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.bonusPoder = bonusPoder;
        this.bonusDefesa = bonusDefesa;
        this.valor = valor;
        this.restricaoDeClasse = restricaoDeClasse;
    }

    public boolean podeSerUsadoPor(Heroi heroi) {
        return restricaoDeClasse == null || restricaoDeClasse.isInstance(heroi);
    }
    
    @Override
    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public TipoEquipamento getTipo() {
        return tipo;
    }

    public int getBonusPoder() {
        return bonusPoder;
    }

    public int getBonusDefesa() {
        return bonusDefesa;
    }
    
    @Override
    public int getValor() {
    	return valor;
    }
}
