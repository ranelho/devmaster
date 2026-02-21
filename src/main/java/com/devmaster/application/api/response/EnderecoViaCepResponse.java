package com.devmaster.application.api.response;

public record EnderecoViaCepResponse(
    String cep,
    String logradouro,
    String complemento,
    String bairro,
    String localidade, // cidade
    String uf, // estado
    Double latitude,
    Double longitude
) {
    public static EnderecoViaCepResponse fromViaCep(ViaCepData viaCepData, Double lat, Double lng) {
        return new EnderecoViaCepResponse(
            viaCepData.cep(),
            viaCepData.logradouro(),
            viaCepData.complemento(),
            viaCepData.bairro(),
            viaCepData.localidade(),
            viaCepData.uf(),
            lat,
            lng
        );
    }
    
    public record ViaCepData(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf
    ) {}
}
