@echo off
echo Running JavaFX App...
set PATH_TO_FX=javafx-sdk-25.0.1\lib
javac --module-path "%PATH_TO_FX%" --add-modules javafx.controls,javafx.fxml -d out src\F.java
java --enable-native-access=javafx.graphics --module-path "%PATH_TO_FX%" --add-modules javafx.controls,javafx.fxml -cp out F
pause
