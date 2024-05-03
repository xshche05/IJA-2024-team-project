module ija.project.robot {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.logging;
    requires java.desktop;

    opens ija.project.robot to javafx.fxml;
    exports ija.project.robot;
    exports ija.project.robot.gui;
    exports ija.project.robot.gui.interfaces;
    exports ija.project.robot.gui.controllers;
}