package negocio.entidades.pagamento;

public class CartaoCredito implements MetodoPagamento {
    private String numero;
    private String nomeTitular;

    public CartaoCredito(String numero, String nomeTitular) {
        this.numero = numero;
        this.nomeTitular = nomeTitular;
    }

    @Override
    public String pagar(double valor) {
        return "Processando pagamento de R$" + valor + " na fatura do Cartão de Crédito...";
    }
}
