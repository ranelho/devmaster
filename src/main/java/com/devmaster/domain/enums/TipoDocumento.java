package com.devmaster.domain.enums;

/**
 * Enum que representa os tipos de documentos que podem ser anexados ao cadastro do entregador.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public enum TipoDocumento {
    
    CNH("Carteira Nacional de Habilitação"),
    RG("Registro Geral"),
    CPF("Cadastro de Pessoa Física"),
    COMPROVANTE_RESIDENCIA("Comprovante de Residência"),
    FOTO_VEICULO("Foto do Veículo"),
    CRLV("Certificado de Registro e Licenciamento de Veículo");
    
    private final String descricao;
    
    TipoDocumento(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
