module com.example.projetgrotte {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;


    opens com.projet.grotte to javafx.fxml;
    exports com.projet.grotte;
}