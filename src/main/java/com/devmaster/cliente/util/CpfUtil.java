package com.devmaster.cliente.util;

public class CpfUtil {
    
    private CpfUtil() {
        throw new IllegalStateException("Utility class");
    }
    
    public static String formatCpf(String cpf) {
        cpf = cpf.replaceAll("\\D", "");
        if (cpf.length() == 11) {
            cpf = cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        }
        return cpf;
    }
}
