# Ponto Lembrete - Lembrete de Horário de Almoço

App Android simples e offline para lembrar de bater o ponto ao final do horário de almoço.

## Como Funciona

1. **Abra o app** e pressione o botão grande "BATER PONTO"
2. O app mostra **"Bom almoço!"** e vai para segundo plano
3. Após exatamente **1 hora e 12 minutos**, o app abre automaticamente com alarme sonoro e vibração
4. Você vê o aviso **"BATA O PONTO!"** e pressiona OK
5. O app fecha completamente e só funciona novamente quando você abrir manualmente

## Características

- **100% Offline** - Não precisa de internet
- **Leve** - Usa AlarmManager do Android (sem serviço em segundo plano)
- **Mínimo consumo de memória** - Não mantém nada rodando
- **Alarme confiável** - Funciona mesmo com app fechado/tela bloqueada
- **Som + Vibração** - Garante que você será avisado

## Requisitos

- Android 8.0 (API 26) ou superior
- Android Studio Hedgehog (2023.1.1) ou superior

## Como Compilar e Instalar

### Via Android Studio

1. Abra o Android Studio
2. `File > Open` e selecione a pasta `ponto-lembrete`
3. Aguarde o Gradle sincronizar
4. Conecte seu celular Android via USB (com Depuração USB ativada)
5. Clique em **Run** (▶️) ou `Shift + F10`

### Via Linha de Comando

```bash
# Na pasta do projeto
./gradlew assembleDebug

# O APK será gerado em:
# app/build/outputs/apk/debug/app-debug.apk

# Para instalar diretamente com celular conectado:
./gradlew installDebug
```

### Instalar APK no celular

1. Compile o projeto (gera o APK)
2. Transfira `app-debug.apk` para o celular
3. No celular: `Configurações > Segurança > Fontes desconhecidas` (ative)
4. Abra o APK no celular e instale

## Permissões Necessárias

| Permissão | Motivo |
|-----------|--------|
| `POST_NOTIFICATIONS` | Mostrar a notificação de lembrete |
| `SCHEDULE_EXACT_ALARM` | Agendar alarme exato de 1h12min |
| `VIBRATE` | Vibrar o celular no lembrete |
| `WAKE_LOCK` | Acordar a tela quando o alarme dispara |
| `USE_FULL_SCREEN_INTENT` | Abrir tela de lembrete sobre tela bloqueada |

## Estrutura do Projeto

```
app/src/main/
├── java/com/ponto/lembrete/
│   ├── MainActivity.kt        # Tela principal com botão
│   ├── AlarmReceiver.kt       # Recebe o alarme e dispara notificação
│   └── ReminderActivity.kt    # Tela de aviso (vermelha)
├── res/
│   ├── layout/
│   │   ├── activity_main.xml      # Layout principal
│   │   └── activity_reminder.xml  # Layout do lembrete
│   ├── values/
│   │   ├── colors.xml
│   │   ├── strings.xml
│   │   └── themes.xml
│   └── drawable/
│       └── ic_clock_foreground.xml
└── AndroidManifest.xml
```

## Tecnologias

- **Kotlin** - Linguagem principal
- **AlarmManager** - Timer eficiente em segundo plano
- **Material Components** - UI limpa e moderna
- **ConstraintLayout** - Layouts responsivos
