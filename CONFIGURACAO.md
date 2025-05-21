# Configuração do Ambiente para o Projeto RFID SDK

## Problema de Compatibilidade do Java
Este projeto está enfrentando um problema de incompatibilidade entre o Java 21 (usado pelo VS Code) e o Gradle 7.5 que estava configurado anteriormente.

## Como resolver

### Método 1: Instalar o Android Studio
A maneira mais simples de resolver este problema é usar o Android Studio, que gerenciará automaticamente as versões do Gradle e Java:

1. Baixe e instale o [Android Studio](https://developer.android.com/studio)
2. Abra o projeto selecionando: File > Open > Navegue até a pasta "RfId"
3. O Android Studio configurará automaticamente o ambiente

### Método 2: Configurar o Java manualmente
Você pode configurar o JDK manualmente seguindo estes passos:

1. Baixe e instale o JDK 17 (recomendado) de [AdoptOpenJDK](https://adoptopenjdk.net/) ou [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
2. Configure a variável de ambiente JAVA_HOME para apontar para o diretório de instalação do JDK 17

### Método 3: Atualizar o Gradle manualmente
Se você quiser continuar usando o VS Code:

1. Baixe o [Gradle 8.0](https://gradle.org/releases/)
2. Descompacte em uma pasta conhecida no seu sistema
3. Adicione o diretório bin do Gradle ao PATH do sistema
4. Crie um novo wrapper do Gradle com: `gradle wrapper --gradle-version=8.0`

## Comandos úteis para compilar o projeto após resolução:

```
# Para limpar o projeto
gradle clean

# Para compilar o projeto
gradle build

# Para instalar o APK no dispositivo conectado
gradle installDebug
```

## Lembre-se
- É necessário ter o Android SDK instalado e configurado
- As variáveis de ambiente ANDROID_HOME ou ANDROID_SDK_ROOT devem estar configuradas
