# TradingJournal

La aplicación **TradingJournal** está diseñada para ayudar a los usuarios a gestionar y analizar sus transacciones de trading. Está desarrollada en Kotlin utilizando Android Studio y el framework nativo de Android SDK. Utiliza una base de datos SQLite para almacenar y gestionar las transacciones localmente.

## Funcionalidades Principales

- **Diario de Trading (Journal)**: Permite a los usuarios agregar, ver y gestionar sus transacciones de trading. Utiliza un RecyclerView con un adaptador personalizado para mostrar las transacciones en una lista, facilitando la navegación y la gestión de las mismas.

- **Detalles de Transacción (Detailed)**: Proporciona una vista detallada de una transacción específica seleccionada desde el diario, mostrando todos los atributos relevantes de la transacción.

- **Gráficos (Graphics)**: Ofrece visualizaciones de datos relacionados con las transacciones de trading, utilizando la biblioteca MPAndroidChart para generar gráficos interactivos y personalizables.

- **Análisis (Analysis)**: Proporciona herramientas para analizar y evaluar las transacciones de trading, ayudando a los usuarios a comprender mejor su desempeño y tomar decisiones informadas.

- **Calculadora (Calculator)**: Permite a los usuarios realizar cálculos relacionados con el trading, como el tamaño de la posición y el riesgo, lo que ayuda a planificar y gestionar sus operaciones de manera más efectiva.

## Tecnologías Utilizadas

- **Kotlin**: Lenguaje de programación oficial de Android respaldado por Google, ofrece una sintaxis concisa y segura que mejora la productividad del desarrollador y reduce la probabilidad de errores.

- **Android Studio**: Entorno de desarrollo integrado (IDE) oficial para el desarrollo de aplicaciones Android, proporciona herramientas para diseñar interfaces de usuario, depurar aplicaciones y gestionar proyectos completos específicos para aplicaciones Android.

- **Android SDK**: Framework nativo de Android que permite aprovechar al máximo las características y funcionalidades específicas de la plataforma Android, garantizando un rendimiento óptimo y una integración perfecta con el ecosistema Android.

- **SQLite**: Base de datos seleccionada por su integración nativa con Android, proporciona una gestión de datos local eficiente y segura sin necesidad de un servidor externo. Destaca por su bajo consumo de recursos, soporte robusto para SQL y facilidad de mantenimiento.

- **MPAndroidChart**: Biblioteca ampliamente utilizada para crear gráficos interactivos en aplicaciones Android, ofrece muchos tipos de gráficos y opciones de personalización para visualizar tendencias y estadísticas de operaciones efectivas.

- **Material Design Components**: Componentes de interfaz de usuario que siguen las pautas de Material Design de Google para Android, ayudan a crear una experiencia de usuario coherente y moderna en la aplicación.

- **RecyclerView**: Componente de Android utilizado para mostrar listas o cuadrículas de elementos de manera eficiente, utilizado para listar transacciones o datos relacionados, proporcionando manejo eficiente de vistas recicladas al desplazarse.

- **Intents y Activity Navigations**: Mecanismos para manejar la navegación entre diferentes pantallas (Activities) y para pasar datos entre ellas.

- **Coroutines de Kotlin**: Utilizadas para manejar operaciones asíncronas como accesos a la base de datos y otras operaciones de I/O sin bloquear el hilo principal de la interfaz de usuario.

- **Data Binding y View Binding**: Técnicas para conectar los componentes de la interfaz de usuario directamente a las fuentes de datos en el código, reduciendo el boilerplate y posibles errores en la manipulación de la UI.

- **LiveData**: Utilizado junto con el patrón de diseño de ViewModel para observar cambios en los datos y actualizar la UI automáticamente de manera eficiente y segura respecto al ciclo de vida de las actividades y fragmentos.

- **JUnit y Espresso**: JUnit es un marco de pruebas unitarias ampliamente utilizado para Android, mientras que Espresso es un marco de pruebas de interfaz de usuario que permite escribir pruebas de integración para asegurar la calidad y fiabilidad de nuestra aplicación.

| ### DashBoard | ### Calculator |
|---|---|
| <img src="" style="height: 20%; width:20%;"/> | <img src="" style="height: 20%; width:20%;"/> |

| ### Journal | ### Add Transaction |
|---|---|
| <img src="" style="height: 20%; width:20%;"/> | <img src="" style="height: 20%; width:20%;"/> |

| ### Update Transaction | ### Graphics |
|---|---|
| <img src="" style="height: 20%; width:20%;"/> | <img src="" style="height: 20%; width:20%;"/> |

| ### Analysis  |
|---|---|
| <img src="" style="height: 20%; width:20%;"/> | <img src="" style="height: 20%; width:20%;"/> |