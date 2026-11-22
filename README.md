# TV Play

Aplicativo Android inicial para TV Play.

## Características iniciais
- Interface escura com detalhes azuis.
- Abas de TV ao vivo e filmes.
- Reprodutor integrado com AndroidX Media3 / ExoPlayer.
- Campo para testar uma URL HTTPS de transmissão autorizada.
- Qualidade adaptativa quando a fonte HLS fornece uma playlist master adaptativa.

## Importante
O projeto não inclui listas de canais, filmes ou URLs de terceiros. Para publicar ou distribuir conteúdo, use apenas transmissões e vídeos para os quais você tenha direitos ou autorização. A reprodução depende de uma URL compatível e válida; nem todo link de página da web é um link de vídeo.

## Gerar APK pelo GitHub
1. Envie todos os arquivos e pastas deste projeto para a raiz do repositório GitHub.
2. Abra a aba **Actions** do repositório.
3. Se necessário, habilite GitHub Actions.
4. Abra **Build TV Play APK** e toque em **Run workflow**, ou faça um commit na branch `main` ou `master`.
5. Quando terminar, abra a execução concluída e baixe o artefato **TV-Play-debug**.
6. Extraia o ZIP do artefato e instale `app-debug.apk` no Android.

## Desenvolvimento
- Android Gradle Plugin 8.13.2
- Gradle 8.13
- Kotlin 2.2.21
- AndroidX Media3 1.11.1
- JDK 17
