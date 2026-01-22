package br.com.felipebrandao.stock.nfce.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScrapedNfceEmitenteData {
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;
    private String endereco;
    private String bairro;
    private String cep;
    private String municipio;
    private String uf;
    private String telefone;
}
