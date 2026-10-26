package contratos;

// Tudo o que pode ser vendido numa loja (Item e Equipamento) assina este
// contrato. A Loja e a tela da loja só conversam com "um Negociavel", sem
// precisar saber se é uma poção ou uma armadura.
public interface Negociavel {
	String getNome();
	String getDescricao();
	int getValor();
}
