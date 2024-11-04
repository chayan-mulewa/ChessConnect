module com.chess.application.chessapplication
{
    requires javafx.controls;
    requires javafx.graphics;
    requires com.google.gson;
    requires java.sql;
    requires java.desktop;
    requires mysql;
    requires java.prefs;

    exports com.chess.common.ENUM;

    opens com.chess.framework.common;
    opens com.chess.framework.client;
    opens com.chess.framework.server;

    opens com.chess.common;

    opens com.chess.application.client.components;
    opens com.chess.application.client.main;
    opens com.chess.application.client.example;
    opens com.chess.application.client.ui;


}