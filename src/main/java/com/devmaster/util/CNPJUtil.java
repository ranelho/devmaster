package com.devmaster.util;

/**
 * Utilitário para manipulação de CNPJ.
 * Remove e aplica máscaras em CNPJs numéricos e alfanuméricos.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public class CNPJUtil {
    
    private CNPJUtil() {
        throw new IllegalStateException("Utility class");
    }
    
    /**
     * Remove a máscara do CNPJ, mantendo apenas caracteres alfanuméricos.
     * 
     * Exemplos:
     * - "53.429.950/0001-00" -> "53429950000100"
     * - "SR.7VA.0TA/0001-70" -> "SR7VA0TA000170"
     * 
     * @param cnpjComMascara CNPJ com máscara
     * @return CNPJ sem máscara (apenas alfanuméricos)
     */
    public static String removerMascara(String cnpjComMascara) {
        if (cnpjComMascara == null) {
            return null;
        }
        return cnpjComMascara.replaceAll("[^a-zA-Z0-9]", "");
    }
    
    /**
     * Aplica a máscara no CNPJ (formato: XX.XXX.XXX/XXXX-XX).
     * 
     * Exemplos:
     * - "53429950000100" -> "53.429.950/0001-00"
     * - "SR7VA0TA000170" -> "SR.7VA.0TA/0001-70"
     * 
     * @param cnpjSemMascara CNPJ sem máscara
     * @return CNPJ com máscara
     */
    public static String aplicarMascara(String cnpjSemMascara) {
        if (cnpjSemMascara == null || cnpjSemMascara.length() != 14) {
            return cnpjSemMascara;
        }
        
        return String.format("%s.%s.%s/%s-%s",
            cnpjSemMascara.substring(0, 2),
            cnpjSemMascara.substring(2, 5),
            cnpjSemMascara.substring(5, 8),
            cnpjSemMascara.substring(8, 12),
            cnpjSemMascara.substring(12, 14)
        );
    }
}
