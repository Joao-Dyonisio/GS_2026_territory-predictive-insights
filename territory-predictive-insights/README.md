# Territory Predictive Insights

Global Solution 2026 – Programação Orientada a Objetos (POO)

Sistema em Java com menu no console, que lê sensores simulados, faz previsões simples e emite alertas explicados.

**Recorte escolhido: Clima local (chuva intensa e risco de alagamento).**
A região "Ipiranga" é fictícia. Nenhum dado de pessoa real é usado: o território é descrito só por região, população (um número) e equipamento (estação de coleta).

## Público beneficiado e decisão apoiada

- **Quem sofre o problema:** moradores das áreas baixas perto de um córrego, que podem ser atingidos por alagamentos, e as pessoas que cuidam desses pontos (associação de moradores, agentes comunitários, Defesa Civil local).
- **Decisão que o sistema apoia:** "devemos avisar a comunidade e preparar a saída das áreas de risco agora, ou ainda dá para esperar?". O sistema mostra a previsão e o porquê do alerta, mas quem decide é a pessoa.

## Como compilar e rodar

Precisa do JDK 11 ou superior. Dentro da pasta do projeto:

```bash
javac -encoding UTF-8 -d out *.java
java -cp out SistemaMonitoramento
```

Se os acentos saírem trocados no Windows: `chcp 65001` antes de rodar, ou `java -Dstdout.encoding=UTF-8 -cp out SistemaMonitoramento`.
Decimais seguem o idioma do computador: em português, digite com vírgula (ex.: `45,5`).
No IntelliJ: abra a pasta como projeto e execute a classe `SistemaMonitoramento`.

**Código de acesso de demonstração** (para alterar limiares): `chuva2026`.

### Roteiro rápido para testar

1. Opção `2` logo ao iniciar: o sistema **se cala** (só há 4 leituras e ele exige 5 ou 6).
2. Opção `4` → `8` leituras → tempo `2` (tempestade): as leituras sobem e os alertas aparecem sozinhos.
3. Opção `3` → `4` com código errado: alteração negada. Opção `3` → `2` → `-5`: rejeitado, valor anterior mantido.
4. Opção `6`: simulação de 24 horas (bônus).

## Estrutura do projeto

| Arquivo | O que é |
|---|---|
| `ComponenteTerritorial.java` | classe **abstrata** (id, nome, região, status; `ativar()` e `desativar()` concretos; `verificarStatus()` abstrato) |
| `EstacaoDeColeta.java` | classe concreta que herda de `ComponenteTerritorial` e tem vários sensores |
| `Sensor.java` | **interface** (ler valor, verificar funcionamento, tipo, limite de alerta) |
| `SensorChuva.java`, `SensorNivelRio.java`, `SensorUmidade.java` | implementam `Sensor` |
| `DadosTerritorio.java` | **encapsulamento**: atributos privados, setters com validação, limiares protegidos por código |
| `EstrategiaPrevisao.java` | classe **abstrata** das previsões |
| `PrevisaoMediaMovel.java`, `PrevisaoComparacao.java` | herdam de `EstrategiaPrevisao`, cada uma com atributo próprio e `prever()` sobrescrito |
| `ClimaSimulado.java` | guarda se há tempestade; os três sensores consultam para variar juntos |
| `SistemaMonitoramento.java` | classe principal: menu, previsões e alertas |

### Onde cada conceito aparece

- **Classe abstrata:** `ComponenteTerritorial` e `EstrategiaPrevisao`.
- **Interface:** `Sensor`, implementada por 3 classes sem relação de herança entre si.
- **Encapsulamento:** `DadosTerritorio` (o código de acesso não tem getter; valor inválido gera mensagem e o anterior é mantido).
- **Herança e `super`:** as duas previsões chamam `super(...)` no construtor e `super.prever(...)` dentro do método sobrescrito.
- **Polimorfismo:** a lista `ArrayList<EstrategiaPrevisao>` roda as duas estratégias com a mesma chamada.
- **Associação:** a estação tem sensores; os sensores têm `DadosTerritorio` e `ClimaSimulado`.

