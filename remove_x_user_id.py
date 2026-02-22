import re
import os

# Arquivos para processar
files = [
    r"c:\workspace\DevMaster IT\devmaster\src\main\java\com\devmaster\application\api\CategoriaAPI.java",
    r"c:\workspace\DevMaster IT\devmaster\src\main\java\com\devmaster\application\api\CupomAPI.java",
    r"c:\workspace\DevMaster IT\devmaster\src\main\java\com\devmaster\application\api\PedidoAPI.java",
    r"c:\workspace\DevMaster IT\devmaster\src\main\java\com\devmaster\application\api\ProdutoAPI.java",
    r"c:\workspace\DevMaster IT\devmaster\src\main\java\com\devmaster\application\api\RestauranteAPI.java",
    r"c:\workspace\DevMaster IT\devmaster\src\main\java\com\devmaster\application\api\TipoPagamentoAPI.java",
    r"c:\workspace\DevMaster IT\devmaster\src\main\java\com\devmaster\application\api\UsuarioRestauranteAPI.java",
]

for file_path in files:
    if not os.path.exists(file_path):
        print(f"❌ Arquivo não encontrado: {file_path}")
        continue
    
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original_content = content
    
    # Remover @RequestHeader("X-User-Id") UUID usuarioId,
    content = re.sub(
        r'@RequestHeader\("X-User-Id"\)\s+UUID\s+usuarioId,\s*\n\s*',
        '',
        content
    )
    
    # Remover import UUID se não for mais usado
    if 'UUID' not in content.replace('import java.util.UUID;', ''):
        content = content.replace('import java.util.UUID;\n', '')
    
    if content != original_content:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"✅ Atualizado: {os.path.basename(file_path)}")
    else:
        print(f"⚠️ Nenhuma mudança: {os.path.basename(file_path)}")

print("\n✅ Processamento concluído!")
