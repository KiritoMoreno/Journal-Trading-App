# TradingJournal

The **TradingJournal** application is designed to help users manage and analyze their trading transactions. It is developed in Kotlin using Android Studio and the native Android SDK framework. It uses a SQLite database to store and manage transactions locally.

## Main Features

- **Trading Journal**: Allows users to add, view, and manage their trading transactions. It uses a RecyclerView with a custom adapter to display transactions in a list, facilitating navigation and management.

- **Transaction Details**: Provides a detailed view of a specific transaction selected from the journal, displaying all relevant attributes of the transaction.

- **Graphics**: Offers visualizations of data related to trading transactions, using the MPAndroidChart library to generate interactive and customizable charts.

- **Analysis**: Provides tools for analyzing and evaluating trading transactions, helping users better understand their performance and make informed decisions.

- **Calculator**: Allows users to perform calculations related to trading, such as position size and risk, aiding in planning and managing operations more effectively.

## Technologies Used

- **Kotlin**: The official programming language for Android backed by Google, offering a concise and safe syntax that enhances developer productivity and reduces the likelihood of errors.

- **Android Studio**: The official integrated development environment (IDE) for Android app development, providing tools for designing user interfaces, debugging applications, and managing complete projects specific to Android apps.

- **Android SDK**: The native Android framework that allows for leveraging the full features and functionalities of the Android platform, ensuring optimal performance and seamless integration with the Android ecosystem.

- **SQLite**: The database selected for its native integration with Android, providing efficient and secure local data management without the need for an external server. SQLite stands out for its low resource consumption, robust support for SQL, and ease of maintenance.

- **MPAndroidChart**: Widely used library for creating interactive charts in Android apps, offering many types of charts and customization options for visualizing trends and effective trading statistics.

- **Material Design Components**: User interface components that follow Google's Material Design guidelines for Android, helping create a coherent and modern user experience in the app.

- **RecyclerView**: An Android component used to efficiently display lists or grids of items, used to list transactions or related data, providing efficient handling of recycled views when scrolling.

- **Intents and Activity Navigations**: Mechanisms for handling navigation between different screens (Activities) and for passing data between them.

- **Kotlin Coroutines**: Used to handle asynchronous operations such as database access and other I/O operations without blocking the main UI thread.

- **Data Binding and View Binding**: Techniques for connecting UI components directly to data sources in the code, reducing boilerplate and potential errors in UI manipulation.

- **LiveData**: Used in conjunction with the ViewModel design pattern to observe changes in data and automatically update the UI efficiently and safely with respect to the lifecycle of activities and fragments.

- **JUnit and Espresso**: JUnit is a widely used unit testing framework for Android, while Espresso is a UI testing framework that allows writing integration tests to ensure the quality and reliability of our application.

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
