package loja;

import java.util.List;

import equipamentos.CatalogoDeEquipamentos;
import itens.CatalogoDeItens;

public class CatalogoDeLojas {
	public static Loja lojaDaMasmorra() {
		/* CatalogoDeItens.pocaoDeVida() (com parênteses) executaria agora e entregaria uma poção pronta.
		CatalogoDeItens::pocaoDeVida não executa nada: entrega o próprio método, que a Oferta chama a cada compra. */
		return new Loja("Loja da Masmorra",
			List.of(new Oferta<>(CatalogoDeItens::pocaoDeVida),
					new Oferta<>(CatalogoDeItens::pocaoDeMana)),
			List.of(new Oferta<>(CatalogoDeEquipamentos::armaduraDeCouro)));
	}
}