## Previsões e alertas

- Cada sensor guarda as últimas 20 leituras em um `ArrayList`. Os valores são simulados com `Math.random()`.
- **Média móvel:** média das últimas 5 leituras.
- **Comparação de períodos:** compara a média das últimas 3 leituras com a das 3 anteriores e soma essa variação por leitura à última leitura.
- O nível do alerta depende de quanto a previsão representa do limite do sensor:

| Previsão | Nível |
|---|---|
| menos de 80% do limite | NORMAL |
| 80% ou mais | ATENÇÃO |
| 100% ou mais | ALERTA |
| 120% ou mais | CRÍTICO |

Cada alerta diz qual sensor, qual valor foi previsto, por qual estratégia e quanto passa (ou chega perto) do limite.
Limites padrão (fictícios): chuva 40 mm/h, nível do rio 3,0 m, umidade do solo 80%.

## Decisões da equipe

- Escolhemos o clima local porque os três sensores formam uma história fácil de explicar: chove, o solo satura e o rio sobe.
- Os limiares ficam em `DadosTerritorio`, não nos sensores, para a comunidade poder ajustá-los (com o código) sem mexer nos sensores.
- Usamos `ClimaSimulado` para os três sensores subirem juntos na tempestade, em vez de três `Math.random()` soltos.
- Os níveis usam limite (80%, 100%, 120%): assim há um limite só por sensor para configurar.
- Escolhemos como bônus a simulação de 24 horas.

## Reflexão crítica

**Quando o sistema deve se calar?** Quando não tem leituras suficientes. Com menos de 5 leituras, a média tem poucos números e um pico isolado vira "tendência". Por isso a média móvel exige 5 leituras e a comparação exige 6 (dois períodos de 3). Abaixo disso o sistema diz por que não prevê, em vez de mostrar um número frágil com cara de certeza. Alerta falso também custa: se a comunidade é avisada à toa várias vezes, passa a ignorar o aviso quando ele for verdadeiro.

**O que os dados não representam?** Uma estação por região diz pouco sobre cada rua: um ponto mais baixo pode alagar enquanto o sensor marca valores normais. Nossos valores são simulados e não incluem relevo, bueiros entupidos, lixo no córrego nem água que vem de outro bairro. Os limites (40 mm/h, 3 m, 80%) são fictícios. As previsões são só média e comparação, e não usam a previsão oficial do tempo.

**Por que o alerta apoia a decisão em vez de decidir?** Porque quem decide sabe coisas que o sistema não sabe: se há idosos na área, se a rota de saída está livre, se a chuva já parou, se o sensor está com defeito. Evacuar tem custo e risco, então a decisão deve ficar com pessoas que respondem por ela.

## Uso de IA

- **Onde usamos:** usamos o Claude apenas na parte de dados do projeto: a classe `DadosTerritorio` (atributos privados, validações e código de acesso) e a simulação das leituras dos sensores (`ClimaSimulado` e o cálculo dos valores com `Math.random()`).
- **O que a equipe fez:** o restante do projeto foi desenvolvido pela própria equipe em conjunto, incluindo `ComponenteTerritorial`, `EstacaoDeColeta`, a interface `Sensor` e suas implementações, as estratégias de previsão, o menu e os alertas em `SistemaMonitoramento`, além deste README.

## Divisão de tarefas

| Integrante | Parte | Arquivos |
|---|---|---|
| Agatha e João | Sensores | `Sensor`, `SensorChuva`, `SensorNivelRio`, `SensorUmidade`, `ClimaSimulado` |
| Agatha| Previsões | `EstrategiaPrevisao`, `PrevisaoMediaMovel`, `PrevisaoComparacao` |
| João | Dados do território e menu | `ComponenteTerritorial`, `EstacaoDeColeta`, `DadosTerritorio`, `SistemaMonitoramento` |
