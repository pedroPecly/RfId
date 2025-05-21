# Resumo do Projeto SDK RFID

## Visão Geral
Este projeto é um SDK (Software Development Kit) para Android que permite a comunicação com dispositivos RFID via porta serial/USB. O aplicativo serve como demonstração das funcionalidades do SDK, oferecendo uma interface para operações com leitores RFID.

## Principais Funcionalidades

### 1. Gerenciamento de Dispositivos RFID
- **Inicialização de dispositivos RFID**: Conexão com leitores RFID via porta serial
- **Monitoramento de status**: Verificação de estado da conexão, modelo do leitor
- **Monitoramento de bateria**: Leitura do nível (%) e status de carregamento
- **Liberação de recursos**: Fechamento correto de conexões e recursos

### 2. Comunicação Serial
- **Configuração de porta serial**: Definição de parâmetros como baud rate, bits de dados, etc.
- **Abertura/fechamento de conexão**: Gerenciamento do ciclo de vida da conexão
- **Transmissão de dados bidirecionais**: Envio e recebimento de dados
- **Processamento de dados TLV (Tag-Length-Value)**: Formatação estruturada das mensagens

### 3. Modos de Operação
- **Modo Host**: Para comunicação com dispositivos conectados
- **Transferência de dados PC-POS**: Importação e exportação de dados entre computador e terminal
- **Modo de demonstração**: Interface simplificada para testes de funcionalidades

### 4. Utilitários e Processamento
- **Conversão de potência**: Transformação de valores brutos em níveis de bateria
- **Manipulação de dados hexadecimais**: Conversão entre formatos de dados
- **Verificação CRC**: Checagem de integridade de dados
- **Formatação e parsing de dados**: Processamento de respostas dos dispositivos

### 5. Interface de Usuário
- **Telas de demonstração**: Interface para mostrar as funcionalidades do SDK
- **Exibição de logs**: Área para visualização de logs e comunicação
- **Tela de configurações**: Ajuste de parâmetros da porta serial
- **Visualização de status**: Indicadores do estado de conexão e do dispositivo

## Estrutura do Aplicativo

### Activities Principais
- **RfidSdkDemoActivity**: Interface principal de demonstração do SDK RFID
- **SerialPortCommunicationActivity**: Gerencia comunicação via porta serial
- **MainActivity**: Menu principal e navegação
- **SerialPortSettingActivity**: Configuração de parâmetros da porta serial
- **ImportActivity** e **DowloadPCActivity**: Transferência de dados entre dispositivos

### Componentes Técnicos
- **AppReceiver**: Monitora eventos USB para detecção de dispositivos
- **BaseApplication**: Classe de aplicação com inicialização global
- **RfidReaderMange**: Classe central de gerenciamento do leitor RFID
- **SerialPortTool**: Utilitário de comunicação com porta serial
- **TLVTools**: Processamento de mensagens no formato TLV

### Recursos Nativos
- **libhwSerial.so**: Biblioteca nativa para comunicação serial em baixo nível
- **Urv_RfidSerialPortSdk**: SDK principal para a funcionalidade RFID
- **platform_sdk**: SDK da plataforma para funcionalidades específicas do hardware

## Variantes do Aplicativo
- **StandardVer**: Versão básica com funcionalidades de porta serial
- **RfidSdkVer**: Versão completa com todas as funcionalidades RFID integradas

## Requisitos Técnicos
- **Android**: API level 24+ (Android 7.0 ou superior)
- **Permissões**: Acesso USB, armazenamento externo
- **Hardware**: Compatível com leitores RFID específicos
- **Bibliotecas**: Uso de GSON para processamento JSON, DataBinding para UI

## Tecnologias Utilizadas
- **Java**: Linguagem principal de desenvolvimento
- **Android SDK**: Framework base para desenvolvimento do aplicativo (API 24+)
- **Gradle**: Sistema de build e gerenciamento de dependências
- **AndroidX**: Biblioteca de suporte atual do Android
- **Material Design Components**: Para interface do usuário consistente
- **DataBinding**: Framework para vincular dados da UI ao modelo de forma declarativa
- **JNI (Java Native Interface)**: Para integração com bibliotecas nativas em C/C++
- **GSON (Google JSON)**: Biblioteca para processamento de dados em formato JSON
- **TLV (Tag-Length-Value)**: Protocolo para formatação de dados na comunicação serial
- **ViewBinding**: Para acesso typesafe aos componentes de interface
- **SerialPort API**: API personalizada para comunicação com a porta serial
- **USB Host API**: Para conexão direta com dispositivos USB
- **Native Libraries (.so)**: Bibliotecas nativas para comunicação em baixo nível com hardware

## Casos de Uso Comuns
1. Inicializar o leitor RFID e verificar seu modelo
2. Monitorar o nível e status da bateria do dispositivo
3. Configurar a porta serial para comunicação
4. Transferir dados entre PC e dispositivo POS
5. Liberar recursos ao encerrar o aplicativo

## Limitações Atuais
- Compatibilidade específica com determinados modelos de leitores
- Processamento síncrono que pode bloquear a UI em operações longas
- Dependência de bibliotecas nativas que podem limitar a portabilidade

---

*Este resumo pode ser utilizado para consultar o ChatGPT sobre esboços de tela e obter orientações para desenvolvimento adicional do projeto.*
